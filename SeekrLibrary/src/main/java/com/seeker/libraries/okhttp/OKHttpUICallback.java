package com.seeker.libraries.okhttp;

import java.io.IOException;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

import okhttp3.Call;
import okhttp3.Response;

/**
 * Created by Seeker on 2016/7/27.
 *
 * 回调主线程的接口
 */
public class OKHttpUICallback {

    /**
     * 异步回调接口
     */
    public static abstract class ResultCallback<T>{

        public Type mType;

        public ResultCallback(){
            mType = getSuperclassTypeParameter(getClass());
        }

        private static Type getSuperclassTypeParameter(Class<?> subclass){
            Type superclass = subclass.getGenericSuperclass();
            if(superclass instanceof Class){
                throw new RuntimeException("Miss type parameter");
            }
            ParameterizedType parameterizedType = (ParameterizedType)superclass;
            return parameterizedType.getActualTypeArguments()[0];
        }

        public abstract void onSuccess(T result);
        public abstract void onError(Call call, IOException e);
    }

    /**
     * 带有进度的上传、下载回调接口
     */
    public interface ProgressCallback{
        void onSuccess(Call call, Response response,String path);
        void onProgress(long byteReadOrWrite, long contentLength, boolean done);
        void onError(Call call, IOException e);
    }
}
