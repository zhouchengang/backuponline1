package com.zhouchengang.backuponline.album;

public class PicStu {
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
}