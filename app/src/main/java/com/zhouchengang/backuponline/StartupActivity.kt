package com.zhouchengang.backuponline

import android.content.Intent
import android.os.Bundle
import com.zhouchengang.backuponline.album.HomeActivity
import com.zhouchengang.backuponline.base.BaseActivity
import com.zhouchengang.fileonlinelaunchapp.R

class StartupActivity : BaseActivity(R.layout.activity_startup) {

    override var TAG: String = "启动引导"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        var iIntent = Intent(this, HomeActivity::class.java)
        startActivity(iIntent)
    }
}