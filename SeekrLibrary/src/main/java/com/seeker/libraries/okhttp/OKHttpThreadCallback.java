package com.seeker.libraries.okhttp;

import android.os.Handler;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

/**
 * Created by Seeker on 2016/7/27.
 */
public final class OKHttpThreadCallback implements Callback {

    private Handler UIHandler;

    private OKHttpUICallback.ProgressCallback UICallback;

    private boolean isDownload;

    private File downFile;

    public OKHttpThreadCallback(Handler handler, OKHttpUICallback.ProgressCallback callback, boolean isDownload){
        this.UIHandler = handler;
        this.UICallback = callback;
        this.isDownload = isDownload;
    }

    @Override
    public void onFailure(final Call call, final IOException e) {
        if(UICallback != null && UIHandler != null){
            UIHandler.post(new Runnable() {
                @Override
                public void run() {
                    UICallback.onError(call,e);
                }
            });
        }
    }

    @Override
    public void onResponse(Call call, Response response) throws IOException {
        if(isDownload){
            download(call,response);
        }else{
            postSuccess(call,response);
        }
    }

    /**
     * 设置保存file
     * @param file
     */
    public OKHttpThreadCallback setFile(File file){
        this.downFile = file;
        return this;
    }

    /**
     * 获取下载数据并写入文件
     * @param response
     */
    private void download(Call call, Response response) throws IOException {
        if(downFile == null){
            throw new NullPointerException("downFile == null");
        }
        byte[] buffer = new byte[2048];
        InputStream is = response.body().byteStream();
        int len;
        FileOutputStream fos = new FileOutputStream(downFile);
        while ((len = is.read(buffer)) != -1){
            fos.write(buffer,0,len);
        }
        fos.flush();
        if(is != null){
            is.close();
        }
        if (fos != null){
            fos.close();
        }
        postSuccess(call,null);
    }

    /**
     * 回调成功信息
     * @param call
     * @param response
     */
    private void postSuccess(final Call call, final Response response){
        if(UICallback != null && UIHandler != null){
            UIHandler.post(new Runnable() {
                @Override
                public void run() {
                    UICallback.onSuccess(call, response,downFile == null?null:downFile.getAbsolutePath());
                }
            });
        }
    }
}
