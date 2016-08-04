package com.seeker.libraries.weight.recycleView;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.AttributeSet;
import com.seeker.libraries.R;

public class FinalRecycleView extends BaseRecycleView{
	
	private Context mContext;
	private int spanCount;//
	private ShowStyle showStyle;

	private Drawable divider;
	private int dividerWidth;

	private enum ShowStyle{
		
		LISTVIEW_VERTICAL,
		LISTVIEW_HORIZONTAL,
		GRIDVIEW_NORMAL,
		STAGGERED_HORIZONTAL,
		STAGGERED_VERTICAL;
		
		public static ShowStyle getValue(int index){
			for(ShowStyle value:values()){
				if(value.ordinal() == index){
					return value;
				}
			}
			return LISTVIEW_VERTICAL;
		}
	}
	
	public FinalRecycleView(Context context) {
		this(context, null);
	}
	
	public FinalRecycleView(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}
	
	public FinalRecycleView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		this.mContext = context;
		init(attrs);
	}
	
	private void init(AttributeSet attrs){
		TypedArray ta = mContext.obtainStyledAttributes(attrs, R.styleable.FinalRecycleView);
		showStyle = ShowStyle.getValue(ta.getInt(R.styleable.FinalRecycleView_showStyle,
				ShowStyle.LISTVIEW_VERTICAL.ordinal()));
		spanCount = ta.getInteger(R.styleable.FinalRecycleView_gridSpanCount, 1);
		divider = ta.getDrawable(R.styleable.FinalRecycleView_innerDivider);
		dividerWidth = ta.getDimensionPixelSize(R.styleable.FinalRecycleView_innerDividerWidth,10);
		ta.recycle();
	}
	
	@Override
	public LayoutManager getLayoutManger() {
		
		switch(showStyle){
			case LISTVIEW_VERTICAL:
				return new LinearLayoutManager(mContext);
			case LISTVIEW_HORIZONTAL:
				return new LinearLayoutManager(mContext, LinearLayoutManager.HORIZONTAL, false);
			case GRIDVIEW_NORMAL:
				return new GridLayoutManager(mContext, spanCount);
			case STAGGERED_HORIZONTAL:
				return new StaggeredGridLayoutManager(spanCount, StaggeredGridLayoutManager.HORIZONTAL);
			case STAGGERED_VERTICAL:
				return new StaggeredGridLayoutManager(spanCount, StaggeredGridLayoutManager.VERTICAL);
		}
		return new LinearLayoutManager(mContext);
	}

	@Override
	public ItemDecoration getItemDecoration() {
		return new FlexDividerDecoration.Builder(getContext())
				.setDrawable(divider)
				.setSize(dividerWidth)
				.build();
	}
}
