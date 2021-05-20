package com.zhouchengang.backuponline

import android.app.Activity
import android.app.Application
import android.os.Bundle
import android.util.Log

class MainApplication : Application() {
    companion object {
        var currentActivity: Activity? = null

    }


    override fun onCreate() {
        super.onCreate()
        registerActivityLifecycleCallbacks(object : ActivityLifecycleCallbacks {
            override fun onActivityCreated(activity: Activity, savedInstanceState: Bundle?) {
                Log.d("MainApplication", "onActivityCreated")
            }

            override fun onActivityStarted(activity: Activity) {
                Log.d("MainApplication", "onActivityStarted")
            }

            override fun onActivityResumed(activity: Activity) {
                Log.d("MainApplication", "onActivityResumed")
                currentActivity = activity
            }

            override fun onActivityPaused(activity: Activity) {
                Log.d("MainApplication", "onActivityPaused")
            }

            override fun onActivityStopped(activity: Activity) {
                Log.d("MainApplication", "onActivityStopped")
            }

            override fun onActivitySaveInstanceState(activity: Activity, outState: Bundle) {
                Log.d("MainApplication", "onActivitySaveInstanceState")
            }

            override fun onActivityDestroyed(activity: Activity) {
                Log.e("MainApplication", "onActivityDestroyed")
            }

        })
    }

}