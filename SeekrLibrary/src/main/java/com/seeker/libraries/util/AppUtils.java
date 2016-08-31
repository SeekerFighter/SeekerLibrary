package com.seeker.libraries.util;

import android.annotation.ColorInt;
import android.annotation.ColorRes;
import android.app.Activity;
import android.app.ActivityManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.DrawableRes;
import android.support.annotation.NonNull;
import android.support.annotation.StringRes;
import android.support.v4.content.ContextCompat;
import java.io.File;
import java.util.List;

/**
 * Created by Seeker on 2016/7/29.
 */

public final class AppUtils {

    /**
     * 打开照相机拍照
     * @param activity
     * @param storefile：图片保存文件
     * @param requstCode
     */
    public static void openCamera(Activity activity, File storefile, int requstCode){
        Validate.notNull(activity,"activity");
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if(storefile != null){
            intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(storefile));
        }
        activity.startActivityForResult(intent,requstCode);
    }

    /**
     * 打开相册获取图片
     * @param activity
     * @param requstCode
     */
    public static void openAlbum(Activity activity,int requstCode){
        Validate.notNull(activity,"activity");
        Intent intent = new Intent(Intent.ACTION_PICK,MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        activity.startActivityForResult(intent,requstCode);
    }

    /**
     * 安装app
     * @param context
     * @param filePath
     */
    public static void installApp(Context context,String filePath){
        Validate.notNull(filePath,"filePath");
        installApp(context,new File(filePath));
    }

    /**
     * 安装app
     * @param context
     * @param file
     */
    public static void installApp(Context context,File file){
        Validate.notNull(context,"context");
        Validate.notNull(file,"file");
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setDataAndType(Uri.fromFile(file), "application/vnd.android.package-archive");
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

    /**
     * 卸载指定包名的App
     *
     * @param context     上下文
     * @param packageName 包名
     */
    public static void uninstallApp(Context context, String packageName) {
        Validate.notNull(context,"context");
        Validate.notNull(packageName,"packageName");
        Intent intent = new Intent(Intent.ACTION_DELETE);
        intent.setData(Uri.parse("package:" + packageName));
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

    /**
     * 判断当前App处于前台还是后台
     * <p>需添加权限 {@code <uses-permission android:name="android.permission.GET_TASKS"/>}</p>
     * <p>并且必须是系统应用该方法才有效</p>
     *
     * @param context 上下文
     * @return {@code true}: 后台<br>{@code false}: 前台
     */
    public static boolean isAppBackground(Context context) {
        Validate.notNull(context,"context");
        ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        @SuppressWarnings("deprecation")
        List<ActivityManager.RunningTaskInfo> tasks = am.getRunningTasks(1);
        if (!tasks.isEmpty()) {
            ComponentName topActivity = tasks.get(0).topActivity;
            if (!topActivity.getPackageName().equals(context.getPackageName())) {
                return true;
            }
        }
        return false;
    }

    /**
     * 获取应用安装信息
     * @param context
     * @return
     */
    public static PackageInfo getAppInfo(Context context){
        Validate.notNull(context,"context");
        PackageManager pm = context.getPackageManager();
        try {
            return pm.getPackageInfo(context.getPackageName(),0);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 根据id获取颜色
     * @param context
     * @param colorResid
     * @return
     */
    @ColorInt
    public static int getColor(Context context,@ColorRes int colorResid){
        Validate.notNull(context,"context");
        return ContextCompat.getColor(context,colorResid);
    }

    /**
     * 根据id获取字符串
     * @param context
     * @param strResid
     * @return
     */
    @NonNull
    public static String getString(Context context, @StringRes int strResid){
        Validate.notNull(context,"context");
        return context.getResources().getString(strResid);
    }

    /**
     * 根据id获取drawable图片
     * @param context
     * @param drawResid
     * @return
     */
    public static Drawable getDrawable(Context context, @DrawableRes int drawResid){
        Validate.notNull(context,"context");
        return ContextCompat.getDrawable(context,drawResid);
    }

}
