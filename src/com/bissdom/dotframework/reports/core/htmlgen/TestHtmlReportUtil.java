package com.bissdom.dotframework.reports.core.htmlgen;


import com.bissdom.dotframework.reports.core.model.FeatureResult;
import com.bissdom.dotframework.reports.core.model.RunResults;
import com.bissdom.dotframework.reports.core.model.ScenarioResult;
import com.bissdom.dotframework.reports.core.vsts.TestCaseUpdate;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import freemarker.template.TemplateExceptionHandler;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class TestHtmlReportUtil {
    private static final int FAILURE_CODE = 2;
    private static final String TEST_FEATURE = "feature";
    private static final String TEST_SCENARIO = "scenario";
    private static final String TEST_ENV = "environment";
    private static final String TEST_RESULT_CODE = "result";
    private static final String TEST_RESULT_MESSAGE = "message";
    private static final String ELAPSED_TIME = "timeElapsed";
    private static final String SCREENSHOT_PATH = "screenshotPath";
    private static final String TEST_IDS = "testcase_ids";
    private static final String FREEMARKER_TEMPLATE_NAME = "TestResultDetail.ftl";
    private static final String TEST_RESULT_HTML_FILENAME = "TestResultDetail-output.html";
    private static ArrayList<Long> executionTimeArray = new ArrayList();
    private static String executionTime = null;
    private static Set<String> featureName = new HashSet();
    private static Set<String> scenariolineNo = new HashSet();

    public TestHtmlReportUtil() {
    }

    public static void generateReport(String project, String runName, String logFilePath, String htmlReportPath, boolean generateReport, boolean updateVSTSTests, String vstsAuth) throws IOException {
        RunResults runResults = getRunResults(logFilePath, project, runName, true);
        if (runResults != null) {
            if (generateReport) {
                generateRunResultHtml(runResults, htmlReportPath);
            }

            if (updateVSTSTests) {
                TestCaseUpdate.batchTestUpdate(runResults, vstsAuth);
            }
        }

    }

    public static void generateReportByTemplate(String project, String runName, String logFilePath, String outputHtmlpath) throws IOException {
        RunResults runResults = getRunResults(logFilePath, project, runName, true);
        if (runResults != null) {
            generateRunResultFromTemplate(runResults, outputHtmlpath);
        }

    }

    private static void generateRunResultFromTemplate(RunResults runResults, String outptHtmlpath) {
        try {
            Configuration cfg = new Configuration();
            cfg.setDefaultEncoding("UTF-8");
            cfg.setTemplateExceptionHandler(TemplateExceptionHandler.RETHROW_HANDLER);
            Map<String, Object> map = new HashMap();
            map.put("project", runResults.getProject());
            map.put("runResults", runResults);
            cfg.setClassForTemplateLoading(TestHtmlReportUtil.class, "/templates");
            Template template = cfg.getTemplate("TestResultDetail.ftl");
            Writer console = new OutputStreamWriter(System.out);
            template.process(map, console);
            console.flush();
            File outDir = new File(outptHtmlpath);
            if (!outDir.exists()) {
                outDir.mkdirs();
            }

            Writer file = new FileWriter(new File(outptHtmlpath + "/" + "TestResultDetail-output.html"));
            template.process(map, file);
            file.flush();
            file.close();
        } catch (IOException var8) {
            var8.printStackTrace();
        } catch (TemplateException var9) {
            var9.printStackTrace();
        }

    }

    private static RunResults getRunResults(String reportFolderPath, String project, String runName, boolean isDetailView) {
        if (reportFolderPath.isEmpty()) {
            System.out.println("Test Report folder path not found.. ");
            return null;
        } else {
            File folder = new File(reportFolderPath);
            String fileName = getReportLogFileName(folder);
            RunResults runResults = null;

            try {
                if (fileName == null || !(new File(fileName)).exists()) {
                    System.out.println("HTML Report Generation ::  Logfile doesn't exists. Log file name +> " + fileName);
                }

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
                            String elapsedTime = (String)obj.get("timeElapsed");
                            String screenshotPath = (String)obj.get("screenshotPath");
                            String testcaseIds = (String)obj.get("testcase_ids");
                            if (runResults == null) {
                                String environment = (String)obj.get("environment");
                                currentFeature = new FeatureResult(featureName);
                                currentScenario = new ScenarioResult(scenarioName, testcaseIds.split(","));
                                ++scenarioCount;
                                ++featureCount;
                                runResults = new RunResults(project, environment, runName);
                            }

                            if (!scenarioName.equals(currentScenario.getScenarioName())) {
                                ++scenarioCount;
                                if (currentScenario.getIsSuccess()) {
                                    ++passCount;
                                } else {
                                    ++failCount;
                                }

                                currentFeature.addScenarioResult(currentScenario);
                                currentScenario = new ScenarioResult(scenarioName, testcaseIds.split(","));
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
                                currentScenario.setScreenshotPath(screenshotPath);
                            }

                            if (elapsedTime != null) {
                                currentScenario.setElapsedTime(elapsedTime);
                                long timeInSecs = Long.valueOf(currentScenario.getElapsedTime());
                                executionTimeArray.add(timeInSecs);
                                long totalExecutionTimeInSecs = executionTimeArray.stream().mapToLong((i) -> {
                                    return i;
                                }).sum();
                                String time = timeConversion(totalExecutionTimeInSecs);
                                executionTime = String.valueOf(time);
                            }
                        } catch (ParseException var29) {
                            var29.printStackTrace();
                        }
                    }
                }

                reader.close();
                if (runResults == null) {
                    runResults = new RunResults(project, "EMPTY_LOG_RESULTS", runName);
                }

                if (currentScenario != null) {
                    if (currentFeature != null) {
                        currentFeature.addScenarioResult(currentScenario);
                    }

                    runResults.addFeatureResult(currentFeature);
                    if (currentScenario.getIsSuccess()) {
                        ++passCount;
                    } else {
                        ++failCount;
                    }
                }

                if (!isDetailView) {
                    runResults.deleteFeatureResults();
                }

                runResults.setPassCount(passCount);
                runResults.setFailCount(failCount);
                runResults.setScenarioCount(scenarioCount);
                runResults.setFeatureCount(featureCount);
                runResults.setTotalExecutionTime(String.valueOf(executionTime));
            } catch (IOException var30) {
                var30.printStackTrace();
            }

            return runResults;
        }
    }

    private static String timeConversion(long totalSeconds) {
        long p1 = (long)Math.ceil((double)(totalSeconds % 60L));
        long p2 = (long)Math.ceil((double)(totalSeconds / 60L));
        long p3 = (long)Math.ceil((double)(p2 % 60L));
        p2 /= 60L;
        String hhmmss = p2 + ":" + p3 + ":" + p1;
        return hhmmss;
    }

    private static String getReportLogFileName(File folder) {
        String reportLogFileName = "";
        if (folder != null && folder.isDirectory()) {
            File[] var5;
            int var4 = (var5 = folder.listFiles()).length;

            for(int var3 = 0; var3 < var4; ++var3) {
                File f = var5[var3];
                if (f.getAbsolutePath().contains("taresults")) {
                    reportLogFileName = f.getAbsolutePath();
                }
            }
        } else {
            System.out.println("Test Report log file 'taresults.log' not found.. ");
        }

        return reportLogFileName;
    }

    private static void generateRunResultHtml(RunResults runResults, String htmlReportPath) {
        File file = new File(htmlReportPath);
        if (file.exists()) {
            deleteHtmlReport(file);
        }

        createHtmlReport(runResults, file);
    }

    private static void createHtmlReport(RunResults runResults, File file) {
        FileWriter fileWriter = null;
        BufferedWriter bufferedWriter = null;

        try {
            fileWriter = new FileWriter(file);
            bufferedWriter = new BufferedWriter(fileWriter);
            String htmlPage = "<html><body style='background-color:#fff'><b><h3><center><u>Test Run Results : Summary</u></center></h3></b></br>";
            bufferedWriter.write(htmlPage);
            appendSummarySection(runResults, bufferedWriter);
            appendDetailsSection(runResults, bufferedWriter);
            System.out.println("Html Run Result page created");
            bufferedWriter.flush();
            fileWriter.flush();
        } catch (IOException var13) {
            var13.printStackTrace();
        } finally {
            try {
                bufferedWriter.close();
                fileWriter.close();
            } catch (IOException var12) {
                var12.printStackTrace();
            }

        }

    }

    private static void appendDetailsSection(RunResults runResults, BufferedWriter bufferedWriter) throws IOException {
        String detailsHdr = "</br><h3><center><u>Test Run Results : Detail</u></center></h3></b></br>";
        bufferedWriter.append(detailsHdr);
        bufferedWriter.append("<table border='1' width='100%'>");
        Iterator var4 = runResults.getFeatureResults().iterator();

        while(var4.hasNext()) {
            FeatureResult featureResult = (FeatureResult)var4.next();
            bufferedWriter.append("<tr><td><b>Feature Name:</b> <b><span style = 'color:#6495ED'>" + featureResult.getFeatureName() + "</span></b></td></tr>");
            bufferedWriter.append("<tr><td>");
            bufferedWriter.append("<table border='1' width='100%'><tr><th>Scenario Name</th><th>TestcaseIds</th><th>Status</th><th>Failure Message</th><th>Screenshots</th><th>Time Elapsed(In secs)</th></tr>");
            Iterator var6 = featureResult.getScenarioResults().iterator();

            while(var6.hasNext()) {
                ScenarioResult scResult = (ScenarioResult)var6.next();
                bufferedWriter.append("<tr>");
                bufferedWriter.append("<td width='30%'>" + scResult.getScenarioName() + "</td>");
                bufferedWriter.append("<td width='10%'> <span style = 'color:#006400'>" + String.join(",", scResult.getTestcaseList()) + "</span></td>");
                String isSuccessStr = scResult.getIsSuccess() ? "Success" : "Failure";
                if (isSuccessStr.equals("Success")) {
                    bufferedWriter.append("<td width='10%'> <span style = 'color:#006400'>" + isSuccessStr + "</span></td>");
                } else if (isSuccessStr.equals("Failure")) {
                    bufferedWriter.append("<td width='10%'> <span style = 'color:#FF0000'>" + isSuccessStr + "</span></td>");
                }

                bufferedWriter.append("<td width='40%'>");
                Iterator var9 = scResult.getFailureMessages().iterator();

                while(var9.hasNext()) {
                    String faiMsg = (String)var9.next();
                    bufferedWriter.append(faiMsg + "</br>");
                }

                if (isSuccessStr.equals("Failure")) {
                    bufferedWriter.append("<td><a href =" + scResult.getScreenshotPath() + ">Click here for screenshot</a></td>");
                } else if (isSuccessStr.equals("Success")) {
                    bufferedWriter.append("<td> </td>");
                }

                bufferedWriter.append("<td width='10%'>" + scResult.getElapsedTime() + "</td>");
                bufferedWriter.append("</td>");
                bufferedWriter.append("</tr>");
            }

            bufferedWriter.append("</table></td></tr>");
        }

        bufferedWriter.append("</table></body></html>");
    }

    private static void appendSummarySection(RunResults runResults, BufferedWriter bufferedWriter) throws IOException {
        String summary = "<table  border='1' width='100%'><tr><th>ENV</th><th>RUN NAME</th><th>PROJECT</th><th>PASS CNT</th><th>FAIL CNT</th><th>SCENARIO CNT</th><th>FEATURE CNT</th><th>EXECUTION TIME</th></tr><tr><td>" + runResults.getEnvironment() + "</td>" + "<td>" + runResults.getRunName() + "</td>" + "<td>" + runResults.getProject() + "</td>" + "<td>" + runResults.getPassCount() + "</td>" + "<td>" + runResults.getFailCount() + "</td>" + "<td>" + runResults.getScenarioCount() + "</td>" + "<td>" + runResults.getFeatureCount() + "</td>" + "<td>" + runResults.getTotalExecutionTime() + " " + "(HH:MM:SS)" + "</td></tr>" + "</table>";
        bufferedWriter.append(summary);
    }

    private static void deleteHtmlReport(File file) {
        System.out.println("HTML result File already exists.. deleting..");

        try {
            if (file.delete()) {
                System.out.println(file.getName() + " is deleted!");
            } else {
                System.out.println("Delete operation is failed.");
            }
        } catch (Exception var2) {
            var2.printStackTrace();
        }

    }

    private static void openRunResultInBrowser(String browserPath, String htmlFile) {
        try {
            Runtime r = Runtime.getRuntime();
            String cmd = browserPath + " " + htmlFile;
            r.exec(cmd);
        } catch (Exception var4) {
            var4.printStackTrace();
        }

    }
}
