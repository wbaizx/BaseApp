package com.video.home.camera

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.BitmapFactory
import android.graphics.ImageFormat
import android.graphics.SurfaceTexture
import android.hardware.camera2.CameraCharacteristics
import android.hardware.camera2.CameraManager
import android.media.MediaCodec
import android.util.Size
import android.view.Surface
import androidx.camera.core.*
import androidx.camera.core.impl.utils.Exif
import androidx.camera.extensions.ExtensionMode
import androidx.camera.extensions.ExtensionsManager
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.lifecycleScope
import androidx.work.await
import com.base.common.util.getScreenRealHeight
import com.base.common.util.getScreenWidth
import com.base.common.util.log
import com.video.home.SavePicture
import kotlinx.coroutines.launch
import java.io.ByteArrayInputStream
import java.util.concurrent.Executors
import kotlin.math.abs

class CameraControl(private var activity: FragmentActivity?, private var cameraControlListener: CameraControlListener?) {
    companion object {
        private const val TAG = "CameraControl"
        private const val RATIO_4_3_VALUE = 4.0 / 3.0
        private const val RATIO_16_9_VALUE = 16.0 / 9.0
    }

    //期望预览尺寸比例
    private var screenAspectRatio = AspectRatio.RATIO_16_9

    //最终确定的尺寸
    private lateinit var previewSize: Size

    private var surfaceTexture: SurfaceTexture? = null
    private var previewSurface: Surface? = null

    private var cameraProvider: ProcessCameraProvider? = null
    private var cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA
    private var camera: Camera? = null
    private var imageCapture: ImageCapture? = null

    private var cameraExecutor = Executors.newSingleThreadExecutor()

    fun setSurfaceTexture(surfaceTexture: SurfaceTexture) {
        this.surfaceTexture = surfaceTexture
        previewSurface = Surface(surfaceTexture)
    }

    private fun hasBackCamera(): Boolean {
        return cameraProvider?.hasCamera(CameraSelector.DEFAULT_BACK_CAMERA) ?: false
    }

    private fun hasFrontCamera(): Boolean {
        return cameraProvider?.hasCamera(CameraSelector.DEFAULT_FRONT_CAMERA) ?: false
    }

    fun switchCamera() {
        activity?.run {
            ContextCompat.getMainExecutor(this).execute {
                cameraSelector = if (CameraSelector.DEFAULT_FRONT_CAMERA == cameraSelector && hasBackCamera()) {
                    CameraSelector.DEFAULT_BACK_CAMERA
                } else if (CameraSelector.DEFAULT_BACK_CAMERA == cameraSelector && hasFrontCamera()) {
                    CameraSelector.DEFAULT_FRONT_CAMERA
                } else {
                    return@execute
                }

                aspectRatio(this)

                bindCameraUseCases(this)
            }
        }
    }

    fun openCamera() {
        activity?.run {
            val cameraProviderFuture = ProcessCameraProvider.getInstance(this)
            cameraProviderFuture.addListener({
                log(TAG, "openCamera 2")
                cameraProvider = cameraProviderFuture.get()

                cameraSelector = when {
                    hasBackCamera() -> CameraSelector.DEFAULT_BACK_CAMERA
                    hasFrontCamera() -> CameraSelector.DEFAULT_FRONT_CAMERA
                    else -> throw IllegalStateException("Back and front camera are unavailable")
                }

                aspectRatio(this)

                bindCameraUseCases(this)

            }, ContextCompat.getMainExecutor(this))
        }
    }

    private fun aspectRatio(activity: FragmentActivity): Int {
        val previewRatio = getScreenRealHeight(activity).toDouble() / getScreenWidth()
        if (abs(previewRatio - RATIO_4_3_VALUE) <= abs(previewRatio - RATIO_16_9_VALUE)) {
            return AspectRatio.RATIO_4_3
        }
        return AspectRatio.RATIO_16_9
    }

