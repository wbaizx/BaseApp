package com.video.home.play.extractor;

import com.video.home.MimeType;

public class AudioExtractor extends Extractor {
    @Override
    protected boolean chooseMime(String mime) {
        return MimeType.AAC.equals(mime);
    }
}
