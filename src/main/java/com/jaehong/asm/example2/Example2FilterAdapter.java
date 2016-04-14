package com.jaehong.asm.example2;

import com.jaehong.asm.example1.AroundMethodAdapter;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;

public class Example2FilterAdapter extends ClassVisitor {
    public Example2FilterAdapter(ClassVisitor cv) {
        super(Opcodes.ASM5, cv);
    }

    @Override
    public MethodVisitor visitMethod(int acc, String name, String desc, String signature, String[] exceptions) {
        MethodVisitor mv = cv.visitMethod(acc, name, desc, signature, exceptions);
        if (isTarget(name, desc)) {
            System.out.println("FIND " + name + " " + desc);
            mv = new Example2AroundMethodAdapter(Opcodes.ASM5, mv, acc, name, desc);
        }

        return mv;
    }

    private boolean isTarget(String name, String desc) {
        if(name.equals("bar") && desc.equals("(Ljava/lang/String;IZJ)I")) {
            return true;
        }

        return false;
    }
}
