package com.jaehong.asm.example6;

import com.jaehong.asm.example5.Example5Interceptor;
import com.jaehong.asm.example5.Example5InterceptorRegistery;

public class Example6Final {

    public void foo() {
        Object[] args = new Object[0];
        Example6Interceptor interceptor = null;
        try {
            interceptor = Example6InterceptorRegistery.get(10);
            interceptor.before(this, args);
        } catch (Throwable t) {
            Example6InterceptorHelper.handleException(t);
        }

        System.out.println("foo");
        Object result = null;
        Throwable throwable = null;
        try {
            interceptor.after(this, args, result, throwable);
        } catch (Throwable t) {
            Example6InterceptorHelper.handleException(t);
        }
    }
}
