package com.seeker.libraries.weight.recycleView;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import java.util.List;

public abstract class BaseRecyleAdapter <T,VH extends BaseViewHolder> extends RecyclerView.Adapter<VH>{

	private static final int OVERFLOW_NO_SET = -1;

	public Context mContext;
	public List<T> mDatas;

	public BaseRecyleAdapter(Context context, List<T> datas) {
		this.mContext = context;
		this.mDatas = datas;
	}
	
	@Override
	public int getItemCount() {
		return mDatas == null?0:mDatas.size();
	}
	
	@Override
	public VH onCreateViewHolder(ViewGroup viewGroup, int viewType) {
		final LayoutInflater inflater = LayoutInflater.from(mContext);
		final View contentView = inflater.inflate(getItemLayoutId(viewType), null, false);
		final int overflowResId = getOverflowLayoutId(viewType);
		VH vh;
		if(overflowResId == OVERFLOW_NO_SET){
			vh = convertCreateViewHolder(contentView);
		}else{
			final View overflowView = inflater.inflate(overflowResId,null,false);
			ScrollOverflowLayout scrollOverflowLayout = new ScrollOverflowLayout(mContext);
			scrollOverflowLayout.setOverFlowLayout(overflowView)
								.setContentLayout(contentView)
								.add();
			scrollOverflowLayout.setLayoutParams(new FrameLayout.LayoutParams(-1,-2));
			vh = convertCreateViewHolder(scrollOverflowLayout);
		}
		return vh;
	}
	
	@Override
	public void onBindViewHolder(VH viewHolder, final int position) {
		convertBindViewHolder(viewHolder,position);
	}


	/**
	 * 给View赋值内容显示
	 * @param viewHolder
	 * @param position
	 */
	public abstract void convertBindViewHolder(VH viewHolder, final int position);

	/**
	 * 设置item布局
	 * @param viewType
	 * @return
	 */
	public abstract int getItemLayoutId(int viewType);

	/**
	 * when scroll left,more action can do.
	 * @return
     */
	public int getOverflowLayoutId(int viewType){
		return OVERFLOW_NO_SET;
	}

	/**
	 * 对外接口，viewHolder与itemView进行绑定
	 * @param itemView
	 * @return VH
	 */
	public abstract VH convertCreateViewHolder(View itemView);
}