    private fun bindCameraUseCases(activity: FragmentActivity) {
        val cameraProvider = cameraProvider ?: throw IllegalStateException("Camera initialization failed.")

        log(TAG, "lifecycleScope")
        activity.lifecycleScope.launch {
            val extensionsManager = ExtensionsManager.getInstanceAsync(activity, cameraProvider).await()
            log(TAG, "BOKEH ${extensionsManager.isExtensionAvailable(cameraSelector, ExtensionMode.BOKEH)}")
            log(TAG, "NIGHT ${extensionsManager.isExtensionAvailable(cameraSelector, ExtensionMode.NIGHT)}")
            log(TAG, "FACE_RETOUCH ${extensionsManager.isExtensionAvailable(cameraSelector, ExtensionMode.FACE_RETOUCH)}")
            log(TAG, "AUTO ${extensionsManager.isExtensionAvailable(cameraSelector, ExtensionMode.AUTO)}")
            log(TAG, "HDR ${extensionsManager.isExtensionAvailable(cameraSelector, ExtensionMode.HDR)}")
        }
        log(TAG, "lifecycleScope x")

        val rotation = activity.windowManager.defaultDisplay.rotation

        //对于分辨率，使用 setTargetAspectRatio 设置比例后让cameraX自动选择合适的分辨率
        //也可以使用 setTargetResolution 指定分辨率，但cameraX最终选择的不一定是你设置的
        val preview = Preview.Builder()
            .setTargetRotation(rotation)
            .setTargetAspectRatio(screenAspectRatio)
//            .setTargetResolution(Size(200,200))
            .build()

        imageCapture = ImageCapture.Builder()
            .setCaptureMode(ImageCapture.CAPTURE_MODE_MINIMIZE_LATENCY)
            .setTargetAspectRatio(screenAspectRatio)
//            .setTargetResolution(Size(200, 200))
            .setTargetRotation(rotation)
            .build()

        val imageAnalyzer = ImageAnalysis.Builder()
            .setTargetAspectRatio(screenAspectRatio)
            .setTargetRotation(rotation)
            .build()
        imageAnalyzer.setAnalyzer(cameraExecutor) {
//            ImageFormat.YUV_420_888
            log(TAG, "analyze format ${it.format} rotation ${it.imageInfo.rotationDegrees} cropRect ${it.cropRect}")
            it.close()
        }

        cameraProvider.unbindAll()

        previewSurface?.let { surface ->
            try {
                camera = cameraProvider.bindToLifecycle(activity, cameraSelector, preview, imageCapture, imageAnalyzer)

                preview.setSurfaceProvider { request ->
                    checkSize(activity)

                    log(TAG, "request.resolution ${request.resolution}")
                    //这里确定最终的分辨率， request.resolution是cameraX选择的最合适的预览分辨率
                    //这个分辨率决定 预览、surface模式拍照、录制的分辨率
                    //你也可以不用这个分辨率，从checkSize方法中遍历一个你想要的分辨率设置进去，不会影响录制尺寸，但可能影响拍照尺寸
                    resetPreviewSize(Size(getScreenRealHeight(activity), getScreenWidth()))

                    request.provideSurface(surface, cameraExecutor) {
                        log(TAG, "provideSurface")
                    }

                    request.setTransformationInfoListener(cameraExecutor) { transformationInfo ->
                        log(TAG, "cropRect ${transformationInfo.cropRect}")
                    }
                }

                observeCameraState(activity, camera?.cameraInfo)
            } catch (exc: Exception) {
            }
        }
    }

    private fun resetPreviewSize(size: Size) {
        log(TAG, "resetPreviewSize size $size - ratio ${size.width.toFloat() / size.height}")
        surfaceTexture?.setDefaultBufferSize(size.width, size.height)
        previewSize = size
    }

