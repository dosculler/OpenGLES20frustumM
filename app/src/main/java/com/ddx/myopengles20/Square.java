package com.ddx.myopengles20;

import android.opengl.GLES20;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.ShortBuffer;

/**
 * Created by dingdx on 2018/8/11.
 */

public class Square {
    private FloatBuffer vertexBuffer;
    private ShortBuffer drawListBuffer;

    private final int mProgram;

    private int mPositionHandle;
    private int mColorHandle;

    private final int vertexCount = squareCoords.length / COORDS_PER_VERTEX;
    private final int vertexStride = COORDS_PER_VERTEX * 4; // 4 bytes per vertex

    //设置每个顶点的坐标数
    static final int COORDS_PER_VERTEX = 3;


    static float squareCoords[] = {
            -0.5f,  0.5f, 0.0f,   // top left
            -0.5f, -0.5f, 0.0f,   // bottom left
            0.5f, -0.5f, 0.0f,   // bottom right
            0.5f,  0.5f, 0.0f }; // top right

    // 设置三角形颜色和透明度（r,g,b,a）
    float color[] = {0.0f, 1.0f, 0f, 1.0f};//绿色不透明

    private short drawOrder[] = { 0, 1, 2, 0, 2, 3 }; // order to draw vertices

    public Square() {
        // initialize vertex byte buffer for shape coordinates
        ByteBuffer bb = ByteBuffer.allocateDirect(
                // (# of coordinate values * 4 bytes per float)
                squareCoords.length * 4);
        bb.order(ByteOrder.nativeOrder());
        vertexBuffer = bb.asFloatBuffer();
        vertexBuffer.put(squareCoords);
        vertexBuffer.position(0);

        // initialize byte buffer for the draw list
        ByteBuffer dlb = ByteBuffer.allocateDirect(
                // (# of coordinate values * 2 bytes per short)
                drawOrder.length * 2);
        dlb.order(ByteOrder.nativeOrder());
        drawListBuffer = dlb.asShortBuffer();
        drawListBuffer.put(drawOrder);
        drawListBuffer.position(0);


        /*在三角形类中编译shader代码，并将它们添加到一个OpenGL ES program 对象中，然后链接这个program*/
        // 编译shader代码
        int vertexShader = MyGLRender.loadShader(GLES20.GL_VERTEX_SHADER,
                vertexShaderCode);
        int fragmentShader = MyGLRender.loadShader(GLES20.GL_FRAGMENT_SHADER,
                fragmentShaderCode);

        // 创建空的OpenGL ES Program
        mProgram = GLES20.glCreateProgram();

        // 将vertex shader添加到program
        GLES20.glAttachShader(mProgram, vertexShader);

        // 将fragment shader添加到program
        GLES20.glAttachShader(mProgram, fragmentShader);

        // 创建可执行的 OpenGL ES program
        GLES20.glLinkProgram(mProgram);
    }

    /*    使用OpenGL ES 2.0绘制一个定义好的形状需要大量的代码，因为必须提供给图形渲染管道很多细节信息

    VertexShader：用于渲染形状的顶点的OpenGL ES图形代码
    FragmentShader：用于渲染形状的外观（颜色或纹理）的OpenGL ES代码
    Program：一个OpenGL ES对象，包含了你想要用来绘制一个或多个形状的shader
    至少需要一个vertex shader来绘制一个形状和一个fragment shader来为形状着色*/
    private final String vertexShaderCode =
            "attribute vec4 vPosition;" +
                    "void main() {" +
                    "  gl_Position = vPosition;" +
                    "}";

    private final String fragmentShaderCode =
            "precision mediump float;" +
                    "uniform vec4 vColor;" +
                    "void main() {" +
                    "  gl_FragColor = vColor;" +
                    "}";

    /*创建一个draw()方法负责绘制形状*/
    public void draw() {
        // 添加program到OpenGL ES环境中
        GLES20.glUseProgram(mProgram);

        // 获取指向vertex shader的成员vPosition的handle
        mPositionHandle = GLES20.glGetAttribLocation(mProgram, "vPosition");

        // 启用一个指向三角形的顶点数组的handle
        GLES20.glEnableVertexAttribArray(mPositionHandle);

        //准备三角形的坐标数据
        GLES20.glVertexAttribPointer(mPositionHandle, COORDS_PER_VERTEX,
                GLES20.GL_FLOAT, false,
                vertexStride, vertexBuffer);

        // 获取指向fragment shader的成员vColor的handle
        mColorHandle = GLES20.glGetUniformLocation(mProgram, "vColor");

        //  绘制三角形
        GLES20.glUniform4fv(mColorHandle, 1, color, 0);

        // Draw the triangle
        GLES20.glDrawArrays(GLES20.GL_TRIANGLES, 0, vertexCount);

        // 禁用指向三角形的顶点数组
        GLES20.glDisableVertexAttribArray(mPositionHandle);
    }
}
