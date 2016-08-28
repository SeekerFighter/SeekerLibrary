package com.seeker.libraries.util;

import android.annotation.Nullable;

/**
 * Created by Seeker on 2016/8/28.
 *
 * 合法有效性检测
 */
public final class Validate {

    private static final String TAG = "Validate";

    /**
     * check object null,if null throw NullPointerException
     * @param location where use this function
     * @param arg check null
     * @param name arg name
     */
    public static void notNull(String location,Object arg, String name) {
        if (arg == null) {
            throw new NullPointerException("At "+location+",Argument '" + name + "' cannot be null.");
        }
    }

    /**
     * Returns true if the string is null or 0-length.
     * @param str the string to be examined
     * @return true if str is null or zero length
     */
    public static boolean isEmpty(@Nullable String str){
        return (str == null || str.trim().length() == 0);
    }

    /**
     * 邮箱有效性判断
     * Validate email string
     * @param email is object of String
     * @return true if valid email otherwise false
     */
    public static boolean isEmail(String email) {
        String pattern = "^([a-zA-Z0-9_.-])+@([a-zA-Z0-9_.-])+\\.([a-zA-Z]){2,4}";
        return !isEmpty(email) && email.matches(pattern);
    }

    /**
     * <p>移动：134(0-8)、135、136、137、138、139、147、150、151、152、157、158、159、178、182、183、184、187、188</p>
     * <p>联通：130、131、132、145、155、156、175、176、185、186</p>
     * <p>电信：133、153、173、177、180、181、189</p>
     * <p>全球星：1349</p>
     * <p>虚拟运营商：170</p>
     * 手机号有效性判断
     * Validate phone string
     * @param phone is object of String
     * @return true if valid phone otherwise false
     */
    public static boolean isMobilephone(String phone){
        String REGEX_MOBILE_EXACT = "^((13[0-9])|(14[5,7])|(15[0-3,5-9])|(17[0,3,5-8])|(18[0-9])|(147))\\d{8}$";
        return !isEmpty(phone) && phone.matches(REGEX_MOBILE_EXACT);
    }

    /**
     * 身份证有效性判断
     * Validate idCard string
     * @param idCard is object of String
     * @return true if valid idCard otherwise false
     */
    public static boolean isIDCard(String idCard){
        String REGEX_IDCARD18 = "^[1-9]\\d{5}[1-9]\\d{3}((0\\d)|(1[0-2]))(([0|1|2]\\d)|3[0-1])\\d{3}([0-9Xx])$";
        return !isEmpty(idCard) && idCard.matches(REGEX_IDCARD18);
    }
}
