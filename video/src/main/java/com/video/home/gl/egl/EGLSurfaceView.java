package com.video.home.gl.egl;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Size;
import android.view.Surface;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import com.video.home.camera.CameraControlListener;
import com.video.home.gl.renderer.filter.FilterType;

public class EGLSurfaceView extends SurfaceView implements SurfaceHolder.Callback {
    private final GLThread glThread;

    public EGLSurfaceView(Context context, AttributeSet attrs) {
        super(context, attrs);
        getHolder().addCallback(this);
        glThread = new GLThread();
        glThread.start();
    }

    public void setGlSurfaceListener(GLSurfaceListener glSurfaceListener) {
        glThread.setGlSurfaceListener(glSurfaceListener);
    }

    public void setCameraControlListener(CameraControlListener cameraControlListener) {
        glThread.setCameraControlListener(cameraControlListener);
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        glThread.surfaceCreated(holder.getSurface());
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        glThread.surfaceChanged(width, height);
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        glThread.surfaceDestroyed();
    }

    /**
     * 此方法涉及fbo纹理配置更新，相机每次打开或状态变更都需要回调，且必须在数据来之前回调
     */
    public void confirmReallySize(Size reallySize) {
        glThread.confirmReallySize(reallySize);
    }

    public void takePicture() {
        glThread.queueEvent(new Runnable() {
            @Override
            public void run() {
                glThread.takePicture();
            }
        });
    }

    public void onDestroy() {
        glThread.onDestroy();
    }

    public void switchFilterType(FilterType type) {
        glThread.queueEvent(new Runnable() {
            @Override
            public void run() {
                glThread.switchFilterType(type);
            }
        });
    }

    public void onEncoderSurfaceCreated(Surface surface) {
        glThread.onEncoderSurfaceCreated(surface);
    }

    public void onEncoderSurfaceDestroy() {
        glThread.queueEvent(new Runnable() {
            @Override
            public void run() {
                glThread.onEncoderSurfaceDestroy();
            }
        });
    }
}
