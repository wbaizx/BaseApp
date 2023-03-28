package com.video.home.record;

import android.media.MediaCodec;
import android.media.MediaFormat;
import android.media.MediaMuxer;
import android.os.ConditionVariable;
import android.text.TextUtils;

import com.base.common.util.AndroidUtilKt;
import com.base.common.util.FileUtilKt;
import com.base.common.util.LogUtilKt;

import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.UUID;

public class MuxerManager {
    private static final String TAG = "MuxerManager";

    private final String path = FileUtilKt.getDiskFilePath("VIDEO") + File.separator;
    private String thisPath;

    public static final int STATUS_READY = 0;
    public static final int STATUS_START = 1;
    public static final int STATUS_INIT = 2;
    public static final int STATUS_SNAP = 3;
    private int status = STATUS_READY;

    private MediaMuxer mediaMuxer;

    private final ConditionVariable conditionVariable = new ConditionVariable();

    private int audioTrackIndex = -1;
    private int videoTrackIndex = -1;

    public int getStatus() {
        return status;
    }

    public boolean init() {
        conditionVariable.close();

        boolean initSuccess = false;
        if (status == STATUS_READY) {
            try {
                thisPath = path + UUID.randomUUID() + ".mp4";
                mediaMuxer = new MediaMuxer(thisPath, MediaMuxer.OutputFormat.MUXER_OUTPUT_MPEG_4);
                status = STATUS_INIT;
                initSuccess = true;
                LogUtilKt.debugLog(TAG, "init");
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else if (status == STATUS_INIT) {
            initSuccess = true;
        }
        return initSuccess;
    }

    public void addVideoTrack(MediaFormat videoFormat) {
        synchronized (this) {
            if (status == STATUS_INIT) {
                LogUtilKt.debugLog(TAG, "addVideoTrack");
                videoTrackIndex = mediaMuxer.addTrack(videoFormat);
                if (audioTrackIndex != -1) {
                    start();
                }
            }
        }
    }

    public void addAudioTrack(MediaFormat audioFormat) {
        synchronized (this) {
            if (status == STATUS_INIT) {
                LogUtilKt.debugLog(TAG, "addAudioTrack");
                audioTrackIndex = mediaMuxer.addTrack(audioFormat);
                if (videoTrackIndex != -1) {
                    start();
                }
            }
        }
    }

    private void start() {
        mediaMuxer.start();
        status = STATUS_START;

        conditionVariable.open();
        LogUtilKt.debugLog(TAG, "start");
    }

    public void writeVideoSampleData(ByteBuffer buffer, MediaCodec.BufferInfo info) {
        LogUtilKt.debugLog(TAG, "writeVideo");
        if (status == STATUS_START || status == STATUS_INIT) {
            conditionVariable.block();

            LogUtilKt.debugLog(TAG, "writeVideoSampleData " + info.presentationTimeUs);
            LogUtilKt.debugLog(TAG, "writeVideoSampleData " + info.flags);
            mediaMuxer.writeSampleData(videoTrackIndex, buffer, info);
        }
    }

    public void writeAudioSampleData(ByteBuffer buffer, MediaCodec.BufferInfo info) {
        LogUtilKt.debugLog(TAG, "writeAudio");
        if (status == STATUS_START || status == STATUS_INIT) {
            conditionVariable.block();

            LogUtilKt.debugLog(TAG, "writeAudioSampleData " + info.presentationTimeUs);
            mediaMuxer.writeSampleData(audioTrackIndex, buffer, info);
        }
    }

    public void stop() {
        if (status == STATUS_START) {
            status = STATUS_SNAP;
            try {
                mediaMuxer.stop();
                mediaMuxer.release();
                LogUtilKt.debugLog(TAG, "stop");

                AndroidUtilKt.showToast("录制成功 " + thisPath, null);

            } catch (Exception e) {
                if (!TextUtils.isEmpty(thisPath)) {
                    File file = new File(thisPath);
                    file.delete();
                }

                AndroidUtilKt.showToast("录制失败", null);

            } finally {
                status = STATUS_READY;
                audioTrackIndex = -1;
                videoTrackIndex = -1;
            }
        }
    }
}
