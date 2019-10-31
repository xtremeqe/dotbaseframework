package com.bissdom.dotframework.common;

import com.bissdom.dotframework.data.DBItem;
import com.bissdom.dotframework.data.Item;
import com.bissdom.dotframework.steps.Screenshot;
import com.bissdom.dotframework.util.*;
import cucumber.api.Scenario;
import org.testng.Assert;
import org.testng.ITestResult;
import org.testng.Reporter;
import org.testng.annotations.AfterSuite;
import org.testng.asserts.SoftAssert;

import java.util.List;
import java.util.Map;

public abstract class TestBase {
    protected SoftAssert sa = new SoftAssert();
    public static final ThreadLocal<ScenarioContext> scenarioContext = new ThreadLocal();

    public TestBase() {
    }

    public ScenarioContext context() {
        if (null == scenarioContext) {
            System.out.println("We have got a problem ");
        }

        return (ScenarioContext)scenarioContext.get();
    }

    public static void sleep(int seconds) throws InterruptedException {
        try {
            for(int i = 0; i < seconds; ++i) {
                Thread.sleep(1000L);
            }

        } catch (InterruptedException var2) {
            throw var2;
        }
    }

    protected void resetSession() {
        if (null != scenarioContext && null != scenarioContext.get()) {
            ((ScenarioContext)scenarioContext.get()).reset();
        }

    }

    protected void startSession(Scenario sc) {
        ScenarioContext context;
        if (null == scenarioContext.get()) {
            context = this.createContext(sc);
            scenarioContext.set(context);
        } else {
            ((ScenarioContext)scenarioContext.get()).reset();
            context = this.createContext(sc);
            scenarioContext.set(context);
        }

    }

    public ScenarioContext createContext(Scenario sc) {
        ScenarioContext context = new ScenarioContext();
        if (null == sc) {
            context.put("project", "Undefined");
            context.put("feature", "Undefined");
            context.put("scenario", "Undefined");
            context.put("environment", "Undefined");
            context.put("runName", "Undefined");
        } else {
            String[] names = sc.getId().split("/");
            context.put("project", FrameworkProperties.getProperty("project"));
            if (null != names) {
                String featureName = names[names.length - 1];
                context.put("feature", featureName.substring(0, featureName.indexOf(58)));
            } else {
                context.put("feature", sc.getName().trim());
            }

            context.put("scenario", sc.getName().trim());
            context.put("environment", FrameworkProperties.getEnvironment());
            context.put("runName", FrameworkProperties.getRunName());
            context.put("tenantId", FrameworkProperties.getProperty("tenantId"));
            context.setScenario(sc);
        }

        try {
            context.setLogAndData();
            return context;
        } catch (Exception var5) {
            System.out.println("Exception is setting up ScenarioContext" + var5.getStackTrace());
            throw new Error(var5.getMessage());
        }
    }

    public void addException(Exception e)
    { this.addException(e); }
//    refer { this.sa.addException(e); }

    @AfterSuite(
            alwaysRun = true
    )
    protected void afterSuite() {
        ITestResult result = Reporter.getCurrentTestResult();
        String suiteName = result.getTestContext().getSuite().getName();
        String hms = CommonUtils.getTimeInHMS();
    }

    protected void assertAllConditions(String msg, boolean... conditions) {
        boolean condition = true;
        boolean[] var4 = conditions;
        int var5 = conditions.length;

        for(int var6 = 0; var6 < var5; ++var6) {
            boolean condition2 = var4[var6];
            condition &= condition2;
        }

        Assert.assertTrue(condition, msg);
    }

    protected void assertTrue(boolean condition, String msg) {
        Assert.assertTrue(condition, msg);
    }

    protected void checkForVerificationErrors() {
        this.sa.assertAll();
    }

