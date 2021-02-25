package com.codecool.morick.viewmodels

import android.app.Application
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.codecool.morick.data.DataStoreRepository
import com.codecool.morick.data.Repository
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
): AndroidViewModel(application) {

    val rickAndMortyResponse: MutableLiveData<NetworkResult<RickAndMortyResponse>> = MutableLiveData()
    val searchedRickAndMortyResponse: MutableLiveData<NetworkResult<RickAndMortyResponse>> = MutableLiveData()
    val readBackOnline = dataStoreRepository.readBackOnline.asLiveData()

    var networkStatus = false
    var backOnline = false

    fun getCharacters() = viewModelScope.launch {
        getCharactersSafeCall()
    }

    fun searchCharacters(name: String) = viewModelScope.launch {
        searchCharactersSafeCall(name)
    }

    fun saveBackOnline(backOnline: Boolean) = viewModelScope.launch(Dispatchers.IO) {
        dataStoreRepository.saveBackOnline(backOnline)
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

    private suspend fun searchCharactersSafeCall(name: String) {
        searchedRickAndMortyResponse.value = NetworkResult.Loading()
        if (hasInternetConnection()) {
            try {
                val response = repository.remote.searchCharacters(name)
                searchedRickAndMortyResponse.value = handleRickAndMortyResponse(response)
            } catch (e: Exception) {
                searchedRickAndMortyResponse.value = NetworkResult.Error("Characters Not Found")
            }
        } else {
            searchedRickAndMortyResponse.value = NetworkResult.Error("No Internet Connection")
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

    fun showNetworkStatus() {
        if (!networkStatus) {
            Toast.makeText(getApplication(), "No Internet Connection", Toast.LENGTH_SHORT).show()
            saveBackOnline(true)
        } else if (networkStatus) {
            if (backOnline) {
                Toast.makeText(getApplication(), "Internet Connection Was Restored", Toast.LENGTH_SHORT).show()
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