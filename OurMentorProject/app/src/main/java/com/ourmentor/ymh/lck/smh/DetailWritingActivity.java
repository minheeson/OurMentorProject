package com.ourmentor.ymh.lck.smh;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.hardware.input.InputManager;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.ourmentor.ymh.lck.smh.common.LikeObject;
import com.ourmentor.ymh.lck.smh.common.OurMentorConstant;
import com.ourmentor.ymh.lck.smh.common.ParseDataParseHandler;
import com.ourmentor.ymh.lck.smh.common.ReplyObject;
import com.ourmentor.ymh.lck.smh.common.WritingObject;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class DetailWritingActivity extends AppCompatActivity {
    private ImageView detail_boardReply, detail_board_navi_btn, detail_board_settings, detail_boardIcon , detail_boardBottomSheet_enter, detail_board_title, detail_boardLikeImg;
    private TextView  detail_boardTitle, detail_boardId, detail_boardTime, detail_boardContent, detail_boardHashTag01, detail_boardHashTag02, detail_boardHashTag03,
            detail_boardLikeText, detail_boardReplyText ;
    private EditText detail_boardBottomSheet_editText;
    private int b_no;
    private int r_no;
    private String directory;
    private AsyncDetailReadJSONList asyncDetailReadJSONList;
    private RecyclerView detail_boardBottomSheet_RecyclerView;
    private ReplyObject replyObject;
    private WritingObject modifyObject;
    private int userNo;
    private int write_userNo;
    private int category;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_writing);

        detail_boardIcon = (ImageView)findViewById(R.id.detail_boardIcon);
        detail_boardTitle = (TextView)findViewById(R.id.detail_boardTitle);
        detail_boardContent = (TextView)findViewById(R.id.detail_boardContent);
        detail_boardId = (TextView)findViewById(R.id.detail_boardId);
        detail_boardTime = (TextView)findViewById(R.id.detail_boardTime);
        detail_boardHashTag01 = (TextView)findViewById(R.id.detail_boardHashTag01);
        detail_boardHashTag02 = (TextView)findViewById(R.id.detail_boardHashTag02);
        detail_boardHashTag03 = (TextView)findViewById(R.id.detail_boardHashTag03);
        detail_boardLikeText = (TextView)findViewById(R.id.detail_boardLikeText);
        detail_boardReplyText = (TextView)findViewById(R.id.detail_boardReplyText);
        detail_board_title = (ImageView)findViewById(R.id.detail_board_title);
        detail_boardLikeImg = (ImageView)findViewById(R.id.detail_boardLikeImg);


        detail_boardId.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(userNo == OurMentorConstant.userNo)
                {
                    Intent intent = new Intent(DetailWritingActivity.this, ProfileActivity.class);
                    startActivity(intent);
                }
                else if(userNo != OurMentorConstant.userNo)
                {
                    Intent intent = new Intent(DetailWritingActivity.this, OtherProfileActivity.class);
                    intent.putExtra("userno", userNo);
                    startActivity(intent);
                }
            }
        });

        detail_boardLikeImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AsyncLikeJSONList().execute();
                new AsyncDetailReadJSONList().execute();
            }
        });
        Intent intent = getIntent();
        b_no=intent.getIntExtra("b_no", 0);
        directory = intent.getStringExtra("directory");

        if(directory.equals("교육"))
        {
            detail_board_title.setImageResource(R.drawable.board_nav_education_p);
        }
        else if(directory.equals("학문"))
        {
            detail_board_title.setImageResource(R.drawable.board_nav_study_p);
        }
        else if(directory.equals("취업"))
        {
            detail_board_title.setImageResource(R.drawable.board_nav_job_p);
        }

        replyObject = new ReplyObject();

        detail_boardReply = (ImageView)findViewById(R.id.detail_boardReply);
        detail_boardReply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                View view = getLayoutInflater().inflate (R.layout.bottom_sheet, null);

                new AsyncDetailReplyReadJSONList().execute();
                ImageView detail_boardBottomSheet_navi = (ImageView)view.findViewById(R.id.detail_boardBottomSheet_navi);
                detail_boardBottomSheet_editText = (EditText)view.findViewById(R.id.detail_boardBottomSheet_editText);

                detail_boardBottomSheet_enter = (ImageView)view.findViewById(R.id.detail_boardBottomSheet_enter);
                detail_boardBottomSheet_enter.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        replyObject.content = detail_boardBottomSheet_editText.getText().toString();
                        InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                        imm.hideSoftInputFromWindow(detail_boardBottomSheet_enter.getWindowToken(),0);
                        detail_boardBottomSheet_editText.setText("");
                        new AsyncDetailReplyWriteJSONList().execute(replyObject);

                    }
                });
                detail_boardBottomSheet_RecyclerView = (RecyclerView)view.findViewById(R.id.detail_boardBottomSheet_RecyclerView);
                detail_boardBottomSheet_RecyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));

                final Dialog mBottomSheetDialog = new Dialog (DetailWritingActivity.this, R.style.MaterialDialogSheet);
                mBottomSheetDialog.setContentView(view);
                mBottomSheetDialog.setCancelable(true);
                mBottomSheetDialog.getWindow().setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                mBottomSheetDialog.getWindow().setGravity(Gravity.BOTTOM);
                mBottomSheetDialog.show();
                final Dialog.OnDismissListener onDismissListener = new DialogInterface.OnDismissListener() {
                    @Override
                    public void onDismiss(DialogInterface dialog) {
                        dialog.dismiss();
                        new AsyncDetailReadJSONList().execute();
                    }
                };
                mBottomSheetDialog.setOnDismissListener(onDismissListener);
                detail_boardBottomSheet_navi.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mBottomSheetDialog.dismiss();
                    }
                });
            }
        });
        detail_board_navi_btn = (ImageView)findViewById(R.id.detail_board_navi_btn);
        detail_board_navi_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });


        detail_board_settings = (ImageView)findViewById(R.id.detail_board_settings);
        detail_board_settings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                View view = getLayoutInflater().inflate (R.layout.dialog_write_modify_delete, null);
                final ImageView detail_writing_setting_modify = (ImageView)view.findViewById(R.id.detail_writing_setting_modify);
                final ImageView detail_writing_setting_delete = (ImageView)view.findViewById(R.id.detail_writing_setting_delete);
                final Dialog dialog = new Dialog(DetailWritingActivity.this, R.style.dialogTheme);
                dialog.setContentView(view);

                WindowManager.LayoutParams params= dialog.getWindow().getAttributes();
                params.x =500;
                params.y = -1050;

                dialog.getWindow().setAttributes(params);
                dialog.setCancelable(true);
                dialog.show();

                detail_writing_setting_modify.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                        Intent intent = new Intent(DetailWritingActivity.this, ModifyActivity.class);
                        Bundle bundle = new Bundle();
                        bundle.putInt("b_no", modifyObject.b_no);
                        bundle.putInt("category", modifyObject.category);
                        bundle.putString("title", modifyObject.title);
                        bundle.putString("content", modifyObject.content);
                        bundle.putString("hashTag01",modifyObject.hashTag01);
                        bundle.putString("hashTag02",modifyObject.hashTag02);
                        bundle.putString("hashTag03",modifyObject.hashTag03);
                        bundle.putString("main_dir", modifyObject.directory);
                        bundle.putString("sub_dir", modifyObject.detail_directory);
                        intent.putExtra("modifyObject", bundle);
                        startActivity(intent);
                    }
                });

                detail_writing_setting_delete.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                        AsyncDetailDeleteJSONList asyncDetailDeleteJSONList = new AsyncDetailDeleteJSONList();
                        asyncDetailDeleteJSONList.execute();
                        onBackPressed();
                    }
                });
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        new AsyncDetailReadJSONList().execute();
    }

    public class AsyncDetailReadJSONList extends AsyncTask<String, Integer, ArrayList<WritingObject>> {
        ProgressDialog dialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected ArrayList<WritingObject> doInBackground(String... params) {
            HttpURLConnection conn = null;
            BufferedReader fromServer = null;
            ArrayList<WritingObject> readlist = null;
            StringBuilder queryBuf = new StringBuilder();
            String result;

            try {
                queryBuf.append("b_no=" + b_no)
                        .append("&userno="+OurMentorConstant.userNo); //좋아요 체크
                URL targetURL = new URL(OurMentorConstant.TARGET_URL + OurMentorConstant.DETAIL_WRITING);
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
                    fromServer = new BufferedReader(new InputStreamReader(conn.getInputStream(),"UTF-8")); //JSON
                    String line = null;
                    StringBuilder jsonBuf = new StringBuilder();
                    while ((line = fromServer.readLine()) != null) {
                        jsonBuf.append(line);
                        Log.e("DeatailReadbuf", String.valueOf(jsonBuf));
                    }
                    readlist = ParseDataParseHandler.getJSONReadRequestAllList(jsonBuf);
                    Log.e("size", String.valueOf(readlist.size()));
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
                if (conn != null) {
                    conn.disconnect(); //열은 순서 반대로 닫는다
                    Log.e("disconnect", "disconnect");
                }
            }
            modifyObject = readlist.get(0);
            return readlist;
        }


        @Override
        protected void onPostExecute(ArrayList<WritingObject> result) {
            if(result.get(0).category == 1) {
                detail_boardIcon.setImageResource(R.drawable.board_list_q);
            } else if(result.get(0).category ==2) {
                detail_boardIcon.setImageResource(R.drawable.board_list_pen);
            }
            if(result.get(0).yorn == 0)
            {
                detail_boardLikeImg.setImageResource(R.drawable.infoex_btn_favorite_n);
            }else if(result.get(0).yorn == 1)
            {
                detail_boardLikeImg.setImageResource(R.drawable.infoex_btn_favorite_p);
            }
            detail_boardTitle.setText(result.get(0).title);
            detail_boardContent.setText(result.get(0).content);
            detail_boardId.setText(result.get(0).id);

            if(result.get(0).hashTag01.length()>0 && result.get(0).hashTag01.charAt(0) == '#')                                            //첫째 해시 OK
            {
                detail_boardHashTag01.setText(result.get(0).hashTag01);
                detail_boardHashTag01.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(DetailWritingActivity.this, BoardActivity.class);
                        intent.putExtra("hashTag",detail_boardHashTag01.getText().toString());
                        startActivity(intent);
                    }
                });
                if(result.get(0).hashTag02.length()>0 && result.get(0).hashTag02.charAt(0) == '#')                                        //둘째 해시 OK
                {
                    detail_boardHashTag02.setText(result.get(0).hashTag02);
                    detail_boardHashTag02.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent(DetailWritingActivity.this, BoardActivity.class);
                            intent.putExtra("hashTag",detail_boardHashTag02.getText().toString());
                            startActivity(intent);
                        }
                    });

                    if(result.get(0).hashTag03.length()>0 && result.get(0).hashTag03.charAt(0) == '#')                                        //셋째 해시 OK
                    {
                        detail_boardHashTag03.setText(result.get(0).hashTag03);
                        detail_boardHashTag03.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent intent = new Intent(DetailWritingActivity.this, BoardActivity.class);
                                intent.putExtra("hashTag",detail_boardHashTag03.getText().toString());
                                startActivity(intent);
                            }
                        });
                    }
                    else
                    {
                        detail_boardHashTag03.setVisibility(View.INVISIBLE);
                    }
                }
                else                                                                                    //둘째 해시 NULl
                {
                    detail_boardHashTag03.setVisibility(View.INVISIBLE);
                    if(result.get(0).hashTag03.length()>0 && result.get(0).hashTag03.charAt(0) == '#')                                        //셋째 해시 OK
                    {
                        detail_boardHashTag02.setText(result.get(0).hashTag03);
                        detail_boardHashTag02.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent intent = new Intent(DetailWritingActivity.this, BoardActivity.class);
                                intent.putExtra("hashTag",detail_boardHashTag02.getText().toString());
                                startActivity(intent);
                            }
                        });
                    }
                    else
                    {
                        detail_boardHashTag02.setVisibility(View.INVISIBLE);
                    }
                }
            } else                                                                                                  //첫째 해시 NULL
            {
                if(result.get(0).hashTag02.length()>0 && result.get(0).hashTag02.charAt(0) == '#')                                                        //둘째 해시 OK
                {
                    detail_boardHashTag01.setText(result.get(0).hashTag02);
                    detail_boardHashTag01.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent(DetailWritingActivity.this, BoardActivity.class);
                            intent.putExtra("hashTag",detail_boardHashTag01.getText().toString());
                            startActivity(intent);
                        }
                    });
                    if(result.get(0).hashTag03.charAt(0) == '#')
                    {
                        detail_boardHashTag02.setText(result.get(0).hashTag03);
                        detail_boardHashTag02.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent intent = new Intent(DetailWritingActivity.this, BoardActivity.class);
                                intent.putExtra("hashTag",detail_boardHashTag02.getText().toString());
                                startActivity(intent);
                            }
                        });
                    }
                    detail_boardHashTag03.setVisibility(View.INVISIBLE);
                }
                else
                {
                    if(result.get(0).hashTag03.length()>0 && result.get(0).hashTag03.charAt(0) == '#')                                                //셋째해시 OK
                    {
                        detail_boardHashTag01.setText(result.get(0).hashTag03);
                        detail_boardHashTag01.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent intent = new Intent(DetailWritingActivity.this, BoardActivity.class);
                                intent.putExtra("hashTag",detail_boardHashTag01.getText().toString());
                                startActivity(intent);
                            }
                        });
                        detail_boardHashTag02.setVisibility(View.INVISIBLE);
                        detail_boardHashTag03.setVisibility(View.INVISIBLE);
                    }
                    else
                    {
                        detail_boardHashTag01.setVisibility(View.INVISIBLE);
                        detail_boardHashTag02.setVisibility(View.INVISIBLE);
                        detail_boardHashTag03.setVisibility(View.INVISIBLE);
                    }
                }
            }

            detail_boardLikeText.setText(String.valueOf(result.get(0).like));
            detail_boardReplyText.setText(String.valueOf(result.get(0).reply));
            detail_boardTime.setText(result.get(0).date);
            userNo = result.get(0).userNo;

            if(userNo != OurMentorConstant.userNo)
            {
                detail_board_settings.setVisibility(View.INVISIBLE);
            }
            category = result.get(0).category;
            write_userNo = result.get(0).userNo;
        }
    }

    public class AsyncDetailReplyWriteJSONList extends AsyncTask<ReplyObject, Integer, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(ReplyObject... params) {
            ReplyObject param = params[0];
            HttpURLConnection conn = null;
            BufferedReader fromServer = null;
            StringBuilder queryBuf = new StringBuilder();
            String result;
            Log.e("content1",param.content);
            try {
                queryBuf.append("b_no=" + b_no)
                        .append("&name=" + OurMentorConstant.userName)
                        .append("&content=" + param.content)
                        .append("&userno=" + OurMentorConstant.userNo);

                URL targetURL = new URL(OurMentorConstant.TARGET_URL + OurMentorConstant.DETAIL_REPLY_WRITE);
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
                        Log.e("replywritejsonBuf", String.valueOf(jsonBuf));
                    }
                    JSONObject responseData = new JSONObject(jsonBuf.toString());
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
            super.onPostExecute(s);
            new AsyncDetailReplyReadJSONList().execute();
            if (s != null) {
                if (s.equalsIgnoreCase("ok")) {
                    showDialog(1, null);

                } else {
                    showDialog(2, null);
                }
            } else {
                Bundle bundle = new Bundle();
                bundle.putString("message", "안드로이드는 잘못이 없습니다. 서버가 잘못됐습니다.");
                showDialog(2, bundle);
            }
        }
    }

    public class AsyncDetailReplyReadJSONList extends AsyncTask<String, Integer, ArrayList<ReplyObject>> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected ArrayList<ReplyObject> doInBackground(String... params) {
            HttpURLConnection conn = null;
            BufferedReader fromServer = null;
            ArrayList<ReplyObject> replylist = null;
            StringBuilder queryBuf = new StringBuilder();
            String result;

            try {
                queryBuf.append("b_no=" + b_no)
                        .append("&userno="+OurMentorConstant.userNo);
                URL targetURL = new URL(OurMentorConstant.TARGET_URL + OurMentorConstant.DETAIL_REPLY_READ);
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
                    fromServer = new BufferedReader(new InputStreamReader(conn.getInputStream(),"UTF-8")); //JSON
                    String line = null;
                    StringBuilder jsonBuf = new StringBuilder();
                    while ((line = fromServer.readLine()) != null) {
                        jsonBuf.append(line);
                        Log.e("replyreadjsonBuf", String.valueOf(jsonBuf));
                    }
                    replylist = ParseDataParseHandler.getJSONReplyRequestAllList(jsonBuf);
                    Log.e("size", String.valueOf(replylist.size()));
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
            return replylist;
        }
        @Override
        protected void onPostExecute(ArrayList<ReplyObject> result) {
            if (result != null && result.size() >= 0) {
                if(category==1) {
                    QuestionReplyReadRecyclerViewAdapter replyReadRecyclerViewAdapter = new QuestionReplyReadRecyclerViewAdapter(getApplicationContext(), result);
                    replyReadRecyclerViewAdapter.notifyDataSetChanged();
                    detail_boardBottomSheet_RecyclerView.setAdapter(replyReadRecyclerViewAdapter);

                }
                else if(category ==2)
                {
                    InfoReplyReadRecyclerViewAdapter replyReadRecyclerViewAdapter = new InfoReplyReadRecyclerViewAdapter(getApplicationContext(), result);
                    replyReadRecyclerViewAdapter.notifyDataSetChanged();
                    detail_boardBottomSheet_RecyclerView.setAdapter(replyReadRecyclerViewAdapter);
                }
            }
        }
    }

    public class QuestionReplyReadRecyclerViewAdapter extends RecyclerView.Adapter<QuestionReplyReadRecyclerViewAdapter.ViewHolder> {
        private Context mConetext;
        private ArrayList<ReplyObject> item;
        private int b_no;
        int category;
        public QuestionReplyReadRecyclerViewAdapter(Context context, ArrayList<ReplyObject> item) {
            mConetext = context;
            this.item = item;
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.reply_question_recyclerview_item, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(final ViewHolder holder, final int position) {
            final ReplyObject replyObject = item.get(position);
            //holder.reply_circleImageView.setImageResource();
            Glide.with(getApplicationContext()).load(replyObject.pic).into(holder.reply_circleImageView);
            holder.reply_circleImageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    if(replyObject.reply_userno == OurMentorConstant.userNo)
                    {
                        Intent intent = new Intent(DetailWritingActivity.this, ProfileActivity.class);
                        startActivity(intent);
                    }
                    else{
                    Intent intent = new Intent(DetailWritingActivity.this, OtherProfileActivity.class);
                    intent.putExtra("userno", replyObject.reply_userno);
                    Log.e("what", String.valueOf(replyObject.reply_userno));
                    startActivity(intent);
                    }
                }
            });
            holder.reply_name.setText(replyObject.name);
            holder.reply_content.setText(replyObject.content);
            holder.reply_date.setText(replyObject.reg_date);

            if(item.get(position).adopt.yorn ==1)
            {
                holder.adopt_imageView.setImageResource(R.drawable.qcomment_thumbup_p);
                holder.adopt_imageView.setClickable(false);
            }
            else if(item.get(position).adopt.yorn ==0)
            {
                holder.adopt_imageView.setImageResource(R.drawable.qcomment_thumbup_n);}
                holder.adopt_imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(write_userNo == OurMentorConstant.userNo) {
                        r_no = item.get(position).r_no;
                        Log.e("r_no", String.valueOf(r_no));
                        holder.adopt_imageView.setImageResource(R.drawable.qcomment_thumbup_p);
                        new AsyncReplyAdoptJSONList().execute();
                    }
                    else
                    {
                        Toast.makeText(getApplicationContext(),"글 작성자만 채택할수있습니다.",Toast.LENGTH_SHORT).show();
                    }
                }
            });

            if(replyObject.reply_userno == OurMentorConstant.userNo || replyObject.adopt.userNo == OurMentorConstant.userNo) {
                holder.reply_content.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        View view = getLayoutInflater().inflate(R.layout.dialog_reply_modify_delte, null);
                        final ImageView reply_setting_modify = (ImageView) view.findViewById(R.id.reply_setting_modify);
                        final ImageView reply_setting_delete = (ImageView) view.findViewById(R.id.reply_setting_delete);
                        final ImageView reply_setting_cancel = (ImageView) view.findViewById(R.id.reply_setting_cancel);
                        final Dialog dialog = new Dialog(DetailWritingActivity.this, R.style.dialogTheme);
                        r_no = item.get(position).r_no;
                        dialog.setContentView(view);

                        WindowManager.LayoutParams params = dialog.getWindow().getAttributes();

                        dialog.getWindow().setAttributes(params);
                        dialog.setCancelable(true);
                        dialog.show();

                        reply_setting_modify.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                dialog.dismiss();
                                //댓글 수정
                                Toast.makeText(getApplicationContext(), "댓글 수정 구현중입니다", Toast.LENGTH_SHORT).show();
//                            Intent intent = new Intent(DetailWritingActivity.this, ModifyActivity.class);
//                            Bundle bundle = new Bundle();
//                            bundle.putInt("b_no", modifyObject.b_no);
//                            bundle.putInt("category", modifyObject.category);
//                            bundle.putString("title", modifyObject.title);
//                            bundle.putString("content", modifyObject.content);
//                            bundle.putString("directory", modifyObject.directory);
//                            bundle.putString("detail_directory", modifyObject.detail_directory);
//                            intent.putExtra("modifyObject", bundle);
//                            startActivity(intent);
                            }
                        });

                        reply_setting_delete.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                new AsyncReplyDeleteJSONList().execute();
                                dialog.dismiss();
                            }
                        });

                        reply_setting_cancel.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                dialog.dismiss();
                            }
                        });
                    }
                });
            }
        }


        @Override
        public int getItemCount() {
            return item.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder {
            public View mView;
            public CircleImageView reply_circleImageView;
            public TextView reply_name;
            public TextView reply_content;
            public TextView reply_date;
            public ImageView adopt_imageView;


            public ViewHolder(View itemView) {
                super(itemView);
                mView = itemView;

                reply_circleImageView = (CircleImageView)itemView.findViewById(R.id.question_reply_circleImageView);
                reply_name = (TextView)itemView.findViewById(R.id.question_reply_name);
                reply_content = (TextView)itemView.findViewById(R.id.question_reply_content);
                reply_date = (TextView)itemView.findViewById(R.id.question_reply_date);
                adopt_imageView = (ImageView)itemView.findViewById(R.id.adopt_imageView);

            }
        }
    }

    public class InfoReplyReadRecyclerViewAdapter extends RecyclerView.Adapter<InfoReplyReadRecyclerViewAdapter.ViewHolder> {
        private Context mConetext;
        private ArrayList<ReplyObject> item;
        private int b_no;
        private int category;
        public InfoReplyReadRecyclerViewAdapter(Context context, ArrayList<ReplyObject> item) {
            mConetext = context;
            this.item = item;
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.reply_info_recyclerview_item, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, final int position) {
            final ReplyObject replyObject = item.get(position);

            Glide.with(getApplicationContext()).load(replyObject.pic).into(holder.reply_circleImageView);
            holder.reply_circleImageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(replyObject.reply_userno == OurMentorConstant.userNo)
                    {
                        Intent intent = new Intent(DetailWritingActivity.this, ProfileActivity.class);
                        startActivity(intent);
                    }
                    else {
                        Intent intent = new Intent(DetailWritingActivity.this, OtherProfileActivity.class);
                        intent.putExtra("userno", replyObject.reply_userno);
                        Log.e("what", String.valueOf(replyObject.reply_userno));
                        startActivity(intent);
                    }
                }
            });
            holder.reply_name.setText(replyObject.name);
            holder.reply_content.setText(replyObject.content);
            holder.reply_date.setText(replyObject.reg_date);

            if(replyObject.reply_userno == OurMentorConstant.userNo || replyObject.adopt.userNo == OurMentorConstant.userNo) {
                holder.reply_content.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        View view = getLayoutInflater().inflate(R.layout.dialog_reply_modify_delte, null);
                        r_no = item.get(position).r_no;
                        final ImageView reply_setting_modify = (ImageView) view.findViewById(R.id.reply_setting_modify);
                        final ImageView reply_setting_delete = (ImageView) view.findViewById(R.id.reply_setting_delete);
                        final ImageView reply_setting_cancel = (ImageView) view.findViewById(R.id.reply_setting_cancel);
                        final Dialog dialog = new Dialog(DetailWritingActivity.this, R.style.dialogTheme);
                        dialog.setContentView(view);

                        WindowManager.LayoutParams params = dialog.getWindow().getAttributes();

                        dialog.getWindow().setAttributes(params);
                        dialog.setCancelable(true);
                        dialog.show();

                        reply_setting_modify.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                dialog.dismiss();
                                //댓글 수정
                                Toast.makeText(getApplicationContext(), "댓글 수정 구현중입니다", Toast.LENGTH_SHORT).show();
                            }
                        });

                        reply_setting_delete.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                dialog.dismiss();
                                new AsyncReplyDeleteJSONList().execute();

                            }
                        });
                        reply_setting_cancel.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                dialog.dismiss();
                            }
                        });
                    }
                });
            }
        }


        @Override
        public int getItemCount() {
            return item.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder {
            public View mView;
            public CircleImageView reply_circleImageView;
            public TextView reply_name;
            public TextView reply_content;
            public TextView reply_date;


            public ViewHolder(View itemView) {
                super(itemView);
                mView = itemView;
                reply_circleImageView = (CircleImageView)itemView.findViewById(R.id.info_reply_circleImageView);
                reply_name = (TextView)itemView.findViewById(R.id.info_reply_name);
                reply_content = (TextView)itemView.findViewById(R.id.info_reply_content);
                reply_date = (TextView)itemView.findViewById(R.id.info_reply_date);
            }
        }
    }
    public class AsyncLikeJSONList extends AsyncTask<String, Integer, LikeObject> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected LikeObject doInBackground(String... params) {
            HttpURLConnection conn = null;
            BufferedReader fromServer = null;
            LikeObject likeObject = null;
            StringBuilder queryBuf = new StringBuilder();
            String result;
            try {

                queryBuf.append("b_no=" + b_no)
                        .append("&userno="+OurMentorConstant.userNo);
                URL targetURL = new URL(OurMentorConstant.TARGET_URL + OurMentorConstant.LIKE);
                conn = (HttpURLConnection) targetURL.openConnection();
                conn.setConnectTimeout(10000);
                conn.setReadTimeout(10000);

                conn.setRequestMethod("POST");
                OutputStream toServer = conn.getOutputStream();
                toServer.write(new String(queryBuf.toString()).getBytes("UTF-8"));
                toServer.close();


                int responseCode = conn.getResponseCode();
                if (responseCode == HttpURLConnection.HTTP_OK) {
                    fromServer = new BufferedReader(new InputStreamReader(conn.getInputStream(),"UTF-8")); //JSON
                    String line = null;
                    StringBuilder jsonBuf = new StringBuilder();
                    while ((line = fromServer.readLine()) != null) {
                        jsonBuf.append(line);
                        Log.e("jsonBuf", String.valueOf(jsonBuf));
                    }
                    likeObject = ParseDataParseHandler.getJSONLikeRequestAllList(jsonBuf);
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
                if (conn != null) {
                    conn.disconnect(); //열은 순서 반대로 닫는다
                }
            }
            return likeObject;
        }

        @Override
        protected void onPostExecute(LikeObject result) {
            if (result != null ) {
                if(result.success_code == 0)
                    detail_boardLikeImg.setImageResource(R.drawable.infoex_btn_favorite_n);
                else if(result.success_code ==1)
                    detail_boardLikeImg.setImageResource(R.drawable.infoex_btn_favorite_p);
            }
        }
    }

    public class AsyncDetailDeleteJSONList extends AsyncTask<String, Integer, String> {


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... params) {
            HttpURLConnection conn = null;
            BufferedReader fromServer = null;
            StringBuilder queryBuf = new StringBuilder();
            String result;
            try {
                queryBuf.append("b_no=" + b_no)
                        .append("&userno=" + OurMentorConstant.userNo);

                URL targetURL = new URL(OurMentorConstant.TARGET_URL + OurMentorConstant.DELETE);
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
                    }
                    JSONObject responseData = new JSONObject(jsonBuf.toString());
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
            super.onPostExecute(s);
            if (s != null) {
                if (s.equalsIgnoreCase("ok")) {
                    showDialog(1, null);

                } else {
                    showDialog(2, null);
                }
            } else {
                Bundle bundle = new Bundle();
                bundle.putString("message", "안드로이드는 잘못이 없습니다. 서버가 잘못됐습니다.");
                showDialog(2, bundle);
            }
        }
    }

    public class AsyncReplyAdoptJSONList extends AsyncTask<String, Integer, String> {


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... params) {
            HttpURLConnection conn = null;
            BufferedReader fromServer = null;
            StringBuilder queryBuf = new StringBuilder();
            String result;
            try {
                queryBuf.append("b_no=" + b_no)
                        .append("&userno=" + OurMentorConstant.userNo)
                        .append("&r_no=" +r_no);

                Log.e("replyQuery", String.valueOf(queryBuf));

                URL targetURL = new URL(OurMentorConstant.TARGET_URL + OurMentorConstant.REPLY_ADOPT);
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
                    }
                    JSONObject responseData = new JSONObject(jsonBuf.toString());
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
            super.onPostExecute(s);
            if (s != null) {
                if (s.equalsIgnoreCase("ok")) {
                    showDialog(1, null);

                } else {
                    showDialog(2, null);
                }
            } else {
                Bundle bundle = new Bundle();
                bundle.putString("message", "안드로이드는 잘못이 없습니다. 서버가 잘못됐습니다.");
                showDialog(2, bundle);
            }
        }
    }

    public class AsyncReplyDeleteJSONList extends AsyncTask<String, Integer, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... params) {
            HttpURLConnection conn = null;
            BufferedReader fromServer = null;
            StringBuilder queryBuf = new StringBuilder();
            String result;
            try {
                queryBuf.append("b_no=" + b_no)
                        .append("&r_no="+ r_no)
                        .append("&userno=" + OurMentorConstant.userNo);

                URL targetURL = new URL(OurMentorConstant.TARGET_URL + OurMentorConstant.REPLY_DELETE);
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
                    }
                    JSONObject responseData = new JSONObject(jsonBuf.toString());
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
            super.onPostExecute(s);
            if (s != null) {
                if (s.equalsIgnoreCase("ok")) {
                    showDialog(1, null);

                } else {
                    showDialog(2, null);
                }
            } else {
                Bundle bundle = new Bundle();
                bundle.putString("message", "안드로이드는 잘못이 없습니다. 서버가 잘못됐습니다.");
                showDialog(2, bundle);
            }

            new AsyncDetailReplyReadJSONList().execute();
        }
    }
}
