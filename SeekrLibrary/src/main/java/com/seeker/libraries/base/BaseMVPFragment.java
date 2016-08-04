package com.seeker.libraries.base;

import android.os.Bundle;

/**
 * Created by Seeker on 2016/7/28.
 */

public abstract class BaseMVPFragment<V extends BaseEmptyView,T extends BasePresenter<V>> extends BaseFragment {

    public T presenter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        presenter = initPresenter();
        presenter.bindView((V)this);
    }

    @Override
    public void onDestroy() {
        presenter.unbindView();
        super.onDestroy();
    }

    //实例化presenter
    public abstract T initPresenter();

}
