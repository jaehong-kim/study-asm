package com.jaehong.asm.example4;

public class Example4ArgsArray {
    public Example4ArgsArray() {
        Object[] array = new Object[0];
    }

    public Example4ArgsArray(String arg1) {
        Object[] array = new Object[1];
        array[0] = arg1;
    }

    public Example4ArgsArray(String arg1, int arg2) {
        Object[] array = new Object[2];
        array[0] = arg1;
        array[1] = arg2;
    }

    public Example4ArgsArray(String arg1, int arg2, boolean arg3) {
        Object[] array = new Object[3];
        array[0] = arg1;
        array[1] = arg2;
        array[2] = arg3;
    }

    public Example4ArgsArray(String arg1, int arg2, boolean arg3, long arg4) {
        Object[] array = new Object[4];
        array[0] = arg1;
        array[1] = arg2;
        array[2] = arg3;
        array[3] = arg4;
    }

    public void foo() {
        Object[] array = new Object[0];
    }

    public void foo(int arg1) {
        Object[] array = new Object[1];
        array[0] = arg1;
    }

    public void foo(long arg1) {
        Object[] array = new Object[1];
        array[0] = arg1;
    }

    public void bar() {
        Object[] array = new Object[0];
    }

    public void bar(String arg1) {
        Object[] array = new Object[1];
        array[0] = arg1;
    }

    public void bar(String arg1, int arg2) {
        Object[] array = new Object[2];
        array[0] = arg1;
        array[1] = arg2;
    }

    public void bar(String arg1, int arg2, boolean arg3) {
        Object[] array = new Object[3];
        array[0] = arg1;
        array[1] = arg2;
        array[2] = arg3;
    }

    public void bar(String arg1, int arg2, boolean arg3, long arg4) {
        Object[] array = new Object[4];
        array[0] = arg1;
        array[1] = arg2;
        array[2] = arg3;
        array[3] = arg4;
    }
}
