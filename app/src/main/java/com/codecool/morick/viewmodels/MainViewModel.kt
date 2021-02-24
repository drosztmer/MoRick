package com.codecool.morick.viewmodels

import android.app.Application
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.codecool.morick.data.Repository
import com.codecool.morick.models.RickAndMortyResponse
import com.codecool.morick.util.NetworkResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import retrofit2.Response
import java.lang.Exception
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val repository: Repository,
    application: Application
): AndroidViewModel(application) {

    val rickAndMortyResponse: MutableLiveData<NetworkResult<RickAndMortyResponse>> = MutableLiveData()

    fun getCharacters() = viewModelScope.launch {
        getCharactersSafeCall()
    }

    private suspend fun getCharactersSafeCall() {
        rickAndMortyResponse.value = NetworkResult.Loading()
        if (hasInternetConnection()) {
            try {
                val response = repository.remote.getAllCharacters()
                rickAndMortyResponse.value = handleRickAndMortyResponse(response)
            } catch (e: Exception) {
                rickAndMortyResponse.value = NetworkResult.Error("Characters Not Found")
            }
        } else {
            rickAndMortyResponse.value = NetworkResult.Error("No Internet Connection")
        }

    }

    fun handleRickAndMortyResponse(response: Response<RickAndMortyResponse>): NetworkResult<RickAndMortyResponse> {
        when {
            response.message().toString().contains("There is nothing here.") -> {
                return NetworkResult.Error("Request couldn't be completed")
            }
            response.message().toString().contains("Character not found") -> {
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