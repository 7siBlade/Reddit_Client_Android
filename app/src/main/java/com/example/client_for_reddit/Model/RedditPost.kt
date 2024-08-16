package com.example.client_for_reddit.Model

data class RedditPost(
    val author: String,
    val decription: String,
    val date: Long,
    val img: String?,
    val countComments: Int
)