package com.seeker.libraries.widget;

import android.content.Context;
import android.graphics.Rect;
import android.support.v4.widget.NestedScrollView;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.TranslateAnimation;

/**
 * Created by Seeker on 2016/7/28.
 *
 * 反弹阻尼效果
 */

public class ElasticNestedScrollView extends NestedScrollView {

    private View childView;

    private float downY;

    private Rect normal = new Rect();

    private boolean ifCanCount = false;//是否开始计算

    public ElasticNestedScrollView(Context context){
        this(context,null);
    }

    public ElasticNestedScrollView(Context context, AttributeSet attributeSet){
        super(context,attributeSet);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        childView = getChildAt(0);
    }

    /***
     * 监听touch
     */
    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        if (childView != null) {
            commOnTouchEvent(ev);
        }
        return super.onTouchEvent(ev);
    }

    /***
     * 触摸事件
     *
     * @param ev
     */
    public void commOnTouchEvent(MotionEvent ev) {
        int action = ev.getAction();
        switch (action) {
            case MotionEvent.ACTION_DOWN:
                break;
            case MotionEvent.ACTION_UP:
                // 手指松开.
                if (isNeedAnimation()) {
                    animation();
                    ifCanCount = false;
                }
                break;
            /**
             * 排除出第一次移动计算，因为第一次无法得知y坐标， 在MotionEvent.ACTION_DOWN中获取不到，
             * 因为此时是MyScrollView的touch事件传递到到了LIstView的孩子item上面.所以从第二次计算开始.
             * 然而我们也要进行初始化，就是第一次移动的时候让滑动距离归0. 之后记录准确了就正常执行.
             */
            case MotionEvent.ACTION_MOVE:
                final float preY = downY;// 按下时的y坐标
                float nowY = ev.getY();// 时时y坐标
                int deltaY = (int) (preY - nowY);// 滑动距离
                if (!ifCanCount) {
                    deltaY = 0; // 在这里要归0.
                }

                downY = nowY;
                // 当滚动到最上或者最下时就不会再滚动，这时移动布局
                if (isNeedMove()) {
                    // 初始化头部矩形
                    if (normal.isEmpty()) {
                        // 保存正常的布局位置
                        normal.set(childView.getLeft(), childView.getTop(),childView.getRight(),
                                childView.getBottom());
                    }
                    // 移动布局
                    childView.layout(childView.getLeft(), childView.getTop() - deltaY / 2,
                            childView.getRight(), childView.getBottom() - deltaY / 2);
                }
                ifCanCount = true;
                break;

            default:
                break;
        }
    }

    /***
     * 回缩动画
     */
    public void animation() {
        // 开启移动动画
        TranslateAnimation ta = new TranslateAnimation(0, 0, childView.getTop(),normal.top);
        ta.setDuration(200);
        childView.startAnimation(ta);
        // 设置回到正常的布局位置
        childView.layout(normal.left, normal.top, normal.right, normal.bottom);
        normal.setEmpty();
    }

    // 是否需要开启动画
    public boolean isNeedAnimation() {
        return !normal.isEmpty();
    }

    /***
     * 是否需要移动布局 childView.getMeasuredHeight():获取的是控件的总高度
     *
     * getHeight()：获取的是屏幕的高度
     *
     * @return
     */
    public boolean isNeedMove() {
        int offset = childView.getMeasuredHeight() - getHeight();
        int scrollY = getScrollY();
        // 0是顶部，后面那个是底部
        if (scrollY == 0 || scrollY == offset) {
            return true;
        }
        return false;
    }
}
