package ru.sibsutis.modelphong;

import android.opengl.GLES20;

import java.nio.FloatBuffer;

public class Shader {
    //будем хранить ссылку на шейдерную программу внутри класса как локальное поле
    private int program_Handle;

    //при создании объекта класса передаем в конструктор строки кода вершинного и фрагментного шейдера
    public Shader(String vertexShaderCode, String fragmentShaderCode) {
        //вызываем метод, создающий шейдерную программу при этом заполняется поле program_Handle
        createProgram(vertexShaderCode, fragmentShaderCode);
    }

    // метод, который создает шейдерную программу, вызывается в конструкторе
    private void createProgram(String vertexShaderCode, String fragmentShaderCode) {

        int vertexShader_Handle = GLES20.glCreateShader(GLES20.GL_VERTEX_SHADER); // получаем ссылку на вершинный шейдер
        GLES20.glShaderSource(vertexShader_Handle, vertexShaderCode); // присоединяем к вершинному шейдеру его код
        GLES20.glCompileShader(vertexShader_Handle);   // компилируем вершинный шейдер

        int fragmentShader_Handle = GLES20.glCreateShader(GLES20.GL_FRAGMENT_SHADER);   //получаем ссылку на фрагментный шейдер
        GLES20.glShaderSource(fragmentShader_Handle, fragmentShaderCode); //присоединяем к фрагментному шейдеру его код
        GLES20.glCompileShader(fragmentShader_Handle);   // компилируем фрагментный шейдер
        program_Handle = GLES20.glCreateProgram();    //получаем ссылку на шейдерную программу

        GLES20.glAttachShader(program_Handle, vertexShader_Handle);   //присоединяем к шейдерной программе вершинный шейдер
        GLES20.glAttachShader(program_Handle, fragmentShader_Handle);  //присоединяем к шейдерной программе фрагментный шейдер
        GLES20.glLinkProgram(program_Handle);   //компилируем шейдерную программу
    }

    // метод, который связывает буфер координат вершин vertexBuffer с атрибутом a_vertex
    public void linkVertexBuffer(FloatBuffer vertexBuffer) {
        GLES20.glUseProgram(program_Handle);  //устанавливаем активную программу
        int a_vertex_Handle = GLES20.glGetAttribLocation(program_Handle, "a_vertex"); //получаем ссылку на атрибут a_vertex
        GLES20.glEnableVertexAttribArray(a_vertex_Handle);  //включаем использование атрибута a_vertex
        //связываем буфер координат вершин vertexBuffer с атрибутом a_vertex
        GLES20.glVertexAttribPointer(a_vertex_Handle, 3, GLES20.GL_FLOAT, false, 0, vertexBuffer);
    }

    //метод, который связывает буфер координат векторов нормалей normalBuffer с атрибутом a_normal
    public void linkNormalBuffer(FloatBuffer normalBuffer) {
        GLES20.glUseProgram(program_Handle);  //устанавливаем активную программу
        int a_normal_Handle = GLES20.glGetAttribLocation(program_Handle, "a_normal"); //получаем ссылку на атрибут a_normal
        GLES20.glEnableVertexAttribArray(a_normal_Handle);   //включаем использование атрибута a_normal
        //связываем буфер нормалей normalBuffer с атрибутом a_normal
        GLES20.glVertexAttribPointer(a_normal_Handle, 3, GLES20.GL_FLOAT, false, 0, normalBuffer);
    }

    //метод, который связывает буфер цветов вершин colorBuffer с атрибутом a_color
    public void linkColorBuffer(FloatBuffer colorBuffer) {
        GLES20.glUseProgram(program_Handle);  //устанавливаем активную программу
        //получаем ссылку на атрибут a_color
        int a_color_Handle = GLES20.glGetAttribLocation(program_Handle, "a_color");
        GLES20.glEnableVertexAttribArray(a_color_Handle); //включаем использование атрибута a_color
        //связываем буфер нормалей colorBuffer с атрибутом a_color
        GLES20.glVertexAttribPointer(a_color_Handle, 4, GLES20.GL_FLOAT, false, 0, colorBuffer);
    }

    // метод, который связывает матрицу модели-вида-проекции modelViewProjectionMatrix с униформой u_modelViewProjectionMatrix
    public void linkModelViewProjectionMatrix(float[] modelViewProjectionMatrix) {
        GLES20.glUseProgram(program_Handle);  //устанавливаем активную программу
        //получаем ссылку на униформу u_modelViewProjectionMatrix
        int u_modelViewProjectionMatrix_Handle = GLES20.glGetUniformLocation(program_Handle, "u_modelViewProjectionMatrix");
        //связываем массив modelViewProjectionMatrix с униформой u_modelViewProjectionMatrix
        GLES20.glUniformMatrix4fv(u_modelViewProjectionMatrix_Handle, 1, false, modelViewProjectionMatrix, 0);
    }

    // метод, который связывает координаты камеры с униформой u_camera
    public void linkCamera(float xCamera, float yCamera, float zCamera) {
        GLES20.glUseProgram(program_Handle);  //устанавливаем активную программу
        int u_camera_Handle = GLES20.glGetUniformLocation(program_Handle, "u_camera"); //получаем ссылку на униформу u_camera
        GLES20.glUniform3f(u_camera_Handle, xCamera, yCamera, zCamera); // связываем координаты камеры с униформой u_camera
    }

    // метод, который связывает координаты источника света с униформой u_lightPosition
    public void linkLightSource(float xLightPosition, float yLightPosition, float zLightPosition) {
        //устанавливаем активную программу
        GLES20.glUseProgram(program_Handle);
        //получаем ссылку на униформу u_lightPosition
        int u_lightPosition_Handle = GLES20.glGetUniformLocation(program_Handle, "u_lightPosition");
        // св€зываем координаты источника света с униформой u_lightPosition
        GLES20.glUniform3f(u_lightPosition_Handle, xLightPosition, yLightPosition, zLightPosition);
    }

    // метод, который делает шейдерную программу данного класса активной
    public void useProgram() {
        GLES20.glUseProgram(program_Handle);
    }
}


