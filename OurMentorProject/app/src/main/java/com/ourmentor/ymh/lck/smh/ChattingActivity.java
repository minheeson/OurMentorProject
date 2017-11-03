package com.ourmentor.ymh.lck.smh;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.support.design.widget.TabLayout;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;

import com.ourmentor.ymh.lck.smh.common.OurMentorConstant;
import com.ourmentor.ymh.lck.smh.common.ReplyObject;
import com.ourmentor.ymh.lck.smh.fragment.ChattingListFragment;
import com.ourmentor.ymh.lck.smh.fragment.ChattingMentorFragment;
import com.ourmentor.ymh.lck.smh.fragment.ChattingMenteeFragment;
import com.ourmentor.ymh.lck.smh.fragment.NaviFragment;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class   ChattingActivity extends AppCompatActivity {
    private TabLayout chatting_tab;
    private String stringArray[];
    private ChattingMentorFragment chattingMentorFragment;
    private ChattingMenteeFragment chattingMenteeFragment;
    private ChattingListFragment chattingListFragment;
    private NaviFragment chatting_naviFragment;
    private ImageView chatting_navi_btn;
    private DrawerLayout chatting_drawer_layout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chatting);

        chatting_drawer_layout = (DrawerLayout)findViewById(R.id.chatting_drawer_layout);
        chatting_tab = (TabLayout)findViewById(R.id.chatting_tab);
        stringArray = new String[3];
        stringArray[0] = "멘토";
        stringArray[1] = "멘티";
        stringArray[2] = "채팅";

        for(int i = 0; i<stringArray.length; i++)
        {
            chatting_tab.addTab(chatting_tab.newTab().setText(stringArray[i]));
        }

        android.support.v4.app.FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.chatting_container, new ChattingMentorFragment());
        ft.commit();
        chatting_tab.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                if(tab.getText() == stringArray[0])
                {
                    android.support.v4.app.FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                    ft.replace(R.id.chatting_container, new ChattingMentorFragment());
                    ft.commit();
                }
                else if (tab.getText() == stringArray[1])
                {
                    android.support.v4.app.FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                    ft.replace(R.id.chatting_container, new ChattingMenteeFragment());
                    ft.commit();
                }
                else if (tab.getText() == stringArray[2])
                {
                    android.support.v4.app.FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                    ft.replace(R.id.chatting_container, new ChattingListFragment());
                    ft.commit();
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        int chatting_navi_img_array[] = new int[6];
        chatting_navi_img_array[0] = R.drawable.home_g;
        chatting_navi_img_array[1] = R.drawable.profile_g;
        chatting_navi_img_array[2] = R.drawable.board_g;
        chatting_navi_img_array[3] = R.drawable.chatting_b;
        chatting_navi_img_array[4] = R.drawable.ranking_g;
        chatting_navi_img_array[5] = R.drawable.settings_g;
        Bundle b = new Bundle();
        b.putIntArray("array",chatting_navi_img_array);
        chatting_naviFragment = new NaviFragment();
        chatting_naviFragment.setArguments(b);

        chatting_navi_btn = (ImageView)findViewById(R.id.chatting_navi_btn);

        android.support.v4.app.FragmentTransaction ft1 = getSupportFragmentManager().beginTransaction();
        ft1.replace(R.id.chatting_navigation_container, chatting_naviFragment);
        ft1.commit();
        chatting_navi_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chatting_drawer_layout.openDrawer(Gravity.LEFT);
            }
        });
    }

    @Override
    protected void onPause() {
        super.onPause();
        chatting_drawer_layout.closeDrawers();
    }



}
