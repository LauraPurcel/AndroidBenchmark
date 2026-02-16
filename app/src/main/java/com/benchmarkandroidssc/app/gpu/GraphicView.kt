package com.benchmarkandroidscc.app.ui.gpu

import android.content.Context
import android.opengl.GLSurfaceView
import android.util.AttributeSet
import com.benchmarkandroidssc.app.R

class GraphicView(context: Context, attrs: AttributeSet?) : GLSurfaceView(context, attrs) {

    private val renderer3D: GpuRenderer

    init {
        setEGLContextClientVersion(3)
        renderer3D = GpuRenderer(context, R.drawable.texture)
        setRenderer(renderer3D)
        renderMode = GLSurfaceView.RENDERMODE_CONTINUOUSLY
    }

    fun startTest(objects: Int) {
        renderer3D.setObjectCount(objects)
        renderer3D.startTest()
    }

    fun getFrameTimeMs(): Double {
        return renderer3D.getLastFrameTimeMs()
    }

}
