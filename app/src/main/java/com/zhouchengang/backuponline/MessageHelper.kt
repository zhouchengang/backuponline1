package com.zhouchengang.backuponline

import android.app.Activity


object MessageHelper {
    private val map = HashMap<String, HashMap<Activity, () -> Unit>>()
    fun register(activity: Activity, messageName: String, event: () -> Unit) {
        if (!map.containsKey(messageName)) {
            map[messageName] = HashMap()
        }
        map[messageName]?.put(activity, event)
    }


    fun unRegister(activity: Activity, messageName: String, event: () -> Unit) {
        map[messageName]?.remove(activity)
    }

    fun leave() {

    }

}