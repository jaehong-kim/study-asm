package com.jaehong.asm.example8;

public class Example8InterceptorRegistery {

    public static Example8Interceptor get(int index) {
        return new Example8Interceptor();
    }
}