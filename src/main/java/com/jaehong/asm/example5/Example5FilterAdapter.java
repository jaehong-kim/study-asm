package com.jaehong.asm.example5;

import com.jaehong.asm.example3.Example3AroundMethodAdapter;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;

public class Example5FilterAdapter extends ClassVisitor {
    public Example5FilterAdapter(ClassVisitor cv) {
        super(Opcodes.ASM5, cv);
    }

    @Override
    public MethodVisitor visitMethod(int acc, String name, String desc, String signature, String[] exceptions) {
        MethodVisitor mv = cv.visitMethod(acc, name, desc, signature, exceptions);
        if (isTarget(name, desc)) {
            mv = new Example5AroundMethodAdapter(Opcodes.ASM5, mv, acc, name, desc);
        }

        return mv;
    }

    private boolean isTarget(String name, String desc) {
        if(name.equals("foo") || name.equals("bar") || name.equals("staticFoo")) {
            return true;
        }

        return false;
    }
}
