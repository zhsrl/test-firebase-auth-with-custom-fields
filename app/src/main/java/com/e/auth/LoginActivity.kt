package com.e.auth

import android.R.attr.password
import android.content.Intent
import android.os.Bundle
import android.service.autofill.UserData
import android.text.TextUtils
import android.util.Log
import android.view.View
import android.widget.*
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
    private lateinit var register: TextView

    private lateinit var mAuth: FirebaseAuth


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        init()

        mAuth = FirebaseAuth.getInstance()



        signInButton.setOnClickListener{
            progressBar.visibility = View.VISIBLE

            val vEmail = mEmail.text.toString().trim()
            val vPassword = mPassword.text.toString().trim()

            mAuth.signInWithEmailAndPassword(vEmail, vPassword)
                    .addOnCompleteListener(object : OnCompleteListener<AuthResult>{
                        override fun onComplete(p0: Task<AuthResult>) {
                            if(p0.isSuccessful){
                                val currentUser: String = mAuth.currentUser!!.uid
                                val admin: String = "PH8ZQtKotKUOtWdn6mRSWnlBUs93"
                                if(currentUser == admin){
                                    progressBar.visibility = View.GONE
                                    val adminUser: FirebaseUser = mAuth.currentUser!!
                                    adminUpdateUI(adminUser)

                                }else{
                                    progressBar.visibility = View.GONE
                                    val user: FirebaseUser = mAuth.currentUser!!
                                    updateUI(user)
                                }
                            }else{
                                progressBar.visibility = View.GONE
                                Toast.makeText(applicationContext, p0.exception?.message , Toast.LENGTH_SHORT).show()
                                Log.e("TAG", "failure!", p0.exception)
                            }
                        }

                    })

        }

        register.setOnClickListener {
            val intent = Intent(applicationContext, RegisterActivity::class.java)
            startActivity(intent)
            finish()
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
        register = findViewById(R.id.registerTextView)
    }

    private fun updateUI(user: FirebaseUser){
        if(user != null){
            if(user.uid == "PH8ZQtKotKUOtWdn6mRSWnlBUs93"){
                adminUpdateUI(user)
                finish()
            }else{
                Log.e("TAG","Signed Successfully!")
                Toast.makeText(applicationContext, "Signed Successfully!", Toast.LENGTH_SHORT).show()
                val intent = Intent(applicationContext, WelcomeActivity::class.java)
                startActivity(intent)
                finish()
            }
        }else{
            Toast.makeText(applicationContext,"You don't signed", Toast.LENGTH_SHORT).show()
        }
    }

    private fun adminUpdateUI(user: FirebaseUser){
        if(user != null){
            Log.e("TAG","ADMIN PANEL ACTIVATED!")
            Toast.makeText(applicationContext, "ADMIN PANEL ACTIVATED!", Toast.LENGTH_SHORT).show()
            val intent = Intent(applicationContext, AdminActivity::class.java)
            startActivity(intent)
            finish()
        }else{
            Toast.makeText(applicationContext,"You don't signed", Toast.LENGTH_SHORT).show()
        }
    }
}   