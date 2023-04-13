package com.base.common.util

import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.content.res.Resources
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.net.Uri
import android.os.Build
import android.os.Handler
import android.os.Looper
import android.util.DisplayMetrics
import android.util.TypedValue
import android.view.Gravity
import android.view.WindowManager
import android.widget.Toast
import androidx.core.content.FileProvider
import com.base.common.getBaseActOrAppContext
import com.base.common.getBaseApplication
import com.base.common.util.http.CodeException
import com.base.common.util.http.NoNetworkException
import com.google.gson.stream.MalformedJsonException
import com.gyf.immersionbar.ImmersionBar
import kotlinx.coroutines.TimeoutCancellationException
import java.io.File
import java.lang.ref.WeakReference
import java.net.SocketTimeoutException
import java.net.UnknownHostException

/**
 * adb logcat -s commonUtil_tag -f /sdcard/log.txt
 * 输入GL_Thread的Log日志到sd卡中，需要数据线连接电脑
 */

private const val TAG = "commonUtil_tag"

/**
 * 获取屏幕宽度
 */
fun getScreenWidth(context: Context = getBaseActOrAppContext()): Int {
    return context.resources.displayMetrics.widthPixels
}

/**
 * 获取屏幕显示高度
 * 可能不包含导航栏和刘海屏高度
 */
fun getScreenShowHeight(context: Context = getBaseActOrAppContext()): Int {
    return context.resources.displayMetrics.heightPixels
}

/**
 * 获取屏幕高度
 * 包含刘海屏高度
 * 如果有导航栏，不包含导航栏高度
 */
fun getScreenRealHeight(context: Context = getBaseActOrAppContext()): Int {
    val windowManager: WindowManager = context.getSystemService(Context.WINDOW_SERVICE) as WindowManager

    var screenHeight = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
        //windowManager.currentWindowMetrics.windowInsets.getInsetsIgnoringVisibility(WindowInsets.Type.systemBars())
        windowManager.currentWindowMetrics.bounds.height()

    } else {
        val outMetrics = DisplayMetrics()
        windowManager.defaultDisplay.getRealMetrics(outMetrics)
        outMetrics.heightPixels
    }

    if (ImmersionBar.hasNavigationBar(context)) {
        //部分手机将导航栏变成小小的一条，并配合上手势操作，此时的导航栏高度不对，所以加上如下判断
        //如果导航栏高度 + 屏幕显示高度 大于 屏幕高度，也认为没有导航栏
        val navigationBarHeight = ImmersionBar.getNavigationBarHeight(context)
        if ((navigationBarHeight + getScreenShowHeight()) <= screenHeight) {
            //如果有导航栏减去导航栏高度
            screenHeight -= navigationBarHeight
        }
    }
    return screenHeight
}

fun sp2px(f: Float) =
//    TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, f, getBaseActOrAppContext().resources.displayMetrics)
    TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, f, Resources.getSystem().displayMetrics)

fun dp2px(f: Float) =
//    TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, f, getBaseActOrAppContext().resources.displayMetrics)
    TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, f, Resources.getSystem().displayMetrics)

/**
 * 获取手机网络连接状况
 */
fun isNetworkAvailable(): Boolean {
    val connectivityManager = getBaseActOrAppContext().getSystemService(Context.CONNECTIVITY_SERVICE) as? ConnectivityManager
    if (connectivityManager != null) {
        val activeNetwork = connectivityManager.activeNetwork
        if (activeNetwork != null) {
            val networkCapabilities = connectivityManager.getNetworkCapabilities(activeNetwork)
            if (networkCapabilities != null) {
                if (networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI)) {
                    debugLog(TAG, "is wifi")
                    return true
                }
                if (networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR)) {
                    debugLog(TAG, "is mobile network")
                    return true
                }
            }
        }
    }
    return false
}

/**
 * 获取当前版本号
 */
fun getVersionCode(): Long {
    val manager = getBaseActOrAppContext().packageManager
    val info = manager.getPackageInfo(getBaseApplication().packageName, 0)
    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
        info.longVersionCode
    } else {
        info.versionCode.toLong()
    }
}

/**
 * 获取apk的版本号
 */
fun getVersionCodeFromApk(filePath: String): Long {
    val pm = getBaseApplication().packageManager
    val packInfo = pm.getPackageArchiveInfo(filePath, PackageManager.GET_ACTIVITIES)
    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
        packInfo?.longVersionCode ?: 0
    } else {
        packInfo?.versionCode?.toLong() ?: 0
    }
}

/**
 * 安装apk
 * 另外还需要在manifest和xml中配置
 * 注意权限 REQUEST_INSTALL_PACKAGES
 */
fun installApk(file: File) {
    val intent = Intent(Intent.ACTION_VIEW)
    intent.addCategory(Intent.CATEGORY_DEFAULT)
    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
    val type = "application/vnd.android.package-archive"
    val uri = FileProvider.getUriForFile(getBaseApplication(), getBaseApplication().packageName + ".fileProvider", file)
    intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
    intent.setDataAndType(uri, type)
    getBaseActOrAppContext().startActivity(intent)
}

/**
 * 跳转拨电话页
 */
fun call(photo: String, context: Context = getBaseActOrAppContext()) {
    val intent = Intent(Intent.ACTION_DIAL)
    val data: Uri = Uri.parse("tel:$photo")
    intent.data = data
    context.startActivity(intent)
}

fun showError(e: Throwable, context: Context? = null) {
    when (e) {
        //withTimeout抛出 TimeoutCancellationException, withTimeoutOrNull不抛出
        is TimeoutCancellationException -> showToast("执行超时", context)
        is SocketTimeoutException -> showToast("连接超时", context)
        is UnknownHostException -> showToast("网络错误", context)
        is NoNetworkException -> showToast("无网络", context)
        is MalformedJsonException -> showToast("json解析错误", context)
        is CodeException -> showToast("服务器code码错误 + code=${e.message}", context)
        else -> showToast("未知错误", context)
    }
}

fun showToast(msg: String, context: Context? = null) {
    fun show(context: Context) {
        val toast = Toast.makeText(context, msg, Toast.LENGTH_SHORT)
        toast.setGravity(Gravity.CENTER, 0, 0)
        toast.show()
    }

    if (Looper.myLooper() == Looper.getMainLooper()) {
        show(context ?: getBaseActOrAppContext())

    } else {
        val weakContext = WeakReference<Context?>(context)
        Handler(Looper.getMainLooper()).post { show(weakContext.get() ?: getBaseActOrAppContext()) }
    }
}