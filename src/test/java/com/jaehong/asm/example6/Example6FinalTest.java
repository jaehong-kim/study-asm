package com.jaehong.asm.example6;

import org.junit.Test;
import org.objectweb.asm.util.ASMifier;


public class Example6FinalTest {

    @Test
    public void test() throws Exception {
        final String className = Example6Final.class.getName();
        ASMifier.main(new String[]{className});
    }

}