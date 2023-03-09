package com.video.home.gl.renderer.filter;

import com.base.common.util.LogUtilKt;
import com.video.home.gl.GLHelper;

public class NoneFilter extends BaseFilter{
    private static final String TAG = "NoneFilter";

    public NoneFilter(FilterType type) {
        super(type);
    }

    @Override
    public void init() {
        LogUtilKt.debugLog(TAG, "init");
        program = GLHelper.compileAndLink("fbo/fbo_v_shader.glsl", "fbo/fbo_f_none.glsl");
    }

    @Override
    public void useFilter() {

    }
}
