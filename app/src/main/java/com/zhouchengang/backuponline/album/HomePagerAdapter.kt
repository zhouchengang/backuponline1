package com.zhouchengang.backuponline.album

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter

/**
 *  @author zhouchengang
 *  @date   2021/2/8
 *  @desc
 */
class HomePagerAdapter(fm: FragmentManager, behavior: Int) : FragmentPagerAdapter(fm, behavior) {

    var tabname: Array<String> = arrayOf("PAGE1", "PAGE2")

    override fun getPageTitle(position: Int): CharSequence? {
        return tabname[position]
    }

    var vp1: Fragment? = null
    var vp2: Fragment? = null

    override fun getItem(position: Int): Fragment {
        if (position == 0) {
            if (vp1 == null) {
                vp1 = HomeOneFragment.newInstance()
            }
            return vp1 as Fragment
        }
        if (position == 1) {
            if (vp2 == null) {
                vp2 = HomeTwoFragment.newInstance()
            }
            return vp2 as Fragment
        }
        return null as Fragment
    }

    override fun getCount(): Int {
        return 2;
    }

}