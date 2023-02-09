package com.base.common

import android.app.Activity
import android.app.Application
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
import com.base.common.util.log
import java.util.*

/**
 * 基类 Application
 *
 * 记录一个翻翻工具 蚂蚁  https://pp.lanshuapi.com/
 *
 * 初始化三方 sdk 还可以可以使用 App Startup 方案
 */

private const val TAG = "BaseAPP-Application"

fun isDebug() = BaseAPP.isDebug
fun getBaseAppContext() = BaseAPP.baseAppContext

abstract class BaseAPP : Application(), ImageLoaderFactory {
    companion object {
        lateinit var baseAppContext: BaseAPP

        /**
         * 判断是否是 Debug 模式
         * 也可以使用 BuildConfig.DEBUG 判断（有些情况不准，具体什么情况百度）
         */
        val isDebug by lazy {
            baseAppContext.applicationInfo != null && baseAppContext.applicationInfo.flags and ApplicationInfo.FLAG_DEBUGGABLE != 0
        }

        private val allActivities = Collections.synchronizedList(arrayListOf<Activity>())

        private val activityLifecycleCallbacks = object : ActivityLifecycleCallbacks {
            override fun onActivityCreated(activity: Activity, savedInstanceState: Bundle?) {
                allActivities.add(activity)
                log(TAG, "onActivityCreated ${activity.javaClass.simpleName} ${allActivities.size}")
            }

            override fun onActivityStarted(activity: Activity) {
                log(TAG, "onActivityStarted ${activity.javaClass.simpleName} ${allActivities.size}")
            }

            override fun onActivityResumed(activity: Activity) {
                log(TAG, "onActivityResumed ${activity.javaClass.simpleName} ${allActivities.size}")
            }

            override fun onActivityPaused(activity: Activity) {
                log(TAG, "onActivityPaused ${activity.javaClass.simpleName} ${allActivities.size}")
            }

            override fun onActivityStopped(activity: Activity) {
                log(TAG, "onActivityStopped ${activity.javaClass.simpleName} ${allActivities.size}")
            }

            override fun onActivitySaveInstanceState(activity: Activity, outState: Bundle) {
                log(TAG, "onActivitySaveInstanceState ${activity.javaClass.simpleName} ${allActivities.size}")
            }

            override fun onActivityDestroyed(activity: Activity) {
                //调用exitApp后这里还是会执行
                allActivities.remove(activity)
                log(TAG, "onActivityDestroyed ${activity.javaClass.simpleName} ${allActivities.size}")
            }
        }

        fun exitApp() {
            log(TAG, "exitApp ${allActivities.size}")

            //Collections.synchronizedList转换同步锁原理就是对其本身加锁，但不包含遍历
            //所以遍历需加同步锁，对象就是自身
            synchronized(allActivities) {
                allActivities.forEach {
                    it.finish()
                }
                allActivities.clear()
            }

            log(TAG, "exitApp ${allActivities.size}")
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

        log(TAG, "newImageLoader ${imageLoader.hashCode()}")

        return imageLoader
    }
}