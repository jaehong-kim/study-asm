package com.jaehong.asm.example7;

import com.jaehong.asm.Startup;
import org.junit.Test;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.util.TraceClassVisitor;

import java.io.PrintWriter;
import java.lang.instrument.ClassFileTransformer;
import java.lang.instrument.IllegalClassFormatException;
import java.lang.instrument.Instrumentation;
import java.security.ProtectionDomain;

public class Example7AddAccessorAdapterTest {
    @Test
    public void test() throws Exception {
        Startup.initializeIfPossible();
        Instrumentation inst = Startup.instrumentation();
        inst.addTransformer(new ClassFileTransformer() {
            public byte[] transform(ClassLoader loader, String className, Class<?> classBeingRedefined, ProtectionDomain protectionDomain, byte[] classfileBuffer) throws IllegalClassFormatException {
                if (className.equals("com/jaehong/asm/example7/Example7")) {
                    ClassReader cr = new ClassReader(classfileBuffer);
                    ClassWriter cw = new ClassWriter(cr, 0);
                    TraceClassVisitor tcv = new TraceClassVisitor(cw, new PrintWriter(System.out));
                    ClassVisitor cv = new Example7AddAccessorAdapter(tcv);
                    cr.accept(cv, 0);

                    return cw.toByteArray();
                }

                return classfileBuffer;
            }
        });
        Example7 example = new Example7();
        example.foo();

        if(example instanceof Example7Accessor) {
            Example7Accessor accessor  = (Example7Accessor) example;
            accessor.setVar("bar");
            System.out.println("Result " + accessor.getVar());
        }
    }
}