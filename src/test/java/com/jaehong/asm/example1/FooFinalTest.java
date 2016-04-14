package com.jaehong.asm.example1;

import com.jaehong.asm.clazz.parsing.ClassPrinter;
import jdk.internal.org.objectweb.asm.util.ASMifier;
import org.junit.Test;
import org.objectweb.asm.ClassReader;

import java.io.IOException;

public class FooFinalTest {

    @Test
    public void test() throws Exception {
        final String className = FooFinal.class.getName();
        ASMifier.main(new String[] {className});
    }
}