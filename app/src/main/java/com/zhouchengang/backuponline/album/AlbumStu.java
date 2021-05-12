package com.zhouchengang.backuponline.album;


import java.io.Serializable;
import java.util.ArrayList;

/**
 * @author zhouchengang
 * @date 2021/2/9
 * @desc
 */
public class AlbumStu {
    public ArrayList<DirStu> dirList;

    AlbumStu() {
        dirList = new ArrayList<DirStu>();
    }

    public void addPic(String path) {
        addPic(path,path);
    }

    public void addPic(String path,String cover) {
        for (DirStu item : dirList) {
            if (item.dirName.equals(UtilKotlin.getDirByPath(path))) {
                item.addPic(path,cover);
                return;
            }
        }
        dirList.add(new DirStu(UtilKotlin.getDirByPath(path)));
        addPic(path,cover);
    }


    public DirStu getDir(String path) {
        for (DirStu item : dirList) {
            if (item.dirName.equals(UtilKotlin.getDirByPath(path))) {
                return item;
            }
        }
        return new DirStu(UtilKotlin.getDirByPath(path));
    }

    public class DirStu implements Serializable {
        public String dirName;
        public ArrayList<PicStu> picList;

        DirStu(String dirName) {
            this.dirName = dirName;
            picList = new ArrayList<PicStu>();
        }

        public void addPic(String path,String cover) {
            if (UtilKotlin.getDirByPath(path).equals(dirName)) {
                picList.add(new PicStu(path,cover));
            }
        }

        public String getCover() {
            if (picList == null || picList.isEmpty()) {
                return "";
            } else {
                return picList.get(0).cover;
            }
        }
    }

}


