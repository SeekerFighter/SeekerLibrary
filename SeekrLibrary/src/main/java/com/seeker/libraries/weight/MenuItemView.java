package com.seeker.libraries.weight;

/**
 * Created by Seeker on 2016/7/21.
 */
import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.RectShape;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import com.seeker.libraries.R;

/**
 * 设置，菜单中的布局项
 * 默认控件是纵向居中显示，所有paddingTop和paddingBottom在这没有作用
 * Created by dandy on 2016/4/14.
 */
public class MenuItemView extends View{

    private static final String TAG = "MenuItemView";

    /**默认控件的高**/
    private static final int HEIGHT_DEFAULT = 50;

    /**文字绘制时默认大小**/
    private static final int TEXTSZIE_DEFAULT = 14;

    /**尾部箭头图标大小**/
    private static final int ARROW_SIZE = 13;

    /***SwitchButton默认宽*/
    private static final int SWITCHBUTTON_WIDTH = 50;

    /***SwitchButton默认高*/
    private static final int SWITCHBUTTON_HEIGHT = 28;

    private static  final long DELAYDURATION = 10;

    /**头标**/
    private Drawable headerDrawable;

    /**头标大小,宽高一致**/

    private int headDrawableSize = -1;

    /**头标宽**/
    private int headerDrawableWidth;
    /**头标高**/
    private int headerDrawableHeight;

    /**绘制头标时，画布在Y轴的绘制偏移量**/
    private float headerDrawableStartDrawY;

    /**文字与图片间的距离**/
    private int drawablePadding = 0;

    /**头部文字提示**/
    private String textHeader;

    /**文字颜色**/
    private int textHeaderColor = Color.parseColor("#5a5a5a");

    /**文字大小**/
    private int textSize = -1;

    /**绘制文字的画笔**/
    private Paint textPaint;

    /** 尾部 > 图片**/
    private Drawable arrowDrawable;

    /**尾部 > 大小**/
    private int arrowSize = -1;

    /** > 绘制的X轴偏移量**/
    private float arrowStartDrawX;

    /** > 绘制的Y轴偏移量**/
    private float arrowStartDrawY;

    /**尾部宽**/
    private int arrowDrawableWidth;
    /**尾部高**/
    private int arrowDrawableHeight;

    /**绘制arrow画笔**/
    private Paint arrowPaint;

    /**arrowPaint 颜色**/
    private int arrowColor = Color.parseColor("#5a5a5a");

    private DisplayMetrics dm;

    /*以下是绘制SwitchButton所用到的参数*/

    private ItemType itemType = ItemType.NORMAL;

    /**默认宽**/
    private  int toggleWidth = -1;

    /**默认高**/
    private int toggleHeight = -1;

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

    /**整个switchButton的区域**/
    private RectF toggleRectF = new RectF();

    /**绘制switchButton的画笔**/
    private Paint togglePaint;

    private OnToggleChangedListener mListener;

    private double currentDelay;

    private float downX = 0;

    /**switchButton在X轴绘制的偏移量**/
    private float switchButtonDrawStartX;

    /**switchButton在Y轴绘制的偏移量**/
    private float switchButtonDrawStartY;

    /**分割线，默认在底部绘制**/
    private Drawable dividerr;

    /**分割线绘制的宽**/
    private int dividerWidth = 2;

    /**是否需要绘制分割线**/
    private boolean dividerVisibilty = false;

    private boolean isPressed = false;

