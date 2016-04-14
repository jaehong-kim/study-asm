package com.jaehong.asm.example1;

import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;

public class FilterAdapter extends ClassVisitor {
    public FilterAdapter(ClassVisitor cv) {
        super(Opcodes.ASM5, cv);
    }

    @Override
    public MethodVisitor visitMethod(int acc, String name, String desc, String signature, String[] exceptions) {
        MethodVisitor mv = cv.visitMethod(acc, name, desc, signature, exceptions);
        if (isTarget(name, desc)) {
            System.out.println("FIND " + name + " " + desc);
            mv = new AroundMethodAdapter(Opcodes.ASM5, mv, acc, name, desc);
        }

        return mv;
    }

    private boolean isTarget(String name, String desc) {
        if(name.equals("bar") && desc.equals("()V")) {
            return true;
        }

        return false;
    }
}
