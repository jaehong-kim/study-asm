package com.jaehong.asm.example1;

public class InterceptorRegistery {

    public static FooClassBarMethodInterceptor get() {
        return new FooClassBarMethodInterceptor();
    }
}
