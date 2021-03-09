package com.codecool.morick.data

import com.codecool.morick.data.network.RickAndMortyApi
import com.codecool.morick.models.Location
import com.codecool.morick.models.RickAndMortyCharacter
import com.codecool.morick.models.RickAndMortyLocation
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

    suspend fun getLocationById(id: String): Response<RickAndMortyLocation> {
        return rickAndMortyApi.getLocationById(id)
    }

    suspend fun getMultipleCharacters(idList: String): Response<List<RickAndMortyCharacter>> {
        return rickAndMortyApi.getMultipleCharacters(idList)
    }

}