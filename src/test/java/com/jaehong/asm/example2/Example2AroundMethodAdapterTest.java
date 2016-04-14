package com.jaehong.asm.example2;

import com.jaehong.asm.Startup;
import com.jaehong.asm.example1.FilterAdapter;
import com.jaehong.asm.example1.Foo;
import org.junit.Test;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.ClassWriter;

import java.lang.instrument.ClassFileTransformer;
import java.lang.instrument.IllegalClassFormatException;
import java.lang.instrument.Instrumentation;
import java.security.ProtectionDomain;

public class Example2AroundMethodAdapterTest {

    @Test
    public void test() throws Exception {
        Startup.initializeIfPossible();
        Instrumentation inst = Startup.instrumentation();
        inst.addTransformer(new ClassFileTransformer() {
            public byte[] transform(ClassLoader loader, String className, Class<?> classBeingRedefined, ProtectionDomain protectionDomain, byte[] classfileBuffer) throws IllegalClassFormatException {
                ClassReader cr = new ClassReader(classfileBuffer);
                ClassWriter cw = new ClassWriter(cr, ClassWriter.COMPUTE_MAXS);
                ClassVisitor cv = new Example2FilterAdapter(cw);
                cr.accept(cv, ClassReader.EXPAND_FRAMES);

                System.out.println("transform " + loader + ", " + className);

                return cw.toByteArray();
            }
        });

        Example2 example2 = new Example2();
        int result = example2.bar("string", 9, true, 88888888);
        System.out.println("RESULT " + result);
    }
}