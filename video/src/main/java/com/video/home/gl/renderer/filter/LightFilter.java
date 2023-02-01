package com.video.home.gl.renderer.filter;

import com.video.home.gl.GLHelper;

public class LightFilter extends BaseFilter{
    public LightFilter(FilterType type) {
        super(type);
    }

    @Override
    public void init() {
        program = GLHelper.compileAndLink("fbo/fbo_v_shader.glsl", "fbo/fbo_f_light.glsl");
    }

    @Override
    public void useFilter() {

    }
}