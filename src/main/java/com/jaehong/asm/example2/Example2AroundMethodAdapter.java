package com.jaehong.asm.example2;


import org.objectweb.asm.Label;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Type;
import org.objectweb.asm.commons.AdviceAdapter;

public class Example2AroundMethodAdapter extends AdviceAdapter {

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

    private String desc;

    protected Example2AroundMethodAdapter(int version, MethodVisitor mv, int access, String name, String desc) {
        super(version, mv, access, name, desc);
    }

    @Override
    protected void onMethodEnter() {
        System.out.println("ENTER");

        visitLabel(start);
        var = newLocal(Type.getType("Lcom/jaehong/asm/example2/Example2Interceptor;"));
        System.out.println("var " + var);
        visitLocalVariable("interceptor", "Lcom/jaehong/asm/example2/Example2Interceptor;", null, start, end, var);
        mv.visitMethodInsn(INVOKESTATIC, "com/jaehong/asm/example2/Example2InterceptorRegistery", "get", "()Lcom/jaehong/asm/example2/Example2Interceptor;", false);
        mv.visitVarInsn(ASTORE, var);

        // arguments array.
//        Type[] argTypes = Type.getArgumentTypes(methodDesc);
//        mv.visitIntInsn(BIPUSH, argTypes.length);
//        mv.visitTypeInsn(ANEWARRAY, "java/lang/Object");
//        args = newLocal(Type.getType("[Ljava/lang/Object;"));
//        System.out.println("args " + args);
//        visitLocalVariable("args", "[Ljava/lang/Object;", null, start, end, args);
//        mv.visitVarInsn(ASTORE, args);
//
//        int arrayIndex = 0;
//        int parameterIndex = 1;
//        for(Type parameterType : argTypes) {
//            mv.visitVarInsn(ALOAD, args);
//            mv.visitInsn(DUP);
//            mv.visitIntInsn(BIPUSH, arrayIndex++);
//            mv.visitVarInsn(parameterType.getOpcode(ILOAD), parameterIndex);
//            generateCastToObject(mv, parameterType);
//            mv.visitInsn(AASTORE);
//            parameterIndex += parameterType.getSize();
//        }
//        System.out.println(arrayIndex + ", " + parameterIndex);

        loadArgArray();
        args = newLocal(Type.getType("[Ljava/lang/Object;"));
        System.out.println("args " + args);
        visitLocalVariable("args", "[Ljava/lang/Object;", null, start, end, args);
        mv.visitVarInsn(ASTORE, args);

        mv.visitVarInsn(ALOAD, var);
        mv.visitVarInsn(ALOAD, 0);
        mv.visitVarInsn(ALOAD, args);

        mv.visitMethodInsn(INVOKEVIRTUAL, "com/jaehong/asm/example2/Example2Interceptor", "before", "(Ljava/lang/Object;[Ljava/lang/Object;)V", false);
    }

    @Override
    protected void onMethodExit(int i) {
        System.out.println("EXIT");
        if(i == ATHROW) {
            mv.visitInsn(DUP);
            throwable = newLocal(Type.getType("Ljava/lang/Throwable;"));
            System.out.println("throwable " + throwable);
            visitLocalVariable("throwable", "Ljava/lang/Throwable;", null, start, end, throwable);
            mv.visitVarInsn(ASTORE, throwable);
        } else {
            if(i==RETURN) {
            } else if(i==ARETURN) {
                dup();
            } else {
                if(i==LRETURN || i == DRETURN) {
                    dup2();
                } else {
                    dup();
                }
                box(Type.getReturnType(methodDesc));
            }

//            mv.visitInsn(DUP);
            result = newLocal(Type.getType("Ljava/lang/Object;"));
            System.out.println("result " + result);
            visitLocalVariable("result", "Ljava/lang/Object;", null, start, end, result);
            mv.visitVarInsn(ASTORE, result);
        }

        mv.visitVarInsn(ALOAD, var);
        mv.visitVarInsn(ALOAD, 0);
        mv.visitVarInsn(ALOAD, args);
        if(i == ATHROW) {
            mv.visitInsn(ACONST_NULL);
            mv.visitVarInsn(ALOAD, throwable);
        } else {
            mv.visitVarInsn(ALOAD, result);
            mv.visitInsn(ACONST_NULL);
        }

//        if(i == RETURN) {
//            mv.visitInsn(ACONST_NULL);
//        } else {
//            mv.visitIntInsn(SIPUSH, i);
//        }

        // return value.
//        Type returnType = Type.getReturnType(methodDesc);
//        generateCastToObject(mv, returnType);
//        generateCastFromObject(mv, returnType);
//        mv.visitVarInsn(ALOAD, returnType.getOpcode(IRETURN));

//        mv.visitInsn(DUP);
//        mv.visitInsn(ACONST_NULL);
//        mv.visitInsn(ACONST_NULL);

        mv.visitMethodInsn(INVOKEVIRTUAL, "com/jaehong/asm/example2/Example2Interceptor", "after", "(Ljava/lang/Object;[Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/Throwable;)V", false);
    }

    @Override
    public void visitMaxs(int stack, int locals) {
        System.out.println("Stack " +  stack + ", Locals " + locals);
        visitLabel(end);
        mv.visitMaxs(stack, locals);
    }

    public static void generateCastToObject(MethodVisitor mv, Type type)
    {
        int sort = type.getSort();

        if (sort < Type.ARRAY) {
            String wrapperType = PRIMITIVE_WRAPPER_TYPE[sort];
            String desc = '(' + type.getDescriptor() + ")L" + wrapperType + ';';
            mv.visitMethodInsn(INVOKESTATIC, wrapperType, "valueOf", desc, false);
        }
    }

    public static void generateCastFromObject(MethodVisitor mv, Type toType)
    {
        int sort = toType.getSort();

        if (sort == Type.VOID) {
            mv.visitInsn(POP);
        }
        else {
            generateTypeCheck(mv, toType);
            if (sort < Type.ARRAY) {
                mv.visitMethodInsn(
                        INVOKEVIRTUAL, PRIMITIVE_WRAPPER_TYPE[sort], UNBOXING_NAME[sort], UNBOXING_DESC[sort], false);
            }
        }
    }

    private static void generateTypeCheck(MethodVisitor mv, Type toType)
    {
        int sort = toType.getSort();
        String typeDesc;

        switch (sort) {
            case Type.ARRAY: typeDesc = toType.getDescriptor(); break;
            case Type.OBJECT: typeDesc = toType.getInternalName(); break;
            default: typeDesc = PRIMITIVE_WRAPPER_TYPE[sort];
        }

        mv.visitTypeInsn(CHECKCAST, typeDesc);
    }

}
