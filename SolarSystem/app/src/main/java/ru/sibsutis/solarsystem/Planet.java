package ru.sibsutis.solarsystem;

import android.opengl.GLSurfaceView;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

public class Planet implements GLSurfaceView.Renderer {

    private int n;
    private FloatBuffer mVertexBuffer;
    public FloatBuffer textureBuffer;

    public Planet(float R) {
        n = 0;
        int dtheta = 15, dphi = 15;
        float DTOR = (float) (Math.PI / 180.0f);
        ByteBuffer byteBuf = ByteBuffer.allocateDirect(5000 * 3 * 4);  // выделение памяти из основной кучи JVM
        byteBuf.order(ByteOrder.nativeOrder()); // извлекает собственный порядок байтов базовой платформы
        mVertexBuffer = byteBuf.asFloatBuffer();
        byteBuf = ByteBuffer.allocateDirect(5000 * 2 * 4);
        byteBuf.order(ByteOrder.nativeOrder());
        textureBuffer = byteBuf.asFloatBuffer();

        for (int theta = -90; theta <= 90 - dtheta; theta += dtheta) {
            for (int phi = 0; phi <= 360 - dphi; phi += dphi) {
                mVertexBuffer.put((float) (Math.cos(theta * DTOR) * Math.cos(phi * DTOR)) * R);
                mVertexBuffer.put((float) (Math.cos(theta * DTOR) * Math.sin(phi * DTOR)) * R);
                mVertexBuffer.put((float) (Math.sin(theta * DTOR)) * R);

                double cosM = Math.cos((theta + dtheta) * DTOR);
                mVertexBuffer.put((float) (cosM * Math.cos(phi * DTOR)) * R);
                mVertexBuffer.put((float) (cosM * Math.sin(phi * DTOR)) * R);

                double sinM = Math.sin((theta + dtheta) * DTOR);
                mVertexBuffer.put((float) sinM * R);
                mVertexBuffer.put((float) (cosM * Math.cos((phi + dphi) * DTOR)) * R);
                mVertexBuffer.put((float) (cosM * Math.sin((phi + dphi) * DTOR)) * R);
                mVertexBuffer.put((float) sinM * R);
                mVertexBuffer.put((float) (Math.cos(theta * DTOR) * Math.cos((phi + dphi) * DTOR)) * R);
                mVertexBuffer.put((float) (Math.cos(theta * DTOR) * Math.sin((phi + dphi) * DTOR)) * R);
                mVertexBuffer.put((float) (Math.sin(theta * DTOR)) * R);
                n += 4;

                textureBuffer.put((float) (phi / 360.0f));
                textureBuffer.put((float) ((90 + theta) / 180.0f));
                textureBuffer.put((float) (phi / 360.0f));
                textureBuffer.put((float) ((90 + theta + dtheta) / 180.0f));
                textureBuffer.put((float) ((phi + dphi) / 360.0f));
                textureBuffer.put((float) ((90 + theta + dtheta) / 180.0f));
                textureBuffer.put((float) ((phi + dphi) / 360.0f));
                textureBuffer.put((float) ((90 + theta) / 180.0f));
            }
        }

        mVertexBuffer.position(0);
        textureBuffer.position(0);
    }

    @Override
    public void onSurfaceCreated(GL10 gl, EGLConfig config) {

    }

    @Override
    public void onSurfaceChanged(GL10 gl, int width, int height) {

    }

    @Override
    public void onDrawFrame(GL10 gl) {
        gl.glEnable(GL10.GL_BLEND); // разрешение на наложение цветов
        gl.glBlendFunc(GL10.GL_SRC_ALPHA, GL10.GL_ONE_MINUS_SRC_ALPHA); //алгоритм смешения с масштабными коэффициентами
        gl.glEnableClientState(GL10.GL_VERTEX_ARRAY); // разрешить массив вершин
        gl.glVertexPointer(3, GL10.GL_FLOAT, 0, mVertexBuffer); // определяет массив данных вершин, хранит в памяти видеокарты
        gl.glTexCoordPointer(2, GL10.GL_FLOAT, 0, textureBuffer);   // c текстурами

        for (int i = 0; i < n; i += 4)
            gl.glDrawArrays(GL10.GL_TRIANGLE_FAN, i, 4);

        // рендер примитивов из массива
        gl.glDisableClientState(GL10.GL_VERTEX_ARRAY);
        gl.glDisable(GL10.GL_BLEND);
    }
}
