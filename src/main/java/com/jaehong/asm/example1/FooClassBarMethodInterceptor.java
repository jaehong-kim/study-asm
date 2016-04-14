package com.jaehong.asm.example1;

public class FooClassBarMethodInterceptor {

    public void before() {
        System.out.println("before");
    }


    public void after() {
        System.out.println("after");
    }
}