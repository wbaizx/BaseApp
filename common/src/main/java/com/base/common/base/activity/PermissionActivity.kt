package com.base.common.base.activity

import android.app.Activity
import android.content.pm.PackageManager
import android.os.Bundle
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.lifecycle.lifecycleScope
import com.base.common.helper.iteratorForEach
import com.base.common.helper.safeLaunch
import com.base.common.util.debugLog
import java.util.*

/**
 *  PermissionActivity 基类
 */
private const val TAG = "PermissionActivity"

fun Activity.safePermissionRequest(result: PermissionResult, vararg perms: String) {
    if (this is PermissionActivity) {
        this.permissionRequest(result, *perms)
    }
}

interface PermissionResult {
    fun onShowRequestPermissionRationale() {}
    fun onGranted()
    fun onDenied() {}
    fun onPermanentlyDenied() {}
}

private data class PermissionRequestBean(val result: PermissionResult, val requestPerms: List<String>)

abstract class PermissionActivity : AppCompatActivity() {

    private val permissionCalls by lazy { LinkedList<PermissionRequestBean>() }
    private lateinit var permissionLaunch: ActivityResultLauncher<Array<String>>

    override fun onCreate(savedInstanceState: Bundle?) {
        permissionLaunch = registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { permissions: Map<String, Boolean> ->
            debugLog(TAG, "onPermissionsResult $permissions")
            matchResults(permissions)
        }
        super.onCreate(savedInstanceState)
    }

    private fun hasPermission(vararg perms: String) =
        perms.all { ActivityCompat.checkSelfPermission(this, it) == PackageManager.PERMISSION_GRANTED }

    private fun isShowRequestPermissionRationale(perms: List<String>) =
        perms.any { ActivityCompat.shouldShowRequestPermissionRationale(this, it) }

    fun permissionRequest(result: PermissionResult, vararg perms: String) {
        lifecycleScope.safeLaunch {
            val requestPerms = perms.filter { !hasPermission(it) }
            if (requestPerms.isEmpty()) {
                result.onGranted()
                return@safeLaunch
            }

            if (isShowRequestPermissionRationale(requestPerms)) {
                result.onShowRequestPermissionRationale()
            }

            permissionCalls.add(PermissionRequestBean(result, requestPerms))
            permissionLaunch.launch(requestPerms.toTypedArray())
        }
    }

    private fun matchResults(resultMap: Map<String, Boolean>) {
        permissionCalls.iteratorForEach { requestBean ->
            if (requestBean.requestPerms.size == resultMap.size &&
                requestBean.requestPerms.all { resultMap[it] != null }
            ) {
                when {
                    resultMap.all { it.value } -> {
                        requestBean.result.onGranted()
                    }
                    isShowRequestPermissionRationale(resultMap.keys.toList()) -> {
                        requestBean.result.onDenied()
                    }
                    else -> {
                        requestBean.result.onPermanentlyDenied()
                    }
                }

                remove()
            }
        }
    }
}