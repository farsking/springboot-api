package com.yanbin.filter.duplication;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Created by yanbin on 2017/7/1.
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface DuplicationAnnotation {

    boolean validateToken() default false;

    BusinessType businessType() default BusinessType.NULL;
}
