package com.ourmentor.ymh.lck.smh.common;

/**
 * Created by ccei on 2016-02-22.
 */
public class ChattingListObject {
    public int room;
    public String name;
    public String last_message;
    public String reg_date;
    public String pic;

    public ChattingListObject() {
    }

    public ChattingListObject(int room, String name, String last_message, String reg_date, String pic) {
        this.room = room;
        this.name = name;
        this.last_message = last_message;
        this.reg_date = reg_date;
        this.pic = pic;
    }
}
