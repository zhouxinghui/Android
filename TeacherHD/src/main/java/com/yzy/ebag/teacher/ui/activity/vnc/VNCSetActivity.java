package com.yzy.ebag.teacher.ui.activity.vnc;

import android.content.Context;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.yun.vnc.bVNC.COLORMODEL;
import com.yun.vnc.bVNC.MainConfiguration;
import com.yun.vnc.bVNC.RfbProto;
import com.yzy.ebag.teacher.R;

import java.net.InetAddress;
import java.net.UnknownHostException;

import ebag.core.util.SPUtils;
import ebag.core.util.SerializableUtils;
import ebag.core.util.T;
import ebag.core.xRecyclerView.SimpleViewSwitcher;
import ebag.core.xRecyclerView.progressindicator.AVLoadingIndicatorView;
import ebag.hd.base.Constants;
import ebag.hd.bean.response.UserEntity;
import ebag.hd.widget.TitleBar;


/**
 * Created by unicho on 2017/11/20.
 */

public class VNCSetActivity extends MainConfiguration implements View.OnClickListener{

    private EditText ipEdit;
    private Spinner colorSpinner;
    private TextView vncAutoText;
    private ImageView vncAutoImage;
    private TextView vncAutoResult;
    private TextView vncAutoConnect;
    private TextView vncAutoTip;
    private PortScan portScan;
    private TitleBar titleBar;

    private String ipResult;
    private SimpleViewSwitcher simpleViewSwitcher;
    private VNCSetActivity mContext;


