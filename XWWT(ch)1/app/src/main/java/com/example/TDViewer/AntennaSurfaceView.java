package com.example.TDViewer;

import java.io.IOException;
import java.io.InputStream;
import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import com.example.TDViewer.AntennaInfo;
import com.example.TDViewer.ShaderManager;
import com.example.fragment.MonitorActivity;
import com.example.xwwt.R;
import com.example.TDView.MatrixState;
import com.example.TDViewer.Constant;
import static com.example.TDViewer.Constant.*;


import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import android.opengl.GLUtils;
import android.view.MotionEvent;


public class AntennaSurfaceView extends GLSurfaceView 
{
	final float TOUCH_SCALE_FACTOR = 180.0f/SCREEN_WIDTH;//角度缩放比例
    SceneRenderer mRenderer;//场景渲染器
    float mPreviousX;//上次的触控位置X坐标
    float mPreviousY;//上次的触控位置Y坐标
    

    int tex_index=0;//广告纹理id索引   
    
    int texWallId[]=new int[3];  //广告墙纹理Id数组
    int texFloorId;//地面纹理id
    

    static Bitmap[] baseBitmap;
    static Bitmap[] frameBitmap;
    static Bitmap[] diskBitmap;
    
    int[] baseId;
    int[] frameId;
    int[] diskId;
	
    static Bitmap bmUp;//前进虚拟按钮
    static Bitmap bmDown;//后退虚拟按钮
    static Bitmap bmWest;//西方向
    static Bitmap bmEast;//东方向
    
    int textureUpId;//系统北向id
    int textureDownId;//系统南向按钮纹理id
    int textureEastId;//东方虚拟按钮纹理ID
    int textureWestId;//西方虚拟按钮纹理ID
    
    static Bitmap[] bmaWall=new Bitmap[3];//广告墙纹理数组
    static Bitmap bmFloor;//地面
    
	private float yAngle=90;//绕Y轴转动角
	private float xAngle=35;//仰角
	//摄像机坐标
	private float cx;
	private float cz;
	private float cy;
	//目标点坐标
	private float tx=0;
	private float tz=0;
	private float ty=0;//-HOUSE_GAO/3;
	//摄像机头顶指向
	private float upX=0;
	private float upY=1;
	private float upZ=0;
	
	//按钮的范围//定义天线的四个朝向
	private float[] button1;
	private float[] button2;
	private float[] button3;
	private float[] button4;
	
	
	//----创建物体----------------------
	HouseForDraw house;//房间
	DisplayStation displayStation;//展台
	AntennaConfig antenna[]=new AntennaConfig[3];//船
	TextureRect button;//按钮
	LoadedObjectVertexNormalXC rome;//创建罗马柱子
	
	private float half_width_button=0.15f;
//	private float half_height_button=0.1f;
	private float offset_X_Button1=-0.7f;
	private float offset_Y_Button1=-0.8f;
	private float offset_X_Button2=0.7f;
	private float offset_Y_Button2=-0.8f;
	
	public boolean flagForThread=true;//线程标志
	
	public int index_boat=0;//船的索引
	private float ratio;//绘制投影矩阵缩放比例
	
