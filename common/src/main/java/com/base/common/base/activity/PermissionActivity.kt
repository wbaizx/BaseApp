package com.base.common.base.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import com.base.common.util.log
import pub.devrel.easypermissions.AppSettingsDialog
import pub.devrel.easypermissions.EasyPermissions

/**
 *  PermissionActivity 基类
 */
private const val TAG = "PermissionActivity"

abstract class PermissionActivity : AppCompatActivity(), EasyPermissions.PermissionCallbacks, EasyPermissions.RationaleCallbacks {
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
        log(TAG, "deniedPermission $requestCode")
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
        log(TAG, "onRequestPermissionsResult $requestCode")
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