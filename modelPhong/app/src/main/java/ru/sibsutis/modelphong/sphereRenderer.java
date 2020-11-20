package ru.sibsutis.modelphong;

import android.content.Context;
import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import android.opengl.Matrix;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

public class sphereRenderer implements GLSurfaceView.Renderer {

    private Context context;

    private Sphere sphere = new Sphere(0.5f);
    private float xСamera, yCamera, zCamera; //координаты камеры

    private int n;
    private final float[] modelMatrix = new float[16];
    private final float[] viewMatrix = new float[16];
    private final float[] modelViewMatrix = new float[16];
    private final float[] projectionMatrix = new float[16];
    private final float[] modelViewProjectionMatrix = new float[16];
    private float[] vertexArray;
    private float[] normalArray;
    static float[] colorArray;

    private FloatBuffer vertexBuffer, normalBuffer, colorBuffer;
    private Shader shader;
    float[] lightDir = {-1.0f, 1.0f, 5.0f}; // расположение блика на сфере

    public sphereRenderer(Context context) {

        n = sphere.count;
        // мы не будем двигать объекты поэтому сбрасываем модельную матрицу на единичную
        Matrix.setIdentityM(modelMatrix, 0);

        //координаты камеры
        xСamera = 0.0f;
        yCamera = 0.0f;
        zCamera = 1.0f;

        // пусть камера смотрит на начало координат и верх у камеры будет вдоль оси Y зная координаты камеры получаем матрицу вида
        Matrix.setLookAtM(viewMatrix, 0, xСamera, yCamera, zCamera, 0f, 0f, 0f, 0f, 1f, 0f);
        // умножая матрицу вида на матрицу модели получаем матрицу модели-вида:
        Matrix.multiplyMM(modelViewMatrix, 0, viewMatrix, 0, modelMatrix, 0);

        // массив координат вершин
        float[] vertexArray = {
                -1f, 1f, 0f,
                -1f, -1f, 0f,
                1f, 1f, 0f,
                1f, -1f, 0f
        };

        //создадим буфер для хранения координат вершин
        ByteBuffer tBuffer = ByteBuffer.allocateDirect(vertexArray.length * 4);
        tBuffer.order(ByteOrder.nativeOrder());
        vertexBuffer = tBuffer.asFloatBuffer();
        //перепишем координаты вершин из массива в буфер
        vertexBuffer.put(vertexArray);
        vertexBuffer.position(0);

        //вектор нормали перпендикулярен плоскости и направлен вдоль оси Y
        //нормаль одинакова для всех вершин
        float[] normalArray = new float[n * 3];
        for (int i = 0; i < n * 3; i++) {
            if (i % 3 == 2) normalArray[i] = 1f;
            else normalArray[i] = 0f;
        }

        //создадим буфер для хранения координат векторов нормали
        tBuffer = ByteBuffer.allocateDirect(normalArray.length * 4);
        tBuffer.order(ByteOrder.nativeOrder());
        normalBuffer = tBuffer.asFloatBuffer();
        normalBuffer.put(normalArray);
        normalBuffer.position(0);
        //перепишем координаты нормалей из массива в буфер
        normalBuffer.put(normalArray);
        normalBuffer.position(0);

        float[] colorArray = new float[n * 4];
        for (int i = 0; i < n * 3; i++) {
            if (i % 4 == 1) colorArray[i] = 0f;
            else colorArray[i] = 1f;
        }
        // буфер для хранения цветов вершин
        tBuffer = ByteBuffer.allocateDirect(colorArray.length * 4);
        tBuffer.order(ByteOrder.nativeOrder());
        colorBuffer = tBuffer.asFloatBuffer();
        colorBuffer.position(0);
        // перепишем цвета вершин из массива в буфер
        colorBuffer.put(colorArray);
        colorBuffer.position(0);
    }

    // метод, который срабатывает при изменении размеров экрана в нем мы получим матрицу проекции и матрицу модели-вида-проекции
    @Override
    public void onSurfaceChanged(GL10 gl, int width, int height) {
        // устанавливаем glViewport
        GLES20.glViewport(0, 0, width, height);
        float ratio = (float) width / height;
        float k = 0.055f;
        float left = -k * ratio;
        float right = k * ratio;
        float bottom = -k;
        float top = k;
        float near = 0.1f;
        float far = 10.0f;
        // получаем матрицу проекции
        Matrix.frustumM(projectionMatrix, 0, left, right, bottom, top, near, far);
        // матрица проекции изменилась, поэтому нужно пересчитать матрицу модели-вида-проекции
        Matrix.multiplyMM(modelViewProjectionMatrix, 0, projectionMatrix, 0, modelViewMatrix, 0);
    }

