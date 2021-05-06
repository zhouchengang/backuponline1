package com.zhouchengang.backuponline.album

public class UtilKotlin {
    companion object {
        fun getDirByPath(path: String): String {
            return path.substring(0, path.lastIndexOf('/') + 1)
        }

        fun getNameByPath(path: String): String {
            return path.substring(path.lastIndexOf('/') + 1)
        }

    }
}