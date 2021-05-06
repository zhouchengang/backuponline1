package com.zhouchengang.backuponline.album

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import com.zhouchengang.backuponline.album.net.AlbumBO
import com.zhouchengang.backuponline.album.net.BaseResponseBean
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
class HomeTwoFragment : Fragment(R.layout.fragment_home_two) {
    companion object {
        fun newInstance(): HomeTwoFragment {
            return HomeTwoFragment()
        }
    }


    lateinit var adapter: PicGridOnlineAdapter
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        var manager = GridLayoutManager(context, 3)

        gridcycle.layoutManager = manager
        adapter = PicGridOnlineAdapter()
        gridcycle.adapter = adapter
        //adapter.addData(getLocalPicFile())

        getNormal()

    }

    fun getNormal() {
        val re: Retrofit = Retrofit.Builder().baseUrl(Utils.UP_URL)
            .addConverterFactory(GsonConverterFactory.create()).build()
        //Retrofit re= new Retrofit.Builder().baseUrl(Utils.BASE_URL).build();
        val getService = re.create(GetService::class.java)
        getService.getAlbum("userid", "password")
            .enqueue(object : Callback<BaseResponseBean<AlbumBO>> {
                override fun onResponse(
                    call: Call<BaseResponseBean<AlbumBO>>,
                    response: Response<BaseResponseBean<AlbumBO>>
                ) {
                    if (response.isSuccessful) {
                        //返回的数据：{"code":1,"msg":"success","data":{"name":"leavesC","mobile":123456}}
                        response.body()?.data?.picList?.let {
                            for (item in it) {
                                Log.e("ZCG", "url :$item")
//                            if (Utils.isNotDir(b.url))
//                                adapter.addData(b.url)
                            }
                        }
                    }
                }

                override fun onFailure(call: Call<BaseResponseBean<AlbumBO>>, t: Throwable) {
                    Log.e("httptest", "onFailure: " + t.message)
                }

            })

    }

}