package com.jaehong.asm.example1;

import com.jaehong.asm.Startup;
import org.junit.Test;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.ClassWriter;

import java.lang.instrument.ClassFileTransformer;
import java.lang.instrument.IllegalClassFormatException;
import java.lang.instrument.Instrumentation;
import java.security.ProtectionDomain;

public class Example1AroundMethodAdapterTest {

    @Test
    public void test() throws Exception {
        Startup.initializeIfPossible();
        Instrumentation inst = Startup.instrumentation();
        inst.addTransformer(new ClassFileTransformer() {
            public byte[] transform(ClassLoader loader, String className, Class<?> classBeingRedefined, ProtectionDomain protectionDomain, byte[] classfileBuffer) throws IllegalClassFormatException {
                ClassReader cr = new ClassReader(classfileBuffer);
                ClassWriter cw = new ClassWriter(cr, 0);
                ClassVisitor cv = new FilterAdapter(cw);
                cr.accept(cv, ClassReader.EXPAND_FRAMES);

                System.out.println("transform " + loader + ", " + className);

                return cw.toByteArray();
            }
        });

        Foo foo = new Foo();
        foo.bar();
    }


}