package com.ourmentor.ymh.lck.smh.common;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by ccei on 2016-02-04.
 */
public class ParseDataParseHandler {
    public static ArrayList<WritingObject> getJSONWriteRequestAllList(StringBuilder buf)
    {
        ArrayList<WritingObject> jsonAllList = null;
        JSONArray jsonArray = null;

        String result = "";

        try {
            jsonAllList = new ArrayList<WritingObject>();
            JSONObject responseData = new JSONObject(buf.toString());
            result = responseData.getString("result");
            jsonArray = new JSONArray(result.toString());
            int jsonObjSize = jsonArray.length();

            for(int i = 0; i<jsonObjSize; i++)
            {
                WritingObject entity = new WritingObject();
                JSONObject jData = jsonArray.getJSONObject(i);
                entity.id = jData.getString("name");
                entity.b_no = jData.getInt("b_no");
                entity.category = jData.getInt("category");
                entity.title = jData.getString("title");
                entity.content = jData.getString("content");
                entity.hashTag01 = jData.getString("h_tag1");
                entity.hashTag02 = jData.getString("h_tag2");
                entity.hashTag03 = jData.getString("h_tag3");
                entity.like =jData.getInt("like");
                entity.reply = jData.getInt("reply");
                entity.directory= jData.getString("main_dir");
                entity.detail_directory = jData.getString("sub_dir");
                entity.date = jData.getString("reg_date");
                jsonAllList.add(entity);
            }
        } catch (JSONException e) {
            Log.e("getJSONwritegRequest", "JSON PARSE Error");
        }
        return  jsonAllList;
    }
    public static ArrayList<WritingObject> getJSONReadRequestAllList(StringBuilder buf)
    {
        ArrayList<WritingObject> jsonAllList = null;
        JSONArray jsonArray = null;

        String result = "";
        int yorn;

        try {
            jsonAllList = new ArrayList<WritingObject>();
            JSONObject responseData = new JSONObject(buf.toString());
            yorn = responseData.getInt("yorn");
            result = responseData.getString("result");
            jsonArray = new JSONArray(result.toString());
            int jsonObjSize = jsonArray.length();

            for(int i = 0; i<jsonObjSize; i++)
            {
                WritingObject entity = new WritingObject();
                JSONObject jData = jsonArray.getJSONObject(i);
                entity.id = jData.getString("name");
                entity.b_no = jData.getInt("b_no");
                entity.userNo = jData.getInt("userno");
                entity.category = jData.getInt("category");
                entity.title = jData.getString("title");
                entity.content = jData.getString("content");
                entity.hashTag01 = jData.getString("h_tag1");
                entity.hashTag02 = jData.getString("h_tag2");
                entity.hashTag03 = jData.getString("h_tag3");
//                Log.e("entityhash",entity.hashTag);
                entity.like =jData.getInt("like");
                entity.reply = jData.getInt("reply");
                entity.directory = jData.getString("main_dir");
                entity.detail_directory = jData.getString("sub_dir");
                entity.date = jData.getString("reg_date");
                entity.yorn = yorn;
                jsonAllList.add(entity);
            }
        } catch (JSONException e) {
            Log.e("getJSONReadRequest", "JSON PARSE Error");
        }
        return  jsonAllList;
    }
    public static ArrayList<ReplyObject> getJSONReplyRequestAllList(StringBuilder buf)
    {
        ArrayList<ReplyObject> jsonAllList = null;
        JSONArray jsonArray = null;
        JSONArray extraInfoArray = null;
        String result = "";
        String extraInfo ="";
        String adopt="";

        try {
            jsonAllList = new ArrayList<ReplyObject>();
            JSONObject responseData = new JSONObject(buf.toString());
            result = responseData.getString("result");
            Log.e("result",result);
            extraInfo = responseData.getString("extraInfo");
            Log.e("extraInfo",extraInfo);


            extraInfoArray = new JSONArray(extraInfo.toString());
            jsonArray = new JSONArray(result.toString());
            int jsonObjSize = jsonArray.length();


            for(int i = 0; i<jsonObjSize; i++)
            {
                ReplyObject entity = new ReplyObject();
                JSONObject jData = jsonArray.getJSONObject(i);
                JSONObject jExtraInfo = extraInfoArray.getJSONObject(i);
                entity.content = jData.getString("content");
                entity.name = jData.getString("name");
                entity.reg_date =jData.getString("reg_date");
                entity.reply_userno = jData.getInt("userno");
                entity.adopt.userNo =jData.getJSONObject("adopt").getInt("userno");
                entity.adopt.yorn =jData.getJSONObject("adopt").getInt("yorn");
                entity.pic = jExtraInfo.getString("pic");
                entity.r_no = jData.getInt("r_no");
                jsonAllList.add(entity);
            }

        } catch (JSONException e) {
            Log.e("getJSONReplyRequest", "JSON PARSE Error");
        }
        return  jsonAllList;
    }

