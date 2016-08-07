package com.seeker.libraries.weight.recycleView;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import com.seeker.libraries.logger.Logger;

public abstract class BaseRecycleView extends RecyclerView{

	private static final String TAG = "BaseRecycleView";
	
	/**
	 * 数据为空时显示的布局
	 */
	private View emptyView;

	/**
	 * 数据监听
	 */
	private DataEmptyObserview dataEmptyObserview;

	private CheckItemTouchListener onItemTouchListener;

	public BaseRecycleView(Context context) {
		this(context, null);
	}
	
	public BaseRecycleView(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}
	
	public BaseRecycleView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		this.dataEmptyObserview = new DataEmptyObserview();
		this.onItemTouchListener = new CheckItemTouchListener(this);
		this.addOnItemTouchListener(onItemTouchListener);
	}
	
	@Override
	public boolean onInterceptTouchEvent(MotionEvent event) {
		Logger.t(TAG).d("[onInterceptTouchEvent]");
		/**
		 *拦截多点触控
		 */
		if(event.getActionMasked() == MotionEvent.ACTION_POINTER_UP ||
				event.getActionMasked() == MotionEvent.ACTION_POINTER_DOWN){
			return true;
		}
		return super.onInterceptTouchEvent(event);
	}

	@Override
	protected void onFinishInflate() {
		super.onFinishInflate();
		setLayoutManager(getLayoutManger());
		addItemDecoration(getItemDecoration());
	}
	
	@Override
	public void setAdapter(Adapter adapter) {
		Adapter oldAdapter = getAdapter();
		if(oldAdapter != null && emptyView != null){
			oldAdapter.unregisterAdapterDataObserver(dataEmptyObserview);
		}
		if(adapter == null){
			throw new NullPointerException("adapter is null!");
		}
		if(!(adapter instanceof BaseRecyleAdapter)){
			throw new ClassCastException("RecyclerView's adapter can not cast to BaseRecyleAdapter," +
					"so userd adapter should extends BaseRecyleAdapter");
		}
		super.setAdapter(adapter);
		adapter.registerAdapterDataObserver(dataEmptyObserview);
		dataEmptyObserview.onChanged();
	}

	/**
	 *设置行点击事件 
	 */
	public void setOnItemClickListener(CheckItemTouchListener.OnItemClickListener listener){
		onItemTouchListener.setOnItemClickListener(listener);
	}
	
	/**
	 *设置行长按事件 
	 */
	public void setOnItemLongClickListener(CheckItemTouchListener.OnLongItemClickListener listener){
		this.setLongClickable(true);
		onItemTouchListener.setOnItemLongClickListener(listener);
	}

	/**
	 * 监听overflow点击
	 * @param listener
     */
	public void setOverflowClickListener(CheckItemTouchListener.OverflowClickListener listener){
		onItemTouchListener.setOverflowClickListener(listener);
	}

	/**
	 * 主动关闭显示了overflow的item
	 * @return
     */
	public boolean closeOpenedItem(){
		return onItemTouchListener.closeOpenedItem();
	}

	/**
	 * 设置数据为空时要显示的布局
	 * @param emptyView
	 */
	public void setEmptyView(View emptyView){
		this.emptyView = emptyView;
		dataEmptyObserview.onChanged();
	}

	/**
	 *根据自己的需要设置不同的LayoutManger 
	 */
	public abstract LayoutManager getLayoutManger();

	/**
	 * 获取分割线
	 */
	public abstract RecyclerView.ItemDecoration getItemDecoration();

	/**
	 * 数据观察者
	 */
	private final class DataEmptyObserview extends AdapterDataObserver{
		@Override
		public void onChanged() {
			Adapter adapter = getAdapter();
			if(adapter != null && emptyView != null){
				final boolean isEmpty = adapter.getItemCount() == 0;
				emptyView.setVisibility(isEmpty?VISIBLE:GONE);
				BaseRecycleView.this.setVisibility(isEmpty?GONE:VISIBLE);
			}
		}
	}
}
