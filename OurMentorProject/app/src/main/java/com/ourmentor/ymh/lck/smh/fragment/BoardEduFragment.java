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
public class BoardEduFragment extends Fragment {
    private TabLayout board_edu_tab;
    private RecyclerView board_edu_recyclerView;
    private String[] stringArray;
    private FloatingActionButton floatingActionButton;
    private AsyncEduRecyclerViewJSONList asyncEduRecyclerViewJSONList;
    private ImageView board_edu_allBtn, board_edu_questionBtn, board_edu_infoBtn, board_edu_search_button;
    private String keyword = "";
    private EditText board_edu_searchEditText;
    private int category = 0;
    private BoardEduRecyclerViewAdapter boardEduRecyclerViewAdapter;
    private int b_no = 0;
    ArrayList<WritingObject> item;

    public BoardEduFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_board_edu, container, false);

        floatingActionButton = (FloatingActionButton) v.findViewById(R.id.board_edu_floatingBtn);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
                                                    @Override
                                                    public void onClick(View v) {
                                                        Intent intent = new Intent(getActivity(), WriteActivity.class);
                                                        startActivity(intent);
                                                    }
                                                }
        );

        board_edu_tab = (TabLayout) v.findViewById(R.id.board_edu_tab);

        stringArray = new String[5];
        stringArray[0] = "중학교";
        stringArray[1] = "고등학교";
        stringArray[2] = "대학교";
        stringArray[3] = "평생";
        stringArray[4] = "영어";

        for (int i = 0; i < stringArray.length; i++) {
            board_edu_tab.addTab(board_edu_tab.newTab().setText(stringArray[i]));
        }


        board_edu_recyclerView = (RecyclerView) v.findViewById(R.id.board_edu_recyclerView);
        LinearLayoutManager linearLayout = new LinearLayoutManager(getContext());
        board_edu_recyclerView.setLayoutManager(linearLayout);
        board_edu_recyclerView.setAdapter(boardEduRecyclerViewAdapter);




        board_edu_tab.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                category = 0;


                board_edu_allBtn.setImageResource(R.drawable.board_btn_total_p);
                board_edu_questionBtn.setImageResource(R.drawable.board_btn_question_n);
                board_edu_infoBtn.setImageResource(R.drawable.board_btn_information_n);
                b_no = 0;
                item.clear();
                new AsyncEduRecyclerViewJSONList(stringArray[board_edu_tab.getSelectedTabPosition()], category, keyword).execute();
                board_edu_recyclerView.setAdapter(boardEduRecyclerViewAdapter);
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        board_edu_searchEditText = (EditText) v.findViewById(R.id.board_edu_searchEditText);
        keyword = board_edu_searchEditText.getText().toString();

        board_edu_search_button = (ImageView) v.findViewById(R.id.board_edu_search_button);
        board_edu_search_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                keyword = board_edu_searchEditText.getText().toString();

                category = 0;
                b_no = 0;
                item.clear();
                new AsyncEduRecyclerViewJSONList(stringArray[board_edu_tab.getSelectedTabPosition()], category, keyword).execute();
                board_edu_allBtn.setImageResource(R.drawable.board_btn_total_p);
                board_edu_questionBtn.setImageResource(R.drawable.board_btn_question_n);
                board_edu_infoBtn.setImageResource(R.drawable.board_btn_information_n);
                board_edu_searchEditText.setText("");
                board_edu_recyclerView.setAdapter(boardEduRecyclerViewAdapter);
            }
        });

        board_edu_allBtn = (ImageView) v.findViewById(R.id.board_edu_allBtn);
        board_edu_questionBtn = (ImageView) v.findViewById(R.id.board_edu_questionBtn);
        board_edu_infoBtn = (ImageView) v.findViewById(R.id.board_edu_infomationBtn);

        board_edu_allBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                board_edu_allBtn.setImageResource(R.drawable.board_btn_total_p);
                board_edu_questionBtn.setImageResource(R.drawable.board_btn_question_n);
                board_edu_infoBtn.setImageResource(R.drawable.board_btn_information_n);

                category = 0;
                b_no = 0;
                item.clear();
                new AsyncEduRecyclerViewJSONList(stringArray[board_edu_tab.getSelectedTabPosition()], category, keyword).execute();
                board_edu_recyclerView.setAdapter(boardEduRecyclerViewAdapter);
            }
        });

        board_edu_questionBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                board_edu_allBtn.setImageResource(R.drawable.board_btn_total_n);
                board_edu_questionBtn.setImageResource(R.drawable.board_btn_question_p);
                board_edu_infoBtn.setImageResource(R.drawable.board_btn_information_n);

                category = 1;
                b_no = 0;
                item.clear();
                new AsyncEduRecyclerViewJSONList(stringArray[board_edu_tab.getSelectedTabPosition()], category, keyword).execute();
                board_edu_recyclerView.setAdapter(boardEduRecyclerViewAdapter);
            }
        });

        board_edu_infoBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                board_edu_allBtn.setImageResource(R.drawable.board_btn_total_n);
                board_edu_questionBtn.setImageResource(R.drawable.board_btn_question_n);
                board_edu_infoBtn.setImageResource(R.drawable.board_btn_information_p);

                category = 2;
                b_no = 0;
                item.clear();
                new AsyncEduRecyclerViewJSONList(stringArray[board_edu_tab.getSelectedTabPosition()], category, keyword).execute();
                board_edu_recyclerView.setAdapter(boardEduRecyclerViewAdapter);
            }
        });

        return v;
    }

    @Override
    public void onResume() {
        super.onResume();

        b_no=0;
        item = new ArrayList<>();
        boardEduRecyclerViewAdapter = new BoardEduRecyclerViewAdapter(getContext(), item);
        new AsyncEduRecyclerViewJSONList(stringArray[board_edu_tab.getSelectedTabPosition()], category, keyword).execute();
    }

    public class AsyncEduRecyclerViewJSONList extends AsyncTask<String, Integer, ArrayList<WritingObject>> {
        ProgressDialog dialog;
        String sub_dir;
        String keyword;
        int category;

        public AsyncEduRecyclerViewJSONList(String sub_dir, int category, String keyword) {
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
            ToServerInfo toServerInfo = new ToServerInfo(sub_dir, category, keyword);
            return toServerInfo.connStart();
        }

        @Override
        protected void onPostExecute(ArrayList<WritingObject> result) {
            if (result != null && result.size() >= 0) {
                if (b_no == 0) {
                    item.clear();
                    board_edu_recyclerView.setAdapter(boardEduRecyclerViewAdapter);
                    boardEduRecyclerViewAdapter.notifyDataSetChanged();
                }
                item.addAll(result);
                if (result.size() - 1 > 0) {
                    b_no = result.get(result.size() - 1).b_no;
                }
                boardEduRecyclerViewAdapter.notifyDataSetChanged();
            }
        }
    }

    public class BoardEduRecyclerViewAdapter extends RecyclerView.Adapter<BoardEduRecyclerViewAdapter.ViewHolder> {
        private Context mContext;
        private ArrayList<WritingObject> item;
        private int b_no;
        private String directory;

        public BoardEduRecyclerViewAdapter(Context context, ArrayList<WritingObject> item) {
            mContext = context;
            this.item = item;
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.board_edu_recyclerview, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {
            final WritingObject writingObject = item.get(position);
            if (writingObject.category == 1) {
                holder.board_edu_Icon.setImageResource(R.drawable.board_list_q);
            } else if (writingObject.category == 2) {
                holder.board_edu_Icon.setImageResource(R.drawable.board_list_pen);
            }
            holder.board_edu_Title.setText(writingObject.title);
            holder.board_edu_Content.setText(writingObject.content);
            holder.board_edu_HashTag01.setVisibility(View.VISIBLE);
            holder.board_edu_HashTag01.setText("");
            holder.board_edu_HashTag02.setVisibility(View.VISIBLE);
            holder.board_edu_HashTag02.setText("");
            holder.board_edu_HashTag03.setVisibility(View.VISIBLE);
            holder.board_edu_HashTag03.setText("");
            if (writingObject.hashTag01.length() > 0 && writingObject.hashTag01.charAt(0) == '#')                                            //첫째 해시 OK
            {
                holder.board_edu_HashTag01.setText(writingObject.hashTag01);

                if (writingObject.hashTag02.length() > 0 && writingObject.hashTag02.charAt(0) == '#')                                        //둘째 해시 OK
                {
                    holder.board_edu_HashTag02.setText(writingObject.hashTag02);


                    if (writingObject.hashTag03.length() > 0 && writingObject.hashTag03.charAt(0) == '#')                                        //셋째 해시 OK
                    {
                        holder.board_edu_HashTag03.setText(writingObject.hashTag03);

                    } else {
                        holder.board_edu_HashTag03.setVisibility(View.INVISIBLE);
                    }
                } else                                                                                    //둘째 해시 NULl
                {
                    holder.board_edu_HashTag03.setVisibility(View.INVISIBLE);
                    if (writingObject.hashTag03.length() > 0 && writingObject.hashTag03.charAt(0) == '#')                                        //셋째 해시 OK
                    {
                        holder.board_edu_HashTag02.setText(writingObject.hashTag03);

                    }
                }
            } else                                                                                                  //첫째 해시 NULL
            {
                if (writingObject.hashTag02.length() > 0 && writingObject.hashTag02.charAt(0) == '#')                                                        //둘째 해시 OK
                {
                    holder.board_edu_HashTag01.setText(writingObject.hashTag02);

                    if (writingObject.hashTag03.length() > 0 && writingObject.hashTag03.charAt(0) == '#') {
                        holder.board_edu_HashTag02.setText(writingObject.hashTag03);
                    }
                    holder.board_edu_HashTag03.setVisibility(View.INVISIBLE);
                } else {
                    if (writingObject.hashTag03.length() > 0 && writingObject.hashTag03.charAt(0) == '#')                                                //셋째해시 OK
                    {
                        holder.board_edu_HashTag01.setText(writingObject.hashTag03);
                        holder.board_edu_HashTag02.setVisibility(View.INVISIBLE);
                        holder.board_edu_HashTag03.setVisibility(View.INVISIBLE);
                    } else {
                        holder.board_edu_HashTag01.setVisibility(View.INVISIBLE);
                        holder.board_edu_HashTag02.setVisibility(View.INVISIBLE);
                        holder.board_edu_HashTag03.setVisibility(View.INVISIBLE);
                    }
                }
            }
            holder.board_edu_ReplyNo.setText(String.valueOf(writingObject.reply));
            holder.board_edu_LikeNo.setText(String.valueOf(writingObject.like));
            holder.board_edu_Category.setText(writingObject.directory);
            holder.board_edu_Detail_category.setText(writingObject.detail_directory);
            holder.board_edu_Time.setText(writingObject.date);
            holder.mView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    b_no = writingObject.b_no;
                    directory = writingObject.directory;
                    Log.e("b_no", String.valueOf(b_no));
                    Intent intent = new Intent(getActivity(), DetailWritingActivity.class);
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
            public ImageView board_edu_Icon;
            public TextView board_edu_Title;
            public TextView board_edu_Content;
            public TextView board_edu_HashTag01, board_edu_HashTag02, board_edu_HashTag03;
            public TextView board_edu_Reply;
            public TextView board_edu_ReplyNo;
            public TextView board_edu_Like;
            public TextView board_edu_LikeNo;
            public TextView board_edu_Category;
            public TextView board_edu_Detail_category;
            public TextView board_edu_Time;


            public ViewHolder(View itemView) {
                super(itemView);
                mView = itemView;
                board_edu_Icon = (ImageView) itemView.findViewById(R.id.board_edu_Icon);
                board_edu_Title = (TextView) itemView.findViewById(R.id.board_edu_Title);
                board_edu_Content = (TextView) itemView.findViewById(R.id.board_edu_Content);
                board_edu_HashTag01 = (TextView) itemView.findViewById(R.id.board_edu_HashTag01);
                board_edu_HashTag02 = (TextView) itemView.findViewById(R.id.board_edu_HashTag02);
                board_edu_HashTag03 = (TextView) itemView.findViewById(R.id.board_edu_HashTag03);
                board_edu_Reply = (TextView) itemView.findViewById(R.id.board_edu_Reply);
                board_edu_ReplyNo = (TextView) itemView.findViewById(R.id.board_edu_ReplyNo);
                board_edu_Like = (TextView) itemView.findViewById(R.id.board_edu_Like);
                board_edu_LikeNo = (TextView) itemView.findViewById(R.id.board_edu_LikeNo);
                board_edu_Category = (TextView) itemView.findViewById(R.id.board_edu_Category);
                board_edu_Detail_category = (TextView) itemView.findViewById(R.id.board_edu_Detail_category);
                board_edu_Time = (TextView) itemView.findViewById(R.id.board_edu_Time);
            }
        }
    }

    public class ToServerInfo {
        HttpURLConnection conn = null;
        BufferedReader fromServer = null;
        ArrayList<WritingObject> writelist = null;
        StringBuilder queryBuf = new StringBuilder();
        String sub_dir;
        String keyword;
        int category;

        public ToServerInfo(String sub_dir, int category, String keyword) {
            this.sub_dir = sub_dir;
            this.category = category;
            this.keyword = keyword;
        }

        public ArrayList<WritingObject> connStart() {
            try {
                queryBuf.append("main_dir=" + "교육")
                        .append("&sub_dir=" + sub_dir)
                        .append("&category=" + category)
                        .append("&keyword=" + keyword);
                Log.e("queryBuf", String.valueOf(queryBuf));
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
