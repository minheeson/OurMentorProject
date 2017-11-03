package com.ourmentor.ymh.lck.smh.common;

import java.sql.Date;

/**
 * Created by ccei on 2016-02-12.
 */
public class ReplyObject {
    public int b_no;
    public String title;
    public String content;
    public String userno;
    public String name;
    public int r_no;
    public String reg_date;
    public int reply_userno;
    public String pic;
    public int like;
    public Adopt adopt;

    public ReplyObject(int b_no, String title, String content, String userno, String name, int r_no, String reg_date, int reply_userno, String pic, int like, Adopt adopt) {
        this.b_no = b_no;
        this.title = title;
        this.content = content;
        this.userno = userno;
        this.name = name;
        this.r_no = r_no;
        this.reg_date = reg_date;
        this.reply_userno = reply_userno;
        this.pic = pic;
        this.like = like;
        this.adopt = adopt;
    }


    public ReplyObject() {
        adopt = new Adopt();
    }

}
