package com.ourmentor.ymh.lck.smh.fragment;


import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.ourmentor.ymh.lck.smh.common.MentoMenteeObject;
import com.ourmentor.ymh.lck.smh.common.ParseDataParseHandler;
import com.ourmentor.ymh.lck.smh.R;
import com.ourmentor.ymh.lck.smh.common.OurMentorConstant;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * A simple {@link Fragment} subclass.
 */
public class ChattingMenteeFragment extends Fragment {
    RecyclerView chatting_mentee_recyclerView;

    public ChattingMenteeFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_chatting_mentee, container, false);

        chatting_mentee_recyclerView = (RecyclerView) v.findViewById(R.id.chatting_mentee_recyclerView);
        chatting_mentee_recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        return v;
    }

    @Override
    public void onResume() {
        super.onResume();
        new AsyncMenteeRecyclerViewJSONList().execute();
    }

    public class AsyncMenteeRecyclerViewJSONList extends AsyncTask<String, Integer, ArrayList<MentoMenteeObject>> {
        ProgressDialog dialog;
        int flag = 0;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected ArrayList<MentoMenteeObject> doInBackground(String... params) {
            HttpURLConnection conn = null;
            BufferedReader fromServer = null;
            ArrayList<MentoMenteeObject> menteeList = null;

            String result;

            try {
                URL targetURL = new URL(OurMentorConstant.TARGET_URL + OurMentorConstant.MENTEE_LIST + OurMentorConstant.userNo);
                conn = (HttpURLConnection) targetURL.openConnection();
                conn.setConnectTimeout(10000);
                conn.setReadTimeout(10000);
                conn.setRequestMethod("GET");


                int responseCode = conn.getResponseCode();
                if (responseCode == HttpURLConnection.HTTP_OK) {
                    fromServer = new BufferedReader(new InputStreamReader(conn.getInputStream(), "UTF-8")); //JSON
                    String line = null;
                    StringBuilder jsonBuf = new StringBuilder();
                    while ((line = fromServer.readLine()) != null) {
                        jsonBuf.append(line);
                        Log.e("menteeListjsonBuf", String.valueOf(jsonBuf));
                    }
                    menteeList = ParseDataParseHandler.getJSONMentoListRequestAllList(jsonBuf);
                    Log.e("size", String.valueOf(menteeList.size()));
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
            return menteeList;
        }

        @Override
        protected void onPostExecute(ArrayList<MentoMenteeObject> mentoMenteeObjects) {
            if(mentoMenteeObjects !=null && mentoMenteeObjects.size()>=0) {
                ChattingMenteeRecyclerViewAdapter chattingMentorRecyclerViewAdapter = new ChattingMenteeRecyclerViewAdapter(getContext(), mentoMenteeObjects);
                chatting_mentee_recyclerView.setAdapter(chattingMentorRecyclerViewAdapter);
                chattingMentorRecyclerViewAdapter.notifyDataSetChanged();
            }
        }
    }
    public class ChattingMenteeRecyclerViewAdapter extends RecyclerView.Adapter<ChattingMenteeRecyclerViewAdapter.ViewHolder> {
        private ArrayList<MentoMenteeObject> item;

        public ChattingMenteeRecyclerViewAdapter(Context context) {

        }

        public ChattingMenteeRecyclerViewAdapter(Context context, ArrayList<MentoMenteeObject> item) {
            this.item = item;
        }

        public class ViewHolder extends RecyclerView.ViewHolder {
            public View mView;
            public CircleImageView menteeImg_circleImgView;
            public TextView menteeId_textview;
            public TextView menteeMessage_textview;

            public ViewHolder(View itemView) {
                super(itemView);
                mView = itemView;
                menteeImg_circleImgView = (CircleImageView) itemView.findViewById(R.id.mentoImg_circleImgView);
                menteeId_textview = (TextView)itemView.findViewById(R.id.mentorId_textview);
                menteeMessage_textview = (TextView)itemView.findViewById(R.id.mentorMessage_textview);
            }
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.mento_mentee_recyclerview, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {
            final MentoMenteeObject mentoMenteeObject = item.get(position);
            Glide.with(getContext()).load(mentoMenteeObject.pic).into(holder.menteeImg_circleImgView);
            holder.menteeId_textview.setText(mentoMenteeObject.name);
            holder.menteeMessage_textview.setText(mentoMenteeObject.msg);
            holder.mView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    android.support.v4.app.FragmentTransaction ft = getFragmentManager().beginTransaction();
                    Bundle b = new Bundle();
                    b.putString("pic", mentoMenteeObject.pic);
                    b.putString("mentoId", mentoMenteeObject.name);
                    b.putInt("userNo", mentoMenteeObject.userNo);
                    b.putString("message", mentoMenteeObject.msg);
                    ChattingProfileFragment chattingProfileFragment = new ChattingProfileFragment();
                    chattingProfileFragment.setArguments(b);

                    ft.add(R.id.chatting_profile_container,chattingProfileFragment);
                    ft.addToBackStack(null);
                    ft.commit();
                }
            });
        }

        @Override
        public int getItemCount() {
            return item.size();
        }
    }

}