    // метод, который срабатывает при создании экрана здесь мы создаем шейдерный объект
    @Override
    public void onSurfaceCreated(GL10 gl, EGLConfig config) {
        GLES20.glClearColor(1.0f, 1.0f, 1.0f, 0.0f);
        //включаем тест глубины
        GLES20.glEnable(GLES20.GL_DEPTH_TEST);
        //включаем отсечение невидимых граней
        GLES20.glEnable(GLES20.GL_CULL_FACE);
        //включаем сглаживание текстур
        GLES20.glHint(GLES20.GL_GENERATE_MIPMAP_HINT, GLES20.GL_NICEST);
        // записываем код вершинного шейдера в виде строки
        String vertexShaderCode =
                "uniform mat4 u_modelViewProjectionMatrix;" +
                        "attribute vec3 a_vertex;" +
                        "attribute vec3 a_normal;" +
                        "attribute vec4 a_color;" +
                        "varying vec3 v_vertex;" +
                        "varying vec3 v_normal;" +
                        "varying vec4 v_color;" +
                        "void main() {" +
                        "v_vertex=a_vertex;" +
                        "vec3 n_normal=normalize(a_normal);" +
                        "v_normal=n_normal;" +
                        "v_color=a_color;" +
                        "gl_Position = u_modelViewProjectionMatrix * vec4(a_vertex,1.0);" +
                        "}";

        //записываем код фрагментного шейдера в виде строки
        String fragmentShaderCode =
                "precision mediump float;" +
                        "uniform vec3 u_camera;" +
                        "uniform vec3 u_lightPosition;" +
                        "varying vec3 v_vertex;" +
                        "varying vec3 v_normal;" +
                        "varying vec4 v_color;" +
                        "void main() {" +
                        "vec3 n_normal=normalize(v_normal);" +
                        "vec3 lightvector = normalize(u_lightPosition - v_vertex);" +
                        "vec3 lookvector = normalize(u_camera - v_vertex);" +
                        "float ambient=0.2;" +
                        "float k_diffuse=0.8;" +
                        "float k_specular=0.4;" +
                        "float diffuse = k_diffuse * max(dot(n_normal, lightvector), 0.0);" +
                        "vec3 reflectvector = reflect(-lightvector, n_normal);" +
                        "float specular = k_specular * pow( max(dot(lookvector,reflectvector),0.0), 40.0 );" +
                        "vec4 one=vec4(1.0,5.0,1.0,1.0);" +
                        "gl_FragColor = (ambient+diffuse+specular)*one;" +
                        "}";

        //создадим шейдерный объект
        shader = new Shader(vertexShaderCode, fragmentShaderCode);
        //свяжем буфер вершин с атрибутом a_vertex в вершинном шейдере
        shader.linkVertexBuffer(vertexBuffer);
        //свяжем буфер нормалей с атрибутом a_normal в вершинном шейдере
        shader.linkNormalBuffer(normalBuffer);
        //свяжем буфер цветов с атрибутом a_color в вершинном шейдере
        shader.linkColorBuffer(colorBuffer);
        //связь атрибутов с буферами сохраняется до тех пор, пока не будет уничтожен шейдерный объект
    }

    @Override
    public void onDrawFrame(GL10 gl) {
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT | GLES20.GL_DEPTH_BUFFER_BIT); //очищаем кадр
        GLES20.glEnable(GL10.GL_BLEND);

        shader.linkVertexBuffer(sphere.mVertexBuffer);
        shader.linkColorBuffer(colorBuffer);   //связь буфера цветов с атрибутом a_color в вершинном шейдере
        //в отличие от атрибутов связь униформ с внешними параметрами не сохраняется, поэтому перед рисованием каждого кадра
        //нужно связывать униформы заново передаем в шейдерный объект матрицу модели-вида-проекции
        shader.linkModelViewProjectionMatrix(modelViewProjectionMatrix);
        shader.linkCamera(xСamera, yCamera, zCamera);  //передаем в шейдерный объект координаты камеры
        shader.linkLightSource(-0.6f, 0.4f, 0.3f);  //передаем в шейдерный объект координаты источника света
        shader.useProgram();

        for (int i = 0; i < n - 3; i += 4) {
            GLES20.glDrawArrays(GLES20.GL_TRIANGLE_FAN, i, 4);
        }

        GLES20.glDisable(GL10.GL_BLEND);
    }
}
