//package com.seeker.libraries.retrofit;
//
//import java.util.List;
//import java.util.concurrent.Executor;
//import okhttp3.OkHttpClient;
//import retrofit2.CallAdapter;
//import retrofit2.Converter;
//import retrofit2.Retrofit;
//
///**
// * Created by Seeker on 2016/8/15.
// *
// * @link url [http://www.jcodecraeer.com/a/anzhuokaifa/androidkaifa/2016/0518/4270.html
// *       http://www.jcodecraeer.com/a/anzhuokaifa/androidkaifa/2015/0915/3460.html]
// *
// */
//
//public abstract class BaseRetrofit {
//
//    public Retrofit retrofit;
//
//    public BaseRetrofit(){
//
//        Retrofit.Builder builder = new Retrofit.Builder();
//
//        if(getOkHttpClient() != null){
//            builder.client(getOkHttpClient());
//        }
//
//        if(getCallFactory() != null){
//            builder.callFactory(getCallFactory());
//        }
//
//        builder.baseUrl(getBaseUrl());
//
//        List<Converter.Factory> factories = getConverterFactory();
//        if(factories != null && factories.size() > 0){
//            for(Converter.Factory factory:factories){
//                builder.addConverterFactory(factory);
//            }
//        }
//
//        List<CallAdapter.Factory> factories1 = getCallAdapterFactory();
//        if(factories1 != null && factories1.size() > 0){
//            for (CallAdapter.Factory factory:factories1){
//                builder.addCallAdapterFactory(factory);
//            }
//        }
//
//        if(getCallbackExecutor() != null){
//            builder.callbackExecutor(getCallbackExecutor());
//        }
//
//        builder.validateEagerly(validateEagerly());
//
//        retrofit = builder.build();
//    }
//
//    /**
//     * 返回OKHttpClient客户端
//     * @return
//     */
//    public OkHttpClient getOkHttpClient(){
//        return null;
//    }
//
//    /**
//     * 返回 okhttp3.Call.Factory
//     * @return
//     */
//    public okhttp3.Call.Factory getCallFactory(){
//        return null;
//    }
//
//    /**
//     * 设置基础url
//     * @return
//     */
//    public abstract String getBaseUrl();
//
//    /**
//     * 返回一个converter factory list数组
//     * @return
//     */
//    public List<Converter.Factory> getConverterFactory(){
//        return null;
//    }
//
//    /**
//     * 返回一个call adapter factory list 数组
//     * @return
//     */
//    public List<CallAdapter.Factory> getCallAdapterFactory(){
//        return null;
//    }
//
//    /**
//     * 返回一个Executor
//     * @return
//     */
//    public Executor getCallbackExecutor(){
//        return null;
//    }
//
//    /**
//     *
//     * @return
//     */
//    public boolean validateEagerly(){
//        return false;
//    }
//
//}
