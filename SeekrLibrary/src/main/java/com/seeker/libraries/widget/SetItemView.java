package com.seeker.libraries.widget;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.text.TextPaint;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.View;
import com.seeker.libraries.R;

/**
 * Created by Seeker on 2016/8/27.
 */

public class SetItemView extends View {

    private static final String TAG = "SetItemView";

    private static final int STYLE_NORMAL = 0;

    private static final int STYLE_ARROW = 1;

    private static final int STYLE_TOGGLE = 2;

    //默认控件的宽，匹配屏幕大小
    private static final int DEFAULT_WIDTH = -1;

    //默认控件的高度
    private static final int DEFAULT_HEIGHT = 50;

    //默认文字的大小
    private static final int DEFAULT_TEXTSIZE = 14;

    //默认文字的颜色
    private static final int DEFAULT_TEXTCOLOR = Color.parseColor("#5a5a5a");

    /***toggleButton默认宽*/
    private static final int TOGGLEBUTTON_WIDTH = 50;

    /***toggleButton默认高*/
    private static final int TOGGLEBUTTON_HEIGHT = 28;

    private static  final long DELAYDURATION = 10;

    private DisplayMetrics dm;

    private int style = STYLE_NORMAL;

    private Drawable drawableStart;

    private int drawableStartWidth = -2,drawableStartHeight = -2;

    private String textStart;

    private int textStartColor = DEFAULT_TEXTCOLOR;

    private int textStartSize = DEFAULT_TEXTSIZE;

    private int textStartLeftMargin = 0;

    private String textEnd;

    private int textEndPadding = 0;

    private int textEndColor = DEFAULT_TEXTCOLOR;

    private int textEndSize = DEFAULT_TEXTSIZE;

    private int textEndBackgroundColor = Color.TRANSPARENT;

    private int textEndRightMargin = 0;

    private Drawable arrowDrawable;

    private int arrowWidth = -2,arrowHeight = -2;

    /**默认宽**/
    private  int toggleWidth = 0;
    /**默认高**/
    private int toggleHeight = 0;
    /**开启颜色**/
    private int onColor = Color.parseColor("#4ebb7f");
    /**关闭颜色**/
    private int offColor = Color.parseColor("#dadbda");
    /**灰色带颜色**/
    private int areaColor = Color.parseColor("#dadbda");
    /**手柄颜色**/
    private int handlerColor = Color.parseColor("#ffffff");
    /**边框颜色**/
    private int borderColor = offColor;
    /**开关状态**/
    private boolean toggleOn = false;
    /**边框宽**/
    private int borderWidth = 2;
    /**纵轴中心**/
    private float centerY;
    /**按钮水平方向开始、结束的位置**/
    private float startX,endX;
    /**手柄x轴方向最小、最大值**/
    private float handlerMinX,handlerMaxX;
    /**手柄大小**/
    private int handlerSize;
    /**手柄在x轴的坐标位置**/
    private float handlerX;
    /**关闭时内部灰色带宽度**/
    private float areaWidth;
    /**是否使用动画效果**/
    private boolean animate = true;
    /**是否默认处于打开状态**/
    private boolean defaultOn = true;
    /**按钮半径**/
    private float radius;
    /**switchButton在X轴绘制的偏移量**/
    private float toggleButtonDrawStartX;

    /**switchButton在Y轴绘制的偏移量**/
    private float toggleButtonDrawStartY;
    /**整个toggleButton的区域**/
    private final RectF toggleRectF = new RectF();

    /**绘制toggleButton的画笔**/
    private Paint togglePaint;

    private OnToggleChangedListener mListener;

    private double currentDelay;

    private TextPaint mTextStartPaint,mTextEndPaint;

    private Paint textEndBackPaint;

    private float downX;

    public SetItemView(Context context) {
        this(context,null);
    }

    public SetItemView(Context context, AttributeSet attrs) {
        this(context, attrs,0);
    }

