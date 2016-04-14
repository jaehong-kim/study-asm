package com.jaehong.asm.example6;


public class Example6InterceptorRegistery {

    public static Example6Interceptor get(int index) {
        return new Example6Interceptor();
    }
}
