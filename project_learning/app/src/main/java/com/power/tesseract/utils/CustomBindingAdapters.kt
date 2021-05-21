package com.power.tesseract.utils

import android.graphics.drawable.Drawable
import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions

@BindingAdapter("setDrawableSrc")
fun setDrawableSrc(view: ImageView, drawable: Drawable) {
    Glide.with(view.context)
        .load(drawable)
        .transition(DrawableTransitionOptions.withCrossFade())
        .into(view)
}
