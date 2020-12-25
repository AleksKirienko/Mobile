package com.example.user.newcurswork;

import android.app.Activity;
import android.opengl.GLSurfaceView;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

public class MainActivity extends Activity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        GLSurfaceView mGLSurfaceView = new GLSurfaceView(this);

        mGLSurfaceView.setEGLContextClientVersion(2);

        ShadowsRenderer renderer = new ShadowsRenderer(this, this);
        mGLSurfaceView.setRenderer(renderer);

        setContentView(mGLSurfaceView);
    }
}
