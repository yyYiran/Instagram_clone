package com.example.insta

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.parse.ParseUser

class LoginActivity : AppCompatActivity() {
    val TAG = "LoginActivity"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        val etUsername: EditText = findViewById(R.id.etUsername)
        val etPassword: EditText = findViewById(R.id.etPassword)
        val btnLogin: Button = findViewById(R.id.btnLogin)
        val btnSignup: Button = findViewById(R.id.btnSignup)

        // User can sign up
        btnSignup.setOnClickListener {
            val username = etUsername.text.toString()
            val password = etPassword.text.toString()
            signupUser(username, password)
        }

//         If user logged in, go to Main Activity
        if (ParseUser.getCurrentUser() != null){
            startMainActivity()
        }

        // User manually log in
        btnLogin.setOnClickListener {
            val username = etUsername.text.toString()
            val password = etPassword.text.toString()
            loginUser(username, password)
        }

//        val firstObject = ParseObject("FirstClass")
//        firstObject.put("message","Hey ! First message from android. Parse is now connected")
//        firstObject.saveInBackground { e->
//            if (e != null){
//                e.localizedMessage?.let { message -> Log.e("LoginActivity", message) }
//            }else{
//                Log.d("LoginActivity","Object saved.")
//            }
//        }
    }

    private fun signupUser(username: String, password: String) {
        val user = ParseUser()
        user.username = username
        user.setPassword(password)
        user.signUpInBackground { e->
            if (e == null){
                // successful signup
                Toast
                    .makeText(this@LoginActivity, "sign in successfully", Toast.LENGTH_SHORT)
                    .show()
                startMainActivity()
            } else{
                Toast
                    .makeText(this@LoginActivity, "signup failed", Toast.LENGTH_SHORT)
                    .show()
                e.printStackTrace()
            }
        }
    }

    private fun startMainActivity() {
        val i = Intent(this@LoginActivity, MainActivity::class.java)
        startActivity(i)
        finish() // destroy current activity so user cannot go back
    }

    private fun loginUser(username: String, password: String) {
        ParseUser.logInInBackground(username, password) { user, e ->
            if (user != null){
                Log.d(TAG, "loginUser success ${user}")
                startMainActivity()
            } else{
                Toast
                    .makeText(this@LoginActivity, "login failed", Toast.LENGTH_SHORT)
                    .show()
                e.printStackTrace()
            }
        }
    }
}