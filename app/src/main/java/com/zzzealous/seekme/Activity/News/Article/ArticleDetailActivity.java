package com.zzzealous.seekme.Activity.News.Article;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.zzzealous.seekme.Adapter.ArticleCommentAdapter;
import com.zzzealous.seekme.Bean.ArticleComment;
import com.zzzealous.seekme.Bean.GlobalValues;
import com.zzzealous.seekme.Database.DatabaseManager;
import com.zzzealous.seekme.MyApplication;
import com.zzzealous.seekme.Preference.SeekmePreference;
import com.zzzealous.seekme.R;
import com.zzzealous.seekme.Utils.ToastUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class ArticleDetailActivity extends AppCompatActivity {
    private String title;
    private String content;
    private String publisher;
    private String comment;
    private int type;
    private int paperid;


    //      点赞部分
    ImageView articleLike;
    private int likenumber;

    //      评论部分
    EditText editCommentText;
    TextView sendCommentBtn;
    LinearLayout commentLL;
    ImageView articleComment;
    int nowVisible = 0;


    ListView mListView;
    private ArticleCommentAdapter mAdapter;
    private List<ArticleComment> data = new ArrayList<>();//获取数据



    Button back;
    TextView titleTV;
    WebView webView;
    TextView publisherTV;
    TextView likenumberTV;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_article_detail);

        initView();
        receiveIntent();
        setView();

        getData();
        setAdapter();
        Log.d("111", "ll"+content);

    }

    private void getData() {
        //获取本地数据
        /*DatabaseManager.getInstance().getVisibleComment(paperid);*/
        data = GlobalValues.ArticleCommentList;
    }

    private void setAdapter() {
        mAdapter = new ArticleCommentAdapter(getApplicationContext(),data);
        mListView.setAdapter(mAdapter);
    }


    private void initView() {
        titleTV = findViewById(R.id.article_detail_title);
        webView = (WebView)findViewById(R.id.webview);
        webView.getSettings().setUseWideViewPort(true);    //设置此属性，可任意比例缩放
        webView.getSettings().setLoadWithOverviewMode(true); //设置加载内容自适应屏幕


        publisherTV = findViewById(R.id.article_detail_publisher);
        likenumberTV = findViewById(R.id.article_detail_likenumber);


        mListView = findViewById(R.id.article_comment_listview);

        editCommentText = findViewById(R.id.edit_article_comment);
        sendCommentBtn = findViewById(R.id.send_comment_button);
        sendCommentBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                comment = editCommentText.getText().toString();
                if(comment.isEmpty()){
                    ToastUtil.setLongToast(ArticleDetailActivity.this,"评论内容不能为空。");
                }
                else {
                    DatabaseManager.getInstance().addComment(paperid, SeekmePreference.getString("name"),
                            comment);
                    ToastUtil.setLongToast(ArticleDetailActivity.this,"评论成功！");
                    commentLL.setVisibility(View.GONE);
                }
            }
        });


        back = findViewById(R.id.article_detail_backbtn);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ArticleDetailActivity.this.finish();
            }
        });

        commentLL = findViewById(R.id.comment_ll);
        articleComment = findViewById(R.id.article_comment_button);
        articleComment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(nowVisible == 0){
                    ToastUtil.setLongToast(getApplicationContext(),content);
                    commentLL.setVisibility(View.VISIBLE);
                    nowVisible = 1;
                }
                else {
                    commentLL.setVisibility(View.GONE);
                    nowVisible = 0;
                }
            }
        });

        articleLike = findViewById(R.id.article_like_button);
        articleLike.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                articleLike.setImageResource(R.mipmap.icon_like);

                String url = GlobalValues.baseUrl+"articlelike/add_like";

                StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {
                        try {

                            JSONObject JObject = new JSONObject(s);
                            String msg = JObject.getString("msg");
                            if (msg.equals("already")){
                                ToastUtil.setShortToast(ArticleDetailActivity.this,"您已经点过赞了");
                            }else {
                                ToastUtil.setShortToast(ArticleDetailActivity.this, "点赞成功");
                                DatabaseManager.getInstance().articleLikeAddOne(paperid);
                                DatabaseManager.getInstance().getArticlecList(type+"");
                                likenumberTV.setText(likenumber+1+"");
                            }

                        } catch (JSONException JException){
                            JException.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
                    // 请求失败时执行的函数
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        volleyError.printStackTrace();
                    }
                })
                {
                    public Map<String, String> getHeaders() throws AuthFailureError {
                        Map<String, String> headers = new LinkedHashMap<>();
                        // 自定义请求头 X-YAuth-Token
                        headers.put("X-YAuth-Token", SeekmePreference.getString("token"));
                        return headers;
                    }
                    protected Map<String ,String> getParams() throws AuthFailureError
                    {   String parentid=String.valueOf(paperid);
                        Map<String,String> hashMap = new HashMap<>();
                        hashMap.put("parentsid",parentid);
                        hashMap.put("supporterid",SeekmePreference.getString("userId"));
                        return hashMap;
                    }

                };
                // 将请求添加到队列中
                MyApplication.getHttpQueues().add(request);
            }
        });

    }

    private void receiveIntent() {
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        title = bundle.getString("title");
        content = bundle.getString("content");
        publisher = bundle.getString("publisher");
        likenumber = bundle.getInt("likenumber");
        type = bundle.getInt("type");
        paperid = bundle.getInt("paperid");
    }

    private void setView() {
        titleTV.setText(title);
        webView.loadUrl(/*GlobalValues.baseUrl + "images/" +*/content);
        publisherTV.setText(publisher);
        likenumberTV.setText(likenumber+"");
    }
}
