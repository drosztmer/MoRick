package com.codecool.morick.bindingadapters

import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.core.view.isVisible
import androidx.databinding.BindingAdapter
import com.codecool.morick.adapters.CharactersAdapter
import com.codecool.morick.models.RickAndMortyResponse
import com.codecool.morick.util.NetworkResult
import com.todkars.shimmer.ShimmerRecyclerView

class CharactersBinding {

    companion object {

        @BindingAdapter("readApiResponse", "getAdapterItemCount", requireAll = true)
        @JvmStatic
        fun handleNetworkError(
            view: View,
            apiResponse: NetworkResult<RickAndMortyResponse>?,
            itemCount: Int
        ) {
            Log.d("ITEMCOUNTTTTTTTTTTTTTTTTTTTTTTTTTTTTTT: ", itemCount.toString())
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