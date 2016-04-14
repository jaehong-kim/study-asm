package com.jaehong.asm.example3;

import com.jaehong.asm.Startup;
import com.jaehong.asm.example2.Example2FilterAdapter;
import org.junit.Test;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.util.TraceClassVisitor;

import java.io.OutputStream;
import java.io.PrintWriter;
import java.lang.instrument.ClassFileTransformer;
import java.lang.instrument.IllegalClassFormatException;
import java.lang.instrument.Instrumentation;
import java.security.ProtectionDomain;

import static java.lang.System.out;

public class Example3AroundMethodAdapterTest {

    @Test
    public void test() throws Exception {
        Startup.initializeIfPossible();
        Instrumentation inst = Startup.instrumentation();
        inst.addTransformer(new ClassFileTransformer() {
            public byte[] transform(ClassLoader loader, String className, Class<?> classBeingRedefined, ProtectionDomain protectionDomain, byte[] classfileBuffer) throws IllegalClassFormatException {
                if(className.equals("com/jaehong/asm/example3/Example3")) {
                    ClassReader cr = new ClassReader(classfileBuffer);
                    //ClassWriter cw = new ClassWriter(cr, ClassWriter.COMPUTE_MAXS);
                    ClassWriter cw = new ClassWriter(cr, 0);
                    TraceClassVisitor tcv = new TraceClassVisitor(cw, new PrintWriter(System.out));
                    ClassVisitor cv = new Example3FilterAdapter(tcv);
                    cr.accept(cv, 0);

                    return cw.toByteArray();
                }

                return classfileBuffer;
            }
        });

        new Example3();
        new Example3("string");
        new Example3("string", 9);
        new Example3("string", 9, true);
        new Example3("string", 9, true, 888888);
        new Example3("string", 9, true, 888888, 0);
        new Example3("string", 9, true, 888888, 0, Byte.valueOf("1"));
        new Example3("string", 9, true, 888888, 0, Byte.valueOf("1"), new Object[] {});

        new Example3().foo();


    }
}