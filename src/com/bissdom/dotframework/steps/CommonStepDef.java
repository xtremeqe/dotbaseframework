package com.bissdom.dotframework.steps;

import com.bissdom.dotframework.common.ScenarioContext;
import com.bissdom.dotframework.common.TestBase;
import com.bissdom.dotframework.util.CommonUtils;
import com.bissdom.dotframework.util.LogResult;
import cucumber.api.Scenario;
import cucumber.api.java.After;
import cucumber.api.java.Before;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import org.testng.ITestResult;
import org.testng.Reporter;
import org.testng.annotations.BeforeSuite;

public class CommonStepDef extends TestBase {
    public CommonStepDef() {
    }

    @Before
    public void beforeScenario(Scenario sc) {
        this.startSession(sc);
        List<String> tagList = (List)sc.getSourceTagNames();
        String testcase_tag = "";

        for(int i = 0; i < tagList.size(); ++i) {
            if (((String)tagList.get(i)).toLowerCase().contains("testcase")) {
                testcase_tag = (String)tagList.get(i);
                testcase_tag = testcase_tag.substring(testcase_tag.indexOf("[") + 1);
                testcase_tag = testcase_tag.substring(0, testcase_tag.indexOf("]"));
                break;
            }
        }

        this.context().markScenarioStart();
        this.context().markScenario_Start();
        this.logStepResult(LogResult.INFO, testcase_tag.split(","), "Starting Scenario");
    }

    @After
    public void afterScenario(Scenario sc) {
        if (null != this.context()) {
            this.context().markScenarioEnd();
            this.context().markScenario_End();
            this.logStepResult(LogResult.INFO, new String[]{"1123", "34345"}, "Finished Scenario");
            String start = (String)((ScenarioContext)scenarioContext.get()).getTestData("start_Time");
            String end = (String)((ScenarioContext)scenarioContext.get()).getTestData("end_Time");
            long timeElapsed = Long.valueOf(end) - Long.valueOf(start);
            long seconds = (long)Math.ceil((double)(timeElapsed / 1000L));
            this.context().getMap().put("timeElapsed", seconds);
            this.recordSessionResults(true);
            this.clanupSessions();
            this.resetSession();
        } else {
            System.out.println("Context Received as NULL. Something WRONG !!!!");
        }

    }

    @BeforeSuite(
            alwaysRun = true
    )
    protected void beforeSuite() {
        ITestResult result = Reporter.getCurrentTestResult();
        String suiteName = result.getTestContext().getSuite().getName();
        String hms = CommonUtils.getTimeInHMS();
    }

    protected void clanupSessions() {
        Map<String, TestBase> testBases = this.context().getBaseMap();
        Iterator var2 = testBases.values().iterator();

        while(var2.hasNext()) {
            TestBase base = (TestBase)var2.next();
            base.cleanup();
        }

    }

    public void cleanup() {
    }
}
