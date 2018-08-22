package com.ddx.myopengles20;

import android.content.Context;
import android.opengl.GLSurfaceView;

/**
 * Created by dingdx on 2018/8/8.
 */

class MyGLSurfaceView extends GLSurfaceView{
    private final MyGLRender mRenderer;

    public MyGLSurfaceView(Context context) {
        super(context);

        // 创建OpenGL ES 2.0上下文
        setEGLContextClientVersion(2);

        mRenderer = new MyGLRender();

        // 设置Renderer用于在GLSurfaceView容器上绘图
        setRenderer(mRenderer);

        //只有在绘制数据改变时才绘制view，可以防止GLSurfaceView帧重绘
        //setRenderMode(GLSurfaceView.RENDERMODE_WHEN_DIRTY);
    }
}
