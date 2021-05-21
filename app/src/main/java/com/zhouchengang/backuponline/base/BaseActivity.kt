package com.zhouchengang.backuponline.base

import android.app.Activity
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.annotation.LayoutRes
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.iterator
import com.gyf.immersionbar.ImmersionBar
import com.zhouchengang.backuponline.MainApplication
import com.zhouchengang.backuponline.MainApplication.Companion.loge
import com.zhouchengang.backuponline.album.SlideBackConstraintLayout
import com.zhouchengang.fileonlinelaunchapp.R
import java.lang.reflect.Field


/**
 *  @author zhouchengang
 *  @date   2021/2/8
 *  @desc
 */

var BaseActivity.TAA: String
    get() = "ttttt"
    set(value) {
        TAA = value
    }

class CustomReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        loge("onReceive onReceive onReceive")
    }
}


open abstract class BaseActivity(
    @LayoutRes contentLayoutId: Int,
    private val useBlackStatusBarTextColor: Boolean = true,
    private val useSlideBack: Boolean = true,
    private val useTransparentStatusBar: Boolean = true,
    private val useBroadcast: Boolean = false
) : AppCompatActivity(contentLayoutId) {
    abstract var TAG: String
    lateinit var timeChangeReceiver: TimeChangeReceiver

    inner class TimeChangeReceiver : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            onBoardCastReceived()
        }
    }

    fun onBoardCastReceived() {
        loge("onBoardCastReceived")
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        overridePendingTransition(R.anim.activity_right_in, R.anim.anim_stay)
        super.onCreate(savedInstanceState)

        //黑色状态栏字体
        if (useBlackStatusBarTextColor) {
            window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
        }

        if (useSlideBack) {
            for (item in (findViewById<View>(android.R.id.content) as ViewGroup)) {
                if (item is SlideBackConstraintLayout) {
                    item.onSwipeOff = {
                        finish()
                    }
//                item.show()
                    break
                }
            }
        }


        //配置透明状态栏
        if (useTransparentStatusBar) {
            ImmersionBar.with(this)
                .transparentStatusBar()  //透明状态栏，不写默认透明色
                .statusBarDarkFont(true)   //状态栏字体是深色，不写默认为亮色
                .fitsSystemWindows(false)
                .init()

            getRootView(this)?.setBackgroundColor(Color.parseColor("#FFFFFFFF"))
            getRootView(this)?.fitsSystemWindows = true
        }



        if (useBroadcast) {
            val intentFilter = IntentFilter()
            intentFilter.addAction("android.intent.action.TIME_TICK")
            timeChangeReceiver = TimeChangeReceiver()
            registerReceiver(timeChangeReceiver, intentFilter)
        }
    }

    fun addMessageListener(messageName: String, messageListener: (str: String) -> Unit) {
        MainApplication.registerMessageListenerForActivity(this, messageName, messageListener)
    }

    fun sendMessage(messageName: String, message: String) {
        MainApplication.responseMessageByMessage(messageName, message)
    }

    private fun getRootView(context: Activity): View? {
        return (context.findViewById<View>(android.R.id.content) as ViewGroup).getChildAt(0)
    }


    //全屏并且隐藏状态栏
    fun hideStatusBar() {
        val attrs = window.attributes
        attrs.flags = attrs.flags or WindowManager.LayoutParams.FLAG_FULLSCREEN
        window.attributes = attrs
    }

    //全屏并且状态栏透明显示
    fun showStatusBar() {
        val attrs = window.attributes
        attrs.flags = attrs.flags and WindowManager.LayoutParams.FLAG_FULLSCREEN.inv()
        window.attributes = attrs
    }

    //获取手机状态栏高度
    open fun getStatusBarHeight(context: Context): Int {
        var c: Class<*>? = null
        var obj: Any? = null
        var field: Field? = null
        var x = 0
        var statusBarHeight = 0
        try {
            c = Class.forName("com.android.internal.R\$dimen")
            obj = c.newInstance()
            field = c.getField("status_bar_height")
            x = field.get(obj).toString().toInt()
            statusBarHeight = context.getResources().getDimensionPixelSize(x)
        } catch (e1: Exception) {
            e1.printStackTrace()
        }
        return statusBarHeight
    }

    override fun onBackPressed() {
        for (item in (findViewById<View>(android.R.id.content) as ViewGroup)) {
            if (item is SlideBackConstraintLayout) {
                item.dismiss()
                return
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        if (useBroadcast)
            if (::timeChangeReceiver.isInitialized)
                unregisterReceiver(timeChangeReceiver)
    }

    override fun toString(): String {
        return TAG + super.toString()
    }
}

