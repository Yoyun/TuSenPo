package com.yoyun.tusenpo.data.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.yoyun.tusenpo.utils.GsonUtil;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Yoyun on 2017/7/18.
 */

public class Article implements Serializable, Parcelable {
    private String pageUrl;
    private String title;
    private String date;
    private List<Photo> photos;

    public String getPageUrl() {
        return pageUrl;
    }

    public Article setPageUrl(String pageUrl) {
        this.pageUrl = pageUrl;
        return this;
    }

    public String getTitle() {
        return title;
    }

    public Article setTitle(String title) {
        this.title = title;
        return this;
    }

    public String getDate() {
        return date;
    }

    public Article setDate(String date) {
        this.date = date;
        return this;
    }

    public List<Photo> getPhotos() {
        return photos;
    }

    public Article setPhotos(List<Photo> photos) {
        this.photos = photos;
        return this;
    }

    public Article() {
    }

    @Override
    public String toString() {
        return GsonUtil.getDefaultGson().toJson(this);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.pageUrl);
        dest.writeString(this.title);
        dest.writeString(this.date);
        dest.writeTypedList(this.photos);
    }

    protected Article(Parcel in) {
        this.pageUrl = in.readString();
        this.title = in.readString();
        this.date = in.readString();
        this.photos = in.createTypedArrayList(Photo.CREATOR);
    }

    public static final Creator<Article> CREATOR = new Creator<Article>() {
        @Override
        public Article createFromParcel(Parcel source) {
            return new Article(source);
        }

        @Override
        public Article[] newArray(int size) {
            return new Article[size];
        }
    };
}
