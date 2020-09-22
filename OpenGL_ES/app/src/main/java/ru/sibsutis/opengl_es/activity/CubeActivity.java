package ru.sibsutis.opengl_es.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.opengl.GLSurfaceView;
import android.os.Bundle;
import android.view.WindowManager;

import ru.sibsutis.opengl_es.R;
import ru.sibsutis.opengl_es.entity.Cube;
import ru.sibsutis.opengl_es.entity.Square;

public class CubeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        GLSurfaceView view = new GLSurfaceView(this);
        view.setRenderer(new Cube());
        setContentView(view);
    }
}