package com.zzzealous.seekme.Fragment;

import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.SDKInitializer;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.Marker;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.MyLocationConfiguration;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.map.PolylineOptions;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.model.LatLngBounds;
import com.baidu.mapapi.search.core.RouteLine;
import com.baidu.mapapi.search.core.SearchResult;
import com.baidu.mapapi.search.route.BikingRouteResult;
import com.baidu.mapapi.search.route.DrivingRouteResult;
import com.baidu.mapapi.search.route.IndoorRouteResult;
import com.baidu.mapapi.search.route.MassTransitRouteResult;
import com.baidu.mapapi.search.route.OnGetRoutePlanResultListener;
import com.baidu.mapapi.search.route.PlanNode;
import com.baidu.mapapi.search.route.RoutePlanSearch;
import com.baidu.mapapi.search.route.TransitRouteResult;
import com.baidu.mapapi.search.route.WalkingRoutePlanOption;
import com.baidu.mapapi.search.route.WalkingRouteResult;
import com.baidu.mapapi.utils.DistanceUtil;
import com.zzzealous.seekme.Activity.MainActivity;
import com.zzzealous.seekme.Bean.GlobalValues;
import com.zzzealous.seekme.Bean.Pair;
import com.zzzealous.seekme.Bean.RealLoc;
import com.zzzealous.seekme.Bean.TraceRecord;
import com.zzzealous.seekme.Database.DatabaseManager;
import com.zzzealous.seekme.Preference.SeekmePreference;
import com.zzzealous.seekme.R;
import com.zzzealous.seekme.UI.AlertDialog;
import com.zzzealous.seekme.UI.DetailDialog;
import com.zzzealous.seekme.Utils.FileUtil;
import com.zzzealous.seekme.Utils.MyOrientationListener;
import com.zzzealous.seekme.Utils.TimeUtil;
import com.zzzealous.seekme.Utils.overlayutil.OverlayManager;
import com.zzzealous.seekme.Utils.overlayutil.WalkingRouteOverlay;

import java.util.ArrayList;
import java.util.List;

/**
 * 主Fragment
 * <p>
 * 定位 进入开始巡检
 * <p>
 * 发送共享位置（runfragment可以共享）  获取位置
 * <p>
 * 点击  弹出dialog 打电话
 */

public class MainFragment extends Fragment implements View.OnClickListener, OnGetRoutePlanResultListener {

    /**
     * 显示相关
     */

    protected Activity mActivity;
    private MapView mMapView = null;
    private BaiduMap mBaiduMap;
    private View view = null;
    private boolean tokenBoolean = true;

    /**
     * 巡检控制
     */

    private LinearLayout traceControlTab;

    private Button start_btn;
    private Button btnTracePause;
    private Button btnTraceStop;
    private Button btnTraceContinue;

    private TextView tvStartTime;
    private TextView tvRealTime;

    /**
     * 求救控制  seeker
     */
    private LinearLayout seekerControlTab;

    private Button btnCallHelper;
    private Button btnSeekFinish;

    private TextView tvHelperName;
    private TextView tvHelperPhone;
    private TextView tvHelperJobnum;

    /**
     * 救援控制  helper
     */

    private LinearLayout helperControlTab;

    private Button btnCallSeeker;
    private Button btnHelpFinish;

    private TextView tvSeekerName;
    private TextView tvSeekerPhone;
    private TextView tvSeekerJobnum;

    /**
     * 时间相关
     */

    private int time = 0; //跑步用时，单位秒


    private String userid;
    private String starttime;
    private String realtime;
    private String today;
    private String snapShotPath = null;
    private String snapShotName = null;

    /**
     * 定位相关
     */
    private LocationClient mLocationClient = null;
    private MyLocationListener mLocationListener;
    private boolean isFirstLoc = true;
    private double mLatitude;
    private double mLongitude;
    private MyOrientationListener myOrientationListener;
    private float mCurrentX;

