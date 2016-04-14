package com.jaehong.asm.example3;

import com.jaehong.asm.example2.Example2AroundMethodAdapter;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;

import java.util.Arrays;

public class Example3FilterAdapter extends ClassVisitor {
    public Example3FilterAdapter(ClassVisitor cv) {
        super(Opcodes.ASM5, cv);
    }

    @Override
    public MethodVisitor visitMethod(int acc, String name, String desc, String signature, String[] exceptions) {
        MethodVisitor mv = cv.visitMethod(acc, name, desc, signature, exceptions);
        if (isTarget(name, desc)) {
            mv = new Example3AroundMethodAdapter(Opcodes.ASM5, mv, acc, name, desc);
        }

        return mv;
    }

    private boolean isTarget(String name, String desc) {
        if(name.equals("<init>")) {
            return true;
        }

        if(name.equals("foo")) {
            return true;
        }

        return false;
    }
}
