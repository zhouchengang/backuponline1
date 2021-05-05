package com.zhouchengang.backuponline.album

import android.database.Cursor
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.blankj.utilcode.util.ConvertUtils
import com.zhouchengang.fileonlinelaunchapp.R
import kotlinx.android.synthetic.main.fragment_home_one.*

/**
 *  @author zhouchengang
 *  @date   2021/2/8
 *  @desc
 */
open class HomeOneFragment : Fragment(R.layout.fragment_home_one) {
    companion object {
        fun newInstance(): HomeOneFragment {
            return HomeOneFragment()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        var manager = GridLayoutManager(context, 3)

        gridcycle.layoutManager = manager
        var adapter = PicGridAdapter()
        gridcycle.adapter = adapter
        adapter.addData(getLocalPicFile())


        gridcycle.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            var maxBase = 12000
            var currentHeight = maxBase
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                currentHeight -= dy
                if (currentHeight > maxBase) {
                    currentHeight = maxBase
                }
                if (currentHeight < maxBase * 0.6) {
                    currentHeight = (maxBase * 0.6).toInt()
                }


                tv_tip.textSize = 40f * currentHeight / maxBase

                val linearParams = tv_tip.layoutParams
//                linearParams.height = ViewGroup.LayoutParams.WRAP_CONTENT
                linearParams.height = ConvertUtils.dp2px(60f * currentHeight / maxBase)
                tv_tip.layoutParams = linearParams
            }

            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
            }
        })
    }

    fun getLocalPicFile(): List<String> {
        var picList: ArrayList<String> = ArrayList<String>()
        var cursor: Cursor? = context?.contentResolver?.query(
            MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
            null,
            null,
            null,
            null
        )
        cursor?.let {
            while (cursor.moveToNext()) {
                val data: ByteArray =
                    cursor.getBlob(cursor.getColumnIndex(MediaStore.Images.Media.DATA))
                var pathh = String(data, 0, data.size - 1);
                picList.add(pathh)
            }
            cursor.close()
        }

        return picList.asReversed()
    }


}