package com.e.auth

import android.app.DownloadManager
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.firebase.ui.database.FirebaseRecyclerOptions
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.material.button.MaterialButton
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.*


class WelcomeActivity : AppCompatActivity() {

    private lateinit var mName: TextView
    private lateinit var signOut: MaterialButton

    private lateinit var mAuth: FirebaseUser

    private lateinit var ref: DatabaseReference

    private lateinit var recView: RecyclerView
    private lateinit var fileDataAdapter: FileDataAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_welcome)

        //Initalize data
        init()
        recView.layoutManager = LinearLayoutManager(this)

        //Get user name(don't username, a name)
        getNameData()

        val options: FirebaseRecyclerOptions<FileData> = FirebaseRecyclerOptions.Builder<FileData>()
                .setQuery(
                    FirebaseDatabase.getInstance().reference.child("Uploads"),
                    FileData::class.java
                )
                .build()

        fileDataAdapter = FileDataAdapter(options)
        recView.adapter = fileDataAdapter



        //Sign out
        signOut.setOnClickListener{
            FirebaseAuth.getInstance().signOut()
            val intent = Intent(applicationContext, LoginActivity::class.java)
            startActivity(intent)
            finish()
//            download()
        }

    }

    override fun onStart() {
        super.onStart()
        fileDataAdapter.startListening()
    }

    override fun onStop() {
        super.onStop()
        fileDataAdapter.stopListening()
    }


    //Download File logic
    fun download(){
        val uriFile: Uri = Uri.parse("https://firebasestorage.googleapis.com/v0/b/authtest-e0a24.appspot.com/o/Books%2FIman_negizderi.pdf?alt=media&token=60e549d2-8ccd-4c31-b79a-119ff1e1fd12")

        downloadManager(uriFile)
    }

    fun downloadManager(uri: Uri){
        val downloadManager: DownloadManager = applicationContext.getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager
        val request: DownloadManager.Request = DownloadManager.Request(uri)

        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_ONLY_COMPLETION)
        Toast.makeText(applicationContext, "Download started", Toast.LENGTH_SHORT).show()

        val reference: Long = downloadManager.enqueue(request)
    }

    fun init(){
        mName = findViewById(R.id.viewNameTV)
        signOut = findViewById(R.id.signOutBTN)
        recView = findViewById(R.id.recView)
    }

    fun getNameData(){
        val user: FirebaseUser? = FirebaseAuth.getInstance().currentUser

        if (user != null){
            ref = FirebaseDatabase.getInstance().getReference().child("Users").child(user.uid)

            ref.addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val name: String = snapshot.child("name").getValue().toString()

                    mName.text = name
                }

                override fun onCancelled(error: DatabaseError) {
                    Toast.makeText(applicationContext, "Error EVENT LISTENER!", Toast.LENGTH_SHORT)
                        .show()
                }


            })
        }
    }

}