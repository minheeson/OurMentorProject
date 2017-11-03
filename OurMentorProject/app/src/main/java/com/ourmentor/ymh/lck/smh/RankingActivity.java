package com.ourmentor.ymh.lck.smh;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.ourmentor.ymh.lck.smh.common.OurMentorConstant;
import com.ourmentor.ymh.lck.smh.common.ParseDataParseHandler;
import com.ourmentor.ymh.lck.smh.common.RankObject;
import com.ourmentor.ymh.lck.smh.fragment.NaviFragment;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class RankingActivity extends AppCompatActivity {
    private DrawerLayout ranking_drawer_layout;
    private NaviFragment ranking_naviFragment;
    private RankObject mentoKing;
    private RankObject actKing;
    private RankObject levelKing;
    private ArrayList<RankObject> rankObjects;

    private ImageView ranking_navi_btn;
    private TextView mentoKing_textView_name , actKing_textView_name , levelKing_textView_name;
    private TextView mentoKing_textView_hashTag01, mentoKing_textView_hashTag02, mentoKing_textView_hashTag03;
    private TextView actKing_textView_hashTag01,actKing_textView_hashTag02 ,actKing_textView_hashTag03;
    private TextView levelKing_textView_hashTag01,levelKing_textView_hashTag02 ,levelKing_textView_hashTag03;
    private CircleImageView mentoKing_img, actKing_img, levelKing_img;
    private RelativeLayout mentoKing_layout, actKing_layout, levelKing_layout;
    private int mentoKing_userNo, actKing_userNo, levelKing_userNo;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ranking);

        ranking_drawer_layout = (DrawerLayout)findViewById(R.id.ranking_drawer_layout);

        int main_navi_img_array[] = new int[6];
        main_navi_img_array[0] = R.drawable.home_g;
        main_navi_img_array[1] = R.drawable.profile_g;
        main_navi_img_array[2] = R.drawable.board_g;
        main_navi_img_array[3] = R.drawable.chatting_g;
        main_navi_img_array[4] = R.drawable.ranking_b;
        main_navi_img_array[5] = R.drawable.settings_g;

        Bundle b = new Bundle();
        b.putIntArray("array",main_navi_img_array);
        ranking_naviFragment = new NaviFragment();                                            //Navi Settirng
        ranking_naviFragment.setArguments(b);
        android.support.v4.app.FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.add(R.id.ranking_navigation_container, ranking_naviFragment);
        ft.commit();                                                                                          //Navi Commit


        ranking_navi_btn = (ImageView) findViewById(R.id.ranking_navi_btn);
        ranking_navi_btn.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                ranking_drawer_layout.openDrawer(Gravity.LEFT);
                return true;
            }
        });

        mentoKing_textView_name = (TextView)findViewById(R.id.mentoKing_textView_name);
        actKing_textView_name = (TextView)findViewById(R.id.actKing_textView_name);
        levelKing_textView_name = (TextView)findViewById(R.id.levelKing_textView_name);

        mentoKing_textView_hashTag01 = (TextView)findViewById(R.id.mentoKing_textView_hashTag01);
        mentoKing_textView_hashTag02 = (TextView)findViewById(R.id.mentoKing_textView_hashTag02);
        mentoKing_textView_hashTag03 = (TextView)findViewById(R.id.mentoKing_textView_hashTag03);

        actKing_textView_hashTag01 = (TextView)findViewById(R.id.actKing_textView_hashTag01);
        actKing_textView_hashTag02 = (TextView)findViewById(R.id.actKing_textView_hashTag02);
        actKing_textView_hashTag03 = (TextView)findViewById(R.id.actKing_textView_hashTag03);

        levelKing_textView_hashTag01 = (TextView)findViewById(R.id.levelKing_textView_hashTag01);
        levelKing_textView_hashTag02 = (TextView)findViewById(R.id.levelKing_textView_hashTag02);
        levelKing_textView_hashTag03 = (TextView)findViewById(R.id.levelKing_textView_hashTag03);

        mentoKing_img =(CircleImageView)findViewById(R.id.mentoKing_img);
        actKing_img =(CircleImageView)findViewById(R.id.actKing_img);
        levelKing_img =(CircleImageView)findViewById(R.id.levelKing_img);

        mentoKing_layout = (RelativeLayout)findViewById(R.id.mentoKing_layout);
        actKing_layout = (RelativeLayout)findViewById(R.id.actKing_layout);
        levelKing_layout = (RelativeLayout)findViewById(R.id.levelKing_layout);

        mentoKing_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mentoKing_userNo == OurMentorConstant.userNo)
                {
                    Intent intent = new Intent(RankingActivity.this, ProfileActivity.class);
                    startActivity(intent);
                }
                else if(mentoKing_userNo!= OurMentorConstant.userNo)
                {
                    Intent intent = new Intent(RankingActivity.this, OtherProfileActivity.class);
                    intent.putExtra("userno", mentoKing_userNo );
                    startActivity(intent);
                }
            }
        });

        actKing_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(actKing_userNo == OurMentorConstant.userNo)
                {
                    Intent intent = new Intent(RankingActivity.this, ProfileActivity.class);
                    startActivity(intent);
                }
                else if(actKing_userNo!= OurMentorConstant.userNo)
                {
                    Intent intent = new Intent(RankingActivity.this, OtherProfileActivity.class);
                    intent.putExtra("userno", actKing_userNo );
                    startActivity(intent);
                }
            }
        });

        levelKing_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(levelKing_userNo == OurMentorConstant.userNo)
                {
                    Intent intent = new Intent(RankingActivity.this, ProfileActivity.class);
                    startActivity(intent);
                }
                else if(levelKing_userNo!= OurMentorConstant.userNo)
                {
                    Intent intent = new Intent(RankingActivity.this, OtherProfileActivity.class);
                    intent.putExtra("userno", levelKing_userNo );
                    startActivity(intent);
                }
            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();
        new AsyncRankingJSONList().execute();
    }

    public class AsyncRankingJSONList extends AsyncTask<String, Integer, ArrayList<RankObject>> {
        ProgressDialog dialog1;

        @Override
        protected void onPreExecute() {

            super.onPreExecute();
            mentoKing = new RankObject();
            actKing = new RankObject();
            levelKing =new RankObject();
        }

        @Override
        synchronized protected ArrayList<RankObject> doInBackground(String... params) {
            HttpURLConnection conn1 = null;
            BufferedReader fromServer1 = null;
            ArrayList<RankObject> rankObjects = new ArrayList<>();

            String result;
            try {
                URL targetURL1 = new URL(OurMentorConstant.TARGET_URL + OurMentorConstant.RANK);
                conn1 = (HttpURLConnection) targetURL1.openConnection();
                conn1.setConnectTimeout(10000);
                conn1.setReadTimeout(10000);


                conn1.setRequestMethod("GET");

                int responseCode = conn1.getResponseCode();
                if (responseCode == HttpURLConnection.HTTP_OK) {
                    fromServer1 = new BufferedReader(new InputStreamReader(conn1.getInputStream(), "UTF-8")); //JSON
                    String line = null;
                    StringBuilder jsonBuf = new StringBuilder();
                    while ((line = fromServer1.readLine()) != null) {
                        jsonBuf.append(line);
                        Log.e("RankjsonBuf", String.valueOf(jsonBuf));
                    }
                    rankObjects = ParseDataParseHandler.getJSONRankRequestAllList(jsonBuf);
                }
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                if (fromServer1 != null) {
                    try {
                        fromServer1.close();
                    } catch (IOException ioe) {

                    }
                }
                if (conn1 != null) {
                    conn1.disconnect(); //열은 순서 반대로 닫는다
                }
            }
            return rankObjects;
        }

        @Override
        protected void onPostExecute(ArrayList<RankObject> rankObjects) {
            super.onPostExecute(rankObjects);
            if (rankObjects != null && rankObjects.size() > 0) {
                mentoKing = rankObjects.get(0);
                actKing = rankObjects.get(1);
                levelKing = rankObjects.get(2);

                mentoKing_textView_name.setText(mentoKing.name);
                Glide.with(getApplicationContext()).load(mentoKing.pic).into(mentoKing_img);
                mentoKing_textView_hashTag01.setText(mentoKing.h_tag1);
                mentoKing_textView_hashTag02.setText(mentoKing.h_tag2);
                mentoKing_textView_hashTag03.setText(mentoKing.h_tag3);
                mentoKing_userNo=mentoKing.userno;


                actKing_textView_name.setText(actKing.name);
                Glide.with(getApplicationContext()).load(actKing.pic).into(actKing_img);
                actKing_textView_hashTag01.setText(actKing.h_tag1);
                actKing_textView_hashTag02.setText(actKing.h_tag2);
                actKing_textView_hashTag03.setText(actKing.h_tag3);
                actKing_userNo=actKing.userno;

                levelKing_textView_name.setText(levelKing.name);
                Glide.with(getApplicationContext()).load(levelKing.pic).into(levelKing_img);
                levelKing_textView_hashTag01.setText(levelKing.h_tag1);
                levelKing_textView_hashTag02.setText(levelKing.h_tag2);
                levelKing_textView_hashTag03.setText(levelKing.h_tag3);
                levelKing_userNo=levelKing.userno;
            }
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        ranking_drawer_layout.closeDrawers();
    }
}
