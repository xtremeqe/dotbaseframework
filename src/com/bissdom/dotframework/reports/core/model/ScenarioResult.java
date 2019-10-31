package com.bissdom.dotframework.reports.core.model;

import java.util.ArrayList;
import java.util.List;

public class ScenarioResult {
    private List<String> failureMessages;
    private String scenarioName;
    private String elapsedTime;
    private String screenshotPath;
    private boolean isSuccess;
    private String[] testcases;

    public ScenarioResult() {
    }

    public ScenarioResult(String scenarioName) {
        this(scenarioName, true);
    }

    public ScenarioResult(String scenarioName, boolean isSuccess) {
        this.isSuccess = isSuccess;
        this.scenarioName = scenarioName;
        this.failureMessages = new ArrayList();
    }

    public ScenarioResult(String scenarioName, String[] asList) {
        this(scenarioName, true);
        this.testcases = asList;
    }

    public String[] getTestcaseList() {
        return this.testcases;
    }

    public void setTestcaseList(String[] testcaseList) {
        this.testcases = testcaseList;
    }

    public String getScenarioName() {
        return this.scenarioName;
    }

    public void setIsSuccess(boolean isSuccess) {
        this.isSuccess = isSuccess;
    }

    public boolean getIsSuccess() {
        return this.isSuccess;
    }

    public List<String> getFailureMessages() {
        return this.failureMessages;
    }

    public void addFailureMessage(String failureMessage) {
        this.failureMessages.add(failureMessage);
    }

    public String getElapsedTime() {
        return this.elapsedTime;
    }

    public void setElapsedTime(String elapsedTime) {
        this.elapsedTime = elapsedTime;
    }

    public String getScreenshotPath() {
        return this.screenshotPath;
    }

    public void setScreenshotPath(String filePath) {
        this.screenshotPath = filePath;
    }
}
