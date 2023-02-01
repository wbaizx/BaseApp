package com.video.home.record;

import android.view.Surface;

public interface RecordListener {
    void onEncoderSurfaceCreated(Surface surface);

    void onEncoderSurfaceDestroy();
}
