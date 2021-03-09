package com.codecool.morick.bindingadapters

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.core.view.isVisible
import androidx.databinding.BindingAdapter
import com.codecool.morick.models.RickAndMortyCharacter
import com.codecool.morick.models.RickAndMortyResponse
import com.codecool.morick.util.NetworkResult
import com.todkars.shimmer.ShimmerRecyclerView

class LocationCharactersBinding {

    companion object {

        @BindingAdapter("readMultipleCharactersResponse", "getMultipleCharactersItemCount", requireAll = true)
        @JvmStatic
        fun handleMultipleCharactersError(
            view: View,
            apiResponse: NetworkResult<List<RickAndMortyCharacter>>?,
            itemCount: Int
        ) {
            when (view) {
                is ImageView -> {
                    view.isVisible = apiResponse is NetworkResult.Error && itemCount == 0
                }
                is TextView -> {
                    view.isVisible = apiResponse is NetworkResult.Error && itemCount == 0
                    view.text = apiResponse?.message.toString()
                }
                is ShimmerRecyclerView -> {
                    view.isVisible = apiResponse !is NetworkResult.Error || itemCount > 0
                }
            }
        }

    }

}