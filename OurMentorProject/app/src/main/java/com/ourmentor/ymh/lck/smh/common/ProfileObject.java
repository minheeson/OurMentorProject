package com.ourmentor.ymh.lck.smh.common;

import java.util.ArrayList;

/**
 * Created by ccei on 2016-02-12.
 */
public class ProfileObject {
    public String name;
    public int userno;
    public String message;
    public String hashTag01, hashTag02, hashTag03;
    public int level;
    public int exp, nextExp;
    public int item;
    public String pic;
    public ArrayList<WritingObject> act_log;
    public int check_mento;
    public int check_apply;
    public String mento_pic;
    public int apply_count;


    public ProfileObject() {
        act_log = new ArrayList<>();
    }

    public ProfileObject(String name, int userno, String message, String hashTag01, String hashTag02, String hashTag03, int level, int exp, int nextExp, int item, String pic, ArrayList<WritingObject> act_log ,int check_mento, int check_apply
    ,String mento_pic, int apply_count) {
        this.name = name;
        this.userno = userno;
        this.message = message;
        this.hashTag01 = hashTag01;
        this.hashTag02 = hashTag02;
        this.hashTag03 = hashTag03;
        this.level = level;
        this.exp = exp;
        this.nextExp = nextExp;
        this.item = item;
        this.pic = pic;
        this.act_log = act_log;
        this.check_mento = check_mento;
        this.check_apply = check_apply;
        this.mento_pic = mento_pic;
        this.apply_count = apply_count;
    }

}

