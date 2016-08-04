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

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(getLayoutId(),null);
        onbind();
        return rootView;
    }

    @Override
    public void onDestroyView() {
        unbind();
        super.onDestroyView();
    }


    /**
     * 绑定初始化一些组件：例如MVP模式中的presenter，注解butterknife等
     */
    public abstract void onbind();

    /**
     * 解绑
     */
    public abstract void unbind();

    public abstract int getLayoutId();

}
