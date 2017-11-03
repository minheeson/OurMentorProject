package com.ourmentor.ymh.lck.smh.fragment;


import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.media.tv.TvView;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.ourmentor.ymh.lck.smh.ChattingRoomActivity;
import com.ourmentor.ymh.lck.smh.R;
import com.ourmentor.ymh.lck.smh.common.ChattingListObject;
import com.ourmentor.ymh.lck.smh.common.MentoMenteeObject;
import com.ourmentor.ymh.lck.smh.common.OurMentorConstant;
import com.ourmentor.ymh.lck.smh.common.ParseDataParseHandler;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * A simple {@link Fragment} subclass.
 */
public class ChattingListFragment extends Fragment {
    RecyclerView chatting_list_recyclerView;
    private ChattingListRecyclerViewAdapter chatting_list_recyclerViewAdapter;
    private String bro_name;
    private int room;

    public ChattingListFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_chatting_list, container, false);

        chatting_list_recyclerView = (RecyclerView) v.findViewById(R.id.chatting_list_recyclerView);
        chatting_list_recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));


        return v;
    }

    @Override
    public void onResume() {
        super.onResume();
        new AsyncChatListRecyclerViewJSONList().execute();
    }

    public class ChattingListRecyclerViewAdapter extends RecyclerView.Adapter<ChattingListRecyclerViewAdapter.ViewHolder> {
        private ArrayList<ChattingListObject> chattingListObjects;

        public ChattingListRecyclerViewAdapter(Context context) {

        }

        public ChattingListRecyclerViewAdapter(Context context, ArrayList<ChattingListObject> chattingListObjects) {
            this.chattingListObjects = chattingListObjects;
        }

        public class ViewHolder extends RecyclerView.ViewHolder {
            public View mView;
            public CircleImageView chattingroom_circleimageview;
            public TextView chattingroom_name_textview, chattingroom_msg_textview, chattingroom_time_textview;


            public ViewHolder(View itemView) {
                super(itemView);
                mView = itemView;
                chattingroom_circleimageview = (CircleImageView) itemView.findViewById(R.id.chattingroom_circleimageview);
                chattingroom_name_textview = (TextView) itemView.findViewById(R.id.chattingroom_name_textview);
                chattingroom_msg_textview = (TextView) itemView.findViewById(R.id.chattingroom_msg_textview);
                chattingroom_time_textview = (TextView) itemView.findViewById(R.id.chattingroom_time_textview);
            }
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.chatting_list_recyclerview, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(final ViewHolder holder, int position) {
            final ChattingListObject chattingListObject = chattingListObjects.get(position);
            Glide.with(getContext()).load(chattingListObject.pic).into(holder.chattingroom_circleimageview);
            holder.chattingroom_name_textview.setText(chattingListObject.name);
            holder.chattingroom_msg_textview.setText(chattingListObject.last_message);
            holder.chattingroom_time_textview.setText(chattingListObject.reg_date);


            holder.mView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    bro_name = chattingListObject.name;
                    room = chattingListObject.room;
                    Intent intent = new Intent(getActivity(), ChattingRoomActivity.class);
                    intent.putExtra("userNo", bro_name);                  //상대방꺼
                    intent.putExtra("room", room);
                    startActivity(intent);
                }
            });
        }

        @Override
        public int getItemCount() {
            return chattingListObjects.size();
        }
    }


    public class AsyncChatListRecyclerViewJSONList extends AsyncTask<String, Integer, ArrayList<ChattingListObject>> {
        ProgressDialog dialog;
        int flag = 0;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected ArrayList<ChattingListObject> doInBackground(String... params) {
            HttpURLConnection conn = null;
            BufferedReader fromServer = null;
            ArrayList<ChattingListObject> chattingList = null;
            StringBuilder queryBuf = new StringBuilder();

            try {
                queryBuf.append("name=" + OurMentorConstant.userName);
                URL targetURL = new URL(OurMentorConstant.TARGET_URL + OurMentorConstant.CHAT_LIST);
                conn = (HttpURLConnection) targetURL.openConnection();
                conn.setConnectTimeout(10000);
                conn.setReadTimeout(10000);
                conn.setRequestMethod("POST");
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
                        Log.e("mentoListjsonBuf", String.valueOf(jsonBuf));
                    }
                    chattingList = ParseDataParseHandler.getJSONChattingListRequestAllList(jsonBuf);

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
            return chattingList;
        }

        @Override
        protected void onPostExecute(ArrayList<ChattingListObject> chattingListObjects) {
            if (chattingListObjects != null && chattingListObjects.size() >= 0) {
                ChattingListRecyclerViewAdapter chattingListRecyclerViewAdapter = new ChattingListRecyclerViewAdapter(getContext(), chattingListObjects);
                chatting_list_recyclerView.setAdapter(chattingListRecyclerViewAdapter);
                chattingListRecyclerViewAdapter.notifyDataSetChanged();
            }
        }
    }


}
