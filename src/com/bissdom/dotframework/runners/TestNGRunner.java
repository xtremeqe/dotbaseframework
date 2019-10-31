package com.bissdom.dotframework.runners;

import com.bissdom.dotframework.reports.core.htmlgen.TestHtmlReportUtil;
import com.bissdom.dotframework.steps.Screenshot;
import com.bissdom.dotframework.util.FrameworkProperties;
import cucumber.api.CucumberOptions;
import cucumber.api.testng.AbstractTestNGCucumberTests;
import java.io.IOException;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeSuite;

@CucumberOptions(
        features = {"src/test/resources/features/datamap.feature"},
        glue = {"com.inovalon.framework.test", "com.inovalon.framework.steps"},
        plugin = {"pretty"},
        tags = {"@test2"}
)
public class TestNGRunner extends AbstractTestNGCucumberTests {
    public TestNGRunner() {
    }

    @AfterClass(
            alwaysRun = true
    )
    public void tearDownClass() throws Exception {
        super.tearDownClass();

        try {
            String htmlRptFlagStr = FrameworkProperties.getProperty("generateHtmlReport");
            if (!htmlRptFlagStr.isEmpty() && (htmlRptFlagStr.equalsIgnoreCase("true") || htmlRptFlagStr.equalsIgnoreCase("false"))) {
                String updateTestFlag = FrameworkProperties.getProperty("updateVSTSTestcases");
                if (!updateTestFlag.isEmpty() && (updateTestFlag.equalsIgnoreCase("true") || updateTestFlag.equalsIgnoreCase("false"))) {
                    String vstsAuth = FrameworkProperties.getProperty("vstsAuthorization");
                    if (updateTestFlag.isEmpty()) {
                        throw new IOException("Configuration Error: vstsAuthorization flag not correctly set !");
                    } else {
                        if (Boolean.valueOf(htmlRptFlagStr) || Boolean.valueOf(updateTestFlag)) {
                            TestHtmlReportUtil.generateReport(FrameworkProperties.getProjectName(), FrameworkProperties.getRunName(), FrameworkProperties.getResultsPath(), FrameworkProperties.getResultsHTMLPath(), Boolean.valueOf(htmlRptFlagStr), Boolean.valueOf(updateTestFlag), vstsAuth);
                        }

                    }
                } else {
                    throw new IOException("Configuration Error: updateVSTSTestcases flag not correctly set !");
                }
            } else {
                throw new IOException("Configuration Error: generateHtmlReport flag not correctly set !");
            }
        } catch (IOException var4) {
            var4.printStackTrace();
        }
    }

    @BeforeSuite(
            alwaysRun = true
    )
    public void beforeSuite() {
        Screenshot a = new Screenshot();
        a.createScreenshotDirectory();
    }
}