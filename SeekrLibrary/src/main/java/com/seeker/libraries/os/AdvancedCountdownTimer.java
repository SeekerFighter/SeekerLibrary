package com.seeker.libraries.os;

/**
 * Created by Seeker on 2016/8/4.
 * 倒计时：可以暂停，取消，快进快退功能
 */
import android.os.Handler;
import android.os.Message;

public final class AdvancedCountdownTimer {

    private static final int MSG_RUN = 0x001;
    private static final int MSG_PAUSE = 0x002;

    private final long mCountdownInterval;

    private long mTotalTime;

    private long mRemainTime;

    private CountDownCallback callback;

    public AdvancedCountdownTimer(Builder builder) {
        this.mTotalTime = builder.millisInFuture;
        this.mCountdownInterval = builder.countDownInterval;
        this.mRemainTime = builder.millisInFuture;
        this.callback = builder.callback;
    }

    public final void seek(int value) {
        synchronized (AdvancedCountdownTimer.this) {
            mRemainTime = ((100 - value) * mTotalTime) / 100;
        }
    }


    public final void cancel() {
        mHandler.removeMessages(MSG_RUN);
        mHandler.removeMessages(MSG_PAUSE);
    }

    public final void resume() {
        mHandler.removeMessages(MSG_PAUSE);
        mHandler.sendMessageAtFrontOfQueue(mHandler.obtainMessage(MSG_RUN));
    }

    public final void pause() {
        mHandler.removeMessages(MSG_RUN);
        mHandler.sendMessageAtFrontOfQueue(mHandler.obtainMessage(MSG_PAUSE));
    }


    public synchronized final AdvancedCountdownTimer start() {
        if (mRemainTime <= 0) {
            if(callback != null){
                callback.onFinish();
            }
            return this;
        }
        mHandler.sendMessageDelayed(mHandler.obtainMessage(MSG_RUN),
                mCountdownInterval);
        return this;
    }

    /**
     * 倒计时回调接口
     */
    public interface CountDownCallback{
        void onTick(long millisUntilFinished, int percent);
        void onFinish();
    }

    private Handler mHandler = new Handler() {

        @Override
        public void handleMessage(Message msg) {

            synchronized (AdvancedCountdownTimer.this) {
                if (msg.what == MSG_RUN) {
                    mRemainTime = mRemainTime - mCountdownInterval;

                    if (mRemainTime <= 0) {
                        if(callback != null){
                            callback.onTick(mRemainTime, new Long(100
                                    * (mTotalTime - mRemainTime) / mTotalTime)
                                    .intValue());
                            callback.onFinish();
                        }
                    } else if (mRemainTime < mCountdownInterval) {
                        sendMessageDelayed(obtainMessage(MSG_RUN), mRemainTime);
                    } else {
                        if(callback != null){
                            callback.onTick(mRemainTime, new Long(100
                                    * (mTotalTime - mRemainTime) / mTotalTime)
                                    .intValue());
                        }
                        sendMessageDelayed(obtainMessage(MSG_RUN),
                                mCountdownInterval);
                    }
                } else if (msg.what == MSG_PAUSE) {

                }
            }
        }
    };

    public static final class Builder{

        private long millisInFuture;

        private long countDownInterval;

        private CountDownCallback callback;

        public Builder(){

        }

        public Builder setMillisInFuture(long millisInFuture){
            this.millisInFuture = millisInFuture;
            return this;
        }

        public Builder setCountDownInterval(long countDownInterval){
            this.countDownInterval = countDownInterval;
            return this;
        }

        public Builder setCoundDownback(CountDownCallback downback){
            this.callback = downback;
            return this;
        }

        public AdvancedCountdownTimer build(){
            return new AdvancedCountdownTimer(this);
        }

    }

}

