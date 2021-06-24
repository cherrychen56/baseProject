package com.magic_chen_.baseproject.utils;

import android.content.Context;
import android.telephony.TelephonyManager;
import android.util.Log;

import com.magic_chen_.baseproject.R;


/**
 * Created by magic_chen_ on 2018/9/29.
 * email:chenyouya@leigod.com
 * project:BoheAccelerator_Android
 */
public class CountryCodeUtil {
    /**
     * 获取国家码
     */
    public static String getCountryCode(Context context) {
        String CountryID = "";
        String CountryZipCode = "";
        TelephonyManager manager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        CountryID = manager.getSimCountryIso().toUpperCase();
        Log.d("ss", "CountryID--->>>" + CountryID);
        String[] rl = context.getResources().getStringArray(R.array.CountryCodes);
        for (int i = 0; i < rl.length; i++) {
            String[] g = rl[i].split(",");
            if (g[1].trim().equals(CountryID.trim())) {
                CountryZipCode = g[0];
                break;
            }
        }
        return CountryZipCode;
    }
}
