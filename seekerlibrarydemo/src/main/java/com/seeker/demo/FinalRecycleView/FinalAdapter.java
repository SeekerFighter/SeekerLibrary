package com.seeker.demo.FinalRecycleView;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import com.seeker.demo.R;
import com.seeker.libraries.weight.recycleView.BaseRecyleAdapter;
import com.seeker.libraries.weight.recycleView.BaseViewHolder;

import java.util.List;

public class FinalAdapter extends BaseRecyleAdapter<String,BaseViewHolder> {

	public FinalAdapter(Context context, List<String> datas) {
		super(context, datas);
	}

	@Override
	public int getItemLayoutId(int viewType) {
		return R.layout.item;
	}

	@Override
	public View getOverflowLayout() {
		return LayoutInflater.from(mContext).inflate(R.layout.finalrecycleview_overflow_layout,null);
	}

	@Override
	public BaseViewHolder convertCreateViewHolder(View itemView) {
		return new BaseViewHolder(itemView);
	}

	@Override
	public void convertBindViewHolder(BaseViewHolder viewHolder, int position) {
		viewHolder.setText(R.id.item_text, mDatas.get(position));
	}
}
