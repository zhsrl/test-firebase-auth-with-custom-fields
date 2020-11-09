package com.e.auth

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.firebase.ui.database.FirebaseRecyclerAdapter
import com.firebase.ui.database.FirebaseRecyclerOptions
import com.google.firebase.database.*
import de.hdodenhof.circleimageview.CircleImageView
import java.io.File


@Suppress("NAME_SHADOWING")
class FileDataAdapter(options: FirebaseRecyclerOptions<FileData>): FirebaseRecyclerAdapter<FileData, FileDataAdapter.ViewHolder>(options) {

    inner class ViewHolder(view: View): RecyclerView.ViewHolder(view){
        var itemImage: CircleImageView
        var itemTitle: TextView
        var descButton: Button

        init {
            itemImage = view.findViewById<View>(R.id.modelItemIV) as CircleImageView
            itemTitle = view.findViewById<View>(R.id.modelItemTitleTV) as TextView
            descButton = view.findViewById<View>(R.id.descBTN) as Button
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view: View =
            LayoutInflater.from(parent.context).inflate(R.layout.list_model, parent, false)
        return ViewHolder(view)
    }

    protected override fun onBindViewHolder(holder: ViewHolder, position: Int, fileData: FileData) {
        holder.itemTitle.text = fileData.fileTitle

        Glide.with(holder.itemImage.context)
            .load(fileData.fileImg)
            .into(holder.itemImage)

        holder.descButton.setOnClickListener{

            val fileData = FileData().fileDesc


            val database: FirebaseDatabase = FirebaseDatabase.getInstance()
            val ref: DatabaseReference = database.getReference().child("Uploads").child(fileData)

            ref.addValueEventListener(object : ValueEventListener{
                override fun onDataChange(snapshot: DataSnapshot) {
                    val value: String = snapshot.child(fileData).toString()

                    Toast.makeText(holder.descButton.context, value, Toast.LENGTH_SHORT).show()


                }

                override fun onCancelled(error: DatabaseError) {
                    Toast.makeText(holder.descButton.context, "CANCELLED", Toast.LENGTH_SHORT).show()
                }

            })
        }
    }



}

