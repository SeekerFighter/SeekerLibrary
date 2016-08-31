package com.seeker.libraries.util;

import android.os.Environment;
import java.io.File;
import java.io.IOException;

/**
 * Created by Seeker on 2016/7/29.
 *
 * 文件操作工具类
 */

public final class FileUtils {

    /**
     * 是否挂载sd卡
     * @return
     */
    public static boolean isSDMounted(){
        return Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED);
    }

    /**
     * 获取sd卡根目录文件
     * @return
     */
    public static File getSDirFile(){
        return Environment.getExternalStorageDirectory();
    }

    /**
     * 获取sd卡根目录绝对路径
     * @return
     */
    public static String getSDirAbsolutePath(){
        return Environment.getExternalStorageDirectory().getAbsolutePath();
    }

    /**
     * 生成文件夹
     * @param path
     * @return
     */
    public static File mkdirsFolder(String path){
        File file = new File(path);
        if(!file.exists()){
            return file.mkdirs()?file:null;
        }
        return file;
    }

    /**
     * 创建文件
     * @param path
     * @return
     */
    public static File createNewFile(String path){
        String dir = path.substring(0,path.lastIndexOf("/"));
        if(mkdirsFolder(dir) == null){
            return null;
        }
        File file = new File(path);
        if(!file.exists()){
            try {
                return file.createNewFile()?file:null;
            }catch (IOException e){
                e.printStackTrace();
                return null;
            }
        }
        return file;
    }

    /**
     * 删除文件
     * @param path
     * @return
     */
    public static boolean deleteFile(String path){
        File file = new File(path);
        if(file != null && file.exists() && file.isFile()){
            return file.delete();
        }
        return true;
    }

    /**
     * 删除文件见夹以及里面的文件
     * @param path
     * @return
     */
    public static boolean deleteFolder(String path){
        return deleteFolder(new File(path));
    }

    /**
     * 删除文件见夹以及里面的文件
     * @param dir
     * @return
     */
    public static boolean deleteFolder(File dir){
        if(dir == null || !dir.exists()){
            return true;
        }
        if(dir.isFile()){
            return dir.delete();
        }
        if(dir.isDirectory()){
            File []files = dir.listFiles();
            if(files == null || files.length == 0){
                return dir.delete();
            }
            for(File file:files){
                deleteFolder(file);
            }
            return dir.delete();
        }
        return false;
    }

}
