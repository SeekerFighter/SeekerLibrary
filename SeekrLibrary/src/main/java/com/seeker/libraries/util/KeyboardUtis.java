package com.seeker.libraries.util;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

/**
 * Created by Seeker on 2016/8/31.
 *
 * 软键盘的显示和隐藏
 */

public final class KeyboardUtis {

    /**
     * 隐藏软键盘
     * @param activity
     */
    public static void hide(Activity activity){
        Validate.notNull(activity,"activity");
        if(activity.getCurrentFocus() != null){
            InputMethodManager imm = (InputMethodManager) activity
                    .getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(activity.getCurrentFocus().getWindowToken(),0);
        }
    }

    /**
     * 显示软键盘
     * @param view
     */
    public static void show(View view){
        Validate.notNull(view,"view");
        InputMethodManager imm = (InputMethodManager) view.getContext()
                .getSystemService(Context.INPUT_METHOD_SERVICE);
        view.requestFocus();
        imm.showSoftInput(view,0);
    }
}