    public SetItemView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        dm = Resources.getSystem().getDisplayMetrics();
        setEnabled(true);
        setClickable(true);
        setLongClickable(false);
        setup(attrs);
    }

    //初始化view的属性
    private void setup(AttributeSet attrs){

        TypedArray ta = getContext().obtainStyledAttributes(attrs, R.styleable.SetItemView);
        if(ta != null){
            try {

                style = ta.getInt(R.styleable.SetItemView_itemStyle,STYLE_NORMAL);

                drawableStart = ta.getDrawable(R.styleable.SetItemView_drawableStart);
                if(drawableStart != null){
                    drawableStartWidth = ta.getDimensionPixelSize(
                            R.styleable.SetItemView_drawableStartWidth,applyDimension(drawableStartWidth));
                    drawableStartHeight = ta.getDimensionPixelSize(
                            R.styleable.SetItemView_drawableStartHeight,applyDimension(drawableStartHeight));
                }

                textStart = ta.getString(R.styleable.SetItemView_textStart);
                textStartColor = ta.getColor(R.styleable.SetItemView_textStartColor,textStartColor);
                textStartSize = ta.getDimensionPixelSize(
                        R.styleable.SetItemView_textStartSize,applyDimension(DEFAULT_TEXTSIZE));
                textStartLeftMargin = ta.getDimensionPixelOffset(
                        R.styleable.SetItemView_textStartLeftMargin,textStartLeftMargin);

                textEnd = ta.getString(R.styleable.SetItemView_textEnd);
                textEndPadding = ta.getDimensionPixelSize(R.styleable.SetItemView_textEndPadding,0);
                textEndColor = ta.getColor(R.styleable.SetItemView_textEndColor,textEndColor);
                textEndSize = ta.getDimensionPixelSize(
                        R.styleable.SetItemView_textEndSize,applyDimension(DEFAULT_TEXTSIZE));
                textEndBackgroundColor = ta.getColor(
                        R.styleable.SetItemView_textEndBackgroundColor,textEndBackgroundColor);
                textEndRightMargin = ta.getDimensionPixelSize(
                        R.styleable.SetItemView_textEndRightMargin,textEndRightMargin);

                if(style == STYLE_ARROW){
                    arrowDrawable = ta.getDrawable(R.styleable.SetItemView_arrowDrawable);
                    arrowWidth = ta.getDimensionPixelSize(
                            R.styleable.SetItemView_arrowWidth,TOGGLEBUTTON_WIDTH);
                    arrowHeight = ta.getDimensionPixelSize(
                            R.styleable.SetItemView_arrowHeight,TOGGLEBUTTON_HEIGHT);
                }else if(style == STYLE_TOGGLE){
                    toggleWidth = ta.getDimensionPixelSize(
                            R.styleable.SetItemView_toggleWidth,applyDimension(TOGGLEBUTTON_WIDTH));
                    toggleHeight = ta.getDimensionPixelSize(
                            R.styleable.SetItemView_toggleHeight,applyDimension(TOGGLEBUTTON_HEIGHT));
                    borderWidth = ta.getDimensionPixelSize(
                            R.styleable.SetItemView_boderWidth,borderWidth);
                    areaColor = ta.getColor(R.styleable.SetItemView_areaColor,areaColor);
                    onColor = ta.getColor(R.styleable.SetItemView_onColor,onColor);
                    offColor = ta.getColor(R.styleable.SetItemView_offColor,offColor);
                    handlerColor = ta.getColor(R.styleable.SetItemView_handlerColor,handlerColor);
                    animate = ta.getBoolean(R.styleable.SetItemView_animate,animate);
                    toggleOn = defaultOn = ta.getBoolean(R.styleable.SetItemView_defaultOn,defaultOn);
                    currentDelay = defaultOn?1:0;
                    caculateEffect(currentDelay);
                }
            }catch (Exception e){
                Log.e(TAG, "setup: e", e);
            }finally {
                ta.recycle();
            }
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        /**计算宽**/
        final int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int width = MeasureSpec.getSize(widthMeasureSpec);
        if(widthMode == MeasureSpec.UNSPECIFIED || widthMode == MeasureSpec.AT_MOST){
            width = applyDimension(DEFAULT_WIDTH);
        }

        /**计算高**/
        final int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int height = MeasureSpec.getSize(heightMeasureSpec);
        if (heightMode == MeasureSpec.UNSPECIFIED || heightMode == MeasureSpec.AT_MOST) {
            height = applyDimension(DEFAULT_HEIGHT);
        }

        Log.i(TAG, "[onMeasure] width = " + width + ",height = " + height);

        if(style == STYLE_TOGGLE){
            setToggleValue();
        }

        setMeasuredDimension(width,height);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        drawStartDrawable(canvas);
        drawTextStart(canvas);
        switch (style){
            case STYLE_ARROW:
                drawArrowDrawable(canvas);
                break;
            case STYLE_TOGGLE:
                drawToggleButton(canvas);
                break;
        }
        drawTextEnd(canvas);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        final int action = event.getActionMasked();

        switch (action){
            case MotionEvent.ACTION_DOWN:
                downX = event.getX();
                break;
            case MotionEvent.ACTION_UP:
                if(style == STYLE_TOGGLE && downX >= toggleButtonDrawStartX){
                    toggle();
                }
                break;
        }
        return super.onTouchEvent(event);
    }

    /**
     * 绘制drawableStart
     * @param canvas
     */
    private void drawStartDrawable(Canvas canvas){
        Log.d(TAG, "drawStartDrawable: ");
        if (drawableStart != null){
            mesureDrawableStartSize();
            final float startY = (getHeight() - drawableStartHeight) / 2.0f;
            canvas.save();
            canvas.translate(getPaddingLeft(),startY);
            drawableStart.setBounds(0,0,drawableStartWidth,drawableStartHeight);
            drawableStart.draw(canvas);
            canvas.restore();
        }
    }

    /**
     * 绘制textStart
     * @param canvas
     */
    private void drawTextStart(Canvas canvas){
        Log.d(TAG, "drawTextStart: ");
        notNull(textStart,"textStart");
        assumeTextStartPaint();
        Paint.FontMetrics fontMetrics = mTextStartPaint.getFontMetrics();
        final int left = getPaddingLeft()+drawableStartWidth+textStartLeftMargin;
        RectF target = new RectF(left,0,left+mTextStartPaint.measureText(textStart),getHeight());
        int baseLine =  (int)((target.bottom + target.top - fontMetrics.bottom - fontMetrics.top) / 2.0f);
        canvas.save();
        canvas.translate(left,baseLine);
        canvas.drawText(textStart,0,0,mTextStartPaint);
        canvas.restore();
    }

    /**
     * 绘制arrowDrawable
     * @param canvas
     */
    private void drawArrowDrawable(Canvas canvas){
        Log.d(TAG, "drawArrowDrawable: ");
        notNull(arrowDrawable,"arrowDrawable");
        mesureDrawablerrowSize();
        final float startY = (getHeight() - arrowHeight) / 2.0f;
        canvas.save();
        canvas.translate(getWidth() - getPaddingRight(),startY);
        arrowDrawable.setBounds(0,0,arrowWidth,arrowHeight);
        arrowDrawable.draw(canvas);
        canvas.restore();
    }

    /**
     * 绘制开关，toggleButton
     * @param canvas
     */
    private void drawToggleButton(Canvas canvas){
        Log.d(TAG, "drawToggleButton: ");
        assumeTogglePaint();
        canvas.save();
        canvas.translate(toggleButtonDrawStartX,toggleButtonDrawStartY);
        /**绘制整个按钮**/
        toggleRectF.set(0, 0, toggleWidth, toggleHeight);
        togglePaint.setColor(borderColor);
        canvas.drawRoundRect(toggleRectF, radius, radius,togglePaint);

        /**绘制关闭灰色区域**/
        if(areaWidth > 0 ){
            final float cy = areaWidth * 0.5f;
            toggleRectF.set(handlerX - cy, centerY - cy, endX + cy, centerY + cy);
            togglePaint.setColor(offColor);
            canvas.drawRoundRect(toggleRectF,cy,cy,togglePaint);
        }

        /**绘制手柄**/
        final float handlerRadius = handlerSize * 0.5f;
        toggleRectF.set(handlerX - handlerRadius, centerY - handlerRadius, handlerX + handlerRadius, centerY + handlerRadius);
        togglePaint.setColor(handlerColor);
        canvas.drawRoundRect(toggleRectF, handlerRadius, handlerRadius, togglePaint);
        canvas.restore();
    }

    /**
     * 绘制textEnd
     * @param canvas
     */
    private void drawTextEnd(Canvas canvas){
        Log.d(TAG, "drawTextEnd: ");
        if(hasTextEnd()){
            assumeTextEndPaint();
            Paint.FontMetrics fontMetrics = mTextEndPaint.getFontMetrics();
            int other = 0;
            if(STYLE_NORMAL == style){
                other = 0;
            }else if(STYLE_ARROW == style){
                other = arrowWidth;
            }else if(STYLE_TOGGLE == style){
                other = toggleWidth;
            }

            //计算背景
            final float backWidth = mTextEndPaint.measureText(textEnd) + textEndPadding *2;
            final float backHeight = (float) Math.ceil(fontMetrics.descent - fontMetrics.ascent) + textEndPadding;

            final float backDrawX = getWidth()-(getPaddingRight()+backWidth+textEndRightMargin+other);
            final float backDrawY = (getHeight() - backHeight) / 2.0f;
            final RectF textBackRectF = new RectF(0,0,backWidth,backHeight);
            final float drawRadius = Math.min(backWidth,backHeight) * 0.5f;
            //绘制背景
            canvas.save();
            canvas.translate(backDrawX,backDrawY);
            canvas.drawRoundRect(textBackRectF,drawRadius,drawRadius,textEndBackPaint);
            canvas.restore();

            final float textDrawLeft = backDrawX +textEndPadding;
            final float textDrawRight = textDrawLeft+mTextEndPaint.measureText(textEnd);
            RectF target = new RectF(textDrawLeft,0,textDrawRight,getHeight());
            int baseLine =  (int)((target.bottom + target.top - fontMetrics.bottom - fontMetrics.top) / 2.0f);
            //绘制文字信息
            canvas.save();
            canvas.translate(textDrawLeft,baseLine);
            canvas.drawText(textEnd,0,0,mTextEndPaint);
            canvas.restore();
        }
    }

    /**
     * 确定drawableStart的大小
     */
    private void mesureDrawableStartSize(){
        drawableStartWidth =
                drawableStartWidth >=0?drawableStartWidth:drawableStart.getIntrinsicWidth();
        drawableStartHeight =
                drawableStartHeight >= 0?drawableStartHeight:drawableStart.getIntrinsicHeight();
    }

    /**
     * 确定arrowDrawable的大小
     */
    private void mesureDrawablerrowSize(){
        arrowWidth = arrowWidth >= 0?arrowWidth:arrowDrawable.getIntrinsicWidth();
        arrowHeight = arrowHeight >= 0?arrowHeight:arrowDrawable.getIntrinsicHeight();
    }

    /**
     * 初始设置toggleButton的一些属性值
     */
    private void setToggleValue(){
        radius = Math.min(toggleWidth,toggleHeight) * 0.5f;
        centerY = radius;
        startX = centerY;
        endX = toggleWidth - radius;
        handlerMinX = startX + borderWidth;
        handlerMaxX = endX - borderWidth;
        handlerSize = toggleHeight - 4*borderWidth;
        handlerX = toggleOn?handlerMaxX:handlerMinX;
        areaWidth = 0;

        toggleButtonDrawStartX = getWidth() - getPaddingRight() - toggleWidth;
        toggleButtonDrawStartY = (getHeight() - toggleHeight) / 2.0f;
    }

    /**
     * 初始化检测textStartPaint
     */
    private void assumeTextStartPaint(){
        if (mTextStartPaint == null){
            final Resources res = getResources();
            mTextStartPaint = new TextPaint(Paint.ANTI_ALIAS_FLAG);
            mTextStartPaint.density = res.getDisplayMetrics().density;
            mTextStartPaint.setTextSize(textStartSize);
            mTextStartPaint.setColor(textStartColor);
            mTextStartPaint.setAntiAlias(true);
        }
    }

    /**
     * 初始化检测textEndPaint
     */
    private void assumeTextEndPaint(){

        if (mTextEndPaint == null){
            final Resources res = getResources();
            mTextEndPaint = new TextPaint(Paint.ANTI_ALIAS_FLAG);
            mTextEndPaint.density = res.getDisplayMetrics().density;
            mTextEndPaint.setTextSize(textEndSize);
            mTextEndPaint.setColor(textEndColor);
            mTextEndPaint.setAntiAlias(true);
        }

        if(textEndBackPaint == null){
            textEndBackPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
            textEndBackPaint.setColor(textEndBackgroundColor);
            textEndBackPaint.setStyle(Paint.Style.FILL);
            textEndBackPaint.setStrokeCap(Paint.Cap.ROUND);
            textEndBackPaint.setAntiAlias(true);
        }
    }

    /**
     * 初始化检测togglePaint
     */
    private void assumeTogglePaint(){
        if(togglePaint == null){
            togglePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
            togglePaint.setStyle(Paint.Style.FILL);
            togglePaint.setStrokeCap(Paint.Cap.ROUND);
            togglePaint.setAntiAlias(true);
        }
    }

    /**
     * 设置textEnd字符显示
     * @param textEnd
     */
    public void setTextEnd(String textEnd){
        this.textEnd = textEnd;
        invalidate();
    }

    /**
     * 设置textEnd背景色
     * @param color
     */
    public void setTextEndBackColor(int color){
        this.textEndBackgroundColor = color;
        invalidate();
    }

    /**
     * 开关状态切换
     */
    public void toggle(){
        toggle(animate);
    }
    /**
     * 开关状态切换
     * @param animate
     */
    public void toggle(boolean animate){
        toggleOn = !toggleOn;
        takeEffect(animate);
    }

    /**
     * 开启状态
     */
    public void toggleOn(){
        toggleOn(animate);
    }
    /**
     * 开启状态
     * @param animate
     */
    public void toggleOn(boolean animate){
        toggleOn = true;
        takeEffect(animate);
    }

    /**
     * 关闭状态
     */
    public void toggleOff(){
        toggleOff(animate);
    }
    /**
     * 关闭状态
     * @param animate
     */
    public void toggleOff(boolean animate){
        toggleOn = false;
        takeEffect(animate);
    }

    /**
     * 开始处理状态切换
     * @param animate
     */
    private void takeEffect(boolean animate){
        if(animate){
            postDelayed(toggleRunnable, DELAYDURATION);
        }else {
            nowInvalidate(toggleOn ? 1 : 0);
        }
        if(mListener != null){
            mListener.onToggle(toggleOn);
        }
    }

    /**
     * 时时计算
     * @param value
     */
    private void caculateEffect(double value){

        handlerX = (float)mapValueFromRangeToRange(value,0,1.0,handlerMinX,handlerMaxX);
        areaWidth = (float)mapValueFromRangeToRange(1.0-value,0,1.0,10,handlerSize);
        Log.d(TAG, "caculateEffect: handlerMinX ="+handlerMinX+",handlerMaxX = "+handlerMaxX
                +",handlerX = "+handlerX);
        final int fb = Color.blue(onColor);
        final int fr = Color.red(onColor);
        final int fg = Color.green(onColor);

        final int tb = Color.blue(offColor);
        final int tr = Color.red(offColor);
        final int tg = Color.green(offColor);

        int sb = (int) mapValueFromRangeToRange(1.0 - value, 0, 1.0, fb, tb);
        int sr = (int) mapValueFromRangeToRange(1.0 - value, 0, 1.0, fr, tr);
        int sg = (int) mapValueFromRangeToRange(1.0 - value, 0, 1.0, fg, tg);

        sb = clamp(sb, 0, 255);
        sr = clamp(sr, 0, 255);
        sg = clamp(sg, 0, 255);

        borderColor = Color.rgb(sr, sg, sb);
    }

    private void nowInvalidate(double value){
        caculateEffect(value);
        invalidate();
    }

    private int clamp(int value, int low, int high) {
        return Math.min(Math.max(value, low), high);
    }
    /**
     * Map a value within a given range to another range.
     * @param value the value to map
     * @param fromLow the low end of the range the value is within
     * @param fromHigh the high end of the range the value is within
     * @param toLow the low end of the range to map to
     * @param toHigh the high end of the range to map to
     * @return the mapped value
     */
    private  double mapValueFromRangeToRange(
            double value, double fromLow, double fromHigh,
            double toLow, double toHigh) {
        if(value >= 1){
            value = 1;
        }else if(value <= 0){
            value = 0;
        }
        double fromRangeSize = fromHigh - fromLow;
        double toRangeSize = toHigh - toLow;
        double valueScale = (value - fromLow) / fromRangeSize;
        return toLow + (valueScale * toRangeSize);
    }

    private final Runnable toggleRunnable = new Runnable() {
        @Override
        public void run() {
            if(toggleOn){
                if(currentDelay <= 1){
                    nowInvalidate(currentDelay);
                    postDelayed(toggleRunnable,DELAYDURATION);
                    currentDelay = currentDelay + 0.1;
                }else{
                    currentDelay = 1;
                }
            }else{
                if(currentDelay >= 0){
                    nowInvalidate(currentDelay);
                    postDelayed(toggleRunnable, DELAYDURATION);
                    currentDelay = currentDelay - 0.1;
                }else{
                    currentDelay = 0;
                }
            }
        }
    };

    /**
     * 是否绘制结束文字
     */
    private boolean hasTextEnd(){
        return TextUtils.isEmpty(textEnd)?false:true;
    }

    /**
     * 空值检测，null，抛空指针异常
     * @param arg
     * @param name
     */
    private void notNull(Object arg, String name) {
        if (arg == null) {
            throw new NullPointerException("Argument '" + name + "' cannot be null.");
        }
    }

    /**
     * px2dp
     * @param value
     */
    private int applyDimension(float value){
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,value,dm);
    }

    /**
     * 设置开关监听
     */
    public void setOnToggleChangedlistener(OnToggleChangedListener listener){
        this.mListener = listener;
    }
    /**
     * 开关状态监听
     */
    public interface OnToggleChangedListener{
        /**
         * 是否开启
         * @param on
         */
        void onToggle(boolean on);
    }
}
