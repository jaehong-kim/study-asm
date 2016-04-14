package com.jaehong.asm.example3;

import org.junit.Test;
import org.objectweb.asm.util.ASMifier;


import static org.junit.Assert.*;

public class Example3FinalTest {

    @Test
    public void test() throws Exception {
        final String className = Example3Final.class.getName();
        ASMifier.main(new String[]{className});
    }
}