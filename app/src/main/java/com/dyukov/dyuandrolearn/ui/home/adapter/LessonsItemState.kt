package com.dyukov.dyuandrolearn.ui.home.adapter

import androidx.databinding.ObservableField
import com.dyukov.dyuandrolearn.base.BaseItemState
import com.dyukov.dyuandrolearn.base.BaseListAdapter
import com.dyukov.dyuandrolearn.data.db.model.Lesson

class LessonsItemState(
    val item: Lesson,
    val listener: BaseListAdapter.Listener<Lesson>
) : BaseItemState() {
    val name = ObservableField<String>()

    init {
        name.set(item.name)
    }

    fun onItemClick() {
        listener.onItemClick(item)
    }
}