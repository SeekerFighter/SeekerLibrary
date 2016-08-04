package com.seeker.demo;

import android.view.View;

import com.seeker.demo.CropImage.CropImageActivity;
import com.seeker.demo.FinalRecycleView.FinalRecycleViewActivity;
import com.seeker.demo.OKHttpManger.OKHttpMangerActivity;
import com.seeker.demo.dialog.DialogActivity;
import com.seeker.libraries.base.BaseActivity;
import com.seeker.libraries.logger.Logger;

import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

public class MainActivity extends BaseActivity {

    private static final String TAG = "MainActivity";

    private Unbinder unbinder;

    @Override
    public int getLayoutId() {
        return R.layout.activity_main;
    }

    @OnClick({R.id.finalRecycleDemo,R.id.okhttpDemo,R.id.cropImageDemo,R.id.libDialogFragmentDemo})
    public void onFinalClick(View view){
        Logger.t(TAG).d("clickViewId = "+view.getId());
        switch (view.getId()){
            case R.id.finalRecycleDemo:
                startActivity(FinalRecycleViewActivity.class);
                break;
            case R.id.okhttpDemo:
                startActivity(OKHttpMangerActivity.class);
                break;
            case R.id.cropImageDemo:
                startActivity(CropImageActivity.class);
                break;
            case R.id.libDialogFragmentDemo:
                startActivity(DialogActivity.class);
                break;
        }
    }

    @Override
    public void onbind() {
       unbinder = ButterKnife.bind(this);
    }

    @Override
    public void unbind() {
        unbinder.unbind();
    }

    @Override
    public void doWork() {

    }

}
