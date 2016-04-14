package com.jaehong.asm.example1;


import org.objectweb.asm.Label;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Type;
import org.objectweb.asm.commons.AdviceAdapter;

public class AroundMethodAdapter extends AdviceAdapter {

    private int var;
    private Label start = new Label();
    private Label end = new Label();

    protected AroundMethodAdapter(int version, MethodVisitor mv, int access, String name, String desc) {
        super(version, mv, access, name, desc);
    }

    @Override
    protected void onMethodEnter() {
        System.out.println("ENTER");
        visitLabel(start);
        var = newLocal(Type.getType("Lcom/jaehong/asm/example1/FooClassBarMethodInterceptor;"));
        System.out.println("LocalVar " + var);
        visitLocalVariable("interceptor", "Lcom/jaehong/asm/example1/FooClassBarMethodInterceptor;", null, start, end, var);
        mv.visitMethodInsn(INVOKESTATIC, "com/jaehong/asm/example1/InterceptorRegistery", "get", "()Lcom/jaehong/asm/example1/FooClassBarMethodInterceptor;", false);
        mv.visitVarInsn(ASTORE, var);
        mv.visitVarInsn(ALOAD, var);
        mv.visitMethodInsn(INVOKEVIRTUAL, "com/jaehong/asm/example1/FooClassBarMethodInterceptor", "before", "()V", false);

    }

    @Override
    protected void onMethodExit(int i) {
        System.out.println("EXIT");
        mv.visitVarInsn(ALOAD, var);
        mv.visitMethodInsn(INVOKEVIRTUAL, "com/jaehong/asm/example1/FooClassBarMethodInterceptor", "after", "()V", false);
    }

    @Override
    public void visitMaxs(int stack, int locals) {
        System.out.println("Stack " +  stack + ", Locals " + locals);
        visitLabel(end);
        mv.visitMaxs(stack, locals + 1);
    }
}
