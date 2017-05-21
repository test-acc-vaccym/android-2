package top.edroplet.encdec.view;
import android.animation.*;
import android.content.*;
import android.content.res.*;
import android.graphics.*;
import android.util.*;
import android.view.*;
import top.edroplet.encdec.*;
import top.edroplet.encdec.utils.util.*;

public class InstrumentPilot extends View
{
	private Context context;
	private Paint paint,paint_2,paint_3,paint_4;
	private int maxNum,startAngle,
	sweepAngle,sweepInWidth,sweepOutWidth,
	mWidth,mHeight,
	radius;

	private static int currentNum;
	
	private int[] indicatorColor = {0xffffffff,0x00ffffff,0x99ffffff,0xffffffff};
	
	public InstrumentPilot(Context context){
		super(context);
		this.context = context;
        initPaint();
	}
	
	public InstrumentPilot(Context context, AttributeSet attrs){
		super(context, attrs);
		this.context = context;
		initAttr(attrs);
        initPaint();
	}
	
	public InstrumentPilot(final Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;
        setBackgroundColor(0xFFFF6347);
        initAttr(attrs);
        initPaint();
    }
	
	public void setMaxNum(int num){
		this.maxNum = num;
	}
	
	private void initAttr(AttributeSet attrs) {
		TypedArray array = context.obtainStyledAttributes(attrs,R.styleable.RoundIndicatorView);
		maxNum = array.getInt(R.styleable.RoundIndicatorView_maxNum,500);
		startAngle = array.getInt(R.styleable.RoundIndicatorView_startAngle,160);
		sweepAngle = array.getInt(R.styleable.RoundIndicatorView_sweepAngle,220);
		//内外圆弧的宽度
		sweepInWidth = ImageOperator.dpToPx(8f,this.context); 
		sweepOutWidth = ImageOperator.dpToPx(3f,this.context); 
		array.recycle();
	}

	private void initPaint() {
		paint = new Paint(Paint.ANTI_ALIAS_FLAG);
		paint.setDither(true);
		paint.setStyle(Paint.Style.STROKE);
		paint.setColor(0xffffffff);
		paint_2 = new Paint(Paint.ANTI_ALIAS_FLAG);
		paint_3 = new Paint(Paint.ANTI_ALIAS_FLAG);
		paint_4 = new Paint(Paint.ANTI_ALIAS_FLAG);
	}

