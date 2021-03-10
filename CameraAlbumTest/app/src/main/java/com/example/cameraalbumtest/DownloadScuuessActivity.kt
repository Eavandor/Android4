package com.example.cameraalbumtest

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView

class DownloadScuuessActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_download_scuuess)
        var text1=findViewById<TextView>(R.id.textView10)
        var text2=findViewById<TextView>(R.id.textView11)
        text1.text=intent.getStringExtra("time")
        text2.text=intent.getStringExtra("location")
    var back=findViewById<Button>(R.id.button7)
        back.setOnClickListener {
            var intent =
                Intent(getApplicationContext(), FolderActivity2::class.java)
            intent.putExtra("previous","mainFolder")   //返回主文件夹
            startActivity(intent)
        }
    }
}