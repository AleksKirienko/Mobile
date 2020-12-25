package com.example.user.newcurswork;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.ShortBuffer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

import android.content.Context;
import android.opengl.GLES20;

class Objects {
    private FloatBuffer colorBuffer;

    private FloatBuffer verticesBuffer;
    private FloatBuffer normalBuffer;

    private ShortBuffer facesVertexBuffer;
    private ShortBuffer facesNormalBuffer;

    private List<String> facesList;

    Objects(Context c, float[] color, String ObjName) {
        List<String> verticesList = new ArrayList<>();
        facesList = new ArrayList<>();
        List<String> normalList = new ArrayList<>();
        try {
            Scanner scanner = new Scanner(c.getAssets().open(ObjName));
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                if (line.startsWith("v ")) {
                    verticesList.add(line);
                } else if (line.startsWith("f ")) {
                    facesList.add(line);
                } else if (line.startsWith("vn ")) {
                    normalList.add(line);
                } else if (line.startsWith("vt ")) {
                    continue;
                }
            }
            ByteBuffer buffer1 = ByteBuffer.allocateDirect(verticesList.size() * 3 * 4);
            buffer1.order(ByteOrder.nativeOrder());
            verticesBuffer = buffer1.asFloatBuffer();

            ByteBuffer buffer2 = ByteBuffer.allocateDirect(normalList.size() * 3 * 4);
            buffer2.order(ByteOrder.nativeOrder());
            normalBuffer = buffer2.asFloatBuffer();

            ByteBuffer buffer3 = ByteBuffer.allocateDirect(facesList.size() * 3 * 2);
            buffer3.order(ByteOrder.nativeOrder());
            facesVertexBuffer = buffer3.asShortBuffer();

            ByteBuffer buffer4 = ByteBuffer.allocateDirect(facesList.size() * 3 * 2);
            buffer4.order(ByteOrder.nativeOrder());
            facesNormalBuffer = buffer4.asShortBuffer();

            for (String vertex : verticesList) {
                String coords[] = vertex.split(" ");
                float x = Float.parseFloat(coords[1]);
                float y = Float.parseFloat(coords[2]);
                float z = Float.parseFloat(coords[3]);
                verticesBuffer.put(x);
                verticesBuffer.put(y);
                verticesBuffer.put(z);
            }
            verticesBuffer.position(0);

            for (String vertex : normalList) {
                String coords[] = vertex.split(" ");
                float x = Float.parseFloat(coords[1]);
                float y = Float.parseFloat(coords[2]);
                float z = Float.parseFloat(coords[3]);
                normalBuffer.put(x);
                normalBuffer.put(y);
                normalBuffer.put(z);
            }
            normalBuffer.position(0);

            for (String face : facesList) {
                String vertexIndices[] = face.split(" ");
                System.out.println("VertexIndices:" + Arrays.toString(vertexIndices));
                String coord1[],coord2[], coord3[];

                if(vertexIndices[1].contains("//"))
                    coord1 = vertexIndices[1].split("//");
                else
                    coord1 = vertexIndices[1].split("/");

                if(vertexIndices[2].contains("//"))
                    coord2 = vertexIndices[2].split("//");
                else
                    coord2 = vertexIndices[2].split("/");

                if(vertexIndices[3].contains("//"))
                    coord3 = vertexIndices[3].split("//");
                else
                    coord3 = vertexIndices[3].split("/");

                short vertex1 = Short.parseShort(coord1[0]);
                short vertex2 = Short.parseShort(coord2[0]);
                short vertex3 = Short.parseShort(coord3[0]);
                facesVertexBuffer.put((short) (vertex1 - 1));
                facesVertexBuffer.put((short) (vertex2 - 1));
                facesVertexBuffer.put((short) (vertex3 - 1));

                vertex1 = Short.parseShort(coord1[1]);
                vertex2 = Short.parseShort(coord2[1]);
                vertex3 = Short.parseShort(coord3[1]);
                facesNormalBuffer.put((short) (vertex1 - 1));
                facesNormalBuffer.put((short) (vertex2 - 1));
                facesNormalBuffer.put((short) (vertex3 - 1));
            }
            facesVertexBuffer.position(0);
            facesNormalBuffer.position(0);

            verticesList.clear();
            normalList.clear();

            scanner.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        float[] colorData = new float[facesList.size() * 4];
        for (int v = 0; v < facesList.size(); v++) {
            colorData[4 * v] = color[0];
            colorData[4 * v + 1] = color[1];
            colorData[4 * v + 2] = color[2];
            colorData[4 * v + 3] = color[3];
        }

        ByteBuffer bColor = ByteBuffer.allocateDirect(colorData.length * 4);
        bColor.order(ByteOrder.nativeOrder());
        colorBuffer = bColor.asFloatBuffer();
        colorBuffer.put(colorData).position(0);
    }

    void render(int positionAttribute, int normalAttribute, int colorAttribute, boolean onlyPosition) {
        facesVertexBuffer.position(0);
        facesNormalBuffer.position(0);
        verticesBuffer.position(0);
        normalBuffer.position(0);
        colorBuffer.position(0);

        GLES20.glVertexAttribPointer(positionAttribute, 3, GLES20.GL_FLOAT, false,
                0, verticesBuffer);
        GLES20.glEnableVertexAttribArray(positionAttribute);

        if (!onlyPosition) {
            GLES20.glVertexAttribPointer(normalAttribute, 3, GLES20.GL_FLOAT, false,
                    0, normalBuffer);
            GLES20.glEnableVertexAttribArray(normalAttribute);

            GLES20.glVertexAttribPointer(colorAttribute, 4, GLES20.GL_FLOAT, false,
                    0, colorBuffer);
            GLES20.glEnableVertexAttribArray(colorAttribute);
        }

        GLES20.glDrawElements(GLES20.GL_TRIANGLES, facesList.size() * 3,
                GLES20.GL_UNSIGNED_SHORT, facesVertexBuffer);
    }
}