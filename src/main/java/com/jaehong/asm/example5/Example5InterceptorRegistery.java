package com.jaehong.asm.example5;


public class Example5InterceptorRegistery {

    public static Example5Interceptor get(int index) {
        return new Example5Interceptor();
    }
}
