package com.zhouchengang.backuponline.album

class HomePageRemote : HomeOneFragment() {
    override fun customTask() {
        getRemotePicList()
    }
}