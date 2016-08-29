package com.seeker.libraries.util;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.renderscript.Allocation;
import android.renderscript.Element;
import android.renderscript.RenderScript;
import android.renderscript.ScriptIntrinsicBlur;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

/**
 * Created by Seeker on 2016/7/14.
 *
 * 图片操作
 */
public class ImageUtils {

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
    public static Bitmap toRoundCornerBitmap(Bitmap bitmap, float pixels){
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
     * 转成圆形图片
     * @param bitmap
     * @return
     */
    public static Bitmap toCircleBitmap(Bitmap bitmap){
        if (bitmap == null) return null;
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        Bitmap output = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(output);
        final Paint paint = new Paint();
        final Rect rect = new Rect(0, 0, width, height);
        paint.setAntiAlias(true);
        canvas.drawARGB(0, 0, 0, 0);
        paint.setColor(Color.TRANSPARENT);
        canvas.drawCircle(width / 2, height / 2, width / 2, paint);
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        canvas.drawBitmap(bitmap, rect, rect, paint);
        return output;
    }

    /**
     * 转为模糊图片
     *
     * @param src  源图片
     * @param radius  模糊度(0...25)
     * @param context 上下文
     * @return 模糊后的图片
     */
    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
    public static Bitmap toBlurBitmap(Bitmap src, float radius, Context context) {
        if (src == null) return null;
        Bitmap outBitmap = Bitmap.createBitmap(src.getWidth(), src.getHeight(), Bitmap.Config.ARGB_8888);
        RenderScript rs = RenderScript.create(context);
        ScriptIntrinsicBlur blurScript = ScriptIntrinsicBlur.create(rs, Element.U8_4(rs));
        Allocation allIn = Allocation.createFromBitmap(rs, src);
        Allocation allOut = Allocation.createFromBitmap(rs, outBitmap);
        if (radius > 25) {
            radius = 25.0f;
        } else if (radius <= 0) {
            radius = 1.0f;
        }
        blurScript.setRadius(radius);
        blurScript.setInput(allIn);
        blurScript.forEach(allOut);
        allOut.copyTo(outBitmap);
        rs.destroy();
        if (!src.isRecycled()) src.recycle();
        return outBitmap;
    }

    /**
     * 添加颜色边框
     *
     * @param src         源图片
     * @param borderWidth 边框宽度
     * @param color       边框的颜色值
     * @return 带颜色边框图
     */
    public static Bitmap addFrame(Bitmap src, int borderWidth, int color) {
        if (src == null) return null;
        int newWidth = src.getWidth() + borderWidth;
        int newHeight = src.getHeight() + borderWidth;
        Bitmap out = Bitmap.createBitmap(newWidth, newHeight, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(out);
        Rect rec = canvas.getClipBounds();
        rec.bottom--;
        rec.right--;
        Paint paint = new Paint();
        paint.setColor(color);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(borderWidth);
        canvas.drawRect(rec, paint);
        canvas.drawBitmap(src, borderWidth / 2, borderWidth / 2, null);
        canvas.save(Canvas.ALL_SAVE_FLAG);
        canvas.restore();
        if (!src.isRecycled()) src.recycle();
        return out;
    }
}
