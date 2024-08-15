package com.example.client_for_reddit.Network

import com.example.client_for_reddit.Model.RedditResponse
import retrofit2.Call
import retrofit2.http.GET

interface RedditApiService {
    @GET("top.json")
    fun getTopPosts(): Call<RedditResponse>
}