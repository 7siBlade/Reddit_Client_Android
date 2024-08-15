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
    val author: String,
    val created_utc: Long,
    val url: String?,
    val num_comments: Int
)