import android.os.Bundle
import android.widget.ListView
import androidx.appcompat.app.AppCompatActivity
import com.example.client_for_reddit.Adapter.RedditPostAdapter
import com.example.client_for_reddit.Model.RedditPost
import com.example.client_for_reddit.Model.RedditResponse
import com.example.client_for_reddit.Network.RetrofitInstance
import com.example.client_for_reddit.R
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        fetchRedditPost()
    }

    private fun fetchRedditPost() {
        val call = RetrofitInstance.api.getTopPosts()

        call.enqueue(object : Callback<RedditResponse> {
            override fun onResponse(call: Call<RedditResponse>, response: Response<RedditResponse>) {
                if (response.isSuccessful) {
                    val redditPosts = response.body()?.data?.children?.map { child ->
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
                    }

                    val adapter = RedditPostAdapter(this@MainActivity, redditPosts ?: listOf())
                    val listView = findViewById<ListView>(R.id.listView); listView.adapter = adapter
                    listView.adapter = adapter
                }
            }

            override fun onFailure(call: Call<RedditResponse>, t: Throwable) {
                TODO("Not yet implemented")
            }
        })
    }
}