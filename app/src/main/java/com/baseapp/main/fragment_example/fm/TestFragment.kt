package com.baseapp.main.fragment_example.fm


import android.os.Bundle
import android.view.View
import com.base.common.base.fragment.BaseBindFragment
import com.base.common.util.log
import com.baseapp.R
import com.baseapp.databinding.FragmentTestBinding

/**
 * 测试Fragment用例
 */
class TestFragment(private val text: String) : BaseBindFragment<FragmentTestBinding>() {
    private val TAG = "TestFragment"

    override fun getContentView() = R.layout.fragment_test

    override fun viewBind(binding: FragmentTestBinding) {
    }

    override fun createView(view: View) {
        log(TAG, "createView  $text")
        binding.testText.text = text
    }

    override fun onFirstVisible() {
        log(TAG, "onFirstVisible  $text")
    }

    override fun onVisible() {
        log(TAG, "onVisible  $text")
    }

    override fun onHide() {
        log(TAG, "onHide  $text")
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        log(TAG, "onCreate  $text")
    }

    override fun onDestroy() {
        log(TAG, "onDestroy  $text")
        super.onDestroy()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        log(TAG, "onDestroyView  $text")
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        log(TAG, "onViewCreated  $text")
    }

    override fun onStop() {
        super.onStop()
        log(TAG, "onStop  $text")
    }
}