    public static ProfileObject getJSONOtherProfileRequestAllList(StringBuilder buf)
    {
        ProfileObject entity = new ProfileObject();
        JSONArray jsonArray = null;
        ArrayList<WritingObject> jsonAllList = null;
        //JSONArray jsonArray = null;
        String result = "";
        try {
            JSONObject responseData =null;
            entity = new ProfileObject();
            responseData = new JSONObject(buf.toString());

            Log.e("profileLog", String.valueOf(responseData));

            result = responseData.getString("result");
            JSONObject jData = new JSONObject(result.toString());
            entity.pic = jData.getString("pic");
            entity.name =jData.getString("name");
            entity.userno=jData.getInt("userno");
            entity.message=jData.getString("message");
            entity.hashTag01 = jData.getString("h_tag1");
            entity.hashTag02 = jData.getString("h_tag2");
            entity.hashTag03 = jData.getString("h_tag3");
            entity.level=jData.getInt("level");
            entity.exp=jData.getInt("exp");
            entity.item=jData.getInt("item");
            entity.exp=jData.getInt("exp");
            entity.nextExp=jData.getInt("nextExp");

            entity.check_mento = responseData.getInt("check_mento");
            entity.check_apply = responseData.getInt("check_apply");

            result = responseData.getString("act_log");


            jsonArray = new JSONArray(result.toString());
            int jsonObjSize = jsonArray.length();
            for(int i = 0; i<jsonObjSize; i++)
            {
                WritingObject entity1 = new WritingObject();
                JSONObject jjData = jsonArray.getJSONObject(i);
                entity1.b_no = jjData.getInt("b_no");
                entity1.category = jjData.getInt("category");
                if(entity1.category ==1 || entity1.category == 2)
                {
                    entity1.title = jjData.getString("title");
                    entity1.content = jjData.getString("content");
                    entity1.hashTag01 = jjData.getString("h_tag1");
                    entity1.hashTag02 = jjData.getString("h_tag2");
                    entity1.hashTag03 = jjData.getString("h_tag3");
                    entity1.like = jjData.getInt("like");
                    entity1.reply = jjData.getInt("reply");
                    entity1.directory = jjData.getString("main_dir");
                    entity1.detail_directory = jjData.getString("sub_dir");
                    entity1.date = jjData.getString("reg_date");
                }
                else if(entity1.category ==3)
                {
                    entity1.title = jjData.getString("title");
                    entity1.content = jjData.getString("content");
                    entity1.hashTag01 = "";
                    entity1.hashTag02 = "";
                    entity1.hashTag03 = "";
                    entity1.like = 0;
                    entity1.reply = 0;
                    entity1.directory = jjData.getString("main_dir");
                    entity1.detail_directory = jjData.getString("sub_dir");
                    entity1.date = jjData.getString("reg_date");
                }
                entity.act_log.add(entity1);
            }
        }
        catch (JSONException e) {
            Log.e("getJSONProfileRequest", "JSON PARSE Error");
        }
        //return  jsonAllList;
        return entity;
    }
    public static ProfileObject getJSONProfileRequestAllList(StringBuilder buf)
    {
        ProfileObject entity = new ProfileObject();
        JSONArray jsonArray = null;
        ArrayList<WritingObject> jsonAllList = null;
        //JSONArray jsonArray = null;
        String result = "";
        try {
            JSONObject responseData =null;
            entity = new ProfileObject();
            responseData = new JSONObject(buf.toString());

            Log.e("profileLog", String.valueOf(responseData));

            result = responseData.getString("result");
            JSONObject jData = new JSONObject(result.toString());
            entity.pic = jData.getString("pic");
            entity.name =jData.getString("name");
            entity.userno=jData.getInt("userno");
            entity.message=jData.getString("message");
            entity.hashTag01 = jData.getString("h_tag1");
            entity.hashTag02 = jData.getString("h_tag2");
            entity.hashTag03 = jData.getString("h_tag3");
            entity.level=jData.getInt("level");
            entity.exp=jData.getInt("exp");
            entity.item=jData.getInt("item");
            entity.exp=jData.getInt("exp");
            entity.nextExp=jData.getInt("nextExp");

            result = responseData.getString("apply_list");
            JSONObject applyData = new JSONObject(result.toString());
            entity.mento_pic = applyData.getString("pic");

            entity.apply_count = responseData.getInt("apply_count");


            result = responseData.getString("act_log");


            jsonArray = new JSONArray(result.toString());
            int jsonObjSize = jsonArray.length();
            for(int i = 0; i<jsonObjSize; i++)
            {
                WritingObject entity1 = new WritingObject();
                JSONObject jjData = jsonArray.getJSONObject(i);
                entity1.b_no = jjData.getInt("b_no");
                entity1.category = jjData.getInt("category");
                if(entity1.category ==1 || entity1.category == 2)
                {
                    entity1.title = jjData.getString("title");
                    entity1.content = jjData.getString("content");
                    entity1.hashTag01 = jjData.getString("h_tag1");
                    entity1.hashTag02 = jjData.getString("h_tag2");
                    entity1.hashTag03 = jjData.getString("h_tag3");
                    entity1.like = jjData.getInt("like");
                    entity1.reply = jjData.getInt("reply");
                    entity1.directory = jjData.getString("main_dir");
                    entity1.detail_directory = jjData.getString("sub_dir");
                    entity1.date = jjData.getString("reg_date");
                }
                else if(entity1.category ==3)
                {
                    entity1.title = jjData.getString("title");
                    entity1.content = jjData.getString("content");
                    entity1.hashTag01 = "";
                    entity1.hashTag02 = "";
                    entity1.hashTag03 = "";
                    entity1.like = 0;
                    entity1.reply = 0;
                    entity1.directory = jjData.getString("main_dir");
                    entity1.detail_directory = jjData.getString("sub_dir");
                    entity1.date = jjData.getString("reg_date");
                }
                entity.act_log.add(entity1);
            }

        }
         catch (JSONException e) {
            Log.e("getJSONProfileRequest", "JSON PARSE Error");
        }
        //return  jsonAllList;
        return entity;
    }


