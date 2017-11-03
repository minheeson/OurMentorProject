package com.ourmentor.ymh.lck.smh.fragment;


import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.ourmentor.ymh.lck.smh.DetailWritingActivity;
import com.ourmentor.ymh.lck.smh.common.ParseDataParseHandler;
import com.ourmentor.ymh.lck.smh.R;
import com.ourmentor.ymh.lck.smh.WriteActivity;
import com.ourmentor.ymh.lck.smh.common.WritingObject;
import com.ourmentor.ymh.lck.smh.common.OurMentorConstant;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class BoardStudyFragment extends Fragment {
    private TabLayout board_study_tab;
    private RecyclerView board_study_recyclerView;
    private String[] stringArray;
    private FloatingActionButton floatingActionButton;
    private AsyncStudyRecyclerViewJSONList  asyncStudyRecyclerViewJSONList;
    private ImageView board_study_allBtn, board_study_questionBtn, board_study_infoBtn , board_study_search_button;
    private String keyword;
    private EditText board_study_searchEditText;
    public BoardStudyFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_board_study, container, false);

        board_study_allBtn = (ImageView)v.findViewById(R.id.board_study_allBtn);
        board_study_questionBtn = (ImageView)v.findViewById(R.id.board_study_questionBtn);
        board_study_infoBtn = (ImageView)v.findViewById(R.id.board_study_infomationBtn);
        board_study_allBtn.setImageResource(R.drawable.board_btn_total_p);
        board_study_questionBtn.setImageResource(R.drawable.board_btn_question_n);
        board_study_infoBtn.setImageResource(R.drawable.board_btn_information_n);
        board_study_searchEditText = (EditText)v.findViewById(R.id.board_study_searchEditText);

        floatingActionButton = (FloatingActionButton)v.findViewById(R.id.board_study_floatingBtn);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
                                                    @Override
                                                    public void onClick(View v) {
                                                        Intent intent = new Intent(getActivity(), WriteActivity.class);
                                                        startActivity(intent);
                                                    }
                                                }
        );

        board_study_tab = (TabLayout) v.findViewById(R.id.board_study_tab);
        keyword="";

        stringArray = new String[7];
        stringArray[0]="인문학";
        stringArray[1]="사회학";
        stringArray[2]="수학";
        stringArray[3]="과학";
        stringArray[4]="공학";
        stringArray[5]="교육정책";
        stringArray[6]="이슈";

        for (int i = 0; i < stringArray.length; i++)
        {
            board_study_tab.addTab(board_study_tab.newTab().setText(stringArray[i]));
        }

        board_study_recyclerView = (RecyclerView) v.findViewById(R.id.board_study_recyclerView);
        board_study_recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        asyncStudyRecyclerViewJSONList = new AsyncStudyRecyclerViewJSONList(stringArray[0],0,keyword);
        asyncStudyRecyclerViewJSONList.execute();

        board_study_tab.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                asyncStudyRecyclerViewJSONList = new AsyncStudyRecyclerViewJSONList(tab.getText().toString(), 0,keyword);
                asyncStudyRecyclerViewJSONList.execute();
                board_study_allBtn.setImageResource(R.drawable.board_btn_total_p);
                board_study_questionBtn.setImageResource(R.drawable.board_btn_question_n);
                board_study_infoBtn.setImageResource(R.drawable.board_btn_information_n);
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });


        keyword = board_study_searchEditText.getText().toString();
        board_study_search_button = (ImageView)v.findViewById(R.id.board_study_search_button);
        board_study_search_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                keyword = board_study_searchEditText.getText().toString();
                asyncStudyRecyclerViewJSONList = new AsyncStudyRecyclerViewJSONList(stringArray[board_study_tab.getSelectedTabPosition()], 0, keyword);
                asyncStudyRecyclerViewJSONList.execute();
                board_study_allBtn.setImageResource(R.drawable.board_btn_total_p);
                board_study_questionBtn.setImageResource(R.drawable.board_btn_question_n);
                board_study_infoBtn.setImageResource(R.drawable.board_btn_information_n);
                board_study_searchEditText.setText("");
            }
        });

        board_study_allBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                board_study_allBtn.setImageResource(R.drawable.board_btn_total_p);
                board_study_questionBtn.setImageResource(R.drawable.board_btn_question_n);
                board_study_infoBtn.setImageResource(R.drawable.board_btn_information_n);
                asyncStudyRecyclerViewJSONList = new AsyncStudyRecyclerViewJSONList(stringArray[board_study_tab.getSelectedTabPosition()], 0, keyword);
                asyncStudyRecyclerViewJSONList.execute();
            }
        });

        board_study_questionBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                board_study_allBtn.setImageResource(R.drawable.board_btn_total_n);
                board_study_questionBtn.setImageResource(R.drawable.board_btn_question_p);
                board_study_infoBtn.setImageResource(R.drawable.board_btn_information_n);
                asyncStudyRecyclerViewJSONList = new AsyncStudyRecyclerViewJSONList(stringArray[board_study_tab.getSelectedTabPosition()], 1, keyword);
                asyncStudyRecyclerViewJSONList.execute();
            }
        });

        board_study_infoBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                board_study_allBtn.setImageResource(R.drawable.board_btn_total_n);
                board_study_questionBtn.setImageResource(R.drawable.board_btn_question_n);
                board_study_infoBtn.setImageResource(R.drawable.board_btn_information_p);
                asyncStudyRecyclerViewJSONList = new AsyncStudyRecyclerViewJSONList(stringArray[board_study_tab.getSelectedTabPosition()], 2, keyword);
                asyncStudyRecyclerViewJSONList.execute();
            }
        });
        return v;
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    public class AsyncStudyRecyclerViewJSONList extends AsyncTask<String, Integer, ArrayList<WritingObject>> {
        ProgressDialog dialog;
        String sub_dir;
        String keyword;
        int category;
        public AsyncStudyRecyclerViewJSONList(String sub_dir, int category, String keyword) {
            this.sub_dir = sub_dir;
            this.category = category;
            this.keyword = keyword;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected ArrayList<WritingObject> doInBackground(String... params) {
                ToServerInfo toServerInfo= new ToServerInfo(sub_dir,category,keyword);
                return toServerInfo.connStart();
        }
        @Override
        protected void onPostExecute(ArrayList<WritingObject> result) {
            if (result != null && result.size() >= 0) {
                BoardStudyRecyclerViewAdapter boardStudyRecyclerViewAdapter = new BoardStudyRecyclerViewAdapter(getContext(), result);
                boardStudyRecyclerViewAdapter.notifyDataSetChanged();
                board_study_recyclerView.setAdapter(boardStudyRecyclerViewAdapter);
            }
        }
    }
    public class BoardStudyRecyclerViewAdapter extends RecyclerView.Adapter<BoardStudyRecyclerViewAdapter.ViewHolder> {
        private Context mContext;
        private ArrayList<WritingObject> item;
        private int b_no;
        private String directory;
        public BoardStudyRecyclerViewAdapter(Context context, ArrayList<WritingObject> item) {
            mContext = context;
            this.item = item;
        }
        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.board_edu_recyclerview, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(final ViewHolder holder, int position) {
            final WritingObject writingObject = item.get(position);
            if(writingObject.category == 1) {
                holder.board_study_Icon.setImageResource(R.drawable.board_list_q);
            } else if(writingObject.category ==2) {
                holder.board_study_Icon.setImageResource(R.drawable.board_list_pen);
            }
            holder.board_study_Title.setText(writingObject.title);
            holder.board_study_Content.setText(writingObject.content);

            holder.board_study_HashTag01.setVisibility(View.VISIBLE);
            holder.board_study_HashTag01.setText("");
            holder.board_study_HashTag02.setVisibility(View.VISIBLE);
            holder.board_study_HashTag02.setText("");
            holder.board_study_HashTag03.setVisibility(View.VISIBLE);
            holder.board_study_HashTag03.setText("");
            if(writingObject.hashTag01.length()>0 && writingObject.hashTag01.charAt(0) == '#')                                            //첫째 해시 OK
            {
                holder.board_study_HashTag01.setText(writingObject.hashTag01);
                if(writingObject.hashTag02.length()>0 && writingObject.hashTag02.charAt(0) == '#')                                        //둘째 해시 OK
                {
                    holder.board_study_HashTag02.setText(writingObject.hashTag02);


                    if(writingObject.hashTag03.length()>0 && writingObject.hashTag03.charAt(0) == '#')                                        //셋째 해시 OK
                    {
                        holder.board_study_HashTag03.setText(writingObject.hashTag03);

                    }
                    else
                    {
                        holder.board_study_HashTag03.setVisibility(View.INVISIBLE);
                    }
                }
                else                                                                                    //둘째 해시 NULl
                {
                    holder.board_study_HashTag03.setVisibility(View.INVISIBLE);
                    if(writingObject.hashTag03.length()>0 && writingObject.hashTag03.charAt(0) == '#')                                        //셋째 해시 OK
                    {
                        holder.board_study_HashTag02.setText(writingObject.hashTag03);

                    }
                }
            } else                                                                                                  //첫째 해시 NULL
            {
                if(writingObject.hashTag02.length()>0 && writingObject.hashTag02.charAt(0) == '#')                                                        //둘째 해시 OK
                {
                    holder.board_study_HashTag01.setText(writingObject.hashTag02);

                    if(writingObject.hashTag03.charAt(0) == '#')
                    {
                        holder.board_study_HashTag02.setText(writingObject.hashTag03);

                    }
                    holder.board_study_HashTag03.setVisibility(View.INVISIBLE);
                }
                else
                {
                    if(writingObject.hashTag03.length()>0 && writingObject.hashTag03.charAt(0) == '#')                                                //셋째해시 OK
                    {
                        holder.board_study_HashTag01.setText(writingObject.hashTag03);
                        holder.board_study_HashTag02.setVisibility(View.INVISIBLE);
                        holder.board_study_HashTag03.setVisibility(View.INVISIBLE);
                    }
                    else
                    {
                        holder.board_study_HashTag01.setVisibility(View.INVISIBLE);
                        holder.board_study_HashTag02.setVisibility(View.INVISIBLE);
                        holder.board_study_HashTag03.setVisibility(View.INVISIBLE);
                    }
                }
            }
            holder.board_study_ReplyNo.setText(String.valueOf(writingObject.reply));
            holder.board_study_LikeNo.setText(String.valueOf(writingObject.like));
            holder.board_study_Category.setText(writingObject.directory);
            holder.board_study_Detail_category.setText(writingObject.detail_directory);
            holder.board_study_Time.setText(writingObject.date);
            holder.mView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    b_no = writingObject.b_no;
                    directory = writingObject.directory;
                    Log.e("b_no", String.valueOf(b_no));
                    Intent intent = new Intent(getActivity(), DetailWritingActivity.class);
                    intent.putExtra("b_no",b_no);
                    intent.putExtra("directory",directory);
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
            public ImageView board_study_Icon;
            public TextView board_study_Title;
            public TextView board_study_Content;
            public TextView board_study_HashTag01;
            public TextView board_study_HashTag02;
            public TextView board_study_HashTag03;
            public TextView board_study_Reply;
            public TextView board_study_ReplyNo;
            public TextView board_study_Like;
            public TextView board_study_LikeNo;
            public TextView board_study_Category;
            public TextView board_study_Detail_category;
            public TextView board_study_Time;


            public ViewHolder(View itemView) {
                super(itemView);
                mView = itemView;
                board_study_Icon = (ImageView) itemView.findViewById(R.id.board_edu_Icon);
                board_study_Title = (TextView) itemView.findViewById(R.id.board_edu_Title);
                board_study_Content = (TextView) itemView.findViewById(R.id.board_edu_Content);
                board_study_HashTag01 = (TextView) itemView.findViewById(R.id.board_edu_HashTag01);
                board_study_HashTag02 = (TextView) itemView.findViewById(R.id.board_edu_HashTag02);
                board_study_HashTag03 = (TextView) itemView.findViewById(R.id.board_edu_HashTag03);
                board_study_Reply = (TextView) itemView.findViewById(R.id.board_edu_Reply);
                board_study_ReplyNo = (TextView) itemView.findViewById(R.id.board_edu_ReplyNo);
                board_study_Like = (TextView) itemView.findViewById(R.id.board_edu_Like);
                board_study_LikeNo = (TextView) itemView.findViewById(R.id.board_edu_LikeNo);
                board_study_Category = (TextView) itemView.findViewById(R.id.board_edu_Category);
                board_study_Detail_category = (TextView) itemView.findViewById(R.id.board_edu_Detail_category);
                board_study_Time = (TextView) itemView.findViewById(R.id.board_edu_Time);
            }
        }
    }
    public class ToServerInfo{
        HttpURLConnection conn = null;
        BufferedReader fromServer = null;
        ArrayList<WritingObject> writelist = null;
        StringBuilder queryBuf = new StringBuilder();
        String sub_dir;
        int category;
        String keyword;
        public ToServerInfo(String sub_dir,int category, String keyword) {
            this.sub_dir = sub_dir;
            this.category = category;
            this.keyword = keyword;
        }
        public ArrayList<WritingObject> connStart() {
            try {
                queryBuf.append("main_dir=" +"학문")
                        .append("&sub_dir="+sub_dir)
                        .append("&category="+category)
                        .append("&keyword="+keyword);
                Log.e("studybuf", String.valueOf(queryBuf));

                URL targetURL = new URL(OurMentorConstant.TARGET_URL + OurMentorConstant.SEARCH);
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
                        Log.e("jsonBuf", String.valueOf(jsonBuf));
                    }
                    writelist = ParseDataParseHandler.getJSONWriteRequestAllList(jsonBuf);
                    Log.e("size", String.valueOf(writelist.size()));
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
    }
}
