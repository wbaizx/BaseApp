package com.video.home

import android.annotation.SuppressLint
import android.graphics.Bitmap
import android.graphics.Matrix
import android.os.Handler
import android.os.Looper
import androidx.camera.core.impl.utils.Exif
import androidx.exifinterface.media.ExifInterface
import com.base.common.util.AndroidUtil.showToast
import com.base.common.util.ImageUtil
import com.base.common.util.log
import java.util.*
import java.util.concurrent.ArrayBlockingQueue

class SavePicture : Thread() {
    companion object {
        private const val TAG = "SavePicture"
    }

    private val mMainHandler = Handler(Looper.getMainLooper())

    private val queue = ArrayBlockingQueue<Picture>(5)

    fun putData(picture: Picture) {
        log(TAG, "putData bytes")
        queue.offer(picture)
    }

    @SuppressLint("RestrictedApi")
    override fun run() {
        super.run()

        try {
            while (!currentThread().isInterrupted) {
                val picture = queue.take()

                log(TAG, "run save")

                val saveBmp = flipBitmap(picture)

                val file = ImageUtil.savePicture(saveBmp, "" + UUID.randomUUID() + ".jpg")

                saveBmp.recycle()
                picture.data.recycle()

                log(TAG, "begin save exif")
                //使用cameraX提供的Exif来操作exif信息，文件写入后会自动携带宽高等exif信息
                val exif = Exif.createFromFile(file)
                //将旧的exif信息覆盖到新的中（这个方法会排除宽高覆盖）
                picture.exif?.copyToCroppedImage(exif)
                //将旋转信息手动重置，因为我们是对bitmap进行了旋转和镜象
                exif.orientation = ExifInterface.ORIENTATION_NORMAL
                exif.save()

                log(TAG, "exif.orientation ${exif.orientation} exif.width ${exif.width} exif.height ${exif.height}")

                if (ImageUtil.updateGallery(file, exif.width, exif.height)) {
                    mMainHandler.post {
                        showToast(null, "拍照成功")
                    }
                }
                log(TAG, "run save x")
            }
        } catch (e: InterruptedException) {
            log(TAG, "InterruptedException")
        }
        queue.clear()
        log(TAG, "SavePictureThread close")
    }

    private fun flipBitmap(picture: Picture): Bitmap {
        val m = Matrix()

        m.postRotate(picture.rotation.toFloat()) //旋转
        log(TAG, "flipRotate ${picture.rotation}")

        if (picture.horizontalMirror) {
            log(TAG, "flipHorizontally")
            m.postScale(-1f, 1f) //镜像水平翻转
        }
        if (picture.verticalMirror) {
            log(TAG, "flipVertically")
            m.postScale(1f, -1f) //镜像垂直翻转
        }
        return Bitmap.createBitmap(picture.data, 0, 0, picture.data.width, picture.data.height, m, true)
    }

    data class Picture(val data: Bitmap) {
        var exif: Exif? = null
        var rotation = 0
        var horizontalMirror = false
        var verticalMirror = false
    }
}