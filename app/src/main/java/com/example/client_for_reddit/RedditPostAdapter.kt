package com.example.client_for_reddit

import android.content.Context
import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.RequiresApi
import com.example.client_for_reddit.Model.RedditPost
import com.squareup.picasso.Picasso
import java.time.Duration
import java.time.Instant
import java.time.ZoneOffset

class RedditPostAdapter(private val context: Context, private val dataSource: List<RedditPost>) : BaseAdapter() {

    private val inflater: LayoutInflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater

    override fun getCount(): Int {
        return dataSource.size
    }

    override fun getItem(position: Int): Any {
        return dataSource[position]
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val rowView = convertView ?: inflater.inflate(R.layout.item, parent, false)

        val usernameTextView = rowView.findViewById<TextView>(R.id.usernameTextView)
        val timeTextView = rowView.findViewById<TextView>(R.id.timeTextView)
        val descriptionTextView = rowView.findViewById<TextView>(R.id.descriptionTextView)
        val postCommentsCount = rowView.findViewById<TextView>(R.id.postCommentsCount)
        val postImageView = rowView.findViewById<ImageView>(R.id.postImageView)

        val post = getItem(position) as RedditPost

        usernameTextView.text = post.author

        val timestamp: Long = post.date
        val eventTime = Instant.ofEpochSecond(timestamp).atOffset(ZoneOffset.UTC).toLocalDateTime()
        val currentTime = Instant.now().atOffset(ZoneOffset.UTC).toLocalDateTime()
        val timeDifference = Duration.between(eventTime, currentTime)
        val hoursAgo = timeDifference.toHours()

        timeTextView.text = "${hoursAgo} hr. ago"
        descriptionTextView.text = post.decription
        postCommentsCount.text = "${post.countComments} comments"
        Picasso.get().load(post.img).into(postImageView)

        return rowView
    }
}