package com.example.client_for_reddit.Network

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitInstance {
    private val retrofit by lazy {
        Retrofit.Builder()
            .baseUrl("https://www.reddit.com/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    val api: RedditApiService by lazy {
        retrofit.create(RedditApiService::class.java)
    }
}