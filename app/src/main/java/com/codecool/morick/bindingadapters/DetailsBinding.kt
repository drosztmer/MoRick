package com.codecool.morick.bindingadapters

import android.widget.ImageView
import androidx.databinding.BindingAdapter
import coil.load
import com.codecool.morick.R

class DetailsBinding {

    companion object {

        @BindingAdapter("loadDetailsImageFromUrl")
        @JvmStatic
        fun loadDetailsImageFromUrl(imageView: ImageView, imageUrl: String) {
            imageView.load(imageUrl) {
                crossfade(600)
                error(R.drawable.ic_error_placeholder)
            }
        }

    }
}