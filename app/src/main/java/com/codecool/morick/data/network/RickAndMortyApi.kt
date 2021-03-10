package com.codecool.morick.data.network

import com.codecool.morick.models.RickAndMortyCharacter
import com.codecool.morick.models.RickAndMortyLocation
import com.codecool.morick.models.RickAndMortyResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface RickAndMortyApi {

    @GET("/api/character/")
    suspend fun getCharacters(@Query("name") name: String, @Query("page") pageNumber: Int): Response<RickAndMortyResponse>

//    @GET("/api/character/")
//    suspend fun searchCharacters(@Query("name") name: String): Response<RickAndMortyResponse>

    @GET("/api/location/{id}")
    suspend fun getLocationById(@Path("id") id: String): Response<RickAndMortyLocation>

    @GET("/api/character/{idList}")
    suspend fun getMultipleCharacters(@Path("idList") idList: String): Response<List<RickAndMortyCharacter>>

//    @GET("/api/character/")
//    suspend fun getNextPage(@Query("page") pageNumber: Int): Response<RickAndMortyResponse>

}