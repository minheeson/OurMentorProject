package com.ourmentor.ymh.lck.smh;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.PatternMatcher;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.ourmentor.ymh.lck.smh.common.OurMentorConstant;
import com.ourmentor.ymh.lck.smh.common.ParseDataParseHandler;
import com.ourmentor.ymh.lck.smh.common.RankObject;
import com.ourmentor.ymh.lck.smh.common.WritingObject;
import com.ourmentor.ymh.lck.smh.fragment.NaviFragment;
import com.ourmentor.ymh.lck.smh.fragment.ViewPagerFragment;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    //RecyclerView Item
    private DrawerLayout main_drawer_layout;
    private ImageView main_navi_btn, main_write_btn, main_reply_btn;
    private NaviFragment main_naviFragment;
    private ViewPager main_viewPager;
    private MainViewPagerFragmentAdapter mainViewPagerFragmentAdapter;
    private int pageSelectedPosition, pageScrollState;
    private RecyclerView main_recyclerView;
    private final long FINISH_INTERVAL_TIME = 2000;
    private long backPressedTime = 0;
    private ArrayList<RankObject> rankObjects1;
    private ArrayList<WritingObject> item;
    private MainRecyclerViewAdapter mainRecyclerViewAdapter;
    private SwipeRefreshLayout main_refreshLayout;


    private int room, b_no;
    String command , bro_name, main_dir;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Intent intent = getIntent();
        command = intent.getStringExtra("command");

        if (command != null) {
            if (command.equals("chatting"))
            {
                room = intent.getIntExtra("room", 0);
                bro_name = intent.getStringExtra("bro_name");
                Intent chattingPushIntent = new Intent(this, ChattingRoomActivity.class);
                chattingPushIntent.putExtra("room", room);
                chattingPushIntent.putExtra("userNo",bro_name);
                startActivity(chattingPushIntent);
            } else if (command.equals("mentoaccept")) {
                Intent mentoAcceptPushIntent = new Intent(this, ChattingActivity.class);
                startActivity(mentoAcceptPushIntent);

            } else if (command.equals("profile")) {
                //mento //프로필
                Intent profilePushIntent = new Intent(this, ProfileActivity.class);
                startActivity(profilePushIntent);

            } else if (command.equals("board")) {

                b_no = intent.getIntExtra("b_no", 0);
                main_dir = intent.getStringExtra("main_dir");
                Intent boardPushIntent = new Intent(this, DetailWritingActivity.class);
                boardPushIntent.putExtra("b_no",b_no);
                boardPushIntent.putExtra("directory",main_dir);

                startActivity(boardPushIntent);

            }
        }


        main_recyclerView = (RecyclerView) findViewById(R.id.main_recyclerView);
        final LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);

        main_recyclerView.setLayoutManager(linearLayoutManager);//RecyclerView Create
        main_recyclerView.setAdapter(mainRecyclerViewAdapter);


        main_refreshLayout = (SwipeRefreshLayout) findViewById(R.id.main_refreshLayout);
        main_refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {

                new AsyncMainRecyclerViewJSONList().execute();
                main_refreshLayout.setRefreshing(false);
            }
        });

        main_viewPager = (ViewPager) findViewById(R.id.main_viewPager);


        new AsyncRankingJSONList().execute();


        main_drawer_layout = (DrawerLayout) findViewById(R.id.main_drawer_layout);
        int main_navi_img_array[] = new int[6];
        main_navi_img_array[0] = R.drawable.home_b;
        main_navi_img_array[1] = R.drawable.profile_g;
        main_navi_img_array[2] = R.drawable.board_g;
        main_navi_img_array[3] = R.drawable.chatting_g;
        main_navi_img_array[4] = R.drawable.ranking_g;
        main_navi_img_array[5] = R.drawable.settings_g;


        main_naviFragment = new NaviFragment();                                            //Navi Settirng
        Bundle b = new Bundle();
        b.putIntArray("array", main_navi_img_array);
        main_naviFragment.setArguments(b);

        android.support.v4.app.FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.add(R.id.main_navigation_container, main_naviFragment);
        ft.commit();                                                                                          //Navi Commit


        main_navi_btn = (ImageView) findViewById(R.id.main_navi_btn);
        main_navi_btn.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                main_drawer_layout.openDrawer(Gravity.LEFT);
                return true;
            }
        });                                                                                                 //Navi menu Event

        main_write_btn = (ImageView) findViewById(R.id.main_write_btn);
        main_write_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, WriteActivity.class);
                startActivity(intent);
            }
        });                                                                                                 //WriteBtn Event ->WriteActivity

        main_reply_btn = (ImageView) findViewById(R.id.main_reply_btn);
        main_reply_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, BoardActivity.class);
                startActivity(intent);
            }
        });                                                                                                 //ReplyBtn Event ->BoardActivity
    }


    @Override
    protected void onResume() {
        super.onResume();
        new AsyncMainRecyclerViewJSONList().execute();
    }

    @Override
    public void onBackPressed() {                                                                           //BackPressed Event
        long currentTime = System.currentTimeMillis();
        long intervalTitme = currentTime - backPressedTime;

        if (0 <= intervalTitme && FINISH_INTERVAL_TIME >= intervalTitme) {
            super.onBackPressed();
        } else {
            backPressedTime = currentTime;
            Toast.makeText(getApplicationContext(), "뒤로 한번더 누르면 종료됩니다.", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onPause() {                                                                              //Intent -> onPause -> Navi closed
        super.onPause();
        main_drawer_layout.closeDrawers();
    }

    public class MainViewPagerFragmentAdapter extends FragmentPagerAdapter                                  //View Pager Adapter
    {
        private static final int ITEM_COUNT = 3;

        public MainViewPagerFragmentAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {

            Bundle b;
            if (rankObjects1 != null && rankObjects1.size() >= 3) {
                switch (position) {
                    case 0:
                        b = new Bundle();
                        b.putParcelable("object", rankObjects1.get(0));
                        b.putString("title", "레벨왕");
                        ViewPagerFragment levelFragment = new ViewPagerFragment();
                        levelFragment.setArguments(b);
                        return levelFragment;

                    case 1:
                        b = new Bundle();
                        b.putParcelable("object", rankObjects1.get(1));
                        b.putString("title", "멘토왕");
                        ViewPagerFragment mentorFragment = new ViewPagerFragment();
                        mentorFragment.setArguments(b);
                        return mentorFragment;

                    case 2:
                        b = new Bundle();
                        b.putParcelable("object", rankObjects1.get(2));
                        b.putString("title", "활동왕");
                        ViewPagerFragment actFragment = new ViewPagerFragment();
                        actFragment.setArguments(b);
                        return actFragment;
                }

            } else {
                switch (position) {
                    case 0:
                        b = new Bundle();
                        b.putParcelable("object", new RankObject());
                        b.putString("title", "레벨왕");
                        ViewPagerFragment levelFragment = new ViewPagerFragment();
                        levelFragment.setArguments(b);
                        return levelFragment;

                    case 1:
                        b = new Bundle();
                        b.putParcelable("object", new RankObject());
                        b.putString("title", "멘토왕");
                        ViewPagerFragment mentorFragment = new ViewPagerFragment();
                        mentorFragment.setArguments(b);
                        return mentorFragment;

                    case 2:
                        b = new Bundle();
                        b.putParcelable("object", new RankObject());
                        b.putString("title", "활동왕");
                        ViewPagerFragment actFragment = new ViewPagerFragment();
                        actFragment.setArguments(b);
                        return actFragment;
                }
            }
            return null;
        }

        @Override
        public int getCount() {
            return ITEM_COUNT;
        }
    }


    public class AsyncMainRecyclerViewJSONList extends AsyncTask<String, Integer, ArrayList<WritingObject>> {
        ProgressDialog dialog;
        int flag = 0;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected ArrayList<WritingObject> doInBackground(String... params) {
            HttpURLConnection conn = null;
            BufferedReader fromServer = null;
            ArrayList<WritingObject> writelist = null;
            StringBuffer queryBuf = null;
            OutputStream toServer = null;
            StringBuilder jsonBuf = null;
            String result;

            try {
                queryBuf = new StringBuffer();
                queryBuf.append("category=" + 0);
                URL targetURL = new URL(OurMentorConstant.TARGET_URL + OurMentorConstant.WRITE_ALL_PATH);
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
                    jsonBuf = new StringBuilder();
                    while ((line = fromServer.readLine()) != null) {
                        jsonBuf.append(line);
                        Log.e("MainRecyclerViewjsonBuf", String.valueOf(jsonBuf));
                    }
                    writelist = ParseDataParseHandler.getJSONWriteRequestAllList(jsonBuf);
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
            return writelist;
        }


        @Override
        protected void onPostExecute(ArrayList<WritingObject> result) {
            if (result != null && result.size() >= 0) {
                mainRecyclerViewAdapter = new MainRecyclerViewAdapter(MainActivity.this,result);
                main_recyclerView.setAdapter(mainRecyclerViewAdapter);
                mainRecyclerViewAdapter.notifyDataSetChanged();
            }
        }
    }

    public class MainRecyclerViewAdapter extends RecyclerView.Adapter<MainRecyclerViewAdapter.ViewHolder>           //RecyclerView Adapter
    {
        Context mContext;
        ArrayList<WritingObject> item;
        private int b_no;
        private String directory;

        public MainRecyclerViewAdapter(MainActivity mainActivity, ArrayList<WritingObject> item) {
            mContext = mainActivity;
            this.item = item;
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.main_recyclerview, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(final ViewHolder holder, int position) {
            final WritingObject writingObject = item.get(position);


            if (writingObject.category == 1) {
                holder.boardIcon.setImageResource(R.drawable.board_list_q);
            } else if (writingObject.category == 2) {
                holder.boardIcon.setImageResource(R.drawable.board_list_pen);
            }
            holder.boardTitle.setText(writingObject.title);
            holder.boardContent.setText(writingObject.content);

            holder.boardHashTag01.setVisibility(View.VISIBLE);
            holder.boardHashTag01.setText("");
            holder.boardHashTag02.setVisibility(View.VISIBLE);
            holder.boardHashTag02.setText("");
            holder.boardHashTag03.setVisibility(View.VISIBLE);
            holder.boardHashTag03.setText("");

            if (writingObject.hashTag01.length() > 0 && writingObject.hashTag01.charAt(0) == '#') {
                holder.boardHashTag01.setText(writingObject.hashTag01);

                if (writingObject.hashTag02.length() > 0 && writingObject.hashTag02.charAt(0) == '#') {
                    holder.boardHashTag02.setText(writingObject.hashTag02);

                    if (writingObject.hashTag03.length() > 0 && writingObject.hashTag03.charAt(0) == '#')                                        //셋째 해시 OK
                    {
                        holder.boardHashTag03.setText(writingObject.hashTag03);

                    } else {
                        holder.boardHashTag03.setVisibility(View.INVISIBLE);
                    }
                } else                                                                                    //둘째 해시 NULl
                {
                    holder.boardHashTag03.setVisibility(View.INVISIBLE);
                    if (writingObject.hashTag03.length() > 0 && writingObject.hashTag03.charAt(0) == '#')                                        //셋째 해시 OK
                    {
                        holder.boardHashTag02.setText(writingObject.hashTag03);
                    }
                }
            } else                                                                                                  //첫째 해시 NULL
            {
                if (writingObject.hashTag02.length() > 0 && writingObject.hashTag02.charAt(0) == '#')                                                        //둘째 해시 OK
                {
                    holder.boardHashTag01.setText(writingObject.hashTag02);

                    if (writingObject.hashTag03.charAt(0) == '#') {
                        holder.boardHashTag02.setText(writingObject.hashTag03);

                    }
                    holder.boardHashTag03.setVisibility(View.INVISIBLE);
                } else {
                    if (writingObject.hashTag03.length() > 0 && writingObject.hashTag03.charAt(0) == '#')                                                //셋째해시 OK
                    {
                        holder.boardHashTag01.setText(writingObject.hashTag03);
                        holder.boardHashTag02.setVisibility(View.INVISIBLE);
                        holder.boardHashTag03.setVisibility(View.INVISIBLE);
                    } else {
                        holder.boardHashTag01.setVisibility(View.INVISIBLE);
                        holder.boardHashTag02.setVisibility(View.INVISIBLE);
                        holder.boardHashTag03.setVisibility(View.INVISIBLE);
                    }
                }
            }

            holder.boardReplyNo.setText(String.valueOf(writingObject.reply));
            holder.boardLikeNo.setText(String.valueOf(writingObject.like));
            holder.boardCategory.setText(writingObject.directory);
            holder.boardDetail_category.setText(writingObject.detail_directory);
            holder.boardTime.setText(writingObject.date);
            holder.mView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    b_no = writingObject.b_no;
                    directory = writingObject.directory;
                    Intent intent = new Intent(MainActivity.this, DetailWritingActivity.class);
                    intent.putExtra("b_no", b_no);
                    intent.putExtra("directory", directory);
                    startActivity(intent);
                }
            });
        }

        @Override
        public int getItemCount() {
            return item.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder {
            public View mView;
            public ImageView boardIcon;
            public TextView boardTitle;
            public TextView boardContent;
            public TextView boardHashTag01, boardHashTag02, boardHashTag03;
            public TextView boardReply;
            public TextView boardReplyNo;
            public TextView boardLike;
            public TextView boardLikeNo;
            public TextView boardCategory;
            public TextView boardDetail_category;
            public TextView boardTime;


            public ViewHolder(View itemView) {
                super(itemView);
                mView = itemView;
                boardIcon = (ImageView) itemView.findViewById(R.id.boardIcon);
                boardTitle = (TextView) itemView.findViewById(R.id.boardTitle);
                boardContent = (TextView) itemView.findViewById(R.id.boardContent);
                boardHashTag01 = (TextView) itemView.findViewById(R.id.boardHashTag01);
                boardHashTag02 = (TextView) itemView.findViewById(R.id.boardHashTag02);
                boardHashTag03 = (TextView) itemView.findViewById(R.id.boardHashTag03);
                boardReply = (TextView) itemView.findViewById(R.id.boardReply);
                boardReplyNo = (TextView) itemView.findViewById(R.id.boardReplyNo);
                boardLike = (TextView) itemView.findViewById(R.id.boardLike);
                boardLikeNo = (TextView) itemView.findViewById(R.id.boardLikeNo);
                boardCategory = (TextView) itemView.findViewById(R.id.boardCategory);
                boardDetail_category = (TextView) itemView.findViewById(R.id.boardDetail_category);
                boardTime = (TextView) itemView.findViewById(R.id.boardTime);
            }
        }


    }

    public class AsyncRankingJSONList extends AsyncTask<String, Integer, ArrayList<RankObject>> {
        ProgressDialog dialog1;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
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
                rankObjects1 = new ArrayList<>();
                for (int i = 0; i < rankObjects.size(); i++) {
                    rankObjects1.add(rankObjects.get(i));
                }
            }

            mainViewPagerFragmentAdapter = new MainViewPagerFragmentAdapter(getSupportFragmentManager());           //ViewPager Create
            main_viewPager.setAdapter(mainViewPagerFragmentAdapter);                                                //ViewPager Adapter
            main_viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {                           //ViewPager Event
                @Override
                public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                }

                @Override
                public void onPageSelected(int position) {
                    pageSelectedPosition = position;

                }

                @Override
                public void onPageScrollStateChanged(int state) {
                    pageScrollState = state;
                }
            });
        }
    }
}
