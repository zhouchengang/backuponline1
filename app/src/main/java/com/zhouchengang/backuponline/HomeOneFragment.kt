package com.zhouchengang.backuponline

import android.database.Cursor
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.View
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import com.google.gson.Gson
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
open class HomeOneFragment : Fragment(R.layout.fragment_home_one) {
    companion object{
        fun newInstance() :HomeOneFragment{
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
        var adapter =PicGridAdapter()
        gridcycle.adapter = adapter
        adapter.addData(getLocalPicFile())

    }

    fun getLocalPicFile(): List<String> {
        var picList :ArrayList<String> = ArrayList<String>()
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