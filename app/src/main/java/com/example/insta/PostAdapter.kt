package com.example.insta

import android.content.Context
import android.opengl.Visibility
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide

class PostAdapter(val context: Context, val posts: List<Post>): RecyclerView.Adapter<PostAdapter.ViewHolder>() {

    class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        val tvAuthor: TextView
        val tvDescription: TextView
        val ivImage: ImageView

        init {
            tvAuthor = itemView.findViewById(R.id.tvUsername)
            tvDescription = itemView.findViewById(R.id.tvDescription)
            ivImage = itemView.findViewById(R.id.ivPic)
        }

        fun bindDataAndMethod(post: Post){
            tvAuthor.text = post.getAuthor()?.username ?: "harry"
            tvDescription.text = post.getDescription()
            if (post.getImage()==null)
                ivImage.visibility = View.GONE
            Glide.with(itemView.context)
                .load(post.getImage()?.url)
                .override(200, 200)
                .into(ivImage)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView = LayoutInflater.from(context).inflate(R.layout.item_post, parent, false)
        return ViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val post = posts[position]
        holder.bindDataAndMethod(post)
    }

    override fun getItemCount(): Int = posts.size
}