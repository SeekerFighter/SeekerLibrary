package com.seeker.libraries.util;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.provider.Settings;

/**
 * Created by Seeker on 2016/8/28.
 *
 * 网络相关的工具类
 *
 * <p>需添加权限 {@code <uses-permission android:name="android.permission.ACCESS_NETWORK_STATE"/>}</p>
 */
public final class NetWorkUtils {


    /**
     * check availability of Internet
     *
     * @param context
     * @return true or false
     */
    public static boolean isNetworkConnect(Context context) {
        Validate.notNull(context,"context");
        ConnectivityManager cm = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        if (netInfo != null && netInfo.isConnectedOrConnecting()) {
            return true;
        }
        return false;
    }

    /**
     * 打开网络设置界面
     * @param context
     */
    public static void openNetworkPager(Context context){
        Validate.notNull(context,"context");
        Intent intent = new Intent();
        if (Build.VERSION.SDK_INT > 10){
            intent.setAction(Settings.ACTION_SETTINGS);
        }else {
            intent.setAction(Settings.ACTION_WIRELESS_SETTINGS);
        }
        context.startActivity(intent);
    }

    /**
     * 判断是否是wifi连接
     * @param context
     * @return
     */
    public static boolean isWIFIConnected(Context context){
        Validate.notNull(context,"context");
        ConnectivityManager cm = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        return cm != null && cm.getActiveNetworkInfo().getType() == ConnectivityManager.TYPE_WIFI;
    }

}
