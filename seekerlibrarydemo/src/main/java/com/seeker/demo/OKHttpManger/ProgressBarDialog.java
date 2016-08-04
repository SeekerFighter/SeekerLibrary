package com.seeker.demo.OKHttpManger;

import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;
import com.seeker.demo.Config;
import com.seeker.demo.R;
import com.seeker.libraries.logger.Logger;
import com.seeker.libraries.okhttp.OKHttpUICallback;
import com.seeker.libraries.okhttp.OkHttpManger;
import java.io.IOException;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import okhttp3.Call;
import okhttp3.Response;

/**
 * Created by Seeker on 2016/7/29.
 */

public class ProgressBarDialog extends Dialog implements OKHttpUICallback.ProgressCallback{

    private static final String TAG = "ProgressBarDialog";

    private Unbinder unbinder;

    @BindView(R.id.progressbar) ProgressBar progressBar;

    private String url;

    public ProgressBarDialog(Context context, int themeResId) {
        super(context, themeResId);
    }

    protected ProgressBarDialog(Context context, boolean cancelable, OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
    }

    public ProgressBarDialog(Context context,String url) {
        super(context);
        this.url = url;
        setContentView(R.layout.progress_layout);
    }

    @Override
    public void setContentView(int layoutResID) {
        super.setContentView(layoutResID);
        unbinder = ButterKnife.bind(this);
    }

    @Override
    public void cancel() {
        unbinder.unbind();
        super.cancel();
    }

    @OnClick(R.id.load)
    public void load(View view){
        try {
            Logger.t(TAG).d("start load!url = "+url);
            OkHttpManger.getInstance().downloadAsync(url, Config.getDirFile("download").getAbsolutePath(), this);
        }catch (IOException e){
            Logger.t(TAG).d("url = "+url+" download error!");
            e.printStackTrace();
        }

    }

    @Override
    public void onSuccess(Call call, Response response,String path) {
        Toast.makeText(getContext(),"download finish!path = "+path,Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onProgress(long byteReadOrWrite, long contentLength, boolean done) {
        Logger.t(TAG).d("byteReadOrWrite = "+byteReadOrWrite+",contentLength = "+contentLength+",done = "+done);
        progressBar.setProgress((int)(byteReadOrWrite / contentLength *100));
    }

    @Override
    public void onError(Call call, IOException e) {
        Toast.makeText(getContext(),"download error!",Toast.LENGTH_SHORT).show();
    }
}
