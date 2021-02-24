package com.codecool.data

import com.codecool.data.network.RickAndMortyApi
import com.codecool.models.RickAndMortyEpisode
import com.codecool.models.RickAndMortyResponse
import retrofit2.Response
import javax.inject.Inject

class RemoteDataSource @Inject constructor(
    private val rickAndMortyApi: RickAndMortyApi
) {

    suspend fun getAllCharacters(): Response<RickAndMortyResponse> {
        return rickAndMortyApi.getAllCharacters()
    }

    suspend fun getEpisodeById(id: String): Response<RickAndMortyEpisode> {
        return rickAndMortyApi.getEpisodeById(id)
    }

}