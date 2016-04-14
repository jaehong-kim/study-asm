package com.jaehong.asm.clazz.generation;

import org.junit.Test;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.Opcodes;

public class ComparableTest {

    @Test
    public void directly() {
        System.out.println(Comparable.class);

        MyClassLoader cl = new MyClassLoader();
        Class c = cl.defineClass("com.jaehong.asm.generation.Comparable", generate());
        System.out.println(c);
    }

    @Test
    public void stub() throws ClassNotFoundException {
        System.out.println(Comparable.class);

        StubClassLoader cl = new StubClassLoader();
        Class c = cl.findClass("com.jaehong.asm.generation.Comparable_Stub");
        System.out.println(c);
    }

    public byte[] generate() {
        ClassWriter cw = new ClassWriter(0);

        cw.visit(Opcodes.V1_5, Opcodes.ACC_PUBLIC + Opcodes.ACC_ABSTRACT + Opcodes.ACC_INTERFACE, "com/jaehong/asm/clazz/generation/Comparable", null, "java/lang/Object", new String[]{"com/jaehong/asm/clazz/generation/Mesurable"});
        cw.visitField(Opcodes.ACC_PUBLIC + Opcodes.ACC_FINAL + Opcodes.ACC_STATIC, "LESS", "I", null, new Integer(-1)).visitEnd();
        cw.visitField(Opcodes.ACC_PUBLIC + Opcodes.ACC_FINAL + Opcodes.ACC_STATIC, "EQUAL", "I", null, new Integer(1)).visitEnd();
        cw.visitField(Opcodes.ACC_PUBLIC + Opcodes.ACC_FINAL + Opcodes.ACC_STATIC, "GREATER", "I", null, new Integer(1)).visitEnd();
        cw.visitMethod(Opcodes.ACC_PUBLIC + Opcodes.ACC_ABSTRACT, "compareTo", "(Ljava/lang/Object;)I", null, null).visitEnd();
        cw.visitEnd();
        byte[] b = cw.toByteArray();

        return b;
    }

    class MyClassLoader extends ClassLoader {
        public Class defineClass(String name, byte[] b) {
            return defineClass(name, b, 0, b.length);
        }
    }

    class StubClassLoader extends ClassLoader {
        protected Class findClass(String name) throws ClassNotFoundException {
            if (name.endsWith("_Stub")) {
                byte[] b = generate();
                return defineClass(name, b, 0, b.length);
            }

            return super.findClass(name);
        }
    }
}