    protected Map<String, Object> getDataMap(String tag) {
        Item dataItem = this.context().getData().getItem(String.valueOf(this.context().get("project")), String.valueOf(this.context().get("feature")), String.valueOf(this.context().get("scenario")), String.valueOf(this.context().get("environment")), tag, String.valueOf(this.context().get("tenantId")));
        return dataItem.getData();
    }

    protected Map<String, Object> getDataMap(Map<String, String> tags) {
        Item dataItem = ((ScenarioContext)scenarioContext.get()).getData().getItem(tags);
        return dataItem.getData();
    }

    protected DBItem getDataFromDB(String query) {
        List<DBItem> dataItem = ((ScenarioContext)scenarioContext.get()).getDB().queryDB(query, DBItem.class);
        return dataItem.size() > 0 ? (DBItem)dataItem.get(0) : null;
    }

    protected <T> T getDataFromDB(String query, Class<T> type) {
        List<T> dataItem = ((ScenarioContext)scenarioContext.get()).getDB().queryDB(query, type);
        return dataItem.size() > 0 ? dataItem.get(0) : null;
    }

    protected void assertTrue(boolean condition, String message, boolean logResult) {
        try {
            Assert.assertTrue(condition, message);
            if (logResult) {
                this.logStepResult(LogResult.PASS, message);
            } else {
                this.logComment(LogResult.PASS, message);
            }
        } catch (Error var5) {
            if (logResult) {
                this.logStepResult(LogResult.FAIL, var5.getMessage());
            } else {
                this.logComment(LogResult.FAIL, var5.getMessage());
            }
        }

    }

    protected void assertFalse(boolean condition, String message, boolean logResult) {
        try {
            Assert.assertFalse(condition, message);
            if (logResult) {
                this.logStepResult(LogResult.PASS, message);
            } else {
                this.logComment(LogResult.PASS, message);
            }
        } catch (Error var5) {
            if (logResult) {
                this.logStepResult(LogResult.FAIL, var5.getMessage());
            } else {
                this.logComment(LogResult.FAIL, var5.getMessage());
            }
        }

    }

    protected void logStepResult(LogResult result, String message) {
        this.logStepResult(result, (String)message, (Exception)null);
    }

    protected void logStepResult(LogResult result, String message, Exception ex) {
        this.recordStepResults(result, message, (String[])null, ex);
    }

    protected void logStepResult(LogResult result, String[] ids, String message) {
        this.recordStepResults(result, message, ids, (Exception)null);
    }

    protected void logComment(LogResult result, String message) {
        this.addNote(result, message, (Exception)null);
    }

    protected void addNote(LogResult result, String message, Exception ex) {
        StringBuilder comment = (new StringBuilder()).append(message);
        if (null != ex) {
            comment.append(" :: Exception => ").append(ex.getMessage());
        }

        comment.append(";");
        this.context().addNote(result, comment.toString());
    }

    protected void recordStepResults(LogResult result, String message, String[] ids, Exception ex) {
        try {
            if (FrameworkProperties.IsTakeScreenshots() && result.equals(LogResult.FAIL)) {
                Screenshot a = new Screenshot();
                a.takeScreenshotOnFailure();
            }
        } catch (Exception var6) {
            var6.printStackTrace();
        }

        this.log().recordResult(JSONMessage.asJsonLog(this.context().getMap(), result, message, ids, ex));
    }

    protected void recordSessionResults(boolean includeNotes) {
        ResultNote rn = this.context().getScenarioResult();
        this.log().recordResult(JSONMessage.asJsonLog(this.context().getMap(), rn.getResult(), includeNotes ? this.context().getNotes() : null));
    }

    protected void debug(String message) {
        this.log().logDebug(message);
    }

    protected LogUtil log() {
        return ((ScenarioContext)scenarioContext.get()).getLog();
    }

    protected void debug(String message, Exception e) {
        this.debug(message + "\n\t" + " Exception " + e.getMessage());
    }

    public abstract void cleanup();
}
