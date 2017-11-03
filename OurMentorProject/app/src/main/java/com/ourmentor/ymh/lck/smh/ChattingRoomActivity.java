package com.ourmentor.ymh.lck.smh;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.ourmentor.ymh.lck.smh.common.OurMentorConstant;
import com.ourmentor.ymh.lck.smh.common.ParseDataParseHandler;
import com.ourmentor.ymh.lck.smh.common.WritingObject;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;
import io.socket.client.IO;
import io.socket.client.Socket;
import io.socket.emitter.Emitter;

public class ChattingRoomActivity extends AppCompatActivity {
    private int room ;
    private String name = null;
    private String username = null;
    private RecyclerView chatting_recyclerView;
    private ChattingRecyclerViewAdapter chattingRecyclerViewAdapter;
    private TextView chatting_room_main_title;
    private ArrayList<ChattingObject> item;
    private ImageView chatting_room_navi_btn, chatting_room_settings;
    private Socket socket;
    {
        try {
            socket = IO.socket(OurMentorConstant.TARGET_URL);
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
    }                                                               //소켓 접속
    private Button sendBtn;
    private EditText txt;
    private ScrollView scroll;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chatting_room);
        chatting_recyclerView = (RecyclerView)findViewById(R.id.chatting_recyclerView);
        chatting_recyclerView.setLayoutManager(new LinearLayoutManager(this));

        chatting_room_main_title = (TextView)findViewById(R.id.chatting_room_main_title);

        chatting_room_settings = (ImageView)findViewById(R.id.chatting_room_settings);
        chatting_room_settings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                View view = getLayoutInflater().inflate (R.layout.dialog_chatting_room_exit, null);
                final TextView chatting_room_exit = (TextView)view.findViewById(R.id.chatting_room_exit);
                final Dialog dialog = new Dialog(ChattingRoomActivity.this, R.style.dialogTheme);
                dialog.setContentView(view);

                WindowManager.LayoutParams params= dialog.getWindow().getAttributes();
                params.x =500;
                params.y = -1050;

                dialog.getWindow().setAttributes(params);
                dialog.setCancelable(true);
                dialog.show();

