package com.jaehong.asm.example8;

public class Example8 {

    public Example8() {
        System.out.println("Example8");
    }

    public void foo() {
        System.out.println("foo");
    }

    public void foo(String arg1, int arg2, boolean arg3, long arg4) {
        System.out.println("foo");
    }

//    public void foo(String arg1, int arg2, boolean arg3, long arg4, byte[] data) {
//        System.out.println("foo");
//        throw new RuntimeException("try catch ?");
//    }

    public long bar() {
        System.out.println("bar");
        return 1;
    }
}
