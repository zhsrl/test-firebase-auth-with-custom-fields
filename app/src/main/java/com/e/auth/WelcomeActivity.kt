package com.e.auth

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import android.widget.Toast
import com.google.android.material.button.MaterialButton
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.*
import com.google.firebase.ktx.Firebase

class WelcomeActivity : AppCompatActivity() {

    private lateinit var mName: TextView
    private lateinit var signOut: MaterialButton

    private lateinit var mAuth: FirebaseAuth

    private lateinit var ref: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_welcome)

        init()

        mAuth = FirebaseAuth.getInstance()

        val user: FirebaseUser? = FirebaseAuth.getInstance().currentUser

        if (user != null){
            ref = FirebaseDatabase.getInstance().getReference().child("Users").child(user.uid)

            ref.addValueEventListener(object : ValueEventListener{
                override fun onDataChange(snapshot: DataSnapshot) {
                    val name: String = snapshot.child("name").getValue().toString()

                    mName.text = name
                }

                override fun onCancelled(error: DatabaseError) {
                    Toast.makeText(applicationContext, "Error EVENT LISTENER!", Toast.LENGTH_SHORT).show()
                }

            })
        }

        signOut.setOnClickListener{
            mAuth.signOut()
        }
    }

    fun init(){
        mName = findViewById(R.id.viewNameTV)
        signOut = findViewById(R.id.signOutBTN)
    }

}