package com.jaehong.asm.example1;

import jdk.internal.org.objectweb.asm.util.ASMifier;
import org.junit.Test;

public class FooTest {
    @Test
    public void test() throws Exception {
        final String className = Foo.class.getName();
        ASMifier.main(new String[] {className});
    }
}