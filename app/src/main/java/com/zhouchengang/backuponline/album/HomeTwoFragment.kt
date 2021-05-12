package com.zhouchengang.backuponline.album

import android.graphics.Rect
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.blankj.utilcode.util.ConvertUtils
import com.google.gson.Gson
import com.zhouchengang.backuponline.album.UtilKotlin.getCoverUrl
import com.zhouchengang.backuponline.album.UtilKotlin.getPicUrl
import com.zhouchengang.backuponline.album.net.*
import com.zhouchengang.fileonlinelaunchapp.R
import kotlinx.android.synthetic.main.activity_album.*
import kotlinx.android.synthetic.main.fragment_home_one.*
import kotlinx.android.synthetic.main.fragment_home_one.gridcycle
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
class HomeTwoFragment : Fragment(R.layout.fragment_home_two) {
    companion object {
        fun newInstance(): HomeTwoFragment {
            return HomeTwoFragment()
        }
    }


    lateinit var adapter: DirGridAdapter
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        var manager = GridLayoutManager(context, 3)

        gridcycle.layoutManager = manager
        adapter = DirGridAdapter()
        gridcycle.adapter = adapter
        //adapter.addData(getLocalPicFile())


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


        getNormal()

    }

    fun getNormal() {
        val re: Retrofit = Retrofit.Builder().baseUrl(Utils.UP_URL)
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
//                                    adapter.addData(PicStu(getPicUrl(path), getCoverUrl(path)))
                                    albumInfo.addPic(getPicUrl(path))
                                }
                            }
                        }
                    }
                    adapter.setList(albumInfo.dirList)
                }

                override fun onFailure(call: Call<BaseBean<GetStringBo>>, t: Throwable) {


                }

            })

    }

}