	//--------测试惯性---------------
	private boolean flag_go;//允许惯性标志位 
	final int countGoStepInitValue=35;
	private int countGoStep=0;
	private float acceleratedVelocity=0.06f;
	private float ori_angle_speed=7;//初始角速度
	private float curr_angle_speed;//当前角速度
	private boolean isMoved;//判断是否移动过
	//---------纹理墙墙上的广告---------------
	public boolean flag_display=true;//广告标志位
	public AntennaSurfaceView(Context context,float pitch,float yaw)
	{
        super(context);
        this.setEGLContextClientVersion(2); //设置使用OPENGL ES2.0
        
        
        mRenderer = new SceneRenderer();	//创建场景渲染器
        mRenderer.pitch=pitch;
        mRenderer.yaw=yaw;
        super.setEGLConfigChooser(8 , 8, 8, 8, 16, 0);
        setRenderer(mRenderer);				//设置渲染器		        
        setRenderMode(GLSurfaceView.RENDERMODE_CONTINUOUSLY);//设置渲染模式为主动渲染   
        this.setKeepScreenOn(true);
        
        cx=20;
        cy=20;
        cz=50;
    }
	
	
	//船部件LoadedObjectVertexNormal列表
	static LoadedObjectVertexTexXC[][] parts=new LoadedObjectVertexTexXC[3][];
	static 
	{
		parts[0]=new LoadedObjectVertexTexXC[AntennaInfo.boatPartNames[0].length];
		parts[1]=new LoadedObjectVertexTexXC[AntennaInfo.boatPartNames[1].length];
		parts[2]=new LoadedObjectVertexTexXC[AntennaInfo.boatPartNames[2].length];
	}
	
	static LoadedObjectVertexNormalXC romeData;
	
	//此处代码在加载模型时有问题，需要更改
	public static void loadVertexFromObj(Resources r)
	{		
		for(int j=0;j<AntennaInfo.boatPartNames.length;j++)
		{
			for(int i=0;i<AntennaInfo.boatPartNames[j].length;i++)  
			{  			
				parts[j][i]=LoadUtilTexXC.loadFromFileVertexOnly//将此处更改
				(
						AntennaInfo.boatPartNames[j][i], 
					r,
					//XCSurfaceView.this
					ShaderManager.getCommTextureShaderProgram()
			     );	
				
			}
		}
	}
	
