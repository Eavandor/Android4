package com.example.cameraalbumtest

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
class CreateFolderActivity2 : AppCompatActivity() {
    companion object{lateinit  var contextInCreateFolderActivity2: Context}
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_folder2)
        contextInCreateFolderActivity2=this
        var createF=findViewById<Button>(R.id.button4)
        createF.setOnClickListener {
            var newFolderName = findViewById<EditText>(R.id.folderName).text.toString()
            var previous: String = intent.getStringExtra("previous")
            val accountService = LoginActivity.retrofit.create(FeedbackService::class.java)
            accountService.sendAccountMsg(newFolderName, FolderActivity2.username, "f", previous).enqueue(
                object : Callback<FeedBack> {
                    override fun onFailure(call: Call<FeedBack>, t: Throwable) {
//                        Toast.makeText(contextInCreateFolderActivity2, "创建文件夹过程出现未知错误", Toast.LENGTH_SHORT).show();
                        var intent =
                            Intent(
                                contextInCreateFolderActivity2,
                                FolderActivity2::class.java
                            )
                        intent.putExtra("previous",previous)
                        startActivity(intent)
                    }
                    override fun onResponse(call: Call<FeedBack>, response: Response<FeedBack>) {
                        Toast.makeText(contextInCreateFolderActivity2, "创建成功", Toast.LENGTH_SHORT).show();
                        var intent =
                            Intent(
                                contextInCreateFolderActivity2 ,
                                FolderActivity2::class.java
                            )
                        intent.putExtra("previous",previous)
                        startActivity(intent)
                    }
                }
            )
        }
    }
}