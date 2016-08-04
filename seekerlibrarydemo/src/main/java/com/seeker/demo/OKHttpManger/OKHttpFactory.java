package com.seeker.demo.OKHttpManger;

import com.seeker.demo.Config;
import com.seeker.demo.OKHttpManger.bean.Result;
import com.seeker.libraries.base.BasePresenter;
import com.seeker.libraries.base.BaseView;
import com.seeker.libraries.okhttp.OKHttpUICallback;
import com.seeker.libraries.okhttp.OkHttpManger;

import java.io.IOException;

import okhttp3.Call;

/**
 * Created by Seeker on 2016/7/28.
 */

public class OKHttpFactory {

    public interface OKHttpView<T> extends BaseView{

        void  setDatas(T datas);

        void loadError();
    }

    public static final class OKHttpPresenter extends BasePresenter<OKHttpView<Result>>{

        public OKHttpPresenter(){

        }

        public void load(){
            if(!checkViewNull()){
                bindedView.onPreLoad();
            }
            try{
                OkHttpManger.getInstance().getAsync(Config.URL,callback);
            }catch (IOException e){
                if(!checkViewNull())
                    bindedView.loadError();
                e.printStackTrace();
            }

        }

        private OKHttpUICallback.ResultCallback callback = new OKHttpUICallback.ResultCallback<Result>(){
            @Override
            public void onSuccess(Result result) {
                if(!checkViewNull()){
                    bindedView.setDatas(result);
                    bindedView.onFinishLoad();
                }
            }

            @Override
            public void onError(Call call, IOException e) {
                if (checkViewNull())
                    bindedView.loadError();
            }
        };

    }

}
