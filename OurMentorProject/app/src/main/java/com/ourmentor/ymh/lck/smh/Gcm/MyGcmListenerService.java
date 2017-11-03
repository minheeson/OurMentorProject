package com.ourmentor.ymh.lck.smh.Gcm;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.google.android.gms.gcm.GcmListenerService;
import com.ourmentor.ymh.lck.smh.R;
import com.ourmentor.ymh.lck.smh.SplashActivity;
import com.ourmentor.ymh.lck.smh.common.OurMentorConstant;
import com.ourmentor.ymh.lck.smh.common.PreferenceUtil;

/**
 * Created by ccei on 2016-02-18.
 */
public class MyGcmListenerService extends GcmListenerService {


    private static final String TAG = "MyGcmListenerService";

    /**
     *
     * @param from SenderID 값을 받아온다.
     * @param data Set형태로 GCM으로 받은 데이터 payload이다.
     */
    @Override
    public void onMessageReceived(String from, Bundle data) {

        String title, message , command, bro_name="", main_dir="";
        int room = 0, b_no =0;
        String w;
        String r;

        title = data.getString("title");
        message = data.getString("message");
        command = data.getString("command");

        if(command!=null) {
            if (command.equals("chatting")) {
                r=data.getString("room");
                room = Integer.parseInt(r);
                Log.d(TAG, "room: "+ room);
                bro_name = data.getString("bro_name");
                Log.d(TAG, "bro_name: " +bro_name);
                //채팅룸
            } else if (command.equals("mentoaccept")) {
                //userNo //멘토 리스트
            } else if (command.equals("profile")) {
                //mento //프로필
            } else if (command.equals("board")) {
                w= data.getString("b_no");
                b_no = Integer.parseInt(w);


                Log.d(TAG, "b_no: "+b_no);
                main_dir = data.getString("main_dir");
                Log.d(TAG, "main_dir "+main_dir);
            }
        }

        Log.d(TAG, "From: " + from);
        Log.d(TAG, "Title: " + title);
        Log.d(TAG, "Message: " + message);




        // GCM으로 받은 메세지를 디바이스에 알려주는 sendNotification()을 호출한다.
        if( PreferenceUtil.instance(getApplicationContext()).regPushFlag()) {
            sendNotification(title, message,command, room, b_no, main_dir, bro_name);
        }

    }


    /**
     * 실제 디바에스에 GCM으로부터 받은 메세지를 알려주는 함수이다. 디바이스 Notification Center에 나타난다.
     * @param title
     * @param message
     * @param command
     * @param room
     * @param b_no
     * @param main_dir
     * @param bro_name
     */
    private void sendNotification(String title, String message, String command, int room , int b_no , String main_dir, String bro_name ) {

        Intent intent = new Intent(this, SplashActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        intent.putExtra("command", command);
        if(command!=null) {
            if (command.equals("chatting")) {
                intent.putExtra("room", room);
                intent.putExtra("bro_name",bro_name);
                //채팅룸
            } else if (command.equals("mentoaccept")) {
                //userNo //멘토 리스트
            } else if (command.equals("profile")) {
                //mento //프로필 멘토 요청
            } else if (command.equals("board")) {
                intent.putExtra("b_no", b_no);
                intent.putExtra("main_dir",main_dir);
            }
        }

        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0 /* Request code */, intent,
                PendingIntent.FLAG_ONE_SHOT);

        Uri defaultSoundUri= RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle(title)
                .setContentText(message)
                .setAutoCancel(true)
                .setSound(defaultSoundUri)
                .setContentIntent(pendingIntent)
                .setVibrate(new long[]{0, 1000});


        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        notificationManager.notify(0 /* ID of notification */, notificationBuilder.build());
    }

}
