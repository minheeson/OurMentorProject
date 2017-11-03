package com.ourmentor.ymh.lck.smh;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Paint;
import android.os.AsyncTask;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EdgeEffect;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.ourmentor.ymh.lck.smh.Gcm.QuickstartPreferences;
import com.ourmentor.ymh.lck.smh.Gcm.RegistrationIntentService;
import com.ourmentor.ymh.lck.smh.common.OurMentorConstant;
import com.ourmentor.ymh.lck.smh.common.PreferenceUtil;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.UUID;

public class JoinActiviy extends AppCompatActivity {

    private EditText join_ourmento_id, join_ourmento_password01, join_ourmento_password02;
    private ImageView join_ok;
    private static final int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;
    private BroadcastReceiver mRegistrationBroadcastReceiver;

    private String id , password01, password02 , token;
    private  int result;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_join_activiy);


        join_ourmento_id = (EditText)findViewById(R.id.join_ourmento_id);
        join_ourmento_password01 = (EditText)findViewById(R.id.join_ourmento_password01);
        join_ourmento_password02 = (EditText)findViewById(R.id.join_ourmento_password02);

        join_ok =(ImageView)findViewById(R.id.join_ok);

        join_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                id = join_ourmento_id.getText().toString();
                password01 = join_ourmento_password01.getText().toString();
                password02 = join_ourmento_password02.getText().toString();
                if(password01.equals(password02))
                {

                    new AsyncJoin().execute();
                }
                else
                {
                    Toast.makeText(JoinActiviy.this, "비밀번호가 일치하지 않습니다.",Toast.LENGTH_SHORT).show();
                }
            }
        });


    }


    public class AsyncJoin extends AsyncTask<String, Integer, Integer>
    {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Integer doInBackground(String... params) {
            HttpURLConnection conn = null;
            BufferedReader fromServer = null;   //서버에서 넘어온 JSON을 받아내는 Stream
            StringBuilder queryBuf = new StringBuilder();

            result = 0;

            try {
                queryBuf.append("name="+id)
                        .append("&pw="+password01)
                        .append("&token="+token);

                Log.e("JoinQueryBuf", String.valueOf(queryBuf));
                URL targetURL = new URL(OurMentorConstant.TARGET_URL + OurMentorConstant.JOIN);
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
                    Log.e("JoinLog",jsonBuf.toString());
                    result = responseData.getInt("success_code");
                } else{
                    //200이 아닐 경우, 처리할 값(보통 다이얼로그를 이용하여 띄운다.
                    //절대 내 앱이 죽어서는 안됨...
                }
            } catch (Exception e) {
                Log.e("JoinError", e.toString());
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
                Toast.makeText(JoinActiviy.this,"아이디가 존재합니다. 다른 아이디를 사용하세요",Toast.LENGTH_SHORT).show();
            }
            else if(result ==1)
            {
                Intent intent = new Intent(JoinActiviy.this, LoginActivity.class);
                startActivity(intent);
            }

        }

    }

}
