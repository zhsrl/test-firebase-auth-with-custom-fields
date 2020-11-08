package com.e.auth

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import de.hdodenhof.circleimageview.CircleImageView
import java.lang.Exception

class ListAdapter(var fileDataList: MutableList<FileData?>?, var context: Context): RecyclerView.Adapter<ListAdapter.ViewHolder>() {

    inner class ViewHolder(val view: View): RecyclerView.ViewHolder(view){
        lateinit var itemImage: CircleImageView
        lateinit var itemTitle: TextView

        fun bind(fileData: FileData?){
            init()

            Glide.with(context)
                    .load(fileData?.fileImg)
                    .into(itemImage)

            itemTitle.text = fileData?.fileTitle

            itemView.setOnClickListener{
                Toast.makeText(context, "TAP", Toast.LENGTH_SHORT).show()
            }


        }

        fun init(){
            itemImage = view.findViewById(R.id.modelItemIV)
            itemTitle = view.findViewById(R.id.modelItemTitleTV)
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListAdapter.ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.list_model, parent, false)
        val params: RecyclerView.LayoutParams = RecyclerView.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
        )

        view.layoutParams = params
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ListAdapter.ViewHolder, position: Int) {
        holder.bind(fileDataList!![position])
    }

    override fun getItemCount(): Int {
        try {
            return fileDataList!!.size
        }catch (e: Exception){
            return Log.e("TAG", e.toString())
        }
    }
}

