package com.example.cameraalbumtest

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface ShowService {
    @GET("ShowManager")
    fun sendAccountMsg(@Query("stuff_name") stuff_name: String, @Query("owner") owner: String, @Query("type") type: String, @Query("previous") previous: String): Call<List<Db>>
}