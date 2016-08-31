package com.seeker.demo.SetItemView;

import android.graphics.Color;
import android.widget.Toast;
import com.seeker.demo.R;
import com.seeker.libraries.base.BaseActivity;
import com.seeker.libraries.widget.SetItemView;

/**
 * Created by Seeker on 2016/8/29.
 */

public class SetItemViewActivity extends BaseActivity{

    private static final String TAG = "SetItemViewActivity";

    private SetItemView supportKa;
    private SetItemView updateVersion;

    @Override
    public int getLayoutId() {
        return R.layout.activity_setitemview;
    }

    @Override
    public void onbind() {

    }

    @Override
    public void unbind() {

    }

    @Override
    public void doWork() {
        updateVersion.setTextEnd("NEW");
        updateVersion.setTextEndBackColor(Color.RED);
    }

    @Override
    public void getViews() {
        supportKa = (SetItemView) findViewById(R.id.supportKa);
        updateVersion = (SetItemView) findViewById(R.id.updateVsersion);
    }

    @Override
    public void registerViewListener() {
        supportKa.setOnToggleChangedlistener(new SetItemView.OnToggleChangedListener() {
            @Override
            public void onToggle(boolean on) {
                Toast.makeText(SetItemViewActivity.this,on?"支持":"不支持",Toast.LENGTH_SHORT).show();
            }
        });
    }
}
