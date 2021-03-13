package com.example.cameraalbumtest

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface LogoutService {
    @GET("Delete")
    fun logout( @Query("owner") owner: String): Call<FeedBack>
}