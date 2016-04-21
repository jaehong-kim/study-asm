package com.jaehong.asm.example8;

import com.jaehong.asm.Startup;
import org.junit.Test;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.tree.*;

import java.lang.instrument.ClassFileTransformer;
import java.lang.instrument.IllegalClassFormatException;
import java.lang.instrument.Instrumentation;
import java.security.ProtectionDomain;
import java.util.List;

public class Example8Test {


    @Test
    public void test() throws Exception {
        Startup.initializeIfPossible();
        Instrumentation inst = Startup.instrumentation();
        inst.addTransformer(new ClassFileTransformer() {
            public byte[] transform(ClassLoader loader, String className, Class<?> classBeingRedefined, ProtectionDomain protectionDomain, byte[] classfileBuffer) throws IllegalClassFormatException {
                if (className.equals("com/jaehong/asm/example8/Example8")) {
                    ClassReader cr = new ClassReader(classfileBuffer);
                    ClassNode cn = new ClassNode();
                    cr.accept(cn, 0);

                    System.out.println("###########################################################");
                    System.out.println("access " + cn.access);
                    System.out.println("attributes " + cn.attrs);
                    System.out.println("fields " + cn.fields);
                    System.out.println("innerClasses " + cn.innerClasses);
                    System.out.println("interfaces " + cn.interfaces);
                    System.out.println("invisibleAnnotations " + cn.invisibleAnnotations);
                    System.out.println("invisibleTypeAnnotations " + cn.invisibleTypeAnnotations);
                    System.out.println("methods " + cn.methods);
                    System.out.println("name " + cn.name);
                    System.out.println("outerClass " + cn.outerClass);
                    System.out.println("outerMethod " + cn.outerMethod);
                    System.out.println("outerMethodDesc "  + cn.outerMethodDesc);
                    System.out.println("signature " + cn.signature);
                    System.out.println("sourceDebug " + cn.sourceDebug);
                    System.out.println("sourceFile " + cn.sourceFile);
                    System.out.println("superName " + cn.superName);
                    System.out.println("version " + cn.version);
                    System.out.println("visibleAnnotations " + cn.visibleAnnotations);
                    System.out.println("visibleTypeAnnotations " + cn.visibleTypeAnnotations);

                    List<MethodNode> methods = cn.methods;
                    for(MethodNode method : methods) {
                        System.out.println("=============================================================");
                        System.out.println("access " + method.access);
                        System.out.println("annotationDefault " + method.annotationDefault);
                        System.out.println("attrs " + method.attrs);
                        System.out.println("desc " + method.desc);
                        System.out.println("exceptions " + method.exceptions);
                        System.out.println("instructions " + method.instructions);
                        for(int i = 0; i < method.instructions.size(); i++) {
                            if(method.instructions.get(i).getType() == AbstractInsnNode.LINE) {
                                LineNumberNode lineNumberNode = (LineNumberNode) method.instructions.get(i);
                                System.out.println(" line " + lineNumberNode.line);
                            }
                        }

                        System.out.println("invisibleAnnotations " + method.invisibleAnnotations);
                        System.out.println("invisibleLocalVariableAnnotations " + method.invisibleLocalVariableAnnotations);
                        System.out.println("invisibleParameterAnnotations " + method.invisibleParameterAnnotations);
                        System.out.println("invisibleTypeAnnotations " + method.invisibleTypeAnnotations);
                        System.out.println("localVariables " + method.localVariables);
                        List<LocalVariableNode> localVariableNodes = method.localVariables;
                        for(LocalVariableNode localVariableNode : localVariableNodes) {
                            System.out.println(" " + localVariableNode.name + ", " + localVariableNode.desc + ", " + localVariableNode.signature);
                        }

                        System.out.println("maxLocals " + method.maxLocals);
                        System.out.println("maxStack " + method.maxStack);
                        System.out.println("name " + method.name);
                        System.out.println("parameters " + method.parameters);
                        System.out.println("signature " + method.signature);
                        System.out.println("tryCatchBlocks " + method.tryCatchBlocks);
                        System.out.println("visibleAnnotations " + method.visibleAnnotations);
                        System.out.println("visibleLocalVariableAnnotations " + method.visibleLocalVariableAnnotations);
                        System.out.println("visibleParameterAnnotations " + method.visibleParameterAnnotations);
                        System.out.println("visibleTypeAnnotations " + method.visibleTypeAnnotations);
                    }

                    return classfileBuffer;
                }

                return classfileBuffer;
            }
        });

        Example8 example = new Example8();
        example.foo();
    }

}