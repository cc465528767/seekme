package com.zzzealous.seekme.Activity;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.Settings;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.igexin.sdk.PushManager;
import com.zzzealous.seekme.Activity.Mine.MineAddressActivity;
import com.zzzealous.seekme.Bean.GlobalValues;
import com.zzzealous.seekme.Database.DatabaseManager;
import com.zzzealous.seekme.Fragment.ChatFragment;
import com.zzzealous.seekme.Fragment.MainFragment;
import com.zzzealous.seekme.Fragment.MineFragment;
import com.zzzealous.seekme.Fragment.NewsFragment;
import com.zzzealous.seekme.Fragment.WorkFragment;
import com.zzzealous.seekme.GTPush.DemoPushService;
import com.zzzealous.seekme.GTPush.GTDataManager;
import com.zzzealous.seekme.GTPush.SeekMeIntentService;
import com.zzzealous.seekme.Preference.SeekmePreference;
import com.zzzealous.seekme.R;
import com.zzzealous.seekme.UI.AlertDialog;
import com.zzzealous.seekme.Utils.TextUtil;
import com.zzzealous.seekme.Utils.TimeUtil;
import com.zzzealous.seekme.Utils.ToastUtil;

import java.util.List;

import pub.devrel.easypermissions.AppSettingsDialog;
import pub.devrel.easypermissions.EasyPermissions;
import pub.devrel.easypermissions.PermissionRequest;

public class MainActivity extends Activity implements View.OnClickListener, EasyPermissions.PermissionCallbacks {
    private final String TAG = "|-----------------------|";
    //紧急救援按钮
    private LinearLayout sosbtn;
    private LinearLayout addlayout;

    //底端按钮
    private LinearLayout mTabMain;
    private LinearLayout mTabChat;
    private LinearLayout mTabWork;
    private LinearLayout mTabNews;
    private LinearLayout mTabMine;

    //五个主Fragment  以及标题text
    private MainFragment mMainFragment;
    private ChatFragment mChatFragment;
    private WorkFragment mWorkFragment;
    private NewsFragment mNewsFragment;
    private MineFragment mMineFragment;

    public TextView title;

    public String sosid;


    private FragmentManager fragmentManager;
    private FragmentTransaction transaction;

