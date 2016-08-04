package com.seeker.libraries.base;

/**
 * Created by Seeker on 2016/6/28.
 */
public abstract class BasePresenter<T extends BaseEmptyView> {

    public T bindedView;

    /**
     * 绑定view
     */
    public void bindView(T view){
        this.bindedView = view;
    }

    /**
     * 销毁view
     */
    public void unbindView(){
        this.bindedView = null;
    }

    /**
     * 检查view是否为null
     */
    public boolean checkViewNull(){
        return bindedView == null;
    }
}
