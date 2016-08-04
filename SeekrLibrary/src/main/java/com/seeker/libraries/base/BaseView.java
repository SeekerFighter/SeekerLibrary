package com.seeker.libraries.base;

/**
 * Created by Seeker on 2016/6/28.
 */
public interface BaseView extends BaseEmptyView{

    /**
     * 开始加载数据之前，初始化进度条,加载提示等
     */
    void onPreLoad();

    /**
     * 数据加载完成之后,隐藏进度条
     */
    void onFinishLoad();
}
