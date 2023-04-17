package com.baseapp.main.paging

import android.view.ViewGroup
import android.widget.TextView
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import com.base.common.base.adapter.BaseHolder
import com.baseapp.R

class PagingDemoAdapter : PagingDataAdapter<String, BaseHolder>(PagingDiff()) {

    companion object Diff {
        class PagingDiff : DiffUtil.ItemCallback<String>() {
            override fun areItemsTheSame(oldItem: String, newItem: String): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(oldItem: String, newItem: String): Boolean {
                return oldItem == newItem
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = BaseHolder(parent, R.layout.paging_item_layout)

    override fun onBindViewHolder(holder: BaseHolder, position: Int) {
        val item = getItem(position)
        holder.itemView.findViewById<TextView>(R.id.paging_text).text = item
    }
}