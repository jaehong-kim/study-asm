package com.jaehong.asm.example3;

public class Example3Final {
    public Example3Final(String arg1, int arg2, boolean arg3, long arg4) {
        Example3Interceptor interceptor = Example3InterceptorRegistery.get();

//        Object[] args = new Object[4];
//        args[0] = arg1;
//        args[1] = arg2;
//        args[2] = arg3;
//        args[3] = arg4;
//
//        interceptor.before(this, args);
        System.out.println("constructor");
//        interceptor.after(this, args, null, null);
    }
}
