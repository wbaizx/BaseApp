package com.baseapp.main.paging

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.baseapp.R
import kotlinx.android.synthetic.main.paging_item_layout.view.*

class PagingDemoAdapter : PagingDataAdapter<String, PagingDemoAdapter.PagingVH>(PagingDiff()) {
    class PagingVH(view: View) : RecyclerView.ViewHolder(view)

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

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        PagingVH(LayoutInflater.from(parent.context).inflate(R.layout.paging_item_layout, parent, false))

    override fun onBindViewHolder(holder: PagingVH, position: Int) {
        val item = getItem(position)
        holder.itemView.paging_text.text = item
    }
}