package com.bissdom.dotframework.reports.core.model;

import java.util.List;

public class RunList {
    public List<String> runNames;

    public RunList() {
    }

    public RunList(List<String> runNames) {
        this.runNames = runNames;
    }

    public List<String> getRunNames() {
        return this.runNames;
    }
}

