package com.zhouchengang.backuponline.album;


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
        for (DirStu item : dirList) {
            if (item.dirName.equals(UtilKotlin.getDirByPath(path))) {
                item.addPic(path);
                return;
            }
        }
        dirList.add(new DirStu(UtilKotlin.getDirByPath(path)));
        addPic(path);
    }


    public DirStu getDir(String path) {
        for (DirStu item : dirList) {
            if (item.dirName.equals(UtilKotlin.getDirByPath(path))) {
                return item;
            }
        }
        return new DirStu(UtilKotlin.getDirByPath(path));
    }

    public class DirStu {
        public String dirName;
        public ArrayList<PicStu> picList;

        DirStu(String dirName) {
            this.dirName = dirName;
            picList = new ArrayList<PicStu>();
        }

        public void addPic(String path) {
            if (UtilKotlin.getDirByPath(path).equals(dirName)) {
                picList.add(new PicStu(path));
            }
        }

        public String getCover() {
            if (picList == null || picList.isEmpty()) {
                return "";
            } else {
                return picList.get(0).path;
            }
        }
    }

    public class PicStu {
        public String path;
        public String name;
        public String dir;

        PicStu(String path) {
            this.path = path;
            this.name = UtilKotlin.getNameByPath(path);
            this.dir = UtilKotlin.getDirByPath(path);
        }
    }

}


