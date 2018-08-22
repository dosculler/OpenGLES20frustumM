package com.ddx.myopengles20;

import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import android.opengl.Matrix;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

/**
 * Created by dingdx on 2018/8/8.
 */

public class MyGLRender implements GLSurfaceView.Renderer{
    private Triangle mTriangle;
    private Square mSquare;

    //（五）使用投影和相机视图:使顶点们跟随据屏幕的宽高比进行修正
    // mMVPMatrix is an abbreviation for "Model View Projection Matrix"
    private final float[] mMVPMatrix = new float[16];
    private final float[] mProjectionMatrix = new float[16];
    private final float[] mViewMatrix = new float[16];
    @Override
    public void onSurfaceCreated(GL10 gl10, EGLConfig eglConfig) {//仅调用一次，用于设置view的OpenGL ES环境
        //设置背景色黑色不透明，或者白色不透明(1.0f, 1.0f, 1.0f, 1.0f)
        GLES20.glClearColor(0.0f, 0.0f, 0.0f, 1.0f);//parameter:r/g/b/alpha, 0.0f~1.0f.
    }

    @Override
    public void onSurfaceChanged(GL10 gl10, int width, int height) {//当view的几何形状发生变化时调用，比如设备屏幕方向改变时
        //绘制窗口
        GLES20.glViewport(0, 0, width, height);

        //（五）使用投影和相机视图:使顶点们跟随据屏幕的宽高比进行修正
        float ratio = (float) width / height;
        // this projection matrix is applied to object coordinates in the onDrawFrame() method
        Matrix.frustumM(mProjectionMatrix, 0, -ratio, ratio, -1, 1, 3, 7);

        //初始化三角形
        mTriangle = new Triangle();
        // 初始化正方形
        //mSquare = new Square();
    }

    @Override
    public void onDrawFrame(GL10 gl10) {//每次重绘view时调用
        //重绘背景色
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT);

        //（五）使用投影和相机视图:使顶点们跟随据屏幕的宽高比进行修正
        // Set the camera position (View matrix)
        Matrix.setLookAtM(mViewMatrix, 0, 0, 0, -3, 0f, 0f, 0f, 0f, 1.0f, 0.0f);
        // Calculate the projection and view transformation
        Matrix.multiplyMM(mMVPMatrix, 0, mProjectionMatrix, 0, mViewMatrix, 0);
        //通过Matrix.setLookAtM()方法计算相机视图变换，然后将其与之前计算出的投影矩阵结合到一起。合并后的矩阵接下来会传递给绘制的图形
        mTriangle.draw(mMVPMatrix);

        //mTriangle.draw();
        //mSquare.draw();
    }

    /*    Shader包含OpenGL Shading Language(GLSL)代码，必须在OpenGL ES环境下先编译再使用。想要编译这些代码，需要在你的Renderer类中创建一个工具类方法*/
    public static int loadShader(int type, String shaderCode){

        //创建一个vertex shader类型(GLES20.GL_VERTEX_SHADER)
        //或一个fragment shader类型(GLES20.GL_FRAGMENT_SHADER)
        int shader = GLES20.glCreateShader(type);

        // 将源码添加到shader并编译它
        GLES20.glShaderSource(shader, shaderCode);
        GLES20.glCompileShader(shader);

        return shader;
    }
}
