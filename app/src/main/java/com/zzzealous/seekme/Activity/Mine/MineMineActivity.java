package com.zzzealous.seekme.Activity.Mine;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.zzzealous.seekme.Preference.SeekmePreference;
import com.zzzealous.seekme.R;


public class MineMineActivity extends AppCompatActivity {

    private LinearLayout emergencycontant;
    private LinearLayout email;
    private TextView name;
    private TextView phone;
    private TextView jobnumber;
    private TextView position;
    private TextView mail;
    private TextView contact1;
    private TextView contact2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mine_mine);


        name=(TextView)findViewById(R.id.mine_activity_name);
        name.setText(SeekmePreference.getString("name"));

        phone=(TextView)findViewById(R.id.mine_activity_phone);
        phone.setText(SeekmePreference.getString("phone1"));

        jobnumber=(TextView)findViewById(R.id.mine_activity_jobnumber);
        jobnumber.setText(SeekmePreference.getString("jobnumber"));

        position=(TextView)findViewById(R.id.mine_activity_position);
        position.setText(SeekmePreference.getString("job"));

        mail=(TextView)findViewById(R.id.mine_activity_email);
        mail.setText(SeekmePreference.getString("mail"));

        contact1=(TextView)findViewById(R.id.mine_activity_contact1);
        contact1.setText(SeekmePreference.getString("Emergency_contact1"));

        contact2=(TextView)findViewById(R.id.mine_activity_contact2);
        contact2.setText(SeekmePreference.getString("Emergency_contact2"));

        emergencycontant = (LinearLayout) findViewById(R.id.EmergencyContant);
        emergencycontant.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MineMineActivity.this, EmergencyContantActivity.class);
                startActivity(intent);

            }
        });
        email = (LinearLayout) findViewById(R.id.Email);
        email.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent= new Intent(MineMineActivity.this,EmailActivity.class);
                startActivity(intent);             }
        });
    }
}
