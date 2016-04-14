package com.jaehong.asm.example3;


import org.objectweb.asm.Label;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Type;
import org.objectweb.asm.commons.AdviceAdapter;
import org.objectweb.asm.commons.Method;

public class Example3AroundMethodAdapter extends AdviceAdapter {

    private static final String[] PRIMITIVE_WRAPPER_TYPE = {
            null, "java/lang/Boolean", "java/lang/Character", "java/lang/Byte", "java/lang/Short", "java/lang/Integer",
            "java/lang/Float", "java/lang/Long", "java/lang/Double"
    };

    private static final String[] UNBOXING_NAME = {
            null, "booleanValue", "charValue", "byteValue", "shortValue", "intValue", "floatValue", "longValue", "doubleValue"
    };
    private static final String[] UNBOXING_DESC = {null, "()Z", "()C", "()B", "()S", "()I", "()F", "()J", "()D"};


    private int var;
    private int args;
    private int result;
    private int throwable;
    private Label start = new Label();
    private Label end = new Label();
    private int argsLength;
    private int maxStacks = 5;
    private int addLocals = 4;
    private boolean constructor;


    protected Example3AroundMethodAdapter(int version, MethodVisitor mv, int access, String name, String desc) {
        super(version, mv, access, name, desc);
        constructor = "<init>".equals(name);
    }

    @Override
    public void visitCode() {
        super.visitCode();
        visitLabel(start);
    }

    @Override
    protected void onMethodEnter() {
        System.out.println("ENTER");

        updateMaxStacks();

        var = newLocal(Type.getType("Lcom/jaehong/asm/example3/Example3Interceptor;"));
        visitLocalVariable("interceptor", "Lcom/jaehong/asm/example3/Example3Interceptor;", null, start, end, var);

        mv.visitMethodInsn(INVOKESTATIC, "com/jaehong/asm/example3/Example3InterceptorRegistery", "get", "()Lcom/jaehong/asm/example3/Example3Interceptor;", false);
        mv.visitVarInsn(ASTORE, var);

        loadArgArray();
        args = newLocal(Type.getType("[Ljava/lang/Object;"));
        visitLocalVariable("args", "[Ljava/lang/Object;", null, start, end, args);
        mv.visitVarInsn(ASTORE, args);

        mv.visitVarInsn(ALOAD, var);
        mv.visitVarInsn(ALOAD, 0);
        mv.visitVarInsn(ALOAD, args);

        mv.visitMethodInsn(INVOKEVIRTUAL, "com/jaehong/asm/example3/Example3Interceptor", "before", "(Ljava/lang/Object;[Ljava/lang/Object;)V", false);
    }

    @Override
    protected void onMethodExit(int i) {
        System.out.println("EXIT");
        if (i == ATHROW) {
            mv.visitInsn(DUP);
            throwable = newLocal(Type.getType("Ljava/lang/Throwable;"));
            System.out.println("throwable " + throwable);
            visitLocalVariable("throwable", "Ljava/lang/Throwable;", null, start, end, throwable);
            mv.visitVarInsn(ASTORE, throwable);
            addLocals += 2;
        } else if (!constructor) {
            if (i == RETURN) {
            } else if (i == ARETURN) {
                dup();
            } else {
                if (i == LRETURN || i == DRETURN) {
                    dup2();
                } else {
                    dup();
                }
                box(Type.getReturnType(methodDesc));
            }

            result = newLocal(Type.getType("Ljava/lang/Object;"));
            System.out.println("result " + result);
            visitLocalVariable("result", "Ljava/lang/Object;", null, start, end, result);
            mv.visitVarInsn(ASTORE, result);
            addLocals += 2;
        }

        mv.visitVarInsn(ALOAD, var);
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

        mv.visitMethodInsn(INVOKEVIRTUAL, "com/jaehong/asm/example3/Example3Interceptor", "after", "(Ljava/lang/Object;[Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/Throwable;)V", false);
    }

    @Override
    public void visitMaxs(int stack, int locals) {
        visitLabel(end);


        int maxStack = Math.max(stack, maxStacks);
        int maxLocals = locals + addLocals;
        System.out.println("Stack " + stack + ", maxStack " + maxStack + ", Locals " + locals + ", maxLocals " + maxLocals);
        mv.visitMaxs(maxStack, maxLocals);
    }


    private void updateMaxStacks() {
        Type[] argumentTypes = Type.getArgumentTypes(methodDesc);
        for (Type type : argumentTypes) {
            if (type.getSort() == Type.OBJECT || type.getSort() == Type.ARRAY) {
                if (maxStacks < 3) {
                    maxStacks = 3;
                }
            } else if (type == Type.VOID_TYPE) {
                if (maxStacks < 4) {
                    maxStacks = 4;
                }
            } else {
                if (type.getSize() == 2) {
                    if (maxStacks < 8) {
                        maxStacks = 8;
                    }
                } else {
                    if (maxStacks < 6) {
                        maxStacks = 6;
                    }
                }
            }
        }
    }
}