    public static LikeObject getJSONLikeRequestAllList(StringBuilder buf)
    {
        LikeObject entity = null;
        String result = "";

        try {
            JSONObject responseDate = null;
            entity = new LikeObject();
            JSONObject responseData = new JSONObject(buf.toString());
            entity.success_code = responseData.getInt("success_code");
            entity.likeCnt = responseData.getInt("count");

        } catch (JSONException e) {
            Log.e("getJSONLikeRequest", "JSON PARSE Error");
        }
        return  entity;
    }

    public static ArrayList<MentoMenteeObject> getJSONMentoListRequestAllList(StringBuilder buf)
    {
        ArrayList<MentoMenteeObject> jsonAllList = null;
        JSONArray jsonArray = null;

        String result = "";

        try {
            jsonAllList = new ArrayList<MentoMenteeObject>();
            JSONObject responseData = new JSONObject(buf.toString());
            result = responseData.getString("result");

            jsonArray = new JSONArray(result.toString());
            int jsonObjSize = jsonArray.length();
            for(int i = 0; i<jsonObjSize; i++)
            {
                MentoMenteeObject entity = new MentoMenteeObject();
                JSONObject jData = jsonArray.getJSONObject(i);
                entity.pic = jData.getString("pic");
                entity.userNo = jData.getInt("userno");
                entity.name = jData.getString("name");
                entity.msg = jData.getString("message");
                jsonAllList.add(entity);
            }
        } catch (JSONException e) {
            Log.e("getJSONMentoListRequest", "JSON PARSE Error");
        }
        return  jsonAllList;

    }


    public static void getJSONLoginAllList(StringBuilder buf)
    {
        try {
            JSONObject responseData = new JSONObject(buf.toString());
            JSONObject result = responseData.getJSONObject("result");
            OurMentorConstant.userNo= result.getInt("userno");
            OurMentorConstant.userName = result.getString("name");
            OurMentorConstant.message = result.getString("message");
            OurMentorConstant.pic = result.getString("pic");

        } catch (JSONException e) {
            Log.e("getJSONLoginequest", "JSON PARSE Error");
        }
    }


