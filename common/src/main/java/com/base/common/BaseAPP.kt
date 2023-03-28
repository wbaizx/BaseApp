package com.base.common

import android.app.Activity
import android.app.Application
import android.content.Context
import android.content.pm.ApplicationInfo
import android.os.Build
import android.os.Bundle
import coil.ImageLoader
import coil.ImageLoaderFactory
import coil.decode.GifDecoder
import coil.decode.ImageDecoderDecoder
import coil.decode.SvgDecoder
import coil.decode.VideoFrameDecoder
import coil.disk.DiskCache
import coil.memory.MemoryCache
import com.alibaba.android.arouter.launcher.ARouter
import com.base.common.base.activity.BaseActivity
import com.base.common.util.debugLog
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.lang.ref.WeakReference
import java.util.*

/**
 * 基类 Application
 *
 * 记录一个翻翻工具 蚂蚁  https://pp.lanshuapi.com/
 *
 * 初始化三方 sdk 还可以可以使用 App Startup 方案
 */

private const val TAG = "Base_Application"

fun isDebug() = BaseAPP.isDebug
fun getCurrAct(): BaseActivity? {
    val baseAct = BaseAPP.currAct?.get() as? BaseActivity ?: return null
    if (baseAct.isDestroyed || baseAct.isFinishing) return null
    return baseAct
}

fun getBaseApplication() = BaseAPP.baseAppContext
fun getBaseActOrAppContext(): Context = getCurrAct() ?: getBaseApplication()

abstract class BaseAPP : Application(), ImageLoaderFactory {
    companion object {
        lateinit var baseAppContext: BaseAPP

        var currAct: WeakReference<Activity>? = null

        /**
         * 判断是否是 Debug 模式
         * 也可以使用 BuildConfig.DEBUG 判断（有些情况不准，具体什么情况百度）
         */
        val isDebug by lazy {
            baseAppContext.applicationInfo != null && baseAppContext.applicationInfo.flags and ApplicationInfo.FLAG_DEBUGGABLE != 0
        }

        private val allActivities = LinkedList<Activity>()

        private val activityLifecycleCallbacks = object : ActivityLifecycleCallbacks {
            override fun onActivityCreated(activity: Activity, savedInstanceState: Bundle?) {
                allActivities.add(activity)
                currAct = WeakReference<Activity>(activity)
                debugLog(TAG, "onActivityCreated $activity size ${allActivities.size}")
            }

            override fun onActivityStarted(activity: Activity) {
                debugLog(TAG, "onActivityStarted $activity size ${allActivities.size}")
            }

            override fun onActivityResumed(activity: Activity) {
                debugLog(TAG, "onActivityResumed $activity size ${allActivities.size}")
            }

            override fun onActivityPaused(activity: Activity) {
                debugLog(TAG, "onActivityPaused $activity size ${allActivities.size}")
            }

            override fun onActivityStopped(activity: Activity) {
                debugLog(TAG, "onActivityStopped $activity size ${allActivities.size}")
            }

            override fun onActivitySaveInstanceState(activity: Activity, outState: Bundle) {
                debugLog(TAG, "onActivitySaveInstanceState $activity size ${allActivities.size}")
            }

            override fun onActivityDestroyed(activity: Activity) {
                //调用exitApp后这里还是会执行
                allActivities.remove(activity)
                debugLog(TAG, "onActivityDestroyed $activity size ${allActivities.size}")
            }
        }

        @DelicateCoroutinesApi
        fun exitApp() {
            GlobalScope.launch(Dispatchers.Main) {
                debugLog(TAG, "exitApp size ${allActivities.size}")
                allActivities.forEach {
                    if (!it.isDestroyed && !it.isFinishing) {
                        it.finish()
                        debugLog(TAG, "exitApp finish $it")
                    }
                }
                allActivities.clear()
                debugLog(TAG, "exitApp X size ${allActivities.size}")
            }
        }
    }

    override fun onCreate() {
        super.onCreate()
        baseAppContext = this

        //注册Activity管理
        registerActivityLifecycleCallbacks(activityLifecycleCallbacks)

        initARouter()
        initKoin()
    }

    private fun initARouter() {
        if (isDebug()) {
            ARouter.openLog()
            ARouter.openDebug()
        }
        ARouter.init(this)
    }

    /**
     * 初始化koin注入框架，在主module或者需要单独运行的module的application中配置
     * 需要添加所有运行需要的module的di配置文件
     * 参考MainApp
     */
    abstract fun initKoin()

    override fun newImageLoader(): ImageLoader {
        val imageLoader = ImageLoader.Builder(this)
            .crossfade(2000)
            .memoryCache {
                MemoryCache.Builder(this)
                    .maxSizePercent(0.25)
                    .build()
            }
            .diskCache {
                DiskCache.Builder()
                    .directory(this.cacheDir.resolve("image_cache"))
                    .maxSizePercent(0.02)
                    .build()
            }
            .components {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                    add(ImageDecoderDecoder.Factory())
                } else {
                    add(GifDecoder.Factory())
                }
                add(SvgDecoder.Factory())
                add(VideoFrameDecoder.Factory())
            }
            .build()

        debugLog(TAG, "newImageLoader ${imageLoader.hashCode()}")

        return imageLoader
    }
}