package com.e.auth

import android.content.Intent
import android.os.Bundle
import android.text.InputType
import android.text.TextUtils
import android.util.Log
import android.util.Patterns
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.Task
import com.google.android.material.textfield.TextInputLayout
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase


class RegisterActivity : AppCompatActivity() {

    private lateinit var mName: EditText
    private lateinit var mUsername: EditText
    private lateinit var mEmail: EditText
    private lateinit var mPassword: EditText
    private lateinit var regButton: Button
    private lateinit var progressBar: ProgressBar
    private lateinit var signIn: TextView

    private lateinit var mAuth: FirebaseAuth



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        init()


        mAuth = FirebaseAuth.getInstance()
        regButton.setOnClickListener{


            if(!validateName() || !validateUsername() || !validatePassword()){
                return@setOnClickListener
            }else{

                progressBar.visibility = View.VISIBLE

                val name = mName.editableText.toString().trim()
                val username = mUsername.editableText.toString().trim()
                val email = mEmail.editableText.toString().trim()
                val password = mPassword.editableText.toString().trim()

                mAuth.createUserWithEmailAndPassword(email, password)
                        .addOnCompleteListener(object : OnCompleteListener<AuthResult>{
                            override fun onComplete(p0: Task<AuthResult>) {
                                if(p0.isSuccessful){
                                    val user = User(name, username, email,password)

                                    FirebaseDatabase.getInstance().getReference("Users")
                                            .child(FirebaseAuth.getInstance().currentUser!!.uid)
                                            .setValue(user)
                                            .addOnCompleteListener(object : OnCompleteListener<Void>{
                                                override fun onComplete(p0: Task<Void>) {
                                                    progressBar.visibility = View.GONE
                                                    if(p0.isSuccessful){

                                                        Toast.makeText(applicationContext,
                                                                "Registered Successfully!",
                                                                Toast.LENGTH_SHORT).show()

                                                        val intent = Intent(applicationContext, LoginActivity::class.java)
                                                        startActivity(intent)
                                                        finish()

                                                    }else{
                                                        Log.e("TAG","failure!",p0.exception)
                                                    }
                                                }

                                            })
                                }else{
                                    progressBar.visibility = View.GONE
                                    Toast.makeText(applicationContext, p0.exception?.message, Toast.LENGTH_LONG).show()
                                    Log.e("TAG_P", "failure!",p0.exception)
                                }
                            }

                        })
            }

        }

        signIn.setOnClickListener {
            val intent = Intent(applicationContext, LoginActivity::class.java)
            startActivity(intent)
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
        mName = findViewById(R.id.nameET)
        mUsername = findViewById(R.id.usernameET)
        mPassword = findViewById(R.id.passwordET)
        mEmail = findViewById(R.id.emailET)
        regButton = findViewById(R.id.regBTN)
        progressBar = findViewById(R.id.progressBar)
        signIn = findViewById(R.id.signInTV)
    }

    fun validateName(): Boolean{
        var vName: String = mName.text.toString()

        if (TextUtils.isEmpty(vName)){
            Toast.makeText(applicationContext, "Name is empty! Enter name", Toast.LENGTH_SHORT).show()
            return false
        }else{
            return true
        }

    }

    fun validateUsername(): Boolean{
        var vUsername = mUsername.text.toString()

        if (TextUtils.isEmpty(vUsername)){
            Toast.makeText(
                applicationContext,
                "Username is empty! Enter username",
                Toast.LENGTH_SHORT
            ).show()
            return false
        }else{
            return true
        }
    }

    private fun validateEmail(): Boolean {
        val vEmail = mEmail.text.toString()
        return if (TextUtils.isEmpty(vEmail)) {
            Toast.makeText(applicationContext, "Enter Your Email", Toast.LENGTH_SHORT).show()
            false
        } else if (android.util.Patterns.EMAIL_ADDRESS.matcher(vEmail).matches()) {
            Toast.makeText(applicationContext, "Please enter valid Email", Toast.LENGTH_SHORT)
                .show()
            false
        } else {
            true
        }
    }

    fun validatePassword(): Boolean{
        val vPassword = mPassword.text.toString()

        if (TextUtils.isEmpty(vPassword)){
            Toast.makeText(
                applicationContext,
                "Username is empty! Enter username",
                Toast.LENGTH_SHORT
            ).show()
            return false
        }else{
            return true
        }
    }

    private fun updateUI(user: FirebaseUser){
        if(user != null){
            Log.e("TAG","Signed Successfully!")
            val intent = Intent(applicationContext, WelcomeActivity::class.java)
            startActivity(intent)
            finish()
        }else{
            Toast.makeText(applicationContext, "You don't signed!", Toast.LENGTH_SHORT).show()
        }
    }

}