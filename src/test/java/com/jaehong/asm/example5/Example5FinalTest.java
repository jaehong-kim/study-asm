package com.jaehong.asm.example5;

import com.jaehong.asm.example4.Example4;
import org.junit.Test;
import org.objectweb.asm.util.ASMifier;


/**
 * Created by Naver on 2016-04-08.
 */
public class Example5FinalTest {

    @Test
    public void test() throws Exception {
        final String className = Example5Final.class.getName();
        ASMifier.main(new String[]{className});
    }

}