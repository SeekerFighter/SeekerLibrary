package com.seeker.libraries.base;

import android.content.ComponentName;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

/**
 * Created by Seeker on 2016/7/21.
 */
public abstract class BaseActivity extends AppCompatActivity{

    public static final int DEFAULT_INTENT_FLAGS = -1;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getLayoutId());
        onbind();
        doWork();
    }

    @Override
    public void onContentChanged() {
        super.onContentChanged();
        getViews();
        registerViewListener();
    }

    @Override
    protected void onDestroy() {
        unbind();
        onRelease();
        super.onDestroy();
    }

    public void startActivity(@NonNull Class<?> clazz){
        this.startActivityForResult(clazz,0,DEFAULT_INTENT_FLAGS);
    }

    public void startActivity(@NonNull Class<?> clazz,int flags){
        this.startActivityForResult(clazz,0,flags);
    }

    public void startActivityForResult(@NonNull Class<?> clazz, int requstCode,int flags){
        ComponentName cn = new ComponentName(this,clazz);
        Intent intent = new Intent();
        intent.setComponent(cn);
        if(flags != DEFAULT_INTENT_FLAGS){
            intent.addFlags(flags);
        }
        startActivityForResult(intent,requstCode);
    }

    /**
     * 返回布局文件id
     * @return
     */
    public abstract int getLayoutId();

    /**
     * 通过findViewById获取view的回调
     */
    public void getViews(){
        // TODO: 2016/8/29
    };

    /**
     * 给View设置各种监听事件
     */
    public void registerViewListener(){
        // TODO: 2016/8/29
    }
    
    /**
     * 绑定初始化一些组件：例如MVP模式中的presenter，注解butterknife等
     */
    public abstract void onbind();

    /**
     * 解绑
     */
    public abstract void unbind();

    /**
     * 初始化完毕之后具体功能操作实现
     */
    public abstract void doWork();

    /**
     * 释放一些资源，比如bitmap，list数组
     */
    public void onRelease(){
        // TODO: 2016/8/29
    }
}
