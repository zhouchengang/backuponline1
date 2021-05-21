package com.zhouchengang.backuponline.album

import android.Manifest
import android.app.AlertDialog
import android.graphics.Color
import android.os.Bundle
import androidx.fragment.app.FragmentPagerAdapter
import com.blankj.utilcode.util.PermissionUtils
import com.zhouchengang.backuponline.base.BaseActivity
import com.zhouchengang.fileonlinelaunchapp.R
import kotlinx.android.synthetic.main.activity_home.*


/**
 *  @author zhouchengang
 *  @date   2021/2/8
 *  @desc
 */
class HomeActivity : BaseActivity(R.layout.activity_home, useSlideBack = false) {

    override var TAG: String = "主页"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initView()
    }

    override fun onBackPressed() {
//        super.onBackPressed()
//        val intent = Intent()
//        intent.action = Intent.ACTION_MAIN
//        intent.addCategory(Intent.CATEGORY_HOME)
//        startActivity(intent)

//        MainApplication.destoryAll()
        AlertDialog.Builder(this).apply {
            setTitle("This is Dialog")
            setMessage("Something important.")
            setCancelable(false)
            setPositiveButton("OK") { dialog, which ->
                dialog.dismiss()
            }
            setNegativeButton("Cancel") { dialog, which ->
                dialog.dismiss()
            }
            show()
        }
    }

    private fun initView() {
        if (!PermissionUtils.isGranted(Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
            PermissionUtils.permission(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                .rationale { activity, shouldRequest ->
                    //没有权限提示
                }.callback(object : PermissionUtils.SimpleCallback {
                    override fun onGranted() {
                        initView()
                    }

                    override fun onDenied() {
                        //没有权限提示
                    }

                }).request()
        } else {
            val homePagerAdapter = HomePagerAdapter(
                this.supportFragmentManager,
                FragmentPagerAdapter.BEHAVIOR_SET_USER_VISIBLE_HINT
            )
            vp_home.adapter = homePagerAdapter
            tabs.setupWithViewPager(vp_home)
        }
    }

}