package com.bissdom.dotframework.common;

import org.testng.asserts.Assertion;
import org.testng.asserts.IAssert;
import org.testng.collections.Maps;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

public class SoftAssert extends Assertion {
    private Map<AssertionError, IAssert> errorMap = Maps.newLinkedHashMap();
    private ArrayList<AssertionError> errors = new ArrayList();
    private ArrayList<Exception> exceptions = new ArrayList();
    private ArrayList<IAssert> pass = new ArrayList();

    public SoftAssert() {
    }

    public void executeAssert(IAssert assertion) {
        try {
            assertion.doAssert();
            this.pass.add(assertion);
        } catch (AssertionError var3) {
            this.errors.add(var3);
            this.errorMap.put(var3, assertion);
        }

    }

    public void addException(Exception e) {
        this.exceptions.add(e);
    }

    public void assertAll() {
        StringBuilder sb = new StringBuilder();
        boolean isErrorsEmpty = this.errorMap.isEmpty();
        boolean isExceptionsEmpty = this.exceptions.isEmpty();
        if (!isErrorsEmpty || !isExceptionsEmpty) {
            sb.append("\n");
            Iterator var4 = this.errorMap.entrySet().iterator();

            while(var4.hasNext()) {
                Entry<AssertionError, IAssert> ae = (Entry)var4.next();
                String message = ((IAssert)ae.getValue()).getMessage();
                sb.append(message + "\n");
            }

            var4 = this.exceptions.iterator();

            while(var4.hasNext()) {
                Exception e = (Exception)var4.next();
                sb.append(e.getMessage());
                sb.append("\n");
            }

            throw new AssertionError(sb.toString());
        }
    }
}