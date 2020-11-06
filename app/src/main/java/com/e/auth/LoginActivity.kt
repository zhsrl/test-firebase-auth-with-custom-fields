package com.e.auth

import android.R.attr.password
import android.content.Intent
import android.os.Bundle
import android.service.autofill.UserData
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase


class LoginActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

//        button_register.setOnClickListener(object : OnClickListener() {
//            fun onClick(v: View?) {
//                if (!validateFullname() or !validateUsername() or !validateEmail() or !validatePassword() or checkUserGender()) {
//                    return
//                }
//                if (password.equals(co_password)) {
//
//                    //    progressbar VISIBLE
//                    signUp_progress.setVisibility(View.VISIBLE)
//                    mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(
//                        OnCompleteListener<AuthResult?> { task ->
//                            if (task.isSuccessful) {
//                                val data = UserData(fullname, username, email, gender)
//                                FirebaseDatabase.getInstance().getReference("UserData")
//                                    .child(FirebaseAuth.getInstance().currentUser!!.uid)
//                                    .setValue(data).addOnCompleteListener { //    progressbar GONE
//                                        signUp_progress.setVisibility(View.GONE)
//                                        Toast.makeText(
//                                            this@SignUpActivity,
//                                            "Successful Registered",
//                                            Toast.LENGTH_SHORT
//                                        ).show()
//                                        val intent =
//                                            Intent(this@SignUpActivity, MainActivity::class.java)
//                                        startActivity(intent)
//                                        finish()
//                                    }
//                            } else {
//                                //    progressbar GONE
//                                signUp_progress.setVisibility(View.GONE)
//                                Toast.makeText(
//                                    this@SignUpActivity,
//                                    "Check Email id or Password",
//                                    Toast.LENGTH_SHORT
//                                ).show()
//                            }
//                        })
//                } else {
//                    Toast.makeText(this@SignUpActivity, "Password didn't match", Toast.LENGTH_SHORT)
//                        .show()
//                }
//            }
//        })

    }
}   