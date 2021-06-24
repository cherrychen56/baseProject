package com.magic_chen_.baseproject.utils;

import android.Manifest;
import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkInfo;
import android.os.Build;
import android.telephony.SubscriptionInfo;
import android.telephony.SubscriptionManager;
import android.telephony.TelephonyManager;
import android.util.Log;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import androidx.annotation.RequiresApi;

public class SimUtils {

    private static final String TAG = SimUtils.class.getSimpleName();
    private static final String SIM_STATE = "getSimState";
    private static final String SIM_OPERATOR_NAME = "getNetworkOperatorName";
    private static final String SIM_NETWORK_TYPE = "getNetworkType";
    private static final String SIM_IMEI = "getImei";
    private static final String SIM_LINE_NUMBER = "getLine1Number";

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP_MR1)
    public static String getSimPhonenumber(Context context, int slotIdx) {

        if (PermissionUtil.hasSelfPermission(context, Manifest.permission.READ_PHONE_STATE) ||
                PermissionUtil.hasSelfPermission(context, "android.permission.READ_PRIVILEGED_PHONE_STATE")) {
            Log.d(TAG, "READ_PHONE_STATE permission has BEEN granted to getSimPhonenumber().");

            Log.d("magic_chen_", "  slotIdx:" + slotIdx);
            Log.d("magic_chen_", "  Subid:" + getSubidBySlotId(context, slotIdx));

            if (getSimStateBySlotIdx(context, slotIdx)) {
                return (String) getSimByMethod(context, SIM_LINE_NUMBER, getSubidBySlotId(context, slotIdx));
            } else
                return null;
        } else {
            Log.d(TAG, "READ_PHONE_STATE permission has NOT been granted to getSimPhonenumber().");
            return null;
        }
    }

    public static String getSimImei(Context context, int slotIdx) {
        if (PermissionUtil.hasSelfPermission(context, Manifest.permission.READ_PHONE_STATE) ||
                PermissionUtil.hasSelfPermission(context, "android.permission.READ_PRIVILEGED_PHONE_STATE")) {
            Log.d(TAG, "READ_PHONE_STATE permission has BEEN granted to getSimImei().");
            return (String) getSimByMethod(context, SIM_IMEI, slotIdx);
        } else {
            Log.d(TAG, "READ_PHONE_STATE permission has NOT been granted to getSimImei().");
            return null;
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP_MR1)
    public static int getSimNetworkType(Context context, int slotIdx) {
        if (PermissionUtil.hasSelfPermission(context, Manifest.permission.READ_PHONE_STATE)) {
            Log.d(TAG, "READ_PHONE_STATE permission has BEEN granted to getSimNetworkType().");
            if (getSimStateBySlotIdx(context, slotIdx)) {
                return (int) getSimByMethod(context, SIM_NETWORK_TYPE, getSubidBySlotId(context, slotIdx));
            }
        } else {
            Log.d(TAG, "READ_PHONE_STATE permission has NOT been granted to getSimNetworkType().");
        }
        return TelephonyManager.NETWORK_TYPE_UNKNOWN;
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP_MR1)
    public static String getSimNetworkName(Context context, int slotIdx) {
        return getNetworkName(getSimNetworkType(context, slotIdx));
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP_MR1)
    public static String getSimOperatorName(Context context, int slotIdx) {
        if (getSimStateBySlotIdx(context, slotIdx)) {
            return (String) getSimByMethod(context, SIM_OPERATOR_NAME, getSubidBySlotId(context, slotIdx));
        }
        return null;
    }

    /**
     * @param context
     * @param slotIdx:0(sim1),1(sim2)
     * @return
     */
    public static boolean getSimStateBySlotIdx(Context context, int slotIdx) {
        boolean isReady = false;
        Object getSimState = getSimByMethod(context, SIM_STATE, slotIdx);
        if (getSimState != null) {
            int simState = Integer.parseInt(getSimState.toString());
            if ((simState != TelephonyManager.SIM_STATE_ABSENT) && (simState != TelephonyManager.SIM_STATE_UNKNOWN)) {
                isReady = true;
            }
        }
        return isReady;
    }

    public static Object getSimByMethod(Context context, String method, int param) {
        TelephonyManager telephony = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        try {
            Class<?> telephonyClass = Class.forName(telephony.getClass().getName());
            Class<?>[] parameter = new Class[1];
            parameter[0] = int.class;
            Method getSimState = telephonyClass.getMethod(method, parameter);
            Object[] obParameter = new Object[1];
            obParameter[0] = param;
            Object ob_phone = getSimState.invoke(telephony, obParameter);

            if (ob_phone != null) {
                return ob_phone;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    public static class CurrentNetwork {
        public String whichSim;//那张卡
        public String netWorkName;//几G网络
        public String operateName;//卡生厂商
        public String simImei; // imei

        public CurrentNetwork(String simImei) {
            this.simImei = simImei;
        }

        @Override
        public String toString() {
            return "CurrentNetwork{" +
                    "whichSim='" + whichSim + '\'' +
                    ", netWorkName='" + netWorkName + '\'' +
                    ", operateName='" + operateName + '\'' +
                    ", simImei='" + simImei + '\'' +
                    '}';
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            CurrentNetwork that = (CurrentNetwork) o;
            return whichSim.equals(that.whichSim);
        }

        @Override
        public int hashCode() {
            return whichSim.hashCode();
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP_MR1)
    public static List<CurrentNetwork> getCurrentNetwork(Context context) {
//        CurrentNetwork currentNetwork = new CurrentNetwork();
        ConnectivityManager connectionManager = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
//        NetworkInfo networkInfo = connectionManager.getActiveNetworkInfo();
        TelephonyManager tm = (TelephonyManager) context
                .getSystemService(Context.TELEPHONY_SERVICE);
        Log.d(TAG, "state:" + tm.getSimState());
        Network[] allNetworks = connectionManager.getAllNetworks();
        List<CurrentNetwork> currentNetworks = new ArrayList<>();

        String simImei1 = getSimImei(context, 0);
        String simImei2 = getSimImei(context, 1);
        int sim1NetWorkType = getSimNetworkType(context, 0);
        int sim2NetWorkType = getSimNetworkType(context, 1);
        ContentResolver contentResolver = context.getContentResolver();
        Log.d("magic_chen_", "  simImei1:" + simImei1);
        Log.d("magic_chen_", "  simImei2:" + simImei2);
        Log.d("magic_chen_", "sim1NetWorkType:" + getNetworkName(sim1NetWorkType));
        Log.d("magic_chen_", "sim2NetWorkType:" + getNetworkName(sim2NetWorkType));
        Log.d("magic_chen_", "sim1operateName:" + getSimOperatorName(context, 0));
        Log.d("magic_chen_", "sim2operateName:" + getSimOperatorName(context, 1));
        for (Network network : allNetworks) {
            NetworkInfo networkInfo = connectionManager.getNetworkInfo(network);
            Log.d("magic_chen_", "  networkinfo  isconnected" + networkInfo.isConnected());
            if (networkInfo != null) {
                Log.d("magic_chen_", "   " + networkInfo.toString());
                if (networkInfo.getType() == ConnectivityManager.TYPE_MOBILE) {
                    boolean status = networkInfo.isConnected();
                    Log.d(TAG, "status:" + status);
                    Log.d("magic_chen_", "  network subtype:" + networkInfo.getSubtype());

                }
            } else {
                // Logger.d(TAG, "network info is null: ");
            }
        }

        return currentNetworks;
    }

    public static String getNetworkName(int networkType) {
        switch (networkType) {
            case TelephonyManager.NETWORK_TYPE_GPRS:
            case TelephonyManager.NETWORK_TYPE_EDGE:
            case TelephonyManager.NETWORK_TYPE_CDMA:
            case TelephonyManager.NETWORK_TYPE_1xRTT:
            case TelephonyManager.NETWORK_TYPE_IDEN:
                return "2G";
            case TelephonyManager.NETWORK_TYPE_UMTS:
            case TelephonyManager.NETWORK_TYPE_EVDO_0:
            case TelephonyManager.NETWORK_TYPE_EVDO_A:
            case TelephonyManager.NETWORK_TYPE_HSDPA:
            case TelephonyManager.NETWORK_TYPE_HSUPA:
            case TelephonyManager.NETWORK_TYPE_HSPA:
            case TelephonyManager.NETWORK_TYPE_EVDO_B:
            case TelephonyManager.NETWORK_TYPE_EHRPD:
            case TelephonyManager.NETWORK_TYPE_HSPAP:
                return "3G";
            case TelephonyManager.NETWORK_TYPE_LTE:
                return "4G";
            default:
                return "UNKNOWN";
        }
    }

    /**
     * to
     *
     * @param context
     * @param slotId
     * @return
     */
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP_MR1)
    public static int getSubidBySlotId(Context context, int slotId) {
        SubscriptionManager subscriptionManager = (SubscriptionManager) context.getSystemService(
                Context.TELEPHONY_SUBSCRIPTION_SERVICE);
        try {
            Class<?> telephonyClass = Class.forName(subscriptionManager.getClass().getName());
            Class<?>[] parameter = new Class[1];
            parameter[0] = int.class;
            Method getSimState = telephonyClass.getMethod("getSubId", parameter);
            Object[] obParameter = new Object[1];
            obParameter[0] = slotId;
            Object ob_phone = getSimState.invoke(subscriptionManager, obParameter);

            if (ob_phone != null) {
                Log.d(TAG, "slotId:" + slotId + ";" + ((int[]) ob_phone)[0]);
                return ((int[]) ob_phone)[0];
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return -1;

    }


    /**
     * 判断数据流量开关是否打开
     *
     * @param context
     * @return
     */
    public static boolean isMobileEnabled(Context context) {
        try {
            Method getMobileDataEnabledMethod = ConnectivityManager.class.getDeclaredMethod("getMobileDataEnabled");

            getMobileDataEnabledMethod.setAccessible(true);

            ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

            return (Boolean) getMobileDataEnabledMethod.invoke(connectivityManager);

        } catch (Exception e) {

            e.printStackTrace();

        }
        return true;
    }


    /**
     * 获取
     *
     * @param context
     * @return
     */
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP_MR1)
    public static List<SubscriptionInfo> getActiveSubscriptionInfoList(Context context) {
        List<SubscriptionInfo> infos = new ArrayList<>();
        try {
//            SubscriptionManager mSubscriptionManager = SubscriptionManager.from(context);
//            Method method = SubscriptionManager.class.getMethod("getActiveSubscriptionInfoList", null);
//            infos = (List<SubscriptionInfo>) method.invoke(mSubscriptionManager);
//            for (SubscriptionInfo info : infos) {
//                info.getSimSlotIndex();
//                getSubscriptionSubId(context, info.getSubscriptionId());
//                Log.d("magic_chen_", "  subscription info:" + info);
//                Log.d("magic_chen_", "  subscription type11:" + getDefaultDataSubId(context));
//            }

            return infos;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return infos;
    }


    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP_MR1)
    public static int getSubscriptionSubId(Context context, int simid) {
        int id = -1;
        try {
            SubscriptionManager mSubscriptionManager = SubscriptionManager.from(context);
            Method method = SubscriptionManager.class.getDeclaredMethod("getSubId", int.class);
            method.setAccessible(true);
            id = ((int[]) method.invoke(mSubscriptionManager, simid))[0];
            return id;
        } catch (Exception e) {
            e.printStackTrace();

        }
        return id;
    }


    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP_MR1)
    public static int getSimSubType(Context context, int subId) {
        int subType = 0;
        try {
            TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService(Activity.TELEPHONY_SERVICE);
            Method method = TelephonyManager.class.getDeclaredMethod("getDataNetworkType", int.class);
            method.setAccessible(true);
            subType = (int) method.invoke(telephonyManager, subId);
        } catch (Exception e) {

        }
        return subType;

    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP_MR1)
    public static int getDefaultDataSubId(Context context) {
        int id = -1;
        try {
            SubscriptionManager mSubscriptionManager = SubscriptionManager.from(context);
//            Method method = SubscriptionManager.class.getDeclaredMethod("getDefaultSubscriptionId");
            Method method = SubscriptionManager.class.getDeclaredMethod("getDefaultDataPhoneId");
            method.setAccessible(true);
            id = (int) method.invoke(mSubscriptionManager);
        } catch (Exception e) {

        }
        return id;
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP_MR1)
    public static int getDefaultNetWorkSimId(Context context) {
        int id = -1;
        try {
            SubscriptionManager sm = null;
            sm = SubscriptionManager.from(context.getApplicationContext());
            Method getSubId = sm.getClass().getDeclaredMethod("getDefaultDataSubId");
            getSubId.setAccessible(true);
            if (getSubId != null) {
                id = (int) getSubId.invoke(sm);
            }
        } catch (NoSuchMethodException e) {
            try {
                SubscriptionManager sm = SubscriptionManager.from(context.getApplicationContext());
                Method getSubId = sm.getClass().getDeclaredMethod("getDefaultSubscriptionId");
                getSubId.setAccessible(true);
                if (getSubId != null) {
                    id = (int) getSubId.invoke(sm);
                }
            } catch (NoSuchMethodException e1) {
                try {
                    SubscriptionManager sm = SubscriptionManager.from(context.getApplicationContext());
                    Method getSubId = sm.getClass().getDeclaredMethod("getDefaultDataSubscriptionId");
                    getSubId.setAccessible(true);
                    if (getSubId != null) {
                        id = (int) getSubId.invoke(sm);
                    }
                } catch (NoSuchMethodException e2) {
                    try {
                        SubscriptionManager sm = null;
                        sm = SubscriptionManager.from(context.getApplicationContext());
                        Method getSubId = sm.getClass().getDeclaredMethod("getDefaultDataPhoneId");
                        getSubId.setAccessible(true);
                        if (getSubId != null) {
                            id = (int) getSubId.invoke(sm);
                        }
                    } catch (NoSuchMethodException e3) {
                        e3.printStackTrace();
                    } catch (IllegalAccessException e4) {
                        e4.printStackTrace();
                    } catch (InvocationTargetException e5) {
                        e5.printStackTrace();
                    }
                } catch (IllegalAccessException e6) {
                    e6.printStackTrace();
                } catch (InvocationTargetException e7) {
                    e7.printStackTrace();
                }

            } catch (IllegalAccessException ex) {
                ex.printStackTrace();
            } catch (InvocationTargetException ex) {
                ex.printStackTrace();
            }

        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
        return id;
    }

}
