package com.seeker.demo.dialog;

import android.view.View;
import android.widget.TextView;
import android.widget.Toast;
import com.seeker.demo.R;
import com.seeker.libraries.base.BaseActivity;
import com.seeker.libraries.os.AdvancedCountdownTimer;
import com.seeker.libraries.widget.dialog.LoadDialog;
import com.seeker.libraries.widget.dialog.WarnDialog;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * Created by Seeker on 2016/8/4.
 */

public class DialogActivity extends BaseActivity{

    private Unbinder unbinder;

    @BindView(R.id.timeReamin)
    TextView timeRemain;

    @Override
    public int getLayoutId() {
        return R.layout.activity_dialog;
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

    @OnClick(R.id.ios_style)
    public void onIOSClick(View view){
        new WarnDialog.Builder()
                .setTitle("提示")
                .setContent("嗨!这是IOS风格的Dialog。它简洁而不失优雅，非常方便使用，希望你能喜欢它！^_^")
                .setNegativeStr("取 消")
                .setPositiveStr("确 定")
                .setPositiveClickListener(new WarnDialog.PositiveClickListener() {
                    @Override
                    public void onPositiveClick() {
                        Toast.makeText(DialogActivity.this,"确 定",Toast.LENGTH_SHORT).show();
                    }
                })
                .setNegativeClickListener(new WarnDialog.NegativeClickListener() {
                    @Override
                    public void onNegativeClick() {
                        Toast.makeText(DialogActivity.this,"取 消",Toast.LENGTH_SHORT).show();
                    }
                })
                .create(getSupportFragmentManager(),"warnIosDemo");
    }

    @OnClick(R.id.material_style)
    public void onMaterialClick(View view){
        new WarnDialog.Builder()
                .setTitle("提示")
                .setContent("嗨!这是Material风格的Dialog。它简洁而不失优雅，非常方便使用，希望你能喜欢它！^_^")
                .setNegativeStr("取 消")
                .setPositiveStr("确 定")
                .setStyle(WarnDialog.STYLE_MATERIAL)
                .setPositiveClickListener(new WarnDialog.PositiveClickListener() {
                    @Override
                    public void onPositiveClick() {
                        Toast.makeText(DialogActivity.this,"确 定",Toast.LENGTH_SHORT).show();
                    }
                })
                .setNegativeClickListener(new WarnDialog.NegativeClickListener() {
                    @Override
                    public void onNegativeClick() {
                        Toast.makeText(DialogActivity.this,"取 消",Toast.LENGTH_SHORT).show();
                    }
                })
                .create(getSupportFragmentManager(),"warnMaterialDemo");
    }

    @OnClick(R.id.classic_style)
    public void onClassicClick(View view){
        final LoadDialog loadDialog = new LoadDialog.Builder()
                .setStyle(LoadDialog.STYLE_CLASSIC)
                .create(getSupportFragmentManager(),"loadClassiclDemo");
        startCountDownTime(loadDialog);
    }

    @OnClick(R.id.load_material_style)
    public void onLoadMaterialClick(View view){
        final LoadDialog loadDialog =  new LoadDialog.Builder()
                .setStyle(LoadDialog.STYLE_MATERIAL)
                .create(getSupportFragmentManager(),"loadClassiclDemo");
        startCountDownTime(loadDialog);
    }

    private void startCountDownTime(final LoadDialog loadDialog){
        timeRemain.setText(""+10);
        new AdvancedCountdownTimer.Builder()
                .setCountDownInterval(1000)
                .setMillisInFuture(10000)
                .setCoundDownback(new AdvancedCountdownTimer.CountDownCallback() {
                    @Override
                    public void onTick(long millisUntilFinished, int percent) {
                        timeRemain.setText(""+millisUntilFinished/ 1000);
                    }

                    @Override
                    public void onFinish() {
                        loadDialog.dismiss();
                    }
                }).build()
        .start();
    }

}
