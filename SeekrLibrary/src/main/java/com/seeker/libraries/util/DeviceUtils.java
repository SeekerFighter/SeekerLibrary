package com.seeker.libraries.util;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Rect;
import android.os.Build;
import android.telephony.TelephonyManager;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.WindowManager;

/**
 * Created by Seeker on 2016/7/13.
 *
 * 获取手机设备等的一些数据
 */
public final class DeviceUtils {

    /**
     * 获取DisplayMetrics
     * @param context
     * @return
     */
    private static DisplayMetrics obtain(Context context){
        WindowManager wm = (WindowManager)context.getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics dm = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(dm);
        return dm;
    }

    /**
     * 获取屏幕宽度
     * @param context
     * @return
     */
    public static int getDeviceWidth(Context context){
        DisplayMetrics outMetrics = obtain(context);
        return outMetrics.widthPixels;
    }

    /**
     * 获取屏幕高度
     * @param context
     * @return
     */
    public static int getDeviceHeight(Context context){
        DisplayMetrics outMetrics = obtain(context);
        return outMetrics.heightPixels;
    }

    /**
     * 获取屏幕大小[0]宽，[1]高
     * @param context
     * @return
     */
    public static int[] getDeviceSize(Context context){
        DisplayMetrics outMetrics = obtain(context);
        int [] sizes = new int[2];
        sizes[0] = outMetrics.widthPixels;
        sizes[1] = outMetrics.heightPixels;
        return sizes;
    }

    /**
     * 获取设备屏幕密度dpi，每寸所包含的像素点
     * @param context
     * @return
     */
    public static float getDeviceDensityDpi(Context context){
        return context.getResources().getDisplayMetrics().densityDpi;
    }

    /**
     * 获取设备屏幕密度,像素的比例
     * @param context
     * @return
     */
    public static float getDeviceDensity(Context context){
        return context.getResources().getDisplayMetrics().density;
    }

    /**
     * 获取状态栏高度
     * @param context
     * @return
     */
    public static int getStatusHeight(Context context){
        int statusHeight = -1;
        try {
            Class<?> clazz = Class.forName("com.android.internal.R$dimen");
            Object object = clazz.newInstance();
            int height = Integer.parseInt(clazz.getField("status_bar_height")
                    .get(object).toString());
            statusHeight = context.getResources().getDimensionPixelSize(height);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return statusHeight;
    }

    /**
     * 截取当前屏幕画面为bitmap图片
     * @param activity
     * @param hasStatusBar 是否包含当前状态栏,true:包含
     * @return
     */
    public static Bitmap snapCurrentScreenShot(Activity activity, boolean hasStatusBar){
        View decorView = activity.getWindow().getDecorView();
        decorView.setDrawingCacheEnabled(true);
        decorView.buildDrawingCache();
        Bitmap bmp = decorView.getDrawingCache();
        int deviceSize[] = getDeviceSize(activity);
        int coordinateY = 0;
        int cutHeight = deviceSize[1];
        if(!hasStatusBar){
            Rect frame = new Rect();
            decorView.getWindowVisibleDisplayFrame(frame);
            coordinateY += frame.top;
            cutHeight -= frame.top;
        }
        Bitmap shot = Bitmap.createBitmap(bmp,0,coordinateY,deviceSize[0],cutHeight);
        decorView.destroyDrawingCache();
        return shot;
    }

    /**
     * 获取手机IMEI号
     * add <uses-permission android:name="android.permission.READ_PHONE_STATE" /> in AndroidManifest.xml
     * @param context
     * @return
     */
    public static String getDeviceIMEI(Context context){
        TelephonyManager tm = (TelephonyManager)context.getSystemService(Context.TELEPHONY_SERVICE);
        return tm.getDeviceId();
    }

    /**
     * 获取手机厂商
     * @return
     */
    public static String getDeviceManufacturer(){
        return Build.MANUFACTURER;
    }

    /**
     * 获取手机型号
     * @return
     */
    public static String getDeviceModel(){
        return Build.MODEL;
    }

    /**
     * 获取手机系统版本号
     * @return
     */
    public static String getDeviceSystemVersion(){
        return Build.VERSION.RELEASE;
    }

    /**
     * 讲px值转变成dip
     * @param context
     * @param px
     * @return
     */
    public static float pxToDip(Context context, float px){
        return px / getDeviceDensity(context) + 0.5f;
    }

    /**
     * 将dip值转成px
     * @param context
     * @param dip
     * @return
     */
    public static float dipToPx(Context context, float dip){
        return dip * getDeviceDensity(context) + 0.5f;
    }

    /**
     * 将px值转成sp值
     * @param context
     * @param px
     * @return
     */
    public static float pxToSp(Context context, float px){
        final float fontScale = context.getResources().getDisplayMetrics().scaledDensity;
        return px / fontScale + 0.5f;
    }

    /**
     * 将sp值转成px值
     * @param context
     * @param sp
     * @return
     */
    public static float spTpPx(Context context, float sp){
        final float fontScale = context.getResources().getDisplayMetrics().scaledDensity;
        return sp * fontScale + 0.5f;
    }
}