	@Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
    }
	
	@Override
	protected void onDraw(Canvas canvas)
	{
		// TODO: Implement this method
		super.onDraw(canvas);
		radius = getMeasuredWidth()/4; //不要在构造方法里初始化，那时还没测量宽高
		canvas.save();
		canvas.translate(mWidth/2,(mWidth)/2);
		drawRound(canvas);  //画内外圆弧
		drawScale(canvas);//画刻度
		drawIndicator(canvas); //画当前进度值
		drawCenterText(canvas);//画中间的文字
		canvas.restore();
	}
	
	@Override

	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

		super.onMeasure(widthMeasureSpec, heightMeasureSpec);

		int wSize = MeasureSpec.getSize(widthMeasureSpec);

		int wMode = MeasureSpec.getMode(widthMeasureSpec);

		int hSize = MeasureSpec.getSize(heightMeasureSpec);

		int hMode = MeasureSpec.getMode(heightMeasureSpec);



		if (wMode == MeasureSpec.EXACTLY ){

			mWidth = wSize;

		}else {

			mWidth = ImageOperator.dpToPx(300f,this.context);

		}

		if (hMode == MeasureSpec.EXACTLY ){

			mHeight= hSize;

		}else {

			mHeight =ImageOperator.dpToPx(400f, this.context);

		}

		setMeasuredDimension(mWidth,mHeight);

	}
	
	/**
	 * drawRound()：这个很简单，内外圆弧所需的属性都已经定义好了，画笔是白色的，我们通过setAlpha()设置一下它的透明度，范围是00~ff。
	*/
	private void drawRound(Canvas canvas) {
		canvas.save();
		//内圆
		paint.setAlpha(0x40);
		paint.setStrokeWidth(sweepInWidth);
		RectF rectf = new RectF(-radius,-radius,radius,radius);
		canvas.drawArc(rectf,startAngle,sweepAngle,false,paint);
		//外圆
		paint.setStrokeWidth(sweepOutWidth);
		int w = ImageOperator.dpToPx(10f, this.context);
		RectF rectf2 = new RectF(-radius-w , -radius-w , radius+w , radius+w);
		canvas.drawArc(rectf2,startAngle,sweepAngle,false,paint);
		canvas.restore();
	}
	
	/**
	 * drawScale()：如果你看过几篇自定义view文章，应该都知道了靠旋转画布来画刻度和文字的套路了，调用canvas.rotate就可以旋转画布，负数代表顺时针，这里我们打算把起始位置旋转到原点正上方，即270度的地方，这样画刻度和文字的坐标就很好计算了，每画完一次让画布逆时针转一个刻度间隔，一直循环到画完。我们观察一下原图，粗的刻度线一共有6条，数字的刻度是再粗刻度线下面的，每两个粗刻度线之间有5条细刻度线，并且中间那条细刻度线下方有对应文字。我们把扫过的角度除以30，就是每个刻度的间隔了，然后通过判断就可以画对应刻度和文字了。

	 关于获取文字的宽高，有两种方法，一种是paint.measureText(text)测量文字宽度，返回值类型是float，但是得不到高度。另一种是Rect rect = new Rect();paint.getTextBounds(text,0,text.length(),rect); 将文字的属性放入rect里，不过是int值，我们画的文字够小的了，所以最好用第一种，除非需要高度值。

	 另外，我发现绘制文字时，坐标值代表的是文字的左下角，不同于一般的从左上角，所以canvas.drawText传入的xy坐标是text的左下角坐标
	*/
	
	private String[] text ={"较差","中等","良好","优秀","极好"};
	private void drawScale(Canvas canvas) {
		canvas.save();
		float angle = (float)sweepAngle/30;//刻度间隔
		canvas.rotate(-270+startAngle); //将起始刻度点旋转到正上方（270)
		for (int i = 0; i <= 30; i++) {
			if(i%6 == 0){   //画粗刻度和刻度值
				paint.setStrokeWidth(ImageOperator.dpToPx(2f,context));
				paint.setAlpha(0x70);
				canvas.drawLine(0, -radius-sweepInWidth/2,0, -radius+sweepInWidth/2+dp2px(1), paint);
				drawText(canvas,i*maxNum/30+"",paint);
			}else {         //画细刻度
				paint.setStrokeWidth(ImageOperator.dpToPx(1f,context));
				paint.setAlpha(0x50);
				canvas.drawLine(0,-radius-sweepInWidth/2,0, -radius+sweepInWidth/2, paint);
			}
			if(i==3 || i==9 || i==15 || i==21 || i==27){  //画刻度区间文字
				paint.setStrokeWidth(ImageOperator.dpToPx(2f,context));
				paint.setAlpha(0x50);
				drawText(canvas,text[(i-3)/6], paint);
			}
			canvas.rotate(angle); //逆时针
		}
		canvas.restore();
	}
	
	private void drawText(Canvas canvas ,String text ,Paint paint) {
        paint.setStyle(Paint.Style.FILL);
        paint.setTextSize(sp2px(8));
        float width = paint.measureText(text); //相比getTextBounds来说，这个方法获得的类型是float，更精确些
//        Rect rect = new Rect();
//        paint.getTextBounds(text,0,text.length(),rect);
        canvas.drawText(text,-width/2 , -radius + ImageOperator.dpToPx(15, this.context),paint);
        paint.setStyle(Paint.Style.STROKE);
    }
	
	/**
	 * drawIndicator:这一步是画外圆弧上的进度值，观察原图，发现有三个问题需要解决：表示进度的弧度值和小圆点的坐标怎么计算，进度值的透明度渐变怎么实现？小圆点像光源一样边缘模糊的效果怎么实现？

	 对于坐标计算，其实也较简单，将当前值比上最大值，得到一个比例就可以计算进度条扫过的弧度，小圆点呢绘制与进度条的尾端，角度已经有了（起始角度+扫过的角度），用三角函数就可以算了。

	 对于颜色渐变，可以用paint的shader渲染，它有5个子类
	 BitmapShader位图

	 LinearGradient线性渐变

	 RadialGradient光束渐变

	 SweepGradient梯度渐变

	 ComposeShader混合渐变


	 我们使用梯度渐变来实现，传入坐标和一个颜色数组就可以实现对颜色的梯度渐变，这里我们对颜色的修改当然只是修改它的透明度，我们知道32位的颜色值前8位就是表示透明度的。

	 对于小圆点有光源一样的边缘模糊效果，我用的是paint的setMaskFilter，其中有一个子类BlurMaskFilter可以实现边缘模糊效果~（ 不知道有没有什么别的方法实现这种效果）
	*/
	/*
	 记得关闭硬件加速，就是加一句<activity android:hardwareAccelerated="false" >
	*/
	
	private void drawIndicator(Canvas canvas) {
		canvas.save();
		paint_2.setStyle(Paint.Style.STROKE);
		int sweep;
		if(currentNum<=maxNum){
			sweep = (int)((float)currentNum/(float)maxNum*sweepAngle);
		}else {
			sweep = sweepAngle;
		}
		paint_2.setStrokeWidth(sweepOutWidth);
		Shader shader =new SweepGradient(0,0,indicatorColor,null);
		paint_2.setShader(shader);
		int w = dp2px(10);
		RectF rectf = new RectF(-radius-w , -radius-w , radius+w , radius+w);
		canvas.drawArc(rectf,startAngle,sweep,false,paint_2);
		float x = (float) ((radius+dp2px(10))*Math.cos(Math.toRadians(startAngle+sweep)));
		float y = (float) ((radius+dp2px(10))*Math.sin(Math.toRadians(startAngle+sweep)));
		paint_3.setStyle(Paint.Style.FILL);
		paint_3.setColor(0xffffffff);
		paint_3.setMaskFilter(new BlurMaskFilter(dp2px(3), BlurMaskFilter.Blur.SOLID)); //需关闭硬件加速
		canvas.drawCircle(x,y,dp2px(3),paint_3);
		canvas.restore();
	}
	
	/*
	 * drawCenterText：这步简单，注意刚才说的绘制文字时从左下角开始的和两种测量文字宽度的区别就好。
	*/
	private void drawCenterText(Canvas canvas) {
		canvas.save();
		paint_4.setStyle(Paint.Style.FILL);
		paint_4.setTextSize(radius/2);
		paint_4.setColor(0xffffffff);
		canvas.drawText(currentNum+"",-paint_4.measureText(currentNum+"")/2,0,paint_4);
		paint_4.setTextSize(radius/4);
		String content = "信用";
		if(currentNum < maxNum*1/5){
			content += text[0];
		}else if(currentNum >= maxNum*1/5 && currentNum < maxNum*2/5){
			content += text[1];
		}else if(currentNum >= maxNum*2/5 && currentNum < maxNum*3/5){
			content += text[2];
		}else if(currentNum >= maxNum*3/5 && currentNum < maxNum*4/5){
			content += text[3];
		}else if(currentNum >= maxNum*4/5){
			content += text[4];
		}
		Rect r = new Rect();
		paint_4.getTextBounds(content,0,content.length(),r);
		canvas.drawText(content,-r.width()/2,r.height()+20,paint_4);
		canvas.restore();
	}
	
	/**
	 * 接下来要实现的是当改变值时的动画效果，同时改变背景颜色。

	 setCurrentNumAnim就是供用户调用的。我们可以通过属性动画来改变当前值，注意要给当前值（currentNum）加上setter和getter，因为属性动画内部需要调用它们。

	 对于动画的时间，简单写个计算公式就好，然后监听动画过程，在里面实现背景颜色的改变。怎么才能像支付宝芝麻信用那样红橙黄绿蓝的渐变呢？我按自己思路实现了一个可以三种颜色之间渐变的效果。

	 大家学习属性动画时应该了解过插值器估值器的作用，我就是用ArgbEvaluator估值器实现颜色渐变的，调用它的evaluate方法，传入一个0~1的比例，传入开始和结束的颜色，就可以根据当前比例得到介于这两个颜色之间的颜色值。

	 这里我实现了红到橙再到蓝的渐变，假设最大值是500，那么当前值x从0~250的过程中是从红到橙，x/(500/2)就可以得到一个0~1的变化比例，当前值从250~500的过程是橙到蓝，也需要一个0~1的变化过程的比例，计算方法就是（x-250）/(250)  其中250就是（500/2）得来的。按照这样的思路当然可以实现更多颜色之间的渐变，就是想办法在各区间里算出一个0~1的比例值就行。注意数据类型转换，上代码！
	*/
	public int getCurrentNum() {
		return currentNum;
	}
	public void setCurrentNum(int currentNum) {
		this.currentNum = currentNum;
		invalidate();
	}
	public void setCurrentNumAnim(int num) {
		float duration = (float)Math.abs(num-currentNum)/maxNum *1500+500; //根据进度差计算动画时间
		ObjectAnimator anim = ObjectAnimator.ofInt(this,"currentNum",num);
		anim.setDuration((long) Math.min(duration,2000));
		anim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
				@Override
				public void onAnimationUpdate(ValueAnimator animation) {
					int value = (int) animation.getAnimatedValue();
					int color = calculateColor(value);
					setBackgroundColor(color);
				}
			});
		anim.start();
	}
	private int calculateColor(int value){
		ArgbEvaluator evealuator = new ArgbEvaluator();
		float fraction = 0;
		int color = 0;
		if(value <= maxNum/2){
			fraction = (float)value/(maxNum/2);
			color = (int) evealuator.evaluate(fraction,0xFFFF6347,0xFFFF8C00); //由红到橙
		}else {
			fraction = ( (float)value-maxNum/2 ) / (maxNum/2);
			color = (int) evealuator.evaluate(fraction,0xFFFF8C00,0xFF00CED1); //由橙到蓝
		}
		return color;
	}
	
	private int dp2px(int dp){
		return ImageOperator.dpToPx((float)dp, this.context);
	}
	
	private int sp2px(int sp){
		return ImageOperator.spTopx(sp,this.context);
	}
}
