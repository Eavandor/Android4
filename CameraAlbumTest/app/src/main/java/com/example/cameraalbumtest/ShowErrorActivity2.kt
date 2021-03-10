package com.example.cameraalbumtest

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView

class ShowErrorActivity2 : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_show_error2)
var mmm=findViewById<TextView>(R.id.errorMsg)
        mmm.text=intent.getStringExtra("msg")
    }
}