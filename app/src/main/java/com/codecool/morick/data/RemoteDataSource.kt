package com.codecool.morick.data

import com.codecool.morick.data.network.RickAndMortyApi
import com.codecool.morick.models.RickAndMortyEpisode
import com.codecool.morick.models.RickAndMortyResponse
import retrofit2.Response
import javax.inject.Inject

class RemoteDataSource @Inject constructor(
    private val rickAndMortyApi: RickAndMortyApi
) {

    suspend fun getAllCharacters(): Response<RickAndMortyResponse> {
        return rickAndMortyApi.getAllCharacters()
    }

    suspend fun searchCharacters(name: String): Response<RickAndMortyResponse> {
        return rickAndMortyApi.searchCharacters(name)
    }

    suspend fun getEpisodeById(id: String): Response<RickAndMortyEpisode> {
        return rickAndMortyApi.getEpisodeById(id)
    }

}