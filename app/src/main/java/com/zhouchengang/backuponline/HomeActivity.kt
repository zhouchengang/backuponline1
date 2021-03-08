package com.zhouchengang.backuponline

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentPagerAdapter
import com.google.android.material.tabs.TabLayout
import com.zhouchengang.fileonlinelaunchapp.R
import kotlinx.android.synthetic.main.activity_home.*

/**
 *  @author zhouchengang
 *  @date   2021/2/8
 *  @desc
 */
class HomeActivity : BaseActivity(R.layout.activity_home) {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val homePagerAdapter = HomePagerAdapter(
            this.supportFragmentManager,
            FragmentPagerAdapter.BEHAVIOR_SET_USER_VISIBLE_HINT
        )
        vp_home.adapter = homePagerAdapter
        tabs.setupWithViewPager(vp_home)
    }

}