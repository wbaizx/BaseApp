package com.video.home

import android.Manifest
import android.app.ActivityManager
import android.graphics.SurfaceTexture
import android.util.Size
import android.view.Surface
import com.alibaba.android.arouter.facade.annotation.Route
import com.base.common.base.activity.BaseBindContentActivity
import com.base.common.base.activity.PermissionResult
import com.base.common.util.launchActivity
import com.base.common.util.debugLog
import com.gyf.immersionbar.BarHide
import com.gyf.immersionbar.ImmersionBar
import com.video.R
import com.video.databinding.ActivityCameraBinding
import com.video.home.camera.CameraControl
import com.video.home.camera.CameraControlListener
import com.video.home.gl.egl.GLSurfaceListener
import com.video.home.record.RecordListener
import com.video.home.record.RecordManager
import com.video.home.videolist.VideoListActivity
import java.util.concurrent.locks.ReentrantLock

private const val TAG = "CameraActivity"

@Route(path = "/video/home", name = "组件化video首页")
class CameraActivity : BaseBindContentActivity<ActivityCameraBinding>(), CameraControlListener, GLSurfaceListener, RecordListener {

    private var hasPermissions = false
    private var isSurfaceCreated = false

    private val cameraControl by lazy { CameraControl(this, this) }
    private val recordManager by lazy { RecordManager(this) }
    private val mSaveThread by lazy { SavePicture() }

    private val look = ReentrantLock()

    private val filterDialog: FilterDialog by lazy {
        FilterDialog().apply {
            setOnItemClickListener {
                binding.eglSurfaceView.switchFilterType(it)
            }
            setOnDismissListener {
                ImmersionBar.with(this@CameraActivity).hideBar(BarHide.FLAG_HIDE_BAR).init()
            }
            init(this@CameraActivity)
        }
    }

    override fun getContentView() = R.layout.activity_camera

    override fun viewBind(binding: ActivityCameraBinding) {}

    override fun setImmersionBar() {
        ImmersionBar.with(this).hideBar(BarHide.FLAG_HIDE_BAR).init()
    }

    override fun initView() {
        permissionRequest(object : PermissionResult {
            override fun onGranted() {
                begin()
            }

            override fun onDenied() {
                finish()
            }

            override fun onPermanentlyDenied() {
                finish()
            }
        }, Manifest.permission.RECORD_AUDIO, Manifest.permission.CAMERA)

        mSaveThread.start()

        binding.eglSurfaceView.setGlSurfaceListener(this)
        binding.eglSurfaceView.setCameraControlListener(this)

        binding.goVideoList.setOnClickListener {
            launchActivity(this@CameraActivity, VideoListActivity::class.java)
        }

        binding.switchCamera.setOnClickListener {
            cameraControl.switchCamera()
        }

        binding.takePicture.setOnClickListener {
            cameraControl.takePicture()
            binding.eglSurfaceView.takePicture()
        }

        binding.switchFilter.setOnClickListener {
            filterDialog.show()
        }

        binding.record.setOnClickListener {
            if (recordManager.isRecording) {
                recordManager.stopRecord()
                binding.record.text = "录制"
            } else if (recordManager.isReady) {
                recordManager.startRecord()
                binding.record.text = "停止录制"
            }
        }
    }

    private fun begin() {
        val am = getSystemService(ACTIVITY_SERVICE) as? ActivityManager
        if (am != null && am.deviceConfigurationInfo.reqGlEsVersion >= 0x30000) {
            look.lock()

            debugLog(TAG, "begin")
            hasPermissions = true
            openCamera()

            look.unlock()
        } else {
            finish()
        }
    }

    override fun onResume() {
        super.onResume()
        debugLog(TAG, "onResume")

        binding.record.text = "录制"
    }

    /**
     * eglSurfaceView 供相机预览的 SurfaceTexture 创建完成回调
     *
     * @param surfaceTexture
     */
    override fun onGLSurfaceCreated(surfaceTexture: SurfaceTexture) {
        cameraControl.setSurfaceTexture(surfaceTexture)

        look.lock()

        debugLog(TAG, "onSurfaceCreated")
        isSurfaceCreated = true
        openCamera()

        look.unlock()
    }

    private fun openCamera() {
        debugLog(TAG, "try openCamera $hasPermissions-$isSurfaceCreated")
        if (hasPermissions && isSurfaceCreated) {
            debugLog(TAG, "openCamera")
            cameraControl.openCamera()
        }
    }

    /**
     * CameraControl 预览大小确定回调
     * 因为是相机过来的数据，所以宽高需要对调
     *
     * @param cameraSize
     */
    override fun confirmCameraSize(cameraSize: Size) {
        val reallySize = Size(cameraSize.height, cameraSize.width)
        binding.eglSurfaceView.confirmReallySize(reallySize)
        recordManager.confirmReallySize(reallySize)
    }

    override fun onEncoderSurfaceCreated(surface: Surface) {
        binding.eglSurfaceView.onEncoderSurfaceCreated(surface)
    }

    override fun onEncoderSurfaceDestroy() {
        binding.eglSurfaceView.onEncoderSurfaceDestroy()
    }

    /**
     * 拍照数据回调
     */
    override fun imageAvailable(picture: SavePicture.Picture) {
        mSaveThread.putData(picture)
    }

    override fun onPause() {
        super.onPause()

        debugLog(TAG, "onPause")
        recordManager.onPause()
    }

    override fun onDestroy() {
        debugLog(TAG, "onDestroy")
        recordManager.onDestroy()
        cameraControl.onDestroy()
        binding.eglSurfaceView.onDestroy()
        mSaveThread.interrupt()
        super.onDestroy()
    }
}