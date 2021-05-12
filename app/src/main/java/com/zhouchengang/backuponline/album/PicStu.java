package com.zhouchengang.backuponline.album;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;

public class PicStu implements Serializable {
    public String path;
    public String cover;
    public String name;
    public String dir;

    public PicStu(String path, String cover) {
        this.path = path;
        this.cover = cover;
        this.name = UtilKotlin.getNameByPath(path);
        this.dir = UtilKotlin.getDirByPath(path);
    }
}