package top.edroplet.encdec.utils.util;

import android.content.*;
import android.graphics.*;
import android.graphics.Bitmap.*;
import android.graphics.PorterDuff.*;
import android.net.*;
import android.util.*;
import android.view.*;
import java.io.*;
import java.util.*;

import android.graphics.Bitmap.Config;

import static android.content.ContentValues.TAG;

/**
 * ImageOperator 操作图像
 * Created by xw on 2017/5/2.
 */

public class ImageOperator {

    /**
     * dpToPx  dp转换为px
     * @param context 上下文
     * @param dpValue dp值
     * @return px值
     */
    public static int dpToPx(float dpValue, Context context) {
        float scale=context.getResources().getDisplayMetrics().density;//获得当前屏幕密度
        return (int)(dpValue*scale+0.5f);
    }

    /**
     *  /px转换为dp
     * @param pxValue px值
     * @param context 上下文
     * @return dp值
     */
    public static int pxToDp(float pxValue, Context context) {
        float scale=context.getResources().getDisplayMetrics().density;//获得当前屏幕密度
        return (int)(pxValue/scale+0.5f);
    }

	public static int spTopx(int sp, Context context){
        return (int)TypedValue.applyDimension(
			TypedValue.COMPLEX_UNIT_SP,
			sp,
			context.getResources().getDisplayMetrics());
    }
	
	public static DisplayMetrics getScreenMetrics(Context context) {
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics dm = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(dm);
        return dm;
    }
	
    /**
     *
     * @param bitmap 原始图片
     * @param xPiece 横向切片数
     * @param yPiece 竖向切片数
     * @return ImagePiece的list
     *  方法split，传入的参数是要切割的Bitmap对象，和横向和竖向的切割片数。
     *  比如传入的是3、3，则横竖向都切割成3片，最终会将整个图片切割成3X3=9片
     */
    public List<ImagePiece> split(Bitmap bitmap, int xPiece, int yPiece) {
        List<ImagePiece> pieces = new ArrayList<>(xPiece * yPiece);
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        int pieceWidth = width / xPiece;
        int pieceHeight = height / yPiece;
//        Matrix matrix = new Matrix();
//        matrix.postScale((float)1/xPiece, (float)1/yPiece);
        Log.e(TAG, "split: pieceWidth: " + pieceWidth + ", pieceHeight: " + pieceHeight);
        for (int i = 0; i < yPiece; i++) {
            for (int j = 0; j < xPiece; j++) {
                ImagePiece piece = new ImagePiece();
                piece.index = j + i * xPiece;
                int xValue = j * pieceWidth;
                int yValue = i * pieceHeight;
                piece.bitmap = Bitmap.createBitmap(bitmap, xValue, yValue,
                        pieceWidth, pieceHeight);
//                piece.bitmap = Bitmap.createBitmap(bitmap, xValue, yValue, pieceWidth, pieceHeight,matrix,false);
                pieces.add(piece);
            }
        }
        if (!bitmap.isRecycled()) {
            bitmap.recycle();
        }
        return pieces;
    }

    /**
     * ImagePiece类，此类保存了一个Bitmap对象和一个标识图片的顺序索引的int变量
     */
    public class ImagePiece {
        public int index = 0;
        public Bitmap bitmap = null;
    }

    /**
     *
     * @param bm 原始图片
     * @param newWidth 新宽度
     * @param newHeight 新高度
     * @return 新的bitmap
     */
    public Bitmap scale(Bitmap bm, int newWidth, int newHeight) {
        // 获得图片的宽高
        int width = bm.getWidth();
        int height = bm.getHeight();

        // 计算缩放比例
        float scaleWidth = ((float) newWidth) / width;
        float scaleHeight = ((float) newHeight) / height;
        // 取得想要缩放的matrix参数
        Matrix matrix = new Matrix();
        matrix.postScale(scaleWidth, scaleHeight);
        // 得到新的图片
        return Bitmap.createBitmap(bm, 0, 0, width, height, matrix, true);
    }

