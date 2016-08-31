package com.seeker.libraries.widget.recycleView;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.LinearLayout;
import android.widget.Scroller;
import com.seeker.libraries.logger.Logger;

/**
 * Created by Seeker on 2016/8/6.
 */
public class ContentContainerLayout extends LinearLayout implements ScrollHelper{

    private static final String TAG = "ContentContainerLayout";

    private Scroller mScroller;

    public ContentContainerLayout(Context context) {
        this(context,null);
    }

    public ContentContainerLayout(Context context, AttributeSet attrs) {
        this(context, attrs,0);
    }

    public ContentContainerLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mScroller = new Scroller(context);
    }

    @Override
    public void computeScroll() {
        if(mScroller.computeScrollOffset()){
            scrollTo(mScroller.getCurrX(),mScroller.getCurrY());
            postInvalidate();
        }
    }

    /**
     *
     * @param startX
     * @param startY
     * @param dx
     * @param dy
     */
    @Override
    public void begin(int startX, int startY, int dx, int dy){
        mScroller.startScroll(startX,startY,dx,dy);
        invalidate();
        Logger.t(TAG).d("startScroll");
    }

}
