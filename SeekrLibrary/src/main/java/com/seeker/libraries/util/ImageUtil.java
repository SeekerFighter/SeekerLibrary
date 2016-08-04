package com.seeker.libraries.util;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

/**
 * Created by Seeker on 2016/7/14.
 *
 * 图片操作
 */
public class ImageUtil {

    /**
     * drawable图片转成bitmap图片
     * @param drawable
     * @return
     */
    public static Bitmap drawableToBitmap(Drawable drawable){
        if(drawable == null){
            return null;
        }
        if(drawable instanceof BitmapDrawable){
            return ((BitmapDrawable)drawable).getBitmap();
        }else{
            Bitmap bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(),drawable.getIntrinsicHeight(),
                    drawable.getOpacity()!= PixelFormat.OPAQUE? Bitmap.Config.ARGB_8888: Bitmap.Config.RGB_565);
            final Canvas canvas = new Canvas(bitmap);
            drawable.setBounds(0, 0, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());
            drawable.draw(canvas);
            return bitmap;
        }
    }

    /**
     * bitmap图片转成drawable图片
     * @param bitmap
     * @return
     */
    public static Drawable bitmapToDrawable(Bitmap bitmap){
        return bitmapToDrawable(null,bitmap);
    }
    public static Drawable bitmapToDrawable(Resources res, Bitmap bitmap){
        return bitmap == null ? null:new BitmapDrawable(res,bitmap);
    }

    /**
     * bitmap图片转成byte数组
     * @param bitmap
     * @return
     */
    public static byte[] bitmapToByte(Bitmap bitmap){
        if(bitmap == null){
            return null;
        }
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
        byte[] bytes = baos.toByteArray();
        try {
            baos.close();
        }catch (IOException e){
            e.printStackTrace();
        }
        return bytes;
    }

    /**
     * byte数组转成bitmap图片
     * @param bytes
     * @return
     */
    public static Bitmap byteToBitmap(byte[] bytes){
        if (bytes == null || bytes.length == 0) return null;
        return BitmapFactory.decodeByteArray(bytes,0,bytes.length);
    }

    /**
     * bitmap图片转成圆角bitmap图片
     * @param bitmap
     * @param pixels
     * @return
     */
    public static Bitmap getRoundCornerBitmap(Bitmap bitmap, float pixels){
        if (bitmap == null) return null;
        Bitmap output = Bitmap.createBitmap(bitmap.getWidth(),bitmap.getHeight(), Bitmap.Config.ARGB_8888);
        final Canvas canvas = new Canvas(output);
        final Paint paint = new Paint();
        final Rect rect = new Rect(0,0,bitmap.getWidth(),bitmap.getHeight());
        final RectF rectF = new RectF(rect);
        paint.setAntiAlias(true);
        canvas.drawARGB(0, 0, 0, 0);
        paint.setColor(0xff424242);
        canvas.drawRoundRect(rectF, pixels, pixels, paint);
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        canvas.drawBitmap(bitmap,rect,rect,paint);
        return output;
    }

    /**
     * drawable图片转成圆角drawable图片
     * @param drawable
     * @param pixels
     * @return
     */
    public static Drawable getRoundCornerDrawable(Drawable drawable, float pixels){
        if(drawable == null) return null;
        return new BitmapDrawable(null,getRoundCornerBitmap(drawableToBitmap(drawable),pixels));
    }
}
