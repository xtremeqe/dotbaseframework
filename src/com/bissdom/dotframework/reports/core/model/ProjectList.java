package com.bissdom.dotframework.reports.core.model;

import java.util.List;

public class ProjectList {
    public List<String> projectNames;

    public ProjectList(List<String> projectNames) {
        this.projectNames = projectNames;
    }

    public List<String> getProjectNames() {
        return this.projectNames;
    }
}