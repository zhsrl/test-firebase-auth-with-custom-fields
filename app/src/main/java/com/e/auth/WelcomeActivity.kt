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
import com.google.android.material.button.MaterialButton
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.*
import retrofit2.Call
import retrofit2.Response


class WelcomeActivity : AppCompatActivity() {

    private lateinit var mName: TextView
    private lateinit var signOut: MaterialButton
    private lateinit var adminPanel: MaterialButton

    private lateinit var mAuth: FirebaseAuth

    private lateinit var ref: DatabaseReference

    private lateinit var recView: RecyclerView

    private lateinit var listAdapter: ListAdapter
    var listData: MutableList<FileData?>? = ArrayList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_welcome)
        //Initalize data
        init()

        mAuth = FirebaseAuth.getInstance()

        //Get user name(don't username, a name)
        getNameData()

        //Set Recycler View adapter
        setAdapter()
        retrieveData()

        //Sign out
        signOut.setOnClickListener{
            mAuth.signOut()
            finish()
//            download()
        }

        adminPanel.setOnClickListener{
            val intent = Intent(applicationContext, AdminActivity::class.java)
            startActivity(intent)

            Toast.makeText(applicationContext, "You move to Admin Panel!", Toast.LENGTH_SHORT).show()
        }
    }

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
        adminPanel = findViewById(R.id.adminPanelBTN)
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

    fun setAdapter(){
        listAdapter = ListAdapter(listData, context = applicationContext)

        recView.setHasFixedSize(true)
        recView.layoutManager = LinearLayoutManager(applicationContext)
        recView.adapter = listAdapter
    }


    fun retrieveData(){
        val reference: DatabaseReference = FirebaseDatabase.getInstance().getReference("Uploads")
        reference.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                Log.d("KEY_VALUE", snapshot.children.toString())
                if (snapshot.exists()) {


//                    for (npsnapshot in snapshot.getChildren()) {
//                        val list: FileData? = npsnapshot.getValue(FileData::class.java)
//                        listData?.add(list)
//                    }
                    listAdapter = ListAdapter(listData, applicationContext)
                    recView.setAdapter(listAdapter)
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(applicationContext,error.toString(),Toast.LENGTH_SHORT).show()
            }
        })
    }

}