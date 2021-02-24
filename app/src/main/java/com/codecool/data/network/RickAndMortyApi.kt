package com.codecool.data.network

import com.codecool.models.RickAndMortyEpisode
import com.codecool.models.RickAndMortyResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

interface RickAndMortyApi {

    @GET("/character")
    suspend fun getAllCharacters(): Response<RickAndMortyResponse>

    @GET("/episode/{episodeId}")
    suspend fun getEpisodeById(@Path("id") id: String): Response<RickAndMortyEpisode>

}