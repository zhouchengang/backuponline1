package com.zhouchengang.backuponline.album.net

import java.io.Serializable

open class BaseBean<T>() : Serializable {
    var code: Int = 0
    var message: String? = null
    var data: T? = null
}


class GetStringBean : BaseBean<GetStringBo>(), Serializable

class GetStringBo : Serializable {
    var item1: String? = null
    var item2: Int = 0
    var picList: ArrayList<FileBo?>? = null
}

class FileBo : Serializable {
    var online: Int = 0
    var path: String? = null
}