    public static ArrayList<RankObject> getJSONRankRequestAllList(StringBuilder buf)
    {
        RankObject entity1 = new RankObject();
        RankObject entity2 = new RankObject();
        RankObject entity3 = new RankObject();
        JSONArray jsonArray = null;
        ArrayList<RankObject> jsonAllList = new ArrayList<RankObject>();
        //JSONArray jsonArray = null;
        String result = "";
        try {
            JSONObject responseData =null;

            responseData = new JSONObject(buf.toString());

            Log.e("rankLog", String.valueOf(responseData));

            result = responseData.getString("kofLevel");
            Log.e("rankLevel",result);
            JSONObject jData = new JSONObject(result.toString());
            entity1.pic = jData.getString("pic");
            entity1.name =jData.getString("name");
            entity1.userno=jData.getInt("userno");
            entity1.h_tag1 = jData.getString("h_tag1");
            entity1.h_tag2 = jData.getString("h_tag2");
            entity1.h_tag3 = jData.getString("h_tag3");

            jsonAllList.add(entity1);


            result = responseData.getString("kofMento");
            Log.e("rankMento",result);
            jData = new JSONObject(result.toString());
            entity2.pic = jData.getString("pic");
            entity2.name =jData.getString("name");
            entity2.userno=jData.getInt("userno");
            entity2.h_tag1 = jData.getString("h_tag1");
            entity2.h_tag2 = jData.getString("h_tag2");
            entity2.h_tag3 = jData.getString("h_tag3");

            jsonAllList.add(entity2);

            result = responseData.getString("kofAct");
            Log.e("rankMento",result);
            jData = new JSONObject(result.toString());
            entity3.pic = jData.getString("pic");
            entity3.name =jData.getString("name");
            entity3.userno=jData.getInt("userno");
            entity3.h_tag1 = jData.getString("h_tag1");
            entity3.h_tag2 = jData.getString("h_tag2");
            entity3.h_tag3 = jData.getString("h_tag3");

            jsonAllList.add(entity3);


        }
        catch (JSONException e) {
            Log.e("getJSONRankRequest", "JSON PARSE Error");
        }
        return  jsonAllList;
    }


    public static ArrayList<MentoMenteeObject> getJSONMentoApplyListRequestAllList(StringBuilder buf)
    {
        ArrayList<MentoMenteeObject> jsonAllList = null;
        JSONArray jsonArray = null;

        String result = "";

        try {
            jsonAllList = new ArrayList<MentoMenteeObject>();
            JSONObject responseData = new JSONObject(buf.toString());
            result = responseData.getString("apply_list");
            Log.e("mento",result);
            jsonArray = new JSONArray(result.toString());
            int jsonObjSize = jsonArray.length();
            Log.e("size", String.valueOf(jsonObjSize));
            for(int i = 0; i<jsonObjSize; i++)
            {
                MentoMenteeObject entity = new MentoMenteeObject();
                JSONObject jData = jsonArray.getJSONObject(i);
                entity.pic = jData.getString("pic");
                entity.userNo = jData.getInt("userno");
                entity.name = jData.getString("name");
                entity.msg = jData.getString("message");
                jsonAllList.add(entity);
            }
        } catch (JSONException e) {
            Log.e("getJSONMentoListRequest", "JSON PARSE Error");
        }
        return  jsonAllList;
    }


    public static ArrayList<ChattingListObject> getJSONChattingListRequestAllList(StringBuilder buf)
    {
        ArrayList<ChattingListObject> jsonAllList = null;
        JSONArray jsonArray = null;

        String result = "";

        try {
            jsonAllList = new ArrayList<ChattingListObject>();
            JSONObject responseData = new JSONObject(buf.toString());
            result = responseData.getString("result");

            jsonArray = new JSONArray(result.toString());
            int jsonObjSize = jsonArray.length();
            for(int i = 0; i<jsonObjSize; i++)
            {
                ChattingListObject entity = new ChattingListObject();
                JSONObject jData = jsonArray.getJSONObject(i);
                entity.pic = jData.getString("pic");
                entity.last_message = jData.getString("message");
                entity.name = jData.getString("name");
                entity.room = jData.getInt("room");
                entity.reg_date= jData.getString("reg_date");
                jsonAllList.add(entity);
            }
        } catch (JSONException e) {
            Log.e("getJSONChatListRequest", "JSON PARSE Error");
        }
        return  jsonAllList;
    }
}

