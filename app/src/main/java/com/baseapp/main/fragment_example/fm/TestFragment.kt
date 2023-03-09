package com.baseapp.main.fragment_example.fm


import android.os.Bundle
import android.view.View
import com.base.common.base.fragment.BaseBindFragment
import com.base.common.util.debugLog
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
        debugLog(TAG, "createView  $text")
        binding.testText.text = text
    }

    override fun onFirstVisible() {
        debugLog(TAG, "onFirstVisible  $text")
    }

    override fun onVisible() {
        debugLog(TAG, "onVisible  $text")
    }

    override fun onHide() {
        debugLog(TAG, "onHide  $text")
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        debugLog(TAG, "onCreate  $text")
    }

    override fun onDestroy() {
        debugLog(TAG, "onDestroy  $text")
        super.onDestroy()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        debugLog(TAG, "onDestroyView  $text")
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        debugLog(TAG, "onViewCreated  $text")
    }

    override fun onStop() {
        super.onStop()
        debugLog(TAG, "onStop  $text")
    }
}
