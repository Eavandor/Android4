package com.example.cameraalbumtest

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface AccountService {

   @GET("Database")
fun sendAccountMsg(@Query("aim") aim: String,@Query("name") name: String,@Query("password") pwd: String): Call<FeedBack>

}