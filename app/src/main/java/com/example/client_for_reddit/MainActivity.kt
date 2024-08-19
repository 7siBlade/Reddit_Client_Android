package com.example.client_for_reddit

import android.os.Bundle
import android.util.Log
import android.widget.ListView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.client_for_reddit.Adapter.RedditPostAdapter
import com.example.client_for_reddit.Model.RedditPost
import com.example.client_for_reddit.Model.RedditResponse
import com.example.client_for_reddit.Network.RetrofitInstance
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainActivity : AppCompatActivity() {

    private lateinit var redditPosts: MutableList<RedditPost>
    private lateinit var listView: ListView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        listView = findViewById(R.id.listView)
        redditPosts = mutableListOf()

        if (savedInstanceState != null) {
            // Восстановление состояния
            redditPosts = savedInstanceState.getParcelableArrayList("POST_LIST") ?: mutableListOf()
            val adapter = RedditPostAdapter(this, redditPosts)
            listView.adapter = adapter

            // Восстановление позиции прокрутки
            val scrollPosition = savedInstanceState.getInt("SCROLL_POSITION", 0)
            listView.setSelection(scrollPosition)
        } else {
            // Загружаем данные, если состояние не сохранено
            fetchRedditPost()
        }
    }

    private fun fetchRedditPost() {
        val call = RetrofitInstance.api.getTopPosts()

        call.enqueue(object : Callback<RedditResponse> {
            override fun onResponse(call: Call<RedditResponse>, response: Response<RedditResponse>) {
                if (response.isSuccessful) {
                    val posts = response.body()?.data?.children?.map { child ->
                        RedditPost(
                            author = child.data.subreddit_name_prefixed,
                            description = child.data.title,
                            date = child.data.created_utc,
                            img = child.data.url,
                            countComments = child.data.num_comments,
                            isVideo = child.data.is_video,
                            urlVideo = child.data.scrubber_media_url,
                            thumbnail = child.data.thumbnail
                        )
                    } ?: listOf()

                    redditPosts.clear()
                    redditPosts.addAll(posts)
                    val adapter = RedditPostAdapter(this@MainActivity, redditPosts)
                    listView.adapter = adapter
                }
            }

            override fun onFailure(call: Call<RedditResponse>, t: Throwable) {
                Log.e("MainActivity", "Ошибка при загрузке данных с Reddit", t)
                Toast.makeText(this@MainActivity, "Ошибка при загрузке данных с Reddit",Toast.LENGTH_SHORT).show()
            }
        })
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        // Сохранение данных
        outState.putParcelableArrayList("POST_LIST", ArrayList(redditPosts))
        // Сохранение текущей позиции прокрутки
        outState.putInt("SCROLL_POSITION", listView.firstVisiblePosition)
    }
}
