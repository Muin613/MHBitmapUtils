package com.mh.mhbitmaputils;

import android.graphics.BitmapFactory;

/**
 * Created by Administrator on 2017/9/13.
 */

public class MHBCalculateUtils {

    //缩放
    public static int zoomByWH(BitmapFactory.Options op, int reqW, int reqH, int scaleRatio) {
        int w = op.outWidth;
        int h = op.outHeight;
        int zoom = 1;
        while ((w / zoom) > reqW && (h / zoom) > reqH) {
            zoom *= scaleRatio;
        }
        return zoom;
    }

    //截取长宽，半径
    public static float[] getWidthHeightRadiusByWH(int w, int h, float wRatio, float hRatio, boolean whEqualFlag, float rRatio, boolean radiusReferWidthFlag, int r) {
        float radius = r, left = 0, top = 0, right = w, bottom = h, dst_left = 0, dst_top = 0, dst_right = w, dst_bottom = h;
        if (whEqualFlag) {
            if (w <= h) {
                if (r == 0)
                    radius = (int) (w / 2 * rRatio);
                dst_bottom = bottom = w;
            } else {
                if (r == 0)
                    radius = (int) (h / 2 * rRatio);
                float clip = (w - h) / 2;
                left = clip;
                right = w - clip;
                dst_right = h;
            }
        } else {
            left = (w - w * wRatio) / 2;
            dst_left = 0;
            top = (h - h * hRatio) / 2;
            dst_top = 0;
            right = w - left;
            dst_right = wRatio * w;
            dst_bottom = h - top;
            bottom = h * hRatio;
            if (r == 0)
                if (radiusReferWidthFlag)
                    radius = (int) (w / 2 * rRatio);
                else
                    radius = (int) (h * rRatio);
        }

        return new float[]{left, top, right, bottom, dst_left, dst_top, dst_right, dst_bottom, radius};

    }
}
