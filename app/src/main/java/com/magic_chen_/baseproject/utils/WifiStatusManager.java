package com.magic_chen_.baseproject.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.DhcpInfo;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.text.format.Formatter;
import android.util.Log;

import com.magic_chen_.baseproject.GlobalApplication;


/**
 * Created by magic_chen_ on 2018/12/4.
 * email:chenyouya@leigod.com
 * project:Vpn_Router
 */
public class WifiStatusManager {

    public static String getWifiIp() {
        Context myContext = GlobalApplication.getGlobalContext().getApplicationContext();
        if (myContext == null) {
            throw new NullPointerException("Global context is null");
        }
        if (isWifiEnabled()) {

            WifiManager wifiMgr = (WifiManager) myContext.getSystemService(Context.WIFI_SERVICE);
            WifiInfo info = wifiMgr.getConnectionInfo();
            String wifiId = info != null ? info.getSSID() : null;

            return wifiId;
        } else {
            return null;
        }
    }


    public static boolean isWifiEnabled() {
        Context myContext = GlobalApplication.getGlobalContext().getApplicationContext();
        WifiManager wifiMgr = (WifiManager) myContext.getSystemService(Context.WIFI_SERVICE);
        if (wifiMgr.getWifiState() == WifiManager.WIFI_STATE_ENABLED) {
            ConnectivityManager connManager = (ConnectivityManager) myContext
                    .getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo wifiInfo = connManager
                    .getNetworkInfo(ConnectivityManager.TYPE_WIFI);
            return wifiInfo.isConnected();
        } else {
            return false;
        }
    }

    public  static String getIpAddress(Context context) {
        String IpAddress = null;
        DhcpInfo wifiInfo = getWifiInfo(context);
        if (wifiInfo != null) {
            IpAddress =  Formatter.formatIpAddress(wifiInfo.gateway);
        }
        return IpAddress;
    }
    public  static DhcpInfo getWifiInfo(Context context) {
        WifiManager wifiManager = (WifiManager) context.getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        DhcpInfo info = null;
        if (null != wifiManager) {
            info = wifiManager.getDhcpInfo();
        }
        Log.d(" WifiStatusManager ","    DhcpInfo:"+info);
        return info;
    }

    public static String getWifiName(Context context) {

        String wifiName = null;
        WifiManager wifiManager = (WifiManager) context.getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        if (wifiManager != null) {
            wifiManager.startScan();
            WifiInfo info = wifiManager.getConnectionInfo();
            Log.d(" WifiStatusManager ", "    ssid:" + info.getSSID());
            Log.d(" WifiStatusManager ", "    hiddenssid:" + info.getHiddenSSID());
            Log.d(" WifiStatusManager ", "    wifi info:" + info.toString());
            wifiName = info.getSSID();
        }
        return wifiName;

    }

    public static String getWifiMac(Context context){
        String wifiMac = null;
        WifiManager wifiManager = (WifiManager) context.getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        if (wifiManager != null) {
            wifiManager.startScan();
            WifiInfo info = wifiManager.getConnectionInfo();
            wifiMac = info.getBSSID();
        }
        return wifiMac;
    }

}
