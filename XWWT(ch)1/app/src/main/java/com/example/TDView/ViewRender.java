//package com.example.TDView;
//
//import static com.bn.st.xc.Constant.HOUSE_GAO;
//import static com.bn.st.xc.Constant.SCREEN_WIDTH;
//import static com.bn.st.xc.Constant.XC_DISTANCE;
//
//import javax.microedition.khronos.opengles.GL10;
//
//import com.bn.core.MatrixState;
//import com.bn.st.xc.TextureRect;
//import com.bn.st.xc.XCSurfaceView.SceneRenderer;
//
//import android.content.Context;
//import android.graphics.Bitmap;
//import android.graphics.BitmapFactory;
//import android.opengl.GLES20;
//import android.opengl.GLSurfaceView;
////三维显示类
//public class ViewRender extends GLSurfaceView
//{
//	final float TOUCH_SCALE_FACTOR = 180.0f/SCREEN_WIDTH;//角度缩放比例 
//	
//	SceneRenderer mRenderer;//场景渲染器
//	
//    int textureNorthId;//北方虚拟按钮纹理id
//    int textureSouthId;//南方虚拟按钮纹理id
//    int textureEastId;//东方虚拟按钮纹理ID
//    int textureWestId;//西方虚拟按钮纹理ID
//    
//    TextureRect button;//按钮
//    
//	//摄像机坐标
//	private float cx;
//	private float cz;
//	private float cy;
//	//目标点坐标
//	private float tx=0;
//	private float tz=0;
//	private float ty=0;
//	//摄像机头顶指向
//	private float upX=0;
//	private float upY=1;
//	private float upZ=0;
//	
//	//构造函数此处为在当前数据背景下，卫星天线的朝向设置，摄像机的朝向不变
//	public ViewRender(Context context)
//	{
//        super(context);
//        this.setEGLContextClientVersion(2); //设置使用OPENGL ES2.0
//        mRenderer = new SceneRenderer();	//创建场景渲染器
//        setRenderer(mRenderer);				//设置渲染器		        
//        setRenderMode(GLSurfaceView.RENDERMODE_CONTINUOUSLY);//设置渲染模式为主动渲染   
//        this.setKeepScreenOn(true);
//        cx=(float)(tx+Math.cos(Math.toRadians(xAngle))*Math.sin(Math.toRadians(yAngle))*XC_DISTANCE);//摄像机x坐标 
//        cz=(float)(tz+Math.cos(Math.toRadians(xAngle))*Math.cos(Math.toRadians(yAngle))*XC_DISTANCE);//摄像机z坐标 
//        cy=(float)(ty+Math.sin(Math.toRadians(xAngle))*XC_DISTANCE);//摄像机y坐标 
//    }
//	
//	
//	//场景渲染类
//	private class SceneRenderer implements GLSurfaceView.Renderer 
//    {
//
//        public void onDrawFrame(GL10 gl) 
//        { 
//        	 
//        	//清除深度缓冲与颜色缓冲
//            GLES20.glClear( GLES20.GL_DEPTH_BUFFER_BIT | GLES20.GL_COLOR_BUFFER_BIT); 
//            MatrixState.setProjectFrustum(-ratio, ratio, -1, 1, 2.5f, 1000);           
//            MatrixState.setCamera(cx,cy,cz,tx,ty,tz,upX,upY,upZ);
//            MatrixState.copyMVMatrix();
//            //初始化光源位置
//            MatrixState.setLightLocation(cx, cy+5, cz+3);            
//
//            
//            //绘制透明圆面,开启混合
//            GLES20.glEnable(GLES20.GL_BLEND);//开启混合
//            GLES20.glBlendFunc(GLES20.GL_SRC_ALPHA, GLES20.GL_ONE_MINUS_SRC_ALPHA);//设置混合因子
//            displayStation.drawTransparentCircle();
//            GLES20.glDisable(GLES20.GL_BLEND); //关闭混合            
//            
//            //绘制天线
//            drawAntenna();
//            //绘制柱子
//            //drawRomeColumn();
//            //绘制四个方向按钮
//            drawButton();
//        }  
//	
//}