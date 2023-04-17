package com.base.common.base.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView

open class BaseHolder(parent: ViewGroup, @LayoutRes id: Int) : RecyclerView.ViewHolder(
    LayoutInflater.from(parent.context).inflate(id, parent, false)
)

open class BaseBindHolder<VB : ViewDataBinding>(parent: ViewGroup, @LayoutRes id: Int) : RecyclerView.ViewHolder(
    LayoutInflater.from(parent.context).inflate(id, parent, false)
) {
    val binding = DataBindingUtil.bind<VB>(itemView)
}