package com.example.user.jni;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.opengl.GLSurfaceView;
import android.opengl.GLUtils;

import java.io.InputStream;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

class RendererWrapper implements GLSurfaceView.Renderer {
    static public int[] texture_name = {
            R.drawable.paint
    };

    Context c;

    public RendererWrapper(Context context) {
        c = context;
    }

    static public int[] textures = new int [texture_name.length];


    private void loadGLTexture(GL10 gl) {
        gl.glGenTextures(1, textures, 0);
        for (int i = 0; i < texture_name.length; ++i) {
            gl.glBindTexture(GL10.GL_TEXTURE_2D, textures[i]);
            gl.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_MIN_FILTER, GL10.GL_LINEAR);
            InputStream is = c.getResources().openRawResource(texture_name[i]);
            Bitmap bitmap = BitmapFactory.decodeStream(is);
            GLUtils.texImage2D(GL10.GL_TEXTURE_2D, 0, bitmap, 0);
            bitmap.recycle();
        }
    }


    @Override
    public void onSurfaceCreated(GL10 gl, EGLConfig config) {
        loadGLTexture(gl);
        JNIWrapper.onsurfacecreated();
    }

    @Override
    public void onSurfaceChanged(GL10 gl, int width, int height) {
        JNIWrapper.onsurfacechanged(width, height);
    }

    @Override
    public void onDrawFrame(GL10 gl) {
        JNIWrapper.ondrawframe();
    }
}
