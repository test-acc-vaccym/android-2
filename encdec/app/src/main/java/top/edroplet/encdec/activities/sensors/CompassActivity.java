package top.edroplet.encdec.activities.sensors;

import android.app.Activity;
import android.content.*;
import android.graphics.*;
import android.graphics.drawable.*;
import android.hardware.*;
import android.os.*;
import android.view.*;
import android.view.animation.*;
import android.widget.*;
import top.edroplet.encdec.*;
import top.edroplet.encdec.utils.util.*;

/**
 * CompassActivity
 *  通过通过手势也可以缩放图片    左--->右 放大 右 --->左 缩小 速度越快，缩放比例越大
 */
public class CompassActivity extends Activity
{
    ImageView imageView;
    ImageOperator imageOperator;

    // 初始化图片资源
    Bitmap bitmap;
    // 定义图片的高和宽
    int width, height;
    // 记录当前的缩放比
    float currentScale = 1;
    // 控制图片缩放的Matrix对象
    Matrix matrix;

    /** 传感器管理器 */
    private SensorManager manager;
    private SensorEventListener listener;
    Sensor orienten;

    @Override
    protected void onCreate(Bundle savedInstanceState)
	{
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_compass);


        imageView = (ImageView) findViewById(R.id.activity_compass_iv);
        imageView.setKeepScreenOn(true);//屏幕高亮
        //获取系统服务（SENSOR_SERVICE)返回一个SensorManager 对象
        manager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);

        /**
         *  获取方向传感器
         *  通过SensorManager对象获取相应的Sensor类型的对象
         */
        orienten = manager.getDefaultSensor(Sensor.TYPE_ORIENTATION);
		
		listener = new SensorListener();
        // manager.registerListener(new SensorListener(), orienten, SensorManager.SENSOR_DELAY_NORMAL);


        // imageView.setImageBitmap(ImageOperator.ReadBitmapById(this, R.drawable.compass_pointer));
        // imageView.setOnTouchListener(new TouchListener());
        if (false)
		{
			matrix = new Matrix();
			// 获取被缩放的源图片
			bitmap = BitmapFactory.decodeResource(this.getResources(), R.drawable.compass_pointer_7x120);

            // 获得位图的宽
            width = bitmap.getWidth();
            // 获得位图的高
            height = bitmap.getHeight();
            // 设置 ImageView初始化显示的图片
            imageView.setImageBitmap(BitmapFactory.decodeResource(this.getResources(), R.drawable.compassnorth_all));
        }
    }

    @Override
    protected void onResume()
	{
        //应用在前台时候注册监听器
        manager.registerListener(listener, orienten, SensorManager.SENSOR_DELAY_GAME);
        super.onResume();
    }

    @Override
    protected void onPause()
	{
        //应用不在前台时候销毁掉监听器
        manager.unregisterListener(listener);
        super.onPause();
    }

    private final class SensorListener implements SensorEventListener
	{
        private float predegree = 0;

        @Override
        public void onSensorChanged(SensorEvent event)
		{
			if (event.sensor.getType() == Sensor.TYPE_ORIENTATION)
			{
				/**
				 *  values[0]: x-axis 方向加速度
				 　　 values[1]: y-axis 方向加速度
				 　　 values[2]: z-axis 方向加速度
				 */
				float degree = event.values[0];// 存放了方向值
				if (Math.abs(degree - predegree) > 0.5)
				{
					/**动画效果*/
					RotateAnimation animation = new RotateAnimation(predegree, degree,
																	Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
					animation.setDuration(200);
					imageView.startAnimation(animation);
					predegree = -degree;

					/**
					 float x=event.values[SensorManager.DATA_X];
					 float y=event.values[SensorManager.DATA_Y];
					 float z=event.values[SensorManager.DATA_Z];
					 Log.i("XYZ", "x="+(int)x+",y="+(int)y+",z="+(int)z);
					 */
				}
			}
        }

        @Override
        public void onAccuracyChanged(Sensor sensor, int accuracy)
		{

        }

    }

    class GestureListener implements GestureDetector.OnGestureListener
	{

        @Override
        public boolean onDown(MotionEvent e)
		{
            // TODO Auto-generated method stub
            return false;
        }

        @Override
        public void onShowPress(MotionEvent e)
		{
            // TODO Auto-generated method stub

        }

        @Override
        public boolean onSingleTapUp(MotionEvent e)
		{
            return false;
        }

        @Override
        public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX,
								float distanceY)
		{
            return false;
        }

        @Override
        public void onLongPress(MotionEvent e)
		{

        }

        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
							   float velocityY)
		{
            velocityX = velocityX > 4000 ? 4000 : velocityX;
            velocityY = velocityY < -4000 ? -4000 : velocityY;
            // 感觉手势的速度来计算缩放比，如果 velocityX>0,放大图像，否则缩小图像
            currentScale += currentScale * velocityX / 4000.0f;
            // 保证 currentScale 不会等于0
            currentScale = currentScale > 0.01 ? currentScale : 0.01f;
            // 重置 Matrix
            matrix.setScale(currentScale, currentScale, 160, 200);
            BitmapDrawable tmp = (BitmapDrawable) imageView.getDrawable();
            // 如果图片还未回收，先强制收回该图片
            if (!tmp.getBitmap().isRecycled())
			{
                tmp.getBitmap().recycle();
            }
            // 根据原始位图和 Matrix创建新图片
            Bitmap bitmap2 = Bitmap.createBitmap(bitmap, 0, 0, width, height,
												 matrix, true);
            //显示新的位图
            imageView.setImageBitmap(bitmap2);
            return true;
        }
    }


    private final class TouchListener implements View.OnTouchListener
	{

        /** 记录是拖拉照片模式还是放大缩小照片模式 */
        private int mode = 0;// 初始状态
        /** 拖拉照片模式 */
        private static final int MODE_DRAG = 1;
        /** 放大缩小照片模式 */
        private static final int MODE_ZOOM = 2;

        /** 用于记录开始时候的坐标位置 */
        private PointF startPoint = new PointF();
        /** 用于记录拖拉图片移动的坐标位置 */
        private Matrix matrix = new Matrix();
        /** 用于记录图片要进行拖拉时候的坐标位置 */
        private Matrix currentMatrix = new Matrix();

        /** 两个手指的开始距离 */
        private float startDis;
        /** 两个手指的中间点 */
        private PointF midPoint;

        @Override
        public boolean onTouch(View v, MotionEvent event)
		{
            /** 通过与运算保留最后八位 MotionEvent.ACTION_MASK = 255 */
            switch (event.getAction() & MotionEvent.ACTION_MASK)
			{
					// 手指压下屏幕
                case MotionEvent.ACTION_DOWN:
                    mode = MODE_DRAG;
                    // 记录ImageView当前的移动位置
                    currentMatrix.set(imageView.getImageMatrix());
                    startPoint.set(event.getX(), event.getY());
                    break;
					// 手指在屏幕上移动，改事件会被不断触发
                case MotionEvent.ACTION_MOVE:
                    // 拖拉图片
                    if (mode == MODE_DRAG)
					{
                        float dx = event.getX() - startPoint.x; // 得到x轴的移动距离
                        float dy = event.getY() - startPoint.y; // 得到x轴的移动距离
                        // 在没有移动之前的位置上进行移动
                        matrix.set(currentMatrix);
                        matrix.postTranslate(dx, dy);
                    }
                    // 放大缩小图片
                    else if (mode == MODE_ZOOM)
					{
                        float endDis = distance(event);// 结束距离
                        if (endDis > 10f)
						{ // 两个手指并拢在一起的时候像素大于10
                            float scale = endDis / startDis;// 得到缩放倍数
                            matrix.set(currentMatrix);
                            matrix.postScale(scale, scale, midPoint.x, midPoint.y);
                        }
                    }
                    break;
					// 手指离开屏幕
                case MotionEvent.ACTION_UP:
                    // 当触点离开屏幕，但是屏幕上还有触点(手指)
                case MotionEvent.ACTION_POINTER_UP:
                    mode = 0;
                    break;
					// 当屏幕上已经有触点(手指)，再有一个触点压下屏幕
                case MotionEvent.ACTION_POINTER_DOWN:
                    mode = MODE_ZOOM;
                    /** 计算两个手指间的距离 */
                    startDis = distance(event);
                    /** 计算两个手指间的中间点 */
                    if (startDis > 10f)
					{ // 两个手指并拢在一起的时候像素大于10
                        midPoint = mid(event);
                        //记录当前ImageView的缩放倍数
                        currentMatrix.set(imageView.getImageMatrix());
                    }
                    break;
            }
            imageView.setImageMatrix(matrix);
            return true;
        }

        /** 计算两个手指间的距离 */
        private float distance(MotionEvent event)
		{
            float dx = event.getX(1) - event.getX(0);
            float dy = event.getY(1) - event.getY(0);
            /** 使用勾股定理返回两点之间的距离 */
            return (float)Math.sqrt(dx * dx + dy * dy);
        }

        /** 计算两个手指间的中间点 */
        private PointF mid(MotionEvent event)
		{
            float midX = (event.getX(1) + event.getX(0)) / 2;
            float midY = (event.getY(1) + event.getY(0)) / 2;
            return new PointF(midX, midY);
        }

    }


}
