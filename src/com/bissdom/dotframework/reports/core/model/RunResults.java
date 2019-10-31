package com.bissdom.dotframework.reports.core.model;

import java.util.ArrayList;
import java.util.List;

public class RunResults {
    private String environment;
    private String runName;
    private String project;
    private List<FeatureResult> featureResults = new ArrayList();
    private int passCount = 0;
    private int failCount = 0;
    private int scenarioCount = 0;
    private int featureCount = 0;
    private String totalExecTime;

    public RunResults(String project, String environment, String runName) {
        this.project = project;
        this.environment = environment;
        this.runName = runName;
    }

    public RunResults() {
    }

    public String getProject() {
        return this.project;
    }

    public String getEnvironment() {
        return this.environment;
    }

    public String getRunName() {
        return this.runName;
    }

    public int getPassCount() {
        return this.passCount;
    }

    public int getFailCount() {
        return this.failCount;
    }

    public int getFeatureCount() {
        return this.featureCount;
    }

    public int getScenarioCount() {
        return this.scenarioCount;
    }

    public List<FeatureResult> getFeatureResults() {
        return this.featureResults;
    }

    public void addFeatureResult(FeatureResult featureResult) {
        this.featureResults.add(featureResult);
    }

    public void deleteFeatureResults() {
        this.featureResults = null;
    }

    public void setPassCount(int passCount) {
        this.passCount = passCount;
    }

    public void setFailCount(int failCount) {
        this.failCount = failCount;
    }

    public void setScenarioCount(int scenarioCount) {
        this.scenarioCount = scenarioCount;
    }

    public void setFeatureCount(int featureCount) {
        this.featureCount = featureCount;
    }

    public String getTotalExecutionTime() {
        return this.totalExecTime;
    }

    public void setTotalExecutionTime(String totalExecTime) {
        this.totalExecTime = totalExecTime;
    }
}
