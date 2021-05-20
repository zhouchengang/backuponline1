package com.zhouchengang.backuponline.album

import android.database.Cursor
import android.graphics.Rect
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.View
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.blankj.utilcode.util.ConvertUtils
import com.google.gson.Gson
import com.zhouchengang.backuponline.album.net.BaseBean
import com.zhouchengang.backuponline.album.net.GetStringBo
import com.zhouchengang.fileonlinelaunchapp.R
import kotlinx.android.synthetic.main.fragment_home_one.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

/**
 *  @author zhouchengang
 *  @date   2021/2/8
 *  @desc
 */
abstract class HomeOneFragment : Fragment(R.layout.fragment_home_one) {
    companion object {

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    lateinit var mAdapter: DirGridAdapter
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        var manager = GridLayoutManager(context, 3)

        gridcycle.layoutManager = manager
        mAdapter = DirGridAdapter()
        gridcycle.adapter = mAdapter



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
        customTask()
    }

    abstract fun customTask()

    protected fun getLocalPicFile() {
        var albumInfo = AlbumStu()

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
                var pathh = String(data, 0, data.size - 1)
                albumInfo.addPic(pathh)
            }
            cursor.close()
        }
        mAdapter.addData(albumInfo.dirList)
    }


    protected fun getRemotePicList() {
        val re: Retrofit = Retrofit.Builder().baseUrl(UtilKotlin.UP_URL)
            .addConverterFactory(GsonConverterFactory.create()).build()
        val getService = re.create(GetService::class.java)
        getService.getAlbum("userid", "password")
            .enqueue(object : Callback<BaseBean<GetStringBo>> {
                override fun onResponse(
                    call: Call<BaseBean<GetStringBo>>,
                    response: Response<BaseBean<GetStringBo>>
                ) {
                    Log.e("http response", response.toString())
                    Log.e("http response", Gson().toJson(response))

                    var albumInfo = AlbumStu()

                    if (response.isSuccessful) {
                        response.body()?.data?.picList?.let {
                            for (item in it) {
                                item?.path?.let { path ->
                                    albumInfo.addPic(
                                        UtilKotlin.getPicUrl(path),
                                        UtilKotlin.getCoverUrl(path)
                                    )
                                }
                            }
                        }
                    }
                    mAdapter.addData(albumInfo.dirList)
                }

                override fun onFailure(call: Call<BaseBean<GetStringBo>>, t: Throwable) {

                }

            })

    }

}