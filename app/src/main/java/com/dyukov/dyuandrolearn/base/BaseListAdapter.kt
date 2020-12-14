package com.dyukov.dyuandrolearn.base

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter

abstract class BaseListAdapter<S : BaseItemState, I : Any>(
    diffUtilItemCallback: DiffUtil.ItemCallback<I>
) : ListAdapter<I, BaseViewHolder<S, I>>(diffUtilItemCallback) {

    lateinit var listener: Listener<I>

    constructor(diffUtilItemCallback: DiffUtil.ItemCallback<I>, listener: Listener<I>) : this(diffUtilItemCallback) {
        this.listener = listener
    }

    protected abstract fun itemState(item: I, listener: Listener<I>): S

    @LayoutRes
    protected abstract fun layoutResId(): Int

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder<S, I> {
        val inflater = LayoutInflater.from(parent.context)
        val itemBinding: ViewDataBinding = DataBindingUtil.inflate(inflater, layoutResId(), parent, false)
        return BaseViewHolder(itemBinding)
    }

    override fun onBindViewHolder(holder: BaseViewHolder<S, I>, position: Int) {
        val item = getItem(position)
        holder.bindItem(item, itemState(item, listener))
    }

    override fun onViewRecycled(holder: BaseViewHolder<S, I>) {
        super.onViewRecycled(holder)
        holder.onViewRecycled()
    }

    fun getPosition(item: I): Int {
        return currentList.indexOf(item)
    }

    fun getItemOrNull(position: Int): I? {
        if ((currentList.size) <= position) return null
        return currentList[position]
    }

    interface Listener<I> {
        fun onItemClick(item: I)
    }
}