package com.jaehong.asm.example1;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Foo {

    public void bar() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy");
        String result = dateFormat.format(new Date());
        System.out.println("foo.bar" + result);
    }
}
