package com.example.insta.fragment

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.insta.Post
import com.example.insta.PostAdapter
import com.example.insta.R
import com.parse.ParseQuery


/**
 * A simple [Fragment] subclass.
 * Use the [FeedFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
open class FeedFragment : Fragment() {
    lateinit var postRecyclerView: RecyclerView
    lateinit var postAdapter: PostAdapter
    val posts: MutableList<Post> = mutableListOf()

    companion object{
        final val TAG = "FeedFragment"
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_feed, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        postRecyclerView = view.findViewById(R.id.rvFeed)
        postAdapter = PostAdapter(requireContext(), posts)
        postRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        postRecyclerView.adapter = postAdapter
        queryPosts()

    }

    open fun queryPosts() {
        val query: ParseQuery<Post> = ParseQuery.getQuery(Post::class.java)

        // By default, when fetching an object, related ParseObjects (pointer to user) are not fetched
        // newer post appear at the top
        query.include(Post.KEY_AUTHOR)
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