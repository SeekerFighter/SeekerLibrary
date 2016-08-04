package com.seeker.libraries.weight.recycleView;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.GestureDetectorCompat;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;

public abstract class BaseRecycleView extends RecyclerView{

	private Context mContext;
	/**
	 * 点击事件监听
	 */
	private OnItemClickListener mClickListener;
	/**
	 * 长按事件的监听
	 */
	private OnLongItemClickListener mLongClickListener;

	/**
	 * 数据为空时显示的布局
	 */
	private View emptyView;

	/**
	 * 数据监听
	 */
	private DataEmptyObserview dataEmptyObserview;

	public BaseRecycleView(Context context) {
		this(context, null);
	}
	
	public BaseRecycleView(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}
	
	public BaseRecycleView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		this.mContext = context;
		this.dataEmptyObserview = new DataEmptyObserview();
		this.addOnItemTouchListener(new OnRecycleViewItemClickListener());
	}
	
	@Override
	public boolean onInterceptTouchEvent(MotionEvent event) {
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
	public void setOnItemClickListener(OnItemClickListener listener){
		this.mClickListener = listener;
	}
	
	/**
	 *设置行长按事件 
	 */
	public void setOnItemLongClickListener(OnLongItemClickListener listener){
		this.mLongClickListener = listener;
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

	/**
	 * 监听RecycleView的触摸事件
	 */
	private final class OnRecycleViewItemClickListener implements RecyclerView.OnItemTouchListener{

		private GestureDetectorCompat mGestureDetectorCompat;

		public OnRecycleViewItemClickListener(){
			this.mGestureDetectorCompat = new GestureDetectorCompat(mContext,
					new ItemTouchHelperGestureListener());
		}

		@Override
		public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e) {
			mGestureDetectorCompat.onTouchEvent(e);
			return false;
		}

		@Override
		public void onTouchEvent(RecyclerView rv, MotionEvent e) {
			mGestureDetectorCompat.onTouchEvent(e);
		}

		@Override
		public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {
			// todo nothing
		}
	}

	/**
	 * 处理触摸事件,响应处理点击，滑动，长按事件
	 */
	private final class ItemTouchHelperGestureListener implements GestureDetector.OnGestureListener{

		private View onDownView;

		private boolean press = false;

		private static final int STATE_PRESSED = 0x001;

		private static final int STATE_NO_PRESSED = 0x002;

		private final Handler stateHandler = new Handler(){
			@Override
			public void handleMessage(Message msg) {
				switch (msg.what){
					case STATE_PRESSED:
						if(onDownView != null){
							onDownView.setPressed(press);
						}
						break;
					case STATE_NO_PRESSED:
						press = false;
						if(onDownView != null){
							onDownView.setPressed(false);
						}
						BaseRecycleView.this.setPressed(false);
						BaseRecycleView.this.refreshDrawableState();
						break;
				}
			}
		};

		@Override
		public boolean onDown(MotionEvent e) {
			press = true;
			onDownView = BaseRecycleView.this.findChildViewUnder(e.getX(), e.getY());
			if(onDownView != null){
				stateHandler.sendEmptyMessageDelayed(STATE_PRESSED, ViewConfiguration.getTapTimeout());
			}
			return true;
		}

		@Override
		public boolean onSingleTapUp(MotionEvent e) {
			stateHandler.sendEmptyMessageDelayed(STATE_NO_PRESSED, ViewConfiguration.getTapTimeout());
			if(mClickListener != null){
				if(onDownView != null){
					RecyclerView.ViewHolder vh = BaseRecycleView.this.getChildViewHolder(onDownView);
					mClickListener.onItemClick(onDownView,vh.getAdapterPosition());
				}
			}
			return true;
		}

		@Override
		public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
			stateHandler.sendEmptyMessage(STATE_NO_PRESSED);
			return true;
		}

		@Override
		public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
			stateHandler.sendEmptyMessage(STATE_NO_PRESSED);
			return true;
		}

		@Override
		public void onShowPress(MotionEvent e) {
			// TODO: 2016/7/19
		}

		@Override
		public void onLongPress(MotionEvent e) {
			stateHandler.sendEmptyMessageDelayed(STATE_NO_PRESSED, ViewConfiguration.getTapTimeout());
			if(mLongClickListener != null){
				if(onDownView != null){
					RecyclerView.ViewHolder vh = BaseRecycleView.this.getChildViewHolder(onDownView);
					mLongClickListener.onLongItemClick(onDownView,vh.getAdapterPosition());
				}
			}
		}
	}

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
