package com.base.common.base

import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.ActivityInfo
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import com.alibaba.android.arouter.launcher.ARouter
import com.base.common.R
import com.base.common.extension.setOnSingleClickListener
import com.base.common.util.http.CodeException
import com.base.common.util.http.NoNetworkException
import com.base.common.util.log
import com.base.common.util.showToast
import com.google.gson.stream.MalformedJsonException
import com.gyf.immersionbar.ImmersionBar
import kotlinx.android.synthetic.main.activity_base_layout.*
import kotlinx.android.synthetic.main.base_stub_error_layout.*
import pub.devrel.easypermissions.AppSettingsDialog
import pub.devrel.easypermissions.EasyPermissions
import java.net.SocketTimeoutException
import java.net.UnknownHostException


/**
 *  Activity基类
 */
private const val TAG = "BaseActivity"

abstract class BaseActivity : AppCompatActivity(), EasyPermissions.PermissionCallbacks, EasyPermissions.RationaleCallbacks {
    private val loadDialog by lazy { LoadDialog(this) }

    private lateinit var contentLayout: View

    private var isLoadErrorLayout = false

    @SuppressLint("SourceLockedOrientationActivity")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //强制竖屏，这里用代码方式，有问题，首次打开页面可能并非竖屏导致闪屏
        //可以使用 xml 中 android:screenOrientation="portrait"，不过每个页面添加有点麻烦
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT

        ARouter.getInstance().inject(this)

        configure()

        //绑定默认布局
        setContentView(R.layout.activity_base_layout)
        //添加content布局
        contentLayout = bindView(LayoutInflater.from(this), baseRootLayout)
        baseRootLayout.addView(contentLayout)

        setImmersionBar()

        initView()
        initData()
    }

    /**
     * 初始化和绑定content布局
     */
    protected open fun bindView(inflater: LayoutInflater, container: ViewGroup): View {
        return inflater.inflate(getContentView(), container, false)
    }

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
     * 有些操作需要在此位置实现的，可以根据需求覆写
     */
    protected open fun configure() {

    }

    protected abstract fun getContentView(): Int

    protected open fun setImmersionBar() {
        ImmersionBar.with(this)
            .statusBarColor(R.color.colorPrimary)
            .fitsSystemWindows(true)
            .init()
    }

    protected abstract fun initView()

    protected abstract fun initData()

    /**
     * 在例如singleTask模式下重启activity，可能需要子类在这里做一些view和presenter的刷新重置清理等操作
     */
    protected open fun resetView() {
    }

    /**
     * 展示内容视图布局
     */
    fun showContent() {
        if (isLoadErrorLayout) {
            errorLayout.visibility = View.GONE
        }
        contentLayout.visibility = View.VISIBLE
    }

    /**
     * 展示错误视图布局
     */
    fun showErrorContent(msg: String? = "统一错误视图") {
        if (isLoadErrorLayout) {
            errorLayout.visibility = View.VISIBLE

        } else {
            errorLayoutStub.inflate()
//            errorLayoutStub.visibility = View.VISIBLE

            errorBtn.setOnSingleClickListener {
                errorContentClick()
            }
        }

        contentLayout.visibility = View.GONE

        errorMsg.text = msg

        isLoadErrorLayout = true
    }

    /**
     * 统一错误视图点击重试时回调，可在子类中重写完成自己的操作
     */
    protected open fun errorContentClick() {
        showContent()
    }

    fun showLoadDialog() {
        if (!loadDialog.isShow) {
            log(TAG, "showLoadDialog")
            loadDialog.showDialog()
        }
    }

    fun hideLoadDialog() {
        if (loadDialog.isShow) {
            log(TAG, "hideLoadDialog")
            loadDialog.dismiss()
        }
    }

    /**
     * 某些情况下需要继承此方法做后续操作，比如关闭下拉刷新状态
     */
    open fun runError(e: Exception) {
        when (e) {
            is SocketTimeoutException -> showToast(this, "连接超时")
            is UnknownHostException -> showToast(this, "网络错误")
            is NoNetworkException -> showToast(this, "无网络")
            is MalformedJsonException -> showToast(this, "json解析错误")
            is CodeException -> showToast(this, "服务器code码错误 + code=${e.message}")
            else -> showToast(this, "未知错误")
        }
    }

    /**
     * 权限允许后回调
     */
    override fun onPermissionsGranted(requestCode: Int, perms: MutableList<String>) {
        log(TAG, "onPermissionsGranted $requestCode")
    }

    /**
     * 权限拒绝后回调
     */
    override fun onPermissionsDenied(requestCode: Int, perms: MutableList<String>) {
        log(TAG, "onPermissionsDenied $requestCode")
        if (EasyPermissions.somePermissionPermanentlyDenied(this, perms)) {
            //如果用户选择了拒绝并不在提示，默认指引到手动打开
            log(TAG, "Denied and not prompted")
            AppSettingsDialog.Builder(this)
                .setTitle("跳转到手动打开")
                .setRationale("跳转到手动打开")
                .build().show()
        } else {
            deniedPermission(requestCode, perms)
        }
    }

    /**
     * 权限拒绝并且拒绝手动打开
     */
    protected open fun deniedPermission(requestCode: Int, perms: MutableList<String>) {
    }

    /**
     * 权限拒绝过一次后的提示框被拒绝
     */
    override fun onRationaleDenied(requestCode: Int) {
        log(TAG, "onRationaleDenied $requestCode")
    }

    /**
     * 权限拒绝过一次后的提示框被允许
     */
    override fun onRationaleAccepted(requestCode: Int) {
        log(TAG, "onRationaleAccepted $requestCode")
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this)
    }

    /**
     * 跳转系统打开权限页面返回，或者关闭跳转系统打开权限的指引弹窗被关闭后回调
     * 此时不会再次调用AfterPermissionGranted注解方法，所以这里要再次检查权限
     */
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == AppSettingsDialog.DEFAULT_SETTINGS_REQ_CODE) {
            resultCheckPermissions()
        }
    }

    /**
     * 跳转系统打开权限页面返回，或者跳转系统打开权限的指引弹窗被关闭后回调
     * 此时不会再次调用AfterPermissionGranted注解方法，所以这里要再次检查权限
     */
    protected open fun resultCheckPermissions() {
    }
}