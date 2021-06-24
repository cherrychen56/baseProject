package com.magic_chen_.baseproject.config;


import android.os.Parcelable;

import com.magic_chen_.baseproject.MainActivity;
import com.magic_chen_.baseproject.jumper.ActivityInfo;
import com.magic_chen_.baseproject.jumper.BaseIntent;
import com.magic_chen_.baseproject.jumper.IntentParam;


/**
 *
 */
public interface Jumper {

    @ActivityInfo(clz = MainActivity.class)
    BaseIntent addressList(@IntentParam("addressModel") Parcelable addressModel);

}