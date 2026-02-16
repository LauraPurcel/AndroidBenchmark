package com.benchmarkandroidssc.app.gpu

import android.opengl.GLES30
import java.nio.ByteBuffer
import java.nio.ByteOrder
import java.nio.FloatBuffer

object MeshCube {

    private val verts = floatArrayOf(
        // --- FRONT ---
        -0.5f, -0.5f, 0.5f,   0f,0f,1f,  0f,0f,
        0.5f, -0.5f, 0.5f,   0f,0f,1f,  1f,0f,
        0.5f,  0.5f, 0.5f,   0f,0f,1f,  1f,1f,
        -0.5f, -0.5f, 0.5f,   0f,0f,1f,  0f,0f,
        0.5f,  0.5f, 0.5f,   0f,0f,1f,  1f,1f,
        -0.5f,  0.5f, 0.5f,   0f,0f,1f,  0f,1f,

        // --- BACK ---
        -0.5f, -0.5f, -0.5f,   0f,0f,-1f,  0f,0f,
        0.5f, -0.5f, -0.5f,   0f,0f,-1f,  1f,0f,
        0.5f,  0.5f, -0.5f,   0f,0f,-1f,  1f,1f,
        -0.5f, -0.5f, -0.5f,   0f,0f,-1f,  0f,0f,
        0.5f,  0.5f, -0.5f,   0f,0f,-1f,  1f,1f,
        -0.5f,  0.5f, -0.5f,   0f,0f,-1f,  0f,1f,

        // --- LEFT ---
        -0.5f,  0.5f,  0.5f,   -1f,0f,0f,  1f,0f,
        -0.5f,  0.5f, -0.5f,   -1f,0f,0f,  1f,1f,
        -0.5f, -0.5f, -0.5f,   -1f,0f,0f,  0f,1f,
        -0.5f, -0.5f, -0.5f,   -1f,0f,0f,  0f,1f,
        -0.5f, -0.5f,  0.5f,   -1f,0f,0f,  0f,0f,
        -0.5f,  0.5f,  0.5f,   -1f,0f,0f,  1f,0f,

        // --- RIGHT ---
        0.5f,  0.5f,  0.5f,    1f,0f,0f,  1f,0f,
        0.5f,  0.5f, -0.5f,    1f,0f,0f,  1f,1f,
        0.5f, -0.5f, -0.5f,    1f,0f,0f,  0f,1f,
        0.5f, -0.5f, -0.5f,    1f,0f,0f,  0f,1f,
        0.5f, -0.5f,  0.5f,    1f,0f,0f,  0f,0f,
        0.5f,  0.5f,  0.5f,    1f,0f,0f,  1f,0f,

        // --- TOP ---
        -0.5f, 0.5f,  0.5f,   0f,1f,0f,  0f,1f,
        0.5f, 0.5f,  0.5f,   0f,1f,0f,  1f,1f,
        0.5f, 0.5f, -0.5f,   0f,1f,0f,  1f,0f,
        -0.5f, 0.5f,  0.5f,   0f,1f,0f,  0f,1f,
        0.5f, 0.5f, -0.5f,   0f,1f,0f,  1f,0f,
        -0.5f, 0.5f, -0.5f,   0f,1f,0f,  0f,0f,

        // --- BOTTOM ---
        -0.5f, -0.5f,  0.5f,   0f,-1f,0f,  0f,1f,
        0.5f, -0.5f,  0.5f,   0f,-1f,0f,  1f,1f,
        0.5f, -0.5f, -0.5f,   0f,-1f,0f,  1f,0f,
        -0.5f, -0.5f,  0.5f,   0f,-1f,0f,  0f,1f,
        0.5f, -0.5f, -0.5f,   0f,-1f,0f,  1f,0f,
        -0.5f, -0.5f, -0.5f,   0f,-1f,0f,  0f,0f
    )

    val VERT_COUNT = verts.size / 8
    private val buffer: FloatBuffer


    init {
        val bb = ByteBuffer.allocateDirect(verts.size * 4).order(ByteOrder.nativeOrder())
        buffer = bb.asFloatBuffer()
        buffer.put(verts)
        buffer.position(0)
    }


    fun bind(program: Int) {
        val posLoc = GLES30.glGetAttribLocation(program, "aPos")
        val normalLoc = GLES30.glGetAttribLocation(program, "aNormal")
        val uvLoc = GLES30.glGetAttribLocation(program, "aUV")

        buffer.position(0)
        GLES30.glEnableVertexAttribArray(posLoc)
        GLES30.glVertexAttribPointer(posLoc, 3, GLES30.GL_FLOAT, false, 32, buffer)

        buffer.position(3)
        GLES30.glEnableVertexAttribArray(normalLoc)
        GLES30.glVertexAttribPointer(normalLoc, 3, GLES30.GL_FLOAT, false, 32, buffer)

        buffer.position(6)
        GLES30.glEnableVertexAttribArray(uvLoc)
        GLES30.glVertexAttribPointer(uvLoc, 2, GLES30.GL_FLOAT, false, 32, buffer)
    }

    fun bindDepth(program: Int) {
        val posLoc = GLES30.glGetAttribLocation(program, "aPos")

        buffer.position(0)
        GLES30.glEnableVertexAttribArray(posLoc)
        GLES30.glVertexAttribPointer(posLoc, 3, GLES30.GL_FLOAT, false, 32, buffer)
    }

    fun drawRaw() {
        GLES30.glDrawArrays(GLES30.GL_TRIANGLES, 0, VERT_COUNT)
    }

    fun unbind(program: Int) {
        val posLoc = GLES30.glGetAttribLocation(program, "aPos")
        val normalLoc = GLES30.glGetAttribLocation(program, "aNormal")
        val uvLoc = GLES30.glGetAttribLocation(program, "aUV")

        if (posLoc >= 0) GLES30.glDisableVertexAttribArray(posLoc)
        if (normalLoc >= 0) GLES30.glDisableVertexAttribArray(normalLoc)
        if (uvLoc >= 0) GLES30.glDisableVertexAttribArray(uvLoc)
    }
}
