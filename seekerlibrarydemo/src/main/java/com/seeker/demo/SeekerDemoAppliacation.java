package com.seeker.demo;

import android.app.Application;
import android.util.Log;
import com.seeker.libraries.logger.Logger;

/**
 * Created by Seeker on 2016/7/28.
 */

public class SeekerDemoAppliacation extends Application{

    @Override
    public void onCreate() {
        super.onCreate();
        Logger.initialize(Logger.Settings.getInstance()
                .setRootTag("Seeker_")
                .isShowMethodLink(true)
                .isShowThreadInfo(false)
                .setMethodOffset(0)
                .setLogPriority(BuildConfig.DEBUG ? Log.VERBOSE:Log.ASSERT));
    }
}
