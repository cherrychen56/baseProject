package com.magic_chen_.baseproject.config;




import com.example.net.ApiInvokeHandler;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.magic_chen_.baseproject.GlobalApplication;
import com.magic_chen_.baseproject.Globals;
import com.magic_chen_.baseproject.jumper.JumperInvokeHandler;
import com.magic_chen_.baseproject.net.Net;

import java.lang.reflect.Modifier;
import java.lang.reflect.Proxy;



/**
 *
 */
public interface Const extends Globals  {

    String BASE_URL = AppConfig.getHostUrl();
    Gson GSON = new GsonBuilder().excludeFieldsWithModifiers(Modifier.PRIVATE, Modifier.PROTECTED)
            .setDateFormat("yyyy-MM-dd HH:mm:ss")
            .create();


//    ILoadViewFactory sLoadViewFactory = new DefaultLoadViewFactory();

    Api API = (Api) Proxy.newProxyInstance(Api.class.getClassLoader(), new Class[]{Api.class}, new ApiInvokeHandler(new Net()));
    Jumper JUMPER = (Jumper) Proxy.newProxyInstance(Jumper.class.getClassLoader(), new Class[]{Jumper.class}, new JumperInvokeHandler(GlobalApplication.getGlobalContext()));

    //signature
    String SIGNATURE_KEY = "00393*&Dldkklabc8793939390ua";


    String isSDExist = "/storage/external_storage";//"/sdcard"; //U盘路径  "/storage/external_storage";

    String URL_KEY_C = "c";
    String URL_KEY_M = "m";
    String CATEGORY_ID = "category_id";
    String IMG_URL = "url";

    //wifi security type
    int SECURITY_OPEN = 0;
    int SECURITY_WEP = 1;
    int SECURITY_EAP = 2;
    int SECURITY_WPA = 3;
    int SECURITY_UNKNOWN = 4;

    int MSG_CHANGE_PROVICE = 111;
    int MSG_CHANGE_COUNTY = MSG_CHANGE_PROVICE + 1;
    int MSG_ARG2_DISMISS = MSG_CHANGE_COUNTY + 1;
    int MSG_DELETE_ADDRESS = MSG_ARG2_DISMISS + 1;
    int MSG_DEFAULT_ADDRESS = MSG_DELETE_ADDRESS + 1;
    int MSG_EDIT_ADDRESS = MSG_DEFAULT_ADDRESS + 1;

    String STR_COUNTY_NULL = "county_null";

    String JUMP_TO_ADD_ADDRESS_TYPE = "jump_to_add_address_type";
    String JUMP_ADD_ADDRESS_FROM_ORDER = "jump_from_order";
    String JUMP_ADD_ADDRESS_FROM_ADDRESS_LIST = "jump_from_address_list";
    String JUMP_ADD_ADDRESS_EDIT_ADDRESS = "jump_edit_address";


}
