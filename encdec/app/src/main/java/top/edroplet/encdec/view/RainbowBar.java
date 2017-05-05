package top.edroplet.encdec.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

import top.edroplet.encdec.R;
import top.edroplet.encdec.utils.util.ImageOperator;
import top.edroplet.encdec.utils.util.Utils;
import java.util.*;

/**
 * http://m.blog.csdn.net/article/details?id=53418940
 * http://www.jianshu.com/p/84cee705b0d3
 * Android所有的控件都是View或者View的子类，它其实表示的就是屏幕上的一块矩形区域，用一个Rect来表示，
 * left，top表示View相对于它的parent View的起点，width，height表示View自己的宽高，
 * 通过这4个字段就能确定View在屏幕上的位置，确定位置后就可以开始绘制View的内容了。
 * Created by xw on 2017/5/3.
 * View绘制过程
 * View的绘制可以分为下面三个过程：
 * Measure
 * View会先做一次测量，算出自己需要占用多大的面积。View的Measure过程给我们暴露了一个接口onMeasure，方法的定义是这样的
 * protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {}
 * View类已经提供了一个基本的onMeasure实现，其中invoke了setMeasuredDimension()方法，
 * 设置了measure过程中View的宽高，getSuggestedMinimumWidth()返回View的最小Width，Height也有对应的方法。
 * 插几句，MeasureSpec类是View类的一个内部静态类，它定义了三个常量UNSPECIFIED、AT_MOST、EXACTLY，
 * 其实我们可以这样理解它，它们分别对应LayoutParams中match_parent、wrap_content、xxxdp。
 * 我们可以重写onMeasure来重新定义View的宽高。
 * Layout
 * Layout过程对于View类非常简单，同样View给我们暴露了onLayout方法
 * protected void onLayout(boolean changed, int left, int top, int right, int bottom) { }
 * 因为我们现在讨论的是View，没有子View需要排列，所以这一步其实我们不需要做额外的工作。
 * 插一句，对ViewGroup类，onLayout方法中，我们需要将所有子View的大小宽高设置好，这个我们下一篇会详细说。
 * Draw
 * Draw过程，就是在canvas上画出我们需要的View样式。同样View给我们暴露了onDraw方法
 * protected void onDraw(Canvas canvas) {}
 * 默认View类的onDraw没有一行代码，但是提供给我们了一张空白的画布，举个例子，就像一张画卷一样，
 * 我们就是画家，能画出什么样的效果，完全取决我们。
 * View中还有三个比较重要的方法
 * requestLayout
 View重新调用一次layout过程。
 * invalidate
 View重新调用一次draw过程
 * forceLayout
 标识View在下一次重绘，需要重新调用layout过程。
 * 自定义属性
 * 整个View的绘制流程我们已经介绍完了，还有一个很重要的知识，自定义控件属性，我们都知道View已经有一些基本的属性，
 * 比如layout_width，layout_height，background等，我们往往需要定义自己的属性，那么具体可以这么做。
    1.在values文件夹下，打开attrs.xml，其实这个文件名称可以是任意的，写在这里更规范一点，表示里面放的全是view的属性。
    2.因为我们下面的实例会用到2个长度，一个颜色值的属性，所以我们这里先创建3个属性。
 */

public class RainbowBar extends View {
    //progress bar color
    int []barColor = {
		Color.parseColor("#1E08E5"), 
		Color.parseColor("#3e080f"),
		Color.parseColor("#5e0801"),
		Color.parseColor("#7e0802"),
		Color.parseColor("#9e0803"),
		Color.parseColor("#ae0804"),
		Color.parseColor("#ce080f"),
		Color.parseColor("#ee081d"),
		Color.parseColor("#ee281d"),
		Color.parseColor("#ee481d"),
		Color.parseColor("#ee681d"),
		Color.parseColor("#ee881d"),
		Color.parseColor("#eea81d"),
		Color.parseColor("#eec81d"),
		Color.parseColor("#eee81d"),
		Color.parseColor("#eee83d"),
		Color.parseColor("#eee85d"),
		Color.parseColor("#eee87d"),
		Color.parseColor("#eee89d"),
		Color.parseColor("#eee8bd"),
		Color.parseColor("#eee8dd"),
		Color.parseColor("#eee8ff"),
	};
    //every bar segment width
    int hSpace = ImageOperator.dpToPx(80, getContext());
    //every bar segment height
    int vSpace = ImageOperator.dpToPx(4, getContext());
    //space among bars
    int space = ImageOperator.dpToPx(10, getContext());
    float startX = 0;
    float delta = 2f; // 控制速度
    Paint mPaint;
    int index = 0;
	Random ran = new Random(100);

