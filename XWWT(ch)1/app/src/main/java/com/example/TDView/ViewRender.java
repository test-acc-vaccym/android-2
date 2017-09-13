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
////��ά��ʾ��
//public class ViewRender extends GLSurfaceView
//{
//	final float TOUCH_SCALE_FACTOR = 180.0f/SCREEN_WIDTH;//�Ƕ����ű��� 
//	
//	SceneRenderer mRenderer;//������Ⱦ��
//	
//    int textureNorthId;//�������ⰴť����id
//    int textureSouthId;//�Ϸ����ⰴť����id
//    int textureEastId;//�������ⰴť����ID
//    int textureWestId;//�������ⰴť����ID
//    
//    TextureRect button;//��ť
//    
//	//���������
//	private float cx;
//	private float cz;
//	private float cy;
//	//Ŀ�������
//	private float tx=0;
//	private float tz=0;
//	private float ty=0;
//	//�����ͷ��ָ��
//	private float upX=0;
//	private float upY=1;
//	private float upZ=0;
//	
//	//���캯���˴�Ϊ�ڵ�ǰ���ݱ����£��������ߵĳ������ã�������ĳ��򲻱�
//	public ViewRender(Context context)
//	{
//        super(context);
//        this.setEGLContextClientVersion(2); //����ʹ��OPENGL ES2.0
//        mRenderer = new SceneRenderer();	//����������Ⱦ��
//        setRenderer(mRenderer);				//������Ⱦ��		        
//        setRenderMode(GLSurfaceView.RENDERMODE_CONTINUOUSLY);//������ȾģʽΪ������Ⱦ   
//        this.setKeepScreenOn(true);
//        cx=(float)(tx+Math.cos(Math.toRadians(xAngle))*Math.sin(Math.toRadians(yAngle))*XC_DISTANCE);//�����x���� 
//        cz=(float)(tz+Math.cos(Math.toRadians(xAngle))*Math.cos(Math.toRadians(yAngle))*XC_DISTANCE);//�����z���� 
//        cy=(float)(ty+Math.sin(Math.toRadians(xAngle))*XC_DISTANCE);//�����y���� 
//    }
//	
//	
//	//������Ⱦ��
//	private class SceneRenderer implements GLSurfaceView.Renderer 
//    {
//
//        public void onDrawFrame(GL10 gl) 
//        { 
//        	 
//        	//�����Ȼ�������ɫ����
//            GLES20.glClear( GLES20.GL_DEPTH_BUFFER_BIT | GLES20.GL_COLOR_BUFFER_BIT); 
//            MatrixState.setProjectFrustum(-ratio, ratio, -1, 1, 2.5f, 1000);           
//            MatrixState.setCamera(cx,cy,cz,tx,ty,tz,upX,upY,upZ);
//            MatrixState.copyMVMatrix();
//            //��ʼ����Դλ��
//            MatrixState.setLightLocation(cx, cy+5, cz+3);            
//
//            
//            //����͸��Բ��,�������
//            GLES20.glEnable(GLES20.GL_BLEND);//�������
//            GLES20.glBlendFunc(GLES20.GL_SRC_ALPHA, GLES20.GL_ONE_MINUS_SRC_ALPHA);//���û������
//            displayStation.drawTransparentCircle();
//            GLES20.glDisable(GLES20.GL_BLEND); //�رջ��            
//            
//            //��������
//            drawAntenna();
//            //��������
//            //drawRomeColumn();
//            //�����ĸ�����ť
//            drawButton();
//        }  
//	
//}