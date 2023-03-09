package com.base.common.base.activity

import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.ActivityInfo
import android.os.Bundle
import com.alibaba.android.arouter.launcher.ARouter
import com.base.common.R
import com.base.common.base.LoadDialog
import com.base.common.util.debugLog
import com.gyf.immersionbar.ImmersionBar

/**
 *  BaseActivity 基类
 */
private const val TAG = "BaseActivity"

abstract class BaseActivity : PermissionActivity() {
    private val loadDialog by lazy { LoadDialog(this) }

    @SuppressLint("SourceLockedOrientationActivity")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //强制竖屏，这里用代码方式，有问题，首次打开页面可能并非竖屏导致闪屏
        //可以使用 xml 中 android:screenOrientation="portrait"，不过每个页面添加有点麻烦
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT

        ARouter.getInstance().inject(this)

        configure()

        loadViewLayout()

        setImmersionBar()

        initView()
    }

    /**
     * 有些操作需要在此位置实现的，可以根据需求覆写
     */
    protected open fun configure() {}

    protected open fun loadViewLayout() {
        setContentView(getContentView())
    }

    protected abstract fun getContentView(): Int

    protected open fun setImmersionBar() {
        ImmersionBar.with(this)
            .statusBarColor(R.color.colorPrimary)
            .fitsSystemWindows(true)
            .init()
    }

    protected abstract fun initView()

    /**
     * 解决singleTask模式，第二次启动Activity A onNewIntent（Intent intent）被@Autowired注解的字段没有更新
     */
    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        setIntent(intent)
        ARouter.getInstance().inject(this)
        resetView()
    }

    /**
     * 在例如singleTask模式下重启activity，可能需要子类在这里做一些view和presenter的刷新重置清理等操作
     */
    protected open fun resetView() {}


    fun showLoadDialog() {
        if (!loadDialog.isShow) {
            debugLog(TAG, "showLoadDialog")
            loadDialog.showDialog()
        }
    }

    fun hideLoadDialog() {
        if (loadDialog.isShow) {
            debugLog(TAG, "hideLoadDialog")
            loadDialog.dismiss()
        }
    }
}