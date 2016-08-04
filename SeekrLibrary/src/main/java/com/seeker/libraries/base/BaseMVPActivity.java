package com.seeker.libraries.base;

/**
 * Created by Seeker on 2016/7/28.
 */

public abstract class BaseMVPActivity<V extends BaseEmptyView,T extends BasePresenter<V>> extends BaseActivity{

    public T presenter;

    @Override
    public void onbind() {
        presenter = initPresenter();
        presenter.bindView((V)this);
    }

    @Override
    public void unbind() {
        presenter.unbindView();
    }

    //实例化presenter
    public abstract T initPresenter();

}