	private class SceneRenderer implements GLSurfaceView.Renderer 
    {
		float yaw;//定义偏航角度
		float pitch;//定义俯仰角度
        public void onDrawFrame(GL10 gl) 
        { 
        	 
        	//清除深度缓冲与颜色缓冲
            GLES20.glClear( GLES20.GL_DEPTH_BUFFER_BIT | GLES20.GL_COLOR_BUFFER_BIT); 
            MatrixState.setProjectFrustum(-ratio, ratio, -1, 1, 2.5f, 1000);           
            MatrixState.setCamera(cx,cy,cz,tx,ty,tz,upX,upY,upZ);
            MatrixState.copyMVMatrix();
            //初始化光源位置
            MatrixState.setLightLocation(cx, cy+5, cz+3);            
            
            house.drawFloor(texFloorId);//绘制地面
                       
            //绘制透明圆面,开启混合
            GLES20.glEnable(GLES20.GL_BLEND);//开启混合
            GLES20.glBlendFunc(GLES20.GL_SRC_ALPHA, GLES20.GL_ONE_MINUS_SRC_ALPHA);//设置混合因子
            //displayStation.drawTransparentCircle();
            GLES20.glDisable(GLES20.GL_BLEND); //关闭混合            
            
            //绘制天线
            drawAntenna(yaw,pitch);
        }  
        public void onSurfaceChanged(GL10 gl, int width, int height)
        {
            //设置视窗大小及位置 
        	GLES20.glViewport(0, 0, width, height); 
        	//计算GLSurfaceView的宽高比
            ratio = (float) width / height;            
            //virtualButton();
            new Thread()
            {
				@Override
            	public void run()
            	{
            		while(flagForThread)
            		{
            			try 
            			{
            				//这里进行惯性测试
            				if(flag_go)//如果允许惯性
            				{
            					countGoStep--;
            					if(countGoStep<=0)
            					{
            						curr_angle_speed=curr_angle_speed+acceleratedVelocity;//计算当前角速度
            					}
            					
            					if(Math.abs(curr_angle_speed)>0.1f)
            					{
            						yAngle=yAngle+curr_angle_speed;
                					cx=(float)(tx+Math.cos(Math.toRadians(xAngle))*Math.sin(Math.toRadians(yAngle))*XC_DISTANCE);//摄像机x坐标 
                				    cz=(float)(tz+Math.cos(Math.toRadians(xAngle))*Math.cos(Math.toRadians(yAngle))*XC_DISTANCE);//摄像机z坐标 
                				    cy=(float)(ty+Math.sin(Math.toRadians(xAngle))*XC_DISTANCE);//摄像机y坐标 
            					}
            					else
            					{
            						curr_angle_speed=0;
            						flag_go=false;
            					}
            				}
							Thread.sleep(10);
						}
            			catch (InterruptedException e)
            			{
							e.printStackTrace();
						}
            		}
            	}
            }.start();
        }
        
        
        public void onSurfaceCreated(GL10 gl, EGLConfig config) 
        {	    
        	//开启背面剪裁   
            GLES20.glEnable(GLES20.GL_CULL_FACE);
            //打开深度检测
            GLES20.glEnable(GLES20.GL_DEPTH_TEST);
            //打开抖动
            GLES20.glEnable(GLES20.GL_DITHER);
            MatrixState.setInitStack();
            
        	synchronized(MonitorActivity.boatInitLock)
        	{        		
            	ShaderManager.compileShaderReal();            	
                texFloorId=initTextureFromBitmap(bmFloor);//地面纹理图3   
                
                baseId=new int[baseBitmap.length];
                frameId=new int[frameBitmap.length];
                diskId=new int[diskBitmap.length];
                
                
              for(int i=0;i<baseBitmap.length;i++)
              {
            	  baseId[i]=initTextureFromBitmap(baseBitmap[i]);
              }
              
              for(int i=0;i<frameBitmap.length;i++)
              {
            	  frameId[i]=initTextureFromBitmap(frameBitmap[i]);
              }
              
              for(int i=0;i<diskBitmap.length;i++)
              {
            	  diskId[i]=initTextureFromBitmap(diskBitmap[i]);
              }
            	
                //设置屏幕背景色RGBA
                GLES20.glClearColor(0.2f,0.2f,1f, 1.0f);
                                
                //立体物体==========================================================
                house=new HouseForDraw();//创建房间
                displayStation=new DisplayStation(RADIUS_DISPLAY, LENGTH_DISPLAY);//创建展台
                antenna[0]=new AntennaConfig(parts[0],AntennaSurfaceView.this);//创建船只ltf
                antenna[1]=new AntennaConfig(parts[1],AntennaSurfaceView.this);//创建船只cjg
                antenna[2]=new AntennaConfig(parts[2],AntennaSurfaceView.this);//创建天线pjh
                //boat[3]=new Boat(parts[3],XCSurfaceView.this);//创建天线pjh,添加
                
                button=new TextureRect(ShaderManager.getCommTextureShaderProgram(), half_width_button, XC_Self_Adapter_Data_TRASLATE[com.example.TDViewer.Constant.screenId][0]);//绘制相关按钮
            	
            	//开启背面剪裁   
                GLES20.glEnable(GLES20.GL_CULL_FACE);
                //打开深度检测
                GLES20.glEnable(GLES20.GL_DEPTH_TEST);
                //打开抖动
                GLES20.glEnable(GLES20.GL_DITHER);
                MatrixState.setInitStack();             
        	}
        }
        //绘制天线并旋转的方法
        public void drawAntenna(float yaw,float pitch)
        {
        	MatrixState.pushMatrix();
        	MatrixState.translate(0, -9f, 1f);             	  
        	MatrixState.scale(RATIO_BOAT, RATIO_BOAT, RATIO_BOAT);
        	//MatrixState.rotate(0, 0, 0, 1);//进行旋转设置
        	antenna[0].drawSelf(baseId);
        	//boat[1].drawSelf(frameId);
        	MatrixState.popMatrix();
        	
        	MatrixState.pushMatrix();
        	MatrixState.translate(0, -9f, 1f);             	  
        	MatrixState.scale(RATIO_BOAT, RATIO_BOAT, RATIO_BOAT);
        	MatrixState.rotate(yaw, 0, 1, 0);//进行旋转设置
        	antenna[1].drawSelf(frameId);
        	MatrixState.popMatrix();
        	
        	MatrixState.pushMatrix();
        	MatrixState.translate(0f, -4f, 1f);
        	MatrixState.scale(RATIO_BOAT, RATIO_BOAT, RATIO_BOAT);
        	MatrixState.rotate(yaw, 0, 1, 0);//进行旋转设置
        	MatrixState.rotate(pitch-40, 0, 0, 1);//进行旋转设置
        	antenna[2].drawSelf(diskId);
        	MatrixState.popMatrix();
        }

    }

