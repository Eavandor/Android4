package com.example.cameraalbumtest

import android.app.Activity
import android.content.ContentResolver
import android.content.Context
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.content.FileProvider
import kotlinx.android.synthetic.main.activity_main.*
import java.io.File
import android.provider.MediaStore
import android.content.Intent
import android.graphics.BitmapFactory
import android.graphics.Bitmap
import android.graphics.Matrix
import android.media.ExifInterface
import android.os.FileUtils
import android.provider.OpenableColumns
import android.webkit.MimeTypeMap
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.RequiresApi
import com.bumptech.glide.Glide
import retrofit2.http.Url
import java.io.FileOutputStream
import java.io.OutputStream
import java.lang.Exception
import java.net.URI
import kotlin.random.Random

class MainActivity : AppCompatActivity() {

    //    val takePhoto = 1
    val fromAlbum = 2
    lateinit var imageUri: Uri
    lateinit var outputImage: File

    companion object {
        var a = FolderActivity2.foldername
      lateinit var contextInMainActivity2: Context
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        contextInMainActivity2=this
        var previous=intent.getStringExtra("previous")
a=previous
        fromAlbumBtn.setOnClickListener {
            // 打开文件选择器
            val intent = Intent(Intent.ACTION_OPEN_DOCUMENT)
            intent.addCategory(Intent.CATEGORY_OPENABLE)
            // 指定只显示照片，只上传图片
            intent.type = "image/*"
            startActivityForResult(intent, fromAlbum)
        }

//        takePhotoBtn.setOnClickListener {
//            var img: ImageView = findViewById(R.id.imageViewM)
//            val imageUri: Uri =
//                Uri.parse("http://192.168.1.4:8080/untitled1_war/pictures/pic1.jpg")
//            Glide.with(this).load(imageUri)
//                .into(img)
//        }
    }

    @RequiresApi(Build.VERSION_CODES.Q)
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode) {
            fromAlbum -> {
                if (resultCode == Activity.RESULT_OK && data != null) {
                    data.data?.let { uri ->
                        // 将选择的照片显示
//                        val bitmap = getBitmapFromUri(uri)
//                        imageView.setImageBitmap(bitmap)

                        try {
//                            var str="http://192.168.43.146:8080/untitled1_war/drawable/p1.jpg"
//                            var u2:Uri=Uri.parse(str)


                            var file = uriToFileQ(this, uri)                  //自定义方法，从uri来得到File
                            var intent=Intent(this,ShowErrorActivity2::class.java)
                            if (file != null) {
                                intent.putExtra("msg",file.name)
                            }
                            startActivity(intent)
                            Glide.with(this).load(file).into(imageViewM)
                            var upl = file?.let { UploadActivity2().up(this, it,a) }

//                            var textView9=findViewById<TextView>(R.id.textView2)
//                            textView9.text=uri.encodedPath
                        } catch (e: Exception) {
                            var intentqaq = Intent(this, LoginActivity::class.java)
                            var msg = e.message
                            intentqaq.putExtra("msg", msg)
                            startActivity(intentqaq)

                        }

                    }

                }


            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.Q)
    private fun uriToFileQ(context: Context, uri: Uri): File? =
        if (uri.scheme == ContentResolver.SCHEME_FILE)
            File(requireNotNull(uri.path))
        else if (uri.scheme == ContentResolver.SCHEME_CONTENT) {
            //把文件保存到沙盒
            val contentResolver = context.contentResolver
            val displayName = run {
                val cursor = contentResolver.query(uri, null, null, null, null)
                cursor?.let {
                    if (it.moveToFirst())
                        it.getString(cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME))
                    else null
                }
            } ?: "${System.currentTimeMillis()}${Random.nextInt(
                0,
                9999
            )}.${MimeTypeMap.getSingleton()
                .getExtensionFromMimeType(contentResolver.getType(uri))}"

            val ios = contentResolver.openInputStream(uri)
            if (ios != null) {
                File("${context.externalCacheDir!!.absolutePath}/$displayName")
                    .apply {
                        val fos = FileOutputStream(this)
                        FileUtils.copy(ios, fos)
                        fos.close()
                        ios.close()
                    }
            } else null
        } else null

    fun saveImage(path: String, bitmap: Bitmap) {
        try {
            val file = File(path)
//outputStream获取文件的输出流对象
//writer获取文件的Writer对象
//printWriter获取文件的PrintWriter对象
            val fos: OutputStream = file.outputStream()
//压缩格式为JPEG图像，压缩质量为80%
            bitmap.compress(Bitmap.CompressFormat.JPEG, 80, fos)
            fos.flush()
            fos.close()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}
