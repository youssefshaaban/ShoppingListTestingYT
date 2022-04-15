package com.androiddevs.shoppinglisttestingyt.data.remote


import com.androiddevs.shoppinglisttestingyt.BuildConfig
import com.androiddevs.shoppinglisttestingyt.data.remote.responses.ImageResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface PixBayApi {
    @GET("/api/")
    suspend fun searchForImage(@Query("q") searchQuery:String, @Query("key") apiKey:String=BuildConfig.api_key):Response<ImageResponse>
}