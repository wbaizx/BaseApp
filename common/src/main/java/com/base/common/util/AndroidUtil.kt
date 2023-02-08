package com.base.common.util

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.net.Uri
import android.os.Build
import android.util.DisplayMetrics
import android.util.TypedValue
import android.view.Gravity
import android.view.WindowManager
import android.widget.Toast
import androidx.core.content.FileProvider
import com.base.common.getBaseAppContext
import com.gyf.immersionbar.ImmersionBar
import java.io.File

/**
 * adb logcat -s GL_Thread -f /sdcard/log.txt
 * 输入GL_Thread的Log日志到sd卡中，需要数据线连接电脑
 */

private const val TAG = "AndroidUtil"

object AndroidUtil {

    /**
     * 获取屏幕宽度
     */
    fun getScreenWidth(): Int {
        return getBaseAppContext().resources.displayMetrics.widthPixels
    }

    /**
     * 获取屏幕显示高度
     * 可能不包含导航栏和刘海屏高度
     */
    fun getScreenShowHeight(): Int {
        return getBaseAppContext().resources.displayMetrics.heightPixels
    }

    /**
     * 获取屏幕高度
     * 包含刘海屏高度
     * 如果有导航栏，不包含导航栏高度
     */
    fun getScreenRealHeight(activity: Activity): Int {
        val outMetrics = DisplayMetrics()

        val windowManager: WindowManager = activity.getSystemService(Context.WINDOW_SERVICE) as WindowManager
        windowManager.defaultDisplay.getRealMetrics(outMetrics)

        //TODO 上面过时部分替代（未验证）
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
//            activity.display?.getRealMetrics(outMetrics)
//        }

        var screenHeight = outMetrics.heightPixels
        log(TAG, "getScreenHeight $screenHeight")

        if (ImmersionBar.hasNavigationBar(activity)) {
            //部分手机将导航栏变成小小的一条，并配合上手势操作，此时的导航栏高度不对，所以加上如下判断
            //如果导航栏高度 + 屏幕显示高度 大于 屏幕高度，也认为没有导航栏
            val navigationBarHeight = ImmersionBar.getNavigationBarHeight(activity)
            if ((navigationBarHeight + getScreenShowHeight()) <= screenHeight) {
                //如果有导航栏减去导航栏高度
                screenHeight -= navigationBarHeight
            }
        }
        log(TAG, "getScreenHeight $screenHeight")
        return screenHeight
    }

    //如果不想要BaseAPP实例（自定义view布局预览会无效），可以换成Resources.getSystem().displayMetrics
    fun sp2px(f: Float) =
        TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, f, getBaseAppContext().resources.displayMetrics)

    fun dp2px(f: Float) =
        TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, f, getBaseAppContext().resources.displayMetrics)

    /**
     * 获取手机网络连接状况
     */
    fun isNetworkAvailable(): Boolean {
        val connectivityManager = getBaseAppContext().getSystemService(Context.CONNECTIVITY_SERVICE) as? ConnectivityManager
        if (connectivityManager != null) {
            val activeNetwork = connectivityManager.activeNetwork
            if (activeNetwork != null) {
                val networkCapabilities = connectivityManager.getNetworkCapabilities(activeNetwork)
                if (networkCapabilities != null) {
                    if (networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI)) {
                        log(TAG, "is wifi")
                        return true
                    }
                    if (networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR)) {
                        log(TAG, "is mobile network")
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
    fun getVersionCode(): Int {
        val manager = getBaseAppContext().packageManager
        val info = manager.getPackageInfo(getBaseAppContext().packageName, 0)
        return info.versionCode
    }

    /**
     * 获取apk的版本号
     */
    fun getVersionCodeFromApk(filePath: String): Int {
        val pm = getBaseAppContext().packageManager
        val packInfo = pm.getPackageArchiveInfo(filePath, PackageManager.GET_ACTIVITIES)
        return packInfo?.versionCode ?: 0
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
        val uri: Uri
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            uri = FileProvider.getUriForFile(getBaseAppContext(), getBaseAppContext().packageName + ".fileProvider", file)
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
        } else {
            uri = Uri.fromFile(file)
        }
        intent.setDataAndType(uri, type)
        getBaseAppContext().startActivity(intent)
    }

    /**
     * 跳转拨电话页
     */
    fun call(context: Context, photo: String) {
        val intent = Intent(Intent.ACTION_DIAL)
        val data: Uri = Uri.parse("tel:$photo")
        intent.data = data
        context.startActivity(intent)
    }

    fun showToast(context: Context?, msg: String) {
        val toast = Toast.makeText(context ?: getBaseAppContext(), msg, Toast.LENGTH_SHORT)
        toast.setGravity(Gravity.CENTER, 0, 0)
        toast.show()
    }
}