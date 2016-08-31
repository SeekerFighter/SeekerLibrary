package com.seeker.demo.OKHttpManger;

import android.content.Context;
import android.view.View;
import com.seeker.demo.OKHttpManger.bean.Info;
import com.seeker.demo.R;
import com.seeker.libraries.widget.recycleView.BaseRecyleAdapter;
import com.seeker.libraries.widget.recycleView.BaseViewHolder;
import java.util.List;

/**
 * Created by Seeker on 2016/7/29.
 */

public class OKHttpAdapter extends BaseRecyleAdapter<Info,BaseViewHolder> {

    public OKHttpAdapter(Context context, List<Info> infos){
        super(context,infos);
    }

    @Override
    public void convertBindViewHolder(BaseViewHolder viewHolder, int position) {
        viewHolder.setText(R.id.item_text, "[点击下载大图]"+mDatas.get(position).getDescription());
    }

    @Override
    public int getItemLayoutId(int viewType) {
        return R.layout.item;
    }

    @Override
    public BaseViewHolder convertCreateViewHolder(View itemView) {
        return new BaseViewHolder(itemView);
    }
}
