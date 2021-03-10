package com.example.cameraalbumtest

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import kotlinx.android.synthetic.main.activity_create_account.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
//@string/app_name
class CreateAccountActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_account)

        //提交按钮点击事件
        findViewById<Button>(R.id.button3).setOnClickListener {
            var n = findViewById<EditText>(R.id.registerName).text.toString()
            var p1 = findViewById<EditText>(R.id.firstP).text.toString()
            var p2 = findViewById<EditText>(R.id.secondP).text.toString()
            if (p1 != p2) {
                Toast.makeText(this, "两次密码输入不相同，请重新输入", Toast.LENGTH_SHORT).show();
                AlertDialog.Builder(this).apply {
                    setTitle("两次密码不相同")
                    setMessage("粗心了哦~小傻瓜，重新填一下密码吧^_^")
                    setCancelable(false)
                    setPositiveButton("好") { dialog, which ->

                    }
                    setNegativeButton("返回") { dialog,
                                              which ->

                    }
                    show()
                }
            } else {

                var retrofit = LoginActivity.retrofit;
                var n = findViewById<EditText>(R.id.registerName).text.toString()
                var p = findViewById<EditText>(R.id.secondP).text.toString()
                var p2 = findViewById<EditText>(R.id.firstP).text.toString()
                if (n == "" || p == "" || p2 == "") {
                    Toast.makeText(this, "用户名,密码,确认密码三处均不能为空哦", Toast.LENGTH_LONG).show()
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
                    val accountService = LoginActivity.retrofit.create(AccountService::class.java)
                    accountService.sendAccountMsg("createAccount", n, p).enqueue(
                        object : Callback<FeedBack> {
                            override fun onFailure(call: Call<FeedBack>, t: Throwable) {
                                Toast.makeText(
                                    getApplicationContext(),
                                    "创建失败，请检查网络",
                                    Toast.LENGTH_LONG
                                )
                                    .show();
                                var intent =
                                    Intent(getApplicationContext(), ShowErrorActivity2::class.java)
                                intent.putExtra("msg", "请检查网络,出错原因:" + t.message)
                                startActivity(intent)

                            }

                            override fun onResponse(
                                call: Call<FeedBack>,
                                response: Response<FeedBack>
                            ) {
                                var fb = response.body()
                                if (fb != null) {
                                    if (fb.status.equals("create_success")) {
                                        FolderActivity2.username=n
                                        Toast.makeText(
                                            getApplicationContext(),
                                            "创建成功!",
                                            Toast.LENGTH_LONG*2
                                        ).show();
                                        var intent =
                                            Intent(
                                                getApplicationContext(),
                                                FolderActivity2::class.java
                                            )
intent.putExtra("previous","mainFolder")
                                        startActivity(intent)
                                    } else if (fb.status.equals("name_duplicated")) {
                                        Toast.makeText(
                                            getApplicationContext(),
                                            "此用户名已有人使用，来晚一步哦！",
                                            Toast.LENGTH_LONG*2
                                        )
                                            .show();
                                    } else {
                                        Toast.makeText(
                                            getApplicationContext(),
                                            "未知错误"+fb.status,
                                            Toast.LENGTH_LONG*2
                                        )
                                            .show();
                                    }
                                } else {
                                    Toast.makeText(
                                        getApplicationContext(),
                                        "返回为空",
                                        Toast.LENGTH_LONG*2
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
}