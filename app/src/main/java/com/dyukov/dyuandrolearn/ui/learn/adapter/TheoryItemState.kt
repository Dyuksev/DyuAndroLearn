package com.dyukov.dyuandrolearn.ui.learn.adapter

import androidx.databinding.ObservableField
import com.dyukov.dyuandrolearn.base.BaseItemState
import com.dyukov.dyuandrolearn.base.BaseListAdapter
import com.dyukov.dyuandrolearn.data.db.model.Theory

class TheoryItemState(
    val item: Theory,
    val listener: BaseListAdapter.Listener<Theory>
) : BaseItemState() {
    val name = ObservableField<String>()

    init {
        name.set(item.name)
    }

    fun onItemClick() {
        listener.onItemClick(item)
    }
}