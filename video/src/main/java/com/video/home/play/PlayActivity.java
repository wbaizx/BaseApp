package com.video.home.play;

import android.graphics.SurfaceTexture;
import android.util.Size;
import android.view.View;

import androidx.annotation.NonNull;

import com.alibaba.android.arouter.facade.annotation.Autowired;
import com.base.common.base.activity.BaseBindContentActivity;
import com.gyf.immersionbar.BarHide;
import com.gyf.immersionbar.ImmersionBar;
import com.video.R;
import com.video.databinding.ActivityPlayBinding;
import com.video.home.gl.egl.GLSurfaceListener;

public class PlayActivity extends BaseBindContentActivity<ActivityPlayBinding> implements GLSurfaceListener, PlayListener {
    @Autowired
    String path;

    private PlayManager playManager;

    @Override
    protected int getContentView() {
        return R.layout.activity_play;
    }

    @Override
    protected void viewBind(@NonNull ActivityPlayBinding binding) {
    }

    @Override
    protected void setImmersionBar() {
        ImmersionBar.with(this).hideBar(BarHide.FLAG_HIDE_BAR).init();
    }

    @Override
    protected void initView() {
        binding.eglSurfaceView.setGlSurfaceListener(this);

        playManager = new PlayManager(this);

        binding.eglSurfaceView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (playManager.isReady()) {
                    playManager.play();
                    binding.playSwitch.setVisibility(View.GONE);
                } else {
                    playManager.pause();
                    binding.playSwitch.setVisibility(View.VISIBLE);
                }
            }
        });
    }

    @Override
    public void onGLSurfaceCreated(SurfaceTexture surfaceTexture) {
        playManager.setSurfaceTexture(surfaceTexture);
        playManager.init(path);
    }

    @Override
    public void confirmPlaySize(Size playSize) {
        binding.eglSurfaceView.confirmReallySize(playSize);
    }

    @Override
    public void playEnd() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                binding.playSwitch.setVisibility(View.VISIBLE);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        playManager.onResume();
    }

    @Override
    protected void onPause() {
        binding.playSwitch.setVisibility(View.VISIBLE);
        playManager.onPause();
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        playManager.onDestroy();
        binding.eglSurfaceView.onDestroy();
        super.onDestroy();
    }
}
