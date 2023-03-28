package com.baseapp.main.show_dialog

import android.view.Gravity
import android.view.View
import android.view.Window
import android.view.WindowManager
import android.widget.TextView
import androidx.fragment.app.FragmentActivity
import com.base.common.base.dialog.BaseFragmentDialog
import com.base.common.helper.setOnSingleClickListener
import com.baseapp.R

class BottomDialog : BaseFragmentDialog {

    constructor() : super()

    constructor(mActivity: FragmentActivity) : super() {
        this.mActivity = mActivity
    }

    override fun setDialogConfigure() {
        setCanceledOnTouchOutside(true)
        setCanceledOnBack(true)
    }

    override fun getLayout() = R.layout.test_dialog_view

    override fun setWindowConfigure(win: Window) {
        val params = win.attributes
        params.gravity = Gravity.BOTTOM
        params.width = WindowManager.LayoutParams.WRAP_CONTENT
        params.height = WindowManager.LayoutParams.WRAP_CONTENT
        win.attributes = params
    }

    override fun getStyleAnimations() = R.style.AnimUp

    override fun initView(view: View) {
        view.findViewById<TextView>(R.id.text).text = "底部"
        view.findViewById<TextView>(R.id.basedialog_cancel).setOnSingleClickListener {
            dismiss()
        }
    }
}