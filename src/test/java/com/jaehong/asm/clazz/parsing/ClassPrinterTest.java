package com.jaehong.asm.clazz.parsing;

import org.junit.Test;
import org.objectweb.asm.ClassReader;

import java.io.IOException;

public class ClassPrinterTest{

    @Test
    public void visit() throws IOException {
        ClassLoader cl = Thread.currentThread().getContextClassLoader();
        final String className = String.class.getName();


        ClassPrinter cp = new ClassPrinter();
        ClassReader cr = new ClassReader(cl.getResourceAsStream(className.replace('.', '/') + ".class"));
        cr.accept(cp, 0);
    }
}