package com.jaehong.asm.example7;

import org.objectweb.asm.*;

import java.util.Arrays;

public class Example7AddAccessorAdapter extends ClassVisitor {

    public Example7AddAccessorAdapter(final ClassVisitor cv) {
        super(Opcodes.ASM5, cv);
    }

    @Override
    public void visit(int version, int access, String name, String signature, String superName, String[] interfaces) {
        if (cv != null) {
            String[] addedInterfaces = new String[interfaces.length + 1];
            System.arraycopy(interfaces, 0, addedInterfaces, 0, interfaces.length);
            addedInterfaces[interfaces.length] = "com/jaehong/asm/example7/Example7Accessor";
            System.out.println("Interfaces " + Arrays.asList(interfaces) + ", " + Arrays.asList(addedInterfaces));
            cv.visit(version, access, name, signature, superName, addedInterfaces);
        }
    }

    @Override
    public void visitEnd() {
        FieldVisitor fv = cv.visitField(Opcodes.ACC_PRIVATE, "var", "Ljava/lang/String;", null, null);

        // name, desc
        MethodVisitor gmv = cv.visitMethod(Opcodes.ACC_PUBLIC, "setVar", "(Ljava/lang/String;)V", null, null);
        gmv.visitCode();
        gmv.visitVarInsn(Opcodes.ALOAD, 0);
        gmv.visitVarInsn(Opcodes.ALOAD, 1);
        gmv.visitFieldInsn(Opcodes.PUTFIELD, "com/jaehong/asm/example7/Example7", "var", "Ljava/lang/String;");
        gmv.visitInsn(Opcodes.RETURN);
        gmv.visitMaxs(2, 2);
        gmv.visitEnd();

        MethodVisitor smv = cv.visitMethod(Opcodes.ACC_PUBLIC, "getVar", "()Ljava/lang/String;", null, null);
        smv.visitCode();
        smv.visitVarInsn(Opcodes.ALOAD, 0);
        smv.visitFieldInsn(Opcodes.GETFIELD, "com/jaehong/asm/example7/Example7", "var", "Ljava/lang/String;");
        smv.visitInsn(Opcodes.ARETURN);
        smv.visitMaxs(1, 1);
        smv.visitEnd();

        cv.visitEnd();
    }
}