package com.e.auth

import android.content.Intent
import android.hardware.biometrics.BiometricPrompt
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import java.util.concurrent.Executor

class MainActivity : AppCompatActivity() {

    private lateinit var executor: Executor
    private lateinit var biometricPrompt: androidx.biometric.BiometricPrompt
    private lateinit var promptInfo: androidx.biometric.BiometricPrompt.PromptInfo

    private lateinit var authButton: Button

    @RequiresApi(Build.VERSION_CODES.P)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        authButton = findViewById(R.id.authBTN)

        executor = ContextCompat.getMainExecutor(this)

        biometricPrompt = androidx.biometric.BiometricPrompt(this,
                executor, object : androidx.biometric.BiometricPrompt.AuthenticationCallback(){

            override fun onAuthenticationError(errorCode: Int, errString: CharSequence) {
                super.onAuthenticationError(errorCode, errString)
                Toast.makeText(applicationContext, "Auth Error: $errString", Toast.LENGTH_SHORT).show()
            }

            override fun onAuthenticationSucceeded(result: androidx.biometric.BiometricPrompt.AuthenticationResult) {
                super.onAuthenticationSucceeded(result)
                Toast.makeText(applicationContext, "Auth success!", Toast.LENGTH_SHORT).show()
                val intent = Intent(applicationContext, WelcomeActivity::class.java)
                startActivity(intent)
            }

            override fun onAuthenticationFailed() {
                super.onAuthenticationFailed()
                Toast.makeText(applicationContext, "Auth Failed!", Toast.LENGTH_SHORT).show()
            }
        })

        promptInfo = androidx.biometric.BiometricPrompt.PromptInfo.Builder()
                .setTitle("Biometric Login for Auth")
                .setSubtitle("Log in using your biometric credential")
                .setNegativeButtonText("Use account password")
                .build()

        authButton.setOnClickListener{
            biometricPrompt.authenticate(promptInfo)
        }

    }
}