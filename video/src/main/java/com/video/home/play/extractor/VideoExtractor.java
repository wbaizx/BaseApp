package com.video.home.play.extractor;

import com.video.home.MimeType;

public class VideoExtractor extends Extractor {
    @Override
    protected boolean chooseMime(String mime) {
        return MimeType.H264.equals(mime) || MimeType.H265.equals(mime);
    }
}
