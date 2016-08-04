package com.seeker.demo.FinalRecycleView;

import android.view.View;
import android.widget.Toast;

import com.seeker.demo.R;
import com.seeker.libraries.base.BaseActivity;
import com.seeker.libraries.weight.recycleView.BaseRecycleView;
import com.seeker.libraries.weight.recycleView.FinalRecycleView;
import java.util.ArrayList;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * Created by Seeker on 2016/7/21.
 */
public class FinalRecycleViewActivity extends BaseActivity
        implements BaseRecycleView.OnItemClickListener,BaseRecycleView.OnLongItemClickListener{

    private Unbinder unbinder;

    @BindView(R.id.finalRecycleView)
    FinalRecycleView finalRecycleView;

    @BindView(R.id.emptyView)
    View emptyView;

    final ArrayList<String> list = new ArrayList<>();

    private FinalAdapter myAdapter;

    @Override
    public int getLayoutId() {
        return R.layout.activity_recycleview;
    }

    @Override
    public void onbind() {
        unbinder = ButterKnife.bind(this);
    }

    @Override
    public void unbind() {
        unbinder.unbind();
    }

    @Override
    public void doWork() {
        myAdapter = new FinalAdapter(this,list);
        finalRecycleView.setAdapter(myAdapter);
        finalRecycleView.setEmptyView(emptyView);
        finalRecycleView.setOnItemClickListener(this);
        finalRecycleView.setOnItemLongClickListener(this);
    }

    @OnClick(R.id.fab)
    public void onFabClick(View view){
        if(list.size() == 0){
            for (int i = 0; i < 52; i++) {
                list.add(""+i);
            }
        }else{
            list.clear();
        }
        myAdapter.notifyDataSetChanged();
    }

    @Override
    public void onItemClick(View itemView, int position) {
        Toast.makeText(this,"[click]position = "+position+"str = "+list.get(position),
                Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onLongItemClick(View itemView, int position) {
        Toast.makeText(this,"[longClick]position = "+position+"str = "+list.get(position),
                Toast.LENGTH_SHORT).show();
    }
}
