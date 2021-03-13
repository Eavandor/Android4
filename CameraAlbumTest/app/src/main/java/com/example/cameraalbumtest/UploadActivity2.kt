package com.example.cameraalbumtest

import android.content.ContentResolver
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat.startActivity
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.File


class UploadActivity2 {
    fun up(th: Context, f: File,previous:String) {
        val requestFile =
            RequestBody.create(MediaType.parse("application/otcet-stream"), f)
        val body =
            MultipartBody.Part.createFormData(previous, f.name, requestFile)

        val descriptionString = previous
        val description = RequestBody.create(
            MediaType.parse("multipart/form-data"), previous           //指定MediaType为表单型式，用来发送文件，后端用SmartUpload易于接受表单型式的文件传送
        )
        val description2 = RequestBody.create(
            MediaType.parse("multipart/form-data"), FolderActivity2.username //发送文件名
        )
        var retrofit =
            Retrofit.Builder().baseUrl("http://192.168.43.146:8080/untitled1_war/servlet/")
                .addConverterFactory(GsonConverterFactory.create()).build()
        var service = retrofit.create(ImgService::class.java)
        val call: Call<ResponseBody> = service.upload(description,description2, body)
        call.enqueue(
            object : Callback<ResponseBody> {
                override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                }

                override fun onResponse(
                    call: Call<ResponseBody>,
                    response: Response<ResponseBody>
                ) {
                    var intentqaq = Intent(th, FolderActivity2::class.java)
                    intentqaq.putExtra("previous", previous)
                    th.startActivity(intentqaq)                     //previous表示上一级文件夹，表示返回上一级文件夹
                    Toast.makeText(th, "上传成功", Toast.LENGTH_SHORT).show()
                }
            }
        )


    }


}