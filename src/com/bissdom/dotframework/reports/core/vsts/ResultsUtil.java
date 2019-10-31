package com.bissdom.dotframework.reports.core.vsts;

import com.bissdom.dotframework.reports.core.model.FeatureResult;
import com.bissdom.dotframework.reports.core.model.ProjectList;
import com.bissdom.dotframework.reports.core.model.RunList;
import com.bissdom.dotframework.reports.core.model.RunResults;
import com.bissdom.dotframework.reports.core.model.ScenarioResult;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileFilter;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class ResultsUtil {
    private static final int FAILURE_CODE = 2;

    public ResultsUtil() {
    }

    public ProjectList getProjectList(String reportFolderPath) {
        File rootFolder = new File(reportFolderPath);
        List<String> runs = new ArrayList();
        File[] var7;
        int var6 = (var7 = rootFolder.listFiles()).length;

        for(int var5 = 0; var5 < var6; ++var5) {
            File file = var7[var5];
            runs.add(file.getName());
        }

        return new ProjectList(runs);
    }

    public RunList getRunList(String reportFolderPath, String project) {
        File projectFolder = new File(reportFolderPath + "/" + project);
        List<String> runs = new ArrayList();
        File[] var8;
        int var7 = (var8 = projectFolder.listFiles()).length;

        for(int var6 = 0; var6 < var7; ++var6) {
            File file = var8[var6];
            runs.add(file.getName());
        }

        return new RunList(runs);
    }

    public RunResults getLastRun(String reportFolderPath, String project) {
        String projectFolderPath = reportFolderPath + "/" + project;
        File projectFolder = new File(projectFolderPath);
        if (!projectFolder.exists() && !projectFolder.isDirectory()) {
            return null;
        } else {
            File lastRunFile = lastFileModified(projectFolder);
            return lastRunFile == null ? null : this.getRunResults(reportFolderPath, project, lastRunFile.getName(), true);
        }
    }

    public RunResults getRunResults(String reportFolderPath, String project, String runName, boolean isDetailView) {
        File folder = new File(reportFolderPath + "/" + project + "/" + runName);
        String fileName = folder.listFiles()[0].getAbsolutePath();
        RunResults runResults = null;

        try {
            FileReader reader = new FileReader(new File(fileName));
            BufferedReader bufferedReader = new BufferedReader(reader);
            JSONParser parser = new JSONParser();
            FeatureResult currentFeature = null;
            ScenarioResult currentScenario = null;
            int passCount = 0;
            int failCount = 0;
            int scenarioCount = 0;
            int featureCount = 0;

            String line;
            while((line = bufferedReader.readLine()) != null) {
                if (!line.isEmpty()) {
                    try {
                        JSONObject obj = (JSONObject)parser.parse(line);
                        String featureName = (String)obj.get("feature");
                        String scenarioName = (String)obj.get("scenario");
                        if (runResults == null) {
                            String environment = (String)obj.get("environment");
                            runResults = new RunResults(project, environment, runName);
                            currentFeature = new FeatureResult(featureName);
                            currentScenario = new ScenarioResult(scenarioName);
                            ++scenarioCount;
                            ++featureCount;
                        }

                        if (!scenarioName.equals(currentScenario.getScenarioName())) {
                            ++scenarioCount;
                            if (currentScenario.getIsSuccess()) {
                                ++passCount;
                            } else {
                                ++failCount;
                            }

                            currentFeature.addScenarioResult(currentScenario);
                            currentScenario = new ScenarioResult(scenarioName);
                        }

                        if (!featureName.equals(currentFeature.getFeatureName())) {
                            ++featureCount;
                            runResults.addFeatureResult(currentFeature);
                            currentFeature = new FeatureResult(featureName);
                        }

                        int resultCode = new Integer((String)obj.get("result"));
                        if (2 == resultCode) {
                            currentScenario.setIsSuccess(false);
                            currentScenario.addFailureMessage((String)obj.get("message"));
                        }
                    } catch (ParseException var22) {
                        var22.printStackTrace();
                    }
                }
            }

            reader.close();
            currentFeature.addScenarioResult(currentScenario);
            runResults.addFeatureResult(currentFeature);
            if (currentScenario.getIsSuccess()) {
                ++passCount;
            } else {
                ++failCount;
            }

            if (!isDetailView) {
                runResults.deleteFeatureResults();
            }

            runResults.setPassCount(passCount);
            runResults.setFailCount(failCount);
            runResults.setScenarioCount(scenarioCount);
            runResults.setFeatureCount(featureCount);
        } catch (IOException var23) {
            var23.printStackTrace();
        }

        return runResults;
    }

    public static File lastFileModified(File dir) {
        File[] files = dir.listFiles(new FileFilter() {
            public boolean accept(File file) {
                return file.exists();
            }
        });
        long lastMod = -9223372036854775808L;
        File choice = null;
        File[] var8 = files;
        int var7 = files.length;

        for(int var6 = 0; var6 < var7; ++var6) {
            File file = var8[var6];
            if (file.lastModified() > lastMod) {
                choice = file;
                lastMod = file.lastModified();
            }
        }

        return choice;
    }
}
