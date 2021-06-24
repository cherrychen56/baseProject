package com.magic_chen_.baseproject.config;



import com.example.net.annotation.Get;
import com.example.net.annotation.Param;
import com.example.net.annotation.Url;
import com.magic_chen_.baseproject.response.AdvertiseResponse;

import rx.Observable;


/**
 * 请求api相关接口
 */
public interface Api {

    //获取广告信息
    @Get(AdvertiseResponse.class)
    @Url("/tools/advertise")
    Observable<AdvertiseResponse> getAdvertise(
            @Param("group") String group,
            @Param("lang") String lang,
            @Param("region_code") int region_code,
            @Param("src_channel") String src_channel);

}
