package com.ourmentor.ymh.lck.smh.common;

/**
 * Created by ccei on 2016-02-17.
 */
public class MentoMenteeObject {
    public String pic;
    public int userNo;
    public String name;
    public String msg;

    public MentoMenteeObject() {
    }

    public MentoMenteeObject(String pic, int userNo, String name, String msg) {
        this.pic = pic;
        this.userNo = userNo;
        this.name = name;
        this.msg = msg;
    }
}
