package com.example.insta

import android.app.Application
import com.parse.Parse
import com.parse.ParseObject

class Insta : Application() {
    override fun onCreate() {
        super.onCreate()

        // Register your parse models before init
        ParseObject.registerSubclass(Post::class.java) // ParseObject convert to Post that we really need

        Parse.initialize(
            Parse.Configuration.Builder(this)
                .applicationId(getString(R.string.back4app_app_id))
                .clientKey(getString(R.string.back4app_client_key))
                .server(getString(R.string.back4app_server_url))
                .build());
    }
}