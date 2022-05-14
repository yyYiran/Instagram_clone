package com.example.insta

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.FileProvider
import androidx.fragment.app.Fragment
import com.example.insta.fragment.ComposeFragment
import com.example.insta.fragment.FeedFragment
import com.example.insta.fragment.ProfileFragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.parse.*
import java.io.File


class MainActivity : AppCompatActivity() {
    val TAG = "MainActivity"


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // 1. setting description of post
        // 2. launch camera to take picture
        // 3. show picture in ImageView
        // 4. Save post=(pic+description) to Parse
        val fragmentManager = supportFragmentManager
        val composeFragment = ComposeFragment()
        val feedFragment = FeedFragment()
        val profileFragment = ProfileFragment()
        val btmNavi = findViewById<BottomNavigationView>(R.id.btmNavi)
        btmNavi.setOnItemSelectedListener { item ->
            lateinit var fragmentToShow:Fragment //default screen to show
            when (item.itemId){
                R.id.ic_home -> { fragmentToShow = feedFragment }
                R.id.ic_compose -> {fragmentToShow = composeFragment}
                R.id.ic_profile -> fragmentToShow = profileFragment
                else -> fragmentToShow = feedFragment
            }
            if (fragmentToShow != null)
                fragmentManager.beginTransaction().replace(R.id.frameContainer, fragmentToShow).commit()
            true
        }

        btmNavi.selectedItemId = R.id.ic_home

//        queryPosts()
    }


}
