package com.seeker.libraries.base;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by Seeker on 2016/7/21.
 */
public abstract class BaseFragment extends Fragment{

    /**
     * 界面是否创建
     */
    private boolean isViewCreated = false;

    /**
     * 数据加载是否是第一次，即判断是否已经加载过
     */
    private boolean isFirstLoad = true;

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if(getUserVisibleHint()){
            onUIVisible();
        }else {
            onUIInVisivle();
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(getLayoutId(),null);
        isViewCreated = true;
        onbind();
        return rootView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if(getUserVisibleHint()){
            onUIVisible();
        }
    }

    @Override
    public void onDestroyView() {
        unbind();
        super.onDestroyView();
    }

    /**
     * 绑定初始化一些组件：例如注解butterknife等
     */
    public abstract void onbind();

    /**
     * 解绑
     */
    public abstract void unbind();

    /**
     * 返回主布局id
     * @return
     */
    public abstract int getLayoutId();

    /**
     * 当界面可见时
     */
    public void onUIVisible(){
        if(isViewCreated && isFirstLoad){
            isFirstLoad = false;
            onLoad();
        }
    };

    /**
     * 当界面不可见时
     */
    public void onUIInVisivle(){}

    /**
     * 界面显示的时候开始加载数据
     */
    public abstract void onLoad();
}
