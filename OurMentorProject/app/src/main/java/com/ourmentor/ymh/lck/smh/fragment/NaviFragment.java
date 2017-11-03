package com.ourmentor.ymh.lck.smh.fragment;


import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.ourmentor.ymh.lck.smh.BoardActivity;
import com.ourmentor.ymh.lck.smh.ChattingActivity;
import com.ourmentor.ymh.lck.smh.MainActivity;
import com.ourmentor.ymh.lck.smh.RankingActivity;
import com.ourmentor.ymh.lck.smh.SettingsActivity;
import com.ourmentor.ymh.lck.smh.common.ParseDataParseHandler;
import com.ourmentor.ymh.lck.smh.ProfileActivity;
import com.ourmentor.ymh.lck.smh.common.ProfileObject;
import com.ourmentor.ymh.lck.smh.R;
import com.ourmentor.ymh.lck.smh.common.OurMentorConstant;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * A simple {@link Fragment} subclass.
 */
public class NaviFragment extends Fragment {
    CircleImageView navi_header_image;
    TextView navi_state_msg;
    ListView navi_listview;
    private NaviAdapter mAdapter;
    int array[];
    DrawerLayout mDrawerLayout;
    Activity a;
    public NaviFragment() {
        // Required empty public constructor

    }


    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_navi, container, false);

        array = new int[6];
        Bundle b = getArguments();
        array=b.getIntArray("array");

        navi_listview = (ListView) v.findViewById(R.id.navi_listview);
        navi_header_image = (CircleImageView)v.findViewById(R.id.navi_header_image);
        navi_state_msg = (TextView)v.findViewById(R.id.navi_state_msg);



        navi_listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (position == 0) {
                    //      mDrawerLayout.closeDrawer(Gravity.LEFT);
                    Intent intent = new Intent(getActivity(), MainActivity.class);
                    startActivity(intent);
                } else if (position == 1) {

                    // mDrawerLayout.closeDrawer(Gravity.LEFT);
                    Intent intent = new Intent(getActivity(), ProfileActivity.class);
                    startActivity(intent);
                } else if (position == 2) {

                    //      mDrawerLayout.closeDrawer(Gravity.LEFT);
                    Intent intent = new Intent(getActivity(), BoardActivity.class);
                    startActivity(intent);
                } else if (position ==3)  {

                    Intent intent = new Intent(getActivity(), ChattingActivity.class);
                    startActivity(intent);
                } else if (position ==4) {

                    Intent intent = new Intent(getActivity(), RankingActivity.class);
                    startActivity(intent);
                } else if (position ==5) {

                    Intent intent = new Intent(getActivity(), SettingsActivity.class);
                    startActivity(intent);
                }


                Log.e("ww", String.valueOf(getActivity()));


            }
        });
        mAdapter = new NaviAdapter(getContext());
        navi_listview.setAdapter(mAdapter);

        Log.i("mylog",getActivity().toString());

        initData();
        return v;
    }

    @Override
    public void onResume() {
        super.onResume();
        new AsyncProfileJSONList().execute();
    }

    private void initData() {
        for(int i =0  ; i <array.length; i++) {
            NaviItemData d = new NaviItemData();
            d.navi_listview_imageId = array[i];
            mAdapter.add(d);
        }
    }

    public class NaviAdapter extends BaseAdapter{

        ArrayList<NaviItemData> items = new ArrayList<NaviItemData>();
        Context mContext;

        public NaviAdapter(Context context)
        {
            mContext = context;
        }

        public void add(NaviItemData item)
        {
            items.add(item);
            notifyDataSetChanged();
        }

        public void addAll(List<NaviItemData> items)
        {
            this.items.addAll(items);
            notifyDataSetChanged();
        }
        @Override
        public int getCount() {
            return items.size();
        }

        @Override
        public Object getItem(int position) {
            return items.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            NaviItemView v = new NaviItemView(mContext);
            v.setItemData(items.get(position));

            return v;
        }
    }


    public class AsyncProfileJSONList extends AsyncTask<String, Integer, ProfileObject> {
        ProgressDialog dialog;

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
                URL targetURL = new URL(OurMentorConstant.TARGET_URL + OurMentorConstant.PROFILE+OurMentorConstant.userNo);
                conn = (HttpURLConnection) targetURL.openConnection();
                conn.setConnectTimeout(10000);
                conn.setReadTimeout(10000);



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
            Glide.with(getContext()).load(result.pic).into(navi_header_image);
            navi_state_msg.setText(result.message);
        }
    }
}
