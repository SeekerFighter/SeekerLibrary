package com.seeker.libraries.weight.recycleView;

import android.content.Context;
import android.content.res.Resources;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import com.seeker.libraries.R;
import com.seeker.libraries.logger.Logger;

/**
 * Created by Seeker on 2016/8/5.
 */

public class ScrollOverflowLayout extends FrameLayout{

    private static final String TAG = "ScrollOverflowLayout";
    
    private int overflowLayoutWidth = 0;

    private View overflowLayout,contentLayout;

    private ViewGroup overflowContainer,contentContainer;

    public ScrollOverflowLayout(Context context) {
        this(context,null);
    }

    public ScrollOverflowLayout(Context context, AttributeSet attrs) {
        this(context, attrs,0);
    }

    public ScrollOverflowLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        final int childCount = getChildCount();
        Logger.t(TAG).d("childCount = "+childCount);
        /**
         * childCount == 0，说明是动态添加的overflowLayout
         */
        if(childCount == 0){
            setContainer();
        }else{
            try {
                overflowContainer = (ViewGroup)findViewById(R.id.overflowContainer);
                contentContainer = (ViewGroup)findViewById(R.id.contentContainer);
            }catch (Resources.NotFoundException rn){
                throw new Resources.NotFoundException("Can not find childs which id is 'overflowContainer' or " +
                        "'contentContainer',you must contain! ");
            }

        }
    }

    /**
     * 动态设置容器
     */
    private void setContainer(){
        View container = LayoutInflater.from(getContext())
                .inflate(R.layout.recycleview_item_container_layout,this,true);
        overflowContainer = (ViewGroup) container.findViewById(R.id.overflowContainer);
        contentContainer = (ViewGroup)container.findViewById(R.id.contentContainer);
    }

    /**
     *  动态设置
     * @param overflowLayout
     */
    public ScrollOverflowLayout setOverFlowLayout(View overflowLayout){
        this.overflowLayout = overflowLayout;
        return this;
    }

    /**
     * 动态设置
     * @param contentLayout
     */
    public ScrollOverflowLayout setContentLayout(View contentLayout){
        this.contentLayout = contentLayout;
        return this;
    }

    /**
     * 动态添加的时候必须调用此方法
     */
    public void add(){

        if(checkContainerNull()){
            setContainer();
        }

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

        overflowContainer.addView(overflowLayout,p);
        contentContainer.addView(contentLayout);
    }

    private boolean checkContainerNull(){
        boolean isNull = false;
        if(overflowContainer == null || contentContainer == null){
            isNull = true;
            overflowContainer = null;
            contentContainer = null;
        }
        return isNull;
    }

    public ViewGroup getOverflowContainer() {
        return overflowContainer;
    }

    public ViewGroup getContentContainer() {
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
