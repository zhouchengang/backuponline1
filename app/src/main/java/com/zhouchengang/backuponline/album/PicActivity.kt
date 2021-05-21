package com.zhouchengang.backuponline.album

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.webkit.WebChromeClient
import com.zhouchengang.backuponline.base.BaseActivity
import com.zhouchengang.fileonlinelaunchapp.R
import kotlinx.android.synthetic.main.activity_pic.*

/**
 * @auther zhouchengang
 * @date 2021/3/8
 * @time 11:15.
 */
class PicActivity : BaseActivity(R.layout.activity_pic) {

    override var TAG: String = "图片详情"

    companion object {
        const val PIC_PATH = "PIC_PATH"
        fun launch(context: Context, picPath: String?) {
            picPath?.let {
                val intent = Intent(context, PicActivity::class.java)
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
//        picPath?.let {
//            Glide.with(this)
//                .load(it)
//                .into(iv_content)
//        }


        //添加浏览器代理
        webpage.setWebChromeClient(object : WebChromeClient() {
            override fun onShowCustomView(view: View, callback: CustomViewCallback) {
                super.onShowCustomView(view, callback);
            }

            override fun onHideCustomView() {
                super.onHideCustomView();
            }
        })

        webpage.loadUrl(picPath)
        webpage.getSettings().setJavaScriptEnabled(true);
        webpage.getSettings().setSupportZoom(true);


        webpage.getSettings().setUseWideViewPort(true);
        webpage.getSettings().setLoadWithOverviewMode(true);
        webpage.getSettings().setAllowFileAccess(true);
        webpage.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);

    }
}