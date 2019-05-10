package com.zzzealous.seekme.Activity.News.Notify;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.zzzealous.seekme.Bean.LocalUser;
import com.zzzealous.seekme.R;

public class VoteActivity extends AppCompatActivity {

    private String[] data = {"Apple", "Banana", "Orange", "Watermelon", "pear", "Grape", "Pineapple", "Strawbeery", "Cheery", "Mango"};
    private TextView Show;

    LinearLayout newVote;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vote);

        Show=findViewById(R.id.show);
        Show.setText(LocalUser.getInstance().getS());
//        Show.setText("2345");


        newVote=(LinearLayout)findViewById(R.id.NewVote);
        newVote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(VoteActivity.this,NewVoteActivity.class);
                startActivity(intent);
            }
        });
    }
}
