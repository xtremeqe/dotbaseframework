package com.bissdom.dotframework.reports.core.htmlgen;

import java.io.IOException;

public class TestSetup {
    public TestSetup() {
    }

    public static void main(String[] args) throws IOException {
        String logFilePath = "C:\\Work\\test-framework-latest\\test-framework\\target\\logs";
        String htmlReportPath = "C:\\Work\\test-framwork-report\\reports-core\\src\\test\\resources\\sample.html";
        new TestHtmlReportUtil();
        TestHtmlReportUtil.generateReport("framework", "TestFramework12-20181024", logFilePath, htmlReportPath, true, true, "Basic Oms0b2o0YWY2cDRudGJ0cTMza2N3NmVib3p0d2lrdWRweG5hdzVhYmJlYmNrbjRmMnltaHE=");
    }
}
