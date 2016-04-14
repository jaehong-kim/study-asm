package com.jaehong.asm.example4;

public class Example4InvokeStatic {

    public Example4InvokeStatic() {
        Example4Interceptor interceptor = Example4InterceptorRegistery.get();
    }


    public void bar() {
        Example4Interceptor interceptor = Example4InterceptorRegistery.get();
    }
}
