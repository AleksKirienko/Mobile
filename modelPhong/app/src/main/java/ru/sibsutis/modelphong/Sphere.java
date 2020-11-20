package ru.sibsutis.modelphong;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

public class Sphere {

    public int count = 0;
    public FloatBuffer mVertexBuffer;
    public FloatBuffer textureBuffer;

    public Sphere(float R) {
        int n = 0;
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
                count++;
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
}
