package com.seeker.libraries.weight.progressView;

import android.animation.ArgbEvaluator;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.View;

/**
 * Created by dandy on 2016/5/31.
 */
public class LoadClassicView extends View {

    private static final String TAG = "LoadingView";

    private static final long DELAY_DURATION = 65;

    private static final float SIZE = 22f;//默认大小

    private static final float RAIDUS = 18f;//内部圆半径

    private static final int START_COLOR = Color.parseColor("#5a5a5a");//起始颜色

    private static final int END_COLOR = Color.parseColor("#dddddd");//结束颜色

    private static final int COUNT = 12;//默认加载条个数

    private static final float STROKE_WIDTH = 6.0f;//加载条粗值

    private float size = SIZE;

    private float radius = RAIDUS;

    private int startColor = START_COLOR;

    private int endColor = END_COLOR;

    private int count = COUNT;

    private float strokeWidth = STROKE_WIDTH;

    private DisplayMetrics dm;

    private ArgbEvaluator colorEvaluator;

    private int [] colors;//加载条颜色

    private LoadingLine [] loadingLines;//加载条集合

    private Paint paint;

    private double startAngle = 0;

    private  int exactlySize;

    private int startIndex = 0;

    public LoadClassicView(Context context){
        this(context, null);
    }

    public LoadClassicView(Context context, AttributeSet attributeSet){
        super(context, attributeSet);
        setUpInit(attributeSet);
    }

    private void setUpInit(AttributeSet set){
        dm = Resources.getSystem().getDisplayMetrics();
        paint = new Paint();
        paint.setAntiAlias(true);
        paint.setStrokeWidth(strokeWidth);
        paint.setStrokeCap(Paint.Cap.ROUND);
        initColor();
        initLoadingLines();
    }

    private void initColor(){
        colorEvaluator = new ArgbEvaluator();
        colors = new int[count];
        for(int i = 0;i < count;i++){
            colors[i] = (int)colorEvaluator.evaluate(i*1.0f/(count-1),startColor,endColor);
        }
    }

    private void initLoadingLines(){
        loadingLines = new LoadingLine[count];
        for(int i = 0;i <count;i++){
            LoadingLine loadingLine = new LoadingLine();
            loadingLine.drawColor = colors[i];
            loadingLines[i] = loadingLine;
        }
    }

    /**
     * 设置显示颜色
     * @param index,线段颜色初始化位置
     */
    private void setColor(int index){
        int lineIndex;
        for(int i = 0;i < count;i++){
            lineIndex = index + i;
            loadingLines[lineIndex >= count?lineIndex - count:lineIndex].drawColor = colors[i];
        }
    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        /**计算宽**/
        final int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int width = MeasureSpec.getSize(widthMeasureSpec);
        if(widthMode == MeasureSpec.UNSPECIFIED || widthMode == MeasureSpec.AT_MOST){
            width = applyDimension(size);
        }

        /**计算高**/
        final int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int height = MeasureSpec.getSize(heightMeasureSpec);
        if (heightMode == MeasureSpec.UNSPECIFIED || heightMode == MeasureSpec.AT_MOST) {
            height = applyDimension(size);
        }

        /**
         * 取小的值作为控件的大小
         */
        exactlySize = width >= height ? height:width;

        this.radius = 0.22f * exactlySize;

        setMeasuredDimension(exactlySize,exactlySize);

    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        float delayAngle = 360.0f / count;
        LoadingLine loadingLine;
        double value;
        for(int i = 0;i < count;i++){
            loadingLine = loadingLines[i];
            value = startAngle * Math.PI / 180;
            loadingLine.startX = (int) Math.round(radius * Math.cos(value));
            loadingLine.startY = (int) Math.round(radius * Math.sin(value));
            loadingLine.endX = (int) Math.round(exactlySize / 2.5f * Math.cos(value));
            loadingLine.endY = (int) Math.round(exactlySize / 2.5f * Math.sin(value));
            startAngle += delayAngle;
        }
        startAngle = 0;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        canvas.save();
        canvas.translate(exactlySize/2.0f,exactlySize/2.0f);
        for(int i = 0; i < count;i++){
            LoadingLine loadingLine = loadingLines[i];
            paint.setColor(loadingLine.drawColor);
            canvas.drawLine(loadingLine.startX, loadingLine.startY, loadingLine.endX, loadingLine.endY, paint);
        }
        canvas.restore();
    }

    public void startLoad(){
        postDelayed(runnable,100);
    }

    public void finishLoad(){
        removeCallbacks(runnable);
    }

    private Runnable runnable = new Runnable() {
        @Override
        public void run() {
            postInvalidate();
            removeCallbacks(runnable);
            setColor(startIndex % count);
            startIndex++;
            postDelayed(runnable,DELAY_DURATION);
        }
    };

    /**
     * px2dp
     * @param value
     */
    private int applyDimension(float value){
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, value, dm);
    }

    private static class LoadingLine{
        private int drawColor;
        private int startX;
        private int startY;
        private int endX;
        private int endY;
    }
}
