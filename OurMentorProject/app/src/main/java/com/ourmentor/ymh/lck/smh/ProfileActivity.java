package com.ourmentor.ymh.lck.smh;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.ourmentor.ymh.lck.smh.common.OurMentorConstant;
import com.ourmentor.ymh.lck.smh.common.ParseDataParseHandler;
import com.ourmentor.ymh.lck.smh.common.ProfileObject;
import com.ourmentor.ymh.lck.smh.common.WritingObject;
import com.ourmentor.ymh.lck.smh.fragment.NaviFragment;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class ProfileActivity extends AppCompatActivity {


    private DrawerLayout profile_drawer_layout;
    private ProfileRecyclerViewAdapter profileRecyclerViewAdapter;
    private int profile_navi_img_array[];
    private NaviFragment profile_naviFragment;
    private RecyclerView profile_recyclerView;
    private ImageView profile_navi_btn, profile_settings_btn , profile_goToItem;
    private CircleImageView profile_myImg;
    private TextView profile_myName, profile_myStateMsg, profile_myHashTag01, profile_myHashTag02, profile_myHashTag03, profile_levelFrom, profile_levelTo, profile_boardReplyNo, profile_boardLikeNo;
    private int itemNo;
    private ProgressBar profile_levelProgressBar;
    private String name ,userMsg ,pic;
    private String hashTag01, hashTag02, hashTag03;
    private LinearLayout profile_follow;
    private CircleImageView followCircleImageView;
    private TextView apply_count_textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        profile_drawer_layout = (DrawerLayout)findViewById(R.id.profile_drawer_layout);                 //DrawerLayout

        profile_navi_img_array = new int[6];
        profile_navi_img_array[0] = R.drawable.home_g;
        profile_navi_img_array[1] = R.drawable.profile_b;
        profile_navi_img_array[2] = R.drawable.board_g;
        profile_navi_img_array[3] = R.drawable.chatting_g;
        profile_navi_img_array[4] = R.drawable.ranking_g;
        profile_navi_img_array[5] = R.drawable.settings_g;

                                                                                                        //Navigation Img Setting
        Bundle b = new Bundle();
        b.putIntArray("array",profile_navi_img_array);
        profile_naviFragment = new NaviFragment();                                //Fragment in Navigation Create
        profile_naviFragment.setArguments(b);

        android.support.v4.app.FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.add(R.id.profile_navigation_container, profile_naviFragment);
        ft.commit();                                                                                    //Fragment commit

        profile_navi_btn = (ImageView)findViewById(R.id.profile_navi_btn);
        profile_navi_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                profile_drawer_layout.openDrawer(Gravity.LEFT);
            }
        });                                                                                             //Navi Menu Btn Click -> openDrawer

        profile_myImg = (CircleImageView)findViewById(R.id.profileSetting_myImg);
        profile_myName = (TextView)findViewById(R.id.profile_myName);
        profile_myStateMsg = (TextView)findViewById(R.id.profile_myStateMsg);
        profile_myHashTag01 = (TextView)findViewById(R.id.profile_myHashTag01);
        profile_myHashTag01.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ProfileActivity.this, BoardActivity.class);
                intent.putExtra("hashTag",profile_myHashTag01.getText().toString());
                startActivity(intent);
            }
        });
        profile_myHashTag02 = (TextView)findViewById(R.id.profile_myHashTag02);
        profile_myHashTag02.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ProfileActivity.this, BoardActivity.class);
                intent.putExtra("hashTag",profile_myHashTag02.getText().toString());
                startActivity(intent);
            }
        });
        profile_myHashTag03 = (TextView)findViewById(R.id.profile_myHashTag03);
        profile_myHashTag03.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ProfileActivity.this, BoardActivity.class);
                intent.putExtra("hashTag",profile_myHashTag03.getText().toString());
                startActivity(intent);
            }
        });
        profile_levelFrom = (TextView)findViewById(R.id.profile_levelFrom);
        profile_levelTo = (TextView)findViewById(R.id.profile_levelTo);
        profile_boardLikeNo = (TextView)findViewById(R.id.profile_boardLikeNo);
        profile_boardReplyNo = (TextView)findViewById(R.id.profile_boardReplyNo);

        profile_recyclerView = (RecyclerView)findViewById(R.id.profile_recyclerView);
        profile_recyclerView.setLayoutManager(new LinearLayoutManager(this));                           //RecyclerView create


        profile_settings_btn = (ImageView)findViewById(R.id.profile_settings);
        profile_settings_btn.setOnClickListener(new View.OnClickListener() {                                //Profile Setting Btn click -> StartActiviy(ProfileSetting Activity)
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ProfileActivity.this ,ProfileSettingActivity.class);
                intent.putExtra("name",name);
                intent.putExtra("pic",pic);
                intent.putExtra("userMsg",userMsg);
                intent.putExtra("hashTag01",hashTag01);
                intent.putExtra("hashTag02",hashTag02);
                intent.putExtra("hashTag03",hashTag03);
                startActivity(intent);
                }
        });

        profile_goToItem = (ImageView)findViewById(R.id.profile_goToItem);
        profile_goToItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ProfileActivity.this, ItemActivity.class);
                intent.putExtra("itemNo", itemNo);
                startActivity(intent);
            }
        });

        profile_levelProgressBar = (ProgressBar)findViewById(R.id.profile_levelProgressBar);

        followCircleImageView = (CircleImageView)findViewById(R.id.followCircleImageView);

        apply_count_textView = (TextView)findViewById(R.id.apply_count_textView);

        profile_follow = (LinearLayout)findViewById(R.id.profile_follow);
        profile_follow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent =new Intent(ProfileActivity.this, MentoRequestListActivity.class);
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onPause() {
        super.onPause();
        profile_drawer_layout.closeDrawers();
    }                                                                                                   //StartActivity -> onPause -> Navi closed

    @Override
    protected void onResume() {
        super.onResume();
        profile_myHashTag01 = (TextView)findViewById(R.id.profile_myHashTag01);
        profile_myHashTag02 = (TextView)findViewById(R.id.profile_myHashTag02);
        profile_myHashTag03 = (TextView)findViewById(R.id.profile_myHashTag03);
        new AsyncProfileJSONList().execute();

    }

    public class AsyncProfileJSONList extends AsyncTask<String, Integer, ProfileObject> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected ProfileObject doInBackground(String... params) {
            HttpURLConnection conn = null;
            BufferedReader fromServer = null;
            ProfileObject profileObject = null;

            String result;
            try {
                URL targetURL = new URL(OurMentorConstant.TARGET_URL + OurMentorConstant.PROFILE+ OurMentorConstant.userNo);
                conn = (HttpURLConnection) targetURL.openConnection();
                conn.setConnectTimeout(10000);
                conn.setReadTimeout(10000);
                Log.e("profileLog",String.valueOf(targetURL));

                conn.setRequestMethod("GET");

                int responseCode = conn.getResponseCode();
                if (responseCode == HttpURLConnection.HTTP_OK) {
                    fromServer = new BufferedReader(new InputStreamReader(conn.getInputStream(),"UTF-8")); //JSON
                    String line = null;
                    StringBuilder jsonBuf = new StringBuilder();
                    while ((line = fromServer.readLine()) != null) {
                        jsonBuf.append(line);
                        Log.e("jsonBuf", String.valueOf(jsonBuf));
                    }
                    profileObject = ParseDataParseHandler.getJSONProfileRequestAllList(jsonBuf);
                }
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                if (fromServer != null) {
                    try {
                        fromServer.close();
                    } catch (IOException ioe) {
                    }
                }
                if (conn != null)
                    conn.disconnect(); //열은 순서 반대로 닫는다
            }
            return profileObject;
        }

        @Override
        protected void onPostExecute(ProfileObject result) {


            if (result != null ) {
                Glide.with(getApplicationContext()).load(result.pic).into(profile_myImg);
                OurMentorConstant.pic = pic;
                name = result.name;
                pic = result.pic;
                profile_myName.setText(result.name);

                profile_myStateMsg.setText(result.message);
                userMsg = result.message;
                profile_myHashTag01.setText(result.hashTag01);

                hashTag01 =result.hashTag01;
                if(hashTag01.equals("") )
                {
                    profile_myHashTag01.setVisibility(View.GONE);
                }
                else
                {
                    profile_myHashTag01.setVisibility(View.VISIBLE);
                }

                profile_myHashTag02.setText(result.hashTag02);
                hashTag02 =result.hashTag02;
                if(hashTag02.equals(""))
                {
                    profile_myHashTag02.setVisibility(View.GONE);
                }
                else
                {
                    profile_myHashTag02.setVisibility(View.VISIBLE);
                }
                profile_myHashTag03.setText(result.hashTag03);
                hashTag03 =result.hashTag03;
                if(hashTag03.equals(""))
                {
                    profile_myHashTag03.setVisibility(View.GONE);
                } else {
                    profile_myHashTag03.setVisibility(View.VISIBLE);
                }
                profile_levelFrom.setText(String.valueOf(result.level));
                profile_levelTo.setText(String.valueOf(result.level + 1));
                itemNo = result.item;
                profile_levelProgressBar.setMax(result.nextExp);
                profile_levelProgressBar.setProgress(result.exp);


                Glide.with(getApplicationContext()).load(result.mento_pic).into(followCircleImageView);

                if(result.apply_count ==0)
                {
                    apply_count_textView.setVisibility(View.INVISIBLE);
                }
                else
                    apply_count_textView.setText(String.valueOf(result.apply_count));

                profileRecyclerViewAdapter = new ProfileRecyclerViewAdapter(ProfileActivity.this, result.act_log);
                profile_recyclerView.setAdapter(profileRecyclerViewAdapter);
                profileRecyclerViewAdapter.notifyDataSetChanged();
            }

        }
    }

    public class ProfileRecyclerViewAdapter extends RecyclerView.Adapter<ProfileRecyclerViewAdapter.ViewHolder>           //RecyclerView Adapter
    {
        Context mContext;
        ArrayList<WritingObject> item;
        private int b_no;
        private String directory;

        public ProfileRecyclerViewAdapter(ProfileActivity profileActivity, ArrayList<WritingObject> item) {
            mContext = profileActivity;
            this.item = item;
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.profile_recyclerview, parent ,false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {
            final WritingObject writingObject = item.get(position);
            if(writingObject.category == 1 ||writingObject.category ==2) {
                if(writingObject.category ==1) {
                    holder.profile_boardIcon.setImageResource(R.drawable.board_list_q);
                }
                else if(writingObject.category ==2) {
                    holder.profile_boardIcon.setImageResource(R.drawable.board_list_pen);
                }
                holder.profile_boardTitle.setText(writingObject.title);
                holder.profile_boardContent.setText(writingObject.content);
                holder.profile_boardReply.setText("답변");
                holder.profile_boardReplyNo.setText(String.valueOf(writingObject.reply));
                holder.profile_boardLike.setText("좋아요");
                holder.profile_boardLikeNo.setText(String.valueOf(writingObject.like));
                holder.profile_boardCategory.setText(writingObject.directory);
                holder.profile_boardDetail_category.setText(writingObject.detail_directory);

                holder.profile_board_line1.setImageResource(R.drawable.main_list_part);
                holder.profile_board_line2.setImageResource(R.drawable.main_list_part);
                holder.profile_board_line3.setImageResource(R.drawable.main_list_part);

                holder.profile_boardTime.setText(writingObject.date);
                holder.mView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        b_no = writingObject.b_no;
                        directory = writingObject.directory;
                        Intent intent = new Intent(ProfileActivity.this, DetailWritingActivity.class);
                        intent.putExtra("b_no", b_no);
                        intent.putExtra("directory", directory);
                        startActivity(intent);
                    }
                });

            } else if(writingObject.category ==3) {
                holder.profile_boardIcon.setImageResource(R.drawable.uprofile_list_thumb);
                holder.profile_boardTitle.setText(writingObject.title);
                holder.profile_boardContent.setText(writingObject.content);
                holder.profile_boardReply.setText(writingObject.directory+" ,");
                holder.profile_boardReplyNo.setText(writingObject.detail_directory);
                holder.profile_board_line1.setImageResource(R.drawable.main_list_part);
                holder.profile_board_line2.setVisibility(View.INVISIBLE);
                holder.profile_board_line3.setVisibility(View.INVISIBLE);
                holder.profile_boardLike.setText(writingObject.date);
                holder.profile_boardLikeNo.setText(null);
                holder.profile_comma.setText(null);
                holder.profile_boardCategory.setText(null);
                holder.profile_boardDetail_category.setText(null);
                holder.profile_boardTime.setText(null);
                holder.mView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        b_no = writingObject.b_no;
                        directory = writingObject.directory;
                        Intent intent = new Intent(ProfileActivity.this, DetailWritingActivity.class);
                        intent.putExtra("b_no", b_no);
                        intent.putExtra("directory", directory);
                        startActivity(intent);
                    }
                });
            }
        }

        @Override
        public int getItemCount() {
            return item.size();
        }
        public class ViewHolder extends RecyclerView.ViewHolder{
            public View mView;
            public ImageView profile_boardIcon;
            public TextView profile_boardTitle;
            public TextView profile_boardContent;
            public TextView profile_boardReply;
            public TextView profile_boardReplyNo;
            public TextView profile_boardLike;
            public TextView profile_boardLikeNo;
            public TextView profile_boardCategory;
            public TextView profile_boardDetail_category;
            public TextView profile_boardTime;
            public TextView profile_comma;
            public ImageView profile_board_line1;
            public ImageView profile_board_line2;
            public ImageView profile_board_line3;


            public ViewHolder(View itemView) {                                                                                      //View holder
                super(itemView);
                mView = itemView;
                profile_boardIcon = (ImageView)itemView.findViewById(R.id.profile_boardIcon);
                profile_boardTitle = (TextView)itemView.findViewById(R.id.profile_boardTitle);
                profile_boardContent = (TextView)itemView.findViewById(R.id.profile_boardContent);
                profile_boardReply = (TextView)itemView.findViewById(R.id.profile_boardReply);
                profile_boardReplyNo = (TextView)itemView.findViewById(R.id.profile_boardReplyNo);
                profile_boardLike = (TextView)itemView.findViewById(R.id.profile_boardLike);
                profile_boardLikeNo = (TextView)itemView.findViewById(R.id.profile_boardLikeNo);
                profile_boardCategory = (TextView)itemView.findViewById(R.id.profile_boardCategory);
                profile_boardDetail_category =(TextView)itemView.findViewById(R.id.profile_boardDetail_category);
                profile_boardTime = (TextView)itemView.findViewById(R.id.profile_boardTime);
                profile_comma = (TextView)itemView.findViewById(R.id.profile_comma);
                profile_board_line1 = (ImageView)itemView.findViewById(R.id.profile_board_line1);
                profile_board_line2 = (ImageView)itemView.findViewById(R.id.profile_board_line2);
                profile_board_line3 = (ImageView)itemView.findViewById(R.id.profile_board_line3);
            }
        }
    }
}