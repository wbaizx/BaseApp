package com.baseapp.main.coordinator.coordinator1

import android.content.Context
import android.util.AttributeSet
import android.view.View
import androidx.coordinatorlayout.widget.CoordinatorLayout
import com.base.common.util.debugLog
import com.google.android.material.appbar.AppBarLayout

class AppBarBehavior(context: Context?, attrs: AttributeSet?) : AppBarLayout.Behavior(context, attrs) {
    private val TAG = "AppBarBehavior"

    override fun onLayoutChild(parent: CoordinatorLayout, abl: AppBarLayout, layoutDirection: Int): Boolean {
        return super.onLayoutChild(parent, abl, layoutDirection)
    }

    override fun onDependentViewChanged(parent: CoordinatorLayout, child: AppBarLayout, dependency: View): Boolean {
        debugLog(TAG, "onDependentViewChanged ${dependency.y}")
        return super.onDependentViewChanged(parent, child, dependency)
    }

    override fun onStartNestedScroll(
        parent: CoordinatorLayout,
        child: AppBarLayout,
        directTargetChild: View,
        target: View,
        nestedScrollAxes: Int,
        type: Int
    ): Boolean {
        val onStartNestedScroll = super.onStartNestedScroll(parent, child, directTargetChild, target, nestedScrollAxes, type)
        debugLog(TAG, "onStartNestedScroll $onStartNestedScroll")
        return onStartNestedScroll
    }
}