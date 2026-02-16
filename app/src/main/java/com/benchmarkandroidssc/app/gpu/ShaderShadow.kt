package com.benchmarkandroidscc.app.ui.gpu

const val DEPTH_VERTEX_SHADER = """
attribute vec3 aPos;
uniform mat4 uLightMVP;
void main() {
    gl_Position = uLightMVP * vec4(aPos,1.0);
}
"""

const val DEPTH_FRAGMENT_SHADER = """
precision mediump float;
void main() { }
"""
