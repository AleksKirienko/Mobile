package ru.lab3.bug;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity {

    private BugView view;
    private Handler handler;
    private final static int interval = 50;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_main);

        view = new BugView(this);
        setContentView(view);

        handler = new Handler();
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        view.invalidate();
                    }
                });
            }
        }, 0, interval);
    }

    /*@Override
    public void onDraw(Canvas c) {
        Paint p1 = new Paint();// Создаем кисть для рисования
        p1.setColor(Color.rgb(100, 0, 255)); // Меняем цвет
        p1.setTextSize(50); // Задаем размер текста
        c.drawText("Жмяк:", 100, 100, p1); // Выводим текст
    }*/
}