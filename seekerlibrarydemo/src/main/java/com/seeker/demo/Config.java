package com.seeker.demo;

import com.seeker.libraries.util.FileUtil;

import java.io.File;

/**
 * Created by Seeker on 2016/7/28.
 */

public class Config {

    public static final String URL = "http://www.imooc.com/api/teacher?type=4&num=15";

    public static File getDirFile(String subFileName){
        if(FileUtil.isSDMounted()){
            return FileUtil.mkdirsFolder(FileUtil.getSDirAbsolutePath()+"/Seeker/"+subFileName);
        }
        return null;
    }

}
