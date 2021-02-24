package com.codecool.morick.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.codecool.morick.data.Repository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val repository: Repository,
    application: Application
): AndroidViewModel(application) {

}