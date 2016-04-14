package com.jaehong.asm.example6;


import org.objectweb.asm.Label;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Type;
import org.objectweb.asm.commons.AdviceAdapter;

public class Example6AroundMethodAdapter extends AdviceAdapter {

    private int interceptor;
    private int args;
    private int result;
    private int throwable;
    private Label start = new Label();
    private Label end = new Label();
    private int maxStacks = 0;
    private int addLocals = 0;
    private boolean constructor;
    private Label beforeTryStart = new Label();
    private Label beforeTryEnd = new Label();
    private Label beforeTryHandler = new Label();
    private Label beforeTryJump = new Label();
    private Label afterTryStart = new Label();
    private Label afterTryEnd = new Label();
    private Label afterTryHandler = new Label();
    private Label afterTryJump = new Label();

    protected Example6AroundMethodAdapter(int version, MethodVisitor mv, int access, String name, String desc) {
        super(version, mv, access, name, desc);
        constructor = "<init>".equals(name);
        visitLabel(start);
    }

    @Override
    public void visitCode() {
        super.visitCode();
    }

    @Override
    protected void onMethodEnter() {
        addInterceptorVar();
        addArgsVar();

        mv.visitTryCatchBlock(beforeTryStart, beforeTryEnd, beforeTryHandler, "java/lang/Throwable");
        visitLabel(beforeTryStart);
        invokeBefore();
        mv.visitLabel(beforeTryEnd);
        mv.visitJumpInsn(GOTO, beforeTryJump);

        mv.visitLabel(beforeTryHandler);
//        int beforeThrowableVar = newLocal(Type.getType("Ljava/lang/Throwable;"));
//        visitLocalVariable("beforeThrowableVar", "Ljava/lang/Throwable;", null, beforeTryHandler, beforeTryJump, beforeThrowableVar);
//        mv.visitVarInsn(ASTORE, beforeThrowableVar);
//        mv.visitVarInsn(ALOAD, beforeThrowableVar);
        mv.visitMethodInsn(INVOKESTATIC, "com/jaehong/asm/example6/Example6InterceptorHelper", "handleException", "(Ljava/lang/Throwable;)V", false);

        mv.visitLabel(beforeTryJump);
    }

    private void addInterceptorVar() {
        interceptor = newLocal(Type.getType("Lcom/jaehong/asm/example6/Example6Interceptor;"));
        visitLocalVariable("interceptor", "Lcom/jaehong/asm/example6/Example6Interceptor;", null, start, end, interceptor);

        push(10);
        mv.visitMethodInsn(INVOKESTATIC, "com/jaehong/asm/example6/Example6InterceptorRegistery", "get", "(I)Lcom/jaehong/asm/example6/Example6Interceptor;", false);
        mv.visitVarInsn(ASTORE, interceptor);

        updateMaxStacks(1);
        addLocals(2);
    }


    private void addArgsVar() {
        loadArgArray();
        args = newLocal(Type.getType("[Ljava/lang/Object;"));
        visitLocalVariable("args", "[Ljava/lang/Object;", null, start, end, args);
        mv.visitVarInsn(ASTORE, args);

        Type[] argumentTypes = Type.getArgumentTypes(methodDesc);
        for (Type type : argumentTypes) {
            if (type.getSort() == Type.OBJECT || type.getSort() == Type.ARRAY) {
                updateMaxStacks(3);
            } else if (type == Type.VOID_TYPE) {
                updateMaxStacks(4);
            } else {
                if (type.getSize() == 2) {
                    updateMaxStacks(8);
                } else {
                    updateMaxStacks(6);
                }
            }
        }
        addLocals(2);
    }

    private void invokeBefore() {
        mv.visitVarInsn(ALOAD, interceptor);
        mv.visitVarInsn(ALOAD, 0);
        mv.visitVarInsn(ALOAD, args);
        mv.visitMethodInsn(INVOKEVIRTUAL, "com/jaehong/asm/example6/Example6Interceptor", "before", "(Ljava/lang/Object;[Ljava/lang/Object;)V", false);
    }

    @Override
    protected void onMethodExit(int i) {
        int topOfStackSize = 0;
        if (i == ATHROW) {
            mv.visitInsn(DUP);
            throwable = newLocal(Type.getType("Ljava/lang/Throwable;"));
            visitLocalVariable("throwable", "Ljava/lang/Throwable;", null, start, end, throwable);
            mv.visitVarInsn(ASTORE, throwable);

            topOfStackSize = 1;
            updateMaxStacks(2);
            addLocals(2);
        } else if (!constructor) {
            int stacks = 0;
            if (i == RETURN) {
                mv.visitInsn(ACONST_NULL);
                stacks += 1;
            } else if (i == ARETURN) {
                dup();
                topOfStackSize = 1;
                stacks += 2;
            } else {
                if (i == LRETURN || i == DRETURN) {
                    topOfStackSize = 2;
                    dup2();
                    stacks += 4;
                } else {
                    topOfStackSize = 1;
                    dup();
                    stacks += 2;
                }
                final Type type = Type.getReturnType(methodDesc);
                box(type);

                if (type.getSort() == Type.OBJECT || type.getSort() == Type.ARRAY) {
                } else if (type == Type.VOID_TYPE) {
                    stacks += 1;
                } else {
                    if (type.getSize() == 2) {
                        stacks += 3;
                    } else {
                        stacks += 1;
                    }
                }
            }

            result = newLocal(Type.getType("Ljava/lang/Object;"));
            visitLocalVariable("result", "Ljava/lang/Object;", null, start, end, result);
            mv.visitVarInsn(ASTORE, result);
            updateMaxStacks(stacks);
            addLocals(2);
        }

        mv.visitVarInsn(ALOAD, interceptor);
        mv.visitVarInsn(ALOAD, 0);
        mv.visitVarInsn(ALOAD, args);
        if (i == ATHROW) {
            mv.visitInsn(ACONST_NULL);
            mv.visitVarInsn(ALOAD, throwable);
        } else if (constructor) {
            mv.visitInsn(ACONST_NULL);
            mv.visitInsn(ACONST_NULL);
        } else {
            mv.visitVarInsn(ALOAD, result);
            mv.visitInsn(ACONST_NULL);
        }

        mv.visitTryCatchBlock(afterTryStart, afterTryEnd, afterTryHandler, "java/lang/Throwable");
        visitLabel(afterTryStart);

        mv.visitMethodInsn(INVOKEVIRTUAL, "com/jaehong/asm/example6/Example6Interceptor", "after", "(Ljava/lang/Object;[Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/Throwable;)V", false);
        updateMaxStacks(topOfStackSize + 5);
        mv.visitLabel(afterTryEnd);
        mv.visitJumpInsn(GOTO, afterTryJump);

        mv.visitLabel(afterTryHandler);
        mv.visitMethodInsn(INVOKESTATIC, "com/jaehong/asm/example6/Example6InterceptorHelper", "handleException", "(Ljava/lang/Throwable;)V", false);

        mv.visitLabel(afterTryJump);
    }

    @Override
    public void visitMaxs(int stack, int locals) {
        visitLabel(end);

        int maxStack = Math.max(stack, maxStacks);
        int maxLocals = locals + addLocals;
        System.out.println("Stack " + stack + ", maxStack " + maxStack + ", Locals " + locals + ", maxLocals " + maxLocals);
        mv.visitMaxs(maxStacks, maxLocals);
    }

    private void updateMaxStacks(int stacks) {
        if (this.maxStacks < stacks) {
            this.maxStacks = stacks;
        }
    }

    private void addLocals(int locals) {
        this.addLocals += locals;
    }
}
