package ru.sibsutis.opengl_es.entity;

import android.opengl.GLSurfaceView;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;
import javax.microedition.khronos.opengles.GL11;

public class Cube implements GLSurfaceView.Renderer {

    private FloatBuffer mFVertexBuffer;
    private ByteBuffer mColorBuffer;
    private ByteBuffer mTfan1;
    private ByteBuffer mTfan2;

    private boolean mTranslucentBackground;
    private Cube mCube;
    private float mTransY;
    private float mAngle;

    public Cube() {

        float[] vertices = {
                -1.0f, 1.0f, 1.0f,
                1.0f, 1.0f, 1.0f,
                1.0f, -1.0f, 1.0f,
                -1.0f, -1.0f, 1.0f,

                -1.0f, 1.0f, -1.0f,
                1.0f, 1.0f, -1.0f,
                1.0f, -1.0f, -1.0f,
                -1.0f, -1.0f, -1.0f
        };

        byte maxColor = (byte) 255;

        byte[] colors = {
                maxColor, maxColor, 0, maxColor,
                0, maxColor, maxColor, maxColor,
                0, 0, 0, maxColor,
                maxColor, 0, maxColor, maxColor,

                maxColor, 0, 0, maxColor,
                0, maxColor, 0, maxColor,
                0, 0, maxColor, maxColor,
                0, 0, 0, maxColor
        };

        byte[] tfan1 = {
                1, 0, 3,
                1, 3, 2,
                1, 2, 6,
                1, 6, 5,
                1, 5, 4,
                1, 4, 0
        };

        byte[] tfan2 = {
                7, 4, 5,
                7, 5, 6,
                7, 6, 2,
                7, 2, 3,
                7, 3, 0,
                7, 0, 4
        };

        ByteBuffer vbb = ByteBuffer.allocateDirect(vertices.length * 4);
        vbb.order(ByteOrder.nativeOrder());
        mFVertexBuffer = vbb.asFloatBuffer();
        mFVertexBuffer.put(vertices);
        mFVertexBuffer.position(0);

        mColorBuffer = ByteBuffer.allocateDirect(colors.length);
        mColorBuffer.put(colors);
        mColorBuffer.position(0);

        mTfan1 = ByteBuffer.allocateDirect(tfan1.length);
        mTfan1.put(tfan1);
        mTfan1.position(0);

        mTfan2 = ByteBuffer.allocateDirect(tfan2.length);
        mTfan2.put(tfan2);
        mTfan2.position(0);

    }

    public void draw(GL10 gl) {
        gl.glVertexPointer(3, GL11.GL_FLOAT, 0, mFVertexBuffer);
        gl.glColorPointer(4, GL11.GL_UNSIGNED_BYTE, 0, mColorBuffer);

        gl.glDrawElements(gl.GL_TRIANGLE_FAN, 6 * 3, gl.GL_UNSIGNED_BYTE, mTfan1);
        gl.glDrawElements(gl.GL_TRIANGLE_FAN, 6 * 3, gl.GL_UNSIGNED_BYTE, mTfan2);
    }

    @Override
    public void onSurfaceCreated(GL10 gl, EGLConfig config) {
        gl.glDisable(GL11.GL_DITHER);

        gl.glHint(GL11.GL_PERSPECTIVE_CORRECTION_HINT, GL11.GL_FASTEST);

        if (mTranslucentBackground) {
            gl.glClearColor(1, 0, 0, 0);
        } else {
            gl.glClearColor(1, 1, 1, 1);
        }

        gl.glEnable(GL11.GL_CULL_FACE);
        gl.glShadeModel(GL11.GL_SMOOTH);
        gl.glEnable(GL11.GL_DEPTH_TEST);
    }

    @Override
    public void onSurfaceChanged(GL10 gl, int width, int height) {
        gl.glViewport(0, 0, width, height);

        float aspectRatio;
        float zNear = .1f;
        float zFar = 1000;
        float fieldOfView = 30.0f / 57.3f;
        float size;

        gl.glEnable(GL10.GL_NORMALIZE);

        aspectRatio = (float) width / (float) height;

        gl.glMatrixMode(GL10.GL_PROJECTION);

        size = zNear * (float) (Math.tan((double) (fieldOfView / 2.0f)));

        gl.glFrustumf(-size, size, -size / aspectRatio,
                size / aspectRatio, zNear, zFar);

        gl.glMatrixMode(GL10.GL_MODELVIEW);
    }

    @Override
    public void onDrawFrame(GL10 gl) {
        gl.glClearColor(1.0f, 1.0f, 1.0f, 1.0f);
        gl.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);

        gl.glMatrixMode(GL11.GL_MODELVIEW);
        gl.glLoadIdentity();
        gl.glTranslatef(0.0f, (float) Math.sin(mTransY), -7.0f);

        gl.glRotatef(mAngle, 0.0f, 1.0f, 0.0f);
        gl.glRotatef(mAngle, 1.0f, 0.0f, 0.0f);

        gl.glEnableClientState(GL11.GL_VERTEX_ARRAY);
        gl.glEnableClientState(GL11.GL_COLOR_ARRAY);

        draw(gl);

        mTransY += .075f;
        mAngle += .4;
    }
}
