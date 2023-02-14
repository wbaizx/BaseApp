package com.base.common.base.activity

import android.view.View
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import com.base.common.R
import com.base.common.databinding.ActivityBaseLayoutBinding
import com.base.common.databinding.BaseStubErrorLayoutBinding
import com.base.common.extension.setOnSingleClickListener

/**
 *  BaseContentActivity 基类
 */
abstract class BaseBindContentActivity<B : ViewDataBinding> : BaseBindActivity<B>() {

    private lateinit var baseBinding: ActivityBaseLayoutBinding
    private var errorBinding: BaseStubErrorLayoutBinding? = null

    override fun loadViewLayout() {
        baseBinding = DataBindingUtil.setContentView(this, R.layout.activity_base_layout)
        baseBinding.lifecycleOwner = this

        binding = DataBindingUtil.inflate(layoutInflater, getContentView(), baseBinding.baseRootLayout, false)
        binding.lifecycleOwner = this
        baseBinding.baseRootLayout.addView(binding.root)

        baseBinding.errorLayoutStub.setOnInflateListener { _, inflated ->
            val bind = BaseStubErrorLayoutBinding.bind(inflated)
            bind.lifecycleOwner = this
            bind.errorBtn.setOnSingleClickListener {
                errorContentClick()
            }
            errorBinding = bind
        }

        viewBind(binding)
    }

    /**
     * 展示内容视图布局
     */
    fun showContent() {
        if (baseBinding.errorLayoutStub.isInflated) {
            baseBinding.errorLayoutStub.root.visibility = View.GONE
        }
        binding.root.visibility = View.VISIBLE
    }

    /**
     * 展示错误视图布局
     */
    fun showErrorContent(msg: String? = "统一错误视图") {
        binding.root.visibility = View.GONE

        if (baseBinding.errorLayoutStub.isInflated) {
            baseBinding.errorLayoutStub.root.visibility = View.VISIBLE

        } else {
            baseBinding.errorLayoutStub.viewStub?.inflate()
        }

        errorBinding?.let { it.errorMsg.text = msg }
    }

    /**
     * 统一错误视图点击重试时回调，可在子类中重写完成自己的操作
     */
    protected open fun errorContentClick() {
        showContent()
    }

    override fun onDestroy() {
        baseBinding.unbind()
        errorBinding?.unbind()
        super.onDestroy()
    }
}