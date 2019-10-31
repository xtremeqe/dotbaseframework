package com.bissdom.dotframework.runners;

import com.bissdom.dotframework.reports.core.htmlgen.TestHtmlReportUtil;
import com.bissdom.dotframework.util.FrameworkProperties;
import cucumber.api.cli.Main;
import java.io.IOException;

public class CLIRunner {
    public CLIRunner() {
    }

    public static void main(String[] args) throws Throwable {
        ClassLoader contextClassLoader = Thread.currentThread().getContextClassLoader();
        Main.run(args, contextClassLoader);
        generateReport();
    }

    public static void generateReport() {
        try {
            String htmlRptFlagStr = FrameworkProperties.getProperty("generateHtmlReport");
            if (htmlRptFlagStr.isEmpty() || !htmlRptFlagStr.equalsIgnoreCase("true") && !htmlRptFlagStr.equalsIgnoreCase("false")) {
                throw new IOException("Configuration Error: generateHtmlReport flag not correctly set !");
            }

            if (Boolean.valueOf(htmlRptFlagStr)) {
                TestHtmlReportUtil.generateReport(FrameworkProperties.getProjectName(), FrameworkProperties.getRunName(), FrameworkProperties.getResultsPath(), FrameworkProperties.getResultsHTMLPath(), true, true, FrameworkProperties.getProperty("vstsAuthorization"));
            }
        } catch (IOException var1) {
            var1.printStackTrace();
        }

    }
}