    private fun observeCameraState(activity: FragmentActivity, cameraInfo: CameraInfo?) {
        cameraInfo?.cameraState?.observe(activity) { cameraState ->
            run {
                when (cameraState.type) {
                    CameraState.Type.PENDING_OPEN -> {
                        log(TAG, "PENDING_OPEN")
                    }
                    CameraState.Type.OPENING -> {
                        log(TAG, "OPENING")
                    }
                    CameraState.Type.OPEN -> {
                        log(TAG, "OPEN")
                        //相机打开后将预览确定的大小传递出去（此时还没有回调真正的预览数据）
                        cameraControlListener?.confirmCameraSize(previewSize)
                    }
                    CameraState.Type.CLOSING -> {
                        log(TAG, "CLOSING")
                    }
                    CameraState.Type.CLOSED -> {
                        log(TAG, "CLOSED")
                    }
                }
            }

            cameraState.error?.let { error ->
//                CameraState.ERROR_STREAM_CONFIG
                log(TAG, "cameraState error ${error.code}")
            }
        }
    }

    private fun checkSize(activity: FragmentActivity) {
        val manager = activity.getSystemService(Context.CAMERA_SERVICE) as CameraManager

        manager.cameraIdList.forEach {
            val characteristics = manager.getCameraCharacteristics(it)
            val integer = characteristics.get(CameraCharacteristics.LENS_FACING)

            if (integer != null) {
                if ((cameraSelector == CameraSelector.DEFAULT_BACK_CAMERA && integer == CameraCharacteristics.LENS_FACING_BACK) ||
                    (cameraSelector == CameraSelector.DEFAULT_FRONT_CAMERA && integer == CameraCharacteristics.LENS_FACING_FRONT)
                ) {
                    log(TAG, "SENSOR_ORIENTATION ${characteristics.get(CameraCharacteristics.SENSOR_ORIENTATION)}")
                    log(TAG, "闪光灯支持 ${characteristics.get(CameraCharacteristics.FLASH_INFO_AVAILABLE)}")

                    val map = characteristics.get(CameraCharacteristics.SCALER_STREAM_CONFIGURATION_MAP)
                        ?: throw RuntimeException("Cannot get available preview/video sizes")

//                    ImageReader：常用来拍照或接收 YUV 数据。
//                    MediaRecorder：常用来录制视频。
//                    MediaCodec：常用来录制视频。
//                    SurfaceHolder：常用来显示预览画面。
//                    SurfaceTexture：常用来显示预览画面。
                    val outputSizes = map.getOutputSizes(MediaCodec::class.java)
                    outputSizes.forEach { size ->
                        log(TAG, "outputSizes $size - ratio ${size.width.toFloat() / size.height}")
                    }

                    return@forEach
                }
            }
        }
    }

    /**
     * 使用相机功能的拍照，最终照片分辨率不定，但会保持横纵比
     */
    fun takePicture() {
        imageCapture?.takePicture(cameraExecutor, object : ImageCapture.OnImageCapturedCallback() {
            @SuppressLint("RestrictedApi")
            override fun onCaptureSuccess(image: ImageProxy) {
                log(TAG, "onCaptureSuccess format ${image.format} rotation ${image.imageInfo.rotationDegrees} cropRect ${image.cropRect}")

                if (image.format == ImageFormat.JPEG) {
                    val byteBuffer = image.planes[0].buffer
                    val bytes = ByteArray(byteBuffer.capacity())
                    byteBuffer.rewind()
                    byteBuffer.get(bytes)

                    val bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.size)
                    val picture = SavePicture.Picture(bitmap).apply {
                        exif = Exif.createFromInputStream(ByteArrayInputStream(bytes))
                        rotation = image.imageInfo.rotationDegrees
                        horizontalMirror = cameraSelector == CameraSelector.DEFAULT_FRONT_CAMERA
                    }

                    cameraControlListener?.imageAvailable(picture)

                    byteBuffer.clear()
                }

                image.close()
                log(TAG, "onCaptureSuccess X")
            }
        })
    }

    fun onDestroy() {
        cameraExecutor.shutdown()
        activity = null
        cameraControlListener = null
        log(TAG, "destroy X")
    }
}