    private List<LatLng> pointList = new ArrayList<>(); //途径坐标点集合
    LatLng last;
    //缓存
    private View rootView;

    /**
     * 地图状态更新
     */
    private MapStatusUpdate update = null;

    /**
     * 实时点覆盖物
     */
    private OverlayOptions realtimeOptions = null;


    /**
     * 开始点覆盖物
     */
    private OverlayOptions startOptions = null;

    /**
     * 结束点覆盖物
     */
    private OverlayOptions endOptions = null;

    /**
     * 路径规划 搜索
     */
    private RoutePlanSearch mSearch = null;
    private RouteLine route = null;  //路线
    private OverlayManager routeOverlay = null;  //该类提供一个能够显示和管理多个Overlay的基类


    /**
     * 路径覆盖物
     */
    private PolylineOptions polyLine = null;
    /**
     * 周围人覆盖物 以及详细信息
     */
    private BitmapDescriptor mMarker;
    private boolean hasMarker = false;

    Thread t = new Thread(new Runnable() {
        @Override
        public void run() {
            if (SeekmePreference.getBoolean("tokennotvalue")) {
                if (GlobalValues.isStart != -1) {
                    DatabaseManager.getInstance().updateMyLoc(
                            SeekmePreference.getString("userId"),
                            mLatitude, mLongitude, mActivity);
                    GlobalValues.SosLat = mLatitude;
                    GlobalValues.SosLng = mLongitude;

                }
                if (GlobalValues.isStart == 0) {
                    //显示周围工人
                    getWorkerLoc();

                }
                handler.postDelayed(this, 10 * 1000);
            }
        }
    });


    public static final int START_TO_RECORD_TIME = 0x00;
    //    public static final int SAVE_LAT_LNG = 0x01;
    public static final int ADD_OVERLAY_OPTIONS = 0x02;
    public static final int REST_AND_REFRESH = 0x09;

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case START_TO_RECORD_TIME:
                    if (GlobalValues.isStart == 1) {
                        time++;
                        tvRealTime.setText(TimeUtil.formatTime(time));
                    }
                    handler.sendEmptyMessageDelayed(START_TO_RECORD_TIME, 1000);

                    break;
                case REST_AND_REFRESH:

                    ((MainActivity) getActivity()).refreshTitle("SeekMe");
                    start_btn.setVisibility(View.VISIBLE);
                    traceControlTab.setVisibility(View.GONE);


                    mBaiduMap.clear();
                    time = 0;
                    pointList = new ArrayList<>();
                    tvRealTime.setText(TimeUtil.formatTime(time));

                    centerToMe(mLatitude, mLongitude);
                    mBaiduMap.setMapStatus(MapStatusUpdateFactory.zoomTo(19.0f));

                    break;
                case ADD_OVERLAY_OPTIONS:
                    addOverlays(GlobalValues.RealLocList);

