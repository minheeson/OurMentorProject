package com.ourmentor.ymh.lck.smh.common;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by ccei on 2016-02-21.
 */
public class RankObject implements Parcelable {
    public int userno;
    public String name;
    public String pic;
    public String h_tag1;
    public String h_tag2;
    public String h_tag3;

    public RankObject() {
    }

    public RankObject(int userno, String name, String pic, String h_tag1, String h_tag2, String h_tag3) {
        this.userno = userno;
        this.name = name;
        this.pic = pic;
        this.h_tag1 = h_tag1;
        this.h_tag2 = h_tag2;
        this.h_tag3 = h_tag3;
    }

    protected RankObject(Parcel in) {
        userno = in.readInt();
        name = in.readString();
        pic = in.readString();
        h_tag1 = in.readString();
        h_tag2 = in.readString();
        h_tag3 = in.readString();
    }

    public static final Creator<RankObject> CREATOR = new Creator<RankObject>() {
        @Override
        public RankObject createFromParcel(Parcel in) {
            return new RankObject(in);
        }

        @Override
        public RankObject[] newArray(int size) {
            return new RankObject[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(userno);
        dest.writeString(name);
        dest.writeString(pic);
        dest.writeString(h_tag1);
        dest.writeString(h_tag2);
        dest.writeString(h_tag3);
    }
}