    public MenuItemView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setup(attrs);
    }
    public MenuItemView(Context context, AttributeSet attrs) {
        super(context, attrs);
        setup(attrs);
    }

    /**
     * 初始化控件,获取相关的控件属性
     * @param attrs
     */
    private void setup(AttributeSet attrs){

        dm = Resources.getSystem().getDisplayMetrics();

        TypedArray typedArray = getContext().obtainStyledAttributes(attrs, R.styleable.MenuItemView);
        if(typedArray != null){
            try {
                int count = typedArray.getIndexCount();
                for(int i = 0;i < count;i++){
                    int attr = typedArray.getIndex(i);
                    if(attr == R.styleable.MenuItemView_itemType){
                        itemType = ItemType.getValue(typedArray.getInt(attr, ItemType.NORMAL.ordinal()));
                    }else if(attr == R.styleable.MenuItemView_headerDrawable){
                        headerDrawable = typedArray.getDrawable(attr);
                    }else if(attr == R.styleable.MenuItemView_headerSize){
                        headDrawableSize = typedArray.getDimensionPixelSize(attr,headDrawableSize);
                    }else if(attr == R.styleable.MenuItemView_drawPadding){
                        drawablePadding = typedArray.getDimensionPixelSize(attr, drawablePadding);
                    }else if(attr == R.styleable.MenuItemView_textHeader){
                        textHeader = typedArray.getString(attr);
                    }else if(attr == R.styleable.MenuItemView_textColor){
                        textHeaderColor = typedArray.getColor(attr, textHeaderColor);
                    }else if(attr == R.styleable.MenuItemView_textSize){
                        textSize = typedArray.getDimensionPixelSize(attr, textSize);
                    }else if(attr == R.styleable.MenuItemView_dividerr){
                        dividerr = typedArray.getDrawable(attr);
                    }else if(attr == R.styleable.MenuItemView_dividerWidth){
                        dividerWidth = typedArray.getDimensionPixelOffset(attr,dividerWidth);
                    }else if(attr == R.styleable.MenuItemView_dividerVisibilty){
                        dividerVisibilty = typedArray.getBoolean(attr,dividerVisibilty);
                    }else if(attr == R.styleable.MenuItemView_arrowDrawable){
                        arrowDrawable = typedArray.getDrawable(attr);
                    }else if(attr == R.styleable.MenuItemView_arrowSize){
                        arrowSize = typedArray.getDimensionPixelSize(attr, arrowSize);
                    }else if(attr == R.styleable.MenuItemView_arrowColor){
                        arrowColor = typedArray.getColor(attr, arrowColor);
                    }else if(attr == R.styleable.MenuItemView_onColor){
                        onColor = typedArray.getColor(attr, onColor);
                    }else if(attr == R.styleable.MenuItemView_offColor){
                        borderColor = offColor = typedArray.getColor(attr,offColor);
                    }else if(attr == R.styleable.MenuItemView_areaColor){
                        areaColor = typedArray.getColor(attr, areaColor);
                    }else if(attr == R.styleable.MenuItemView_handlerColor){
                        handlerColor = typedArray.getColor(attr, handlerColor);
                    }else if(attr == R.styleable.MenuItemView_bordeWidth){
                        borderWidth = typedArray.getColor(attr, borderWidth);
                    }else if(attr == R.styleable.MenuItemView_animate){
                        animate = typedArray.getBoolean(attr, animate);
                    }else if(attr == R.styleable.MenuItemView_defaultOn){
                        defaultOn = typedArray.getBoolean(attr, defaultOn);
                    }else if(attr == R.styleable.MenuItemView_toggleWidth){
                        toggleWidth = typedArray.getDimensionPixelOffset(attr, toggleWidth);
                    }else if(attr == R.styleable.MenuItemView_toggleHeight){
                        toggleHeight = typedArray.getDimensionPixelOffset(attr, toggleHeight);
                    }
                }
            }finally {
                typedArray.recycle();
            }
        }

        if(textSize == -1){
            textSize = applyDimension(TEXTSZIE_DEFAULT);
        }

        /**
         * 初始化文字画笔
         */
        textPaint = new Paint();
        textPaint.setColor(textHeaderColor);
        textPaint.setTextSize(textSize);
        textPaint.setAntiAlias(true);

        if(itemType == ItemType.TOGGLE){

            if(toggleWidth == -1){
                toggleWidth  = applyDimension(SWITCHBUTTON_WIDTH);
            }

            if(toggleHeight == -1){
                toggleHeight = applyDimension(SWITCHBUTTON_HEIGHT);
            }

            togglePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
            togglePaint.setStyle(Paint.Style.FILL);
            togglePaint.setStrokeCap(Paint.Cap.ROUND);
            togglePaint.setAntiAlias(true);

            if(defaultOn){
                currentDelay = 1;
                toggleOn();
            }else{
                currentDelay = 0;
                toggleOff();
            }
        }else if(itemType == ItemType.ARROW){
            if(arrowSize == -1){
                arrowSize = applyDimension(ARROW_SIZE);
            }
            arrowPaint = new Paint();
            arrowPaint.setColor(arrowColor);
            arrowPaint.setAntiAlias(true);
            arrowPaint.setStrokeWidth(3.0f);
        }

        /**
         * 设置默认背景色
         */
        if(getBackground() == null){
            setBackgroundColor(Color.parseColor("#FFFFFF"));
        }
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {

        if(headerDrawable != null){
            if(headDrawableSize >= 0 ){
                headerDrawableWidth = headerDrawableHeight = headDrawableSize;
            }else{
                headerDrawableWidth = headerDrawable.getIntrinsicWidth();
                headerDrawableHeight = headerDrawable.getIntrinsicHeight();
            }
            Log.i(TAG,"[onLayout] headerDrawableWidth = "+headerDrawableWidth+",headerDrawableHeight = "+headerDrawableHeight);

            headerDrawableStartDrawY = (getHeight() - headerDrawableHeight)/ 2.0f;
            Log.i(TAG, "[onLayout] headerDrawableStartDrawY = " + headerDrawableStartDrawY);
        }

        if(itemType == ItemType.ARROW){
            if(arrowDrawable != null){
                arrowDrawableWidth = arrowDrawable.getIntrinsicWidth();
                arrowDrawableHeight = arrowDrawable.getIntrinsicHeight();
            }
            if(arrowSize >= 0){
                arrowDrawableWidth = arrowDrawableHeight = arrowSize;
            }
            Log.i(TAG, "[onLayout] arrowDrawableWidth = " + arrowDrawableWidth + ",arrowDrawableHeight = " + arrowDrawableHeight);

            arrowStartDrawX = getWidth() - getPaddingRight() - arrowDrawableWidth;
            arrowStartDrawY = (getHeight() - arrowDrawableHeight) / 2.0f;
            Log.i(TAG, "[onLayout] arrowStartDrawX = " + arrowStartDrawX + ",arrowStartDrawY = " + arrowStartDrawY);
        }else if(itemType == ItemType.TOGGLE){
            radius = Math.min(toggleWidth,toggleHeight) * 0.5f;
            centerY = radius;
            startX = centerY;
            endX = toggleWidth - radius;
            handlerMinX = startX + borderWidth;
            handlerMaxX = endX - borderWidth;
            handlerSize = toggleHeight - 4*borderWidth;
            handlerX = toggleOn?handlerMaxX:handlerMinX;
            areaWidth = 0;

            switchButtonDrawStartX = getWidth() - getPaddingRight() - toggleWidth;
            switchButtonDrawStartY = (getHeight() - toggleHeight) / 2.0f;
            Log.i(TAG, "[onLayout] switchButtonDrawStartX = " + switchButtonDrawStartX + ",switchButtonDrawStartY = " + switchButtonDrawStartY);
        }

        super.onLayout(changed, left, top, right, bottom);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

        /**计算宽**/
        final int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int width = MeasureSpec.getSize(widthMeasureSpec);
        if(widthMode == MeasureSpec.UNSPECIFIED || widthMode == MeasureSpec.AT_MOST){
            width = applyDimension(-1);
            widthMeasureSpec = MeasureSpec.makeMeasureSpec(width, MeasureSpec.EXACTLY);
        }

        /**计算高**/
        final int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int height = MeasureSpec.getSize(heightMeasureSpec);
        if (heightMode == MeasureSpec.UNSPECIFIED || heightMode == MeasureSpec.AT_MOST) {
            height = applyDimension(HEIGHT_DEFAULT);
            heightMeasureSpec = MeasureSpec.makeMeasureSpec(height, MeasureSpec.EXACTLY);
        }

        Log.i(TAG, "[onMeasure] width = " + width + ",height = " + height);

        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    protected void onDraw(Canvas canvas) {

        Log.i(TAG, "[onDraw] start draw!");

        if(headerDrawable != null){
            drawHeaderDrawable(canvas);
        }

        drawHeaderText(canvas);

        if (itemType == ItemType.ARROW) {
            drawFooterDrawable(canvas);
        }else if(itemType == ItemType.TOGGLE){
            drawSwitchButton(canvas);
        }

        if(dividerVisibilty){
            drawDivider(canvas);
        }

        super.onDraw(canvas);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()){
            case MotionEvent.ACTION_DOWN:
                isPressed = true;
                if (itemType != ItemType.TOGGLE){
                    postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            MenuItemView.this.setPressed(isPressed);
                        }
                    }, ViewConfiguration.getTapTimeout());
                }else{
                    downX = event.getX();
                }
                break;
            case MotionEvent.ACTION_MOVE:
                isPressed = false;
                this.setPressed(false);
                break;
            case MotionEvent.ACTION_UP:
                if(itemType != ItemType.TOGGLE){
                    performClick();
                    postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            MenuItemView.this.setPressed(false);
                        }
                    }, ViewConfiguration.getTapTimeout());
                }else if(downX >= switchButtonDrawStartX){//为了扩大switchButton的响应区域
                    toggle();
                }
                break;
            default:
                isPressed = false;
                this.setPressed(false);
                break;
        }

        return true;
    }

    /**
     * 绘制头标
     * @param canvas
     */
    private void drawHeaderDrawable(Canvas canvas){
        if(headerDrawable == null){
            throw new NullPointerException("headerDrawable was setted null !");
        }
        canvas.save();
        canvas.translate(getPaddingLeft(), headerDrawableStartDrawY);
        headerDrawable.setBounds(0, 0, headerDrawableWidth, headerDrawableHeight);
        headerDrawable.draw(canvas);
        canvas.restore();
    }

    /**
     * 绘制头部文字提示
     * @param canvas
     */
    private void drawHeaderText(Canvas canvas){
        canvas.save();
        Paint.FontMetrics fontMetrics = textPaint.getFontMetrics();
        RectF targetRect = new RectF(getPaddingLeft() + headerDrawableWidth + drawablePadding,0,getWidth(),getHeight());
        int baseLine =  (int)((targetRect.bottom + targetRect.top - fontMetrics.bottom - fontMetrics.top) / 2);
        canvas.translate(getPaddingLeft() + headerDrawableWidth + drawablePadding, baseLine);
        canvas.drawText(textHeader, 0, 0, textPaint);
        canvas.restore();
    }

    /**
     * 绘制尾部指示图标
     *
     * @param canvas
     */
    private void drawFooterDrawable(Canvas canvas){
        canvas.save();
        canvas.translate(arrowStartDrawX, arrowStartDrawY);
        if(arrowDrawable != null){
            arrowDrawable.setBounds(0, 0, arrowDrawableWidth, arrowDrawableHeight);
            arrowDrawable.draw(canvas);
        }else{
            /**
             * 自行绘制arrow图标，绘制规则是：X轴坐标在arrow中间位置开始绘制，Y轴绘制不变
             */
            final float centerY = arrowDrawableHeight / 2.0f;
            final float lineStartX = arrowDrawableWidth / 2.0f;
            final float lineEndX = arrowDrawableWidth;
            canvas.drawLine(lineStartX,0,lineEndX,centerY,arrowPaint);
            canvas.drawLine(lineStartX,arrowDrawableHeight,lineEndX,centerY,arrowPaint);
        }
        canvas.restore();
    }

    /**
     * 绘制switchButton
     * @param canvas
     */
    private void drawSwitchButton(Canvas canvas){
        canvas.save();
        canvas.translate(switchButtonDrawStartX,switchButtonDrawStartY);
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
     * 绘制分割线
     * @param canvas
     */
    private void drawDivider(Canvas canvas){
        canvas.save();
        canvas.translate(getPaddingLeft(), getHeight() - dividerWidth);
        if(dividerr == null){
            ShapeDrawable divider = new ShapeDrawable(new RectShape());
            divider.getPaint().setStrokeWidth(dividerWidth);
            divider.getPaint().setAntiAlias(true);
            divider.getPaint().setColor(Color.parseColor("#CCCCCC"));
            divider.setBounds(0, 0, getWidth(), dividerWidth);
            divider.draw(canvas);

        }else{
            dividerr.setBounds(0, 0, getWidth(),dividerWidth);
            dividerr.draw(canvas);
        }
        canvas.restore();
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
        if(mListener != null){
            mListener.onToggle(toggleOn);
        }
        if(animate){
            postDelayed(toggleRunnable, DELAYDURATION);
        }else {
            caculateEffect(toggleOn ? 1 : 0);
        }
    }

    /**
     * 时时计算
     * @param value
     */
    private void caculateEffect(double value){

        handlerX = (float)mapValueFromRangeToRange(value,0,1.0,handlerMinX,handlerMaxX);

        areaWidth = (float)mapValueFromRangeToRange(1.0-value,0,1.0,10,handlerSize);

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

        postInvalidate();
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
                    caculateEffect(currentDelay);
                    postDelayed(toggleRunnable,DELAYDURATION);
                    currentDelay = currentDelay + 0.1;
                }else{
                    currentDelay = 1;
                }
            }else{
                if(currentDelay >= 0){
                    caculateEffect(currentDelay);
                    postDelayed(toggleRunnable, DELAYDURATION);
                    currentDelay = currentDelay - 0.1;
                }else{
                    currentDelay = 0;
                }
            }
        }
    };

    /**
     * px2dp
     * @param value
     */
    private int applyDimension(float value){
        return (int)TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,value,dm);
    }

    /**
     * 类型
     */
    private enum ItemType{

        NORMAL, ARROW, TOGGLE;

        public static ItemType getValue(int index){
            for(ItemType type:values()){
                if(type.ordinal() == index){
                    return type;
                }
            }
            return NORMAL;
        }
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
