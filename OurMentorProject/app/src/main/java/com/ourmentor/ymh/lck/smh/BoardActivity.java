package com.ourmentor.ymh.lck.smh;

import android.app.FragmentTransaction;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.ourmentor.ymh.lck.smh.common.OurMentorConstant;
import com.ourmentor.ymh.lck.smh.fragment.BoardAllFragment;
import com.ourmentor.ymh.lck.smh.fragment.BoardEduFragment;
import com.ourmentor.ymh.lck.smh.fragment.BoardStudyFragment;
import com.ourmentor.ymh.lck.smh.fragment.BoardWorkFragment;
import com.ourmentor.ymh.lck.smh.fragment.NaviFragment;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class BoardActivity extends AppCompatActivity {
    private BoardEduFragment boardEduFragment;
    private BoardStudyFragment boardStudyFragment;
    private BoardWorkFragment boardWorkFragment;
    private BoardAllFragment boardAllFragment;
    private DrawerLayout board_drawer_layout;
    private ImageView boardAllBtn, boardEduBtn, boardStudyBtn, boardWorkBtn;
    private ImageView board_navi_btn;
    private NaviFragment board_naviFragment;
    private android.support.v4.app.FragmentTransaction ft, ft2;
    String hashTag;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_board);

        boardAllBtn = (ImageView) findViewById(R.id.board_navi_all);
        boardEduBtn = (ImageView) findViewById(R.id.board_navi_edu);
        boardStudyBtn = (ImageView) findViewById(R.id.board_navi_study);
        boardWorkBtn = (ImageView) findViewById(R.id.board_navi_work);
        board_drawer_layout = (DrawerLayout) findViewById(R.id.board_drawer_layout);
        board_navi_btn = (ImageView) findViewById(R.id.board_navi_btn);
        boardEduFragment = new BoardEduFragment();
        boardStudyFragment = new BoardStudyFragment();
        boardWorkFragment = new BoardWorkFragment();
        boardAllFragment = new BoardAllFragment();

        int board_navi_img_array[] = new int[6];
        board_navi_img_array[0] = R.drawable.home_g;
        board_navi_img_array[1] = R.drawable.profile_g;
        board_navi_img_array[2] = R.drawable.board_b;
        board_navi_img_array[3] = R.drawable.chatting_g;
        board_navi_img_array[4] = R.drawable.ranking_g;
        board_navi_img_array[5] = R.drawable.settings_g;
        Bundle c = new Bundle();
        c.putIntArray("array",board_navi_img_array);
        board_naviFragment = new NaviFragment();
        board_naviFragment.setArguments(c);

        ft = getSupportFragmentManager().beginTransaction();
        ft.add(R.id.board_navigation_container, board_naviFragment);
        ft.commit();
        board_navi_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                board_drawer_layout.openDrawer(Gravity.LEFT);
            }
        });

        Intent intent = getIntent();
        hashTag = intent.getStringExtra("hashTag");
        if(hashTag != null)
        {
            BoardAllFragment boardAllFragment = new BoardAllFragment();
            Bundle b = new Bundle();
            b.putString("hashTag",hashTag);
            boardAllFragment.setArguments(b);
            ft2 = getSupportFragmentManager().beginTransaction();
            ft2.replace(R.id.board_container, boardAllFragment);
            ft2.commit();
        }
        else {
            ft2 = getSupportFragmentManager().beginTransaction();
            ft2.replace(R.id.board_container, boardAllFragment);
            ft2.commit();
        }

        boardAllBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boardAllBtn.setImageResource(R.drawable.board_nav_total_p);
                boardEduBtn.setImageResource(R.drawable.board_nav_education_n);
                boardStudyBtn.setImageResource(R.drawable.board_nav_study_n);
                boardWorkBtn.setImageResource(R.drawable.board_nav_job_n);
                android.support.v4.app.FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                ft.replace(R.id.board_container, boardAllFragment);
                ft.commit();
            }
        });
        boardEduBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boardAllBtn.setImageResource(R.drawable.board_nav_total_n);
                boardEduBtn.setImageResource(R.drawable.board_nav_education_p);
                boardStudyBtn.setImageResource(R.drawable.board_nav_study_n);
                boardWorkBtn.setImageResource(R.drawable.board_nav_job_n);
                android.support.v4.app.FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                ft.replace(R.id.board_container, boardEduFragment);
                ft.commit();
            }
        });
        boardStudyBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boardAllBtn.setImageResource(R.drawable.board_nav_total_n);
                boardEduBtn.setImageResource(R.drawable.board_nav_education_n);
                boardStudyBtn.setImageResource(R.drawable.board_nav_study_p);
                boardWorkBtn.setImageResource(R.drawable.board_nav_job_n);
                android.support.v4.app.FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                ft.replace(R.id.board_container, boardStudyFragment);
                ft.commit();
            }
        });

        boardWorkBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boardAllBtn.setImageResource(R.drawable.board_nav_total_n);
                boardEduBtn.setImageResource(R.drawable.board_nav_education_n);
                boardStudyBtn.setImageResource(R.drawable.board_nav_study_n);
                boardWorkBtn.setImageResource(R.drawable.board_nav_job_p);
                android.support.v4.app.FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                ft.replace(R.id.board_container, boardWorkFragment);
                ft.commit();
            }
        });
    }

    @Override
    protected void onPause() {
        super.onPause();
        board_drawer_layout.closeDrawers();
    }
}

