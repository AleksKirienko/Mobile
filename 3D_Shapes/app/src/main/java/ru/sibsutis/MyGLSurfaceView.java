package ru.sibsutis;

import android.content.Context;
import android.opengl.GLSurfaceView;
import android.util.AttributeSet;

public class MyGLSurfaceView extends GLSurfaceView {

    public MyGLSurfaceView(Context context) {
        super(context);
        init();
    }

    public MyGLSurfaceView(Context context, AttributeSet attrs) {
        super(context);
        init();
    }

    private void init() {
        // Create an OpenGL ES 2.0 context
        setEGLContextClientVersion(2);
        setPreserveEGLContextOnPause(true);
        setRenderer(new MyGLRenderer());
    }
}
