package com.zhouchengang.backuponline.album;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;

public class PicStu implements Serializable, Parcelable {
    public String path;
    public String cover;
    public String name;
    public String dir;

    public PicStu(String path) {
        this.path = path;
        this.cover = path;
        this.name = UtilKotlin.getNameByPath(path);
        this.dir = UtilKotlin.getDirByPath(path);
    }

    public PicStu(String path, String cover) {
        this.path = path;
        this.cover = cover;
        this.name = UtilKotlin.getNameByPath(path);
        this.dir = UtilKotlin.getDirByPath(path);
    }

    protected PicStu(Parcel in) {
        path = in.readString();
        cover = in.readString();
        name = in.readString();
        dir = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(path);
        dest.writeString(cover);
        dest.writeString(name);
        dest.writeString(dir);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<PicStu> CREATOR = new Creator<PicStu>() {
        @Override
        public PicStu createFromParcel(Parcel in) {
            return new PicStu(in);
        }

        @Override
        public PicStu[] newArray(int size) {
            return new PicStu[size];
        }
    };
}