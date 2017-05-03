package top.edroplet.encdec.utils.util;

import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import static android.content.ContentValues.TAG;

/**
 * Created by xw on 2017/5/2.
 */

public class ImageOperator {
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
        List<ImagePiece> pieces = new ArrayList<ImagePiece>(xPiece * yPiece);
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
        Bitmap newbm = Bitmap.createBitmap(bm, 0, 0, width, height, matrix,
                true);
        return newbm;
    }
}
