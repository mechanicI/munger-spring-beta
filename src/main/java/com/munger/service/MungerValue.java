package com.munger.service;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @Author: Munger.;
 * @Date: 2023/3/31 14:04;
 * @Params:
 * @Return:
 * @Description:
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface MungerValue {

    String value() default "";

}
