package com.jaehong.asm.method.before;

import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;

public class FilterAdapter extends ClassVisitor {
    private String mName;
    private String mDesc;

    public FilterAdapter(ClassVisitor classVisitor, String mName, String mDesc) {
        super(Opcodes.ASM5, classVisitor);
        this.mName = mName;
        this.mDesc = mDesc;
    }

    public MethodVisitor visitMethod(int acc, String name, String desc, String signature, String[] exceptions) {
        MethodVisitor mv = cv.visitMethod(acc, name, desc, signature, exceptions);

        if(name.equals(mName) && desc.equals(mDesc)) {
            System.out.println("FIND");
            mv = new BeforeMethodAdaper(Opcodes.ASM5, mv, acc, name, desc);
        }

        return mv;
    }
}
