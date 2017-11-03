package com.ourmentor.ymh.lck.smh.common;

/**
 * Created by ccei on 2016-01-25.
 */
public class OurMentorConstant {
    public static final String TARGET_URL = "http://52.79.104.226:3000/";
    public static final String WRITE_INSERT_PATH = "board/write";
    public static final String WRITE_ALL_PATH = "board/list";
    public static final String BOARD_DIR_PATH = "board/list/sub";
    public static final String DETAIL_WRITING = "board/read";
    public static final String DETAIL_REPLY_WRITE = "board/reply";
    public static final String DETAIL_REPLY_READ = "board/readre";
    public static final String PROFILE="user/myprofile/";
    public static final String LIKE="board/like";
    public static final String MODIFY="board/update";
    public static final String DELETE="board/delete";
    public static final String SEARCH="board/search";
    public static final String OTHER_PROFILE ="user/profile/";

    public static final String RANK ="user/kofs";


    public static final String APPLY_MENTO = "chat/applymento";
    public static final String APPLY_LIST = "chat/applylist";
    public static final String JOIN_MENTO ="chat/joinmento";

    public static final String REFUSE_MENTO ="chat/refusementee";



    public static final String OFF_MENTO ="chat/cutoffmento";


    public static final String MENTO_LIST ="chat/mentolist/";
    public static final String MENTEE_LIST="chat/menteelist/";


    public static final String PROFILE_MODIFY ="user/updateprofile";
    public static final String REPLY_ADOPT="board/adopt";
    public static final String REPLY_DELETE="board/deletereply";
    public static final String REPLY_MODIFY="board/updatereply"; // b_no, r_no, userno, content
    public static final String SEARCH_HASHTAG ="board/htsearch"; //h_tag, category
    public static final String LOGIN ="user/login/";
    public static final String AUTO_LOGIN="user/autologin";
    public static final String JOIN ="user/join";

    public static final String CHAT= "chat";
    public static final String CHAT_LIST="chat/chatlog";
    public static final String CHAT_EXIT="chat/chatout";


    public static String uuId;
    public static int userNo;
    public static String userName ;
    public static String pic;
    public static String message;


    public OurMentorConstant() {
    }
}
