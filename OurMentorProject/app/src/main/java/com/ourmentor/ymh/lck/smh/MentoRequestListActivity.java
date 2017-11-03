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
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.ourmentor.ymh.lck.smh.common.MentoMenteeObject;
import com.ourmentor.ymh.lck.smh.common.OurMentorConstant;
import com.ourmentor.ymh.lck.smh.common.ParseDataParseHandler;
import com.ourmentor.ymh.lck.smh.fragment.ChattingProfileFragment;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class MentoRequestListActivity extends AppCompatActivity {
    private ImageView mento_request_list_navi_btn;
    private RecyclerView mento_request_list_recyclerView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mento_request_list);

        mento_request_list_recyclerView = (RecyclerView)findViewById(R.id.mento_request_list_recyclerView);
        mento_request_list_recyclerView.setLayoutManager(new LinearLayoutManager(this));

        mento_request_list_navi_btn = (ImageView)findViewById(R.id.mento_request_list_navi_btn);
        mento_request_list_navi_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        new AsyncMentorRecyclerViewJSONList().execute();
    }

    public class MentoRequestRecyclerViewAdapter extends RecyclerView.Adapter<MentoRequestRecyclerViewAdapter.ViewHolder> {
        private ArrayList<MentoMenteeObject> item;

        public MentoRequestRecyclerViewAdapter(Context context) {

        }

        public MentoRequestRecyclerViewAdapter(Context context, ArrayList<MentoMenteeObject> item) {
            this.item = item;
        }

        public class ViewHolder extends RecyclerView.ViewHolder {
            public View mView;
            public CircleImageView mento_request_Img_circleImgView;
            public TextView mento_request_Id_textview;
            public ImageView mento_request_reject , mento_request_accept;



            public ViewHolder(View itemView) {
                super(itemView);
                mView = itemView;
                mento_request_Img_circleImgView = (CircleImageView) itemView.findViewById(R.id.mento_request_Img_circleImgView);
                mento_request_Id_textview = (TextView)itemView.findViewById(R.id.mento_request_Id_textview);
                mento_request_reject =(ImageView)itemView.findViewById(R.id.mento_request_reject);
                mento_request_accept =(ImageView)itemView.findViewById(R.id.mento_request_accept);
            }
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.mento_request_list_recyclerviewitem, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, final int position) {
            final MentoMenteeObject mentoMenteeObject = item.get(position);
            Glide.with(getApplicationContext()).load(mentoMenteeObject.pic).into(holder.mento_request_Img_circleImgView);
            holder.mento_request_Id_textview.setText(mentoMenteeObject.name);
            holder.mView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(MentoRequestListActivity.this, OtherProfileActivity.class);
                    intent.putExtra("userno", mentoMenteeObject.userNo);
                    startActivity(intent);
                }
            });

            holder.mento_request_accept.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    new AsyncAceptMentoJSONList(mentoMenteeObject.userNo).execute();
                }
            });

        }

        @Override
        public int getItemCount() {
            return item.size();
        }
    }


    public class AsyncMentorRecyclerViewJSONList extends AsyncTask<String, Integer, ArrayList<MentoMenteeObject>> {

                    int flag = 0;

                    @Override
                    protected void onPreExecute() {
                        super.onPreExecute();
                    }

                    @Override
                    protected ArrayList<MentoMenteeObject> doInBackground(String... params) {
                        HttpURLConnection conn = null;
                        BufferedReader fromServer = null;
                        ArrayList<MentoMenteeObject> mentoList = null;
                        StringBuffer queryBuf = new StringBuffer();



                        OutputStream toServer = null;

                        String result;

                        try {
                            queryBuf.append("userno="+OurMentorConstant.userNo);
                            URL targetURL = new URL(OurMentorConstant.TARGET_URL + OurMentorConstant.APPLY_LIST);
                            conn = (HttpURLConnection) targetURL.openConnection();
                            conn.setConnectTimeout(10000);
                            conn.setReadTimeout(10000);
                            conn.setRequestMethod("POST");
                            conn.setDoOutput(true);
                            conn.setDoInput(true);
                            toServer = conn.getOutputStream();
                            toServer.write(new String(queryBuf.toString()).getBytes("UTF-8"));
                            toServer.flush();
                            toServer.close();


                            int responseCode = conn.getResponseCode();
                            if (responseCode == HttpURLConnection.HTTP_OK) {
                                fromServer = new BufferedReader(new InputStreamReader(conn.getInputStream(), "UTF-8")); //JSON
                                String line = null;
                                StringBuilder jsonBuf = new StringBuilder();
                                while ((line = fromServer.readLine()) != null) {
                                    jsonBuf.append(line);
                                    Log.e("mentoListjsonBuf", String.valueOf(jsonBuf));
                                }
                                mentoList = ParseDataParseHandler.getJSONMentoApplyListRequestAllList(jsonBuf);
                                Log.e("size", String.valueOf(mentoList.size()));
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
            return mentoList;
        }

        @Override
        protected void onPostExecute(ArrayList<MentoMenteeObject> mentoMenteeObjects) {

            MentoRequestRecyclerViewAdapter mentoRequestRecyclerViewAdapter = new MentoRequestRecyclerViewAdapter(MentoRequestListActivity.this, mentoMenteeObjects);
            mento_request_list_recyclerView.setAdapter(mentoRequestRecyclerViewAdapter);
            mentoRequestRecyclerViewAdapter.notifyDataSetChanged();
        }
    }


    public class AsyncAceptMentoJSONList extends AsyncTask<String, Integer, String> {

        int flag = 0;
        int userNo;
        public AsyncAceptMentoJSONList(int userNo) {
            this.userNo = userNo;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... params) {
            HttpURLConnection conn = null;
            BufferedReader fromServer = null;
            StringBuffer queryBuf = new StringBuffer();



            OutputStream toServer = null;

            String result;

            try {
                queryBuf.append("userno="+OurMentorConstant.userNo)
                        .append("&mentee_no="+userNo);
                URL targetURL = new URL(OurMentorConstant.TARGET_URL + OurMentorConstant.JOIN_MENTO);

                Log.e("aceptLog",String.valueOf(queryBuf));

                conn = (HttpURLConnection) targetURL.openConnection();
                conn.setConnectTimeout(10000);
                conn.setReadTimeout(10000);
                conn.setRequestMethod("POST");
                conn.setDoOutput(true);
                conn.setDoInput(true);
                toServer = conn.getOutputStream();
                toServer.write(new String(queryBuf.toString()).getBytes("UTF-8"));
                toServer.flush();
                toServer.close();


                int responseCode = conn.getResponseCode();
                if (responseCode == HttpURLConnection.HTTP_OK) {
                    fromServer = new BufferedReader(new InputStreamReader(conn.getInputStream(), "UTF-8")); //JSON
                    String line = null;
                    StringBuilder jsonBuf = new StringBuilder();
                    while ((line = fromServer.readLine()) != null) {
                        jsonBuf.append(line);
                        Log.e("aceptMentojsonBuf", String.valueOf(jsonBuf));
                    }
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
            return null;
        }

        @Override
        protected void onPostExecute(String s) {

            new AsyncMentorRecyclerViewJSONList().execute();
        }
    }
}


