package com.seeker.libraries.weight.recycleView;

import android.graphics.Rect;
import android.support.v4.view.ViewCompat;
import android.support.v4.view.ViewConfigurationCompat;
import android.support.v7.widget.RecyclerView;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import com.seeker.libraries.logger.Logger;

/**
 * Created by Seeker on 2016/8/5.
 */

public class CheckItemTouchListener implements RecyclerView.OnItemTouchListener{

    private static final String TAG = "CheckItemTouchListener";
    
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

    /**
     * 滚动方向，none
     */
    private static final int MOVE_I = -1;

    /**
     * 水平滚动，即子view打开
     */
    private static final int MOVE_H = 0;

    /**
     * 竖直方向，即recycleView滚动
     */
    private static final int MOVE_V = 1;

    private int moveDirection = MOVE_I;

    private RecyclerView mRecycleView;

    /**
     * The X value associated with the the down motion event
     */
    private float mMotionDownX;

    /**
     * The Y value associated with the the down motion event
     */
    private float mMotionDownY;

    private float mLastX,mLastY;

    /**
     * 点击事件监听
     */
    private OnItemClickListener mClickListener;
    /**
     * 长按事件的监听
     */
    private OnLongItemClickListener mLongClickListener;

    /**
     * overflow监听
     */
    private OverflowClickListener overflowClickListener;

    private CheckForLongPress mPendingCheckForLongPress;

    private CheckForTap mPendingCheckForTap;

    private PerformClick mPerformClick;

    /**
     * associated with pointToView,if moved true, else false
     */
    private boolean isMove = false;

    /**
     * 当前touch事件下，pointToView是否可以往下执行click以及longPress监听
     */
    private boolean canClick = true;

    /**
     * 判断是否有子view处于打开状态
     */
    private boolean hasChildOpen = false;

    /**
     * 当recycleView滑动时，拦截此次所有事件
     */
    private boolean canIntercept = false;

    private float mTouchSlop;

    private View pointToView;

    private ScrollOverflowLayout soflChild;

    private ViewGroup contentGroup;

    private final Rect outRect = new Rect();

    public CheckItemTouchListener(RecyclerView recyclerView){
        this.mRecycleView = recyclerView;
        this.mTouchSlop = ViewConfigurationCompat.getScaledPagingTouchSlop(ViewConfiguration.get(mRecycleView.getContext()));
    }

