package com.seeker.libraries.util;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.provider.MediaStore;

import java.io.File;
import java.net.URI;

/**
 * Created by Seeker on 2016/7/29.
 */

public class AppUtil {

    /**
     * 打开照相机拍照
     * @param activity
     * @param storefile：图片保存文件
     * @param requstCode
     */
    public static void takePicture(Activity activity, File storefile, int requstCode){
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
    public static void openAlbumPicture(Activity activity,int requstCode){
        Intent intent = new Intent(Intent.ACTION_PICK,MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        activity.startActivityForResult(intent,requstCode);
    }

}
