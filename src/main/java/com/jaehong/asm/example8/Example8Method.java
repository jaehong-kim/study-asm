package com.jaehong.asm.example8;

import org.objectweb.asm.Label;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.Type;
import org.objectweb.asm.commons.Method;
import org.objectweb.asm.commons.TryCatchBlockSorter;
import org.objectweb.asm.tree.*;

import java.util.Iterator;
import java.util.ListIterator;

public class Example8Method {
    private static final Type BYTE_TYPE = Type.getObjectType("java/lang/Byte");

    private static final Type BOOLEAN_TYPE = Type.getObjectType("java/lang/Boolean");

    private static final Type SHORT_TYPE = Type.getObjectType("java/lang/Short");

    private static final Type CHARACTER_TYPE = Type.getObjectType("java/lang/Character");

    private static final Type INTEGER_TYPE = Type.getObjectType("java/lang/Integer");

    private static final Type FLOAT_TYPE = Type.getObjectType("java/lang/Float");

    private static final Type LONG_TYPE = Type.getObjectType("java/lang/Long");

    private static final Type DOUBLE_TYPE = Type.getObjectType("java/lang/Double");

    private static final Type NUMBER_TYPE = Type.getObjectType("java/lang/Number");

    private static final Type OBJECT_TYPE = Type.getObjectType("java/lang/Object");

    private static final Method BOOLEAN_VALUE = Method.getMethod("boolean booleanValue()");

    private static final Method CHAR_VALUE = Method.getMethod("char charValue()");

    private static final Method INT_VALUE = Method.getMethod("int intValue()");

    private static final Method FLOAT_VALUE = Method.getMethod("float floatValue()");

    private static final Method LONG_VALUE = Method.getMethod("long longValue()");

    private static final Method DOUBLE_VALUE = Method.getMethod("double doubleValue()");


    MethodNode methodNode;

    private LabelNode start = new LabelNode();
    private LabelNode end = new LabelNode();
    private int interceptorVarIndex;
    private int argsVarIndex;
    private int throwableVarIndex;
    private int resultVarIndex;
    private int nextLocal;

    private LabelNode beforeTryStart = new LabelNode();
    private LabelNode beforeTryEnd = new LabelNode();
    private LabelNode beforeTryHandler = new LabelNode();
    private LabelNode beforeTryJump = new LabelNode();
    private LabelNode afterTryStart = new LabelNode();
    private LabelNode afterTryEnd = new LabelNode();
    private LabelNode afterTryHandler = new LabelNode();
    private LabelNode afterTryJump = new LabelNode();

    public Example8Method(MethodNode methodNode) {
        this.methodNode = methodNode;
        this.nextLocal = methodNode.maxLocals;
    }

