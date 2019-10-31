package com.bissdom.dotframework.common;

import com.bissdom.dotframework.util.LogResult;

;

public class ResultNote {
    LogResult result;
    String message;

    public ResultNote(LogResult resultParam, String messageParam) {
        this.result = resultParam;
        this.message = messageParam;
    }

    public LogResult getResult() {
        return this.result;
    }

    public void setResult(LogResult result) {
        this.result = result;
    }

    public String getMessage() {
        return this.message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String toString() {
        return this.result + "::" + this.message;
    }
}
