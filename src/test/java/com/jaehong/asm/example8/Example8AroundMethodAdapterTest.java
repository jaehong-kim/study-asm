package com.jaehong.asm.example8;

import com.jaehong.asm.Startup;
import org.junit.Test;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.*;

import java.lang.instrument.ClassFileTransformer;
import java.lang.instrument.IllegalClassFormatException;
import java.lang.instrument.Instrumentation;
import java.security.ProtectionDomain;
import java.util.List;

import static org.junit.Assert.*;

public class Example8AroundMethodAdapterTest {


    @Test
    public void test() throws Exception {
        Startup.initializeIfPossible();
        Instrumentation inst = Startup.instrumentation();
        inst.addTransformer(new ClassFileTransformer() {
            public byte[] transform(ClassLoader loader, String className, Class<?> classBeingRedefined, ProtectionDomain protectionDomain, byte[] classfileBuffer) throws IllegalClassFormatException {
                if (className.equals("com/jaehong/asm/example8/Example8")) {
                    ClassReader cr = new ClassReader(classfileBuffer);
                    ClassNode cn = new ClassNode();
                    cr.accept(cn, 0);
                    List<MethodNode> methods = cn.methods;
                    System.out.println(methods);
                    for (MethodNode method : methods) {
                        if (method.name.equals("foo")) {
                            System.out.println("name=" + method.name);

                            Example8Method transform = new Example8Method(method);
                            transform.addAroundMethod();
                        }
                    }

                    ClassWriter cw = new ClassWriter(cr, ClassWriter.COMPUTE_MAXS);
                    cn.accept(cw);

                    return cw.toByteArray();
                }

                return classfileBuffer;
            }
        });

        Example8 example = new Example8();
        example.foo();
        example.foo("arg1", 1, false, 9999);
    }
}