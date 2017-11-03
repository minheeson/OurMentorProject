package com.ourmentor.ymh.lck.smh.common;

import java.sql.Date;

/**
 * Created by ccei on 2016-02-04.
 */
public class WritingObject {
    public int userNo;                             //고유 번호
    public int b_no;
    public String id;                       //작성자
    public int category;                        //질문 or 정보
    public String title;                    //제목
    public String content;                  //내용
    public String hashTag01, hashTag02, hashTag03;                  //해시태그 ->DB에서 #으로 짤라서 배열에
    public String directory;                    //1차 카테고리 0이면 전체
    public String detail_directory;             //2차 카테고리
    public String date;                       //시간
    public int reply;
    public int like;
    public int yorn;
    public WritingObject()
    {}

    public WritingObject(int userNo, int b_no, String id, int category, String title, String content, String hashTag01, String hashTag02, String hashTag03, String directory, String detail_directory, String date , int reply, int like, int yorn) {
        this.userNo = userNo;
        this.id = id;
        this.b_no = b_no;
        this.category = category;
        this.title = title;
        this.content = content;
        this.hashTag01 = hashTag01;
        this.hashTag02 = hashTag02;
        this.hashTag03 = hashTag03;
        this.directory = directory;
        this.detail_directory = detail_directory;
        this.date = date;
        this.reply = reply;
        this.like = like;
        this.yorn = yorn;
    }
}
