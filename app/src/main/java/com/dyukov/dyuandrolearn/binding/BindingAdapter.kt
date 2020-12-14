package com.dyukov.dyuandrolearn.binding

import android.util.TypedValue
import android.view.View
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.Guideline
import androidx.databinding.BindingAdapter
import com.dyukov.dyuandrolearn.extensions.TextOption
import io.github.kbiakov.codeview.CodeView


@BindingAdapter("visibility")
fun setVisibility(view: View, isVisible: Boolean) {
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

@BindingAdapter("app:codeText")
fun setViewCode(view: CodeView, codeText: String?) {
    if (codeText.isNullOrEmpty())
        return
    view.setCode(codeText)
}

@BindingAdapter("app:setPercent")
fun constraintPercent(guideLine: View, percent: Float?) {
    if (percent == null)
        return
    val params = (guideLine as Guideline).layoutParams as ConstraintLayout.LayoutParams
    params.guidePercent = percent // 45% // range: 0 <-> 1
    guideLine.layoutParams = params
}

@BindingAdapter(value = ["setText", "textOptions"], requireAll = true)
fun setTextOptions(text: TextView, string: String?, textOptions: List<TextOption>?) {
    text.text = string
    if (textOptions == null) {
        return
    }
    val textLength = text.length()
    val maxTextOption = textOptions.last()
    textOptions.sortedBy { it.length }.forEach { textOption ->
        if (textLength <= textOption.length) {
            text.maxLines = textOption.lines
            text.setTextSize(TypedValue.COMPLEX_UNIT_SP, textOption.size)
            return
        } else {
            text.maxLines = maxTextOption.lines
            text.setTextSize(TypedValue.COMPLEX_UNIT_SP, maxTextOption.size)
            return
        }
    }
}