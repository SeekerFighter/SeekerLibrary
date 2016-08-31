package com.seeker.libraries.util;

import android.app.Activity;
import java.util.Iterator;
import java.util.Stack;

/**
 * Created by Seeker on 2016/8/31.
 *
 * Activity管理类
 */

public final class ActivityManger {

    private final Stack<Activity> activityStack = new Stack<>();

    private static final class Factory{
        private static final ActivityManger instance = new ActivityManger();
    }

    public static ActivityManger getInstance(){
        return Factory.instance;
    }

    /**
     * 添加一个Activity
     * @param activity
     */
    public void addThisActivity(Activity activity){
        if (activity != null && !activityStack.contains(activity)){
            activityStack.push(activity);
        }
    }

    /**
     * 移除一个Activity
     * @param activity
     */
    public void removeThisActivity(Activity activity){
        if(activity != null && activityStack.contains(activity)){
            activityStack.remove(activity);
        }
    }

    /**
     *  获得当前最顶层的Activity
     * @return
     */
    public Activity topActivity(){
        return activityStack.isEmpty()?null:activityStack.peek();
    }

    /**
     * 主动关闭一个Activity
     * @param activity
     */
    public void finishActivity(Activity activity){
        if(activity != null){
            activityStack.remove(activity);
            activity.finish();
        }
    }

    /**
     * 主动关闭所有存活的Activity
     */
    public void finishAllActivity(){
      Iterator<Activity> iterators = activityStack.iterator();
        while (iterators.hasNext()){
            Activity a = iterators.next();
            iterators.remove();
            a.finish();
        }
    }
}
