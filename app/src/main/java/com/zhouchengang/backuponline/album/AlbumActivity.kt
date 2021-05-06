package com.zhouchengang.backuponline.album

import android.content.Context
import android.content.Intent
import android.database.Cursor
import android.graphics.Rect
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.blankj.utilcode.util.ConvertUtils
import com.zhouchengang.backuponline.base.BaseActivity
import com.zhouchengang.fileonlinelaunchapp.R
import kotlinx.android.synthetic.main.activity_album.gridcycle
import kotlinx.android.synthetic.main.activity_album.tv_tip

/**
 * @auther zhouchengang
 * @date 2021/3/8
 * @time 11:15.
 */
class AlbumActivity : BaseActivity(R.layout.activity_album) {
    companion object {
        const val DIR = "DIR"
        fun launch(context: Context, picPath: String?) {
            picPath?.let {
                val intent = Intent(context, AlbumActivity::class.java)
                intent.putExtra(DIR, it)
                context.startActivity(intent)
            }
        }
    }

    var dir: String? = null
    private fun parseIntent() {
        intent?.apply {
            dir = getStringExtra(DIR)
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
            adapter.setList(getLocalPicFile(it))


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


    fun getLocalPicFile(dir: String): Collection<AlbumStu.PicStu> {
        var albumInfo = AlbumStu()

        var cursor: Cursor? = contentResolver?.query(
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
                var pathh = String(data, 0, data.size - 1)
                albumInfo.addPic(pathh)
            }
            cursor.close()
        }

        return albumInfo.getDir(dir).picList
    }

}