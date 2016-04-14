package com.jaehong.asm.example7;

public class Example7Final implements Example7Accessor {
    private String var = null;
    private int i;
    private boolean b;
    private long l;

    public void setVar(String var) {
        this.var = var;
    }

    public String getVar() {
        return var;
    }

    public void setInt(int i) {
        this.i = i;
    }

    public int getInt() {
        return i;
    }

    public void setBoolean(boolean b) {
        this.b = b;
    }

    public boolean getBoolean() {
        return b;
    }

    public void setLong(long l) {
        this.l = l;
    }

    public long getLong() {
        return l;
    }

    public void foo() {
        System.out.println("foo");
    }
}
