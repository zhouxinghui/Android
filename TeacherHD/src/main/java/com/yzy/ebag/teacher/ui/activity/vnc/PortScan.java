package com.yzy.ebag.teacher.ui.activity.vnc;

import android.content.Context;

import com.yun.vnc.bVNC.RfbProto;

import java.lang.ref.WeakReference;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import ebag.core.util.L;

/**
 * Created by unicho on 2017/10/20.
 */

public class PortScan {

    private String userId;
    private String ipResult = null;
    private boolean isCancel = false;
    private String[] threadTag;
    private WeakReference<Context> weakReference;
    private String firstIp;
    public PortScan(String firstIp, Context context){
        this.firstIp = firstIp;
        weakReference = new WeakReference<>(context);
    }

    /**
     * 多线程扫描同一网段下的端口
     *
     * @param ip
     *            待扫描IP或域名,eg:180.97.161.184 www.zifangsky.cn
     * @param port
     *            待扫描的端口的Set集合
     * @param threadNumber
     *            线程数
     * @param timeout
     *            连接超时时间
     * */
    public void scanLargePorts(String ip, int port, int userId, int threadNumber, int timeout) {
        isCancel = false;
        this.userId = String.valueOf(userId);
        if(weakReference.get() != null && onScanListener != null)
            onScanListener.start();
        ExecutorService threadPool = Executors.newCachedThreadPool();
        threadTag = new String[threadNumber];
        for (int i = 0; i < threadNumber; i++) {
            ScanMethod scanMethod = new ScanMethod(ip, port,
                    threadNumber, i, timeout);
            threadPool.execute(scanMethod);
        }
        threadPool.shutdown();
    }

    /**
     * 扫描方式：同一网段下的某个端口扫描
     *
     * */
    private class ScanMethod implements Runnable {
        private String ip; // 目标IP
        private int port; // 待扫描的端口的Set集合
        private int threadNumber, timeout; // 线程数，这是第几个线程，超时时间
        private int serial;
        public ScanMethod(String ip, int port
                , int threadNumber, int serial, int timeout) {
            this.ip = ip;
            this.port = port;
            this.serial = serial;
            this.threadNumber = threadNumber;
            this.timeout = timeout;
        }

        public void run() {
            String top3Ip = ip.substring(0,ip.lastIndexOf(".")+1);
            String ipString;
            for (int i = serial; i <= 255; i += threadNumber) {
                if(ipResult != null)
                    break;
                if(i == 0 && firstIp.startsWith(top3Ip)){
                    ipString = firstIp;
                }else{
                    ipString = top3Ip + i;
                }

                if(ipString.endsWith(".0"))
                    return;
                if(canHostConnect(ipString,port,timeout)){
                    setIpString(isCancel ? "" : ipString);
                }
            }

            threadTag[serial] = String.valueOf(serial);
            if(weakReference.get() != null){
                if(isAllThreadFinished()){
                    if(weakReference.get() != null && onScanListener != null)
                        if(isCancel || ipResult == null || ipResult.isEmpty()){
                            onScanListener.cancel();
                            isCancel = false;
                        }else{
                            onScanListener.end(ipResult);
                        }
                }
            }
        }

    }

    private synchronized void setIpString(String ip){
        ipResult = ip;
    }

    private synchronized boolean isAllThreadFinished(){
        for(String str : threadTag){
            if(str == null)
                return false;
        }
        return true;
    }
    private OnScanListener onScanListener;

    public void setOnScanListener(OnScanListener onScanListener) {
        this.onScanListener = onScanListener;
    }

    public interface OnScanListener{
        void start();

        void end(String ipString);

        void cancel();
    }

    private boolean canHostConnect(String host, int port, int timeOut) {
        try {

            RfbProto rfb = new RfbProto(null, null, host, port, -1, true,
                    false, timeOut);
            rfb.initializeAndAuthenticate("", userId);
//            authenticateVNC(socket,String.valueOf(userId));
            rfb.close();
        } catch (Exception e) {
            L.INSTANCE.e("VNC fail",host+":"+port+e.getMessage());
            return false;
        }finally {
        }
        L.INSTANCE.e("VNC success",host+":"+port+":"+userId);
        return true;
    }

    public void cancel(){
        isCancel = true;
    }

}
