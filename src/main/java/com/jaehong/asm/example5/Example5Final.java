package com.jaehong.asm.example5;

public class Example5Final {
    public void foo() {
        Example5Interceptor interceptor = Example5InterceptorRegistery.get(10);
        Object[] args = new Object[0];
        interceptor.before(this, args);
        System.out.println("foo");
        Object result = null;
        Throwable throwable = null;
        interceptor.after(this, args, result, throwable);
    }
}