    /**
     * 通过资源id转化成Bitmap
     *
     * @param context 上下文
     * @param resId 资源ID
     * @return bitmap
     */
    public static Bitmap ReadBitmapById(Context context, int resId)
    {
        BitmapFactory.Options opt = new BitmapFactory.Options();
        opt.inPreferredConfig = Bitmap.Config.RGB_565;
        // opt.inPurgeable = true;
        // opt.inInputShareable = true;
        InputStream is = context.getResources().openRawResource(resId);
        return BitmapFactory.decodeStream(is, null, opt);
    }

    /**
     * 缩放Bitmap满屏
     *
     * @param bitmap 原始图
     * @param screenWidth 屏幕宽
     * @param screenHight 屏幕高
     * @return bitmap
     */
    public static Bitmap getBitmap(Bitmap bitmap, int screenWidth, int screenHight)
    {
        int w = bitmap.getWidth();
        int h = bitmap.getHeight();
        Matrix matrix = new Matrix();
        float scale = (float) screenWidth / w;
        float scale2 = (float) screenHight / h;
        // scale = scale < scale2 ? scale : scale2;
        matrix.postScale(scale, scale);
        Bitmap bmp = Bitmap.createBitmap(bitmap, 0, 0, w, h, matrix, true);
        if (bitmap != null && !bitmap.equals(bmp) && !bitmap.isRecycled())
        {
            bitmap.recycle();
            bitmap = null;
        }
        // Bitmap.createBitmap(bitmap, 0, 0, w, h, matrix, true);
        return bmp;
    }