    public void addAroundMethod() {
        // init start/end label.
        setScopeLabel();

        // before
        interceptorVarIndex = addLocalVariable("interceptor", "Lcom/jaehong/asm/example8/Example8Interceptor;", null, this.start, this.end);
        System.out.println(interceptorVarIndex);
        InsnList beforeInsnList = new InsnList();

        invokeInterceptorRegistery(beforeInsnList);
        loadArgsVar(beforeInsnList);
        argsVarIndex = addLocalVariable("args", "[Ljava/lang/Object;", null, start, end);
        beforeInsnList.add(new VarInsnNode(Opcodes.ASTORE, argsVarIndex));

        TryCatchBlockNode beforeTryCatchBlockNode = new TryCatchBlockNode(beforeTryStart, beforeTryEnd, beforeTryHandler, "java/lang/Throwable");
        methodNode.tryCatchBlocks.add(beforeTryCatchBlockNode);

        beforeInsnList.add(beforeTryStart);
        beforeInsnList.add(new VarInsnNode(Opcodes.ALOAD, interceptorVarIndex));
        beforeInsnList.add(new VarInsnNode(Opcodes.ALOAD, 0));
        beforeInsnList.add(new VarInsnNode(Opcodes.ALOAD, argsVarIndex));
        beforeInsnList.add(new MethodInsnNode(Opcodes.INVOKEVIRTUAL, "com/jaehong/asm/example8/Example8Interceptor", "before", "(Ljava/lang/Object;[Ljava/lang/Object;)V", false));
        beforeInsnList.add(beforeTryEnd);
        beforeInsnList.add(new JumpInsnNode(Opcodes.GOTO, beforeTryJump));

        beforeInsnList.add(beforeTryHandler);
        beforeInsnList.add(new MethodInsnNode(Opcodes.INVOKESTATIC, "com/jaehong/asm/example8/Example8InterceptorHelper", "handleException", "(Ljava/lang/Throwable;)V", false));
        beforeInsnList.add(beforeTryJump);
        methodNode.instructions.insert(beforeInsnList);

        // after
        AbstractInsnNode node = methodNode.instructions.getFirst();
        while (node != null) {
            int opcode = node.getOpcode();
            if (opcode == Opcodes.IRETURN || opcode == Opcodes.RETURN || opcode == Opcodes.ARETURN || opcode == Opcodes.LRETURN || opcode == Opcodes.DRETURN || opcode == Opcodes.ATHROW) {
                InsnList afterInsnList = new InsnList();
                if (opcode == Opcodes.ATHROW) {
                    dup(afterInsnList);
                    throwableVarIndex = addLocalVariable("throwable", "Ljava/lang/Throwable;", null, start, end);
                    afterInsnList.add(new VarInsnNode(Opcodes.ASTORE, throwableVarIndex));
                } else {
                    if (opcode == Opcodes.RETURN) {
                        afterInsnList.add(new InsnNode(Opcodes.ACONST_NULL));
                    } else if (opcode == Opcodes.ARETURN) {
                        // dup
                        afterInsnList.add(new InsnNode(Opcodes.DUP));
                    } else {
                        if (opcode == Opcodes.LRETURN || opcode == Opcodes.DRETURN) {
                            // dup2
                            afterInsnList.add(new InsnNode(Opcodes.DUP2));
                        } else {
                            // dup
                            afterInsnList.add(new InsnNode(Opcodes.DUP));
                        }
                        final Type type = Type.getReturnType(methodNode.desc);
                        // box
                        if (type.getSort() == Type.OBJECT || type.getSort() == Type.ARRAY) {
                            // nothing
                        } else if (type == Type.VOID_TYPE) {
                            afterInsnList.add(new InsnNode(Opcodes.ACONST_NULL));
                        } else {
                            Type boxed = getBoxedType(type);
                            // new instance.
                            afterInsnList.add(new TypeInsnNode(Opcodes.NEW, type.getInternalName()));
                            if (type.getSize() == 2) {
                                // Pp -> Ppo -> oPpo -> ooPpo -> ooPp -> o
                                // dupX2
                                afterInsnList.add(new InsnNode(Opcodes.DUP_X2));
                                // dupX2
                                afterInsnList.add(new InsnNode(Opcodes.DUP_X2));
                                // pop
                                afterInsnList.add(new InsnNode(Opcodes.POP));
                            } else {
                                // p -> po -> opo -> oop -> o
                                // dupX1
                                afterInsnList.add(new InsnNode(Opcodes.DUP_X1));
                                // swap
                                afterInsnList.add(new InsnNode(Opcodes.SWAP));
                            }
                            Method method = new Method("<init>", Type.VOID_TYPE, new Type[]{type});
                            String owner = boxed.getSort() == Type.ARRAY ? boxed.getDescriptor() : boxed.getInternalName();
                            afterInsnList.add(new MethodInsnNode(Opcodes.INVOKESPECIAL, owner, method.getName(), method.getDescriptor(), false));
                        }
                    }
                    resultVarIndex = addLocalVariable("result", "Ljava/lang/Object;", null, start, end);
                    afterInsnList.add(new VarInsnNode(Opcodes.ASTORE, resultVarIndex));
                }

                afterInsnList.add(new VarInsnNode(Opcodes.ALOAD, interceptorVarIndex));
                afterInsnList.add(new VarInsnNode(Opcodes.ALOAD, 0));
                afterInsnList.add(new VarInsnNode(Opcodes.ALOAD, argsVarIndex));
                if (opcode == Opcodes.ATHROW) {
                    afterInsnList.add(new InsnNode(Opcodes.ACONST_NULL));
                    afterInsnList.add(new VarInsnNode(Opcodes.ALOAD, throwableVarIndex));
                } else {
                    afterInsnList.add(new VarInsnNode(Opcodes.ALOAD, resultVarIndex));
                    afterInsnList.add(new InsnNode(Opcodes.ACONST_NULL));
                }

                afterInsnList.add(new MethodInsnNode(Opcodes.INVOKEVIRTUAL, "com/jaehong/asm/example8/Example8Interceptor", "after", "(Ljava/lang/Object;[Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/Throwable;)V", false));

                methodNode.instructions.insertBefore(node, afterInsnList);
            }
            node = node.getNext();
        }
    }

