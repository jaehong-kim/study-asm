package com.jaehong.asm.example8;

public class Example8Final {

    public Example8Final() {
        System.out.println("Example8");
    }

    public void foo() {
        Example8Interceptor interceptor = null;
        interceptor = Example8InterceptorRegistery.get(10);
        System.out.println("foo");
    }

    public void foo(String arg1, int arg2, boolean arg3, long arg4) {
        System.out.println("foo");
    }

    public long bar() {
        System.out.println("bar");
        return 1;
    }
}
