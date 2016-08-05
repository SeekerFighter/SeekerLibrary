package com.seeker.libraries.weight.recycleView;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import com.seeker.libraries.R;

/**
 * Created by Seeker on 2016/8/5.
 */

public class ScrollOverflowLayout extends FrameLayout{

    private static final String TAG = "ScrollOverflowLayout";
    
    private int overflowLayoutWidth = 0;

    private View overflowLayout,contentLayout;

    private LinearLayout overflowContainer,contentContainer;

    public ScrollOverflowLayout(Context context) {
        this(context,null);
    }

    public ScrollOverflowLayout(Context context, AttributeSet attrs) {
        this(context, attrs,0);
    }

    public ScrollOverflowLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        View container = LayoutInflater.from(context).inflate(R.layout.recycleview_item_container_layout,this,true);
        overflowContainer = (LinearLayout) container.findViewById(R.id.overflowContainer);
        contentContainer = (LinearLayout) container.findViewById(R.id.contentContainer);
    }

    /**
     *
     * @param overflowLayout
     */
    public ScrollOverflowLayout setOverFlowLayout(View overflowLayout){
        this.overflowLayout = overflowLayout;
        return this;
    }

    /**
     *
     * @param contentLayout
     */
    public ScrollOverflowLayout setContentLayout(View contentLayout){
        this.contentLayout = contentLayout;
        return this;
    }

    public void add(){
        if(contentLayout == null){
            throw new NullPointerException("contentLayout == null");
        }
        if(overflowLayout == null){
            throw new NullPointerException("overflowLayout == null");
        }
        overflowLayout.measure(0,0);
        contentLayout.measure(0,0);

        this.overflowLayoutWidth = overflowLayout.getMeasuredWidth();

        FrameLayout.LayoutParams p = new LayoutParams(-2,-2);
        p.gravity = Gravity.RIGHT|Gravity.CENTER_VERTICAL;
        p.height = contentLayout.getMeasuredHeight();

        overflowContainer.addView(overflowLayout);
        contentContainer.addView(contentLayout);
    }

    public LinearLayout getOverflowContainer() {
        return overflowContainer;
    }

    public LinearLayout getContentContainer() {
        return contentContainer;
    }

    /**
     *
     * @return
     */
    public int getOverflowLayoutWidth() {
        return overflowLayoutWidth;
    }
}
