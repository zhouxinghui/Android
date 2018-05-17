package com.yzy.ebag.student.service;

import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.RemoteCallbackList;
import android.os.RemoteException;
import android.support.annotation.Nullable;

import com.yzy.ebag.student.IParticipateCallback;
import com.yzy.ebag.student.ITestAidlInterface;

import java.util.Timer;
import java.util.TimerTask;

import ebag.core.util.HandlerUtil;
import ebag.core.util.L;

/**
 * Created by 90323 on 2017/8/23.
 */


public class AIDLTestService extends Service {

    private Timer timer;

    private TimerTask task = new TimerTask() {
        public void run() {
            mHandler.sendEmptyMessage(0);
        }
    };
    private int timeSce;
    private ITestAidlInterface.Stub mBinder = new ITestAidlInterface.Stub(){

        @Override
        public void start(IParticipateCallback cb, int time) throws RemoteException {
            L.INSTANCE.e("AIDLTestService","start");
            timeSce = time * 60;
            mCallbacks.register(cb);
            startTime();
        }

        @Override
        public void unregisterParticipateCallback(IParticipateCallback cb) throws RemoteException {
            L.INSTANCE.e("AIDLTestService", "unregister");
            if (timer != null) {
                mCallbacks.finishBroadcast();
                timer.cancel();
                timer = null;
                task = null;
            }
            mCallbacks.unregister(cb);
        }
    };

    RemoteCallbackList<IParticipateCallback> mCallbacks = new RemoteCallbackList<>();


    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        L.INSTANCE.e("AIDLTestService","kill");
        // 取消掉所有的回调
        mCallbacks.kill();
    }

    private void startTime(){
        final int len = mCallbacks.beginBroadcast();
        L.INSTANCE.e("AIDLTestService",len+"");
        if(len > 0){
            timer = new Timer();
            timer.schedule(task,0,1000);
        }
    }

    private StringBuilder sb;
    private void callBack(){
        try {
            int hour = timeSce / 3600;
            int min = (timeSce - hour * 3600) / 60;
            int sec = timeSce % 60;
            sb = new StringBuilder();
            if(hour > 0)
                sb.append(hour).append(":");
            if(min < 10)
                sb.append(0);
            sb.append(min).append(":");
            if(sec < 10)
                sb.append(0);
            sb.append(sec);
            L.INSTANCE.e("AIDLTestService",sb.toString());
            // 通知回调
            mCallbacks.getBroadcastItem(0).setText(sb.toString(),timeSce);
            if(timeSce == 0) {
                if (timer != null) {
                    mCallbacks.finishBroadcast();
                    timer.cancel();
                    timer = null;
                    task = null;
                }
            }else
                --timeSce;
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    private Handler mHandler = new TimeHandler(this);

    private static class TimeHandler extends HandlerUtil<AIDLTestService> {

        private TimeHandler(AIDLTestService object) {
            super(object);
        }

        @Override
        public void handleMessage(AIDLTestService object, Message msg) {
            switch (msg.what){
                case 0:
                    object.callBack();
                    break;
            }
        }
    }
}
