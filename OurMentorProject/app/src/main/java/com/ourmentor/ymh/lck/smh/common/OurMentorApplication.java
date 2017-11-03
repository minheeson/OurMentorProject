package com.ourmentor.ymh.lck.smh.common;

import android.app.Application;
import android.content.Context;

/**
 * Created by ccei on 2016-01-25.
 * 내 앱을 사용자가 런처했을때 가장먼저 실행되는 클래스
 */
public class OurMentorApplication extends Application {
    private static Context mContext;

    @Override
    public void onCreate() {
        super.onCreate();
        mContext = this;
    }
    public static Context getMentorContext(){
        return mContext;
    }
}
