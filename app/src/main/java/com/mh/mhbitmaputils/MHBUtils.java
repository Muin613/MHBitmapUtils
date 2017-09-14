package com.mh.mhbitmaputils;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.widget.ImageView;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by Administrator on 2017/9/13.
 */

public class MHBUtils {

    /**
     * @param res   资源类
     * @param resId 资源id
     * @param reqH  请求高度
     * @param reqW  请求宽度
     */
    public static Bitmap createBitmapFromRes(Resources res, int resId, int reqW, int reqH) {
        BitmapFactory.Options op = new BitmapFactory.Options();
        op.inJustDecodeBounds = true;//只是获取边界大小
        BitmapFactory.decodeResource(res, resId, op);
        op.inSampleSize = MHBCalculateUtils.zoomByWH(op, reqW, reqH, 2);//计算缩放比例
        op.inJustDecodeBounds = false;
        return BitmapFactory.decodeResource(res, resId, op);
    }

    public static Bitmap modify2RoundBitmap(Bitmap bitmap) {
        int w = bitmap.getWidth();
        int h = bitmap.getHeight();
        float data[] = MHBCalculateUtils.getWidthHeightRadiusByWH(w, h, 0, 0, true, 1, true, 0);
        Bitmap outBitmap = Bitmap.createBitmap((int) data[6], (int) data[7], Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(outBitmap);
        int color = 0xff424242;
        Paint paint = new Paint();
        Rect src = new Rect((int) data[0], (int) data[1], (int) data[2], (int) data[3]);
        Rect dst = new Rect((int) data[4], (int) data[5], (int) data[6], (int) data[7]);
        RectF rectF = new RectF(dst);
        paint.setAntiAlias(true);
        canvas.drawARGB(0, 0, 0, 0);
        paint.setColor(color);
        canvas.drawRoundRect(rectF, data[8], data[8], paint);
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        canvas.drawBitmap(bitmap, src, dst, paint);
        return outBitmap;
    }


    public static Bitmap drawable2Bitmap(Drawable drawable) {
        int width = drawable.getIntrinsicWidth();
        int height = drawable.getIntrinsicHeight();
        Bitmap bitmap = Bitmap.createBitmap(width, height,
                drawable.getOpacity() != PixelFormat.OPAQUE ? Bitmap.Config.ARGB_8888 : Bitmap.Config.RGB_565);
        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0, 0, width, height);
        drawable.draw(canvas);
        return bitmap;
    }

    public static Bitmap resId2Bitmap(Context context, int resId) {
        BitmapFactory.Options opt = new BitmapFactory.Options();
        opt.inPreferredConfig = Bitmap.Config.RGB_565;//颜色模式
        opt.inPurgeable = true;
        opt.inInputShareable = true;
        InputStream is = context.getResources().openRawResource(resId);
        return BitmapFactory.decodeStream(is, null, opt);
    }


    public static Bitmap file2Bitmap(File file) {
        File f = file;
        Bitmap bitmap = null;
        if (f.exists()) {
            //文件存在
            bitmap = BitmapFactory.decodeFile(f.getAbsolutePath());
        }
        return bitmap;
    }


    /**
     * 更改位图的RGBA
     * 位图 defaultFlag 默认 参数RGBA(5个)
     *
     * @description:
     */
    public static Bitmap changeBitmapRGBA(Bitmap bitmapSrc, Boolean defaultFlag, float R1, float R2, float R3, float R4, float R5, float G1, float G2, float G3, float G4, float G5, float B1, float B2, float B3, float B4, float B5, float A1, float A2, float A3, float A4, float A5) {
        if (defaultFlag) {
            R1 = 1f;
            R2 = 0f;
            R3 = 0f;
            R4 = 0f;
            R5 = 0f;//red
            G1 = 0f;
            G2 = 1f;
            G3 = 0f;
            G4 = 0f;
            G5 = 0f;//green
            B1 = 0f;
            B2 = 0f;
            B3 = 1f;
            B4 = 0f;
            B5 = 0f;//blue
            A1 = 0f;
            A2 = 0f;
            A3 = 0f;
            A4 = 1f;
            A5 = 0f;//alpha
        }
        ColorMatrix cMatrix = new ColorMatrix();
        cMatrix.set(new float[]{R1, R2, R3, R4, R5,//改变red
                G1, G2, G3, G4, G5,//改变green
                B1, B2, B3, B4, B5,// 改变Blue
                A1, A2, A3, A4, A5,//改变alpha
        });
        Paint paint = new Paint();
        paint.setColorFilter(new ColorMatrixColorFilter(cMatrix));
        Canvas canvas = new Canvas();
        canvas.drawBitmap(bitmapSrc, 0, 0, paint);
        return bitmapSrc;
    }
    public static Bitmap byte2Bitmap(byte[] data) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inSampleSize = 1;
        options.inPreferredConfig = Bitmap.Config.RGB_565;
        Bitmap bitmap = BitmapFactory.decodeByteArray(data, 0, data.length, options);
        return bitmap;
    }


    public static Bitmap scaleBitmap(Bitmap bitmap, int width, int height) {
        int w = bitmap.getWidth();
        int h = bitmap.getHeight();
        Matrix matrix = new Matrix();
        matrix.postScale((float) width / w, (float) height / h);
        return Bitmap.createBitmap(bitmap, 0, 0, w, h, matrix, true);
    }
    /**
     * 图片变byte数组
     *
     * @param bm
     * @return
     */
    private byte[] bitmap2Bytes(Bitmap bm) {

        ByteArrayOutputStream baos = new ByteArrayOutputStream();

        bm.compress(Bitmap.CompressFormat.PNG, 100, baos);

        return baos.toByteArray();

    }

    public static Bitmap color2Bitmap(String rgb, int width, int height) {
        Bitmap bmp = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        int color = Color.parseColor(rgb);
        bmp.eraseColor(color);
        return bmp;
    }

    // 利用colormatrix调节颜色(0灰色50正常100过度彩色)
    public static ColorMatrixColorFilter colorFilter(int rate) {
        ColorMatrix matrix = new ColorMatrix();
        matrix.setSaturation(rate);
        ColorMatrixColorFilter filter = new ColorMatrixColorFilter(matrix);
        return filter;
    }
    public static Bitmap url2Bitmap(String url) {
        URL imageURL = null;
        Bitmap bitmap = null;
        try {
            imageURL = new URL(url);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        try {
            HttpURLConnection conn = (HttpURLConnection) imageURL
                    .openConnection();
            conn.setDoInput(true);
            conn.connect();
            InputStream is = conn.getInputStream();
            BitmapFactory.Options options=new BitmapFactory.Options();
            options.inMutable=true;
            bitmap = BitmapFactory.decodeStream(is,null,options);
            is.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
        return bitmap;
    }

}
