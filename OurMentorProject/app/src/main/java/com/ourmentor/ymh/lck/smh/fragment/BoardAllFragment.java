package com.ourmentor.ymh.lck.smh.fragment;


import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.ourmentor.ymh.lck.smh.DetailWritingActivity;
import com.ourmentor.ymh.lck.smh.MainActivity;
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
public class BoardAllFragment extends Fragment {

    RecyclerView board_all_recyclerView;
    FloatingActionButton floatingActionButton;
    ImageView board_all_allBtn, board_all_questionBtn, board_all_infoBtn, search_button;
    private EditText board_all_searchEditText;
    private String keyword = "";
    private String hashTag;
    Bundle b;
    private int category = 0;
    private int b_no;
    private BoardAllRecyclerViewAdapter boardAllRecyclerViewAdapter;
    ArrayList<WritingObject> item;
    LinearLayoutManager linearLayout;

    public BoardAllFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_board_all, container, false);

        b_no = 0;
        floatingActionButton = (FloatingActionButton) v.findViewById(R.id.board_all_floatingBtn);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
                                                    @Override
                                                    public void onClick(View v) {
                                                        Intent intent = new Intent(getActivity(), WriteActivity.class);
                                                        startActivity(intent);
                                                    }
                                                }
        );


        board_all_recyclerView = (RecyclerView) v.findViewById(R.id.board_all_recyclerView);
        linearLayout = new LinearLayoutManager(getContext());
        board_all_recyclerView.setLayoutManager(linearLayout);
        board_all_recyclerView.setAdapter(boardAllRecyclerViewAdapter);




        board_all_allBtn = (ImageView) v.findViewById(R.id.board_all_allBtn);
        board_all_questionBtn = (ImageView) v.findViewById(R.id.board_all_questionBtn);
        board_all_infoBtn = (ImageView) v.findViewById(R.id.board_all_informationBtn);

        b = getArguments();
        if (b != null) {
            hashTag = b.getString("hashTag");
        }

        keyword = "";
        board_all_allBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                category = 0;

                board_all_allBtn.setImageResource(R.drawable.board_btn_total_p);
                board_all_questionBtn.setImageResource(R.drawable.board_btn_question_n);
                board_all_infoBtn.setImageResource(R.drawable.board_btn_information_n);


                b_no=0;
                item.clear();
                new AsyncAllSearchRecyclerViewJSONList(category, keyword).execute();
                board_all_recyclerView.setAdapter(boardAllRecyclerViewAdapter);
            }
        });

        board_all_questionBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                category = 1;

                board_all_allBtn.setImageResource(R.drawable.board_btn_total_n);
                board_all_questionBtn.setImageResource(R.drawable.board_btn_question_p);
                board_all_infoBtn.setImageResource(R.drawable.board_btn_information_n);


                b_no=0;
                item.clear();
                new AsyncAllSearchRecyclerViewJSONList(category, keyword).execute();
                board_all_recyclerView.setAdapter(boardAllRecyclerViewAdapter);
            }
        });

        board_all_infoBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                category = 2;

                board_all_allBtn.setImageResource(R.drawable.board_btn_total_n);
                board_all_questionBtn.setImageResource(R.drawable.board_btn_question_n);
                board_all_infoBtn.setImageResource(R.drawable.board_btn_information_p);


                b_no=0;
                item.clear();
                new AsyncAllSearchRecyclerViewJSONList(category, keyword).execute();
                board_all_recyclerView.setAdapter(boardAllRecyclerViewAdapter);
            }
        });


        board_all_searchEditText = (EditText) v.findViewById(R.id.board_all_searchEditText);

        search_button = (ImageView) v.findViewById(R.id.board_all_search_button);
        search_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                category = 1;
                keyword = board_all_searchEditText.getText().toString();

                b_no=0;
                new AsyncAllSearchRecyclerViewJSONList(category, keyword).execute();
                board_all_recyclerView.setAdapter(boardAllRecyclerViewAdapter);
                board_all_allBtn.setImageResource(R.drawable.board_btn_total_p);
                board_all_questionBtn.setImageResource(R.drawable.board_btn_question_n);
                board_all_infoBtn.setImageResource(R.drawable.board_btn_information_n);
                board_all_searchEditText.setText("");
            }
        });

        return v;
    }

    @Override
    public void onResume() {
        super.onResume();

        board_all_recyclerView.setLayoutManager(linearLayout);
        board_all_recyclerView.setAdapter(boardAllRecyclerViewAdapter);

        b_no=0;
        item = new ArrayList<>();
        item.clear();
        boardAllRecyclerViewAdapter = new BoardAllRecyclerViewAdapter(getContext(), item);
        if (b != null) {
            hashTag = b.getString("hashTag");
            new AsyncHashTagSearchRecyclerViewJSONList(hashTag).execute();
        } else
            new AsyncAllSearchRecyclerViewJSONList(category, keyword).execute();
    }


    public class BoardAllRecyclerViewAdapter extends RecyclerView.Adapter<BoardAllRecyclerViewAdapter.ViewHolder> {
        private Context mConetext;
        private ArrayList<WritingObject> item;
        private int b_no;
        private String directory;

        public BoardAllRecyclerViewAdapter(Context context, ArrayList<WritingObject> item) {
            mConetext = context;
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
            if (writingObject.category == 1) {
                holder.board_all_Icon.setImageResource(R.drawable.board_list_q);
            } else if (writingObject.category == 2) {
                holder.board_all_Icon.setImageResource(R.drawable.board_list_pen);
            }
            holder.board_all_Title.setText(writingObject.title);
            holder.board_all_Content.setText(writingObject.content);

            if (holder.board_all_HashTag01.getVisibility() == View.INVISIBLE)
                holder.board_all_HashTag01.setVisibility(View.VISIBLE);
            if (holder.board_all_HashTag01.length() > 0)
                holder.board_all_HashTag01.setText("");
            if (holder.board_all_HashTag02.getVisibility() == View.INVISIBLE)
                holder.board_all_HashTag02.setVisibility(View.VISIBLE);
            if (holder.board_all_HashTag02.length() > 0)
                holder.board_all_HashTag02.setText("");
            if (holder.board_all_HashTag03.getVisibility() == View.INVISIBLE)
                holder.board_all_HashTag03.setVisibility(View.VISIBLE);
            if (holder.board_all_HashTag03.length() > 0)
                holder.board_all_HashTag03.setText("");

            if (writingObject.hashTag01.length() > 0 && writingObject.hashTag01.charAt(0) == '#')                                            //첫째 해시 OK
            {
                holder.board_all_HashTag01.setText(writingObject.hashTag01);

                if (writingObject.hashTag02.length() > 0 && writingObject.hashTag02.charAt(0) == '#')                                        //둘째 해시 OK
                {
                    holder.board_all_HashTag02.setText(writingObject.hashTag02);


                    if (writingObject.hashTag03.length() > 0 && writingObject.hashTag03.charAt(0) == '#')                                        //셋째 해시 OK
                    {
                        holder.board_all_HashTag03.setText(writingObject.hashTag03);

                    } else {
                        holder.board_all_HashTag03.setVisibility(View.INVISIBLE);
                    }
                } else                                                                                    //둘째 해시 NULl
                {
                    holder.board_all_HashTag03.setVisibility(View.INVISIBLE);
                    if (writingObject.hashTag03.length() > 0 && writingObject.hashTag03.charAt(0) == '#')                                        //셋째 해시 OK
                    {
                        holder.board_all_HashTag02.setText(writingObject.hashTag03);

                    }
                }
            } else                                                                                                  //첫째 해시 NULL
            {
                if (writingObject.hashTag02.length() > 0 && writingObject.hashTag02.charAt(0) == '#')                                                        //둘째 해시 OK
                {
                    holder.board_all_HashTag01.setText(writingObject.hashTag02);

                    if (writingObject.hashTag03.length() > 0 && writingObject.hashTag03.charAt(0) == '#') {
                        holder.board_all_HashTag02.setText(writingObject.hashTag03);

                    }
                    holder.board_all_HashTag03.setVisibility(View.INVISIBLE);
                } else {
                    if (writingObject.hashTag03.length() > 0 && writingObject.hashTag03.charAt(0) == '#')                                                //셋째해시 OK
                    {
                        holder.board_all_HashTag01.setText(writingObject.hashTag03);
                        holder.board_all_HashTag02.setVisibility(View.INVISIBLE);
                        holder.board_all_HashTag03.setVisibility(View.INVISIBLE);
                    } else {
                        holder.board_all_HashTag01.setVisibility(View.INVISIBLE);
                        holder.board_all_HashTag02.setVisibility(View.INVISIBLE);
                        holder.board_all_HashTag03.setVisibility(View.INVISIBLE);
                    }
                }
            }
            holder.board_all_ReplyNo.setText(String.valueOf(writingObject.reply));
            holder.board_all_LikeNo.setText(String.valueOf(writingObject.like));
            holder.board_all_Category.setText(writingObject.directory);
            holder.board_all_Detail_category.setText(writingObject.detail_directory);
            holder.board_all_Time.setText(writingObject.date);
            holder.mView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    b_no = writingObject.b_no;
                    directory = writingObject.directory;
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
            public ImageView board_all_Icon;
            public TextView board_all_Title;
            public TextView board_all_Content;
            public TextView board_all_HashTag01, board_all_HashTag02, board_all_HashTag03;
            public TextView board_all_Reply;
            public TextView board_all_ReplyNo;
            public TextView board_all_Like;
            public TextView board_all_LikeNo;
            public TextView board_all_Category;
            public TextView board_all_Detail_category;
            public TextView board_all_Time;


            public ViewHolder(View itemView) {
                super(itemView);
                mView = itemView;
                board_all_Icon = (ImageView) itemView.findViewById(R.id.board_edu_Icon);
                board_all_Title = (TextView) itemView.findViewById(R.id.board_edu_Title);
                board_all_Content = (TextView) itemView.findViewById(R.id.board_edu_Content);
                board_all_HashTag01 = (TextView) itemView.findViewById(R.id.board_edu_HashTag01);
                board_all_HashTag02 = (TextView) itemView.findViewById(R.id.board_edu_HashTag02);
                board_all_HashTag03 = (TextView) itemView.findViewById(R.id.board_edu_HashTag03);
                board_all_Reply = (TextView) itemView.findViewById(R.id.board_edu_Reply);
                board_all_ReplyNo = (TextView) itemView.findViewById(R.id.board_edu_ReplyNo);
                board_all_Like = (TextView) itemView.findViewById(R.id.board_edu_Like);
                board_all_LikeNo = (TextView) itemView.findViewById(R.id.board_edu_LikeNo);
                board_all_Category = (TextView) itemView.findViewById(R.id.board_edu_Category);
                board_all_Detail_category = (TextView) itemView.findViewById(R.id.board_edu_Detail_category);
                board_all_Time = (TextView) itemView.findViewById(R.id.board_edu_Time);
            }
        }
    }

    public class AsyncAllSearchRecyclerViewJSONList extends AsyncTask<String, Integer, ArrayList<WritingObject>> {
        ProgressDialog dialog;
        int category;
        String keyword;

        public AsyncAllSearchRecyclerViewJSONList(int i, String s) {
            category = i;
            keyword = s;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected ArrayList<WritingObject> doInBackground(String... params) {
            HttpURLConnection conn = null;
            BufferedReader fromServer = null;
            ArrayList<WritingObject> writelist = null;
            StringBuilder queryBuf = new StringBuilder();

            String result;
            try {

                queryBuf.append("category=" + category)
                        .append("&keyword=" + keyword);
                URL targetURL = new URL(OurMentorConstant.TARGET_URL + OurMentorConstant.SEARCH);
                conn = (HttpURLConnection) targetURL.openConnection();
                conn.setConnectTimeout(10000);
                conn.setReadTimeout(10000);

                Log.e("search", String.valueOf(queryBuf));

                conn.setRequestMethod("POST");
                OutputStream toServer = conn.getOutputStream();
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


        @Override
        protected void onPostExecute(ArrayList<WritingObject> result) {

            if (result != null && result.size() >= 0) {
                if (b_no == 0) {
                    item.clear();
                    board_all_recyclerView.setAdapter(boardAllRecyclerViewAdapter);
                    boardAllRecyclerViewAdapter.notifyDataSetChanged();
                }
                item.addAll(result);
                if(result.size()-1>0) {
                    b_no = result.get(result.size() - 1).b_no;
                }
                boardAllRecyclerViewAdapter.notifyDataSetChanged();

            }
        }
    }

    public class AsyncHashTagSearchRecyclerViewJSONList extends AsyncTask<String, Integer, ArrayList<WritingObject>> {
        ProgressDialog dialog;
        String hashTag;

        public AsyncHashTagSearchRecyclerViewJSONList(String s) {

            hashTag = s;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected ArrayList<WritingObject> doInBackground(String... params) {
            HttpURLConnection conn = null;
            BufferedReader fromServer = null;
            ArrayList<WritingObject> writelist = null;
            StringBuilder queryBuf = new StringBuilder();

            String result;
            try {

                queryBuf.append("category=" + 0)
                        .append("&h_tag=" + hashTag);
                URL targetURL = new URL(OurMentorConstant.TARGET_URL + OurMentorConstant.SEARCH_HASHTAG);
                conn = (HttpURLConnection) targetURL.openConnection();
                conn.setConnectTimeout(10000);
                conn.setReadTimeout(10000);

                Log.e("search", String.valueOf(queryBuf));

                conn.setRequestMethod("POST");
                OutputStream toServer = conn.getOutputStream();
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
                        Log.e("hashtagsearchjsonBuf", String.valueOf(jsonBuf));
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


        @Override
        protected void onPostExecute(ArrayList<WritingObject> result) {

            if (result != null && result.size() >= 0) {
                boardAllRecyclerViewAdapter = new BoardAllRecyclerViewAdapter(getContext(), result);
                boardAllRecyclerViewAdapter.notifyDataSetChanged();
                board_all_recyclerView.setAdapter(boardAllRecyclerViewAdapter);
            }
        }
    }
}