    private void setScopeLabel() {
        AbstractInsnNode first = methodNode.instructions.getFirst();
        methodNode.instructions.insertBefore(first, start);
        methodNode.instructions.add(end);
    }

    private void invokeInterceptorRegistery(InsnList insnList) {
        push(insnList, 10);
        insnList.add(new MethodInsnNode(Opcodes.INVOKESTATIC, "com/jaehong/asm/example8/Example8InterceptorRegistery", "get", "(I)Lcom/jaehong/asm/example8/Example8Interceptor;", false));
        insnList.add(new VarInsnNode(Opcodes.ASTORE, interceptorVarIndex));
    }

    private void loadArgsVar(InsnList insnList) {
        Type[] argumentTypes = Type.getArgumentTypes(methodNode.desc);

        push(insnList, argumentTypes.length);
        // new array
        newArray(insnList, OBJECT_TYPE);
        for (int i = 0; i < argumentTypes.length; i++) {
            Type type = argumentTypes[i];
            dup(insnList);
            push(insnList, i);
            // loadArg
            loadArg(insnList, argumentTypes, i);
            // box
            box(insnList, type);
            // arrayStore
            arrayStore(insnList, OBJECT_TYPE);
        }
    }

    private static Type getBoxedType(final Type type) {
        switch (type.getSort()) {
            case Type.BYTE:
                return BYTE_TYPE;
            case Type.BOOLEAN:
                return BOOLEAN_TYPE;
            case Type.SHORT:
                return SHORT_TYPE;
            case Type.CHAR:
                return CHARACTER_TYPE;
            case Type.INT:
                return INTEGER_TYPE;
            case Type.FLOAT:
                return FLOAT_TYPE;
            case Type.LONG:
                return LONG_TYPE;
            case Type.DOUBLE:
                return DOUBLE_TYPE;
        }
        return type;
    }

    private int getArgIndex(final Type[] argumentTypes, final int arg) {
        int index = (methodNode.access & Opcodes.ACC_STATIC) == 0 ? 1 : 0;
        for (int i = 0; i < arg; i++) {
            index += argumentTypes[i].getSize();
        }
        return index;
    }


    private int addLocalVariable(final String name, final String desc, final String signature, final LabelNode start, final LabelNode end) {
        int index = this.nextLocal;
        this.nextLocal += 1;
        final LocalVariableNode interceptorVar = new LocalVariableNode("interceptor", "Lcom/jaehong/asm/example8/Example8Interceptor;", null, this.start, this.end, index);
        methodNode.localVariables.add(interceptorVar);

        return index;
    }

    public void push(InsnList insnList, final int value) {
        if (value >= -1 && value <= 5) {
            insnList.add(new InsnNode(Opcodes.ICONST_0 + value));
        } else if (value >= Byte.MIN_VALUE && value <= Byte.MAX_VALUE) {
            insnList.add(new IntInsnNode(Opcodes.BIPUSH, value));
        } else if (value >= Short.MIN_VALUE && value <= Short.MAX_VALUE) {
            insnList.add(new IntInsnNode(Opcodes.SIPUSH, value));
        } else {
            insnList.add(new LdcInsnNode(value));
        }
    }

    private void newArray(final InsnList insnList, final Type type) {
        insnList.add(new TypeInsnNode(Opcodes.ANEWARRAY, type.getInternalName()));
    }


    private void dup(final InsnList insnList) {
        insnList.add(new InsnNode(Opcodes.DUP));
    }

    private void dupX1(final InsnList insnList) {
        insnList.add(new InsnNode(Opcodes.DUP_X1));
    }

