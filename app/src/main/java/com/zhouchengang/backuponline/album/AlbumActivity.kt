package com.zhouchengang.backuponline.album

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import com.bumptech.glide.Glide
import com.zhouchengang.backuponline.base.BaseActivity
import com.zhouchengang.fileonlinelaunchapp.R
import kotlinx.android.synthetic.main.activity_album.*

/**
 * @auther zhouchengang
 * @date 2021/3/8
 * @time 11:15.
 */
class AlbumActivity : BaseActivity(R.layout.activity_album) {
    companion object {
        const val PIC_PATH = "PIC_PATH"
        fun launch(context: Context, picPath: String?) {
            picPath?.let {
                val intent = Intent(context, AlbumActivity::class.java)
                intent.putExtra(PIC_PATH, it)
                context.startActivity(intent)
            }
        }
    }

    var picPath: String? = null
    private fun parseIntent() {
        intent?.apply {
            picPath = getStringExtra(PIC_PATH)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        parseIntent()
        picPath?.let {
            Glide.with(this)
                .load(it)
                .into(iv_content)
        }

    }
}