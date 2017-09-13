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
	final float TOUCH_SCALE_FACTOR = 180.0f/SCREEN_WIDTH;//�Ƕ����ű���
    SceneRenderer mRenderer;//������Ⱦ��
    float mPreviousX;//�ϴεĴ���λ��X����
    float mPreviousY;//�ϴεĴ���λ��Y����
    

    int tex_index=0;//�������id����   
    
    int texWallId[]=new int[3];  //���ǽ����Id����
    int texFloorId;//��������id
    

    static Bitmap[] baseBitmap;
    static Bitmap[] frameBitmap;
    static Bitmap[] diskBitmap;
    
    int[] baseId;
    int[] frameId;
    int[] diskId;
	
    static Bitmap bmUp;//ǰ�����ⰴť
    static Bitmap bmDown;//�������ⰴť
    static Bitmap bmWest;//������
    static Bitmap bmEast;//������
    
    int textureUpId;//ϵͳ����id
    int textureDownId;//ϵͳ����ť����id
    int textureEastId;//�������ⰴť����ID
    int textureWestId;//�������ⰴť����ID
    
    static Bitmap[] bmaWall=new Bitmap[3];//���ǽ��������
    static Bitmap bmFloor;//����
    
	private float yAngle=90;//��Y��ת����
	private float xAngle=35;//����
	//���������
	private float cx;
	private float cz;
	private float cy;
	//Ŀ�������
	private float tx=0;
	private float tz=0;
	private float ty=0;//-HOUSE_GAO/3;
	//�����ͷ��ָ��
	private float upX=0;
	private float upY=1;
	private float upZ=0;
	
	//��ť�ķ�Χ//�������ߵ��ĸ�����
	private float[] button1;
	private float[] button2;
	private float[] button3;
	private float[] button4;
	
	
	//----��������----------------------
	HouseForDraw house;//����
	DisplayStation displayStation;//չ̨
	AntennaConfig antenna[]=new AntennaConfig[3];//��
	TextureRect button;//��ť
	LoadedObjectVertexNormalXC rome;//������������
	
	private float half_width_button=0.15f;
