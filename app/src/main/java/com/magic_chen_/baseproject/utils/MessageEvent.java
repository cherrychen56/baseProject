package com.magic_chen_.baseproject.utils;

/**
 * eventbus实现类
 */

public class MessageEvent {

    public enum Event {
        USER_IS_LOGOUT,//用户登出
        USER_IS_LOGIN,//用户已经登录
        CLEAR_CACHE,//清除缓存
        INSTALL_SUCCESS,//安装成功
        UNINSTALL,//卸载成功
        MOBILE_GAME_INFO,//手机游戏信息
        CONSOLE_GAME_INFO,//主机游戏信息
        INSTALL_XAPK_GAME,//安装xapk包
        DEV_LIST,//设备列表
        ROUTER_SUPPORT_NUM,//主机支持数量
        SET_ROUTER_SUPPORT_NUM,//设置主机支持数量
        ROUTER_PROXY_PLUGIN,//代理路由器设备
        ROUTER_NO_PROXY_PLUGIN,//不代理路由器设备
        CONSOLE_GAME_IS_ACC,//主机游戏加速中
        ROUTER_ACCELERATER_STATE,//路由器正在加速中
        ROUTER_NOT_ACCELERATOR,//路由器未加速
        ROUTER_STOP_ACCELERATER,//路由器停止加速
        ROUTER_ACCELERATER_SUCCESS,//路由器加速成功
        ROUTER_STATE_FAILED,//路由器状态获取失败
        ROUTER_GALLERY_CLOSE,//路由器状态获取失败
        ROUTER_LOGIN_TIMEOUT,//登录超时
        ROUTER_LOGIN_FAILED,//登录失败
        ROUTER_LOGIN_SUCCESS,//登录成功
        ROUTER_PULGIN_DOWNLOAD_SUCCESS,//路由器插件下载成功
        ROUTER_PULGIN_DOWNLOAD_FAILED,//路由器插件下载失败
        ROUTER_PULGIN_INSTALL_FAILED,//路由器插件安装失败
        ROUTER_PULGIN_INSTALL_SUCCESS,//路由器插件安装成功
        ROUTER_PULGIN_CHMOD_FAILED,//插件增加执行权限失败
        ROUTER_VERSION,//路由器当前版本
        ACCELERATOR_LINE_INFO,//加速线路信息
        ROUTER_LOG,//路由器日志
        GOTO_DOWNLOAD_GAME,//去下载游戏
        ROUTER_UNINSTALL_SUCCESS,//路由器卸载成功
        WIFI_SINGLE_CHANGE,//wifi信号发生改变
        GPRS_SINGLE_CHANGE,//gprs信号发生改变

    }

    public Event eventType;
    private String string;
    private Object objectInfo;

    public MessageEvent(Event eventType) {
        this.eventType = eventType;
    }

    public MessageEvent(Event eventType, String string) {
        this.eventType = eventType;
        this.string = string;
    }
    public MessageEvent(Event eventType, Object objectModel) {
        this.eventType = eventType;
        this.objectInfo = objectModel;
    }

    public String getString() {
        return string;
    }

    public Object getObjectInfo() {
        return objectInfo;
    }
}
