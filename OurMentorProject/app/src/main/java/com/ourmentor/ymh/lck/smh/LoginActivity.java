package com.ourmentor.ymh.lck.smh;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.AsyncTask;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.ourmentor.ymh.lck.smh.Gcm.QuickstartPreferences;
import com.ourmentor.ymh.lck.smh.Gcm.RegistrationIntentService;
import com.ourmentor.ymh.lck.smh.common.OurMentorConstant;
import com.ourmentor.ymh.lck.smh.common.ParseDataParseHandler;
import com.ourmentor.ymh.lck.smh.common.PreferenceUtil;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.UUID;

public class LoginActivity extends AppCompatActivity {
    private EditText login_ourmento_id, login_ourmento_password01;
    private ImageView login_ok;
    private ImageView login_to_join;
    private String id,password;
    private String uuID;
    private String token;


    private static final int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;
    private BroadcastReceiver mRegistrationBroadcastReceiver;


    private final long FINISH_INTERVAL_TIME = 2000;
    private long backPressedTime = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        login_ourmento_id = (EditText)findViewById(R.id.login_ourmento_id);
        login_ourmento_password01 = (EditText)findViewById(R.id.login_ourmento_password01);
        login_ok = (ImageView)findViewById(R.id.login_ok);
        login_to_join = (ImageView)findViewById(R.id.login_to_join);

        getInstanceIdToken();
        registBroadcastReceiver();

        login_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                id = login_ourmento_id.getText().toString();
                password = login_ourmento_password01.getText().toString();
                new AsyncLogin().execute();
            }
        });

        login_to_join.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, JoinActiviy.class);
                startActivity(intent);
            }
        });
    }


    public class AsyncLogin extends AsyncTask<String, Integer, Integer>
    {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            uuID = UUID.randomUUID().toString();
            OurMentorConstant.uuId =uuID;
        }

        @Override
        protected Integer doInBackground(String... params) {
            HttpURLConnection conn = null;
            BufferedReader fromServer = null;   //서버에서 넘어온 JSON을 받아내는 Stream
            StringBuilder queryBuf = new StringBuilder();
            int result = 0;

            try {
                queryBuf.append("name=" + id)
                        .append("&pw=" + password)
                        .append("&uuid="+uuID)
                        .append("&token="+token);

                URL targetURL = new URL(OurMentorConstant.TARGET_URL + OurMentorConstant.LOGIN);
                Log.e("LoginQueryBuf",String.valueOf(queryBuf));
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
                    Log.e("LoginLog",jsonBuf.toString());
                    result = responseData.getInt("success_code");
                    Log.e("loginCode", String.valueOf(result));
                    if(result ==1)
                    {
                        ParseDataParseHandler.getJSONLoginAllList(jsonBuf);
                    }

                } else{
                    //200이 아닐 경우, 처리할 값(보통 다이얼로그를 이용하여 띄운다.
                    //절대 내 앱이 죽어서는 안됨...
                }
            } catch (Exception e) {
                Log.e("LoginError", e.toString());
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
            return result;
        }

        @Override
        protected void onPostExecute(Integer result) {
            super.onPostExecute(result);
            if(result == 0)
            {
                Toast.makeText(LoginActivity.this, "아이디나 비밀번호가 일치하지 않습니다.", Toast.LENGTH_SHORT).show();
            }
            else if(result ==1)
            {
                storeRegistrationId(uuID);
                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                startActivity(intent);
            }

        }

    }
    // registraion id를 preference에 저장한다.
    private void storeRegistrationId(String regId)
    {
        PreferenceUtil.instance(getApplicationContext()).putRedId(regId);
    }

    private void storeRegistrationTokenId(String regPushId)
    {
        PreferenceUtil.instance(getApplicationContext()).putRedPushId(regPushId);
    }



    // registraion id를 preference에 저장한다.

    public void getInstanceIdToken() {
        if (checkPlayServices()) {
            // Start IntentService to register this application with GCM.
            Intent intent = new Intent(this, RegistrationIntentService.class);
            startService(intent);
        }
    }

    public void registBroadcastReceiver(){
        mRegistrationBroadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                String action = intent.getAction();
                if(action.equals(QuickstartPreferences.REGISTRATION_READY)){
                    // 액션이 READY일 경우

                } else if(action.equals(QuickstartPreferences.REGISTRATION_GENERATING)){
                    // 액션이 GENERATING일 경우


                } else if(action.equals(QuickstartPreferences.REGISTRATION_COMPLETE)){
                    // 액션이 COMPLETE일 경우

                    token = intent.getStringExtra("token");

                    storeRegistrationTokenId(token);


                    Log.d("my android push token :", token);
                }

            }
        };
    }


    /**
     * 앱이 실행되어 화면에 나타날때 LocalBoardcastManager에 액션을 정의하여 등록한다.
     */
    @Override
    protected void onResume() {
        super.onResume();
        LocalBroadcastManager.getInstance(this).registerReceiver(mRegistrationBroadcastReceiver,
                new IntentFilter(QuickstartPreferences.REGISTRATION_READY));
        LocalBroadcastManager.getInstance(this).registerReceiver(mRegistrationBroadcastReceiver,
                new IntentFilter(QuickstartPreferences.REGISTRATION_GENERATING));
        LocalBroadcastManager.getInstance(this).registerReceiver(mRegistrationBroadcastReceiver,
                new IntentFilter(QuickstartPreferences.REGISTRATION_COMPLETE));
    }

    /**
     * 앱이 화면에서 사라지면 등록된 LocalBoardcast를 모두 삭제한다.
     */
    @Override
    protected void onPause() {
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mRegistrationBroadcastReceiver);
        super.onPause();
    }
    /**
     * Google Play Service를 사용할 수 있는 환경이지를 체크한다.
     */
    private boolean checkPlayServices() {
        int resultCode = GooglePlayServicesUtil.isGooglePlayServicesAvailable(this);
        if (resultCode != ConnectionResult.SUCCESS) {
            if (GooglePlayServicesUtil.isUserRecoverableError(resultCode)) {
                GooglePlayServicesUtil.getErrorDialog(resultCode, this,
                        PLAY_SERVICES_RESOLUTION_REQUEST).show();
            } else {
                Log.i("SplashActivity", "This device is not supported.");
                finish();
            }
            return false;
        }
        return true;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        long currentTime = System.currentTimeMillis();
        long intervalTitme = currentTime - backPressedTime;

        if (0 <= intervalTitme && FINISH_INTERVAL_TIME >= intervalTitme) {
            super.onBackPressed();
        } else {
            backPressedTime = currentTime;
            Toast.makeText(getApplicationContext(), "뒤로 한번더 누르면 종료됩니다.", Toast.LENGTH_SHORT).show();
        }
    }
}
