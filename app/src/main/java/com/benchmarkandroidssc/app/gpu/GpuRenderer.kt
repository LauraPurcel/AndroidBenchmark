package com.benchmarkandroidscc.app.ui.gpu

import PHONG_FRAGMENT_SHADER
import PHONG_VERTEX_SHADER
import android.content.Context
import android.graphics.BitmapFactory
import android.opengl.GLES30
import android.opengl.GLSurfaceView
import android.opengl.GLUtils
import android.opengl.Matrix
import com.benchmarkandroidssc.app.gpu.MeshCube
import kotlin.system.measureTimeMillis
import javax.microedition.khronos.egl.EGLConfig
import javax.microedition.khronos.opengles.GL10
class GpuRenderer(
    private val ctx: Context,
    private val textureResId: Int
) : GLSurfaceView.Renderer {

    private var sceneProgram = 0
    private var shadowProgram = 0
    private var shadowFramebuffer = 0
    private var shadowDepthTexture = 0
    private val SHADOW_RES = 1024

    private var textureId = 0

    private val modelMatrix = FloatArray(16)
    private val viewMatrix = FloatArray(16)
    private val projectionMatrix = FloatArray(16)

    private val lightViewMatrix = FloatArray(16)
    private val lightProjectionMatrix = FloatArray(16)
    private val lightSpaceMatrix = FloatArray(16)

    private var width = 1
    private var height = 1
    private var frameCount = 0
    private var runningTest = false
    private var objectCount = 50
    private var lastFrameTimeMs = 0.0

    override fun onSurfaceCreated(gl: GL10?, config: EGLConfig?) {
        GLES30.glClearColor(0.1f, 0.1f, 0.1f, 1f)
        GLES30.glEnable(GLES30.GL_DEPTH_TEST)

        sceneProgram = ShaderUtils.createProgram(PHONG_VERTEX_SHADER, PHONG_FRAGMENT_SHADER)
        shadowProgram = ShaderUtils.createProgram(DEPTH_VERTEX_SHADER, DEPTH_FRAGMENT_SHADER)

        textureId = loadTexture(ctx, textureResId)

        initShadowBuffer()
    }

    private fun initShadowBuffer() {
        val fboId = IntArray(1)
        GLES30.glGenFramebuffers(1, fboId, 0)
        shadowFramebuffer = fboId[0]

        val texId = IntArray(1)
        GLES30.glGenTextures(1, texId, 0)
        shadowDepthTexture = texId[0]

        GLES30.glBindTexture(GLES30.GL_TEXTURE_2D, shadowDepthTexture)
        GLES30.glTexImage2D(GLES30.GL_TEXTURE_2D, 0, GLES30.GL_DEPTH_COMPONENT24,
            SHADOW_RES, SHADOW_RES, 0, GLES30.GL_DEPTH_COMPONENT, GLES30.GL_UNSIGNED_INT, null)

        GLES30.glTexParameteri(GLES30.GL_TEXTURE_2D, GLES30.GL_TEXTURE_MIN_FILTER, GLES30.GL_NEAREST)
        GLES30.glTexParameteri(GLES30.GL_TEXTURE_2D, GLES30.GL_TEXTURE_MAG_FILTER, GLES30.GL_NEAREST)
        GLES30.glTexParameteri(GLES30.GL_TEXTURE_2D, GLES30.GL_TEXTURE_WRAP_S, GLES30.GL_CLAMP_TO_EDGE)
        GLES30.glTexParameteri(GLES30.GL_TEXTURE_2D, GLES30.GL_TEXTURE_WRAP_T, GLES30.GL_CLAMP_TO_EDGE)

        GLES30.glBindFramebuffer(GLES30.GL_FRAMEBUFFER, shadowFramebuffer)
        GLES30.glFramebufferTexture2D(GLES30.GL_FRAMEBUFFER, GLES30.GL_DEPTH_ATTACHMENT,
            GLES30.GL_TEXTURE_2D, shadowDepthTexture, 0)

        GLES30.glBindFramebuffer(GLES30.GL_FRAMEBUFFER, 0)
    }

    override fun onDrawFrame(gl: GL10?) {
        if (!runningTest) return

        val timeMs = measureTimeMillis {
            frameCount++

            GLES30.glViewport(0, 0, SHADOW_RES, SHADOW_RES)
            GLES30.glBindFramebuffer(GLES30.GL_FRAMEBUFFER, shadowFramebuffer)
            GLES30.glClear(GLES30.GL_DEPTH_BUFFER_BIT)

            GLES30.glUseProgram(shadowProgram)

            Matrix.setLookAtM(lightViewMatrix, 0, 5f, 8f, 5f, 0f, 0f, 0f, 0f, 1f, 0f)
            Matrix.orthoM(lightProjectionMatrix, 0, -10f, 10f, -10f, 10f, 1f, 20f)
            Matrix.multiplyMM(lightSpaceMatrix, 0, lightProjectionMatrix, 0, lightViewMatrix, 0)

            renderObjects(shadowProgram, "uLightMVP", lightSpaceMatrix)

            GLES30.glBindFramebuffer(GLES30.GL_FRAMEBUFFER, 0)
            GLES30.glViewport(0, 0, width, height)
            GLES30.glClear(GLES30.GL_COLOR_BUFFER_BIT or GLES30.GL_DEPTH_BUFFER_BIT)

            GLES30.glUseProgram(sceneProgram)

            Matrix.setLookAtM(viewMatrix, 0, 0f, 5f, 10f, 0f, 0f, 0f, 0f, 1f, 0f)

            val uView = GLES30.glGetUniformLocation(sceneProgram, "uView")
            val uProj = GLES30.glGetUniformLocation(sceneProgram, "uProj")
            GLES30.glUniformMatrix4fv(uProj, 1, false, projectionMatrix, 0)
            val uLightSpace = GLES30.glGetUniformLocation(sceneProgram, "uLightSpaceMatrix")
            val uTexture = GLES30.glGetUniformLocation(sceneProgram, "uTexture")
            val uShadowMap = GLES30.glGetUniformLocation(sceneProgram, "uShadowMap")

            GLES30.glUniformMatrix4fv(uView, 1, false, viewMatrix, 0)
            GLES30.glUniformMatrix4fv(uProj, 1, false, projectionMatrix, 0)
            GLES30.glUniformMatrix4fv(uLightSpace, 1, false, lightSpaceMatrix, 0)

            GLES30.glActiveTexture(GLES30.GL_TEXTURE0)
            GLES30.glBindTexture(GLES30.GL_TEXTURE_2D, textureId)
            GLES30.glUniform1i(uTexture, 0)

            GLES30.glActiveTexture(GLES30.GL_TEXTURE1)
            GLES30.glBindTexture(GLES30.GL_TEXTURE_2D, shadowDepthTexture)
            GLES30.glUniform1i(uShadowMap, 1)

            renderObjects(sceneProgram, "uModel", null)
        }
        lastFrameTimeMs = timeMs.toDouble()
    }

    private fun renderObjects(currentProgram: Int, modelTag: String, lightSpace: FloatArray?) {
        val uModelLoc = GLES30.glGetUniformLocation(currentProgram, modelTag)
        MeshCube.bind(currentProgram)

        for (i in 0 until objectCount) {
            Matrix.setIdentityM(modelMatrix, 0)
            Matrix.translateM(modelMatrix, 0, (i % 10 - 5) * 1.5f, 0f, (i / 10 - 5) * 1.5f)
            Matrix.rotateM(modelMatrix, 0, frameCount * 0.5f, 0f, 1f, 0f)

            if (lightSpace != null) {
                val mvp = FloatArray(16)
                Matrix.multiplyMM(mvp, 0, lightSpace, 0, modelMatrix, 0)
                GLES30.glUniformMatrix4fv(uModelLoc, 1, false, mvp, 0)
            } else {

                GLES30.glUniformMatrix4fv(uModelLoc, 1, false, modelMatrix, 0)
            }
            MeshCube.drawRaw()
        }
        MeshCube.unbind(currentProgram)
    }

    private fun loadTexture(context: Context, resId: Int): Int {
        val textureIds = IntArray(1)
        GLES30.glGenTextures(1, textureIds, 0)

        val options = BitmapFactory.Options().apply { inScaled = false }
        val bitmap = BitmapFactory.decodeResource(context.resources, resId, options)

        GLES30.glBindTexture(GLES30.GL_TEXTURE_2D, textureIds[0])

        GLES30.glTexParameteri(GLES30.GL_TEXTURE_2D, GLES30.GL_TEXTURE_MIN_FILTER, GLES30.GL_LINEAR_MIPMAP_LINEAR)
        GLES30.glTexParameteri(GLES30.GL_TEXTURE_2D, GLES30.GL_TEXTURE_MAG_FILTER, GLES30.GL_LINEAR)

        GLUtils.texImage2D(GLES30.GL_TEXTURE_2D, 0, bitmap, 0)
        GLES30.glGenerateMipmap(GLES30.GL_TEXTURE_2D)

        bitmap.recycle()
        return textureIds[0]
    }

    override fun onSurfaceChanged(gl: GL10?, w: Int, h: Int) {
        width = w
        height = h
        GLES30.glViewport(0, 0, w, h)
        val ratio = w.toFloat() / h
        Matrix.perspectiveM(projectionMatrix, 0, 45f, ratio, 0.1f, 100f)
    }


    fun startTest() {
        runningTest = true
        frameCount = 0
    }

    fun setObjectCount(count: Int) {
        objectCount = count.coerceAtLeast(1)
    }

    fun getLastFrameTimeMs(): Double = lastFrameTimeMs
}