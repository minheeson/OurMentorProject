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
public class BoardWorkFragment extends Fragment {
    private TabLayout board_work_tab;
    private String stringArray[];
    RecyclerView board_work_recyclerView;
    private AsyncWorkRecyclerViewJSONList  asyncWorkRecyclerViewJSONList;
    private FloatingActionButton floatingActionButton;
    ImageView board_work_allBtn, board_work_questionBtn, board_work_infoBtn ,board_work_search_button;
    private String keyword;
    private EditText board_work_searchEditText;

    public BoardWorkFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_board_work, container, false);

        floatingActionButton = (FloatingActionButton)v.findViewById(R.id.board_work_floatingBtn);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
                                                    @Override
                                                    public void onClick(View v) {
                                                        Intent intent = new Intent(getActivity(), WriteActivity.class);
                                                        startActivity(intent);
                                                    }
                                                }
        );
        board_work_tab = (TabLayout)v.findViewById(R.id.board_work_tab);

        stringArray = new String[3];
        stringArray[0] ="스펙";
        stringArray[1] ="자소서";
        stringArray[2] ="면접";
        for(int i =0; i < stringArray.length; i++) {
            board_work_tab.addTab(board_work_tab.newTab().setText(stringArray[i]));
        }

        board_work_recyclerView = (RecyclerView) v.findViewById(R.id.board_work_recyclerView);
        board_work_recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        keyword = "";
        board_work_searchEditText = (EditText)v.findViewById(R.id.board_work_searchEditText);
        board_work_search_button = (ImageView)v.findViewById(R.id.board_work_search_button);
        board_work_search_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                keyword = board_work_searchEditText.getText().toString();
                asyncWorkRecyclerViewJSONList = new AsyncWorkRecyclerViewJSONList(stringArray[board_work_tab.getSelectedTabPosition()], 0, keyword);
                asyncWorkRecyclerViewJSONList.execute();
                board_work_allBtn.setImageResource(R.drawable.board_btn_total_p);
                board_work_questionBtn.setImageResource(R.drawable.board_btn_question_n);
                board_work_infoBtn.setImageResource(R.drawable.board_btn_information_n);
                board_work_searchEditText.setText("");
            }
        });

        asyncWorkRecyclerViewJSONList = new AsyncWorkRecyclerViewJSONList(stringArray[0],0, keyword);
        asyncWorkRecyclerViewJSONList.execute();

        board_work_tab.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                asyncWorkRecyclerViewJSONList = new AsyncWorkRecyclerViewJSONList(tab.getText().toString(),0, keyword);
                asyncWorkRecyclerViewJSONList.execute();
                board_work_allBtn.setImageResource(R.drawable.board_btn_total_p);
                board_work_questionBtn.setImageResource(R.drawable.board_btn_question_n);
                board_work_infoBtn.setImageResource(R.drawable.board_btn_information_n);
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        board_work_allBtn = (ImageView)v.findViewById(R.id.board_work_allBtn);
        board_work_questionBtn = (ImageView)v.findViewById(R.id.board_work_questionBtn);
        board_work_infoBtn = (ImageView)v.findViewById(R.id.board_work_infomationBtn);

        board_work_allBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                board_work_allBtn.setImageResource(R.drawable.board_btn_total_p);
                board_work_questionBtn.setImageResource(R.drawable.board_btn_question_n);
                board_work_infoBtn.setImageResource(R.drawable.board_btn_information_n);
                asyncWorkRecyclerViewJSONList = new AsyncWorkRecyclerViewJSONList(stringArray[board_work_tab.getSelectedTabPosition()], 0, keyword);
                asyncWorkRecyclerViewJSONList.execute();
            }
        });

        board_work_questionBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                board_work_allBtn.setImageResource(R.drawable.board_btn_total_n);
                board_work_questionBtn.setImageResource(R.drawable.board_btn_question_p);
                board_work_infoBtn.setImageResource(R.drawable.board_btn_information_n);
                asyncWorkRecyclerViewJSONList = new AsyncWorkRecyclerViewJSONList(stringArray[board_work_tab.getSelectedTabPosition()], 1, keyword);
                asyncWorkRecyclerViewJSONList.execute();
            }
        });

        board_work_infoBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                board_work_allBtn.setImageResource(R.drawable.board_btn_total_n);
                board_work_questionBtn.setImageResource(R.drawable.board_btn_question_n);
                board_work_infoBtn.setImageResource(R.drawable.board_btn_information_p);
                asyncWorkRecyclerViewJSONList = new AsyncWorkRecyclerViewJSONList(stringArray[board_work_tab.getSelectedTabPosition()], 2 , keyword);
                asyncWorkRecyclerViewJSONList.execute();
            }
        });
        return v;
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    public class AsyncWorkRecyclerViewJSONList extends AsyncTask<String, Integer, ArrayList<WritingObject>> {
        ProgressDialog dialog;
        String sub_dir;
        int category;
        String keyword;
        public AsyncWorkRecyclerViewJSONList(String sub_dir, int category, String keyword) {
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
                BoardWorkRecyclerViewAdapter boardWorkRecyclerViewAdapter = new BoardWorkRecyclerViewAdapter(getContext(), result);
                boardWorkRecyclerViewAdapter.notifyDataSetChanged();
                board_work_recyclerView.setAdapter(boardWorkRecyclerViewAdapter);
            }
        }
    }
    public class BoardWorkRecyclerViewAdapter extends RecyclerView.Adapter<BoardWorkRecyclerViewAdapter.ViewHolder> {
        private Context mContext;
        private ArrayList<WritingObject> item;
        private int b_no;
        private String directory;
        public BoardWorkRecyclerViewAdapter(Context context, ArrayList<WritingObject> item) {
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
                holder.board_work_Icon.setImageResource(R.drawable.board_list_q);
            } else if(writingObject.category ==2) {
                holder.board_work_Icon.setImageResource(R.drawable.board_list_pen);
            }
            holder.board_work_Title.setText(writingObject.title);
            holder.board_work_Content.setText(writingObject.content);

            holder.board_work_HashTag01.setVisibility(View.VISIBLE);
            holder.board_work_HashTag01.setText("");
            holder.board_work_HashTag02.setVisibility(View.VISIBLE);
            holder.board_work_HashTag02.setText("");
            holder.board_work_HashTag03.setVisibility(View.VISIBLE);
            holder.board_work_HashTag03.setText("");

            if(writingObject.hashTag01.length()>0 && writingObject.hashTag01.charAt(0) == '#')                                            //첫째 해시 OK
            {
                holder.board_work_HashTag01.setText(writingObject.hashTag01);

                if(writingObject.hashTag02.length()>0 && writingObject.hashTag02.charAt(0) == '#')                                        //둘째 해시 OK
                {
                    holder.board_work_HashTag02.setText(writingObject.hashTag02);


                    if(writingObject.hashTag03.length()>0 && writingObject.hashTag03.charAt(0) == '#')                                        //셋째 해시 OK
                    {
                        holder.board_work_HashTag03.setText(writingObject.hashTag03);
                    }
                    else
                    {
                        holder.board_work_HashTag03.setVisibility(View.INVISIBLE);
                    }
                }
                else                                                                                    //둘째 해시 NULl
                {
                    holder.board_work_HashTag03.setVisibility(View.INVISIBLE);
                    if(writingObject.hashTag03.length()>0 && writingObject.hashTag03.charAt(0) == '#')                                        //셋째 해시 OK
                    {
                        holder.board_work_HashTag02.setText(writingObject.hashTag03);
                    }
                }
            } else                                                                                                  //첫째 해시 NULL
            {
                if(writingObject.hashTag02.length()>0 && writingObject.hashTag02.charAt(0) == '#')                                                        //둘째 해시 OK
                {
                    holder.board_work_HashTag01.setText(writingObject.hashTag02);
                    if(writingObject.hashTag03.charAt(0) == '#')
                    {
                        holder.board_work_HashTag02.setText(writingObject.hashTag03);
                    }
                    holder.board_work_HashTag03.setVisibility(View.INVISIBLE);
                }
                else
                {
                    if(writingObject.hashTag03.length()>0 && writingObject.hashTag03.charAt(0) == '#')                                                //셋째해시 OK
                    {
                        holder.board_work_HashTag01.setText(writingObject.hashTag03);
                        holder.board_work_HashTag02.setVisibility(View.INVISIBLE);
                        holder.board_work_HashTag03.setVisibility(View.INVISIBLE);
                    }
                    else
                    {
                        holder.board_work_HashTag01.setVisibility(View.INVISIBLE);
                        holder.board_work_HashTag02.setVisibility(View.INVISIBLE);
                        holder.board_work_HashTag03.setVisibility(View.INVISIBLE);
                    }
                }
            }
            holder.board_work_ReplyNo.setText(String.valueOf(writingObject.reply));
            holder.board_work_LikeNo.setText(String.valueOf(writingObject.like));
            holder.board_work_Category.setText(writingObject.directory);
            holder.board_work_Detail_category.setText(writingObject.detail_directory);
            holder.board_work_Time.setText(writingObject.date);
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
            public ImageView board_work_Icon;
            public TextView board_work_Title;
            public TextView board_work_Content;
            public TextView board_work_HashTag01, board_work_HashTag02, board_work_HashTag03;
            public TextView board_work_Reply;
            public TextView board_work_ReplyNo;
            public TextView board_work_Like;
            public TextView board_work_LikeNo;
            public TextView board_work_Category;
            public TextView board_work_Detail_category;
            public TextView board_work_Time;



            public ViewHolder(View itemView) {
                super(itemView);
                mView = itemView;
                board_work_Icon = (ImageView) itemView.findViewById(R.id.board_edu_Icon);
                board_work_Title = (TextView) itemView.findViewById(R.id.board_edu_Title);
                board_work_Content = (TextView) itemView.findViewById(R.id.board_edu_Content);
                board_work_HashTag01 = (TextView) itemView.findViewById(R.id.board_edu_HashTag01);
                board_work_HashTag02 = (TextView) itemView.findViewById(R.id.board_edu_HashTag02);
                board_work_HashTag03 = (TextView) itemView.findViewById(R.id.board_edu_HashTag03);
                board_work_Reply = (TextView) itemView.findViewById(R.id.board_edu_Reply);
                board_work_ReplyNo = (TextView) itemView.findViewById(R.id.board_edu_ReplyNo);
                board_work_Like = (TextView) itemView.findViewById(R.id.board_edu_Like);
                board_work_LikeNo = (TextView) itemView.findViewById(R.id.board_edu_LikeNo);
                board_work_Category = (TextView) itemView.findViewById(R.id.board_edu_Category);
                board_work_Detail_category = (TextView) itemView.findViewById(R.id.board_edu_Detail_category);
                board_work_Time = (TextView) itemView.findViewById(R.id.board_edu_Time);
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
        public ToServerInfo(String sub_dir, int category, String keyword) {
            this.sub_dir = sub_dir;
            this.category = category;
            this.keyword = keyword;
        }
        public ArrayList<WritingObject> connStart() {
            try {
                queryBuf.append("main_dir=" +"취업")
                        .append("&sub_dir="+sub_dir)
                        .append("&category="+category)
                        .append("&keyword="+keyword);

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
