package com.zhouchengang.backuponline

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.zhouchengang.fileonlinelaunchapp.R

/**
 *  @author zhouchengang
 *  @date   2021/2/8
 *  @desc
 */
open class BaseActivity(Res :Int): AppCompatActivity(Res) {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //黑色状态栏字体
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
    }
}