package com.example.client_for_reddit

import RedditPagingSource
import android.annotation.SuppressLint
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.client_for_reddit.Adapter.RedditPostAdapter
import com.example.client_for_reddit.Network.RetrofitInstance

class MainActivity : AppCompatActivity() {

    private lateinit var adapter: RedditPostAdapter

    @SuppressLint("WrongViewCast")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val recyclerView = findViewById<RecyclerView>(R.id.recyclerView)
        adapter = RedditPostAdapter()
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(this)

        loadRedditPosts()
    }

    private fun loadRedditPosts() {
        val pager = Pager(
            config = PagingConfig(pageSize = 5),
            pagingSourceFactory = { RedditPagingSource(redditApiService = RetrofitInstance.api) }
        ).flow
    }
}
