package com.benchmarkandroidscc.app.ui.gpu

import android.content.Context
import android.graphics.BitmapFactory
import android.opengl.GLES20
import android.opengl.GLUtils

object TextureUtils {

    fun loadTexture(ctx: Context, resourceId: Int): Int {
        val texture = IntArray(1)
        GLES20.glGenTextures(1, texture, 0)

        val bmp = BitmapFactory.decodeResource(ctx.resources, resourceId)

        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, texture[0])
        GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MIN_FILTER, GLES20.GL_LINEAR_MIPMAP_LINEAR)
        GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MAG_FILTER, GLES20.GL_LINEAR)

        GLUtils.texImage2D(GLES20.GL_TEXTURE_2D, 0, bmp, 0)
        GLES20.glGenerateMipmap(GLES20.GL_TEXTURE_2D)

        bmp.recycle()
        return texture[0]
    }
}
