package com.example.client_for_reddit.Model

data class RedditPost(
    val author: String,
    val description: String,
    val date: Long,
    val img: String?,
    val countComments: Int,
    val isVideo: Boolean,
    val urlVideo: String?,
    val thumbnail: String?
)