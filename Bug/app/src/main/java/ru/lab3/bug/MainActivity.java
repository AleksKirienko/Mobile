package ru.lab3.bug;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_main);
        setContentView(new Panel(this)); // заменяем разметку на наш элемент
    }

    class Panel extends View {
        public Panel(Context context) {
            super(context);
            // установим обработчик нажатия
            this.setOnTouchListener(new OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    // Здесь выводится тип нажатия и координаты X Y
                    Log.v("Sasha", "Pos:" + event.getAction()
                            + event.getX() + " "
                            + event.getY());
                    return false;
                }
            });
        }

        @Override
        public void onDraw(Canvas c) {
            Paint p1 = new Paint();// Создаем кисть для рисования
            p1.setColor(Color.rgb(100, 0, 255)); // Меняем цвет
            p1.setTextSize(50); // Задаем размер текста
            c.drawText("Жмяк:", 100, 100, p1); // Выводим текст
        }
    }
}