                chatting_room_exit.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Toast.makeText(getApplicationContext(),"추후 구현 예정중입니당ㅋ",Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });


        item = new ArrayList<ChattingObject>();

        Intent intent = getIntent();
        name = OurMentorConstant.userName;
        username = intent.getStringExtra("userNo");
        chatting_room_main_title.setText(username);
        room = intent.getIntExtra("room", 0);

        sendBtn = (Button)findViewById(R.id.chattingroom_send_btn);
        txt = (EditText)findViewById(R.id.chattingroom_input);


        chatting_room_navi_btn = (ImageView)findViewById(R.id.chatting_room_navi_btn);
        chatting_room_navi_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                JSONObject bye = new JSONObject();
                try{
                    bye.put("message","bye")
                        .put("room",room);
                }catch (JSONException e)
                {
                    e.printStackTrace();
                }
                socket.emit("bye", bye);
                onBackPressed();
            }
        });



        sendBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {                                           //쓴거
                String s = txt.getText().toString();
                txt.setText("");
                Log.e("스트링", s);
                JSONObject jo = new JSONObject();
                try {
                    jo.put("me",name);
                    jo.put("message",s);
                    jo.put("room",room);
                    jo.put("pic",OurMentorConstant.pic);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                socket.emit("message", jo);
            }                                                               //서버에 전송
        });


        JSONObject jo = new JSONObject();
        try {                                                                               //방
            jo.put("name",name);
            jo.put("username",username);
            jo.put("room",room);
            jo.put("pic",OurMentorConstant.pic);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        socket.emit("chat", jo);
        socket.on("new message", newMessage);
        socket.on("old message", oldMessage);
        socket.connect();


        chattingRecyclerViewAdapter = new ChattingRecyclerViewAdapter(ChattingRoomActivity.this, item);
        chatting_recyclerView.setAdapter(chattingRecyclerViewAdapter);
    }



    private Emitter.Listener newMessage = new Emitter.Listener() {
        @Override
        public void call(final Object... args) {
            ChattingRoomActivity.this.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    JSONObject data = (JSONObject) args[0];

                    Log.e("onNewMessage",((JSONObject)args[0]).toString());
                    ChattingObject chattingObject = new ChattingObject();
                    try {
                        chattingObject.username = data.getString("name");                   //내이름
                        chattingObject.msg = data.getString("message");
                        chattingObject.reg_date = data.getString("reg_date");
                        chattingObject.pic = data.getString("pic");

                        chattingRecyclerViewAdapter.item.add(chattingObject);
                        chattingRecyclerViewAdapter.notifyDataSetChanged();
                        chatting_recyclerView.smoothScrollToPosition(chattingRecyclerViewAdapter.item.size());
                    } catch (JSONException e) {
                        return;
                    }
                }
            });
        }
    };
    private Emitter.Listener oldMessage = new Emitter.Listener() {
        @Override
        public void call(final Object... args) {
            ChattingRoomActivity.this.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Log.e("onOldMessage",((JSONArray)args[0]).toString());
                    JSONArray data = (JSONArray)args[0];


                    try {
                        for(int i = data.length()-1 ; i >=0 ; i --) {
                            JSONObject obj = data.getJSONObject(i);
                            ChattingObject chattingObject = new ChattingObject();
                            chattingObject.username= obj.getString("name");                     //내이름
                            chattingObject.msg = obj.getString("message");
                            chattingObject.reg_date = obj.getString("reg_date");
                            chattingObject.pic = obj.getString("pic");

                            chattingRecyclerViewAdapter.item.add(chattingObject);
                            chattingRecyclerViewAdapter.notifyDataSetChanged();
                            chatting_recyclerView.smoothScrollToPosition(chattingRecyclerViewAdapter.item.size());
                        }

                    } catch (JSONException e) {
                        return;
                    }
                }
            });
        }
    };

    public class ChattingObject{
        String username;
        String msg;
        String reg_date;
        String pic;
    }
    public class ChattingRecyclerViewAdapter extends RecyclerView.Adapter<ChattingRecyclerViewAdapter.ViewHolder>           //RecyclerView Adapter
    {
        Context mContext;
        ArrayList<ChattingObject> item;

        public ChattingRecyclerViewAdapter(ChattingRoomActivity chattingRoomActivity, ArrayList<ChattingObject> item) {
            mContext = chattingRoomActivity;
            this.item = item;
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.chattingviewitem, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {
            final ChattingObject chattingObject = item.get(position);
            if( chattingObject.username.equals(name))
            {
                holder.myChattingLayout.setVisibility(View.VISIBLE);
                holder.myChatting.setText(chattingObject.msg);
                holder.myChattingDate.setText(chattingObject.reg_date);
                holder.otherChattingLayout.setVisibility(View.INVISIBLE);

            }
            else if(chattingObject.username.equals(username))
            {
                //내가안쓴거
                holder.otherChattingLayout.setVisibility(View.VISIBLE);
                Glide.with(getApplicationContext()).load(chattingObject.pic).into(holder.otherChattingImg);
                holder.otherChatting.setText(chattingObject.msg);
                holder.otherChattingName.setText(chattingObject.username);
                holder.otherChattingDate.setText(chattingObject.reg_date);
                holder.myChattingLayout.setVisibility(View.INVISIBLE);
            }
        }

        @Override
        public int getItemCount() {
            return item.size();
        }
        public class ViewHolder extends RecyclerView.ViewHolder{
            public View mView;
            public TextView otherChatting;
            public TextView myChatting;
            public CircleImageView otherChattingImg;
            public TextView otherChattingDate;
            public TextView myChattingDate;
            public TextView otherChattingName;
            public LinearLayout otherChattingLayout , myChattingLayout;
            public ViewHolder(View itemView) {                                                                                      //View holder
                super(itemView);
                mView = itemView;
                otherChatting = (TextView)mView.findViewById(R.id.otherChatting);
                myChatting = (TextView)mView.findViewById(R.id.myChatting);
                otherChattingImg = (CircleImageView)mView.findViewById(R.id.otherChattingImg);
                otherChattingDate = (TextView)mView.findViewById(R.id.otherChattingDate);
                myChattingDate = (TextView)mView.findViewById(R.id.myChattingDate);
                otherChattingName = (TextView)mView.findViewById(R.id.otherChattingName);
                otherChattingLayout = (LinearLayout)mView.findViewById(R.id.otherChattingLayout);
                myChattingLayout = (LinearLayout)mView.findViewById(R.id.myChattingLayout);
            }
        }

        @Override
        public int getItemViewType(int position) {
            return super.getItemViewType(position);
        }

        @Override
        public void setHasStableIds(boolean hasStableIds) {
            super.setHasStableIds(hasStableIds);
        }
    }

    public class AsyncChatExitJSONList extends AsyncTask<String, Integer, String> {

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
                queryBuf.append("name=" + OurMentorConstant.userName)
                        .append("&bro_name="+username); //좋아요 체크
                URL targetURL = new URL(OurMentorConstant.TARGET_URL + OurMentorConstant.CHAT_EXIT);
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
                        Log.e("ChatEXITReadbuf", String.valueOf(jsonBuf));
                    }
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
            return null;
        }


        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            onBackPressed();
        }

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

    }
}
