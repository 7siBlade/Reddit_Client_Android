package com.example.client_for_reddit.Network

import com.example.client_for_reddit.Model.RedditResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface RedditApiService {
    @GET("top.json")
    suspend fun getTopPosts(
        @Query("count") after: Int, // Параметр after для пагинации
        @Query("limit") limit: Int  // Количество элементов на странице
    ): Response<List<RedditResponse>>

}