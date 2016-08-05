package com.seeker.libraries.weight.recycleView;

import android.support.v4.view.ViewConfigurationCompat;
import android.support.v7.widget.RecyclerView;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import com.seeker.libraries.logger.Logger;

/**
 * Created by Seeker on 2016/8/5.
 */

public class CheckItemTouchListener implements RecyclerView.OnItemTouchListener{

    private static final String TAG = "OnRecycleViewItemTouchL";
    
    /**
     * Indicates that we are not in the middle of a touch gesture
     */
    private static final int TOUCH_MODE_REST = -1;

    /**
     * Indicates we just received the touch event and we are waiting to see if the it is a tap or a
     * scroll gesture.
     */
    private static final int TOUCH_MODE_DOWN = 0;

    /**
     * Indicates the touch has been recognized as a tap and we are now waiting to see if the touch
     * is a longpress
     */
    private static final int TOUCH_MODE_TAP = 1;

    /**
     * Indicates we have waited for everything we can wait for, but the user's finger is still down
     */
    private static final int TOUCH_MODE_DONE_WAITING = 2;

    /**
     * One of TOUCH_MODE_REST, TOUCH_MODE_DOWN, TOUCH_MODE_TAP, TOUCH_MODE_SCROLL, or
     * TOUCH_MODE_DONE_WAITING
     */
    private int mTouchMode = TOUCH_MODE_REST;

    private RecyclerView mRecycleView;

    private int mNestedXoffset = 0;

    private int mNestYoffset = 0;

    /**
     * The X value associated with the the down motion event
     */
    private int mMotionDownX;

    /**
     * The Y value associated with the the down motion event
     */
    private int mMotionDownY;

    /**
     * 点击事件监听
     */
    private OnItemClickListener mClickListener;
    /**
     * 长按事件的监听
     */
    private OnLongItemClickListener mLongClickListener;

    private CheckForLongPress mPendingCheckForLongPress;

    private CheckForTap mPendingCheckForTap;

    private PerformClick mPerformClick;

    private boolean isMove = false;

    private float touchSlop;

    public CheckItemTouchListener(RecyclerView recyclerView){
        this.mRecycleView = recyclerView;
        this.touchSlop = ViewConfigurationCompat.getScaledPagingTouchSlop(ViewConfiguration.get(mRecycleView.getContext()));
    }