	public int initTextureFromBitmap(Bitmap bitmapTmp)//textureId
	{
		//生成纹理ID
		int[] textures = new int[1];
		GLES20.glGenTextures
		(
				1,          //产生的纹理id的数量
				textures,   //纹理id的数组
				0           //偏移量
		);    
		int textureId=textures[0];    
		GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, textureId);
		GLES20.glTexParameterf(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MIN_FILTER,GLES20.GL_NEAREST);
		GLES20.glTexParameterf(GLES20.GL_TEXTURE_2D,GLES20.GL_TEXTURE_MAG_FILTER,GLES20.GL_NEAREST);
		GLES20.glTexParameterf(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_WRAP_S,GLES20.GL_REPEAT);
		GLES20.glTexParameterf(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_WRAP_T,GLES20.GL_REPEAT);    
        
        //实际加载纹理
        GLUtils.texImage2D
        (
        		GLES20.GL_TEXTURE_2D,   //纹理类型，在OpenGL ES中必须为GL10.GL_TEXTURE_2D
        		0, 					  //纹理的层次，0表示基本图像层，可以理解为直接贴图
        		bitmapTmp, 			  //纹理图像
        		0					  //纹理边框尺寸
        );
        
        return textureId;
	}
	
   	public static void loadWelcomeBitmap(Resources r)
	{
		  InputStream is=null;
          try  
          {
        	  
        	  
//-------------删除标志
        	  is= r.openRawResource(R.drawable.south);	
        	  bmUp=BitmapFactory.decodeStream(is);
        	  is= r.openRawResource(R.drawable.north);	
        	  bmDown=BitmapFactory.decodeStream(is);
        	  is= r.openRawResource(R.drawable.east);//此处需要更改为西方按钮图标	
        	  bmWest=BitmapFactory.decodeStream(is);
        	  is= r.openRawResource(R.drawable.west);	//此处需要改为东方按钮图标
        	  bmEast=BitmapFactory.decodeStream(is);
//--------------------------  
        	  is= r.openRawResource(R.drawable.floor);	
        	  bmFloor=BitmapFactory.decodeStream(is);
        
              baseBitmap=new Bitmap[parts[0].length];
              frameBitmap=new Bitmap[parts[1].length];
              diskBitmap=new Bitmap[parts[2].length]; 
              
        	  for(int i=0;i<parts[0].length;i++)
        	  {
        		  is= r.openRawResource(AntennaInfo.boatTexIdName[0][i]);	
        		  baseBitmap[i]=BitmapFactory.decodeStream(is);
        	  }
        	  
        	  for(int i=0;i<parts[1].length;i++)
        	  {
        		  is= r.openRawResource(AntennaInfo.boatTexIdName[1][i]);	
        		  frameBitmap[i]=BitmapFactory.decodeStream(is);
        	  }
        	  
        	  for(int i=0;i<parts[2].length;i++)
        	  {
        		  is= r.openRawResource(AntennaInfo.boatTexIdName[2][i]);	
        		  diskBitmap[i]=BitmapFactory.decodeStream(is);
        	  }
          } 
	      finally 
	      {
            try 
            {
                is.close();
            } 
            catch(IOException e) 
            {
                e.printStackTrace();
            }
	      }	      
	}   
}
