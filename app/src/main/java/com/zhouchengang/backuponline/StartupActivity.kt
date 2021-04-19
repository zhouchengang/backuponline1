package com.zhouchengang.backuponline

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.zhouchengang.backuponline.album.HomeActivity
import com.zhouchengang.fileonlinelaunchapp.R

class StartupActivity : AppCompatActivity(R.layout.activity_startup) {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        var iIntent = Intent(this, HomeActivity::class.java)
        startActivity(iIntent)
    }
}