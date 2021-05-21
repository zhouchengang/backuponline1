package com.zhouchengang.backuponline

import android.app.Activity
import android.app.Application
import android.os.Bundle
import android.util.Log
import com.zhouchengang.fileonlinelaunchapp.BuildConfig

class MainApplication : Application() {
    companion object {
        var currentActivity: Activity? = null
        var mActivities = ArrayList<Activity>()

        @JvmStatic
        fun destoryAll() {
            for (item in mActivities) {
                if (item != currentActivity)
                    item.finish()
            }
        }

        fun loge(vararg obj: Any) {
            var objClone = obj.clone()
            if (!BuildConfig.DEBUG) return
            val sb = StringBuilder()
            for ((index, item) in objClone.withIndex()) {
                if (index != 0)
                    sb.append(" , ")
                sb.append(item.toString())
            }
            Log.e("e", sb.toString())
        }

    }

    override fun onCreate() {
        super.onCreate()
        registerActivityLifecycleCallbacks(object : ActivityLifecycleCallbacks {
            override fun onActivityCreated(activity: Activity, savedInstanceState: Bundle?) {
                mActivities.add(activity)
                loge("onActivityCreated", activity.javaClass)
                loge(mActivities)
            }

            override fun onActivityStarted(activity: Activity) {
                loge("onActivityStarted", activity.javaClass)
            }

            override fun onActivityResumed(activity: Activity) {
                loge("onActivityResumed", activity.javaClass)
            }

            override fun onActivityPaused(activity: Activity) {
                loge("onActivityPaused", activity.javaClass)
            }

            override fun onActivityStopped(activity: Activity) {
                loge("onActivityStopped", activity.javaClass)
            }

            override fun onActivitySaveInstanceState(activity: Activity, outState: Bundle) {
                loge("onActivitySaveInstanceState", activity.javaClass)
            }

            override fun onActivityDestroyed(activity: Activity) {
                mActivities.remove(activity)
                loge("onActivityDestroyed", activity.javaClass)
                loge(mActivities)
            }

        })
    }

}