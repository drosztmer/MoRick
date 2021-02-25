package com.codecool.morick.bindingadapters

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.core.view.isVisible
import androidx.databinding.BindingAdapter
import com.codecool.morick.models.RickAndMortyResponse
import com.codecool.morick.util.NetworkResult
import com.todkars.shimmer.ShimmerRecyclerView

class CharactersBinding {

    companion object {

        @BindingAdapter("handleNetworkError")
        @JvmStatic
        fun handleNetworkError(view: View, apiResponse: NetworkResult<RickAndMortyResponse>?) {
            when (view) {
                is ImageView -> {
                    view.isVisible = apiResponse is NetworkResult.Error
                }
                is TextView -> {
                    view.isVisible = apiResponse is NetworkResult.Error
                    view.text = apiResponse?.message.toString()
                }
                is ShimmerRecyclerView -> {
                    view.isVisible = apiResponse !is NetworkResult.Error
                }
            }
        }

    }
}