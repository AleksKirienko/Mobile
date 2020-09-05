package ru.sibsutis;

import android.app.Activity;
import android.opengl.GLSurfaceView;
import android.os.Bundle;

public class MainActivity extends Activity {
    private MyGLSurfaceView gLView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        gLView = new MyGLSurfaceView(this);
        setContentView(gLView);
    }

    @Override
    protected void onResume() {
        super.onResume();
        gLView.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        gLView.onPause();
    }
}