    @Override
    public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent me) {
        Logger.t(TAG).d("[onInterceptTouchEvent]");
        final int actionMasked = me.getActionMasked();

        if(actionMasked == MotionEvent.ACTION_DOWN){
            if(mRecycleView.getScrollState() != RecyclerView.SCROLL_STATE_IDLE){
                return false;
            }
        }

        switch (actionMasked){
            case MotionEvent.ACTION_DOWN:{
                onTouchDown(me);
            }break;
            case MotionEvent.ACTION_MOVE: {
                onTouchMove(me);
            }break;
            case MotionEvent.ACTION_UP:{
                onTouchUp(me);
            }break;
            case MotionEvent.ACTION_CANCEL:{
                final View pointToView = mRecycleView.findChildViewUnder(mMotionDownX,mMotionDownY);
                pointToView.setPressed(false);
            }break;
        }
        return false;
    }

    @Override
    public void onTouchEvent(RecyclerView rv, MotionEvent me) {
        Logger.t(TAG).d("[onTouchEvent]");
    }

    @Override
    public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {
        Logger.t(TAG).d("[onRequestDisallowInterceptTouchEvent]");
    }

    private void onTouchDown(MotionEvent event){
        Logger.t(TAG).d("[onTouchDown]");
        mNestedXoffset = 0;
        mNestYoffset = 0;
        isMove = false;
        mTouchMode = TOUCH_MODE_DOWN;
        if(mPendingCheckForTap == null){
            mPendingCheckForTap = new CheckForTap();
        }
        mPendingCheckForTap.x = event.getX();
        mPendingCheckForTap.y = event.getY();
        mRecycleView.postDelayed(mPendingCheckForTap,ViewConfiguration.getTapTimeout());

        mMotionDownX = (int) event.getX();
        mMotionDownY = (int) event.getY();
    }

    private void onTouchMove(MotionEvent event){
        mNestedXoffset = (int) (event.getX() - mMotionDownX);
        mNestYoffset = (int)(event.getY() - mMotionDownY);

        isMove = Math.max(Math.abs(mNestedXoffset),Math.abs(mNestYoffset)) > touchSlop;

        if(!isMove) return;

        switch (mTouchMode){
            case TOUCH_MODE_DOWN:
            case TOUCH_MODE_TAP:
            case TOUCH_MODE_DONE_WAITING:
                final View pointToView = mRecycleView.findChildViewUnder(event.getX(),event.getY());
                if(pointToView != null){
                    pointToView.setPressed(false);
                    mRecycleView.removeCallbacks(mTouchMode == TOUCH_MODE_DOWN?
                    mPendingCheckForTap:mPendingCheckForLongPress);
                }
                break;
        }
    }

    private void onTouchUp(MotionEvent event){
        Logger.t(TAG).d("[onTouchUp]");
        switch (mTouchMode){
            case TOUCH_MODE_DOWN:
            case TOUCH_MODE_TAP:
            case TOUCH_MODE_DONE_WAITING:
                final View pointToView = mRecycleView.findChildViewUnder(event.getX(),event.getY());
                if(pointToView != null){
                    if(mTouchMode != TOUCH_MODE_DOWN){
                        pointToView.setPressed(false);
                    }

                    final float x = event.getX();

                    final boolean inRecycle = x > mRecycleView.getLeft() && x < mRecycleView.getRight();

                    if(inRecycle && !pointToView.hasFocusable()){
                        if(mPerformClick == null){
                            mPerformClick = new PerformClick();
                        }
                        mPerformClick.x = event.getX();
                        mPerformClick.y = event.getY();

                        if(mTouchMode == TOUCH_MODE_DOWN || mTouchMode == TOUCH_MODE_TAP){
                            mRecycleView.removeCallbacks(mTouchMode == TOUCH_MODE_DOWN?
                                    mPendingCheckForTap:mPendingCheckForLongPress);
                            if(!isMove){
                                pointToView.setPressed(true);
                                mRecycleView.postDelayed(mPerformClick,ViewConfiguration.getPressedStateDuration());
                            }
                        }else{
                            mPerformClick.run();
                        }
                    }
                }
                mTouchMode = TOUCH_MODE_REST;
                break;
        }
    }


    /**
     *设置行点击事件
     */
    protected void setOnItemClickListener(OnItemClickListener listener){
        this.mClickListener = listener;
    }

    /**
     *设置行长按事件
     */
    protected void setOnItemLongClickListener(OnLongItemClickListener listener){
        this.mLongClickListener = listener;
    }

    private final class CheckForTap implements Runnable{

        float x,y;

        @Override
        public void run() {
            if(mTouchMode == TOUCH_MODE_DOWN){
                mTouchMode = TOUCH_MODE_TAP;
                final View pointToView = mRecycleView.findChildViewUnder(x,y);
                if(pointToView != null && !pointToView.hasFocusable()){
                    pointToView.setPressed(true);
                    mRecycleView.refreshDrawableState();
                    final int longPressTimeout = ViewConfiguration.getLongPressTimeout();
                    Logger.t(TAG).d("RecycleView.isLongClickable = "+mRecycleView.isLongClickable());
                    if(mRecycleView.isLongClickable()){
                        if(mPendingCheckForLongPress == null){
                            mPendingCheckForLongPress = new CheckForLongPress();
                        }
                        mPendingCheckForLongPress.x = x;
                        mPendingCheckForLongPress.y = y;
                        mRecycleView.postDelayed(mPendingCheckForLongPress,longPressTimeout);
                    }else{
                        mTouchMode = TOUCH_MODE_DONE_WAITING;
                    }
                }
            }
        }
    }

    private final class CheckForLongPress implements Runnable{

        float x,y;

        @Override
        public void run() {
            final View pointToView = mRecycleView.findChildViewUnder(x,y);
            if(pointToView != null && mLongClickListener != null && !isMove){
                RecyclerView.ViewHolder vh = mRecycleView.getChildViewHolder(pointToView);
                mLongClickListener.onLongItemClick(pointToView,vh.getAdapterPosition());
            }
            mTouchMode = TOUCH_MODE_REST;
            pointToView.setPressed(false);
        }
    }

    private final class PerformClick implements Runnable{

        float x,y;

        @Override
        public void run() {
            final View pointToView = mRecycleView.findChildViewUnder(x,y);
            if(pointToView != null && mClickListener != null){
                RecyclerView.ViewHolder vh = mRecycleView.getChildViewHolder(pointToView);
                mClickListener.onItemClick(pointToView,vh.getAdapterPosition());
            }
            mTouchMode = TOUCH_MODE_REST;
            pointToView.setPressed(false);
        }
    }

    //点击事件监听接口
    public interface OnItemClickListener{
        /**
         * 点击事件回调
         * @param itemView：当前被点击的view
         * @param position：当前被点击的位置
         */
        void onItemClick(View itemView, int position);
    }

    //长按事件监听接口
    public interface OnLongItemClickListener{
        /**
         * 长按事件回调
         * @param itemView：当前被长按的view
         * @param position：当前被长按的位置
         */
        void onLongItemClick(View itemView, int position);
    }


}
