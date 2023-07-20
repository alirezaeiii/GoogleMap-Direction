package io.fetchapp.data.network

import io.fetchapp.data.response.DirectionResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface DirectionService {

    @GET("api/directions/json")
    suspend fun getDirection(
        @Query("origin") origin: String,
        @Query("destination") destination: String,
        @Query("mode") mode: String,
        @Query("key") apiKey: String
    ): DirectionResponse
}