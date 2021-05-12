package com.codecool.morick.bindingadapters

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.isVisible
import androidx.databinding.BindingAdapter
import com.codecool.morick.models.RickAndMortyCharacter
import com.codecool.morick.models.RickAndMortyLocation
import com.codecool.morick.util.NetworkResult
import com.todkars.shimmer.ShimmerRecyclerView

class LocationBinding {

    companion object {

        @BindingAdapter("readLocationResponse", "isLocationLoaded", requireAll = true)
        @JvmStatic
        fun handleLocationResponse(
            view: View,
            apiResponse: NetworkResult<RickAndMortyLocation>?,
            isLocationLoaded: Boolean
        ) {
            when (view) {
                is ImageView -> {
                    view.isVisible = apiResponse is NetworkResult.Error && !isLocationLoaded
                }
                is TextView -> {
                    view.isVisible = apiResponse is NetworkResult.Error && !isLocationLoaded
                    view.text = apiResponse?.message.toString()
                }
                is ConstraintLayout -> {
                    view.isVisible = (apiResponse is NetworkResult.Success) || (apiResponse is NetworkResult.Error && isLocationLoaded)
                }
            }
        }
    }
}