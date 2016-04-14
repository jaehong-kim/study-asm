package com.jaehong.asm.example2;

public class Exampl2Final {
    public int bar(String arg1, int arg2, boolean arg3, long arg4) {
        Example2Interceptor interceptor = Example2InterceptorRegistery.get();
        Object[] args = new Object[2];
        args[0] = arg1;
        args[1] = arg2;
        interceptor.before(this, args);
        System.out.println("bar");
        interceptor.after(this, args, null, null);
        return 1;
    }
}