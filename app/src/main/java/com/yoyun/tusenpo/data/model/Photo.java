package com.yoyun.tusenpo.data.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.yoyun.tusenpo.utils.GsonUtil;

import java.io.Serializable;

/**
 * Created by Yoyun on 2017/7/18.
 */

public class Photo implements Serializable, Parcelable {
    private String imgUrl;
    private String describe;

    public String getImgUrl() {
        return imgUrl;
    }

    public Photo setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
        return this;
    }

    public String getDescribe() {
        return describe;
    }

    public Photo setDescribe(String describe) {
        this.describe = describe;
        return this;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.imgUrl);
        dest.writeString(this.describe);
    }

    public Photo() {
    }

    protected Photo(Parcel in) {
        this.imgUrl = in.readString();
        this.describe = in.readString();
    }

    public static final Parcelable.Creator<Photo> CREATOR = new Parcelable.Creator<Photo>() {
        @Override
        public Photo createFromParcel(Parcel source) {
            return new Photo(source);
        }

        @Override
        public Photo[] newArray(int size) {
            return new Photo[size];
        }
    };

    @Override
    public String toString() {
        return GsonUtil.getDefaultGson().toJson(this);
    }
}
