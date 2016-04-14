package com.jaehong.asm.clazz.transforming;

import com.jaehong.asm.Startup;
import com.jaehong.asm.clazz.transforming.Foo;
import com.jaehong.asm.clazz.transforming.RemoveMethodAdapter;
import org.junit.Test;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.ClassWriter;

import java.lang.instrument.ClassFileTransformer;
import java.lang.instrument.IllegalClassFormatException;
import java.lang.instrument.Instrumentation;
import java.security.ProtectionDomain;

public class RemoveMethodAdapterTest {

    @Test
    public void visitMethod() {
        Startup.initializeIfPossible();
        Instrumentation inst = Startup.instrumentation();
        inst.addTransformer(new ClassFileTransformer() {
            public byte[] transform(ClassLoader loader, String className, Class<?> classBeingRedefined, ProtectionDomain protectionDomain, byte[] classfileBuffer) throws IllegalClassFormatException {
                ClassReader cr = new ClassReader(classfileBuffer);
                ClassWriter cw = new ClassWriter(cr, 0);
                ClassVisitor cv = new RemoveMethodAdapter(cw, "a", "()V");
                cr.accept(cv, 0);

                System.out.println("transform " + loader + ", " + className);

                return cw.toByteArray();
            }
        });

        Foo foo = new Foo();
        try {
            foo.a();
        } catch(NoSuchMethodError e) {
        }
    }

}