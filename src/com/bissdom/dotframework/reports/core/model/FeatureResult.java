package com.bissdom.dotframework.reports.core.model;

import java.util.ArrayList;
import java.util.List;

public class FeatureResult {
    private List<ScenarioResult> scenarioResults;
    private String featureName;

    public FeatureResult() {
    }

    public FeatureResult(String featureName) {
        this.featureName = featureName;
        this.scenarioResults = new ArrayList();
    }

    public List<ScenarioResult> getScenarioResults() {
        return this.scenarioResults;
    }

    public String getFeatureName() {
        return this.featureName;
    }

    public void addScenarioResult(ScenarioResult scenarioResult) {
        this.scenarioResults.add(scenarioResult);
    }
}
