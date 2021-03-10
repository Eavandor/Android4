package com.example.cameraalbumtest


import android.content.Intent
import android.net.Uri
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.widget.Toast
import com.bumptech.glide.Glide
class FruitAdapter(val fruitList: List<Fruit>,val previous:String ) : RecyclerView.Adapter<FruitAdapter.ViewHolder>() {

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val fruitImage: ImageView = view.findViewById(R.id.fruitImage)
        val fruitName: TextView = view.findViewById(R.id.fruitName)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.fruit_item, parent, false)
        val viewHolder = ViewHolder(view)

        viewHolder.itemView.setOnClickListener {
            val position = viewHolder.adapterPosition
            val fruit = fruitList[position]


            if(position==1){         //点击recyclerView中的第1项，进入添加图片的Acticvity
                var intent=Intent(fruit.cont,MainActivity::class.java)
                intent.putExtra("previous",previous)
                fruit.cont.startActivity(intent)
            }else if(position==0){                          //点击recyclerView中的第0项，进入添加文件夹的Acticvity
                var intent=Intent(fruit.cont,CreateFolderActivity2::class.java)
                intent.putExtra("previous",previous)
                fruit.cont.startActivity(intent)
            }else{
if(fruit.imgurl.equals("_")){
    var intent=Intent(fruit.cont,FolderActivity2::class.java)
    intent.putExtra("previous",fruit.name)
    fruit.cont.startActivity(intent)
}else{
    var intent=Intent(fruit.cont,ImageActivity::class.java)
    intent.putExtra("url",fruit.imgurl)
    fruit.cont.startActivity(intent)
}
            }
        }
        viewHolder.fruitImage.setOnClickListener {   //图片被点击，和上面的itemView被点击有一样效果，确保recyclerView功能的整体完整性
            val position = viewHolder.adapterPosition
            val fruit = fruitList[position]
//            Toast.makeText(parent.context, "you clicked image ${fruit.name}", Toast.LENGTH_SHORT).show()
            if(position==1){
//                FolderActivity2().goToUpload(fruit)
                var intent=Intent(fruit.cont,MainActivity::class.java)
                intent.putExtra("previous",previous)
                fruit.cont.startActivity(intent)
            }else if(position==0){
                var intent=Intent(fruit.cont,CreateFolderActivity2::class.java)
                intent.putExtra("previous",previous)
                fruit.cont.startActivity(intent)
            }else{
                if(fruit.imgurl.equals("_")){
                    var intent=Intent(fruit.cont,FolderActivity2::class.java)
                    intent.putExtra("previous",fruit.name)
                    fruit.cont.startActivity(intent)
                }else{
                    var intent=Intent(fruit.cont,ImageActivity::class.java)
                    intent.putExtra("url",fruit.imgurl)
                    fruit.cont.startActivity(intent)
                }
            }
        }
        return viewHolder
    }



    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val fruit = fruitList[position]
        holder.fruitName.text = fruit.name
        if (fruit.imgurl.equals("_")){     //只有文件夹的url值为"_"（一个下划线）,imgurl为"_"时，必为文件夹
            holder.fruitImage.setImageResource(R.drawable.folder)
        }else if(fruit.imgurl.equals("__")){           //只有添加图片按钮或添加文件夹按钮的两个ItemView的url值为"__"（两个连续的下划线）,imgurl为"__"时，必为添加图片按钮或添加文件夹按钮
            holder.fruitImage.setImageResource(R.drawable.add) //对，我把添加图片按钮，添加文件夹按钮，图片，文件夹，全都放在一个recyclerView里面显示
        }else{
            val imageUri: Uri =               //imgurl既不是"_"(单下划线）也不是"__"（两个连续的下划线），则只剩下图片了，图片的imgurl里面存这真正的图片地址，http开头
                Uri.parse(fruit.imgurl)    //用Glide加载url,因为URI是URL的父类，所以可以直接那URL的String类型的字符串，parse转化成Uri对象
            Glide.with(fruit.cont).load(imageUri)
                .into(holder.fruitImage)
        }
    }
    override fun getItemCount() = fruitList.size
}