package com.ourmentor.ymh.lck.smh.fragment;


import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.ourmentor.ymh.lck.smh.ChattingActivity;
import com.ourmentor.ymh.lck.smh.ChattingRoomActivity;
import com.ourmentor.ymh.lck.smh.OtherProfileActivity;
import com.ourmentor.ymh.lck.smh.R;
import com.ourmentor.ymh.lck.smh.common.OurMentorConstant;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * A simple {@link Fragment} subclass.
 */
public class ChattingProfileFragment extends Fragment {
    private FrameLayout chatting_profile_framelayout;
    private ImageView chatting_profile_btn_chatting, chatting_profile_btn_lprofile;
    private CircleImageView chatting_profile_img;
    private TextView chatting_profile_textView_id, chatting_profile_textView_state;
    Bundle b;
    private int room;
    public ChattingProfileFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_chatting_profile, container, false);
        chatting_profile_img = (CircleImageView)v.findViewById(R.id.chatting_profile_img);
        chatting_profile_textView_id = (TextView)v.findViewById(R.id.chatting_profile_textView_id);
        chatting_profile_textView_state = (TextView)v.findViewById(R.id.chatting_profile_textView_state);
        b = getArguments();

        Glide.with(getContext()).load(b.getString("pic")).into(chatting_profile_img);
        chatting_profile_textView_id.setText(b.getString("mentoId"));
        chatting_profile_textView_state.setText(b.getString("message"));
        chatting_profile_framelayout = (FrameLayout)v.findViewById(R.id.chatting_profile_framelayout);
        chatting_profile_framelayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((ChattingActivity)getActivity()).getSupportFragmentManager().popBackStack();
            }
        });

        chatting_profile_btn_chatting = (ImageView)v.findViewById(R.id.chatting_profile_btn_chatting);
        chatting_profile_btn_chatting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AsyncChatJSONList().execute();

            }
        });

        chatting_profile_btn_lprofile = (ImageView)v.findViewById(R.id.chatting_profile_btn_lprofile);
        chatting_profile_btn_lprofile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), OtherProfileActivity.class);
                intent.putExtra("userno",b.getInt("userNo"));
                startActivity(intent);
                ((ChattingActivity)getActivity()).getSupportFragmentManager().popBackStack();


            }
        });
        return v;
    }

    public class AsyncChatJSONList extends AsyncTask<String, Integer, Integer> {
        ProgressDialog dialog;
        private int roomNo;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Integer doInBackground(String... params) {
            HttpURLConnection conn = null;
            BufferedReader fromServer = null;
            StringBuilder queryBuf = new StringBuilder();
            String result;
            try {
                queryBuf.append("name=" + OurMentorConstant.userName)
                        .append("&bro_name=" + b.getString("mentoId"));

                URL targetURL = new URL(OurMentorConstant.TARGET_URL+OurMentorConstant.CHAT);
                conn = (HttpURLConnection) targetURL.openConnection();
                conn.setConnectTimeout(10000);
                conn.setReadTimeout(10000);
                conn.setDoInput(true);
                conn.setDoOutput(true);
                conn.setRequestMethod("POST");

                OutputStream toServer = conn.getOutputStream();
                toServer.write(new String(queryBuf.toString()).getBytes("UTF-8"));
                toServer.close();

                int responseCode = conn.getResponseCode();
                if (responseCode == HttpURLConnection.HTTP_OK) {
                    fromServer = new BufferedReader(new InputStreamReader(conn.getInputStream(), "UTF-8")); //JSON
                    String line = null;
                    StringBuilder jsonBuf = new StringBuilder();
                    while ((line = fromServer.readLine()) != null) {
                        jsonBuf.append(line);
                        Log.e("ChatjsonBuf", String.valueOf(jsonBuf));
                    }

                    JSONObject responseData = new JSONObject(jsonBuf.toString());
                    roomNo = responseData.getInt("room");
                    Log.e("parseRoom", String.valueOf(roomNo));
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
            return roomNo;
        }

        @Override
        protected void onPostExecute(Integer roomNo) {
            super.onPostExecute(roomNo);
            room =roomNo;
            Intent intent = new Intent(getActivity(), ChattingRoomActivity.class);
            intent.putExtra("userNo", b.getString("mentoId"));                  //상대방꺼
            intent.putExtra("room", room);
            Log.e("intentroom", String.valueOf(room));
            startActivity(intent);
        }
    }
}
