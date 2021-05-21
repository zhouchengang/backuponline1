package com.zhouchengang.backuponline.album

import android.content.Context
import android.content.Intent
import android.graphics.Rect
import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.blankj.utilcode.util.ConvertUtils
import com.zhouchengang.backuponline.base.BaseActivity
import com.zhouchengang.fileonlinelaunchapp.R
import kotlinx.android.synthetic.main.activity_album.*
import java.util.*

/**
 * @auther zhouchengang
 * @date 2021/3/8
 * @time 11:15.
 */
class AlbumActivity() : BaseActivity(R.layout.activity_album) {
    override var TAG: String = "文件夹"

    companion object {
        const val DIR = "DIR"
        const val DIRSTU = "DirStu"
        fun launch(context: Context, picPath: String?, dirStu: ArrayList<PicStu>?) {
            dirStu?.let {
                val intent = Intent(context, AlbumActivity::class.java)
                intent.putExtra(DIR, picPath)
                intent.putExtra(DIRSTU, it)
                context.startActivity(intent)
            }
        }
    }

    var dir: String? = null
    var dirStu: ArrayList<PicStu>? = null
    private fun parseIntent() {
        intent?.apply {
            dir = getStringExtra(DIR)
            dirStu = getSerializableExtra(DIRSTU) as ArrayList<PicStu>
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        parseIntent()
        dir?.let {
            tv_tip.text = it


            var manager = GridLayoutManager(this, 3)

            gridcycle.layoutManager = manager
            var adapter = PicGridAdapter()
            gridcycle.adapter = adapter
            adapter.setList(dirStu)


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


                    tv_tip.textSize = 20f * currentHeight / maxBase

                    val linearParams = tv_tip.layoutParams
                    linearParams.height = ConvertUtils.dp2px(60f * currentHeight / maxBase)
                    tv_tip.layoutParams = linearParams
                }

                override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                }
            })

            gridcycle.addItemDecoration(object : RecyclerView.ItemDecoration() {
                override fun getItemOffsets(
                    outRect: Rect,
                    view: View,
                    parent: RecyclerView,
                    state: RecyclerView.State
                ) {
                    outRect.apply {
                        val column = (view.layoutParams as GridLayoutManager.LayoutParams).spanIndex
                        left = ConvertUtils.dp2px(if (column == 0) 5f else 5f)
                        right = ConvertUtils.dp2px(if (column == 2) 5f else 5f)
                        top = ConvertUtils.dp2px(3f)
                        bottom = ConvertUtils.dp2px(3f)
                    }
                }
            })


        }
    }

}