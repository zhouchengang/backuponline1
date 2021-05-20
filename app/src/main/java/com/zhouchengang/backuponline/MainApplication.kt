package com.zhouchengang.backuponline

import android.app.Activity
import android.app.Application
import android.os.Bundle
import android.util.Log
import com.blankj.utilcode.util.LogUtils

class MainApplication : Application() {
    companion object {
        var currentActivity: Activity
            get() = currentActivity
            set(value) {}
    }


    override fun onCreate() {
        super.onCreate()
        registerActivityLifecycleCallbacks(object : ActivityLifecycleCallbacks {
            override fun onActivityCreated(activity: Activity, savedInstanceState: Bundle?) {
                Log.e("MainApplication", "onActivityCreated")
            }

            override fun onActivityStarted(activity: Activity) {
                Log.e("MainApplication", "onActivityStarted")
            }

            override fun onActivityResumed(activity: Activity) {
                Log.e("MainApplication", "onActivityResumed")
            }

            override fun onActivityPaused(activity: Activity) {
                Log.e("MainApplication", "onActivityPaused")
            }

            override fun onActivityStopped(activity: Activity) {
                Log.e("MainApplication", "onActivityStopped")
            }

            override fun onActivitySaveInstanceState(activity: Activity, outState: Bundle) {
                Log.e("MainApplication", "onActivitySaveInstanceState")
            }

            override fun onActivityDestroyed(activity: Activity) {
                Log.e("MainApplication", "onActivityDestroyed")
            }

        })
    }

}