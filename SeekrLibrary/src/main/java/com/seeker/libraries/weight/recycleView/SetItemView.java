package com.seeker.libraries.weight.recycleView;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import com.seeker.libraries.R;
import com.seeker.libraries.weight.MenuItemView;

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

    //默认结尾处arrow的宽,高
    private static final int DEFAULT_ARROWSIZE = 14;

    /***toggleButton默认宽*/
    private static final int TOGGLEBUTTON_WIDTH = 50;

    /***toggleButton默认高*/
    private static final int TOGGLEBUTTON_HEIGHT = 28;

    private static  final long DELAYDURATION = 10;

    private DisplayMetrics dm;

    private int style = STYLE_ARROW;

    private Drawable drawableStart;

    private int drawableStartWidth = -2,drawableStartHeight = -2;

    private String textStart;

    private int textStartColor = DEFAULT_TEXTCOLOR;

    private int textStartSize = DEFAULT_TEXTSIZE;

    private int textStartLeftMargin = 0;

    private String textEnd;

    private int textEndColor = DEFAULT_TEXTCOLOR;

    private int textEndSize = DEFAULT_TEXTSIZE;

    private Drawable textEndBackground;

    private int textEndBackgroundColor = Color.RED;

    private int textEndBackgroundWidth = -2,textEndBackgroundHeight = -2;

    private int textEndRightMargin = 0;

    private Drawable arrowDrawable;

    private int arrowWidth = DEFAULT_ARROWSIZE;

    private int arrowHeight = DEFAULT_ARROWSIZE;

    private int arrowColor = DEFAULT_TEXTCOLOR;

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

    /**整个toggleButton的区域**/
    private RectF toggleRectF = new RectF();

    /**绘制toggleButton的画笔**/
    private Paint togglePaint;

    private MenuItemView.OnToggleChangedListener mListener;

    private double currentDelay;

    public SetItemView(Context context) {
        this(context,null);
    }

    public SetItemView(Context context, AttributeSet attrs) {
        this(context, attrs,0);
    }

    public SetItemView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        dm = Resources.getSystem().getDisplayMetrics();
        setup(attrs);
    }

    //初始化view的属性
    private void setup(AttributeSet attrs){
        TypedArray typedArray = getContext().obtainStyledAttributes(attrs, R.styleable.SetItemView);
        if(typedArray != null){
            try {

                int count = typedArray.getIndexCount();
                for(int i = 0;i<count;i++){
                    int attr = typedArray.getIndex(i);
                    if(attr == R.styleable.SetItemView_itemStyle){
                        style = typedArray.getInt(attr,STYLE_ARROW);
                    }else if(attr == R.styleable.SetItemView_drawableStart){
                        drawableStart = typedArray.getDrawable(attr);
                    }else if(attr == R.styleable.SetItemView_drawableStartWidth){
                        drawableStartWidth = typedArray.getDimensionPixelSize(attr,drawableStartWidth);
                    }else if(attr == R.styleable.SetItemView_drawableStartHeight){
                        drawableStartHeight = typedArray.getDimensionPixelSize(attr,drawableStartHeight);
                    }else if(attr == R.styleable.SetItemView_textStart){
                        textStart = typedArray.getString(attr);
                    }else if(attr == R.styleable.SetItemView_textStartColor){
                        textStartColor = typedArray.getColor(attr,textStartColor);
                    }else if(attr == R.styleable.SetItemView_textStartSize){
                        textStartSize = typedArray.getDimensionPixelSize(attr,textStartSize);
                    }else if(attr == R.styleable.SetItemView_textStartLeftMargin){
                        textStartLeftMargin = typedArray.getDimensionPixelOffset(attr,textStartLeftMargin);
                    }else if(attr == R.styleable.SetItemView_textEnd){
                        textEnd = typedArray.getString(attr);
                    }else if(attr == R.styleable.SetItemView_textEndColor){
                        textEndColor = typedArray.getColor(attr,textEndColor);
                    }else if(attr == R.styleable.SetItemView_textEndSize){
                        textEndSize = typedArray.getDimensionPixelSize(attr,textEndSize);
                    }else if(attr == R.styleable.SetItemView_textEndBackground){
                        textEndBackground = typedArray.getDrawable(attr);
                    }else if(attr == R.styleable.SetItemView_textEndBackgroundColor){
                        textEndBackgroundColor = typedArray.getColor(attr,textEndBackgroundColor);
                    }else if(attr == R.styleable.SetItemView_textEndBackgroundWidth){
                        textEndBackgroundWidth = typedArray.getDimensionPixelSize(attr,textEndBackgroundWidth);
                    }else if(attr == R.styleable.SetItemView_textEndBackgroundHeight){
                        textEndBackgroundHeight = typedArray.getDimensionPixelSize(attr,textEndBackgroundHeight);
                    }else if(attr == R.styleable.SetItemView_textEndRightMargin){
                        textEndRightMargin = typedArray.getDimensionPixelSize(attr,textEndRightMargin);
                    }else if(attr == R.styleable.SetItemView_arrowDrawable){
                        arrowDrawable = typedArray.getDrawable(attr);
                    }else if(attr == R.styleable.SetItemView_arrowWidth){
                        arrowWidth = typedArray.getDimensionPixelSize(attr,arrowWidth);
                    }else if(attr == R.styleable.SetItemView_arrowHeight){
                        arrowHeight = typedArray.getDimensionPixelSize(attr,arrowHeight);
                    }else if(attr == R.styleable.SetItemView_arrowColor){
                        arrowColor = typedArray.getColor(attr,arrowColor);
                    }else if(attr == R.styleable.SetItemView_toggleWidth){
                        toggleWidth = typedArray.getDimensionPixelSize(attr,toggleWidth);
                    }else if(attr == R.styleable.SetItemView_toggleHeight){
                        toggleHeight = typedArray.getDimensionPixelSize(attr,toggleHeight);
                    }else if(attr == R.styleable.SetItemView_borderWidth){
                        borderWidth = typedArray.getDimensionPixelSize(attr,borderWidth);
                    }else if(attr == R.styleable.SetItemView_areaColor){
                        areaColor = typedArray.getColor(attr,areaColor);
                    }else if(attr == R.styleable.SetItemView_offColor){
                        offColor = typedArray.getColor(attr,offColor);
                    }else if(attr == R.styleable.SetItemView_onColor){
                        onColor = typedArray.getColor(attr,onColor);
                    }else if(attr == R.styleable.SetItemView_handlerColor){
                        handlerColor = typedArray.getColor(attr,handlerColor);
                    }else if(attr == R.styleable.SetItemView_animate){
                        animate = typedArray.getBoolean(attr,animate);
                    }else if(attr == R.styleable.SetItemView_defaultOn){
                        defaultOn = typedArray.getBoolean(attr,defaultOn);
                    }
                }
            }catch (Exception e){
                Log.e(TAG, "setup: e", e);
            }finally {
                typedArray.recycle();
            }
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
    }

    /**
     * 设置开关监听
     */
    public void setOnToggleChangedlistener(MenuItemView.OnToggleChangedListener listener){
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