//	private float half_height_button=0.1f;
	private float offset_X_Button1=-0.7f;
	private float offset_Y_Button1=-0.8f;
	private float offset_X_Button2=0.7f;
	private float offset_Y_Button2=-0.8f;
	
	public boolean flagForThread=true;//�̱߳�־
	
	public int index_boat=0;//��������
	private float ratio;//����ͶӰ�������ű���
	
	//--------���Թ���---------------
	private boolean flag_go;//������Ա�־λ 
	final int countGoStepInitValue=35;
	private int countGoStep=0;
	private float acceleratedVelocity=0.06f;
	private float ori_angle_speed=7;//��ʼ���ٶ�
	private float curr_angle_speed;//��ǰ���ٶ�
	private boolean isMoved;//�ж��Ƿ��ƶ���
	//---------����ǽǽ�ϵĹ��---------------
	public boolean flag_display=true;//����־λ
	public AntennaSurfaceView(Context context,float pitch,float yaw)
	{
        super(context);
        this.setEGLContextClientVersion(2); //����ʹ��OPENGL ES2.0
        
        
        mRenderer = new SceneRenderer();	//����������Ⱦ��
        mRenderer.pitch=pitch;
        mRenderer.yaw=yaw;
        super.setEGLConfigChooser(8 , 8, 8, 8, 16, 0);
        setRenderer(mRenderer);				//������Ⱦ��		        
        setRenderMode(GLSurfaceView.RENDERMODE_CONTINUOUSLY);//������ȾģʽΪ������Ⱦ   
        this.setKeepScreenOn(true);
        
        cx=20;
        cy=20;
        cz=50;
    }
	
	
	//������LoadedObjectVertexNormal�б�
	static LoadedObjectVertexTexXC[][] parts=new LoadedObjectVertexTexXC[3][];
	static 
	{
		parts[0]=new LoadedObjectVertexTexXC[AntennaInfo.boatPartNames[0].length];
		parts[1]=new LoadedObjectVertexTexXC[AntennaInfo.boatPartNames[1].length];
		parts[2]=new LoadedObjectVertexTexXC[AntennaInfo.boatPartNames[2].length];
	}
	
	static LoadedObjectVertexNormalXC romeData;
	
	//�˴������ڼ���ģ��ʱ�����⣬��Ҫ����
	public static void loadVertexFromObj(Resources r)
	{		
		for(int j=0;j<AntennaInfo.boatPartNames.length;j++)
		{
			for(int i=0;i<AntennaInfo.boatPartNames[j].length;i++)  
			{  			
				parts[j][i]=LoadUtilTexXC.loadFromFileVertexOnly//���˴�����
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
		float yaw;//����ƫ���Ƕ�
		float pitch;//���帩���Ƕ�
        public void onDrawFrame(GL10 gl) 
        { 
        	 
        	//�����Ȼ�������ɫ����
            GLES20.glClear( GLES20.GL_DEPTH_BUFFER_BIT | GLES20.GL_COLOR_BUFFER_BIT); 
            MatrixState.setProjectFrustum(-ratio, ratio, -1, 1, 2.5f, 1000);           
            MatrixState.setCamera(cx,cy,cz,tx,ty,tz,upX,upY,upZ);
            MatrixState.copyMVMatrix();
            //��ʼ����Դλ��
            MatrixState.setLightLocation(cx, cy+5, cz+3);            
            
            house.drawFloor(texFloorId);//���Ƶ���
                       
            //����͸��Բ��,�������
            GLES20.glEnable(GLES20.GL_BLEND);//�������
            GLES20.glBlendFunc(GLES20.GL_SRC_ALPHA, GLES20.GL_ONE_MINUS_SRC_ALPHA);//���û������
            //displayStation.drawTransparentCircle();
            GLES20.glDisable(GLES20.GL_BLEND); //�رջ��            
            
            //��������
            drawAntenna(yaw,pitch);
        }  
        public void onSurfaceChanged(GL10 gl, int width, int height)
        {
            //�����Ӵ���С��λ�� 
        	GLES20.glViewport(0, 0, width, height); 
        	//����GLSurfaceView�Ŀ�߱�
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
            				//������й��Բ���
            				if(flag_go)//����������
            				{
            					countGoStep--;
            					if(countGoStep<=0)
            					{
            						curr_angle_speed=curr_angle_speed+acceleratedVelocity;//���㵱ǰ���ٶ�
            					}
            					
            					if(Math.abs(curr_angle_speed)>0.1f)
            					{
            						yAngle=yAngle+curr_angle_speed;
                					cx=(float)(tx+Math.cos(Math.toRadians(xAngle))*Math.sin(Math.toRadians(yAngle))*XC_DISTANCE);//�����x���� 
                				    cz=(float)(tz+Math.cos(Math.toRadians(xAngle))*Math.cos(Math.toRadians(yAngle))*XC_DISTANCE);//�����z���� 
                				    cy=(float)(ty+Math.sin(Math.toRadians(xAngle))*XC_DISTANCE);//�����y���� 
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
        	//�����������   
            GLES20.glEnable(GLES20.GL_CULL_FACE);
            //����ȼ��
            GLES20.glEnable(GLES20.GL_DEPTH_TEST);
            //�򿪶���
            GLES20.glEnable(GLES20.GL_DITHER);
            MatrixState.setInitStack();
            
        	synchronized(MonitorActivity.boatInitLock)
        	{        		
            	ShaderManager.compileShaderReal();            	
                texFloorId=initTextureFromBitmap(bmFloor);//��������ͼ3   
                
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
            	
                //������Ļ����ɫRGBA
                GLES20.glClearColor(0.2f,0.2f,1f, 1.0f);
                                
                //��������==========================================================
                house=new HouseForDraw();//��������
                displayStation=new DisplayStation(RADIUS_DISPLAY, LENGTH_DISPLAY);//����չ̨
                antenna[0]=new AntennaConfig(parts[0],AntennaSurfaceView.this);//������ֻltf
                antenna[1]=new AntennaConfig(parts[1],AntennaSurfaceView.this);//������ֻcjg
                antenna[2]=new AntennaConfig(parts[2],AntennaSurfaceView.this);//��������pjh
                //boat[3]=new Boat(parts[3],XCSurfaceView.this);//��������pjh,���
                
                button=new TextureRect(ShaderManager.getCommTextureShaderProgram(), half_width_button, XC_Self_Adapter_Data_TRASLATE[com.example.TDViewer.Constant.screenId][0]);//������ذ�ť
            	
            	//�����������   
                GLES20.glEnable(GLES20.GL_CULL_FACE);
                //����ȼ��
                GLES20.glEnable(GLES20.GL_DEPTH_TEST);
                //�򿪶���
                GLES20.glEnable(GLES20.GL_DITHER);
                MatrixState.setInitStack();             
        	}
        }
        //�������߲���ת�ķ���
        public void drawAntenna(float yaw,float pitch)
        {
        	MatrixState.pushMatrix();
        	MatrixState.translate(0, -9f, 1f);             	  
        	MatrixState.scale(RATIO_BOAT, RATIO_BOAT, RATIO_BOAT);
        	//MatrixState.rotate(0, 0, 0, 1);//������ת����
        	antenna[0].drawSelf(baseId);
        	//boat[1].drawSelf(frameId);
        	MatrixState.popMatrix();
        	
        	MatrixState.pushMatrix();
        	MatrixState.translate(0, -9f, 1f);             	  
        	MatrixState.scale(RATIO_BOAT, RATIO_BOAT, RATIO_BOAT);
        	MatrixState.rotate(yaw, 0, 1, 0);//������ת����
        	antenna[1].drawSelf(frameId);
        	MatrixState.popMatrix();
        	
        	MatrixState.pushMatrix();
        	MatrixState.translate(0f, -4f, 1f);
        	MatrixState.scale(RATIO_BOAT, RATIO_BOAT, RATIO_BOAT);
        	MatrixState.rotate(yaw, 0, 1, 0);//������ת����
        	MatrixState.rotate(pitch-40, 0, 0, 1);//������ת����
        	antenna[2].drawSelf(diskId);
        	MatrixState.popMatrix();
        }

    }

	public int initTextureFromBitmap(Bitmap bitmapTmp)//textureId
	{
		//��������ID
		int[] textures = new int[1];
		GLES20.glGenTextures
		(
				1,          //����������id������
				textures,   //����id������
				0           //ƫ����
		);    
		int textureId=textures[0];    
		GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, textureId);
		GLES20.glTexParameterf(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MIN_FILTER,GLES20.GL_NEAREST);
		GLES20.glTexParameterf(GLES20.GL_TEXTURE_2D,GLES20.GL_TEXTURE_MAG_FILTER,GLES20.GL_NEAREST);
		GLES20.glTexParameterf(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_WRAP_S,GLES20.GL_REPEAT);
		GLES20.glTexParameterf(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_WRAP_T,GLES20.GL_REPEAT);    
        
        //ʵ�ʼ�������
        GLUtils.texImage2D
        (
        		GLES20.GL_TEXTURE_2D,   //�������ͣ���OpenGL ES�б���ΪGL10.GL_TEXTURE_2D
        		0, 					  //����Ĳ�Σ�0��ʾ����ͼ��㣬�������Ϊֱ����ͼ
        		bitmapTmp, 			  //����ͼ��
        		0					  //����߿�ߴ�
        );
        
        return textureId;
	}
	
   	public static void loadWelcomeBitmap(Resources r)
	{
		  InputStream is=null;
          try  
          {
        	  
        	  
//-------------ɾ����־
        	  is= r.openRawResource(R.drawable.south);	
        	  bmUp=BitmapFactory.decodeStream(is);
        	  is= r.openRawResource(R.drawable.north);	
        	  bmDown=BitmapFactory.decodeStream(is);
        	  is= r.openRawResource(R.drawable.east);//�˴���Ҫ����Ϊ������ťͼ��	
        	  bmWest=BitmapFactory.decodeStream(is);
        	  is= r.openRawResource(R.drawable.west);	//�˴���Ҫ��Ϊ������ťͼ��
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