    public static final int SAVE_SOS_RECORD = 0x00;
    private static final int RC_CAMERA_LOCATION_WRITE = 100;

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case SAVE_SOS_RECORD:
                    DatabaseManager.getInstance().saveSosFirStep(
                            TimeUtil.getNowTime(),
                            TimeUtil.getDate(),
                            String.valueOf(GlobalValues.SosLat),
                            String.valueOf(GlobalValues.SosLng),
                            GlobalValues.sosName,
                            sosid);
                    break;

            }
        }
    };


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            moveTaskToBack(true);
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        SharedPreferences sp = getSharedPreferences("loginToken", Activity.MODE_PRIVATE);
        boolean Loginstate = sp.getBoolean("Loginstate", false);
        if (Loginstate) {
            requestPermissions();
        } else {
            Intent loginIntent = new Intent(MainActivity.this, LoginActivity.class);
            startActivity(loginIntent);
            finish();
        }
    }

    private void requestPermissions() {
        Log.d("dreamkong", "requestPermissions: ");
        String[] perms = {Manifest.permission.CAMERA, Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.WRITE_EXTERNAL_STORAGE};
        if (!EasyPermissions.hasPermissions(this, perms)) {
            // 没有申请过权限，现在去申请
//            EasyPermissions.requestPermissions(this, getString(R.string.app_name) + "需要如下权限，否则无法正常使用",
//                    RC_CAMERA_LOCATION_WRITE, perms);

            EasyPermissions.requestPermissions(
                    new PermissionRequest.Builder(this, RC_CAMERA_LOCATION_WRITE, perms)
                            .setRationale(getString(R.string.app_name) + "需要如下权限，否则无法正常使用")
                            .setPositiveButtonText("确定")
                            .setNegativeButtonText("")
                            .build());
        } else {
            // com.getui.demo.DemoPushService 为第三方自定义推送服务
            PushManager.getInstance().initialize(this.getApplicationContext(), DemoPushService.class);
            // com.getui.demo.SeekMeIntentService 为第三方自定义的推送服务事件接收类
            PushManager.getInstance().registerPushIntentService(this.getApplicationContext(), SeekMeIntentService.class);
            initView();//关联图像
            setDefaultFragment();//设置默认主界面
            SeekmePreference.save("tokennotvalue", true);
        }
    }


    @Override
    public void onPermissionsGranted(int requestCode, List<String> list) {
        //请求成功执行相应的操作
        // com.getui.demo.DemoPushService 为第三方自定义推送服务
        PushManager.getInstance().initialize(this.getApplicationContext(), DemoPushService.class);
        // com.getui.demo.SeekMeIntentService 为第三方自定义的推送服务事件接收类
        PushManager.getInstance().registerPushIntentService(this.getApplicationContext(), SeekMeIntentService.class);
        initView();//关联图像
        setDefaultFragment();//设置默认主界面
        SeekmePreference.save("tokennotvalue", true);
    }

    @Override
    public void onPermissionsDenied(int requestCode, List<String> perms) {
        //处理权限名字字符串
        StringBuffer sb = new StringBuffer();
        for (String str : perms) {
            if (str.equals(Manifest.permission.CAMERA)) {
                str = "相机";
            } else if (str.equals(Manifest.permission.ACCESS_FINE_LOCATION)) {
                str = "位置";
            } else if (str.equals(Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                str = "存储";
            }
            sb.append(str);
        }
        sb.replace(sb.length() - 2, sb.length(), "");
        if (EasyPermissions.somePermissionPermanentlyDenied(this, perms)) {
            new android.support.v7.app.AlertDialog.Builder(this)
                    .setMessage("需要手动开启权限")
                    .setPositiveButton("前往设置", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            getIntentSetting();
                        }
                    })
                    .setCancelable(false)
                    .show();
        } else {
            finish();
        }
    }


    private void initView() {
        //标题栏加监听
        title = (TextView) findViewById(R.id.title_name);
        sosbtn = (LinearLayout) findViewById(R.id.sos_button);
       addlayout=(LinearLayout) findViewById(R.id.address_button);
        //底边按钮关联并加监听
        mTabMain = (LinearLayout) findViewById(R.id.tab_main);
        mTabChat = (LinearLayout) findViewById(R.id.tab_chat);
        mTabWork = (LinearLayout) findViewById(R.id.tab_work);
        mTabNews = (LinearLayout) findViewById(R.id.tab_news);
        mTabMine = (LinearLayout) findViewById(R.id.tab_mine);

        sosbtn.setOnClickListener(this);
        mTabMain.setOnClickListener(this);
        mTabChat.setOnClickListener(this);
        mTabWork.setOnClickListener(this);
        mTabNews.setOnClickListener(this);
        mTabMine.setOnClickListener(this);
        addlayout.setOnClickListener(this);
        fragmentManager = getFragmentManager();
    }

    private void setDefaultFragment() {

        // 开启Fragment事务
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        mMainFragment = new MainFragment();
        transaction.add(R.id.id_content, mMainFragment);
        transaction.commit();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.sos_button:
//                ToastUtil.setShortToast(MainActivity.this,GlobalValues.isStart+"状态");
                GlobalValues.ClientArray = null;
                GTDataManager.getInstance().getClientArray();
                showSOSdialog();
                setTabSelection(0);
                break;
            case R.id.address_button:
                Intent intent = new Intent(mChatFragment.getActivity(), MineAddressActivity.class);
                startActivity(intent);
            case R.id.tab_main:

                setTabSelection(0);

                break;
            case R.id.tab_chat:

                setTabSelection(1);

                break;
            case R.id.tab_work:

                setTabSelection(2);
                break;

            case R.id.tab_news:

                setTabSelection(3);

                break;
            case R.id.tab_mine:

                setTabSelection(4);

                break;

            default:
                break;

        }
    }


    private void setTabSelection(int index) {
        // 重置按钮
        resetBtn();
        // 开启一个Fragment事务
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        // 先隐藏掉所有的Fragment，以防止有多个Fragment显示在界面上的情况
        hideFragments(transaction);
        switch (index) {
            case 0:
                if (GlobalValues.isStart != 1) {
                    GlobalValues.isStart = 0;
                }
                // 当点击了主页tab时，改变控件的图片和文字颜色
                ((ImageView) findViewById(R.id.btn_main))
                        .setImageResource(R.mipmap.main_selected);
                ((ImageView) findViewById(R.id.address_mag))
                        .setImageResource(0);
                refreshTitle("SeekMe");

                if (GlobalValues.isStart == 1) {
                    refreshTitle("巡检中...");
                } else if (GlobalValues.isStart == 2) {
                    refreshTitle("暂停中...");
                }

                transaction.show(mMainFragment);
                break;

            case 1:
                if (GlobalValues.isStart != 1) {
                    GlobalValues.isStart = 4;
                }

                // 当点击了消息tab时，改变控件的图片和文字颜色
                ((ImageView) findViewById(R.id.address_mag))
                        .setImageResource(R.mipmap.icon_add);
                ((ImageView) findViewById(R.id.btn_chat))
                        .setImageResource(R.mipmap.chat_selected);
                refreshTitle("消息");

                if (mChatFragment == null) {
                    mChatFragment = new ChatFragment();
                    transaction.add(R.id.id_content, mChatFragment);
                } else {
                    transaction.show(mChatFragment);
                }
                break;

            case 2:
                if (GlobalValues.isStart != 1) {
                    GlobalValues.isStart = 4;
                }

                // 当点击了运维tab时，改变控件的图片和文字颜色
                ((ImageView) findViewById(R.id.address_mag))
                        .setImageResource(0);
                ((ImageView) findViewById(R.id.btn_work))
                        .setImageResource(R.mipmap.work_selected);
                refreshTitle("运维");

                if (mWorkFragment == null) {
                    mWorkFragment = new WorkFragment();
                    transaction.add(R.id.id_content, mWorkFragment);
                } else {
                    transaction.show(mWorkFragment);
                }
                break;

            case 3:
                if (GlobalValues.isStart != 1) {
                    GlobalValues.isStart = 4;
                }

                // 当点击了发现tab时，改变控件的图片和文字颜色
                ((ImageView) findViewById(R.id.address_mag))
                        .setImageResource(0);
                ((ImageView) findViewById(R.id.btn_news))
                        .setImageResource(R.mipmap.news_selected);
                refreshTitle("发现");

                if (mNewsFragment == null) {
                    mNewsFragment = new NewsFragment();
                    transaction.add(R.id.id_content, mNewsFragment);
                } else {
                    transaction.show(mNewsFragment);
                }
                break;

            case 4:
                if (GlobalValues.isStart != 1) {
                    GlobalValues.isStart = 4;
                }

                // 当点击了我的tab时，改变控件的图片和文字颜色
                ((ImageView) findViewById(R.id.address_mag))
                        .setImageResource(0);
                ((ImageView) findViewById(R.id.btn_mine))
                        .setImageResource(R.mipmap.mine_selected);
                refreshTitle("我的");

                if (mMineFragment == null) {
                    mMineFragment = new MineFragment();
                    transaction.add(R.id.id_content, mMineFragment);
                } else {
                    transaction.show(mMineFragment);
                }
                break;
        }
        transaction.commit();
    }

    private void resetBtn() {

        ((ImageView) findViewById(R.id.btn_main))
                .setImageResource(R.mipmap.main_unselected);
        ((ImageView) findViewById(R.id.btn_chat))
                .setImageResource(R.mipmap.chat_unselected);
        ((ImageView) findViewById(R.id.btn_work))
                .setImageResource(R.mipmap.work_unselected);
        ((ImageView) findViewById(R.id.btn_news))
                .setImageResource(R.mipmap.news_unselected);
        ((ImageView) findViewById(R.id.btn_mine))
                .setImageResource(R.mipmap.mine_unselected);

    }


    public void hideFragments(FragmentTransaction transaction) {
//
        if (mMainFragment != null) {
            transaction.hide(mMainFragment);
        }
        if (mChatFragment != null) {
            transaction.hide(mChatFragment);
        }
        if (mWorkFragment != null) {
            transaction.hide(mWorkFragment);
        }
        if (mNewsFragment != null) {
            transaction.hide(mNewsFragment);
        }
        if (mMineFragment != null) {
            transaction.hide(mMineFragment);
        }

    }


    @Override
    protected void onResume() {
        super.onResume();
        // com.getui.demo.DemoPushService 为第三方自定义推送服务
        PushManager.getInstance().initialize(this.getApplicationContext(), DemoPushService.class);

    }

    @Override
    protected void onStop() {
        super.onStop();

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();


    }


    public void refreshTitle(String string) {
        title.setText(string);
    }

    private void showSOSdialog() {
        new AlertDialog(this,
                "紧急救援", "是否要发出紧急救援?", true, 0,
                new AlertDialog.OnDialogButtonClickListener() {
                    @Override
                    public void onDialogButtonClick(int requestCode, boolean isPositive) {
                        if (isPositive) {
                            //进入等待状态
                            //接受后 弹窗

                            if (GlobalValues.ClientArray != null && GlobalValues.ClientArray.length() != 0) {
                                try {

                                    sosid = TextUtil.getUUID();

                                    GTDataManager.getInstance().sendSosMsg(GlobalValues.ClientArray,
                                            sosid);

                                    GlobalValues.sosName = null;
                                    mMainFragment.saveSosPic();

                                    handler.postDelayed(new Runnable() {
                                        @Override
                                        public void run() {
                                            if (GlobalValues.sosName != null) {
                                                handler.sendEmptyMessage(SAVE_SOS_RECORD);
                                            }
                                        }
                                    }, 400);

                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            } else {
                                ToastUtil.setShortToast(MainActivity.this, "周围没有人");
                            }
                        }
                    }
                }).show();
    }

    public MainFragment getmMainFragment() {
        return mMainFragment;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        // Forward results to EasyPermissions
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
    }

    public void getIntentSetting() {
        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        Uri uri = Uri.fromParts("package", getPackageName(), null);
        intent.setData(uri);
        try {
            startActivity(intent);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
