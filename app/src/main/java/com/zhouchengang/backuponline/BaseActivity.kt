package com.zhouchengang.backuponline

import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import com.gyf.immersionbar.ImmersionBar
import com.r0adkll.slidr.Slidr
import com.r0adkll.slidr.model.SlidrConfig
import com.r0adkll.slidr.model.SlidrInterface
import com.r0adkll.slidr.model.SlidrPosition
import com.zhouchengang.fileonlinelaunchapp.R
import java.lang.reflect.Field


/**
 *  @author zhouchengang
 *  @date   2021/2/8
 *  @desc
 */
open class BaseActivity(Res: Int) : AppCompatActivity() {
    var Res: Int = 0

    init {
        this.Res = Res
    }

    lateinit var slidrInterface: SlidrInterface
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        var view = View.inflate(this, Res, null)

        view.setPadding(
            view.paddingLeft,
            view.paddingTop + getStatusBarHeight(this),
            view.paddingTop,
            view.paddingBottom
        )

        setContentView(view)
        //黑色状态栏字体
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);

        var config: SlidrConfig = SlidrConfig.Builder()
            .position(SlidrPosition.LEFT)
            .sensitivity(1f)
            .scrimColor(Color.BLACK)
            .scrimStartAlpha(0.6f)
            .scrimEndAlpha(0f)
            .build()
        slidrInterface = Slidr.attach(this, config)
        //showStatusBar()

        ImmersionBar.with(this)
            .transparentStatusBar()  //透明状态栏，不写默认透明色
            .statusBarDarkFont(true)   //状态栏字体是深色，不写默认为亮色
            .init();

        overridePendingTransition(R.anim.transition_bottom_up, R.anim.transition_bottom_silent)
    }

    //是否使用滑动返回
    fun enableSwipeBack(enableSwipeBack: Boolean) {
        if (enableSwipeBack) {
            slidrInterface.unlock()
        } else {
            slidrInterface.lock()
        }
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

    override fun finish() {
        super.finish()
        overridePendingTransition(R.anim.transition_bottom_silent, R.anim.transition_bottom_down)
    }

}

