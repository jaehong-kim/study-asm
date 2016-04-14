package com.jaehong.asm.example2;

public class Example2 {

    public int bar(String arg1, int arg2, boolean arg3, long arg4) {
        System.out.println("bar");
        if(false) {
            throw new RuntimeException("THROW");
        }

        return 1;
    }
}
