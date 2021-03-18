package com.codecool.morick.viewmodels

import android.app.Application
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.widget.Toast
import androidx.lifecycle.*
import com.codecool.morick.data.DataStoreRepository
import com.codecool.morick.data.Repository
import com.codecool.morick.data.database.entities.FavoriteCharacterEntity
import com.codecool.morick.models.RickAndMortyCharacter
import com.codecool.morick.models.RickAndMortyLocation
import com.codecool.morick.models.RickAndMortyResponse
import com.codecool.morick.util.NetworkResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Response
import java.lang.Exception
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val repository: Repository,
    private val dataStoreRepository: DataStoreRepository,
    application: Application
) : AndroidViewModel(application) {

    // Room

    val readFavoriteCharacters: LiveData<List<FavoriteCharacterEntity>> =
        repository.local.readFavoriteCharacters().asLiveData()

    fun insertFavoriteCharacter(favoriteCharacterEntity: FavoriteCharacterEntity) =
        viewModelScope.launch(Dispatchers.IO) {
            repository.local.insertFavoriteCharacter(favoriteCharacterEntity)
        }

    fun deleteFavoriteCharacter(favoriteCharacterEntity: FavoriteCharacterEntity) =
        viewModelScope.launch(Dispatchers.IO) {
            repository.local.deleteFavoriteCharacter(favoriteCharacterEntity)
        }

    fun deleteAllFavoriteCharacters() = viewModelScope.launch(Dispatchers.IO) {
        repository.local.deleteAllFavoriteCharacters()
    }

    // Retrofit

    val rickAndMortyResponse: MutableLiveData<NetworkResult<RickAndMortyResponse>> =
        MutableLiveData()
    val nextPageResponse: MutableLiveData<NetworkResult<RickAndMortyResponse>> =
        MutableLiveData()
    val locationResponse: MutableLiveData<NetworkResult<RickAndMortyLocation>> = MutableLiveData()

    val isLocationLoaded: MutableLiveData<Boolean> = MutableLiveData(false)

    val multipleCharacters: MutableLiveData<NetworkResult<List<RickAndMortyCharacter>>> =
        MutableLiveData()

    val readBackOnline = dataStoreRepository.readBackOnline.asLiveData()

    var networkStatus = false
    var backOnline = false

    fun getCharacters(name: String, pageNumber: Int) = viewModelScope.launch {
        getCharactersSafeCall(name, pageNumber)
    }

    fun getNextPage(name: String, pageNumber: Int) = viewModelScope.launch {
        getNextPageSafeCall(name, pageNumber)
    }


    fun getLocationById(locationId: String) = viewModelScope.launch {
        getLocationByIdSafeCall(locationId)
    }

    fun getMultipleCharacters(idList: String) = viewModelScope.launch {
        getMultipleCharactersSafeCall(idList)
    }

    fun saveBackOnline(backOnline: Boolean) = viewModelScope.launch(Dispatchers.IO) {
        dataStoreRepository.saveBackOnline(backOnline)
    }

    private suspend fun getCharactersSafeCall(name: String, pageNumber: Int) {
        rickAndMortyResponse.value = NetworkResult.Loading()
        if (hasInternetConnection()) {
            try {
                val response = repository.remote.getCharacters(name, pageNumber)
                rickAndMortyResponse.value = handleRickAndMortyResponse(response)
            } catch (e: Exception) {
                rickAndMortyResponse.value = NetworkResult.Error("Characters Not Found")
            }
        } else {
            rickAndMortyResponse.value = NetworkResult.Error("No Internet Connection")
        }

    }

    private suspend fun getNextPageSafeCall(name: String, pageNumber: Int) {
        nextPageResponse.value = NetworkResult.Loading()
        if (hasInternetConnection()) {
            try {
                val response = repository.remote.getCharacters(name, pageNumber)
                nextPageResponse.value = handleRickAndMortyResponse(response)
            } catch (e: Exception) {
                nextPageResponse.value = NetworkResult.Error("Characters Not Found")
            }
        } else {
            nextPageResponse.value = NetworkResult.Error("No Internet Connection")
        }
    }

    private suspend fun getLocationByIdSafeCall(locationId: String) {
        locationResponse.value = NetworkResult.Loading()
        if (hasInternetConnection()) {
            try {
                val response = repository.remote.getLocationById(locationId)
                locationResponse.value = handleLocationResponse(response)
            } catch (e: Exception) {
                locationResponse.value = NetworkResult.Error("Location Not Found")
            }
        } else {
            locationResponse.value = NetworkResult.Error("No Internet Connection")
        }
    }

    private suspend fun getMultipleCharactersSafeCall(idList: String) {
        multipleCharacters.value = NetworkResult.Loading()
        if (hasInternetConnection()) {
            try {
                val response = repository.remote.getMultipleCharacters(idList)
                multipleCharacters.value = handleMultipleCharactersResponse(response)
            } catch (e: Exception) {
                multipleCharacters.value = NetworkResult.Error("Characters Not Found")
            }
        } else {
            multipleCharacters.value = NetworkResult.Error("No Internet Connection")
        }
    }

    private fun handleRickAndMortyResponse(response: Response<RickAndMortyResponse>): NetworkResult<RickAndMortyResponse> {
        when {
            response.code().toString() == "404" -> {
                return NetworkResult.Error("Characters Not Found")
            }
            response.isSuccessful -> {
                val characters = response.body()
                return NetworkResult.Success(characters!!)
            }
            else -> {
                return NetworkResult.Error(response.message())
            }
        }
    }

    private fun handleLocationResponse(response: Response<RickAndMortyLocation>): NetworkResult<RickAndMortyLocation> {
        when {
            response.code().toString() == "404" -> {
                return NetworkResult.Error("Location Not Found")
            }
            response.isSuccessful -> {
                val location = response.body()
                return NetworkResult.Success(location!!)
            }
            else -> {
                return NetworkResult.Error(response.message())
            }
        }
    }

    private fun handleMultipleCharactersResponse(response: Response<List<RickAndMortyCharacter>>): NetworkResult<List<RickAndMortyCharacter>> {
        when {
            response.code().toString() == "404" -> {
                return NetworkResult.Error("Characters Not Found")
            }
            response.isSuccessful -> {
                val multipleCharacters = response.body()
                return NetworkResult.Success(multipleCharacters!!)
            }
            else -> {
                return NetworkResult.Error(response.message())
            }
        }
    }

    fun showNetworkStatus() {
        if (!networkStatus) {
            Toast.makeText(getApplication(), "No Internet Connection", Toast.LENGTH_SHORT).show()
            saveBackOnline(true)
        } else if (networkStatus) {
            if (backOnline) {
                Toast.makeText(
                    getApplication(),
                    "Internet Connection Was Restored",
                    Toast.LENGTH_SHORT
                ).show()
                saveBackOnline(false)
            }
        }
    }

    fun hasInternetConnection(): Boolean {
        val connectivityManager = getApplication<Application>().getSystemService(
            Context.CONNECTIVITY_SERVICE
        ) as ConnectivityManager
        val activeNetwork = connectivityManager.activeNetwork ?: return false
        val capabilities = connectivityManager.getNetworkCapabilities(activeNetwork) ?: return false
        return when {
            capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
            capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
            capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> true
            else -> false

        }
    }

}