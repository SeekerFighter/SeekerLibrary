package com.seeker.demo.OKHttpManger;

import android.view.View;
import android.widget.Toast;

import com.seeker.demo.OKHttpManger.bean.Info;
import com.seeker.demo.OKHttpManger.bean.Result;
import com.seeker.demo.R;
import com.seeker.libraries.base.BaseMVPActivity;
import com.seeker.demo.OKHttpManger.OKHttpFactory.OKHttpView;
import com.seeker.demo.OKHttpManger.OKHttpFactory.OKHttpPresenter;
import com.seeker.libraries.weight.recycleView.FinalRecycleView;
import com.seeker.libraries.weight.recycleView.CheckItemTouchListener;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * Created by Seeker on 2016/7/28.
 */

public class OKHttpMangerActivity extends BaseMVPActivity
        <OKHttpView<Result>,OKHttpPresenter> implements OKHttpView<Result>,CheckItemTouchListener.OnItemClickListener{

    private Unbinder unbinder;

    private List<Info> infos = new ArrayList<>();

    @BindView(R.id.finalRecycleView)
    FinalRecycleView finalRecycleView;

    @BindView(R.id.emptyView)
    View emptyView;

    private OKHttpAdapter myAdapter;

    @Override
    public void onbind() {
        super.onbind();
        unbinder = ButterKnife.bind(this);
    }

    @Override
    public void unbind() {
        super.unbind();
        unbinder.unbind();
    }

    @Override
    public void onPreLoad() {
        Toast.makeText(this,"加载数据",Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onFinishLoad() {
        Toast.makeText(this,"加载完成",Toast.LENGTH_SHORT).show();
    }

    @Override
    public void loadError() {
        Toast.makeText(this,"加载有误",Toast.LENGTH_SHORT).show();
    }

    @Override
    public void setDatas(Result datas) {
        infos.addAll(datas.getData());
        myAdapter.notifyDataSetChanged();
    }

    @Override
    public OKHttpFactory.OKHttpPresenter initPresenter() {
        return new OKHttpFactory.OKHttpPresenter();
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_recycleview;
    }

    @Override
    public void doWork() {
        myAdapter = new OKHttpAdapter(this,infos);
        finalRecycleView.setAdapter(myAdapter);
        finalRecycleView.setEmptyView(emptyView);
        finalRecycleView.setOnItemClickListener(this);
    }

    @Override
    public void onItemClick(View itemView, int position) {
        new ProgressBarDialog(this,infos.get(position).getPicBig()).show();
    }

    @OnClick(R.id.fab)
    public void onFabClick(View view){
        presenter.load();
    }






}
