package com.seeker.libraries.base;

import android.content.ComponentName;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

/**
 * Created by Seeker on 2016/7/21.
 */
public abstract class BaseActivity extends AppCompatActivity{


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getLayoutId());
        onbind();
        doWork();
    }

    @Override
    protected void onDestroy() {
        unbind();
        super.onDestroy();
    }

    public void startActivity(@NonNull Class<?> clazz){
        this.startActivityForResult(clazz,0);
    }

    public void startActivityForResult(@NonNull Class<?> clazz, int requstCode){
        ComponentName cn = new ComponentName(this,clazz);
        Intent intent = new Intent();
        intent.setComponent(cn);
        startActivityForResult(intent,requstCode);
    }

    /**
     * 返回布局文件id
     * @return
     */
    public abstract int getLayoutId();

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

}
