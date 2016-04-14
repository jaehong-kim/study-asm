package com.jaehong.asm.example7;

import org.junit.Test;
import org.objectweb.asm.util.ASMifier;

public class Example7FinalTest {
    @Test
    public void test() throws Exception {
        final String className = Example7Final.class.getName();
        ASMifier.main(new String[]{className});
    }

}