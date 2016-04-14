package com.jaehong.asm.example4;

import org.junit.Test;
import org.objectweb.asm.util.ASMifier;

import static org.junit.Assert.*;

public class Example4InvokeStaticTest {

    @Test
    public void test() throws Exception {
        final String className = Example4InvokeStatic.class.getName();
        ASMifier.main(new String[]{className});
    }

}