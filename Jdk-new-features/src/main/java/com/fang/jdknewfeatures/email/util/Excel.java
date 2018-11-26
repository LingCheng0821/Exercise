package com.fang.jdknewfeatures.email.util;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @program: bi-cloud
 * @Date: 2018/11/20 17:37
 * @Author: chengling.bj
 * @Description:
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD, ElementType.TYPE})
public @interface Excel {

  //列名
  String name() default "";

  //宽度
  int width() default 20;

}