package com.seeker.libraries.base;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by Seeker on 2016/7/21.
 *
 * 内置懒加载
 */
public abstract class BaseFragment extends Fragment {


    private static final String DATA_SAVE = "data_save";

    /**
     * 界面是否创建
     */
    private boolean isViewCreated = false;

    /**
     * 数据加载是否是第一次，即判断是否已经加载过
     */
    private boolean isFirstLoad = true;

    private Bundle origin = new Bundle();

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if(getUserVisibleHint()){
            /**
             * 当view已经创建，只是显示界面的时候调用此方法
             */
            resetState();
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
        onbind(rootView);
        return rootView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if(getUserVisibleHint()){
            resetState();
            onUIVisible();
        }
    }

    @Override
    public void onDestroyView() {
        isViewCreated = false;
        unbind();
        saveState();
        super.onDestroyView();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
       saveState();
    }

    /**
     * 绑定初始化一些组件：例如注解butterknife等
     */
    public abstract void onbind(View rootView);

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
    private final void onUIVisible(){
        if(isViewCreated && isFirstLoad){
            isFirstLoad = false;
            onLoad();
        }
    }

    private final void saveState(){
        /**
         * 当点击相邻的tab时，此时，view已经创建，但是并未显示，这个时候需要判断是否已经保存过当前fragment的状态
         */
        if(getArguments().getBundle(DATA_SAVE) == null){
            Bundle save = (Bundle) origin.clone();
            onSaveCurrentState(save);
            getArguments().putBundle(DATA_SAVE,save);
        }
    }

    private final void resetState(){
        Bundle bundle = getArguments().getBundle(DATA_SAVE);
        if(bundle != null && isViewCreated){
            onResetBeforeState(bundle);
            getArguments().remove(DATA_SAVE);
        }
    }

    /**
     * 当界面不可见时
     */
    public void onUIInVisivle(){
        // TODO: 2016/8/31
    }
    

    /**
     * 界面显示的时候开始加载数据
     */
    public abstract void onLoad();

    /**
     * 保存当前界面的状态
     */
    public void onSaveCurrentState(Bundle bundle){
        // TODO: 2016/8/31  
    }

    /**
     * 设置保存的状态到界面
     */
    public void onResetBeforeState(Bundle bundle){
        // TODO: 2016/8/31
    }
}
