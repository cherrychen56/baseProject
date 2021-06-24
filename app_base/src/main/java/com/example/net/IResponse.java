package com.example.net;

import java.io.Serializable;


/**
 * Created by magic_chen_ on 2018/9/5.
 * email:chenyouya@leigod.com
 * project:BoheAccelerator_Android
 */
public interface IResponse extends Serializable {
    int SUCCESS_RESPONSE       = 0;
    int ERROR_RESPONSE         = 1;
    int NO_DATA_RESPONSE       = -1;
    void  setErrorCode(int errorCode);
    void setMsg(String msg);
}
