package com.ourmentor.ymh.lck.smh;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.ourmentor.ymh.lck.smh.common.OurMentorConstant;
import com.ourmentor.ymh.lck.smh.common.ParseDataParseHandler;
import com.ourmentor.ymh.lck.smh.common.ProfileObject;
import com.ourmentor.ymh.lck.smh.common.WritingObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class OtherProfileActivity extends AppCompatActivity {

    private int userNo;
    private RecyclerView other_profile_recyclerView;
    private CircleImageView otherProfile_myImg;
    private TextView otherProfile_myName, otherProfile_myStateMsg, otherProfile_myHashTag01, otherProfile_myHashTag02, otherProfile_myHashTag03, otherProfile_levelFrom, otherProfile_levelTo;
    private int itemNo;
    private ImageView otherProfile_navi_btn;
    private ImageView otherProfile_goToItem;
    private ProgressBar otherProfile_levelProgressBar;

    private ImageView mentor_btn;
    private int check_mento;
    private int check_apply;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_other_profile);
        Intent intent = getIntent();
        userNo =intent.getIntExtra("userno",0);

        otherProfile_myImg = (CircleImageView)findViewById(R.id.otherProfile_myImg);
        otherProfile_myName = (TextView)findViewById(R.id.otherProfile_myName);
        otherProfile_myStateMsg = (TextView)findViewById(R.id.otherProfile_myStateMsg);
        otherProfile_myHashTag01 = (TextView)findViewById(R.id.otherProfile_myHashTag01);
        otherProfile_myHashTag02 = (TextView)findViewById(R.id.otherProfile_myHashTag02);
        otherProfile_myHashTag03 = (TextView)findViewById(R.id.otherProfile_myHashTag03);
        otherProfile_levelFrom = (TextView)findViewById(R.id.otherProfile_levelFrom);
        otherProfile_levelTo = (TextView)findViewById(R.id.otherProfile_levelTo);
        otherProfile_levelProgressBar = (ProgressBar)findViewById(R.id.otherProfile_levelProgressBar);

        otherProfile_navi_btn = (ImageView)findViewById(R.id.otherProfile_navi_btn);
        otherProfile_navi_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });


        other_profile_recyclerView = (RecyclerView)findViewById(R.id.otherProfile_recyclerView);
        other_profile_recyclerView.setLayoutManager(new LinearLayoutManager(this));

        ///////////////////상대방 프로필 아이템
        otherProfile_goToItem = (ImageView) findViewById(R.id.otherProfile_goToItem);
        otherProfile_goToItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(OtherProfileActivity.this, ItemActivity.class);
                intent.putExtra("itemNo", itemNo);
                startActivity(intent);
            }
        });

        ///////////////////멘토 추가
        mentor_btn = (ImageView)findViewById(R.id.mentor_btn);


        mentor_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(check_apply ==0 && check_mento ==0) {
                    new AsyncTaskJoinMento().execute();
                }
                if(check_apply ==1 && check_mento ==0)
                {
                    new AsyncTaskJoinMento().execute();
                }
                if(check_apply ==1 && check_mento ==1)
                {
                    new AsyncTaskOffMento().execute();
                }

            }
        });
    }



    @Override
    protected void onResume() {
        super.onResume();
        new AsyncOtherProfileJSONList().execute();
    }

    public class AsyncOtherProfileJSONList extends AsyncTask<String, Integer, ProfileObject> {
        ProgressDialog dialog1;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        synchronized protected ProfileObject doInBackground(String... params) {
            HttpURLConnection conn1 = null;
            BufferedReader fromServer1 = null;
            ProfileObject profileObject1 = null;

            String result;
            try {
                URL targetURL1 = new URL(OurMentorConstant.TARGET_URL + OurMentorConstant.OTHER_PROFILE+OurMentorConstant.userNo+"/"+userNo);
                conn1 = (HttpURLConnection) targetURL1.openConnection();
                conn1.setConnectTimeout(10000);
                conn1.setReadTimeout(10000);


                conn1.setRequestMethod("GET");

                int responseCode = conn1.getResponseCode();
                if (responseCode == HttpURLConnection.HTTP_OK) {
                    fromServer1 = new BufferedReader(new InputStreamReader(conn1.getInputStream(),"UTF-8")); //JSON
                    String line = null;
                    StringBuilder jsonBuf = new StringBuilder();
                    while ((line = fromServer1.readLine()) != null) {
                        jsonBuf.append(line);
                        Log.e("otherProfilejsonBuf", String.valueOf(jsonBuf));
                    }
                    profileObject1 = ParseDataParseHandler.getJSONOtherProfileRequestAllList(jsonBuf);
                }
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                if (fromServer1 != null) {
                    try {
                        fromServer1.close();
                    }
                    catch (IOException ioe)
                    {

                    }
                }
                if (conn1 != null) {
                    conn1.disconnect(); //열은 순서 반대로 닫는다
                }
            }
            return profileObject1;
        }

        @Override
        protected void onPostExecute(ProfileObject result) {
            if (result != null ) {
                Glide.with(getApplicationContext()).load(result.pic).into(otherProfile_myImg);
                otherProfile_myName.setText(result.name);
                otherProfile_myStateMsg.setText(result.message);
                otherProfile_myHashTag01.setText(result.hashTag01);
                otherProfile_myHashTag02.setText(result.hashTag02);
                otherProfile_myHashTag03.setText(result.hashTag03);
                otherProfile_levelFrom.setText(String.valueOf(result.level));
                otherProfile_levelTo.setText(String.valueOf(result.level + 1));
                itemNo = result.item;

                check_mento = result.check_mento;
                check_apply = result.check_apply;

                otherProfile_levelProgressBar.setMax(result.nextExp);
                otherProfile_levelProgressBar.setProgress(result.exp);

                Log.e("check_apply",String.valueOf(check_apply));

                if(check_apply == 0 && check_mento ==0)
                {
                    mentor_btn.setImageResource(R.drawable.oprofile_cnt_btn_mentor_n);
                }
                else if(check_apply == 1 && check_mento ==0)
                {
                    mentor_btn.setImageResource(R.drawable.mento_request);
                }
                else if(check_mento ==1 && check_apply ==1)
                {
                    mentor_btn.setImageResource(R.drawable.oprofile_cnt_btn_mentor_p);
                }



                OtherProfileRecyclerViewAdapter otherProfileRecyclerViewAdapter = new OtherProfileRecyclerViewAdapter(OtherProfileActivity.this, result.act_log);
                other_profile_recyclerView.setAdapter(otherProfileRecyclerViewAdapter);
                otherProfileRecyclerViewAdapter.notifyDataSetChanged();
            }
        }
    }

    public class OtherProfileRecyclerViewAdapter extends RecyclerView.Adapter<OtherProfileRecyclerViewAdapter.ViewHolder>           //RecyclerView Adapter
    {
        Context mContext;
        ArrayList<WritingObject> item;
        private int b_no;
        private String directory;

        public OtherProfileRecyclerViewAdapter(OtherProfileActivity otherProfileActivity, ArrayList<WritingObject> item) {
            mContext = otherProfileActivity;
            this.item = item;
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.profile_recyclerview, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {
            final WritingObject writingObject = item.get(position);
            if(writingObject.category == 1 ||writingObject.category ==2) {
                if (writingObject.category == 1) {
                    holder.other_profile_boardIcon.setImageResource(R.drawable.board_list_q);
                } else if (writingObject.category == 2) {
                    holder.other_profile_boardIcon.setImageResource(R.drawable.board_list_pen);
                }
                holder.other_profile_boardTitle.setText(writingObject.title);
                holder.other_profile_boardContent.setText(writingObject.content);
                holder.other_profile_boardReply.setText(" 답변");
                holder.other_profile_boardReplyNo.setText(String.valueOf(writingObject.reply));
                Log.e("reply", String.valueOf(writingObject.reply));
                holder.other_profile_boardLike.setText("좋아요");
                holder.other_profile_boardLikeNo.setText(String.valueOf(writingObject.like));
                Log.e("like", String.valueOf(writingObject.like));
                holder.other_profile_boardCategory.setText(writingObject.directory);
                holder.other_profile_boardDetail_category.setText(writingObject.detail_directory);

                holder.other_profile_board_line1.setImageResource(R.drawable.main_list_part);
                holder.other_profile_board_line2.setImageResource(R.drawable.main_list_part);
                holder.other_profile_board_line3.setImageResource(R.drawable.main_list_part);

                holder.other_profile_boardTime.setText(writingObject.date);
                holder.mView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        b_no = writingObject.b_no;
                        directory = writingObject.directory;
                        Intent intent = new Intent(OtherProfileActivity.this, DetailWritingActivity.class);
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
            public ImageView other_profile_boardIcon;
            public TextView other_profile_boardTitle;
            public TextView other_profile_boardContent;
            public TextView other_profile_boardReply;
            public TextView other_profile_boardReplyNo;
            public TextView other_profile_boardLike;
            public TextView other_profile_boardLikeNo;
            public TextView other_profile_boardCategory;
            public TextView other_profile_boardDetail_category;
            public TextView other_profile_boardTime;
            public ImageView other_profile_board_line1,other_profile_board_line2,other_profile_board_line3;


            public ViewHolder(View itemView) {                                                                                      //View holder
                super(itemView);
                mView = itemView;
                other_profile_boardIcon = (ImageView)itemView.findViewById(R.id.profile_boardIcon);
                other_profile_boardTitle = (TextView)itemView.findViewById(R.id.profile_boardTitle);
                other_profile_boardContent = (TextView)itemView.findViewById(R.id.profile_boardContent);
                other_profile_boardReply = (TextView)itemView.findViewById(R.id.profile_boardReply);
                other_profile_boardReplyNo = (TextView)itemView.findViewById(R.id.profile_boardReplyNo);
                other_profile_boardLike = (TextView)itemView.findViewById(R.id.profile_boardLike);
                other_profile_boardLikeNo = (TextView)itemView.findViewById(R.id.profile_boardLikeNo);
                other_profile_boardCategory = (TextView)itemView.findViewById(R.id.profile_boardCategory);
                other_profile_boardDetail_category =(TextView)itemView.findViewById(R.id.profile_boardDetail_category);
                other_profile_boardTime = (TextView)itemView.findViewById(R.id.profile_boardTime);
                other_profile_board_line1 = (ImageView)itemView.findViewById(R.id.profile_board_line1);
                other_profile_board_line2 = (ImageView)itemView.findViewById(R.id.profile_board_line2);
                other_profile_board_line3 = (ImageView)itemView.findViewById(R.id.profile_board_line3);
            }
        }
    }

    public class AsyncTaskJoinMento extends AsyncTask<String, Integer, String> {
        private ProgressDialog progressDialog;

        @Override
        protected String doInBackground(String... params) {
            HttpURLConnection conn = null;
            BufferedReader fromServer = null;   //서버에서 넘어온 JSON을 받아내는 Stream
            StringBuilder queryBuf = new StringBuilder();

            String result = "";

            try {
                queryBuf.append("userno=" + OurMentorConstant.userNo)
                        .append("&mento_no="+userNo);

                URL targetURL = new URL(OurMentorConstant.TARGET_URL + OurMentorConstant.APPLY_MENTO);
                conn = (HttpURLConnection)targetURL.openConnection();
                conn.setConnectTimeout(10000);
                conn.setReadTimeout(10000);
                conn.setDoInput(true);
                conn.setDoOutput(true);

                //반드시 대분자
                conn.setRequestMethod("POST");

                OutputStream toServer = conn.getOutputStream();
                toServer.write(new String(queryBuf.toString()).getBytes("UTF-8"));
                toServer.flush();
                toServer.close();

                int responseCode = conn.getResponseCode();
                if(responseCode == HttpURLConnection.HTTP_OK){
                    fromServer = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                    String oneLine = "";
                    StringBuilder jsonBuf = new StringBuilder();
                    while((oneLine = fromServer.readLine()) !=null ){
                        jsonBuf.append(oneLine);
                    }
                    //OK or FAIL
                } else{
                    //200이 아닐 경우, 처리할 값(보통 다이얼로그를 이용하여 띄운다.
                    //절대 내 앱이 죽어서는 안됨...
                }
            } catch (Exception e) {
                Log.e("JoinMentoError", e.toString());
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
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            progressDialog.dismiss();
            mentor_btn.setImageResource(R.drawable.mento_request);
            new AsyncOtherProfileJSONList().execute();
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = ProgressDialog.show(OtherProfileActivity.this, "서버전송중", "서버로 데이터를 전송중입니다.", true);
        }
    }

    public class AsyncTaskOffMento extends AsyncTask<String, Integer, String> {
        private ProgressDialog progressDialog;

        @Override
        protected String doInBackground(String... params) {
            HttpURLConnection conn = null;
            BufferedReader fromServer = null;   //서버에서 넘어온 JSON을 받아내는 Stream
            StringBuilder queryBuf = new StringBuilder();

            String result = "";

            try {
                queryBuf.append("userno=" + OurMentorConstant.userNo)
                        .append("&mento_no="+userNo);

                URL targetURL = new URL(OurMentorConstant.TARGET_URL + OurMentorConstant.OFF_MENTO);
                conn = (HttpURLConnection)targetURL.openConnection();
                conn.setConnectTimeout(10000);
                conn.setReadTimeout(10000);
                conn.setDoInput(true);
                conn.setDoOutput(true);

                //반드시 대분자
                conn.setRequestMethod("POST");

                OutputStream toServer = conn.getOutputStream();
                toServer.write(new String(queryBuf.toString()).getBytes("UTF-8"));
                toServer.flush();
                toServer.close();

                int responseCode = conn.getResponseCode();
                if(responseCode == HttpURLConnection.HTTP_OK){
                    fromServer = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                    String oneLine = "";
                    StringBuilder jsonBuf = new StringBuilder();
                    while((oneLine = fromServer.readLine()) !=null ){
                        jsonBuf.append(oneLine);
                    }
                    Log.e("offMentoLog",jsonBuf.toString());
                    //OK or FAIL
                } else{
                    //200이 아닐 경우, 처리할 값(보통 다이얼로그를 이용하여 띄운다.
                    //절대 내 앱이 죽어서는 안됨...
                }
            } catch (Exception e) {
                Log.e("JoinMentoError", e.toString());
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
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            progressDialog.dismiss();
            mentor_btn.setImageResource(R.drawable.oprofile_cnt_btn_mentor_n);

        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = ProgressDialog.show(OtherProfileActivity.this, "서버전송중", "서버로 데이터를 전송중입니다.", true);
        }
    }
}
