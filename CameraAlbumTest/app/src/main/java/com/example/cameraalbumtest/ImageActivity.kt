package com.example.cameraalbumtest

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.widget.Button
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.SimpleTarget
import java.io.File
import java.io.FileNotFoundException
import java.io.FileOutputStream
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*

class ImageActivity : AppCompatActivity() {
    companion object {
        lateinit var contextInImageActivity: Context  //把Context存这，在onCreate()方法外，也能用到从onCreate()里传来的Context对象
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_image)
        contextInImageActivity = this     //把context传到companion object里面，类似Java的静态变量，全局都可以用
        var botton = findViewById<Button>(R.id.button5)
        var img = findViewById<ImageView>(R.id.imageView)
        var url: String = intent.getStringExtra("url")
        val imageUri: Uri =
            Uri.parse(url)
        Glide.with(this).load(imageUri)
            .into(img)
        botton.setOnClickListener {
            download(this, url)
        }
    }

    fun download(context: Context, picUrl: String) {
        Glide.with(context).asBitmap().load(picUrl).into(object : SimpleTarget<Bitmap>() {
            override fun onResourceReady(
                resource: Bitmap,
                transition: com.bumptech.glide.request.transition.Transition<in Bitmap>?
            ) {
                //resource即为下载取得的bitmap
                saveBitmap(resource)

            }
        })
    }

    private fun saveBitmap(bitmap: Bitmap) {
        val date = Date()
        val formatter =
            SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
        var time:String=""+formatter.format(date)             //得到日期和时间
        var str=getExternalFilesDir(Environment.DIRECTORY_PICTURES).toString() + "/"+time+".jpg"   //得到存储地址，用日期和时间来命名
        val file = File(
            str
        )
        if (!file.exists()) {
            try {
                file.createNewFile()
            } catch (e: IOException) {
                Toast.makeText(contextInImageActivity, e.message.toString(), Toast.LENGTH_LONG).show()
                e.printStackTrace()
            }
        }
        try {
            val out = FileOutputStream(file)
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, out)
            out.flush()
            out.close()
            Toast.makeText(contextInImageActivity, "下载成功!", Toast.LENGTH_LONG).show()
            var intent =
                Intent(getApplicationContext(), DownloadScuuessActivity::class.java)   //下载成功，返回文件夹体系界面
            intent.putExtra("time","下载时间：   "+time)
            intent.putExtra("location","储存地址： "+str)
            startActivity(intent)
        } catch (e: FileNotFoundException) {
            e.printStackTrace()
            Toast.makeText(contextInImageActivity, e.message.toString(), Toast.LENGTH_LONG).show()
        } catch (e: IOException) {
            e.printStackTrace()
            Toast.makeText(contextInImageActivity, e.message.toString(), Toast.LENGTH_LONG).show()
        }
    }
}