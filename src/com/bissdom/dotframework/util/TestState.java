package com.bissdom.dotframework.util;

public enum TestState {
    BEGIN(0),
    STEP(1),
    END(2);

    private int stateCode;

    private TestState(int code) {
        this.stateCode = code;
    }

    public String toString() {
        return String.valueOf(this.stateCode);
    }
}