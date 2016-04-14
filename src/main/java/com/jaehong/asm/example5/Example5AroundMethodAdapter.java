package com.jaehong.asm.example5;


import org.objectweb.asm.Label;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Type;
import org.objectweb.asm.commons.AdviceAdapter;

public class Example5AroundMethodAdapter extends AdviceAdapter {

    private int interceptor;
    private int args;
    private int result;
    private int throwable;
    private Label start = new Label();
    private Label end = new Label();
    private int maxStacks = 0;
    private int addLocals = 0;
    private boolean constructor;

    protected Example5AroundMethodAdapter(int version, MethodVisitor mv, int access, String name, String desc) {
        super(version, mv, access, name, desc);
        constructor = "<init>".equals(name);
    }

    @Override
    public void visitCode() {
        super.visitCode();
    }

    @Override
    protected void onMethodEnter() {
        System.out.println("ENTER");
        visitLabel(start);

        addInterceptorVar();
        addArgsVar();
        invokeBefore();
    }

    private void addInterceptorVar() {
        push(10);
        mv.visitMethodInsn(INVOKESTATIC, "com/jaehong/asm/example5/Example5InterceptorRegistery", "get", "(I)Lcom/jaehong/asm/example5/Example5Interceptor;", false);
        interceptor = newLocal(Type.getType("Lcom/jaehong/asm/example5/Example5Interceptor;"));
        visitLocalVariable("interceptor", "Lcom/jaehong/asm/example5/Example5Interceptor;", null, start, end, interceptor);
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
        mv.visitMethodInsn(INVOKEVIRTUAL, "com/jaehong/asm/example5/Example5Interceptor", "before", "(Ljava/lang/Object;[Ljava/lang/Object;)V", false);
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
                } else if(type == Type.VOID_TYPE) {
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

        mv.visitMethodInsn(INVOKEVIRTUAL, "com/jaehong/asm/example5/Example5Interceptor", "after", "(Ljava/lang/Object;[Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/Throwable;)V", false);
        updateMaxStacks(topOfStackSize + 5);
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
        if(this.maxStacks < stacks) {
            this.maxStacks = stacks;
        }
    }

    private void addLocals(int locals) {
        this.addLocals += locals;
    }
}
