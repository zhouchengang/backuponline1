package com.zhouchengang.backuponline

import android.app.Activity
import android.app.Application
import android.os.Bundle
import android.util.Log
import com.zhouchengang.fileonlinelaunchapp.BuildConfig

data class BaseActivityData(
    val activity: Activity,
    val messages: HashMap<String, (str: String) -> Unit>
)

class MainApplication : Application() {
    companion object {
        var currentActivity: Activity? = null
        var mActivities = ArrayList<BaseActivityData>()

        @JvmStatic
        fun destoryAll() {
            for (item in mActivities) {
                if (item.activity != currentActivity)
                    item.activity.finish()
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


        fun registerMessageListenerForActivity(
            activity: Activity,
            messageName: String,
            MessageListener: (str: String) -> Unit
        ) {
            for (item in mActivities) {
                if (activity == item.activity)
                    item.messages[messageName] = MessageListener
            }
        }


        fun responseMessageByMessage(messageName: String, messageStr: String) {
            for (item in mActivities) {
                item.messages[messageName]?.invoke(messageStr)
            }
        }

        fun removeMessage(activity: Activity, messageName: String) {
            for (item in mActivities.reversed()) {
                if (item.activity == activity) {
                    item.messages.remove(messageName)
                }
            }
        }

    }


    override fun onCreate() {
        super.onCreate()
        registerActivityLifecycleCallbacks(object : ActivityLifecycleCallbacks {
            override fun onActivityCreated(activity: Activity, savedInstanceState: Bundle?) {
                mActivities.add(BaseActivityData(activity, HashMap()))
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
                mActivities.removeIf { it.activity == activity }
                loge("onActivityDestroyed", activity.javaClass)
                loge(mActivities)
            }

        })
    }

}