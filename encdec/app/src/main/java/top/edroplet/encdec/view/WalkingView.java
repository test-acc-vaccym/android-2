package top.edroplet.encdec.view;

import android.content.Context;
import android.content.res.Resources;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.Paint.Align;
import android.graphics.Point;
import android.util.AttributeSet;
import android.view.View;

import java.util.ArrayList;

import top.edroplet.encdec.R;
import top.edroplet.encdec.utils.util.ImageOperator;
import top.edroplet.encdec.utils.util.ImageOperator.ImagePiece;
import top.edroplet.encdec.activities.sensors.StepCounterActivity;
import top.edroplet.encdec.utils.data.StepCounterSQLiteHelper;

/**
 * Created by xw on 2017/5/2.
 */

public class WalkingView extends View {
    public ArrayList<String> stepsInWeek=null;//存历史数据
    public int stepsToday=0; //记录今天走的步数
    int gapY = 8; //屏幕最上面留出的空隙
    int distY = 10; //每一条的间距
    int cellHeight = 30; //每一条的高度
    float STEP_MAX = 10000.0f; //每天最大的步数
    int maxStepWidth = 280; //最大步数在屏幕中宽度

    Bitmap [] sprite; //运动小人的图片数组
    Bitmap [] digit;  // 数字图片数组
    Bitmap back_cell; //颜色渐变条

    public boolean isMoving = false;
    int frameIndex; //记录运动小人的帧索引
    StepCounterSQLiteHelper schelper; //操作数据库的辅助类
    SQLiteDatabase db; //数据库操作对象


    ImageOperator is;
    int screenWidth = 0;
    int getScreenHeight = 0;

    public WalkingView(Context context, AttributeSet attributeSet){
        super(context, attributeSet);
        sprite = new Bitmap[9];
        digit = new Bitmap[10];

        is = new ImageOperator();
        Point p = is.GetScreenSize(context);
        screenWidth = p.x;
        getScreenHeight = p.y;

        //初始化图片
        Resources res = getResources();
        Bitmap sports = BitmapFactory.decodeResource(res, R.drawable.sports);
        for (ImagePiece ip : is.split(sports, 3, 3)) {
            // 存储缩放后的照片
            sprite[ip.index] = is.scale(ip.bitmap, 250, 250);
        }

        Bitmap numberBitmap = BitmapFactory.decodeResource(res, R.drawable.number);
        for (ImagePiece ip : is.split(numberBitmap, 5, 2)) {
            int index = ip.index;
            if (index + 1 >= 10){
                index = -1;
            }
            // 缩放为32*32
            digit[index + 1] = is.scale(ip.bitmap, 50,50);

        }

        back_cell = BitmapFactory.decodeResource(res, R.drawable.back_cell);
        //获取数据库中最近7 天内的数据
        schelper = new StepCounterSQLiteHelper(context, StepCounterActivity.DB_NAME, null, 1);
        stepsInWeek = getSQLData("7");
    }

    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        drawPrevious(canvas);//画以前走过的步数
        drawToday(canvas); //画今天走过的步数
    }

    // 画今天走的步数
    private void drawToday(Canvas canvas) {
        Paint paint = new Paint();
        paint.setColor(Color.CYAN);

        float strokewidth = paint.getStrokeWidth();

        Style s = paint.getStyle();
        paint.setStyle(Style.STROKE);
        paint.setStrokeWidth(2.0f);

        canvas.drawLine(0, 10, screenWidth, 10, paint);
        paint.setStyle(s);
        paint.setStrokeWidth(strokewidth);//恢复画笔

        //把当前步数换算为在屏幕上绘制的条宽度
        int width = (int)(stepsToday/STEP_MAX*280);
        canvas.drawBitmap(back_cell, 0, 20, paint);
        paint.setColor(Color.TRANSPARENT);
        canvas.drawRect(width, 22, screenWidth, screenWidth+cellHeight, paint);

        //画出遮罩层
        if(isMoving){
            //如果在运动，就切换帧序列
            canvas.drawBitmap(sprite[(++frameIndex)%5], width+20, 25, paint);
            isMoving = false;
        } else{
            //如果没在走步，就绘制静止的那张图片
            canvas.drawBitmap(sprite[4], width + 20, 25, paint);
        }
        drawDigit(canvas,width); //绘制数字
    }

    // 画之前走过的步数
    private void drawPrevious(Canvas canvas) {
        Paint paint = new Paint();

        for(int i=0;i<stepsInWeek.size();i++){
            String os = stepsInWeek.get(i);
            int s = (Integer.valueOf(os)).intValue();
            int width = (int) (s/STEP_MAX * maxStepWidth); //求出指定的步数在统计条中占得宽度
            int tempY = (cellHeight+distY)*i;
            canvas.drawBitmap(back_cell, 0, (cellHeight + distY)*i, paint); //画出渐变条

            paint.setColor(Color.BLACK);

            canvas.drawRect(width, tempY, screenWidth, tempY + cellHeight, paint);

            paint.setTextAlign(Align.LEFT);
            paint.setColor(Color.CYAN);
            paint.setAntiAlias(true);
            canvas.drawText("走了"  +stepsInWeek.get(i)+"步", width, tempY + cellHeight/2, paint);
        }
    }

    // 从数据库中获取历史数据
    public ArrayList<String> getSQLData(String limit){
        //获得SQLiteDatabase 对象
        db = schelper.getReadableDatabase();

        String [] cols = {StepCounterSQLiteHelper.ID, StepCounterSQLiteHelper.STEP};

        Cursor c = db.query (StepCounterSQLiteHelper.TABLE_NAME, cols, null, null, null, null, StepCounterSQLiteHelper.ID+" DESC",limit);

        ArrayList<String> al = new ArrayList<String>();

        for(c.moveToFirst();! (c.isAfterLast());c.moveToNext()){
            al.add(c.getString(1));
        }
        c.close();
        db.close();
        return al;
    }

    //将数字通过数字图片绘制到屏幕上
    public void drawDigit(Canvas canvas,int width){
        String sStep = ""+stepsToday;
        int l = sStep.length();
        for(int i=0;i<l;i++){
            int index = sStep.charAt(i) - '0';
            canvas.drawBitmap(digit[index], width+20+40+32*i, 0, null);//绘制数字图片
        }
    }
}
