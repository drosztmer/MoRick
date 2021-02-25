package com.codecool.morick.data.network

import com.codecool.morick.models.RickAndMortyEpisode
import com.codecool.morick.models.RickAndMortyResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface RickAndMortyApi {

    @GET("/api/character")
    suspend fun getAllCharacters(): Response<RickAndMortyResponse>

    @GET("/api/character/")
    suspend fun searchCharacters(@Query("name") name: String): Response<RickAndMortyResponse>

    @GET("/episode/{episodeId}")
    suspend fun getEpisodeById(@Path("id") id: String): Response<RickAndMortyEpisode>

}