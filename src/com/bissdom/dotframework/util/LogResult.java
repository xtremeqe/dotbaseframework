package com.bissdom.dotframework.util;

public enum LogResult {
    PASS(1),
    FAIL(2),
    INFO(3);

    private int statusCode;

    private LogResult(int code) {
        this.statusCode = code;
    }

    public String toString() {
        return String.valueOf(this.statusCode);
    }
}

