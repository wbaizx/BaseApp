package com.base.common.base.dialog

import android.app.Dialog
import android.content.DialogInterface
import android.os.Bundle
import android.view.Gravity
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.view.WindowManager
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentActivity
import com.base.common.R
import com.base.common.util.debugLog

/**
 * 屏幕旋转后 会重建 Fragment ，可能导致字段，变量重置，需要注意！
 *
 * 还可以尝试 BottomSheetDialogFragment
 */
abstract class BaseFragmentDialog : DialogFragment() {
    companion object {
        private const val IS_SHOW = "IS_SHOW"
        private const val BACK_CANCEL = "BACK_CANCEL"
        private const val REBUILD_CANCEL = "REBUILD_CANCEL"
    }

    private val TAG = "BaseFragmentDialog"

    //可以去掉这个参数
    protected lateinit var mActivity: FragmentActivity

    //屏幕旋转后，且rebuildCancel=false，则dialog重建后会导致onDismissListener丢失
    private var onDismissListener: (() -> Unit)? = null

    /**
     * 是否是显示状态
     */
    var isShow = false

    /**
     * 是否调用了onSaveInstanceState保存状态
     * 如果调用了onSaveInstanceState之后不能再show和dimiss，会报错
     */
    private var isSaveInstanceState = false

    /**
     * 点击返回键是否关闭
     */
    private var backCancel = true

    private var rebuildCancel = true

    override fun onCreate(savedInstanceState: Bundle?) {
        debugLog(TAG, "onCreate")
        super.onCreate(savedInstanceState)

        //屏幕旋转的字段控制 can arguments
        if (savedInstanceState != null) {
            isShow = savedInstanceState.getBoolean(IS_SHOW, isShow)
            backCancel = savedInstanceState.getBoolean(BACK_CANCEL, backCancel)
            rebuildCancel = savedInstanceState.getBoolean(REBUILD_CANCEL, rebuildCancel)
            if (rebuildCancel) {
                dismiss()
            }
        }

        setStyle(STYLE_NO_FRAME, getStyle())
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        debugLog(TAG, "onCreateDialog")
        return super.onCreateDialog(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        debugLog(TAG, "onCreateView")

        dialog?.setOnKeyListener { dialog, keyCode, event ->
            //点击返回键也不关闭
            (keyCode == KeyEvent.KEYCODE_BACK && event.action == KeyEvent.ACTION_DOWN && !backCancel)
        }

        setDialogConfigure()

        return inflater.inflate(getLayout(), container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView(view)
        debugLog(TAG, "onViewCreated")
    }

    override fun onStart() {
        debugLog(TAG, "onStart")
        dialog?.window?.let {
            setWindowConfigure(it)
            it.setWindowAnimations(getStyleAnimations())
        }
        super.onStart()
    }

    /**
     * 设置主题
     */
    protected open fun getStyle() = R.style.default_fragmentdialog_style


    /**
     * 配置dialog
     */
    protected abstract fun setDialogConfigure()

    /**
     * 设置布局
     */
    protected abstract fun getLayout(): Int

    /**
     * 配置Window
     */
    protected open fun setWindowConfigure(win: Window) {
        val params = win.attributes
        params.gravity = Gravity.CENTER
        params.width = WindowManager.LayoutParams.WRAP_CONTENT
        params.height = WindowManager.LayoutParams.WRAP_CONTENT
        win.attributes = params
    }

    /**
     * 设置进出动画
     * R.style.animate_dialog
     * <style name="animate_dialog">
     * <item name="android:windowEnterAnimation">@anim/dialog_enter</item>
     * <item name="android:windowExitAnimation">@anim/dialog_out</item>
     * </style>
     */
    protected open fun getStyleAnimations() = 0

    /**
     * 初始化布局，每次调用showDialog都会走，只要关闭了再次show就会重走生命周期，不管页面中是不是同一个fragmentDialog
     */
    protected abstract fun initView(view: View)

    /**
     * 调用showDialog展示dialog，注意已经show了再调容易出错，可以用isShow标志控制
     */
    fun showDialog() {
        //onSaveInstanceState调用后，最好不要调showDialog
        if (!isSaveInstanceState) {
            val tag = getDialogTag()
            debugLog(TAG, "showDialog  $tag")
            val ft = mActivity.supportFragmentManager.beginTransaction()
            mActivity.supportFragmentManager.findFragmentByTag(tag)?.let {
                //这句话实际效果不行
                ft.remove(it)
            }
            show(ft, tag)
            isShow = true
        }
    }

    fun getDialogTag() = "fragment_tag_${this::class.java.simpleName}_${hashCode()}"

    /**
     * 任何关闭的情况都会回调该方法，包括activity旋转重建也会回调该方法，但我们在activity旋转时不需要回调
     * 所以这个方法用不到
     */
    override fun onDismiss(dialog: DialogInterface) {
        super.onDismiss(dialog)
    }

    /**
     * 屏幕旋转的字段控制
     */
    override fun onSaveInstanceState(outState: Bundle) {
        debugLog(TAG, "onSaveInstanceState")
        isSaveInstanceState = true
        outState.putBoolean(IS_SHOW, isShow)
        outState.putBoolean(BACK_CANCEL, backCancel)
        outState.putBoolean(REBUILD_CANCEL, rebuildCancel)
        super.onSaveInstanceState(outState)
    }

    /**
     * 点击外部或者返回键是会回调该方法
     */
    override fun onCancel(dialog: DialogInterface) {
        debugLog(TAG, "onCancel")
        isShow = false
        super.onCancel(dialog)
        onDismissListener?.invoke()
    }

    /**
     * 主动调用dismiss才会回调该方法
     */
    override fun dismiss() {
        //onSaveInstanceState调用后，不能再调dismiss
        if (isShow && !isSaveInstanceState) {
            isShow = false
            super.dismiss()
            onDismissListener?.invoke()
        }
    }

    override fun onDestroyView() {
        debugLog(TAG, "onDestroyView")
        super.onDestroyView()
    }

    override fun onDestroy() {
        debugLog(TAG, "onDestroy")
        super.onDestroy()
    }

    /**
     * 设置点击外部是否关闭
     */
    fun setCanceledOnTouchOutside(outsideCancel: Boolean) {
        dialog?.setCanceledOnTouchOutside(outsideCancel)
    }

    /**
     * 设置点击返回键是否关闭
     */
    fun setCanceledOnBack(backCancel: Boolean) {
        this.backCancel = backCancel
    }

    fun setRebuildCancel(rebuildCancel: Boolean) {
        this.rebuildCancel = rebuildCancel
    }

    /**
     * 设置关闭监听，只有主动关闭或者点击返回键或者点击外部才会调用
     */
    fun setOnDismissListener(onDismissListener: () -> Unit) {
        this.onDismissListener = onDismissListener
    }

    /**
     * 设置 FragmentActivity 必须设置，也可以通过子类添加构造方法在子类中配置
     */
    fun setContext(mActivity: FragmentActivity) {
        this.mActivity = mActivity
    }
}
