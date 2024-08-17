package com.example.client_for_reddit.Model

data class RedditResponse(
    val data: RedditData
)

data class RedditData(
    val children: List<RedditChildren>
)

data class RedditChildren(
    val data: RedditPostData
)

data class RedditPostData(
    val subreddit_name_prefixed: String,
    val title: String,
    val created_utc: Long,
    val url: String?,
    val num_comments: Int,
    val is_video: Boolean,
    val scrubber_media_url: String?,
    val thumbnail: String?
)