    @Override
    public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent me) {

        final int actionMasked = me.getActionMasked();

        /**
         * 判断RecycleView是否需要拦截此次事件
         */
        if(actionMasked == MotionEvent.ACTION_DOWN){
            canIntercept = mRecycleView.getScrollState() != RecyclerView.SCROLL_STATE_IDLE;
        }

        if(!canIntercept){
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
                    Logger.t(TAG).d("MotionEvent.ACTION_CANCEL");
                    if(pointToView != null){
                        pointToView.setPressed(false);
                    }
                }break;
            }
        }
        return false;
    }

    @Override
    public void onTouchEvent(RecyclerView rv, MotionEvent me) {

    }

    @Override
    public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {

    }

    private void onTouchDown(MotionEvent event){
        Logger.t(TAG).d("[onTouchDown]");

        mMotionDownX =  event.getX();
        mMotionDownY =  event.getY();
        mLastX = mMotionDownX;
        mLastY = mMotionDownY;
        canClick = true;

        isMove = canCloseItemIfNeed();

        if(mPendingCheckForTap == null){
            mPendingCheckForTap = new CheckForTap();
        }

        if(canClick)
            mRecycleView.postDelayed(mPendingCheckForTap,ViewConfiguration.getTapTimeout());

        pointToView = mRecycleView.findChildViewUnder(mMotionDownX,mMotionDownY);

        initScrollView(pointToView);

        moveDirection = MOVE_I;
        mTouchMode = TOUCH_MODE_DOWN;
    }

    private void onTouchMove(MotionEvent event){
        if(pointToView == null){
            pointToView = mRecycleView.findChildViewUnder(mMotionDownX,mMotionDownY);
        }
        switch (mTouchMode){
            case TOUCH_MODE_DOWN:
            case TOUCH_MODE_TAP:
            case TOUCH_MODE_DONE_WAITING:

                if(startHScrollIfNeed(pointToView,event.getX(),event.getY())){
                    break;
                }

                if(!pointInView(getSoflChildRect(),(int)event.getX(),(int)event.getY())){
                    pointToView.setPressed(false);
                    mRecycleView.removeCallbacks(mTouchMode == TOUCH_MODE_DOWN? mPendingCheckForTap:mPendingCheckForLongPress);
                    mTouchMode = TOUCH_MODE_DONE_WAITING;
                }
                break;
        }
    }

    private void onTouchUp(MotionEvent event){
        Logger.t(TAG).d("[onTouchUp]");
        if(pointToView == null){
            pointToView = mRecycleView.findChildViewUnder(mMotionDownX,mMotionDownY);
        }

        if(canAutoOpenOrCloseItem(event.getX(),event.getY())){
            return;
        }

        /**
         * 开始处理点击overflow的操作
         */
        if(canDoWorkIfNeed(event.getX(),event.getY()) != null){
            return;
        }

        switch (mTouchMode){
            case TOUCH_MODE_DOWN:
            case TOUCH_MODE_TAP:
            case TOUCH_MODE_DONE_WAITING:
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
                        }

                        if(canClick && !hasChildOpen){
                            pointToView.setPressed(true);
                            mRecycleView.postDelayed(mPerformClick,ViewConfiguration.getPressedStateDuration());
                        }
                    }
                }
                mTouchMode = TOUCH_MODE_REST;
                break;
        }
    }

    /**
     * 当有item打开时，点击的时候，判断此item点击的view
     * @param x
     * @param y
     * @return
     */
    private View canDoWorkIfNeed(float x,float y){
        if(hasChildOpen && pointInView(getSoflChildRect(),(int) x,(int) y)){
            ViewGroup overflow = (ViewGroup)soflChild.getOverflowContainer().getChildAt(0);
            final int overflowCount = overflow.getChildCount();
            Rect overChildRect = new Rect();
            Rect overflowRect = getOverflowRect();
            for(int i = 0;i < overflowCount;i++){
                View child = overflow.getChildAt(i);
                child.getHitRect(overChildRect);
                int width = overChildRect.right - overChildRect.left;
                overChildRect.left += overflowRect.left;
                overChildRect.right = overChildRect.left+width;
                overChildRect.top = overflowRect.top;
                overChildRect.bottom = overflowRect.bottom;
                if(overChildRect.contains((int) x ,(int)y )){
                    if(overflowClickListener != null){
                        overflowClickListener.onOveflowClick(child,mRecycleView.getChildAdapterPosition(pointToView));
                    }
                    return child;
                }
            }
        }
        return null;
    }

    /**
     * 子view中contentContainer横向滚动
     * @param childView
     * @param x
     * @param y
     * @return
     */
    private boolean startHScrollIfNeed(View childView,float x,float y){

        if(Math.max(Math.abs(x - mMotionDownX),Math.abs(y - mMotionDownY)) > mTouchSlop || isMove){
            isMove = true;
            canClick = false;
        }else {
            return false;
        }

        if(moveDirection == MOVE_I){
            moveDirection = Math.abs(x - mMotionDownX)>Math.abs(y - mMotionDownY)?MOVE_H:MOVE_V;
        }

        mRecycleView.removeCallbacks(mPendingCheckForLongPress);
        pointToView.setPressed(false);

        /**
         * 事件拦截，当第一次判断是滑动手势时，滚动方向是否为横向滚动
         */
        if(!(childView instanceof ScrollOverflowLayout) || moveDirection != MOVE_H){
            return false;
        }

        float delatX = 0;

        /**
         * 禁止向右滑出屏幕
         */
        if(x >= mMotionDownX){
            hasChildOpen = false;
            delatX = 0;
            mLastX = x;
            contentGroup.scrollTo(0,0);
        }else
        /**
         * overflow界面还未完全展示
         */
        if(mMotionDownX - x < soflChild.getOverflowLayoutWidth()){
            hasChildOpen = false;
            contentGroup.scrollBy((int) (mLastX - x),0);
            mLastX = x;
        }else
        /**
         * 当overflow界面全部显示出来之后，禁止在往左滑动,但可以右滑
         */
        if(mMotionDownX - x >= soflChild.getOverflowLayoutWidth()){
            hasChildOpen = true;
            contentGroup.scrollTo(soflChild.getOverflowLayoutWidth(),0);
            mLastX = x;
//            delatX = mLastX - soflChild.getLeft() + soflChild.getOverflowLayoutWidth();
//            mLastX = soflChild.getLeft() - soflChild.getOverflowLayoutWidth();
        }

        return true;
    }

    /**
     * 关闭打开的child
     */
    private boolean canCloseItemIfNeed(){

        if(!hasChildOpen || soflChild == null || pointToView == null){
            canClick = true;
            return false;
        }

        final boolean inOpenView = pointInView(getSoflChildRect(),(int) mMotionDownX,(int)mMotionDownY);

        canClick = !inOpenView;

        final boolean inContent = pointInView(getContentRect(),(int) mMotionDownX,(int)mMotionDownY);

        Logger.t(TAG).d("inOpenView = "+inOpenView+",inContent = "+inContent);

        final boolean canClose = !inOpenView || (inOpenView && inContent);

        if(canClose){
            return autoScrollItem(soflChild.getOverflowLayoutWidth(),
                    -soflChild.getOverflowLayoutWidth(),false);
        }
        return false;
    }

    /**
     * 判断是否需要自动打开或者关闭item，判断依据是横向滑动的距离>overflowWidth / 2;
     * @param x
     * @param y
     * @return
     */
    private boolean canAutoOpenOrCloseItem(float x,float y){
        if(isMove && moveDirection == MOVE_H && soflChild != null && !hasChildOpen && x < mMotionDownX){
            final int distance = (int) Math.abs(x - mMotionDownX);
            final int overflowWidth = soflChild.getOverflowLayoutWidth();
            if(distance > overflowWidth / 2){
                autoScrollItem(distance,overflowWidth - distance,true);
            }else {
                autoScrollItem(distance,-distance,false);
            }
            return true;
        }
        return false;
    }

    /**
     * 外部调用主动关闭打开的item
     */
    protected boolean closeOpenedItem(){
        if (soflChild != null && hasChildOpen)
           return autoScrollItem(soflChild.getOverflowLayoutWidth(),
                   -soflChild.getOverflowLayoutWidth(),false);
        return false;
    }

    /**
     * 清除赋值
     */
    private void setNull(){
        pointToView = null;
        soflChild = null;
        contentGroup = null;
    }

    /**
     * @param startX
     * @param delatX
     * @param open
     * @return
     */
    protected boolean autoScrollItem(int startX, int delatX,boolean open){
        if(contentGroup != null && contentGroup instanceof ScrollHelper){
            ScrollHelper ccl = (ScrollHelper)contentGroup;
            ccl.begin(startX,0,delatX,0);
        }else {
            contentGroup.scrollBy(delatX,0);
        }
        hasChildOpen = open;
        return true;
    }

    /**
     * 判断当前手势操作是否还在rect之内
     * @param localX
     * @param localY
     * @return
     */
    private boolean pointInView(Rect rect,int localX,int localY){
        Rect out = new Rect();
        mRecycleView.getDrawingRect(out);
        if(!out.contains(localX,localY)){
            return false;
        }
        if (localX >= rect.left && localX <= rect.right &&
                localY >= rect.top && localY <= rect.bottom) {
            return true;
        }
        return false;
    }

    /**
     * 获取soflChild的范围
     * @return
     */
    private Rect getSoflChildRect(){
        if(pointToView != null){
            final float translationX = ViewCompat.getTranslationX(pointToView);
            final float translationY = ViewCompat.getTranslationY(pointToView);
            outRect.left = (int) (pointToView.getLeft() + translationX);
            outRect.right = (int)(pointToView.getRight() + translationX);
            outRect.top = (int) (pointToView.getTop() + translationY);
            outRect.bottom = (int) (pointToView.getBottom() +translationY);
        }else {
            outRect.set(0,0,0,0);
        }

        return outRect;
    }

    /**
     * 获取soflChild中contentGroup的范围
     * @return
     */
    private Rect getContentRect(){
        Rect rect = getSoflChildRect();
        if(soflChild != null){
            rect.right -= soflChild.getOverflowLayoutWidth();
        }else{
            rect.set(0,0,0,0);
        }
        return rect;
    }

    /**
     * 获取soflChild中overflow的范围
     * @return
     */
    private Rect getOverflowRect(){
        Rect rect = getSoflChildRect();
        rect.left = rect.right - soflChild.getOverflowLayoutWidth();
        return rect;
    }

    /**
     * 当点击的时候初始化判断根布局是否是ScrollOverflowLayout
     */
    private void initScrollView(View child){
        if(child instanceof ScrollOverflowLayout){
            soflChild = (ScrollOverflowLayout)child;
            contentGroup = soflChild.getContentContainer();
        }else {
            soflChild = null;
            contentGroup = null;
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

    protected void setOverflowClickListener(OverflowClickListener listener){
        this.overflowClickListener = listener;
    }

    private final class CheckForTap implements Runnable{

        @Override
        public void run() {
            if(mTouchMode == TOUCH_MODE_DOWN){
                mTouchMode = TOUCH_MODE_TAP;
                if(pointToView != null && !pointToView.hasFocusable()){
                    pointToView.setPressed(true);
                    mRecycleView.refreshDrawableState();
                    final int longPressTimeout = ViewConfiguration.getLongPressTimeout();
                    Logger.t(TAG).d("RecycleView.isLongClickable = "+mRecycleView.isLongClickable());
                    if(mRecycleView.isLongClickable()){
                        if(mPendingCheckForLongPress == null){
                            mPendingCheckForLongPress = new CheckForLongPress();
                        }
                        mRecycleView.postDelayed(mPendingCheckForLongPress,longPressTimeout);
                    }else{
                        mTouchMode = TOUCH_MODE_DONE_WAITING;
                    }
                }
            }
        }
    }

    private final class CheckForLongPress implements Runnable{

        @Override
        public void run() {
            if(pointToView != null && mLongClickListener != null && canClick && !hasChildOpen){
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
            if(pointToView != null && mClickListener != null){
                pointToView.setPressed(true);
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
         * @param view：当前被点击的view
         * @param position：当前被点击的位置
         */
        void onItemClick(View view, int position);
    }

    //长按事件监听接口
    public interface OnLongItemClickListener{
        /**
         * 长按事件回调
         * @param view：当前被长按的view
         * @param position：当前被长按的位置
         */
        void onLongItemClick(View view, int position);
    }

    //overflow布局监听回调
    public interface OverflowClickListener{
        /**
         *
         * @param view,被点击的view
         * @param position,当前所在的位置
         */
        void onOveflowClick(View view,int position);
    }
}
