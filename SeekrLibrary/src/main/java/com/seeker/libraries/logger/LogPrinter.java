package com.seeker.libraries.logger;

import android.support.annotation.NonNull;

import lombok.Getter;
import timber.log.Timber;

/**
 * Wrap {@link timber.log.Timber.Tree} for make log pretty
 */
public final class LogPrinter extends Timber.DebugTree {

    private static final int STACK_OFFSET = 8;

    private static final String BOTTOM_BORDER = "╚═════════════Function Leave══════════════";

    private static final String PREFIX_BORDER = "║";

    /**
     * 因为如果设置了tag，那么会在timber中多走一个方法，方法栈会发生变化，造成不准确的情况。
     */
    private boolean isCustomTag = true;

    private final StringBuilder sb = new StringBuilder();

    @Getter
    private final Logger.Settings settings;

    private static final String PROPERTY = System.getProperty("line.separator");

    public LogPrinter(Logger.Settings settings) {
        this.settings = settings;
    }

    /**
     * rule for auto tag
     */
    @Override
    protected String createStackElementTag(StackTraceElement ignored) {
        isCustomTag = false;
        int offset = STACK_OFFSET + settings.methodOffset - 1;
        return super.createStackElementTag(new Throwable().getStackTrace()[offset]);
    }

    @Override
    protected void log(int priority, String tag, @NonNull String message, Throwable ignored) {

        super.log(priority, settings.rootTag + tag, PREFIX_BORDER + getHeader(), null);

        String[] lines = message.split(PROPERTY);
        for (int i = 0, length = lines.length; i < length; i++) {
            super.log(priority, settings.rootTag + tag, PREFIX_BORDER + lines[i], null);
        }
        // Finally print bottom line
        super.log(priority, settings.rootTag + tag, BOTTOM_BORDER, null);

        isCustomTag = true;
    }

    /**
     * ==> onCreate(MainActivity.java:827) Thread:main
     */
    private String getHeader() {
        if (!settings.showMethodLink) {
            return "";
        }

        int index = STACK_OFFSET + settings.methodOffset + 1;
        if (isCustomTag) {
            index -= 2;
        }
        final StackTraceElement stack = Thread.currentThread().getStackTrace()[index];

        sb.delete(0, sb.length());
        sb.append(String.format("==>Print Log In Function [%s](%s:%s)",
                stack.getMethodName(),
                stack.getFileName(),
                stack.getLineNumber()));

        if (settings.showThreadInfo) {
            sb.append(" Thread: ").append(Thread.currentThread().getName()); // Thread:main
        }

        return sb.toString();
    }

    /**
     * 根据级别显示log
     *
     * @return 默认所有级别都显示
     */
    @Override
    protected boolean isLoggable(int priority) {
        return priority >= settings.priority;
    }
}
