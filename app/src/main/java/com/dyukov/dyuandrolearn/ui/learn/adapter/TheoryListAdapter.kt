package com.dyukov.dyuandrolearn.ui.learn.adapter

import androidx.recyclerview.widget.DiffUtil
import com.dyukov.dyuandrolearn.R
import com.dyukov.dyuandrolearn.base.BaseListAdapter
import com.dyukov.dyuandrolearn.data.db.model.Theory

class TheoryListAdapter : BaseListAdapter<TheoryItemState, Theory>(DIFF_CALLBACK) {

    override fun itemState(item: Theory, listener: Listener<Theory>): TheoryItemState =
        TheoryItemState(item, listener as Listener<Theory>)

    override fun layoutResId(): Int = R.layout.item_theory

    companion object {

        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<Theory>() {
            override fun areItemsTheSame(oldItem: Theory, newItem: Theory): Boolean =
                oldItem.id == newItem.id

            override fun areContentsTheSame(oldItem: Theory, newItem: Theory): Boolean =
                oldItem == newItem
        }
    }

}