package com.zhouchengang.backuponline.album;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * 描述:
 * demoone-
 *
 * @Author J_jiasheng@qq.com
 * @create 2020-12-15 9:49
 */
public interface GetService {
    @GET("Get/OSS")
    Call<Stu> getNormal();
    //不带任何参数的 Get 请求

    @GET("uploads")
    Call<ResponseBody> getGson(@Query("name") String picfilename);
    //携带请求参数的 Get 请求

    @GET("uploadss")
    Call<ResponseBody> getPic(@Query("name") String picfilename);
    //携带请求参数的 Get 请求



    @GET("send")
    Call<ResponseBody> getWithPic(@Query("name") String picfilename, @Query("name") String picfile);
    //携带请求参数的 Get 请求


}
