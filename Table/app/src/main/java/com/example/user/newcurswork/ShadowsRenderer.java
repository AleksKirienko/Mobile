package com.example.user.newcurswork;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import android.content.Context;
import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import android.opengl.Matrix;
import android.util.Log;

public class ShadowsRenderer implements GLSurfaceView.Renderer {

    private final MainActivity mShadowsActivity;

    private RenderProgram mSimpleShadowProgram;

    private RenderProgram mDepthMapProgram;

    private int mActiveProgram;

    private final float[] mMVPMatrix = new float[16];
    private final float[] mMVMatrix = new float[16];
    private final float[] mNormalMatrix = new float[16];
    private final float[] mProjectionMatrix = new float[16];
    private final float[] mViewMatrix = new float[16];
    private final float[] mModelMatrix = new float[16];

    private final float[] mLightMvpMatrix = new float[16];

    private final float[] mLightProjectionMatrix = new float[16];

    private final float[] mLightViewMatrix = new float[16];

    private final float[] mLightPosInEyeSpace = new float[16];

    private final float[] mLightPosModel = new float[]
            {-0.5f, -0.5f, 5.8f, 1.0f};
    private float[] mActualLightPosition = new float[4];

    private int mDisplayWidth;
    private int mDisplayHeight;
    private float s = 0;

    private int mShadowMapWidth;
    private int mShadowMapHeight;

    private int[] fboId;
    private int[] renderTextureId;

    private int scene_mvpMatrixUniform;
    private int scene_mvMatrixUniform;
    private int scene_normalMatrixUniform;
    private int scene_lightPosUniform;
    private int scene_shadowProjMatrixUniform;
    private int scene_textureUniform;
    private int scene_mapStepXUniform;
    private int scene_mapStepYUniform;

    private int shadow_mvpMatrixUniform;

    private int scene_positionAttribute;
    private int scene_normalAttribute;
    private int scene_colorAttribute;

    private int shadow_positionAttribute;

    private Context c;

    private Objects table;
    private Objects chair;
    private Objects comp;
    private Objects cup;
    private Objects book;
    private Objects plate;
    private Objects nizhnyayabulka;
    private Objects kartohakonchlas;
    private Objects kruzhechka;
    private Objects tel;
    private Objects screen;

    ShadowsRenderer(final MainActivity shadowsActivity, Context c) {
        mShadowsActivity = shadowsActivity;
        this.c = c;
    }

    @Override
    public void onSurfaceCreated(GL10 unused, EGLConfig config) {
        GLES20.glClearColor(1.0f, 1.0f, 1.0f, 1.0f);

        GLES20.glEnable(GLES20.GL_DEPTH_TEST);

        GLES20.glEnable(GLES20.GL_CULL_FACE);

        table = new Objects(c, new float[]{0.4f, 0.2f, 0.2f, 1.0f}, "table_ikea.obj");
        comp = new Objects(c, new float[]{0.0f, 0.0f, 1.0f, 1.0f}, "comp1.obj");
        chair = new Objects(c, new float[]{0.0f, 0.3f, 0.0f, 1.0f}, "stulotpynoogotrudovica.obj");
        book = new Objects(c, new float[]{0.4f, 0.2f, 0.3f, 1.0f}, "books.obj");
//        plate = new Objects(c, new float[]{0.4f, 0.2f, 0.3f, 1.0f}, "plate.obj");
        nizhnyayabulka = new Objects(c, new float[]{0.0f, 5f, 3f, 1.0f}, "nizhnyayabulka.obj");
//        kartohakonchlas = new Objects(c, new float[]{1f, 0.2f, 0.3f, 1.0f}, "kartohakonchlas.obj");
        kruzhechka = new Objects(c, new float[]{1f, 1f, 1f, 1.0f}, "kruzhechka.obj");
        tel = new Objects(c, new float[]{1f, 1f, 1f, 1f}, "tel.obj");
        screen = new Objects(c, new float[]{0.0f, 0.0f, 1f, 1f}, "screen.obj");

        mSimpleShadowProgram = new RenderProgram(R.raw.depth_tex_v_with_shadow, R.raw.depth_tex_f_with_simple_shadow, mShadowsActivity);
        mDepthMapProgram = new RenderProgram(R.raw.depth_tex_v_depth_map, R.raw.depth_tex_f_depth_map, mShadowsActivity);
        mActiveProgram = mSimpleShadowProgram.getProgram();
    }

    private void generateShadowFBO() {
        mShadowMapWidth = Math.round(mDisplayWidth);
        mShadowMapHeight = Math.round(mDisplayHeight);

        fboId = new int[1];
        int[] depthTextureId = new int[1];
        renderTextureId = new int[1];

        GLES20.glGenFramebuffers(1, fboId, 0);

        GLES20.glGenRenderbuffers(1, depthTextureId, 0);
        GLES20.glBindRenderbuffer(GLES20.GL_RENDERBUFFER, depthTextureId[0]);
        GLES20.glRenderbufferStorage(GLES20.GL_RENDERBUFFER, GLES20.GL_DEPTH_COMPONENT16, mShadowMapWidth, mShadowMapHeight);

        GLES20.glGenTextures(1, renderTextureId, 0);
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, renderTextureId[0]);

        GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MIN_FILTER, GLES20.GL_NEAREST);
        GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MAG_FILTER, GLES20.GL_NEAREST);
        GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_WRAP_S, GLES20.GL_CLAMP_TO_EDGE);
        GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_WRAP_T, GLES20.GL_CLAMP_TO_EDGE);

        GLES20.glBindFramebuffer(GLES20.GL_FRAMEBUFFER, fboId[0]);

        GLES20.glTexImage2D(GLES20.GL_TEXTURE_2D, 0, GLES20.GL_DEPTH_COMPONENT, mShadowMapWidth, mShadowMapHeight, 0, GLES20.GL_DEPTH_COMPONENT, GLES20.GL_UNSIGNED_INT, null);
        GLES20.glFramebufferTexture2D(GLES20.GL_FRAMEBUFFER, GLES20.GL_DEPTH_ATTACHMENT, GLES20.GL_TEXTURE_2D, renderTextureId[0], 0);

    }

    @Override
    public void onSurfaceChanged(GL10 unused, int width, int height) {
        mDisplayWidth = width;
        mDisplayHeight = height;
        GLES20.glViewport(0, 0, mDisplayWidth, mDisplayHeight);

        generateShadowFBO();

        float ratio = (float) mDisplayWidth / mDisplayHeight;
        float bottom = -1.0f;
        float top = 1.0f;
        float near = 1.0f;
        float far = 100.0f;

        Matrix.frustumM(mProjectionMatrix, 0, -ratio, ratio, bottom, top, near, far);
        Matrix.frustumM(mLightProjectionMatrix, 0, -1.1f * ratio, 1.1f * ratio, 1.1f * bottom, 1.1f * top, near, far);
    }

    @Override
    public void onDrawFrame(GL10 unused) {
        mActiveProgram = mSimpleShadowProgram.getProgram();

        Matrix.setLookAtM(mViewMatrix, 0,
                8, 10, 0,
                0, 0, 0,
                -5, 0, 0);

        scene_mvpMatrixUniform = GLES20.glGetUniformLocation(mActiveProgram, "uMVPMatrix");
        scene_mvMatrixUniform = GLES20.glGetUniformLocation(mActiveProgram, "uMVMatrix");
        scene_normalMatrixUniform = GLES20.glGetUniformLocation(mActiveProgram, "uNormalMatrix");
        scene_lightPosUniform = GLES20.glGetUniformLocation(mActiveProgram, "uLightPos");
        scene_shadowProjMatrixUniform = GLES20.glGetUniformLocation(mActiveProgram, "uShadowProjMatrix");
        scene_textureUniform = GLES20.glGetUniformLocation(mActiveProgram, "uShadowTexture");
        scene_positionAttribute = GLES20.glGetAttribLocation(mActiveProgram, "aPosition");
        scene_normalAttribute = GLES20.glGetAttribLocation(mActiveProgram, "aNormal");
        scene_colorAttribute = GLES20.glGetAttribLocation(mActiveProgram, "aColor");
        scene_mapStepXUniform = GLES20.glGetUniformLocation(mActiveProgram, "uxPixelOffset");
        scene_mapStepYUniform = GLES20.glGetUniformLocation(mActiveProgram, "uyPixelOffset");

        int shadowMapProgram = mDepthMapProgram.getProgram();
        shadow_mvpMatrixUniform = GLES20.glGetUniformLocation(shadowMapProgram, "uMVPMatrix");
        shadow_positionAttribute = GLES20.glGetAttribLocation(shadowMapProgram, "aShadowPosition");

        float[] basicMatrix = new float[16];

        Matrix.setIdentityM(basicMatrix, 0);
        Matrix.multiplyMV(mActualLightPosition, 0, basicMatrix, 0, mLightPosModel, 0);

        Matrix.setIdentityM(mModelMatrix, 0);

        Matrix.setLookAtM(mLightViewMatrix, 0,
                mActualLightPosition[0], mActualLightPosition[1], mActualLightPosition[2],
                mActualLightPosition[0], -mActualLightPosition[1], mActualLightPosition[2],
                -mActualLightPosition[0], 0, -mActualLightPosition[2]);

        GLES20.glCullFace(GLES20.GL_FRONT);

        s += 0.3f;
        if (s >= 360) s -= 360;
        Matrix.rotateM(mModelMatrix, 0, 25, 0, 1, 0);

        renderShadowMap();

        GLES20.glCullFace(GLES20.GL_BACK);

        renderScene();


    }

    private void renderShadowMap() {
        GLES20.glBindFramebuffer(GLES20.GL_FRAMEBUFFER, fboId[0]);

        GLES20.glViewport(0, 0, mShadowMapWidth, mShadowMapHeight);

        GLES20.glClearColor(1f, 1f, 1f, 1.0f);
        GLES20.glClear(GLES20.GL_DEPTH_BUFFER_BIT | GLES20.GL_COLOR_BUFFER_BIT);

        GLES20.glUseProgram(mDepthMapProgram.getProgram());

        float[] tempResultMatrix = new float[16];

        Matrix.multiplyMM(mLightMvpMatrix, 0, mLightViewMatrix, 0, mModelMatrix, 0);

        Matrix.multiplyMM(tempResultMatrix, 0, mLightProjectionMatrix, 0, mLightMvpMatrix, 0);
        System.arraycopy(tempResultMatrix, 0, mLightMvpMatrix, 0, 16);

        GLES20.glUniformMatrix4fv(shadow_mvpMatrixUniform, 1, false, mLightMvpMatrix, 0);
        table.render(shadow_positionAttribute, 0, 0, true);
        chair.render(shadow_positionAttribute, 0, 0, true);
        comp.render(shadow_positionAttribute, 0, 0, true);
        book.render(shadow_positionAttribute, 0, 0, true);
//        plate.render(shadow_positionAttribute, 0, 0, true);
        nizhnyayabulka.render(shadow_positionAttribute, 0, 0, true);
//        kartohakonchlas.render(shadow_positionAttribute, 0, 0, true);
        kruzhechka.render(shadow_positionAttribute, 0, 0, true);
        tel.render(shadow_positionAttribute, 0, 0, true);
        screen.render(shadow_positionAttribute, 0, 0, true);


    }

    private void renderScene() {
        GLES20.glBindFramebuffer(GLES20.GL_FRAMEBUFFER, 0);

        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT | GLES20.GL_DEPTH_BUFFER_BIT);

        GLES20.glUseProgram(mActiveProgram);

        GLES20.glViewport(0, 0, mDisplayWidth, mDisplayHeight);

        GLES20.glUniform1f(scene_mapStepXUniform, (float) (1.0 / mShadowMapWidth));
        GLES20.glUniform1f(scene_mapStepYUniform, (float) (1.0 / mShadowMapHeight));

        float[] tempResultMatrix = new float[100];

        float[] bias = new float[]{   // коорд глубины, тень
                0.5f, 0.0f, 0.0f, 0.0f,
                0.0f, 0.5f, 0.0f, 0.0f,
                0.0f, 0.0f, 0.5f, 0.0f,
                0.5f, 0.5f, 0.5f, 1.0f};

        float[] depthBiasMVP = new float[16]; // смещение глубины

        Matrix.multiplyMM(tempResultMatrix, 0, mViewMatrix, 0, mModelMatrix, 0);
        System.arraycopy(tempResultMatrix, 0, mMVMatrix, 0, 16);

        GLES20.glUniformMatrix4fv(scene_mvMatrixUniform, 1, false, mMVMatrix, 0);

        Matrix.invertM(tempResultMatrix, 0, mMVMatrix, 0);
        Matrix.transposeM(mNormalMatrix, 0, tempResultMatrix, 0);

        GLES20.glUniformMatrix4fv(scene_normalMatrixUniform, 1, false, mNormalMatrix, 0);

        Matrix.multiplyMM(tempResultMatrix, 0, mProjectionMatrix, 0, mMVMatrix, 0);
        System.arraycopy(tempResultMatrix, 0, mMVPMatrix, 0, 16);

        GLES20.glUniformMatrix4fv(scene_mvpMatrixUniform, 1, false, mMVPMatrix, 0);

        Matrix.multiplyMV(mLightPosInEyeSpace, 0, mViewMatrix, 0, mActualLightPosition, 0);

        GLES20.glUniform3f(scene_lightPosUniform, mLightPosInEyeSpace[0], mLightPosInEyeSpace[1], mLightPosInEyeSpace[2]);

        Matrix.multiplyMM(depthBiasMVP, 0, bias, 0, mLightMvpMatrix, 0);
        System.arraycopy(depthBiasMVP, 0, mLightMvpMatrix, 0, 16);

        GLES20.glUniformMatrix4fv(scene_shadowProjMatrixUniform, 1, false, mLightMvpMatrix, 0);

        table.render(scene_positionAttribute, scene_normalAttribute, scene_colorAttribute, false);
        chair.render(scene_positionAttribute, scene_normalAttribute, scene_colorAttribute, false);
        comp.render(scene_positionAttribute, scene_normalAttribute, scene_colorAttribute, false);
        book.render(scene_positionAttribute, scene_normalAttribute, scene_colorAttribute, false);
//        plate.render(scene_positionAttribute, scene_normalAttribute, scene_colorAttribute, false);
        nizhnyayabulka.render(scene_positionAttribute, scene_normalAttribute, scene_colorAttribute, false);
//        kartohakonchlas.render(scene_positionAttribute, scene_normalAttribute, scene_colorAttribute, false);
        kruzhechka.render(scene_positionAttribute, scene_normalAttribute, scene_colorAttribute, false);
        tel.render(scene_positionAttribute, scene_normalAttribute, scene_colorAttribute, false);
        screen.render(scene_positionAttribute, scene_normalAttribute, scene_colorAttribute, false);
    }
}
