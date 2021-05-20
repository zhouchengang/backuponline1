package com.zhouchengang.backuponline.album

class HomePageMix : HomeOneFragment() {
    override fun customTask() {
        getLocalPicFile()
        getRemotePicList()
    }
}