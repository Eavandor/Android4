package com.example.cameraalbumtest

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import kotlinx.android.synthetic.main.activity_folder2.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class FolderActivity2 : AppCompatActivity() {
    companion object {
        var username = ""
        var foldername = ""
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_folder2)

        val fruitList = ArrayList<Fruit>()
        var previous: String = intent.getStringExtra("previous")
        val t = Thread() {
            initFruits(this, previous, fruitList) // 初始化水果数据
        }
        t.isDaemon = false
        t.name = "tThread"
        t.priority = 10
        t.start()
//        while (t.isAlive){
//            Thread.sleep(100)
//        }

//        val layoutManager = StaggeredGridLayoutManager(3, StaggeredGridLayoutManager.VERTICAL)
//        recyclerView.layoutManager = layoutManager
//        val adapter = FruitAdapter(fruitList, previous)
//        recyclerView.adapter = adapter

        var searchB = findViewById<Button>(R.id.button10)
        var targetP = findViewById<EditText>(R.id.search)
        searchB.setOnClickListener {
            var tage = targetP.text.toString().trim()
            var b = false
            for (fruit in fruitList) {


                if (fruit.name.trim().equals(tage)) {   //对比用户输入的搜索项目，与文件夹中的文件名或文件夹名是否有相同的
                    b = true
                    Toast.makeText(this, fruit.name, Toast.LENGTH_LONG).show()
                    if (fruit.imgurl.equals("_")) {
                        var intent = Intent(fruit.cont, FolderActivity2::class.java)
                        intent.putExtra("previous", fruit.name)
                        fruit.cont.startActivity(intent)
                    } else {
                        var intent = Intent(fruit.cont, ImageActivity::class.java)
                        intent.putExtra("url", fruit.imgurl)
                        fruit.cont.startActivity(intent)
                    }
                    break
                }

            }
            if (b == false) {
                Toast.makeText(this, "未找到匹配项", Toast.LENGTH_LONG).show()  //无，则弹出：未找到匹配项
            }

        }
    }

    private fun initFruits(cont: Context, previous: String, fruitList: ArrayList<Fruit>) {
        fruitList.add(
            Fruit(
                "添加文件夹",   //点击recyclerView中的第0项，进入添加文件夹的Acticvity
                R.drawable.add,
                "http://192.168.1.4:8080/untitled1_war/drawables/add.jpg",
                cont
            )
        )
        fruitList.add(
            Fruit(
                "添加图片",   //点击recyclerView中的第1项，进入添加图片的Acticvity
                R.drawable.add,
                "http://192.168.1.4:8080/untitled1_war/drawables/add.jpg",
                cont
            )
        )
//        fruitList.add(
//            Fruit(
//                "凑数图",
//                -1,
//                "http://192.168.1.4:8080/untitled1_war/pictures/browse.jpg",
//                cont
//            )
//        )
        addFromInternet("_", username, "_", previous, this, fruitList)

    }

    fun addFromInternet(
        stuff_name: String,
        owner: String,
        type: String,
        previous: String,       //传入当前所在的文件夹是哪一个，如果是最外的文件夹，则传入"mainFolder"
        contf: Context,
        fruitList: ArrayList<Fruit>
    ) {
        val retrofit = LoginActivity.retrofit
        val accountService = LoginActivity.retrofit.create(ShowService::class.java)
        accountService.sendAccountMsg("_", username, "_", previous).enqueue(  //查询文件夹中的项目，和创建文件夹，都用到了
                                                                                              //同一个接口，此接口查询时，type值为"_",stuff_name值为"_"
                                                                                            //以此来辨析此多功能接口此时的目的是什么，后端也会有对应的返回方式。
            object : Callback<List<Db>> {
                override fun onFailure(call: Call<List<Db>>, t: Throwable) {
//                    if(t.message?.trim().equals("Unexpected valueat line 1 column 2 path $".trim())){
//                        Toast.makeText(contf, "这是个空文件夹", Toast.LENGTH_LONG).show()
//                    }else{
//                        Toast.makeText(contf, "FolderActivity2未知错误", Toast.LENGTH_LONG).show()
//                    var intentqaq2 = Intent(contf, ShowErrorActivity2::class.java)
//                    intentqaq2.putExtra("msg", t.message)
//                    contf.startActivity(intentqaq2)
//                    }
                    Toast.makeText(contf, "此文件夹下无文件", Toast.LENGTH_SHORT).show()
                    val layoutManager =      //无文件夹后端不返回JSON，会到onFailure里面来
                        StaggeredGridLayoutManager(3, StaggeredGridLayoutManager.VERTICAL)  //放入recyclerView
                    recyclerView.layoutManager = layoutManager
                    val adapter = FruitAdapter(fruitList, previous)
                    recyclerView.adapter = adapter
                }

                override fun onResponse(
                    call: Call<List<Db>>,
                    response: Response<List<Db>>
                ) {
                    val list = response.body()
                    if (list != null) {
                        Toast.makeText(contf, "请给我5秒加载时间", Toast.LENGTH_SHORT).show()  //网速较慢时，经常要4-6秒来完成图片加载，服务器只有一核，2G运行内存，数据吞吐速度很慢。
                        for (db in list) {
                            fruitList.add(Fruit(db.stuff_name, -1, db.url, contf))
                            Log.d("list", db.stuff_name + "  and  " + db.url)
                        }
                        val layoutManager =
                            StaggeredGridLayoutManager(3, StaggeredGridLayoutManager.VERTICAL)//放入recyclerView
                        recyclerView.layoutManager = layoutManager
                        val adapter = FruitAdapter(fruitList, previous)
                        recyclerView.adapter = adapter
                    } else {
                        Toast.makeText(contf, "无项目", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        )
    }
}