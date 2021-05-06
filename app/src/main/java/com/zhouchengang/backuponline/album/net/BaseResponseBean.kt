package com.zhouchengang.backuponline.album.net

class BaseResponseBean<T> {
    val data: T? = null
    var msg = ""
    var code = 0
}