package com.jaehong.asm.example4;

import org.junit.Test;
import org.objectweb.asm.util.ASMifier;

import static org.junit.Assert.*;

public class Example4ArgsArrayTest {

    @Test
    public void test() throws Exception {
        final String className = Example4ArgsArray.class.getName();
        ASMifier.main(new String[]{className});
    }

}