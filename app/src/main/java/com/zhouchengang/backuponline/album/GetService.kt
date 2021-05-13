package com.zhouchengang.backuponline.album

import com.zhouchengang.backuponline.album.net.BaseBean
import com.zhouchengang.backuponline.album.net.GetStringBo
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface GetService {
//    @get:GET("Get/OSS")
//    val normal: Call<BaseResponseBean<AlbumBO>>
//
//    //不带任何参数的 Get 请求
//    @GET("uploads")
//    fun getGson(@Query("name") picfilename: String): Call<ResponseBody>
//
//    //携带请求参数的 Get 请求
//    @GET("uploadss")
//    fun getPic(@Query("name") picfilename: String): Call<ResponseBody>

    //携带请求参数的 Get 请求
    @GET("get/string")
    fun getAlbum(
        @Query("userID") picfilename: String,
        @Query("password") picfile: String
    ): Call<BaseBean<GetStringBo>> //携带请求参数的 Get 请求
}