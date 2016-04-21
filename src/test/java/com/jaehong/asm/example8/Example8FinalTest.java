package com.jaehong.asm.example8;

import com.jaehong.asm.example7.Example7Final;
import org.junit.Test;
import org.objectweb.asm.util.ASMifier;

import static org.junit.Assert.*;

public class Example8FinalTest {

    @Test
    public void test() throws Exception {
        final String className = Example8Final.class.getName();
        ASMifier.main(new String[]{className});
    }

}