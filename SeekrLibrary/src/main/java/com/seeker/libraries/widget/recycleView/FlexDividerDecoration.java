package com.seeker.libraries.widget.recycleView;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.support.annotation.ColorRes;
import android.support.annotation.DimenRes;
import android.support.annotation.DrawableRes;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.LayoutManager;
import android.support.v7.widget.RecyclerView.State;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.View;

public class FlexDividerDecoration extends RecyclerView.ItemDecoration {

	private static final int[] ATTRS = new int[] { android.R.attr.listDivider };

	private static final int TYPE_NON = 0x000;
	private static final int TYPE_DRAWABLE = 0X001;
	private static final int TYPE_COLOR = 0x002;
	private static final int TYPE_PAINT = 0x003;

	private int type = TYPE_NON;
	private Drawable mDivider;
	private Paint mPaint;
	private int mColor;
	private int mSize;
	private final Rect bounds = new Rect();

	public FlexDividerDecoration(Builder builder){
		final Builder br = builder;
		if(br.mColor != -1){
			this.mColor = br.mColor;
			this.type = TYPE_COLOR;
		}
		if(br.mPaint != null){
			this.mPaint = br.mPaint;
			this.type = TYPE_PAINT;
		}
		if(br.mDrawable != null){
			this.mDivider = br.mDrawable;
			this.type = TYPE_DRAWABLE;
		}
		if(type == TYPE_NON){
			final TypedArray a = br.mContext.obtainStyledAttributes(ATTRS);
			this.mDivider = a.getDrawable(0);
			a.recycle();
			this.type = TYPE_DRAWABLE;
		}
		this.mSize = br.mSize;
	}

	@Override
	public void onDraw(Canvas c, RecyclerView parent, State state) {
		if(parent.getAdapter() == null){
			return;
		}
		drawDivider(c,parent);
	}

	private void drawDivider(Canvas c, RecyclerView parent){
		int childCount = parent.getChildCount();
		for (int i = 0; i < childCount; i++) {
			final View child = parent.getChildAt(i);
			final RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) child.getLayoutParams();
			drawRect(c,measureHorizontalBounds(params,child));
			drawRect(c,measureVerticalBounds(params, child));
		}
	}

	private int getSpanCount(RecyclerView parent) {
		// 列数
		int spanCount = 1;
		LayoutManager layoutManager = parent.getLayoutManager();
		if (layoutManager instanceof GridLayoutManager) {
			spanCount = ((GridLayoutManager) layoutManager).getSpanCount();
		} else if (layoutManager instanceof StaggeredGridLayoutManager) {
			spanCount = ((StaggeredGridLayoutManager) layoutManager)
					.getSpanCount();
		}
		return spanCount;
	}

	private void drawRect(Canvas c, Rect bounds){
		switch (type){
			case TYPE_DRAWABLE:
				mDivider.setBounds(bounds);
				mDivider.draw(c);
				break;
			case TYPE_PAINT:
				c.drawLine(bounds.left, bounds.top, bounds.right, bounds.bottom, mPaint);
				break;
			case TYPE_COLOR:
				if(mPaint == null){
					mPaint = new Paint();
					mPaint.setAntiAlias(true);
					mPaint.setColor(mColor);
					mPaint.setStrokeWidth(mSize);
				}
				c.drawLine(bounds.left, bounds.top, bounds.right, bounds.bottom, mPaint);
				break;
		}
	}

	private Rect measureHorizontalBounds(RecyclerView.LayoutParams params, View child) {
		bounds.left = child.getLeft() - params.leftMargin;
		bounds.right = child.getRight() + params.rightMargin+mSize;
		bounds.top = child.getBottom() + params.bottomMargin;
		bounds.bottom = bounds.top+mSize;
		return bounds;
	}

	private Rect measureVerticalBounds(RecyclerView.LayoutParams params, View child) {
		bounds.top = child.getTop() - params.topMargin;
		bounds.bottom = child.getBottom() + params.bottomMargin+mSize;
		bounds.left = child.getRight() + params.rightMargin;
		bounds.right = bounds.left+mSize;
		return bounds;
	}

	private Rect getOutRect(RecyclerView parent, int pos, int spanCount, int childCount){
		LayoutManager layoutManager = parent.getLayoutManager();
		if(layoutManager instanceof GridLayoutManager){
			final int lastRowStartIndex = childCount - (childCount % spanCount == 0?spanCount:childCount % spanCount);
			final boolean isLastRow = pos >= lastRowStartIndex;
			final boolean isLastColum = (pos + 1) % spanCount == 0;
			if (isLastRow && isLastColum){
				return new Rect(0,0,0,0);
			}else if(isLastRow && !isLastColum){
				return new Rect(0,0,mSize,0);
			}else if(!isLastRow && isLastColum){
				return new Rect(0,0,0,mSize);
			}else{
				return new Rect(0,0,mSize,mSize);
			}
		}else if(layoutManager instanceof LinearLayoutManager){
			int orientation = ((LinearLayoutManager)layoutManager).getOrientation();
			if(orientation == LinearLayoutManager.VERTICAL){//垂直风向滚动
				if((pos+1) == childCount)//最后一行不显示分割线
					return new Rect(0,0,0,0);
				else
					return new Rect(0,0,0,mSize);
			} else{//水平风向滚动
				if((pos+1) == childCount)//最后一列不显示分割线
					return new Rect(0,0,0,0);
				else
					return new Rect(0,0,mSize,0);
			}
		}else if (layoutManager instanceof StaggeredGridLayoutManager){
			return new Rect(0,0,0,0);
		}
		return null;
	}


	@Override
	public void getItemOffsets(Rect outRect, View view, RecyclerView parent, State state) {
		int itemPosition = ((RecyclerView.LayoutParams) view.getLayoutParams()).getViewLayoutPosition();
		int childCount = parent.getAdapter().getItemCount();
		final int spanCount = getSpanCount(parent);
		outRect.set(getOutRect(parent, itemPosition, spanCount, childCount));
	}

	public static final class Builder{

		private Context mContext;

		private Resources mResource;

		/**
		 * 分割线图片,level 1
		 */
		private Drawable mDrawable = null;

		/**
		 * 绘制分割线画笔,level 2
		 */
		private Paint mPaint = null;

		/**
		 * 分割线颜色,level 3
		 */
		private int mColor = -1;

		/**
		 * 分割线宽或者高
		 */
		private int mSize = 0;

		public Builder(Context context){
			this.mContext = context;
			this.mResource = mContext.getResources();
		}

		public Builder setDrawableResId(@DrawableRes int resId){
			return setDrawable(ContextCompat.getDrawable(mContext,resId));
		}

		public Builder setDrawable(final Drawable drawable){
			this.mDrawable = drawable;
			return this;
		}

		public Builder setPaint(final Paint paint){
			this.mPaint = paint;
			return this;
		}

		public Builder setColorResId(@ColorRes int resId){
			return setColor(ContextCompat.getColor(mContext,resId));
		}

		public Builder setColor(final int color){
			this.mColor = color;
			return this;
		}

		public Builder setSizeResId(@DimenRes int resId){
			return setSize(mResource.getDimensionPixelSize(resId));
		}

		public Builder setSize(final int size){
			this.mSize = size;
			return this;
		}

		public FlexDividerDecoration build(){
			return new FlexDividerDecoration(this);
		}

	}

}
