package com.example.github.network

import com.example.github.models.response.AccessTokenResponse
import com.example.github.models.response.SearchResponse
import com.example.github.models.request.TokenRequest
import io.reactivex.Single
import retrofit2.http.*

interface GithubService {

    @Headers("Accept: application/json")
    @POST("https://github.com/login/oauth/access_token/")
    fun getAccessToken(
        @Body tokenRequest: TokenRequest
    ): Single<AccessTokenResponse>

    @Headers("Accept: application/vnd.github.v3+json")
    @GET("/search/repositories")
    fun searchRepositories(
        @Query("q") q: String,
        @Query("sort") sort: String,
        @Query("order") order: String,
        @Query("per_page") perPage: Int,
        @Query("page") pageNumber: Int
    ): Single<SearchResponse>
}