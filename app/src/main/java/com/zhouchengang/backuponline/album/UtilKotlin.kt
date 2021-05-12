package com.zhouchengang.backuponline.album


object UtilKotlin {

    @JvmStatic
    fun getDirByPath(path: String): String {
        return path.substring(0, path.lastIndexOf('/') + 1)
    }

    @JvmStatic
    fun getNameByPath(path: String): String {
        return path.substring(path.lastIndexOf('/') + 1)
    }

    @JvmStatic
    fun getCoverUrl(path :String) ="http://192.168.0.113:8889/$path"


    @JvmStatic
    fun getPicUrl(path :String) ="http://192.168.0.113:8888/$path"

}