    /**
     * 按最大边按一定大小缩放图片
     * */
    public static Bitmap scaleImage(byte[] buffer, float size)
    {
        // 获取原图宽度
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        // options.inPurgeable = true;
        // options.inInputShareable = true;
        Bitmap bm;
        bm = BitmapFactory.decodeByteArray(buffer, 0, buffer.length, options);
        // 计算缩放比例
        float reSize = options.outWidth / size;
        if (options.outWidth < options.outHeight)
        {
            reSize = options.outHeight / size;
        }
        // 如果是小图则放大
        if (reSize <= 1)
        {
            int newWidth = 0;
            int newHeight = 0;
            if (options.outWidth > options.outHeight)
            {
                newWidth = (int) size;
                newHeight = options.outHeight * (int) size / options.outWidth;
            } else
            {
                newHeight = (int) size;
                newWidth = options.outWidth * (int) size / options.outHeight;
            }
            bm = BitmapFactory.decodeByteArray(buffer, 0, buffer.length);
            bm = scaleImage(bm, newWidth, newHeight);
            if (bm == null)
            {
                Log.e(TAG, "convertToThumb, decode fail:" + null);
                return null;
            }
            return bm;
        }
        // 缩放
        options.inJustDecodeBounds = false;
        options.inSampleSize = (int) reSize;
        bm = BitmapFactory.decodeByteArray(buffer, 0, buffer.length, options);
        if (bm == null)
        {
            Log.e(TAG, "convertToThumb, decode fail:" + null);
            return null;
        }
        return bm;
    }
    /**
     * 检查图片是否超过一定值，是则缩小
     *
     * @param buffer
     * @param size
     * @return bitmap
     */
    public static Bitmap convertToThumb(byte[] buffer, float size)
    {
        // 获取原图宽度
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        options.inPurgeable = true;
        options.inInputShareable = true;
        Bitmap bm = BitmapFactory.decodeByteArray(buffer, 0, buffer.length,
                options);
        // 计算缩放比例
        float reSize = options.outWidth / size;
        if (options.outWidth > options.outHeight)
        {
            reSize = options.outHeight / size;
        }
        if (reSize <= 0)
        {
            reSize = 1;
        }
        Log.d(TAG, "convertToThumb, reSize:" + reSize);
        // 缩放
        options.inJustDecodeBounds = false;
        options.inSampleSize = (int) reSize;
        if (bm != null && !bm.isRecycled())
        {
            bm.recycle();
            bm = null;
            Log.e(TAG, "convertToThumb, recyle");
        }
        bm = BitmapFactory.decodeByteArray(buffer, 0, buffer.length, options);
        if (bm == null)
        {
            Log.e(TAG, "convertToThumb, decode fail:" + null);
            return null;
        }
        return bm;
    }
    /**
     * Bitmap --> byte[]
     *
     * @param bmp bitmap
     * @return bytes
     */
    private static byte[] readBitmap(Bitmap bmp)
    {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.JPEG, 60, baos);
        try
        {
            baos.flush();
            baos.close();
        } catch (IOException e)
        {
            e.printStackTrace();
        }
        return baos.toByteArray();
    }
    /**
     * Bitmap --> byte[]
     *
     * @param buffer bytes
     * @param size 长度
     * @return bytes
     */
    public static byte[] readBitmapFromBuffer(byte[] buffer, float size)
    {
        return readBitmap(convertToThumb(buffer, size));
    }

    /**
     * 以屏幕宽度为基准，显示图片
     *
     * @param context 上下文
     * @param data 数据
     * @param size 大小
     * @return bitmap
     */
    public static Bitmap decodeStream(Context context, Intent data, float size)
    {
        Bitmap image = null;
        try
        {
            Uri dataUri = data.getData();
            // 获取原图宽度
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inJustDecodeBounds = true;
            options.inPurgeable = true;
            options.inInputShareable = true;
            BitmapFactory.decodeStream(context.getContentResolver()
                    .openInputStream(dataUri), null, options);
            // 计算缩放比例
            float reSize = (int) (options.outWidth / size);
            if (reSize <= 0)
            {
                reSize = 1;
            }
            Log.d(TAG, "old-w:" + options.outWidth + ", llyt-w:" + size
                    + ", resize:" + reSize);
            // 缩放
            options.inJustDecodeBounds = false;
            options.inSampleSize = (int) reSize;
            image = BitmapFactory.decodeStream(context.getContentResolver()
                    .openInputStream(dataUri), null, options);
        } catch (Exception e)
        {
            e.printStackTrace();
        }
        return image;
    }

    /**
     * 按新的宽高缩放图片
     *
     * @param bm  bitmap
     * @param newWidth 新宽度
     * @param newHeight 新高度
     * @return bitmap
     */
    public static Bitmap scaleImage(Bitmap bm, int newWidth, int newHeight)
    {
        if (bm == null)
        {
            return null;
        }
        int width = bm.getWidth();
        int height = bm.getHeight();
        float scaleWidth = ((float) newWidth) / width;
        float scaleHeight = ((float) newHeight) / height;
        Matrix matrix = new Matrix();
        matrix.postScale(scaleWidth, scaleHeight);
        Bitmap newbm = Bitmap.createBitmap(bm, 0, 0, width, height, matrix,
                true);
        if (bm != null & !bm.isRecycled())
        {
            bm.recycle();
            bm = null;
        }
        return newbm;
    }

    /**
     * fuction: 设置固定的宽度，高度随之变化，使图片不会变形
     *
     * @param target  需要转化bitmap参数
     * @param newWidth 设置新的宽度
     * @return bitmap
     */
    public static Bitmap fitBitmap(Bitmap target, int newWidth)
    {
        int width = target.getWidth();
        int height = target.getHeight();
        Matrix matrix = new Matrix();
        float scaleWidth = ((float) newWidth) / width;
        // float scaleHeight = ((float)newHeight) / height;
        int newHeight = (int) (scaleWidth * height);
        matrix.postScale(scaleWidth, scaleWidth);
        // Bitmap result = Bitmap.createBitmap(target,0,0,width,height,
        // matrix,true);
        Bitmap bmp = Bitmap.createBitmap(target, 0, 0, width, height, matrix,
                true);
        if (target != null && !target.equals(bmp) && !target.isRecycled())
        {
            target.recycle();
            target = null;
        }
        return bmp;// Bitmap.createBitmap(target, 0, 0, width, height, matrix,
        // true);
    }

    /**
     * 根据指定的宽度平铺图像
     *
     * @param width 宽度
     * @param src 源bitmap
     * @return bitmap
     */
    public static Bitmap createRepeater(int width, Bitmap src)
    {
        int count = (width + src.getWidth() - 1) / src.getWidth();
        Bitmap bitmap = Bitmap.createBitmap(width, src.getHeight(),
                Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        for (int idx = 0; idx < count; ++idx)
        {
            canvas.drawBitmap(src, idx * src.getWidth(), 0, null);
        }
        return bitmap;
    }

    /**
     * 图片的质量压缩方法
     *
     * @param image 元bitmap
     * @return 压缩后的bitmap
     */
    public static Bitmap compressImage(Bitmap image)
    {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        image.compress(Bitmap.CompressFormat.JPEG, 100, baos);// 质量压缩方法，这里100表示不压缩，把压缩后的数据存放到baos中
        int options = 100;
        while (baos.toByteArray().length / 1024 > 100)
        { // 循环判断如果压缩后图片是否大于100kb,大于继续压缩
            baos.reset();// 重置baos即清空baos
            image.compress(Bitmap.CompressFormat.JPEG, options, baos);// 这里压缩options%，把压缩后的数据存放到baos中
            options -= 10;// 每次都减少10
        }
        ByteArrayInputStream isBm = new ByteArrayInputStream(baos.toByteArray());// 把压缩后的数据baos存放到ByteArrayInputStream中
        Bitmap bitmap = BitmapFactory.decodeStream(isBm, null, null);// 把ByteArrayInputStream数据生成图片
        if (baos != null)
        {
            try
            {
                baos.close();
            } catch (IOException e)
            {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
        if (isBm != null)
        {
            try
            {
                isBm.close();
            } catch (IOException e)
            {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
        if (image != null && !image.isRecycled())
        {
            image.recycle();
            image = null;
        }
        return bitmap;
    }

    /**
     * 图片按比例大小压缩方法(根据Bitmap图片压缩)
     *
     * @param image
     * @return
     */
    public static Bitmap getImage(Bitmap image)
    {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        image.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        if (baos.toByteArray().length / 1024 > 1024)
        {// 判断如果图片大于1M,进行压缩避免在生成图片（BitmapFactory.decodeStream）时溢出
            baos.reset();// 重置baos即清空baos
            image.compress(Bitmap.CompressFormat.JPEG, 50, baos);// 这里压缩50%，把压缩后的数据存放到baos中
        }
        ByteArrayInputStream isBm = new ByteArrayInputStream(baos.toByteArray());
        BitmapFactory.Options newOpts = new BitmapFactory.Options();
        // 开始读入图片，此时把options.inJustDecodeBounds 设回true了
        newOpts.inJustDecodeBounds = true;
        Bitmap bitmap = BitmapFactory.decodeStream(isBm, null, newOpts);
        newOpts.inJustDecodeBounds = false;
        int w = newOpts.outWidth;
        int h = newOpts.outHeight;
        // 现在主流手机比较多是800*480分辨率，所以高和宽我们设置为
        float hh = 800f;// 这里设置高度为800f
        float ww = 480f;// 这里设置宽度为480f
        // 缩放比。由于是固定比例缩放，只用高或者宽其中一个数据进行计算即可
        int be = 1;// be=1表示不缩放
        if (w > h && w > ww)
        {// 如果宽度大的话根据宽度固定大小缩放
            be = (int) (newOpts.outWidth / ww);
        } else if (w < h && h > hh)
        {// 如果高度高的话根据宽度固定大小缩放
            be = (int) (newOpts.outHeight / hh);
        }
        if (be <= 0)
            be = 1;
        newOpts.inSampleSize = be;// 设置缩放比例
        // 重新读入图片，注意此时已经把options.inJustDecodeBounds 设回false了
        isBm = new ByteArrayInputStream(baos.toByteArray());
        bitmap = BitmapFactory.decodeStream(isBm, null, newOpts);
        if (isBm != null)
        {
            try
            {
                isBm.close();
            } catch (IOException e)
            {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
        if (image != null && !image.isRecycled())
        {
            image.recycle();
            image = null;
        }
        return compressImage(bitmap);// 压缩好比例大小后再进行质量压缩
    }

    /**
     * 通过资源id转化成Bitmap 全屏显示
     *
     * @param context 上下文
     * @param drawableId 资源ID
     * @param screenWidth 屏幕宽
     * @param screenHeight 屏幕高
     * @return
     */
    public static Bitmap ReadBitmapById(Context context, int drawableId,
                                        int screenWidth, int screenHeight)
    {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inPreferredConfig = Config.ARGB_8888;
        // options.inInputShareable = true;
        // options.inPurgeable = true;
        InputStream stream = context.getResources().openRawResource(drawableId);
        Bitmap bitmap = BitmapFactory.decodeStream(stream, null, options);
        return getBitmap(bitmap, screenWidth, screenHeight);
    }

    /**
     * 获取屏幕尺寸
     * @param context 上下文
     * @return 屏幕尺寸，x,y
     */
    public Point GetScreenSize(Context context){
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        Point outSize = new Point();
        wm.getDefaultDisplay().getSize(outSize);
        return outSize;
    }

    /**
     * 设置背景为圆角
     *
     * @param bitmap 源图片
     * @param pixels 像素
     * @return 圆图片
     */
    public static Bitmap removeYuanjiao(Bitmap bitmap, int pixels)
    {
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();

        Bitmap creBitmap = Bitmap.createBitmap(width, height,
                android.graphics.Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(creBitmap);

        Paint paint = new Paint();
        float roundPx = pixels;
        RectF rectF = new RectF(0, 0, bitmap.getWidth() - pixels,
                bitmap.getHeight() - pixels);
        paint.setAntiAlias(true);

        canvas.drawARGB(0, 0, 0, 0);
        canvas.drawRoundRect(rectF, roundPx, roundPx, paint);
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));

        canvas.drawBitmap(bitmap, 0, 0, paint);
        if (!bitmap.isRecycled()) {
            bitmap.recycle();
        }
        return creBitmap;
    }

    /**
     * 按正方形裁切图片
     * @param bitmap 源图片
     * @param isRecycled 是否需要回收资源
     * @return 裁剪后的图片
     */
    public static Bitmap ImageCrop(Bitmap bitmap, boolean isRecycled)
    {

        if (bitmap == null)
        {
            return null;
        }

        int w = bitmap.getWidth(); // 得到图片的宽，高
        int h = bitmap.getHeight();

        int wh = w > h ? h : w;// 裁切后所取的正方形区域边长

        int retX = w > h ? (w - h) / 2 : 0;// 基于原图，取正方形左上角x坐标
        int retY = w > h ? 0 : (h - w) / 2;

        Bitmap bmp = Bitmap.createBitmap(bitmap, retX, retY, wh, wh, null,
                false);
        if (isRecycled && bitmap != null && !bitmap.equals(bmp)
                && !bitmap.isRecycled())
        {
            bitmap.recycle();
            bitmap = null;
        }

        // 下面这句是关键
        return bmp;// Bitmap.createBitmap(bitmap, retX, retY, wh, wh, null,
        // false);
    }

    /**
     * 按长方形裁切图片
     *
     * @param bitmap 源图片
     * @return 裁剪后的图片
     */
    public static Bitmap ImageCropWithRect(Bitmap bitmap)
    {
        if (bitmap == null)
        {
            return null;
        }

        int w = bitmap.getWidth(); // 得到图片的宽，高
        int h = bitmap.getHeight();

        int nw, nh, retX, retY;
        if (w > h)
        {
            nw = h / 2;
            nh = h;
            retX = (w - nw) / 2;
            retY = 0;
        } else
        {
            nw = w / 2;
            nh = w;
            retX = w / 4;
            retY = (h - w) / 2;
        }

        // 下面这句是关键
        Bitmap bmp = Bitmap.createBitmap(bitmap, retX, retY, nw, nh, null,
                false);
        if (bitmap != null && !bitmap.equals(bmp) && !bitmap.isRecycled())
        {
            bitmap.recycle();
            bitmap = null;
        }
        return bmp;// Bitmap.createBitmap(bitmap, retX, retY, nw, nh, null,
        // false);
    }

    /**
     * 将图像裁剪成圆形
     *Bitmap的剪切基本操作
     * public static Bitmap createBitmap (Bitmap source, int x, int y, int width, int height, Matrix m, boolean filter)
     * 从原始位图剪切图像，这是一种高级的方式。可以用Matrix(矩阵)来实现旋转等高级方式截图
     参数说明：
     　　Bitmap source：要从中截图的原始位图
     　　int x:起始x坐标
     　　int y：起始y坐标
     int width：要截的图的宽度
     int height：要截的图的宽度
     Bitmap.Config  config：一个枚举类型的配置，可以定义截到的新位图的质量
     返回值：返回一个剪切好的Bitmap
     示例代码：http://download.csdn.net/detail/stop_pig/7162695
     * @param bitmap 源图片
     * @return 裁剪成圆形图片
     */
    public static Bitmap toRoundBitmap(Bitmap bitmap)
    {
        if (bitmap == null){return null;}

        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        float roundPx;
        float left, top, right, bottom, dst_left, dst_top, dst_right, dst_bottom;
        if (width <= height)
        {
            roundPx = width / 2;
            top = 0;
            bottom = width;
            left = 0;
            right = width;
            height = width;
            dst_left = 0;
            dst_top = 0;
            dst_right = width;
            dst_bottom = width;
        } else
        {
            roundPx = height / 2;
            float clip = (width - height) / 2;
            left = clip;
            right = width - clip;
            top = 0;
            bottom = height;
            width = height;
            dst_left = 0;
            dst_top = 0;
            dst_right = height;
            dst_bottom = height;
        }

        Bitmap output = Bitmap.createBitmap(width, height, Config.ARGB_8888);
        Canvas canvas = new Canvas(output);

        final int color = 0xff424242;
        final Paint paint = new Paint();
        final Rect src = new Rect((int) left, (int) top, (int) right,
                (int) bottom);
        final Rect dst = new Rect((int) dst_left, (int) dst_top,
                (int) dst_right, (int) dst_bottom);
        final RectF rectF = new RectF(dst);

        paint.setAntiAlias(true);

        canvas.drawARGB(0, 0, 0, 0);
        paint.setColor(color);
        canvas.drawRoundRect(rectF, roundPx, roundPx, paint);

        paint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));
        canvas.drawBitmap(bitmap, src, dst, paint);
        if (bitmap != null && !bitmap.isRecycled())
        {
            bitmap.recycle();
            bitmap = null;
        }
        return output;
    }


    /**
     * 将图片变成带圆边的圆形图片
     * @param bitmap 源图片
     * @param color 颜色
     * @param height 高度
     * @param width 宽度
      */
    public static Bitmap getRoundBitmap(Bitmap bitmap, int width, int height,int color) {
        if (bitmap == null){ return null; }
        // 将图片变成圆角
        Bitmap roundBitmap = Bitmap.createBitmap(width, height,
                Config.ARGB_8888);
        Canvas canvas = new Canvas(roundBitmap);
        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        int len = (width > height) ? height : width;
        canvas.drawCircle(width / 2, height / 2, len / 2 - 8, paint);
        paint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));
        Bitmap scaledBitmap = Bitmap.createScaledBitmap(bitmap, len, len, true);
        canvas.drawBitmap(scaledBitmap, 0, 0, paint);
        // 将图片加圆边
        Bitmap outBitmap = Bitmap.createBitmap(width, height, Config.ARGB_8888);
        canvas = new Canvas(outBitmap);
        paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setColor(color);
        canvas.drawCircle(width / 2, height / 2, len / 2 - 4, paint);
        paint.setXfermode(new PorterDuffXfermode(Mode.SRC_OVER));
        canvas.drawBitmap(roundBitmap, 0, 0, paint);
        bitmap.recycle();
        bitmap = null;
        roundBitmap.recycle();
        roundBitmap = null;
        scaledBitmap.recycle();
        scaledBitmap = null;
        return outBitmap;
    }

    /**
     * function:图片转圆角
     * @param bitmap 需要转的bitmap
     * @param pixels 转圆角的弧度
     * @return 转圆角的bitmap
     */
    public static Bitmap toRoundCorner(Bitmap bitmap, int pixels)
    {
        Bitmap output = Bitmap.createBitmap(bitmap.getWidth(),
                bitmap.getHeight(), Config.ARGB_8888);
        Canvas canvas = new Canvas(output);
        final int color = 0xff424242;
        final Paint paint = new Paint();
        final Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
        final RectF rectF = new RectF(rect);
        final float roundPx = pixels;
        paint.setAntiAlias(true);
        canvas.drawARGB(0, 0, 0, 0);
        paint.setColor(color);
        canvas.drawRoundRect(rectF, roundPx, roundPx, paint);
        paint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));
        canvas.drawBitmap(bitmap, rect, rect, paint);
        if (bitmap != null && !bitmap.isRecycled())
        {
            bitmap.recycle();
        }
        return output;
    }

    /**
     * 获取指定的圆角图片
     *
     * @param bitmap
     * @return
     */
    public static Bitmap getRadiusBitmap(Bitmap bitmap){
        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setColor(0xffffffff);
        Bitmap radiusBitmap = Bitmap.createBitmap(bitmap.getWidth(),
                bitmap.getHeight(), Config.ARGB_8888);
        Canvas canvas = new Canvas(radiusBitmap);
        RectF rectF = new RectF(0, 0, bitmap.getWidth(), bitmap.getHeight());
        canvas.drawRoundRect(rectF, 7, 7, paint);
        paint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));
        canvas.drawBitmap(bitmap, 0, 0, paint);
        if (bitmap != null && !bitmap.isRecycled()){
            bitmap.recycle();
        }
        return radiusBitmap;
    }

    /**
     * function:获得指定大小的圆边的bitmap数组
     * @param pathArray 需要转的bitmap
     * @param size 转圆角的弧度
     * @param color 颜色
     *  @param len 长度
     *  @param radius 角度
     * @return 转圆角的bitmap
     */
    public static ArrayList<Bitmap> getRadiusBitmapList(String[] pathArray, int size, int len, float radius, int color){
        Bitmap canvasBitmap = null;
        Canvas canvas = null;
        Paint paint = null;
        RectF rectF = new RectF(0, 0, len - radius, len - radius);
        File file = null;
        FileInputStream fis = null;
        Bitmap bitmap = null;
        Bitmap scaledBitmap = null;

        ArrayList<Bitmap> list = new ArrayList<Bitmap>();
        for (int i = 0; i < pathArray.length; i++){
            file = new File(pathArray[i]);
            if (!file.exists())
                continue;
            try{
                fis = new FileInputStream(file);
                bitmap = BitmapFactory.decodeStream(fis);
                if (bitmap != null){
                    canvasBitmap = Bitmap.createBitmap(len, len,
                            Config.ARGB_8888);
                    canvas = new Canvas(canvasBitmap);
                    paint = new Paint(Paint.ANTI_ALIAS_FLAG);
                    paint.setColor(color);
                    canvas.drawRoundRect(rectF, radius, radius, paint);
                    paint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));

                    scaledBitmap = Bitmap.createScaledBitmap(bitmap, len, len,
                            true);
                    canvas.drawBitmap(scaledBitmap, 0, 0, paint);
                    list.add(canvasBitmap);
                }
            } catch (FileNotFoundException e){
            } finally {
                if (fis != null){
                    try{
                        fis.close();
                    } catch (IOException e1){  }
                }
            }
            if (list.size() == size) {
                break;
            }
        }
        if (scaledBitmap != null && !scaledBitmap.isRecycled())
        {
            scaledBitmap.recycle();
            scaledBitmap = null;
        }
        if (bitmap != null && !bitmap.isRecycled())
        {
            bitmap.recycle();
            bitmap = null;
        }
        return list;
    }





    /**
     * 按照一定的宽高比例裁剪图片
     *
     * @param bitmap
     * @param num1 长边的比例
     * @param num2 短边的比例
     * @return
     */
    public static Bitmap ImageCrop(Bitmap bitmap, int num1, int num2,
                                   boolean isRecycled) {
        if (bitmap == null) { return null;}
        int w = bitmap.getWidth(); // 得到图片的宽，高
        int h = bitmap.getHeight();
        int retX, retY;
        int nw, nh;
        if (w > h) {
            if (h > w * num2 / num1) {
                nw = w;
                nh = w * num2 / num1;
                retX = 0;
                retY = (h - nh) / 2;
            } else {
                nw = h * num1 / num2;
                nh = h;
                retX = (w - nw) / 2;
                retY = 0;
            }
        } else{
            if (w > h * num2 / num1) {
                nh = h;
                nw = h * num2 / num1;
                retY = 0;
                retX = (w - nw) / 2;
            } else {
                nh = w * num1 / num2;
                nw = w;
                retY = (h - nh) / 2;
                retX = 0;
            }
        }
        Bitmap bmp = Bitmap.createBitmap(bitmap, retX, retY, nw, nh, null,
                false);
        if (isRecycled && bitmap != null && !bitmap.equals(bmp)
                && !bitmap.isRecycled())  {
            bitmap.recycle();
            bitmap = null;
        }
        return bmp;// Bitmap.createBitmap(bitmap, retX, retY, nw, nh, null, false);
    }
}
