package com.example.net.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;


/**
 * Created by magic_chen_ on 2018/9/5.
 * email:chenyouya@leigod.com
 * project:BoheAccelerator_Android
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Post {
    Class<?> value();  //同Get
}
