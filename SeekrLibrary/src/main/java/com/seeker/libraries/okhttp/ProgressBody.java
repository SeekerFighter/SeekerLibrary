package com.seeker.libraries.okhttp;

import android.os.Handler;
import java.io.IOException;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import okio.Buffer;
import okio.BufferedSink;
import okio.BufferedSource;
import okio.ForwardingSink;
import okio.ForwardingSource;
import okio.Okio;
import okio.Sink;
import okio.Source;

/**
 * Created by Seeker on 2016/7/27.
 */
public final class ProgressBody {

    /**
     * 包装响应体，用于处理提示上传进度
     *
     * Created by Seeker on 2016/6/29.
     */
    public static final class ProgressRequestBody extends RequestBody {

        //实际待包装的请求体
        private final RequestBody requestBody;

        //上传进度回调接口
        private OKHttpUICallback.ProgressCallback mListener;

        //包装完成的BufferedSink
        private BufferedSink bufferedSink;

        //传递下载进度到主线程
        private Handler mHandler;

        public ProgressRequestBody(RequestBody requestBody, OKHttpUICallback.ProgressCallback listener, Handler handler){
            this.requestBody = requestBody;
            this.mListener = listener;
            this.mHandler = handler;
        }

        @Override
        public long contentLength() throws IOException {
            return requestBody.contentLength();
        }

        @Override
        public MediaType contentType() {
            return requestBody.contentType();
        }

        @Override
        public void writeTo(BufferedSink sink) throws IOException {
            if(bufferedSink == null){
                //开始包装
                bufferedSink = Okio.buffer(sink(sink));
            }
            //写入
            requestBody.writeTo(bufferedSink);
            bufferedSink.flush();
        }

        /**
         * 写入，回调进度接口
         */

        private Sink sink(Sink sink){
            return new ForwardingSink(sink) {
                //当前写入字节数
                long byteWriteed = 0L;
                //总得字节数
                long contentBytes = 0L;
                @Override
                public void write(Buffer source, long byteCount) throws IOException {
                    super.write(source, byteCount);
                    if(mHandler != null && mListener != null){
                        if(contentBytes == 0L){
                            contentBytes = contentLength();
                        }
                        byteWriteed += byteCount;
                        mListener.onProgress(byteWriteed, contentBytes, byteWriteed == contentBytes);
                    }
                }
            };
        }
    }


    /**
     * 包装响应体，用于处理提示下载进度
     *
     * Created by Seeker on 2016/6/29.
     */
    public static final class ProgressResponseBody extends ResponseBody {

        //实际待包装的响应体
        private final ResponseBody responseBody;

        //进度回调接口
        private OKHttpUICallback.ProgressCallback mListener;

        //包装完成的BufferedSource
        private BufferedSource bufferedSource;

        //传递下载进度到主线程
        private Handler mHandler;

        public ProgressResponseBody(ResponseBody responseBody, OKHttpUICallback.ProgressCallback listener, Handler handler){
            this.responseBody = responseBody;
            this.mListener = listener;
            this.mHandler = handler;
        }

        @Override
        public MediaType contentType() {
            return responseBody.contentType();
        }

        @Override
        public long contentLength() {
            return responseBody.contentLength();
        }

        @Override
        public BufferedSource source() {
            if(bufferedSource == null){
                bufferedSource = Okio.buffer(source(responseBody.source()));
            }
            return bufferedSource;
        }

        /**
         * 读取，回调进度接口
         * @return
         */
        private Source source(Source source){
            return new ForwardingSource(source) {
                //读取当前获取的字节数
                long totalBytesRead = 0L;

                @Override
                public long read(Buffer sink, long byteCount) throws IOException {
                    final long byteRead =  super.read(sink, byteCount);
                    if(mHandler != null && mListener != null){
                        //增加当前读取的字节数，如果读取完成则返回-1
                        totalBytesRead += byteRead != -1?byteRead:0;
                        //回调，若是contentLength()不知道长度，则返回-1
                        mHandler.post(new Runnable() {
                            @Override
                            public void run() {
                                mListener.onProgress(totalBytesRead, contentLength(), byteRead == -1);
                            }
                        });
                    }
                    return byteRead;
                }
            };
        }
    }

}
