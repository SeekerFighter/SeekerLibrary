package com.seeker.libraries.widget.recycleView;

import android.graphics.Bitmap;
import android.support.v4.util.SparseArrayCompat;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

public class BaseViewHolder extends RecyclerView.ViewHolder{

	private View mConvertView;
	private SparseArrayCompat<View> mViews;
	
	public BaseViewHolder(View view) {
		super(view);
		this.mConvertView = view;
		this.mViews = new SparseArrayCompat<>();
	}
	
	/**
	 * 通过控件的Id获取对于的控件，如果没有则加入views
	 * @param viewId
	 * @return
	 */
	public <V extends View> V getView(int viewId) {
		View view = mViews.get(viewId);
		if (view == null) {
			view = mConvertView.findViewById(viewId);
			mViews.put(viewId, view);
		}
		return (V) view;
	}

	/**
	 * 为TextView设置字符串
	 * @param viewId
	 * @param text
	 * @return
	 */
	public void setText(int viewId, String text) {
		TextView view = getView(viewId);
		if(view != null && text != null){
			view.setText(text);
		}
	}

	/**
	 * 设置图片
	 * @param viewId
	 * @param bitmap
     */
	public void setImageBitmap(int viewId, Bitmap bitmap){
		ImageView imageView = getView(viewId);
		if(imageView != null){
			imageView.setImageBitmap(bitmap);
		}
	}


	/**
	 * 返回当前view
	 * @return
	 */
	public View getContentView(){
		return mConvertView;
	}

}
