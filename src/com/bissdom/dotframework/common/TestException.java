package com.bissdom.dotframework.common;

public class TestException extends Exception {
    private static final long serialVersionUID = 1L;

    public TestException() {
    }

    public TestException(String message) {
        super(message);
    }
}