                    break;
            }
        }
    };


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        //在使用SDK各组件之前初始化context信息，传入ApplicationContext
        //注意该方法要再setContentView方法之前实现

        SDKInitializer.initialize(getActivity().getApplicationContext());
        view = inflater.inflate(R.layout.fragment_main, container, false);

        initView();//初始化View

        initLocation();
        if (GlobalValues.isStart != 1) {
            GlobalValues.isStart = 0;
        }
        return view;

    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        t.start();
    }




    private void initView() {


        //获取地图
        mMapView = (MapView) view.findViewById(R.id.bmapView);
        mBaiduMap = mMapView.getMap();
        // 开启定位图层，一定不要少了这句，否则对在地图的设置、绘制定位点将无效
        mBaiduMap.setMyLocationEnabled(true);
        start_btn = (Button) view.findViewById(R.id.start_btn);

        mMarker = BitmapDescriptorFactory.fromResource(R.mipmap.icon_gcoding);

        //巡检界面控制
        btnTracePause = (Button) view.findViewById(R.id.btn_pause);
        btnTraceContinue = (Button) view.findViewById(R.id.btn_continue);
        btnTraceStop = (Button) view.findViewById(R.id.btn_stop);
        traceControlTab = (LinearLayout) view.findViewById(R.id.trace_ll);

        tvStartTime = (TextView) view.findViewById(R.id.tv_start_time);
        tvRealTime = (TextView) view.findViewById(R.id.tv_real_time);

        //求救界面控制

        seekerControlTab = (LinearLayout) view.findViewById(R.id.seeker_ll);
        btnCallHelper = (Button) view.findViewById(R.id.btn_call_helper);
        btnSeekFinish = (Button) view.findViewById(R.id.btn_seek_finish);

        btnCallHelper.setOnClickListener(this);
        btnSeekFinish.setOnClickListener(this);

        tvSeekerName = (TextView) view.findViewById(R.id.tv_helper_name);
        tvSeekerPhone = (TextView) view.findViewById(R.id.tv_helper_phone);
        tvSeekerJobnum = (TextView) view.findViewById(R.id.tv_helper_jobnum);

        //施救界面控制

        helperControlTab = (LinearLayout) view.findViewById(R.id.helper_ll);
        btnCallHelper = (Button) view.findViewById(R.id.btn_call_helper);
        btnHelpFinish = (Button) view.findViewById(R.id.btn_help_finish);

        btnCallHelper.setOnClickListener(this);
        btnHelpFinish.setOnClickListener(this);

        tvHelperName = (TextView) view.findViewById(R.id.tv_seeker_name);
        tvHelperPhone = (TextView) view.findViewById(R.id.tv_seeker_phone);
        tvHelperJobnum = (TextView) view.findViewById(R.id.tv_seeker_jobnum);


        //移动缩放按钮
        mMapView.showZoomControls(true);//设置是否显示缩放控件
        mMapView.getChildAt(2).setPadding(0, 0, 0, 300);//这是控制缩放控件的位置
        //取消logo和比例尺
        mMapView.removeViewAt(1);


        start_btn.setOnClickListener(this);
        btnTracePause.setOnClickListener(this);
        btnTraceContinue.setOnClickListener(this);
        btnTraceStop.setOnClickListener(this);


        mBaiduMap.setOnMarkerClickListener(new BaiduMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                //弹出dialog 显示巡检人员的信息 以及打电话

                final Bundle extraInfo = marker.getExtraInfo();

                if (extraInfo.getString("name") != null) {

                    final String name = extraInfo.getString("name");
                    final String phone = extraInfo.getString("phone1");
                    final String jobnum = extraInfo.getString("jobnum");
                    Double seeker_lat = extraInfo.getDouble("lat");
                    Double seeker_lng = extraInfo.getDouble("lng");
                    last = new LatLng(seeker_lat, seeker_lng);

                    new DetailDialog(getActivity(), name, phone, jobnum, 0, new DetailDialog.OnDialogButtonClickListener() {
                        @Override
                        public void onDialogButtonClick(int requestCode, boolean isPositive) {
                            if (isPositive) {


                                Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + phone));
                                startActivity(intent);

//                                getRoute(last);

                            }
                        }
                    }).show();

                }
                return true;
            }
        });

    }


    /**
     * 刷新周围人定位  在这里
     */
    private void initLocation() {

        //定位客户端的设置
        mLocationClient = new LocationClient(getActivity().getApplicationContext());
        mLocationListener = new MyLocationListener();
        //注册监听
        mLocationClient.registerLocationListener(mLocationListener);

        myOrientationListener = new MyOrientationListener(getActivity().getApplicationContext());
        myOrientationListener.setmOnOrientationListener(new MyOrientationListener.OnOrientationListener() {
            @Override
            public void onOrientationChanged(float x) {
                mCurrentX = x;
            }
        });

        LocationClientOption mOption = new LocationClientOption();
        mOption.setLocationMode(LocationClientOption.LocationMode.Hight_Accuracy);//设置是否使用gps定位
        mOption.setOpenGps(true);//设置是否使用gps定位
        mOption.setCoorType("bd09ll");//为百度坐标系，其中bd09ll表示百度经纬度坐标，
        mOption.setScanSpan(1000);//设置间隔需大于等于1000ms，表示周期性定位
        mOption.setIsNeedAddress(true);//设置是否需要地址信息
        mOption.setIsNeedLocationPoiList(true);//设置是否需要POI结果，可以在BDLocation.getPoiList里得到

        mLocationClient.setLocOption(mOption);
        mLocationClient.start();
        myOrientationListener.start();


//        handler.post();

    }



    @Override
    public void onDestroy() {
        super.onDestroy();

        DatabaseManager.getInstance().cleanMyLoc(SeekmePreference.getString("userId"));
        DatabaseManager.getInstance().updateClientId("");

        mLocationClient.unRegisterLocationListener(mLocationListener);
        mLocationClient.stop();
        //在activity执行onDestroy时执行mMapView.onDestroy()，实现地图生命周期管理
        mBaiduMap.setMyLocationEnabled(false);
        myOrientationListener.stop();
        mBaiduMap.clear();
        GlobalValues.isStart = -1;
//        mSearch.destroy();
        mMapView.onDestroy();
        mMapView = null;
        //t.interrupt();
    }

    @Override
    public void onResume() {
        super.onResume();
        //在activity执行onResume时执行mMapView. onResume ()，实现地图生命周期管理
        mMapView.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();

        //在activity执行onPause时执行mMapView. onPause ()，实现地图生命周期管理
        mMapView.onPause();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        //t.interrupt();
    }

    @Override
    public void onClick(View v) {

        LatLng ll;
        MapStatus.Builder builder = new MapStatus.Builder();

        switch (v.getId()) {
            case R.id.start_btn:
                MainActivity mainActivity = (MainActivity) getActivity();
                mainActivity.refreshTitle("巡检中...");

                mBaiduMap.clear();
                GlobalValues.isStart = 1;
                //显示

                start_btn.setVisibility(View.GONE);
                traceControlTab.setVisibility(View.VISIBLE);
                starttime = TimeUtil.getNowTime();
                tvStartTime.setText(starttime);

                //计时
                handler.removeMessages(START_TO_RECORD_TIME);
                handler.sendEmptyMessageDelayed(START_TO_RECORD_TIME, 1000);

                //先放置起始点 再绘图
                LatLng firstLL = new LatLng(mLatitude, mLongitude);

                /**
                 * 调整中心位置和缩放  很重要
                 */
                centerToMe(mLatitude, mLongitude);
                mBaiduMap.setMapStatus(MapStatusUpdateFactory.zoomTo(18.0f));

                /**
                 * 设置起始点图标
                 */
                startOptions = new MarkerOptions()
                        .icon(BitmapDescriptorFactory.fromResource(R.mipmap.icon_start))
                        .position(firstLL);
                pointList.add(firstLL);
                //Toast.makeText(mainActivity, "firstLL"+firstLL, Toast.LENGTH_SHORT).show();
                mBaiduMap.addOverlay(startOptions);


                break;
            case R.id.btn_pause:
                //停止计时  停止绘图
                btnTraceContinue.setVisibility(View.VISIBLE);
                btnTracePause.setVisibility(View.GONE);

                GlobalValues.isStart = 2;

                ((MainActivity) getActivity()).refreshTitle("暂停中...");

                break;

            case R.id.btn_continue:
                //继续计时  从当前点继续绘图

                btnTraceContinue.setVisibility(View.GONE);
                btnTracePause.setVisibility(View.VISIBLE);


                GlobalValues.isStart = 1;
                handler.removeMessages(START_TO_RECORD_TIME);
                handler.sendEmptyMessageDelayed(START_TO_RECORD_TIME, 1000);


                ((MainActivity) getActivity()).refreshTitle("巡检中...");

                centerToMe(mLatitude, mLongitude);
                mBaiduMap.setMapStatus(MapStatusUpdateFactory.zoomTo(18.0f));


                break;
            case R.id.btn_seek_finish:
                seekerControlTab.setVisibility(View.GONE);
                start_btn.setVisibility(View.VISIBLE);
                GlobalValues.isStart = 0;
                break;

            case R.id.btn_help_finish:
                helperControlTab.setVisibility(View.GONE);
                start_btn.setVisibility(View.VISIBLE);
                mBaiduMap.clear();
                GlobalValues.isStart = 0;
                break;
            case R.id.btn_stop:
                //弹出dialog 再次确定
                showDialog();

                break;

        }
    }


    private void addOverlays(List<RealLoc> Locs) {
        LatLng latLng = null;

        Marker marker = null;
        OverlayOptions options;
        for (RealLoc Loc : Locs) {
            //经纬度
            latLng = new LatLng(Loc.getLat(), Loc.getLng());
            //图标
            options = new MarkerOptions()
                    .position(latLng)
                    .icon(mMarker)
                    .zIndex(5);
            marker = (Marker) mBaiduMap.addOverlay(options);


            Bundle bundle = new Bundle();
            bundle.putString("name", Loc.getName());
            bundle.putString("phone1", Loc.getPhone1());
            bundle.putString("jobnum", Loc.getJobnum());
            bundle.putDouble("lat", Loc.getLat());
            bundle.putDouble("lng", Loc.getLng());

            if (marker != null) {
                marker.setExtraInfo(bundle);
            }

        }
    }

    //刷新周围人坐标
    private void getWorkerLoc() {

        GlobalValues.RealLocList.clear();
//            Log.i("===开始查找===", LocalUser.getInstance().getUserId());
        if (GlobalValues.isStart == 0) {
            DatabaseManager.getInstance().getRealLocList(
                    SeekmePreference.getString("userId"),
                    SeekmePreference.getInt("belong"),
                    SeekmePreference.getInt("leaf"));

            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    mBaiduMap.clear();//清除图层
                    handler.sendEmptyMessage(ADD_OVERLAY_OPTIONS);
                }
            }, 1000);
        }
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mActivity = activity;//保存activity引用
    }

    private void showDialog() {

        new AlertDialog(getActivity(),
                "结束巡检", "确定要结束么?", true, 0,
                new AlertDialog.OnDialogButtonClickListener() {
                    @Override
                    public void onDialogButtonClick(int requestCode, boolean isPositive) {
                        if (isPositive) {
                            GlobalValues.isStart = 0;

                            realtime = TimeUtil.formatTime(time);
                            today = TimeUtil.getDate();

                            drawFinishMap();


                            //停0.3秒  回到主界面
                            handler.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    handler.sendEmptyMessage(REST_AND_REFRESH);

                                    DatabaseManager.getInstance().UpdatePicture(snapShotPath, snapShotName);
                                }
                            }, 300);

                        }
                    }


                }).show();
    }


    private void drawFinishMap() {
        GlobalValues.isStart = 0;
        LatLng startLatLng = pointList.get(0);
        LatLng endLatLng = pointList.get(pointList.size() - 1);
        //地理范围
        LatLngBounds bounds = new LatLngBounds.Builder().include(startLatLng).include(endLatLng).build();

        update = MapStatusUpdateFactory.newLatLngBounds(bounds);

        endOptions = new MarkerOptions()
                .icon(BitmapDescriptorFactory.fromResource(R.mipmap.icon_end))
                .position(endLatLng);
        pointList.add(endLatLng);
        mBaiduMap.addOverlay(endOptions);


        //截图
        mBaiduMap.snapshot(new BaiduMap.SnapshotReadyCallback() {
            @Override
            public void onSnapshotReady(Bitmap bitmap) {
                Pair<String, String> res = FileUtil.saveBitmapToFile(bitmap, "BaiduSnap");
                snapShotName = res.getFirst();
                snapShotPath = res.getSecond();
                Log.i("TAG", "截图 名称" + snapShotName);


                TraceRecord traceRecord = new TraceRecord();

                traceRecord.setUserId(SeekmePreference.getString("userId"));
                traceRecord.setDate(today);
                traceRecord.setTrace_starttime(starttime);
                traceRecord.setTrace_realtime(realtime);
                traceRecord.setTrace_pic_url(snapShotName);


//                Log.i("TAG", LocalUser.getInstance().getUserId()+"|--|"+today+"|--|"+starttime+"|--|"+realtime+"|--|"+snapShotName);
                DatabaseManager.getInstance().addTraceRecord(traceRecord);

            }
        });
    }

    public void saveSosPic() {
        mBaiduMap.snapshot(new BaiduMap.SnapshotReadyCallback() {
            @Override
            public void onSnapshotReady(Bitmap bitmap) {
                Pair<String, String> res = FileUtil.saveBitmapToFile(bitmap, "SosSnap");
                snapShotName = res.getFirst();
                snapShotPath = res.getSecond();
                GlobalValues.sosName = res.getFirst();

                DatabaseManager.getInstance().UpdatePicture(snapShotPath, snapShotName);

            }
        });
    }


    private class MyLocationListener implements BDLocationListener {

        @Override
        public void onReceiveLocation(BDLocation location) {
            //定位位置回调
            /**
             * MyLocationData 定位数据类，地图上的定位位置需要经纬度、精度、方向这几个参数，所以这里我们把这个几个参数传给地图
             * 如果不需要要精度圈，推荐builder.accuracy(0);
             * mCurrentDirection 是通过手机方向传感器获取的方向；也可以设置option.setNeedDeviceDirect(true)获取location.getDirection()，
             但是这不会时时更新位置的方向，因为周期性请求定位有时间间隔。
             * location.getLatitude()和location.getLongitude()经纬度，如果你只需要在地图上显示某个固定的位置，完全可以写入固定的值，
             如纬度36.958454，经度114.137588，这样就不要要同过定位sdk来获取位置了
             */
            MyLocationData locData = new MyLocationData.Builder()
//                     .accuracy(location.getRadius())
                    .direction(mCurrentX)
                    .latitude(location.getLatitude())
                    .longitude(location.getLongitude())
                    .build();
            MyLocationConfiguration config = new
                    MyLocationConfiguration
                    (MyLocationConfiguration.LocationMode.NORMAL, true, null);

            mBaiduMap.setMyLocationConfiguration(config);
            mBaiduMap.setMyLocationData(locData);//给地图设置定位数据，这样地图就显示位置了


            if (isFirstLoc) {

                isFirstLoc = false;
                LatLng ll = new LatLng(location.getLatitude(), location.getLongitude());

                //设置缩放中心点；缩放比例；
                MapStatus.Builder builder = new MapStatus.Builder().target(ll).zoom(19.0f);

                //更新地图状态
                mBaiduMap.animateMapStatus(MapStatusUpdateFactory.newMapStatus(builder.build()));

            }

            /**
             * 更新经纬度  保存 上传
             */
            mLongitude = location.getLongitude();
            mLatitude = location.getLatitude();

            LatLng latLng = new LatLng(mLatitude, mLongitude);

            if (GlobalValues.isStart == 1) {
                if (pointList != null && pointList.size()==0)pointList.add(latLng);
                last = pointList.get(0);
                //从第二个点开始
                LatLng ll = new LatLng(location.getLatitude(), location.getLongitude());

            /*
            * sdk回调gps位置的频率是1秒1个，位置点太近动态画在图上不是很明显，
              可以设置点之间距离大于为3米才添加到集合中
            */
                if (DistanceUtil.getDistance(last, ll) < 1) {
                    return;
                }

                pointList.add(ll);//如果要运动完成后画整个轨迹，位置点都在这个集合中

                last = ll;

                //显示当前定位点，缩放地图
//                locateAndZoom(location, ll);

                //清除上一次轨迹，避免重叠绘画
                mMapView.getMap().clear();
                //起始点图层也会被清除，重新绘画


                MarkerOptions oStart = new MarkerOptions();
                oStart.position(pointList.get(0));
                oStart.icon(BitmapDescriptorFactory.fromResource(R.mipmap.icon_start));
                mBaiduMap.addOverlay(oStart);

                //将points集合中的点绘制轨迹线条图层，显示在地图上
                polyLine = new PolylineOptions().width(13).color(0xAAFF0000).points(pointList);
                mBaiduMap.addOverlay(polyLine);

            }


        }
    }


    //中心切换到自己
    private void centerToMe(double mLatitude, double mLongitude) {
        LatLng latLng = new LatLng(mLatitude, mLongitude);
        MapStatusUpdate msu = MapStatusUpdateFactory
                .newLatLng(latLng);
        mBaiduMap.setMapStatus(msu);
    }

    //根据 pointList  调整 zoom  方便截图

    private void getRoute(LatLng latlng) {

        final LatLng startLL = new LatLng(mLatitude, mLongitude);
        final LatLng endLL = latlng;

        mSearch = RoutePlanSearch.newInstance();

        mSearch.setOnGetRoutePlanResultListener(this);

        PlanNode stNode = PlanNode.withLocation(startLL);

        PlanNode enNode = PlanNode.withLocation(endLL);

        mSearch.walkingSearch((new WalkingRoutePlanOption())
                .from(stNode)
                .to(enNode)
        );

    }


    @Override
    public void onGetWalkingRouteResult(WalkingRouteResult result) {
        if (result == null || result.error != SearchResult.ERRORNO.NO_ERROR) {
            Log.i("|-------|", "抱歉，未找到结果");
        }
        if (result.error == SearchResult.ERRORNO.AMBIGUOUS_ROURE_ADDR) {
            // 起终点或途经点地址有岐义，通过以下接口获取建议查询信息
            // result.getSuggestAddrInfo()
            return;
        }
        if (result.error == SearchResult.ERRORNO.NO_ERROR) {
            mBaiduMap.clear();
            route = result.getRouteLines().get(0);
            WalkingRouteOverlay overlay = new WalkingRouteOverlay(mBaiduMap);
//            mBaiduMap.setOnMarkerClickListener(overlay);
            routeOverlay = overlay;
            //设置路线数据

            overlay.setData(result.getRouteLines().get(0));
            overlay.addToMap();  //将所有overlay添加到地图中
            overlay.zoomToSpan();//缩放地图
        }

    }

    @Override
    public void onGetTransitRouteResult(TransitRouteResult transitRouteResult) {

    }

    @Override
    public void onGetMassTransitRouteResult(MassTransitRouteResult massTransitRouteResult) {

    }

    @Override
    public void onGetDrivingRouteResult(DrivingRouteResult drivingRouteResult) {

    }

    @Override
    public void onGetIndoorRouteResult(IndoorRouteResult indoorRouteResult) {

    }

    @Override
    public void onGetBikingRouteResult(BikingRouteResult bikingRouteResult) {

    }

    public void transToSeekerStatus(final String name, final String phone, final String jobnum) {
        seekerControlTab.setVisibility(View.VISIBLE);
        start_btn.setVisibility(View.GONE);
        tvSeekerJobnum.setText(jobnum);
        tvSeekerName.setText(name);
        tvSeekerPhone.setText(phone);
    }

    public void transToHelperStatus(final String name, final String phone, final String jobnum,
                                    final Double seekerlat, final Double seekerlng) {
        helperControlTab.setVisibility(View.VISIBLE);
        start_btn.setVisibility(View.GONE);
        GlobalValues.isStart = 1;

        tvHelperName.setText(name);
        tvHelperPhone.setText(phone);
        tvHelperJobnum.setText(jobnum);
        LatLng latLng = new LatLng(seekerlat, seekerlng);

        getRoute(latLng);

    }

}
