package ru.sibsutis.phong;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import android.annotation.SuppressLint;
import android.content.Context;
import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import android.opengl.Matrix;

import static android.opengl.GLES20.GL_FRAGMENT_SHADER;
import static android.opengl.GLES20.GL_VERTEX_SHADER;

public class sphereRenderer implements GLSurfaceView.Renderer {

    @SuppressLint("StaticFieldLeak")
    private static Context context;
    public volatile float mXAngle;
    public volatile float mYAngle;
    public volatile float mZoom;

    private final float[] mVMatrix = new float[16];
    private final float[] mProjMatrix = new float[16];
    private final float[] mNormalMatrix = new float[16];
    private final float[] mMVPMatrix = new float[16];
    private final float[] mRotationMatrixX = new float[16];
    private final float[] mRotationMatrixY = new float[16];
    private final float[] mPVMatrix = new float[16];
    private final float[] mTempMatrix = new float[16];

    private Sphere[] sphere;

    public sphereRenderer(Context context) {

        sphereRenderer.context = context;
    }

    @Override
    public void onSurfaceCreated(GL10 unused, EGLConfig cfg) {
        GLES20.glClearColor(1.0f, 1.0f, 1.0f, 0.0f);
        GLES20.glEnable(GL10.GL_DEPTH_TEST);

        mZoom = -6f;
        sphere = new Sphere[1];
        for (int i = 0; i < 1; i++)
            sphere[i] = new Sphere(10, 30);
    }

    @Override
    public void onDrawFrame(GL10 unused) {
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT | GLES20.GL_DEPTH_BUFFER_BIT);

        Matrix.setLookAtM(mVMatrix, 0, 0, 0, mZoom, 0f, 0f, 0f, 0f, 1.0f, 0.0f);
        Matrix.multiplyMM(mPVMatrix, 0, mProjMatrix, 0, mVMatrix, 0);
        Matrix.setRotateM(mRotationMatrixX, 0, mXAngle, 0, 1.0f, 0f);
        Matrix.setRotateM(mRotationMatrixY, 0, mYAngle, 1.0f, 0, 0);
        Matrix.multiplyMM(mTempMatrix, 0, mPVMatrix, 0, mRotationMatrixX, 0);
        Matrix.multiplyMM(mMVPMatrix, 0, mTempMatrix, 0, mRotationMatrixY, 0);

        Matrix.multiplyMM(mTempMatrix, 0, mVMatrix, 0, mRotationMatrixX, 0);

        Matrix.transposeM(mNormalMatrix, 0, mTempMatrix, 0);
        drawSphere();
    }

    @Override
    public void onSurfaceChanged(GL10 unused, int width, int height) {
        GLES20.glViewport(0, 0, width, height);
        float ratio = (float) width / height;
        Matrix.frustumM(mProjMatrix, 0, -ratio, ratio, -1, 1, 1, 20);
    }

    public static int createShaderProgram() {
        int vertexShaderId = ShaderUtils.createShader(context, GL_VERTEX_SHADER, R.raw.vertex_shader);
        int fragmentShaderId = ShaderUtils.createShader(context, GL_FRAGMENT_SHADER, R.raw.fragment_shader);
        return ShaderUtils.createProgram(vertexShaderId, fragmentShaderId);
    }

    private void drawSphere() {
        float[] scalerMatrix = new float[16];
        float[] finalMVPMatrix = new float[16];
        float[] tempMatrix = new float[16];

        Matrix.setIdentityM(scalerMatrix, 0);
        Matrix.scaleM(scalerMatrix, 0, 3f, 3f, 0.5f);
        Matrix.multiplyMM(tempMatrix, 0, mMVPMatrix, 0, scalerMatrix, 0);

        Matrix.translateM(finalMVPMatrix, 0, tempMatrix, 0, 0f, 0f, 3f);
        sphere[0].draw(finalMVPMatrix, mNormalMatrix, mVMatrix);
    }
}