    /**
     * 第一个方法，一般我们这样使用时会被调用，View view = new View(context);
     * @param context
     */
    public RainbowBar(Context context) {
        super(context);
    }

    /**
     * 第二个方法，当我们在xml布局文件中使用View时，会在inflate布局时被调用，
     * <View layout_width="match_parent" layout_height="match_parent"/>。
     * @param context
     * @param attrs
     */
    public RainbowBar(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    /**
     * 第三个方法，跟第二种类似，但是增加style属性设置，这时inflater布局时会调用第三个构造方法。
     * <View style="@styles/MyCustomStyle" layout_width="match_parent" layout_height="match_parent"/>。
     * @param context
     * @param attrs
     * @param defStyleAttr
     */
    public RainbowBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        //read custom attrs
        TypedArray t = context.obtainStyledAttributes(attrs,
                R.styleable.rainbowbar, 0, 0);
        hSpace = t.getDimensionPixelSize(R.styleable.rainbowbar_rainbowbar_hspace, hSpace);
        vSpace = t.getDimensionPixelOffset(R.styleable.rainbowbar_rainbowbar_vspace, vSpace);
		
		//int i = ran.nextInt(barColor.length);
		
        barColor[index] = t.getColor(R.styleable.rainbowbar_rainbowbar_color, barColor[index]);
        t.recycle();   // we should always recycle after used
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setColor(barColor[index]);
        mPaint.setStrokeWidth(vSpace);
    }

    /**
     * 因为我们这里不用关注measrue和layout过程，直接重写onDraw方法即可。
     * 其实就是调用canvas的drawLine方法，然后每次将draw的起点向前推进，在方法的结尾，我们调用了invalidate方法，
     * 上面我们已经说明了，这个方法会让View重新调用onDraw方法，所以就达到我们的进度条一直在向前绘制的效果。
     * 下面是最后的显示效果，制作成gif时好像有色差，但是真实效果是蓝色的。
     * 我们只写了短短的几十行代码，自定义View并不是我们想象中那么难，下一篇我们会继续ViewGroup的绘制流程学习。
     * @param canvas
     */
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
		
		// mPaint.setColor(barColor[ran.nextInt(barColor.length)]);
		
        //get screen width
        float sw = this.getMeasuredWidth();
        if (startX >= sw + (hSpace + space) - (sw % (hSpace + space))) {
            startX = 0;
        } else {
            startX += delta;
        }
		// mPaint.setColor(barColor[index++]);
        float start = startX;
		// 左边逐渐移出的过程
        // draw latter parse
        while (start < sw) {
            canvas.drawLine(start, 5, start + hSpace, 5, mPaint);
            start += (hSpace + space);
			mPaint.setColor(barColor[ran.nextInt(barColor.length)]);
        }

		
		//右边逐渐移入的过程
        start = startX - space - hSpace;

        // draw front parse
        while (start >= -hSpace) {
            canvas.drawLine(start, 5, start + hSpace, 5, mPaint);
            start -= (hSpace + space);
			// mPaint.setColor(barColor[ran.nextInt(barColor.length)]);
			// mPaint.setColor(barColor[index++]);
        }
        if (index >= barColor.length -1) {
            index = 0;
        }
		//index++;
        invalidate();
    }
}