    @Override
    public void onCreate(Bundle icicle) {
        layoutID = R.layout.activity_vnc_setting;
        super.onCreate(icicle);
        mContext = this;

        titleBar = findViewById(R.id.title_bar);
        titleBar.setTitle("电脑同屏");
        titleBar.setRightText("重新扫描", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startScan();
            }
        });
        vncAutoText = (TextView) findViewById(R.id.vnc_auto_text);
        vncAutoImage = (ImageView) findViewById(R.id.vnc_auto_image);
        vncAutoResult = (TextView) findViewById(R.id.vnc_auto_result);
        vncAutoConnect = (TextView) findViewById(R.id.vnc_auto_connect);
        vncAutoTip = (TextView) findViewById(R.id.vnc_auto_tip);
        simpleViewSwitcher = (SimpleViewSwitcher) findViewById(R.id.simpleViewSwitcher);
        AVLoadingIndicatorView progressView = new  AVLoadingIndicatorView(this);
        progressView.setIndicatorColor(0xff333333);
        progressView.setIndicatorId(0);
        simpleViewSwitcher.setView(progressView);

        vncAutoImage.setSelected((Boolean) SPUtils.get(this, "vnc_auto_connect", false));

        ipEdit = (EditText) findViewById(R.id.ip_edit);
        ipEdit.setText((String)SPUtils.get(mContext, "vnc_last_connected_ip", null));
        colorSpinner = (Spinner) findViewById(R.id.color_format);
        COLORMODEL[] models= COLORMODEL.values();
        ArrayAdapter<COLORMODEL> colorSpinnerAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, models);
        colorSpinner.setAdapter(colorSpinnerAdapter);
        colorSpinner.setSelection(0);


        findViewById(R.id.vnc_connect).setOnClickListener(this);
        vncAutoConnect.setOnClickListener(this);
        vncAutoImage.setOnClickListener(this);

        startScan();
    }

    @Override
    protected void updateViewFromSelected() {
        if (selected == null)
            return;
        ipEdit.setText(selected.getAddress());

        textNickname.setText(selected.getNickname());
        COLORMODEL cm = COLORMODEL.valueOf(selected.getColorModel());
        COLORMODEL[] colors= COLORMODEL.values();
        for (int i=0; i<colors.length; ++i)
        {
            if (colors[i] == cm) {
                colorSpinner.setSelection(i);
                break;
            }
        }
    }

    @Override
    protected void updateSelectedFromView() {
        if (selected == null) {
            return;
        }

        try {
            String ipString = ipEdit.getText().toString();
            if(ipString.isEmpty()) {
                T.INSTANCE.show(mContext, "请输入IP地址");
                return;
            }
            InetAddress ip = InetAddress.getByName(ipString);
            selected.setAddress(ip.getHostAddress());
        } catch (UnknownHostException e) {
            e.printStackTrace();
            T.INSTANCE.show(mContext, "IP地址输入错误");
            return;
        }

        selected.setPort(5900);
        selected.setNickname(textNickname.getText().toString());


        // If we are using an SSH key, then the ssh password box is used
        // for the key pass-phrase instead.
        selected.setUserName("");
        selected.setForceFull(0);
        UserEntity userEntity = SerializableUtils.getSerializable(Constants.INSTANCE.getTEACHER_USER_ENTITY());
        int userId = Integer.parseInt(userEntity.getUid());
        selected.setPassword(String.valueOf(userId));
        selected.setKeepPassword(true);
        selected.setUseDpadAsArrows(true);
        selected.setUseLocalCursor(false);
        selected.setPrefEncoding(RfbProto.EncodingTight);
        selected.setViewOnly(false);

        selected.setColorModel(((COLORMODEL)colorSpinner.getSelectedItem()).nameString());
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()){
            case R.id.vnc_connect:
                if(portScan != null){
                    portScan.cancel();
                }
                canvasStart();
                break;
            case R.id.vnc_auto_connect:
                if(ipResult == null || ipResult.isEmpty()){
                    T.INSTANCE.show(mContext, "请重新扫描，或者手动输入IP地址");
                    return;
                }
                canvasStart();
                break;
            case R.id.vnc_auto_image:
                v.setSelected(!v.isSelected());
                SPUtils.put(this, "vnc_auto_connect", v.isSelected());
                break;
        }
    }

    private void startScan(){
        if(portScan == null){
            portScan = new PortScan(ipEdit.getText().toString(),this);

            portScan.setOnScanListener(new PortScan.OnScanListener() {
                @Override
                public void start() {
                    vncAutoText.setText("设备搜索中");
                    vncAutoResult.setText("IP 地址:搜索中...");
                    toggleConnect(false);
                }

                @Override
                public void end(final String ipString) {
                    mContext.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if(ipString == null || ipString.isEmpty()){
                                T.INSTANCE.show(mContext, "请在电脑端打开同步服务，并且确保电脑和平板处于同一网段下");
                                return;
                            }
                            ipResult = ipString;
                            SPUtils.put(mContext, "vnc_last_connected_ip", ipResult);
                            ipEdit.setText(ipResult);
                            vncAutoText.setText("发现可连接设备");
                            vncAutoResult.setText("IP 地址:"+ipString);
                            toggleConnect(true);
                            if(vncAutoImage.isSelected()){
                                canvasStart();
//                                startVnc(ipResult,((COLORMODEL)colorSpinner.getSelectedItem()).nameString());
                            }
                        }
                    });
                }

                @Override
                public void cancel() {
                    mContext.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            vncAutoText.setText("暂无可连接设备");
                            simpleViewSwitcher.setVisibility(View.GONE);
                            vncAutoResult.setText("IP 地址: 0.0.0.0");
                            titleBar.setRightBtnVisable(true);
                        }
                    });
                }
            });
        }

        //获取wifi服务
        WifiManager wifiManager = (WifiManager) this.getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        //判断wifi是否开启
        if (!wifiManager.isWifiEnabled()) {
            T.INSTANCE.show(mContext, "请开启WIFI，并确保和电脑处于同一网段下");
            return;
        }

        WifiInfo wifiInfo = wifiManager.getConnectionInfo();
        String top3ip = intToIp(wifiInfo.getIpAddress());
        UserEntity userEntity = SerializableUtils.getSerializable(Constants.INSTANCE.getTEACHER_USER_ENTITY());
        int userId = Integer.parseInt(userEntity.getUid());

        portScan.scanLargePorts(top3ip,5900,userId,4,300);
    }

    private void toggleConnect(boolean showConnect){
        if(showConnect){
            vncAutoConnect.setVisibility(View.VISIBLE);
            titleBar.setRightBtnVisable(true);
            vncAutoImage.setVisibility(View.GONE);
            vncAutoTip.setVisibility(View.INVISIBLE);
            simpleViewSwitcher.setVisibility(View.GONE);
        }else{
            vncAutoConnect.setVisibility(View.GONE);
            titleBar.setRightBtnVisable(false);
            vncAutoImage.setVisibility(View.VISIBLE);
            vncAutoTip.setVisibility(View.VISIBLE);
            simpleViewSwitcher.setVisibility(View.VISIBLE);
        }

    }

    /**
     * 曲IP地址段的前3个
     */
    private String intToIp(int i) {

//        return (i & 0xFF ) + "." +
//                ((i >> 8 ) & 0xFF) + "." +
//                ((i >> 16 ) & 0xFF) + "." +
//                ( i >> 24 & 0xFF) ;

        return (i & 0xFF ) + "." +
                ((i >> 8 ) & 0xFF) + "." +
                ((i >> 16 ) & 0xFF) + ".";
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(portScan != null){
            portScan.cancel();
        }
    }
}
