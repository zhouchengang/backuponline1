package com.zhouchengang.backuponline.album

class HomePageLocal : HomeOneFragment() {
    override fun customTask() {
        getLocalPicFile()
    }
}