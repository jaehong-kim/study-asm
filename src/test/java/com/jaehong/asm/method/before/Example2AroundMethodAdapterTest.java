package com.jaehong.asm.method.before;

import com.jaehong.asm.Startup;
import com.jaehong.asm.clazz.transforming.Foo;
import com.jaehong.asm.example1.FooClassBarMethodInterceptor;
import org.junit.Test;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.MethodVisitor;

import java.lang.instrument.ClassFileTransformer;
import java.lang.instrument.IllegalClassFormatException;
import java.lang.instrument.Instrumentation;
import java.security.ProtectionDomain;

public class Example2AroundMethodAdapterTest {

    @Test
    public void visitMethod() {
        Startup.initializeIfPossible();
        Instrumentation inst = Startup.instrumentation();
        inst.addTransformer(new ClassFileTransformer() {
            public byte[] transform(ClassLoader loader, String className, Class<?> classBeingRedefined, ProtectionDomain protectionDomain, byte[] classfileBuffer) throws IllegalClassFormatException {
                ClassReader cr = new ClassReader(classfileBuffer);
                ClassWriter cw = new ClassWriter(cr, 0);

                System.out.println("transform " + loader + ", " + className);

                return cw.toByteArray();
            }
        });

        Foo foo = new Foo();
        foo.a();
    }

    static class InterceptorRegistery {
        public static FooClassBarMethodInterceptor getInterceptor() {
            return new FooClassBarMethodInterceptor();
        }
    }

    class AroundMethodAdapter extends MethodVisitor {
        public AroundMethodAdapter(int i, MethodVisitor methodVisitor) {
            super(i, methodVisitor);
        }
    }
}