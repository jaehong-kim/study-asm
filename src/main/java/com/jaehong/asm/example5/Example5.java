package com.jaehong.asm.example5;

public class Example5 {

    public void foo() {
        System.out.println("foo");
    }

    public void foo(String arg1) {
        System.out.println("foo");
    }

    public void foo(String arg1, int arg2) {
        System.out.println("foo");
    }

    public void foo(String arg1, int arg2, boolean arg3) {
        System.out.println("foo");
    }

    public void foo(String arg1, int arg2, boolean arg3, long arg4) {
        System.out.println("foo");
    }

    public void foo(String arg1, int arg2, boolean arg3, long arg4, double arg5) {
        System.out.println("foo");
    }

    public void foo(String arg1, int arg2, boolean arg3, long arg4, double arg5, byte arg6) {
        System.out.println("foo");
    }

    public void foo(String arg1, int arg2, boolean arg3, long arg4, double arg5, byte arg6, Object[] arg7) {
        System.out.println("foo");
    }

    public Object bar() {
        System.out.println("foo");
        return null;
    }

    public String bar(String arg1) {
        System.out.println("foo");
        return arg1;
    }

    public int bar(String arg1, int arg2) {
        System.out.println("foo");
        return arg2;
    }

    public boolean bar(String arg1, int arg2, boolean arg3) {
        System.out.println("foo");
        return arg3;
    }

    public long bar(String arg1, int arg2, boolean arg3, long arg4) {
        System.out.println("foo");
        return arg4;
    }

    public double bar(String arg1, int arg2, boolean arg3, long arg4, double arg5) {
        System.out.println("foo");
        return arg5;
    }

    public byte bar(String arg1, int arg2, boolean arg3, long arg4, double arg5, byte arg6) {
        System.out.println("foo");
        return arg6;
    }

    public void bar(String arg1, int arg2, boolean arg3, long arg4, double arg5, byte arg6, Object[] arg7) {
        System.out.println("foo");
    }

    public static void staticFoo() {
        System.out.println("foo");
    }
}
