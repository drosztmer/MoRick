package com.codecool.morick.bindingadapters

import android.widget.ImageView
import android.widget.TextView
import androidx.databinding.BindingAdapter
import coil.load
import com.codecool.morick.R
import com.codecool.morick.util.Constants.Companion.UNKNOWN

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

        @BindingAdapter("loadText")
        @JvmStatic
        fun loadText(textView: TextView, detailText: String) {
            if (detailText.isEmpty()) {
                textView.text = UNKNOWN
            } else {
                textView.text = detailText
            }
        }

    }
}