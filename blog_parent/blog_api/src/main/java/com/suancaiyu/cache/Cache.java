package com.suancaiyu.cache;

public @interface  Cache {

    long expire() default 1 * 60 * 1000;

    String name() default "";
}
