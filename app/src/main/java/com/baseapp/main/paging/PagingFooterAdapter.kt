package com.baseapp.main.paging

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.paging.LoadState
import androidx.paging.LoadStateAdapter
import androidx.recyclerview.widget.RecyclerView
import com.baseapp.R
import kotlinx.android.synthetic.main.paging_footer_layout.view.*

class PagingFooterAdapter(val retry: () -> Unit) : LoadStateAdapter<PagingFooterAdapter.PagingFooterVH>() {
    class PagingFooterVH(view: View) : RecyclerView.ViewHolder(view)

    override fun onCreateViewHolder(parent: ViewGroup, loadState: LoadState): PagingFooterVH {
        val pagingFooterVH = PagingFooterVH(LayoutInflater.from(parent.context).inflate(R.layout.paging_footer_layout, parent, false))
        pagingFooterVH.itemView.setOnClickListener {
            retry()
        }
        return pagingFooterVH
    }

    override fun onBindViewHolder(holder: PagingFooterVH, loadState: LoadState) {
        holder.itemView.footer_text.text = if (loadState is LoadState.Loading) "加载中..." else "完成"
    }
}