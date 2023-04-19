package com.baseapp.main.special_rc.slide_rc

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.base.common.base.adapter.BaseBindHolder
import com.base.common.base.adapter.BaseRecycleAdapter
import com.base.common.helper.setOnSingleClickListener
import com.baseapp.R
import com.baseapp.databinding.SlidItemLayoutBinding

/**
 * 可以把这个Adapter封成一个侧滑菜单基类
 */
class SlidAdapter : BaseRecycleAdapter<String, BaseBindHolder<SlidItemLayoutBinding>>() {
    private lateinit var rv: RecyclerView
    private var expandPosition = -1

    fun bind(rv: RecyclerView) {
        this.rv = rv
        rv.adapter = this
    }

    private fun getSlidView(holder: BaseBindHolder<*>?) = holder?.binding?.root as? ListSlidMenuItem

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseBindHolder<SlidItemLayoutBinding> {
        val holder = BaseBindHolder<SlidItemLayoutBinding>(parent, R.layout.slid_item_layout)
        getSlidView(holder)?.slidListener = object : ListSlidMenuItem.SlidListener {
            override fun onDownEvent() {
                if (expandPosition != holder.bindingAdapterPosition && expandPosition != -1) {
                    val oldVh = rv.findViewHolderForAdapterPosition(expandPosition) as? BaseBindHolder<*>
                    getSlidView(oldVh)?.smoothFoldMenu()
                    expandPosition = -1
                }
            }

            override fun onExpand() {
                expandPosition = holder.bindingAdapterPosition
            }

            override fun onFold() {
                if (expandPosition == holder.bindingAdapterPosition) {
                    expandPosition = -1
                }
            }
        }

        holder.binding?.delete?.setOnSingleClickListener {
            getItem(holder.bindingAdapterPosition)
        }

        return holder
    }

    override fun onBindViewHolder(holder: BaseBindHolder<SlidItemLayoutBinding>, position: Int) {
        if (position == expandPosition) {
            getSlidView(holder)?.expandMenu()
        } else {
            getSlidView(holder)?.foldMenu()
        }
    }
}