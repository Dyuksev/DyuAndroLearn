package com.dyukov.dyuandrolearn.binding

import android.view.View
import androidx.databinding.BindingAdapter
import timber.log.Timber

@BindingAdapter("visibility")
fun setVisibility(view: View, isVisible: Boolean) {
     Timber.d("+++ isPrivate: $isVisible")
     view.visibility = if (isVisible) View.VISIBLE else View.GONE
}

@BindingAdapter("app:selected")
fun setSelected(view: View, isSelected: Boolean) {
     view.isSelected = isSelected
     view.isEnabled = isSelected
}

@BindingAdapter("app:setSelectedOnly")
fun setSelectedOnly(view: View, isSelected: Boolean) {
     view.isSelected = isSelected
}