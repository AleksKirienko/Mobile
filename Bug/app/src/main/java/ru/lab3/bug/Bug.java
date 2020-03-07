package ru.lab3.bug;

import android.graphics.Bitmap;
import android.graphics.Point;

class Bug {
    public Bitmap texture;
    public Float x, y, stepX, stepY, destX, destY;
    public Integer p;

    private Point point;

    public Bug() {
        x = 0f;
        y = 0f;
        p = 0;
        destX = 0f;
        destY = 0f;
        p = 0;
    }

    public boolean checkCol(float Sx, float Sy) {
        return true;
    }
}
