package com.jaehong.asm.example6;

import com.jaehong.asm.Startup;
import com.jaehong.asm.example5.Example5;
import com.jaehong.asm.example5.Example5FilterAdapter;
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

import static org.junit.Assert.*;

public class Example6AroundMethodAdapterTest {
    @Test
    public void test() throws Exception {
        Startup.initializeIfPossible();
        Instrumentation inst = Startup.instrumentation();
        inst.addTransformer(new ClassFileTransformer() {
            public byte[] transform(ClassLoader loader, String className, Class<?> classBeingRedefined, ProtectionDomain protectionDomain, byte[] classfileBuffer) throws IllegalClassFormatException {
                if (className.equals("com/jaehong/asm/example6/Example6")) {
                    ClassReader cr = new ClassReader(classfileBuffer);
                    ClassWriter cw = new ClassWriter(cr, 0);
                    TraceClassVisitor tcv = new TraceClassVisitor(cw, new PrintWriter(System.out));
                    ClassVisitor cv = new Example6FilterAdapter(tcv);
                    cr.accept(cv, 0);

                    return cw.toByteArray();
                }

                return classfileBuffer;
            }
        });
        Example6 example = new Example6();
        example.foo();
    }
}