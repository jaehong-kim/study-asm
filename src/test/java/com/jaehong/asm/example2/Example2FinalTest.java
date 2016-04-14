package com.jaehong.asm.example2;

import jdk.internal.org.objectweb.asm.util.ASMifier;
import org.junit.Test;

public class Example2FinalTest {

    @Test
    public void test() throws Exception {
        final String className = Exampl2Final.class.getName();
        ASMifier.main(new String[]{className});
    }

}