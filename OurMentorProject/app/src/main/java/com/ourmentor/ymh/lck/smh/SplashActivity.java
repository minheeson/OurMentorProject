package com.ourmentor.ymh.lck.smh;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Looper;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ProgressBar;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.ourmentor.ymh.lck.smh.Gcm.QuickstartPreferences;
import com.ourmentor.ymh.lck.smh.Gcm.RegistrationIntentService;
import com.ourmentor.ymh.lck.smh.common.OurMentorConstant;
import com.ourmentor.ymh.lck.smh.common.ParseDataParseHandler;
import com.ourmentor.ymh.lck.smh.common.PreferenceUtil;
import com.ourmentor.ymh.lck.smh.common.WritingObject;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.UUID;

public class SplashActivity extends AppCompatActivity {

    private SplashView mSplashView;
    private MediaPlayer mMediaPlayer;
    private String uuid;
    private int result;
    private int room, b_no;
    String command, main_dir, bro_name;
    private MediaPlayer.OnPreparedListener videoPreparedListener = new MediaPlayer.OnPreparedListener(){
        public void onPrepared(MediaPlayer mp){
            if(mp != null && !mp.isPlaying()) {
                mp.start();
            }
        }
    };
    private MediaPlayer.OnCompletionListener videoCompletionListener = new MediaPlayer.OnCompletionListener(){
        public void onCompletion(MediaPlayer mp) {
            releaseMediaPlayer();
            new AsyncAutoLogin().execute();
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        Intent intent = getIntent();

        command = intent.getStringExtra("command");
        if(command !=null) {
            if (command.equals("chatting")) {
                room = intent.getIntExtra("room", 0);
                bro_name = intent.getStringExtra("bro_name");
                //채팅룸
            } else if (command.equals("mentoaccept")) {
                //userNo //멘토 리스트
            } else if (command.equals("profile")) {
                //mento //프로필
            } else if (command.equals("board")) {
                b_no = intent.getIntExtra("b_no", 0);
                main_dir =intent.getStringExtra("main_dir");
            }
        }



        FrameLayout frame = (FrameLayout) findViewById(R.id.splash_frame);
        mSplashView = new SplashView(this);
        frame.addView(mSplashView);

    }

    public class AsyncAutoLogin extends AsyncTask<String, Integer, String>
    {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            uuid = getRegistrationId();
            OurMentorConstant.uuId =uuid;
        }

        @Override
        protected String doInBackground(String... params) {
            HttpURLConnection conn = null;
            BufferedReader fromServer = null;   //서버에서 넘어온 JSON을 받아내는 Stream
            StringBuilder queryBuf = new StringBuilder();

            result =0;

            try {
                queryBuf.append("uuid=" + uuid);

                URL targetURL = new URL(OurMentorConstant.TARGET_URL + OurMentorConstant.AUTO_LOGIN);
                conn = (HttpURLConnection)targetURL.openConnection();
                conn.setConnectTimeout(10000);
                conn.setReadTimeout(10000);
                conn.setDoInput(true);
                conn.setDoOutput(true);

                //반드시 대분자
                conn.setRequestMethod("POST");

                OutputStream toServer = conn.getOutputStream();
                toServer.write(new String(queryBuf.toString()).getBytes("UTF-8"));
                toServer.flush();
                toServer.close();

                int responseCode = conn.getResponseCode();
                if(responseCode == HttpURLConnection.HTTP_OK){
                    fromServer = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                    String oneLine = "";
                    StringBuilder jsonBuf = new StringBuilder();
                    while((oneLine = fromServer.readLine()) !=null ){
                        jsonBuf.append(oneLine);
                    }
                    JSONObject responseData = new JSONObject(jsonBuf.toString());
                    Log.e("autoLogin",jsonBuf.toString());
                    result = responseData.getInt("success_code");

                    ParseDataParseHandler.getJSONLoginAllList(jsonBuf);
                } else{
                    //200이 아닐 경우, 처리할 값(보통 다이얼로그를 이용하여 띄운다.
                    //절대 내 앱이 죽어서는 안됨...
                }
            } catch (Exception e) {
                Log.e("WriteError", e.toString());
            } finally {
                if(fromServer != null){
                    try{
                        fromServer.close();
                    } catch (IOException e){
                    }

                    if( conn != null){
                        conn.disconnect();
                    }

                }
            }
            if( conn != null) {
            }
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);


            if(uuid.equals("") || result ==0)
            {
                Intent intent = new Intent(SplashActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();
            }
            else
            {
                Intent intent = new Intent(SplashActivity.this, MainActivity.class);
                intent.putExtra("command", command);

                if(command !=null) {
                    if (command.equals("chatting")) {
                        intent.putExtra("room", room);
                        intent.putExtra("bro_name",bro_name);
                        //채팅룸
                    } else if (command.equals("mentoaccept")) {
                        //userNo //멘토 리스트
                    } else if (command.equals("profile")) {
                        //mento //프로필
                    } else if (command.equals("board")) {
                        intent.putExtra("b_no", b_no);
                        intent.putExtra("main_dir",main_dir);
                    }
                }
                startActivity(intent);
                finish();
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        releaseMediaPlayer();
    }



    private void releaseMediaPlayer() {
        if(mMediaPlayer != null) {
            mMediaPlayer.reset();
            mMediaPlayer.release();
            mMediaPlayer = null;
        }
    }

    private class SplashView extends SurfaceView implements SurfaceHolder.Callback {
        private SurfaceHolder holder;
        private Context context;

        private void initView() {
            holder = getHolder();
            holder.addCallback(this);
        }

        public SplashView(Context context) {
            super(context);
            this.context = context;
            initView();
        }

        @Override
        public void surfaceCreated(SurfaceHolder holder) {
            if(mMediaPlayer == null){
                setVideoMediaPlayer(holder);
            }
        }

        @Override
        public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {


        }

        @Override
        public void surfaceDestroyed(SurfaceHolder holder) {  releaseMediaPlayer();  }

        public void setVideoMediaPlayer(SurfaceHolder holder){
            if(mMediaPlayer == null) mMediaPlayer = new MediaPlayer();
            else mMediaPlayer.reset();

            Uri uri = Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.splah);

            try {
                mMediaPlayer.setDataSource(context, uri);
                mMediaPlayer.setDisplay(holder);
                mMediaPlayer.setOnPreparedListener(videoPreparedListener);
                mMediaPlayer.setOnCompletionListener(videoCompletionListener);
                mMediaPlayer.prepare();
            } catch (Exception e) {
                new AsyncAutoLogin().execute();

            }

        }
    }

    private String getRegistrationId()
    {
        String registrationId = PreferenceUtil.instance(getApplicationContext()).regId();

        if (TextUtils.isEmpty(registrationId))
        {
            return "";
        }
        return registrationId;
    }

}