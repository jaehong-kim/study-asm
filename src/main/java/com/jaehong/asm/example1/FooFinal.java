package com.jaehong.asm.example1;

public class FooFinal {

    public void bar() {
        FooClassBarMethodInterceptor interceptor = InterceptorRegistery.get();
        interceptor.before();

        System.out.println("foo.bar");

        interceptor.after();
    }
}
