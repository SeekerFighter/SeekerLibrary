package com.seeker.libraries.logger;

import android.support.annotation.NonNull;
import android.util.Log;

import timber.log.Timber;

/**
 * Logger is a wrapper of {@link Timber}
 * But more pretty, simple and powerful
 */
public class Logger {

    public static void initialize(Settings settings) {
        Timber.plant(new LogPrinter(settings));
    }

    public static Timber.Tree t(String tag) {
        return Timber.tag(tag);
    }

    public static void v(String message, Object... args) {
        message = handleNullMsg(message);
        Timber.v(message, args);
    }

    public static void d(String message, Object... args) {
        message = handleNullMsg(message);
        Timber.d(message, args);
    }

    public static void i(String message, Object... args) {
        message = handleNullMsg(message);
        Timber.i(message, args);
    }

    public static void w(String message, Object... args) {
        message = handleNullMsg(message);
        Timber.w(message, args);
    }

    public static void w(Throwable throwable, String message, Object... args) {
        message = handleNullMsg(message);
        Timber.w(throwable, message, args);
    }

    public static void e(String message, Object... args) {
        message = handleNullMsg(message);
        Timber.e(message, args);
    }

    public static void e(Throwable throwable, String message, Object... args) {
        message = handleNullMsg(message);
        Timber.e(throwable, message, args);
    }

    /**
     * Formats the json content and print it
     *
     * @param json the json content
     */
    public static void json(String json) {
        Timber.d(XmlJsonParser.json(json));
    }

    /**
     * Formats the json content and print it
     *
     * @param xml the xml content
     */
    public static void xml(String xml) {
        Timber.d(XmlJsonParser.xml(xml));
    }

    /**
     * Formats the json content and print it
     *
     * @param object Bean,Array,Collection,Map,Pojo and so on
     */
    public static void object(Object object) {
        Timber.d(ObjParser.parseObj(object));
    }

    public static void uprootAll() {
        Timber.uprootAll();
    }

    /**
     * Timber will swallow message if it's null and there's no throwable.
     */
    @NonNull
    private static String handleNullMsg(String message) {
        if (message == null) {
            message = "null";
        }
        return message;
    }


    public static class Settings {

        private static final String ROOT_TAG = "Seeker_";

        protected int methodOffset = 0;

        protected boolean showMethodLink = true;

        protected boolean showThreadInfo = false;

        protected int priority = Log.VERBOSE;

        protected String rootTag;

        public static Settings getInstance() {
            return new Settings();
        }

        private Settings() {
            this.rootTag = ROOT_TAG;
        }

        public Settings setMethodOffset(int methodOffset) {
            this.methodOffset = methodOffset;
            return this;
        }

        public Settings isShowThreadInfo(boolean showThreadInfo) {
            this.showThreadInfo = showThreadInfo;
            return this;
        }

        public Settings isShowMethodLink(boolean showMethodLink) {
            this.showMethodLink = showMethodLink;
            return this;
        }

        public Settings setLogPriority(int priority) {
            this.priority = priority;
            return this;
        }

        public Settings setRootTag(String rootTag){
            this.rootTag = rootTag;
            return this;
        }
    }

}
