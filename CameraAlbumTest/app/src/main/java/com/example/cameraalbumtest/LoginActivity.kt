package com.example.cameraalbumtest

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import okhttp3.OkHttpClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

class LoginActivity : AppCompatActivity() {

    companion object {

val client: OkHttpClient=OkHttpClient.Builder().connectTimeout(5,TimeUnit.SECONDS)
    .readTimeout(5,TimeUnit.SECONDS).writeTimeout(5,TimeUnit.SECONDS).build()   //设置各种超时都是5秒钟

        var retrofit =
            Retrofit.Builder().baseUrl("http://192.168.43.146:8080/untitled1_war/servlet/").client(
                client)
                .addConverterFactory(GsonConverterFactory.create()).build()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        //直接前往注册界面：
        findViewById<Button>(R.id.button2).setOnClickListener {
            var intentToC = Intent(this, CreateAccountActivity::class.java)
            startActivity(intentToC)
        }
        //登录界面点击事件：
        findViewById<Button>(R.id.button).setOnClickListener {
            var n = findViewById<EditText>(R.id.editText).text.toString()
            var p = findViewById<EditText>(R.id.writePassword).text.toString()



            if (n == "" || p == "") {           //密码或用户名为空，弹窗提示
                Toast.makeText(this, "用户名与密码均不能为空", Toast.LENGTH_LONG).show()
                AlertDialog.Builder(this).apply {
                    setTitle("无效输入")
                    setMessage("用户名与密码都不能为空哦，请重新填写T^T")
                    setCancelable(false)
                    setPositiveButton("好") { dialog, which ->

                    }
                    setNegativeButton("返回") { dialog,
                                              which ->
                    }
                    show()

                }
            } else {

                val accountService = retrofit.create(AccountService::class.java)
                accountService.sendAccountMsg("login", n, p).enqueue(
                    object : Callback<FeedBack> {
                        override fun onFailure(call: Call<FeedBack>, t: Throwable) {
                            Toast.makeText(getApplicationContext(), "创建失败，请检查网络", Toast.LENGTH_LONG)
                                .show();
                            var intent =
                                Intent(getApplicationContext(), ShowErrorActivity2::class.java)
                            intent.putExtra("msg", "请检查网络,出错原因:"+t.message)
                            startActivity(intent)
                        }
                        override fun onResponse(
                            call: Call<FeedBack>,
                            response: Response<FeedBack>
                        ) {
                            var fb = response.body()
                            if (fb != null) {
                                if (fb.status.equals("login_success")) {
                                    FolderActivity2.username=n
                                    var intent =
                                        Intent(getApplicationContext(), FolderActivity2::class.java)
                                    FolderActivity2.foldername="mainFolder"
                                    intent.putExtra("previous","mainFolder")

                                    startActivity(intent)
                                } else if (fb.status.equals("password_mismatch")) {   //比对从后端返回的值，看看是哪一种情况
                                    Toast.makeText(
                                        getApplicationContext(),
                                        "密码错误",
                                        Toast.LENGTH_LONG
                                    )
                                        .show();

                                } else if (fb.status.equals("name_not_found")) {

                                    Toast.makeText(
                                        getApplicationContext(),
                                        "用户名错误哦，请重新输入，或注册一个账号",
                                        Toast.LENGTH_LONG
                                    )
                                        .show();
                                } else {
                                    Toast.makeText(
                                        getApplicationContext(),
                                        "未知错误",
                                        Toast.LENGTH_LONG
                                    )
                                        .show();
                                }
                            }else{
                                Toast.makeText(
                                    getApplicationContext(),
                                    "返回为空",
                                    Toast.LENGTH_LONG
                                )
                                    .show();
                            }
                        }
                    }
                )

            }
        }

    }
}