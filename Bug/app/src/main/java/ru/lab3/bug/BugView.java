package ru.lab3.bug;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.util.DisplayMetrics;
import android.view.MotionEvent;
import android.view.View;

public class BugView extends View {

    private Bitmap background;
    private Bug bug = new Bug();
    private Paint score;
    private Matrix matrix;
    private boolean reached;
    private Integer sc = 0;

    public BugView(Context context) {
        super(context);
        bug.texture = BitmapFactory.decodeResource(context.getResources(), R.drawable.images);

        /*background = BitmapFactory.decodeResource(getResources(), R.drawable.grass);
        BitmapDrawable bitmapDrawable = new BitmapDrawable(background);
        bitmapDrawable.setTileModeXY(android.graphics.Shader.TileMode.REPEAT, android.graphics.Shader.TileMode.REPEAT); // гшоворим обьекту как рисовать (у меня это повторяющийся фон)
        layout.setBackgroundDrawable(bitmapDrawable); */

        score = new Paint();
        score.setColor(Color.BLACK);
        score.setTextSize(30);
        score.setTypeface(Typeface.DEFAULT_BOLD);
        score.setAntiAlias(true);
        matrix = new Matrix();
        reached = true;
        DisplayMetrics metrics = getResources().getDisplayMetrics();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        //canvas.drawBitmap(background, 0, 0, null);
        canvas.drawText("score: " + sc, 150, 50, score);
        //matrix.postScale(3.0f, 3.0f); // Масштабируем
        if (reached) {
            bug.destX = (float) Math.random() * getWidth();
            bug.destY = (float) Math.random() * getHeight();
            bug.stepX = (bug.destX - bug.x) / 50;
            bug.stepY = (bug.destY - bug.y) / 50;
            Integer tp;
            if (bug.x <= bug.destX && bug.y >= bug.destY)
                tp = (int) Math.floor(Math.toDegrees(Math.atan(Math.abs(bug.x - bug.destX) / Math.abs(bug.y - bug.destY))));
            else if (bug.x <= bug.destX && bug.y <= bug.destY)
                tp = 90 + (int) Math.floor(Math.toDegrees(Math.atan(Math.abs(bug.y - bug.destY) / Math.abs(bug.x - bug.destX))));
            else if (bug.x >= bug.destX && bug.y <= bug.destY)
                tp = 180 + (int) Math.floor(Math.toDegrees(Math.atan(Math.abs(bug.x - bug.destX) / Math.abs(bug.y - bug.destY))));
            else
                tp = 270 + (int) Math.floor(Math.toDegrees(Math.atan(Math.abs(bug.y - bug.destY) / Math.abs(bug.x - bug.destX))));
            matrix.preRotate(tp - bug.p, bug.texture.getWidth() / 2, bug.texture.getHeight() / 2);
            bug.p = tp;
            reached = false;
        } else {
            if (Math.abs(bug.x - bug.destX) < 0.1 &&
                    Math.abs(bug.y - bug.destY) < 0.1)
                reached = true;

            matrix.postTranslate(bug.stepX, bug.stepY);
            bug.x += bug.stepX;
            bug.y += bug.stepY;
        }
        canvas.drawBitmap(bug.texture, matrix, null);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            if (Math.abs(bug.x - event.getX() + 30) < 100 &&
                    Math.abs(bug.y - event.getY() + 30) < 120) {
                matrix.setRotate(0, bug.texture.getWidth() / 2, bug.texture.getHeight() / 2);
                matrix.reset();
                bug.p = 0;
                ++sc;
                reached = true;
                float ty, tx;
                int temp = (int) Math.floor(Math.random() * 4);
                switch (temp) {
                    case 0:
                        ty = (float) Math.random() * getHeight();
                        bug.x = 0f;
                        bug.y = ty;
                        break;
                    case 1:
                        ty = (float) Math.random() * getHeight();
                        bug.x = (float) getWidth();
                        bug.y = ty;
                        break;
                    case 2:
                        tx = (float) Math.random() * getWidth();
                        bug.x = tx;
                        bug.y = 0f;
                        break;
                    case 3:
                        tx = (float) Math.random() * getWidth();
                        bug.x = tx;
                        bug.y = (float) getHeight();
                        break;
                }
                matrix.postTranslate(bug.x, bug.y);
            }

        }
        return true;
    }
}
