package com.jaehong.asm.clazz.transforming;

import org.objectweb.asm.Label;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.Type;
import org.objectweb.asm.commons.AdviceAdapter;

public class EnteringAdapter extends AdviceAdapter {

    private String name;
    private int timeVar;
    private Label timeVarStart = new Label();
    private Label timeVarEnd = new Label();

    public EnteringAdapter(MethodVisitor mv, int acc, String name, String desc) {
        super(Opcodes.ASM5, mv, acc, name, desc);
        this.name = name;
    }

    protected void onMethodEnter() {
        visitLabel(timeVarStart);
        int timeVar = newLocal(Type.getType("J"));
        visitLocalVariable("timeVar", "J", null, timeVarStart, timeVarEnd, timeVar);

        super.visitFieldInsn(GETSTATIC, "java/lang/System", "err", "Ljava/io/PrintStream;");
        super.visitLdcInsn("Entering " + name);
        super.visitMethodInsn(INVOKEVIRTUAL, "java/io/PrintStream", "println", "(Ljava/lang/String;)v");
    }

}
