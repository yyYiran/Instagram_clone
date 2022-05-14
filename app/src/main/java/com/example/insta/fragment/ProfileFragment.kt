package com.example.insta.fragment

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.insta.Post
import com.example.insta.R
import com.parse.ParseQuery
import com.parse.ParseUser

class ProfileFragment : FeedFragment() {
    override fun queryPosts(){
        val query: ParseQuery<Post> = ParseQuery.getQuery(Post::class.java)

        // By default, when fetching an object, related ParseObjects (pointer to user) are not fetched
        // newer post appear at the top
        query.whereEqualTo(Post.KEY_AUTHOR, ParseUser.getCurrentUser())
            .include(Post.KEY_AUTHOR)
            .orderByDescending("createdAt")
            .setLimit(10)
        query.findInBackground { posts, e ->
            if (e != null) {
                Log.e(TAG, "error in fetching posts", e)
                e.printStackTrace()
            } else {
                if (posts != null) {
                    for (post in posts) {
                        Log.d(TAG, "done: " + post.getDescription() + " image: " + post.getImage() + " username: " + post.getAuthor()?.username
                        )
                    }
                    this.posts.clear()
                    this.posts.addAll(posts)
                    postAdapter.notifyDataSetChanged()
                }
            }
        }
    }
}