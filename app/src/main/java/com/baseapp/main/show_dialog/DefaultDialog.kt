package com.baseapp.main.show_dialog

import android.view.View
import android.widget.TextView
import androidx.fragment.app.FragmentActivity
import com.base.common.base.dialog.BaseFragmentDialog
import com.base.common.extension.setOnSingleClickListener
import com.baseapp.R

class DefaultDialog : BaseFragmentDialog {

    constructor() : super()

    constructor(mActivity: FragmentActivity) : super() {
        this.mActivity = mActivity
    }

    override fun setDialogConfigure() {
        setCanceledOnTouchOutside(true)
        setCanceledOnBack(true)
    }

    override fun getLayout() = R.layout.test_dialog_view

    override fun initView(view: View) {
        view.findViewById<TextView>(R.id.text).text = "默认"
        view.findViewById<TextView>(R.id.basedialog_cancel).setOnSingleClickListener { dismiss() }
    }
}