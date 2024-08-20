package com.example.client_for_reddit.Adapter

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.VideoView
import androidx.annotation.RequiresApi
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.client_for_reddit.FullScreenImageActivity
import com.example.client_for_reddit.Model.RedditPost
import com.example.client_for_reddit.R
import com.squareup.picasso.Picasso
import java.time.Duration
import java.time.Instant
import java.time.ZoneOffset

class RedditPostAdapter : PagingDataAdapter<RedditPost, RedditPostAdapter.RedditPostViewHolder>(DIFF_CALLBACK) {

    companion object {
        private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<RedditPost>() {
            override fun areItemsTheSame(oldItem: RedditPost, newItem: RedditPost): Boolean {
                return oldItem.img == newItem.img
            }

            override fun areContentsTheSame(oldItem: RedditPost, newItem: RedditPost): Boolean {
                return oldItem == newItem
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RedditPostViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item, parent, false)
        return RedditPostViewHolder(view)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onBindViewHolder(holder: RedditPostViewHolder, position: Int) {
        val post = getItem(position)

        if (post != null) {
            holder.bind(post)
        }
    }

    class RedditPostViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val usernameTextView: TextView = itemView.findViewById(R.id.usernameTextView)
        private val timeTextView: TextView = itemView.findViewById(R.id.timeTextView)
        private val descriptionTextView: TextView = itemView.findViewById(R.id.descriptionTextView)
        private val postCommentsCount: TextView = itemView.findViewById(R.id.postCommentsCount)
        private val postImageView: ImageView = itemView.findViewById(R.id.postImageView)
        private val postVideoView: VideoView = itemView.findViewById(R.id.postVideoView)

        @RequiresApi(Build.VERSION_CODES.O)
        fun bind(post: RedditPost) {
            usernameTextView.text = post.author

            val timestamp: Long = post.date
            val eventTime = Instant.ofEpochSecond(timestamp).atOffset(ZoneOffset.UTC).toLocalDateTime()
            val currentTime = Instant.now().atOffset(ZoneOffset.UTC).toLocalDateTime()
            val timeDifference = Duration.between(eventTime, currentTime)
            val hoursAgo = timeDifference.toHours()

            timeTextView.text = "${hoursAgo} hr. ago"
            descriptionTextView.text = post.description
            postCommentsCount.text = "${post.countComments} comments"

            if (post.isVideo) {
                postVideoView.visibility = View.VISIBLE
                postImageView.visibility = View.GONE

                if (!post.urlVideo.isNullOrEmpty()) {
                    postVideoView.setVideoURI(Uri.parse(post.urlVideo))
                    postVideoView.start()
                } else {
                    postVideoView.visibility = View.GONE
                }
            } else {
                postImageView.visibility = View.VISIBLE
                postVideoView.visibility = View.GONE

                if (!post.img.isNullOrEmpty()) {
                    if (post.img.endsWith(".jpeg")) {
                        Picasso.get().load(post.img).into(postImageView)
                    } else {
                        Picasso.get().load(post.thumbnail).into(postImageView)
                    }

                    postImageView.setOnClickListener {
                        val intent = Intent(itemView.context, FullScreenImageActivity::class.java)
                        if (post.img.endsWith(".jpeg")) {
                            intent.putExtra("IMAGE_URL", post.img)
                        } else {
                            intent.putExtra("IMAGE_URL", post.thumbnail)
                        }
                        itemView.context.startActivity(intent)
                    }

                } else {
                    postImageView.visibility = View.GONE
                }
            }
        }
    }
}
