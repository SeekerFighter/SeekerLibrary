package com.seeker.demo;

import com.seeker.libraries.util.FileUtils;

import java.io.File;

/**
 * Created by Seeker on 2016/7/28.
 */

public class Config {

    public static final String URL = "http://www.imooc.com/api/teacher?type=4&num=15";

    public static File getDirFile(String subFileName){
        if(FileUtils.isSDMounted()){
            return FileUtils.mkdirsFolder(FileUtils.getSDirAbsolutePath()+"/Seeker/"+subFileName);
        }
        return null;
    }

}
