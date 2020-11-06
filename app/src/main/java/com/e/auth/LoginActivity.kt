package com.e.auth

import android.R.attr.password
import android.content.Intent
import android.os.Bundle
import android.service.autofill.UserData
import android.text.TextUtils
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.FirebaseDatabase
import kotlin.math.sign


class LoginActivity : AppCompatActivity() {

    private lateinit var mEmail: EditText
    private lateinit var mPassword: EditText
    private lateinit var progressBar: ProgressBar
    private lateinit var signInButton: Button

    private lateinit var mAuth: FirebaseAuth


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        init()

        mAuth = FirebaseAuth.getInstance()



        signInButton.setOnClickListener{
//            if(!TextUtils.isEmpty(vEmail) || !TextUtils.isEmpty(vPassword)){
//
//
//
//            }
            progressBar.visibility = View.VISIBLE

            val vEmail = mEmail.text.toString().trim()
            val vPassword = mPassword.text.toString().trim()

            mAuth.signInWithEmailAndPassword(vEmail, vPassword)
                    .addOnCompleteListener(object : OnCompleteListener<AuthResult>{
                        override fun onComplete(p0: Task<AuthResult>) {
                            if(p0.isSuccessful){
                                progressBar.visibility = View.GONE
                                Toast.makeText(applicationContext,"Sign in successfully!", Toast.LENGTH_SHORT).show()

                                val user: FirebaseUser = mAuth.currentUser!!
                                updateUI(user)
                            }else{
                                progressBar.visibility = View.GONE
                                Toast.makeText(applicationContext, p0.exception?.message , Toast.LENGTH_SHORT).show()
                                Log.e("TAG", "failure!", p0.exception)
                            }
                        }

                    })

        }

    }

    override fun onStart() {
        super.onStart()
        val currentUser: FirebaseUser? = mAuth.currentUser
        if (currentUser != null) {
            updateUI(currentUser)
        }
    }

    fun init(){
        mEmail = findViewById(R.id.signInEmailET)
        mPassword = findViewById(R.id.signInPasswordET)
        progressBar = findViewById(R.id.mProgressBar)
        signInButton = findViewById(R.id.loginBTN)
    }

    private fun updateUI(user: FirebaseUser){
        if(user != null){
            Log.e("TAG","Signed Successfully!")
            val intent = Intent(applicationContext, WelcomeActivity::class.java)
            startActivity(intent)
            finish()
        }else{
            Toast.makeText(applicationContext,"You don't signed", Toast.LENGTH_SHORT).show()
        }
    }
}   