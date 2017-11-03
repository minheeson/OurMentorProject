package com.ourmentor.ymh.lck.smh;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;

import com.ourmentor.ymh.lck.smh.common.PreferenceUtil;

public class SettingsActivity extends AppCompatActivity {

    private Switch switch1;
    private TextView logOut;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        logOut = (TextView)findViewById(R.id.logoutBtn);
        logOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SettingsActivity.this , LoginActivity.class);
                PreferenceUtil.instance(getApplicationContext()).putRedId("");
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        });

        switch1 = (Switch)findViewById(R.id.switch1);
        switch1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    PreferenceUtil.instance(getApplicationContext()).putRedPushFlag(true);
                } else {
                    PreferenceUtil.instance(getApplicationContext()).putRedPushFlag(false);
                }
            }
        });

    }


}
