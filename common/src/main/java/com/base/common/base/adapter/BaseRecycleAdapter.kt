package com.base.common.base.adapter

import android.annotation.SuppressLint
import androidx.recyclerview.widget.RecyclerView

abstract class BaseRecycleAdapter<T, VH : RecyclerView.ViewHolder> : RecyclerView.Adapter<VH>() {
    private var items = listOf<T>()

    @SuppressLint("NotifyDataSetChanged")
    fun submitList(items: List<T>) {
        this.items = items
        notifyDataSetChanged()
    }

    fun putData(index: Int, t: T) {
        getMutableItems().run {
            add(index, t)
            items = this
        }
        notifyItemInserted(index)
    }

    private fun getMutableItems(): MutableList<T> {
        return items.let {
            when (it) {
                is ArrayList -> it
                is MutableList -> it
                else -> it.toMutableList()
            }
        }
    }

    fun getItems(): List<T> = items

    fun getItem(position: Int): T? {
        if (position > -1 && position < items.size) return items[position]
        return null
    }

    override fun getItemCount() = items.size
}