package com.jaehong.asm.example4;

import org.junit.Test;
import org.objectweb.asm.util.ASMifier;

public class Example4Test {

    @Test
    public void test() throws Exception {
        final String className = Example4.class.getName();
        ASMifier.main(new String[]{className});
    }
}