    private void dupX2(final InsnList insnList) {
        insnList.add(new InsnNode(Opcodes.DUP_X2));
    }

    private void pop(final InsnList insnList) {
        insnList.add(new InsnNode(Opcodes.POP));
    }

    private void swap(final InsnList insnList) {
        insnList.add(new InsnNode(Opcodes.SWAP));
    }

    private void loadArg(final InsnList insnList, Type[] argumentTypes, int i) {
        final int index = getArgIndex(argumentTypes, i);
        final Type type = argumentTypes[i];
        insnList.add(new VarInsnNode(type.getOpcode(Opcodes.ILOAD), index));
    }

    private void box(final InsnList insnList, Type type) {
        if (type.getSort() == Type.OBJECT || type.getSort() == Type.ARRAY) {
            return;
        }

        if (type == Type.VOID_TYPE) {
            // push null
            insnList.add(new InsnNode(Opcodes.ACONST_NULL));
        } else {
            Type boxed = getBoxedType(type);
            // new instance.
            newInstance(insnList, boxed);
            if (type.getSize() == 2) {
                // Pp -> Ppo -> oPpo -> ooPpo -> ooPp -> o
                // dupX2
                dupX2(insnList);
                // dupX2
                dupX2(insnList);
                // pop
                pop(insnList);
            } else {
                // p -> po -> opo -> oop -> o
                // dupX1
                dupX1(insnList);
                // swap
                swap(insnList);
            }
            invokeConstructor(insnList, boxed, new Method("<init>", Type.VOID_TYPE, new Type[]{type}));
        }
    }

    private void arrayStore(final InsnList insnList, final Type type) {
        insnList.add(new InsnNode(type.getOpcode(Opcodes.IASTORE)));
    }

    private void newInstance(final InsnList insnList, final Type type) {
        insnList.add(new TypeInsnNode(Opcodes.NEW, type.getInternalName()));
    }

    private void invokeConstructor(final InsnList insnList, final Type type, final Method method) {
        String owner = type.getSort() == Type.ARRAY ? type.getDescriptor() : type.getInternalName();
        insnList.add(new MethodInsnNode(Opcodes.INVOKESPECIAL, owner, method.getName(), method.getDescriptor(), false));
    }

    /**
     * @return
     */
    private int getFreeLocalIndex() {
        final Iterator i = methodNode.localVariables.iterator();
        int maxIndex = -1;
        while (i.hasNext()) {
            final LocalVariableNode node = (LocalVariableNode) i.next();
            if (node.index >= maxIndex) {
                if (node.desc.equals(Type.DOUBLE_TYPE.getDescriptor())
                        || node.desc.equals(Type.LONG_TYPE.getDescriptor())) {
                    maxIndex = node.index + 1;
                } else {
                    maxIndex = node.index;
                }
            }
        }
        maxIndex++;

        final int other = getFreeLocalByCode();
        return Math.max(other, maxIndex);
    }


    /**
     * @return
     */
    private int getFreeLocalByCode() {
        int maxIndex = -1;
        final Iterator i = methodNode.instructions.iterator();
        while (i.hasNext()) {
            final AbstractInsnNode insn = (AbstractInsnNode) i.next();
            if (insn instanceof VarInsnNode) {
                final VarInsnNode varInsnNode = (VarInsnNode) insn;
                if (varInsnNode.var >= maxIndex) { // >= needed because index could be reused with larger type
                    switch (varInsnNode.getOpcode()) {
                        case Opcodes.ILOAD:
                        case Opcodes.FLOAD:
                        case Opcodes.ALOAD:
                        case Opcodes.ISTORE:
                        case Opcodes.FSTORE:
                        case Opcodes.ASTORE:
                            maxIndex = varInsnNode.var;
                            break;

                        case Opcodes.LLOAD:
                        case Opcodes.DLOAD:
                        case Opcodes.LSTORE:
                        case Opcodes.DSTORE:
                            maxIndex = varInsnNode.var + 1;
                            break;
                        case Opcodes.RET:
                            // ignore already covered with the ASTORE
                            break;
                        default:
                            throw new IllegalArgumentException("Unexpected opcode " + insn.getOpcode() + " in VarInsnNode operation.");
                    }
                }
            }
        }

        return maxIndex + 1;
    }
}
