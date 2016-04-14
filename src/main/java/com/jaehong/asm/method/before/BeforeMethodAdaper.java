package com.jaehong.asm.method.before;

import org.objectweb.asm.Label;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Type;
import org.objectweb.asm.commons.AdviceAdapter;

public class BeforeMethodAdaper extends AdviceAdapter{
    private String name;
    private int timeVar;
    private Label timeVarStart = new Label();
    private Label timeVarEnd = new Label();

    public BeforeMethodAdaper(int api, MethodVisitor methodVisitor, int acc, String name, String desc) {
        super(api, methodVisitor, acc, name, desc);
    }

    public void onMethodEnter() {
        System.out.println("ENTER");
        visitLabel(timeVarStart);
        int timeVar = newLocal(Type.getType("J"));
        visitLocalVariable("timeVar", "J", null, timeVarStart, timeVarEnd, timeVar);
        visitFieldInsn(GETSTATIC, "java/lang/System", "err", "Ljava/io/PrintStream;");
        visitLdcInsn("Entering " + name);
        visitMethodInsn(INVOKEVIRTUAL, "java/io/PrintStream", "println", "(Ljava/lang/String;)V");
    }

    public void visitMaxs(int stack, int locals) {
        visitLabel(timeVarEnd);
        visitMaxs(stack, locals);
    }
}
