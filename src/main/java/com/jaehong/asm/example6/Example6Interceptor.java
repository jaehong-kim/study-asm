package com.jaehong.asm.example6;

import java.util.Arrays;

public class Example6Interceptor {

    public void before(Object target, Object[] args) {
        System.out.println("before target=" + target + ", args=" + Arrays.asList(args));
        throw new RuntimeException("before exception");
    }

    public void after(Object target, Object[] args, Object result, Throwable throwable) {
        System.out.println("after target=" + target + ", args=" + Arrays.asList(args) + ", result=" + result + ", throwable=" + throwable);
        throw new RuntimeException("after exception");
    }
}