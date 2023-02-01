package com.video.home.camera

import android.util.Size
import com.video.home.SavePicture

interface CameraControlListener {
    fun confirmCameraSize(cameraSize: Size)

    fun imageAvailable(picture: SavePicture.Picture)
}