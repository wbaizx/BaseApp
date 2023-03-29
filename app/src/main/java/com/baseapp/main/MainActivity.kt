package com.baseapp.main

import android.Manifest
import android.annotation.SuppressLint
import android.os.Debug
import androidx.lifecycle.lifecycleScope
import androidx.work.*
import com.alibaba.android.arouter.facade.annotation.Route
import com.base.common.BaseAPP
import com.base.common.base.activity.BaseBindContentActivity
import com.base.common.base.activity.PermissionResult
import com.base.common.base.dialog.DialogFactory
import com.base.common.helper.safeLaunch
import com.base.common.util.*
import com.base.common.util.http.ObjectBean
import com.base.common.util.http.ParcelableBean
import com.base.common.util.http.ParcelableBean2
import com.base.common.util.http.SerializableBean
import com.base.common.util.imageload.imgUrl
import com.base.common.util.imageload.loadBlurImg
import com.baseapp.R
import com.baseapp.databinding.ActivityMainBinding
import com.baseapp.main.coordinator.CoordinatorActivity
import com.baseapp.main.fragment_example.FragmentExampleActivity
import com.baseapp.main.item_animation.ItemAnimationMainActivity
import com.baseapp.main.lyrics.LyricsActivity
import com.baseapp.main.mediastore.MediaActivity
import com.baseapp.main.mvvm.MVVMDemoActivity
import com.baseapp.main.paging.PagingActivity
import com.baseapp.main.shape_btn.ShowShapeBtnActivity
import com.baseapp.main.show_dialog.ShowDialogActivity
import com.baseapp.main.special_rc.SpecialRCActivity
import com.baseapp.main.workmanager.MainWork
import kotlinx.coroutines.Dispatchers
import java.util.concurrent.TimeUnit

private const val TAG = "MainActivity"

@Route(path = "/main/main_home", name = "功能选择页")
class MainActivity : BaseBindContentActivity<ActivityMainBinding>() {

    override fun getContentView() = R.layout.activity_main

    override fun viewBind(binding: ActivityMainBinding) {}

    override fun setImmersionBar() {}

    override fun initView() {
        //adb pull /sdcard/Android/data/com.baseapp/files/baseapp.trace  可用Profiler查看分析文件
        Debug.startMethodTracing("baseapp")
        binding.mainImg.loadBlurImg(imgUrl)
        Debug.stopMethodTracing()

        binding.saveImg.setOnClickListener {
            lifecycleScope.safeLaunch(Dispatchers.IO) {
                val bitmap = ImageUtil.createBitmapFromView(binding.mainImg)
                val file = ImageUtil.savePicture(bitmap, "test.jpg")
                if (ImageUtil.updateGallery(file, bitmap.width, bitmap.height)) {
                    showToast("保存成功", this@MainActivity)
                }
                //注意如果是 ImageView 直接返回的 bitmap，用完后不要 recycle
//                bitmap.recycle()
            }
        }

        binding.login.setOnClickListener {
            //测试 ARouter 带参数跳转
            launchARouter("/login/login_home")
                .withSerializable("serializable_bean", SerializableBean("1", "2", arrayListOf("3", "4")))
                .withParcelable("parcelable_bean", ParcelableBean("1", "2", arrayListOf("3", "4"), ParcelableBean2("5", "6")))
                .withObject("object_bean", ObjectBean("1", "2", arrayListOf("3", "4")))
                .normalNavigation(this)
        }

        binding.fragmentExample.setOnClickListener {
            launchActivity(this, FragmentExampleActivity::class.java)
        }

        binding.coordinator.setOnClickListener {
            launchActivity(this, CoordinatorActivity::class.java)
        }

        binding.recyclerViewItemAnimation.setOnClickListener {
            launchActivity(this, ItemAnimationMainActivity::class.java)
        }

        binding.specialRc.setOnClickListener {
            launchActivity(this, SpecialRCActivity::class.java)
        }

        binding.showDialog.setOnClickListener {
            launchActivity(this, ShowDialogActivity::class.java)
        }

        binding.mvvmRoom.setOnClickListener {
            launchActivity(this, MVVMDemoActivity::class.java)
        }

        binding.shapeBtn.setOnClickListener {
            launchActivity(this, ShowShapeBtnActivity::class.java)
        }

        binding.lyricsBtn.setOnClickListener {
            launchActivity(this, LyricsActivity::class.java)
        }

        binding.camera.setOnClickListener {
            launchARouter("/video/home").loginNavigation(this)
        }

        binding.ndk.setOnClickListener {
            launchARouter("/ndk/ndk_home").loginNavigation(this)
        }

        binding.paging3.setOnClickListener {
            launchActivity(this, PagingActivity::class.java)
        }

        binding.media.setOnClickListener {
            launchActivity(this, MediaActivity::class.java)
        }

        binding.exit.setOnClickListener {
            DialogFactory.createNormalDialog(
                this,
                "警告",
                "确认退出？",
                "确定",
                { BaseAPP.exitApp() },
                "取消"
            ).showDialog()
        }

        startWork()

        permissionRequest(object : PermissionResult {
            override fun onShowRequestPermissionRationale() {
            }

            override fun onGranted() {
                debugLog(TAG, "hasPermissions")
            }

            override fun onDenied() {
                finish()
            }

            override fun onPermanentlyDenied() {
                finish()
            }
        }, Manifest.permission.WRITE_EXTERNAL_STORAGE)
    }

    /**
     * 后台执行任务，保证一定能执行，即使清理后台也会在下次启动时续上上次的任务
     */
    @SuppressLint("IdleBatteryChargingConstraints")
    private fun startWork() {
        debugLog("MainWork", "startWork")
        // 创建约束条件
        val constraints = Constraints.Builder()
//            .setRequiresBatteryNotLow(true)                 // 电量不低
//            .setRequiredNetworkType(NetworkType.CONNECTED)  // 连接了网络
//            .setRequiresCharging(true)                      // 充电中
//            .setRequiresStorageNotLow(true)                 // 储存空间不低
//            .setRequiresDeviceIdle(true)                    // 设备空闲中
            .build()

        val mainWorkRequest: WorkRequest = OneTimeWorkRequestBuilder<MainWork>()
            .setConstraints(constraints)    // 约束条件
            //重试任务时的策略
            .setBackoffCriteria(BackoffPolicy.LINEAR, 10, TimeUnit.SECONDS)
            .build()

        WorkManager.getInstance(this).getWorkInfoByIdLiveData(mainWorkRequest.id).observe(this) {
            debugLog("MainWork", "${it.state}")
            debugLog("MainWork", "${it.progress.getInt("int", 0)}")
        }
        WorkManager.getInstance(this).enqueue(mainWorkRequest)
    }

//    override fun onBackPressed() {
//        moveTaskToBack(true)
//    }
}
