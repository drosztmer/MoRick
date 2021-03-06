package com.codecool.morick.bindingadapters

import android.util.Log
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.databinding.BindingAdapter
import coil.load
import com.codecool.morick.R
import com.codecool.morick.util.Constants.Companion.ALIVE
import com.codecool.morick.util.Constants.Companion.DEAD
import com.codecool.morick.util.Constants.Companion.UNKNOWN
import com.codecool.morick.util.Constants.Companion.UNKNOWN_LOWERCASE

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
            Log.d("TEXT: ", detailText)
            if (detailText == UNKNOWN_LOWERCASE || detailText.isEmpty()) {
                textView.text = UNKNOWN
            } else {
                textView.text = detailText
            }
        }

        @BindingAdapter("applyStatusColor")
        @JvmStatic
        fun applyStatusColor(textView: TextView, status: String) {
            if (status == ALIVE) {
                textView.setTextColor(ContextCompat.getColor(textView.context, R.color.green))
            } else if (status == DEAD) {
                textView.setTextColor(ContextCompat.getColor(textView.context, R.color.red))
            }
        }
    }
}