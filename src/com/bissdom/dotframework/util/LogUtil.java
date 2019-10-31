package com.bissdom.dotframework.util;

import com.bissdom.dotframework.common.ScenarioContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.testng.Reporter;

import java.io.File;
import java.util.Calendar;

public class LogUtil {
    private static Logger taResultsLog = LogManager.getLogger("TAResultLog");
    private static Logger taLog = LogManager.getLogger("TALog");
    ScenarioContext sc = null;

    private LogUtil(ScenarioContext context) {
        this.sc = context;
    }

    public void logData(Object[][] data) {
        for(int i = 0; i < data.length; ++i) {
            this.log("------------", 2, false);
            this.log("Data set #" + (i + 1) + ":", 2, false);

            for(int j = 0; j < data[i].length; ++j) {
                String dataWithNewLines = data[i][j].toString().replaceAll(";", "\n");
                this.log(dataWithNewLines.replaceAll(":", ":\n"), 2, false);
            }

            this.log("------------", 2, false);
        }

    }

    public void log(String content, int level, boolean logToConsole) {
        if (logToConsole) {
        }

        Reporter.log(content, level, false);
    }

    public void log(String content) {
        this.log(content, FrameworkProperties.getIntProperty("logLevel"), FrameworkProperties.getBooleanProperty("logToConsole"));
    }

    public void logScreenshot(File file, int logLevel, boolean logToConsole) {
        this.log("<img src=\"file://" + file.getAbsolutePath() + "\"/>", logLevel, logToConsole);
    }

    public String generateScreenshotName(String description) {
        Calendar c = Calendar.getInstance();
        StringBuilder fileName = new StringBuilder();
        int hour = c.get(11);
        if (hour > 12) {
            hour -= 12;
        }

        String h = String.format("%02d", hour);
        String m = String.format("%02d", c.get(12));
        String s = String.format("%02d", c.get(13));
        fileName.append(h + "-" + m + "-" + s + "_");
        fileName.append(description);
        fileName.append(".png");
        return fileName.toString();
    }

    public static LogUtil withContext(ScenarioContext context) {
        LogUtil util = new LogUtil(context);
        return util;
    }

    public LogUtil reset() {
        this.sc = null;
        return this;
    }

    public void initWith(ScenarioContext context) {
        this.sc = context;
    }

    public void logDebug(String message) {
        taLog.debug(message);
    }

    public void logInfo(String message) {
        taLog.info(message);
    }

    public void logError(String message) {
        taLog.error(message);
    }

    public void logException(String message, Throwable ex) {
        taLog.error(message, ex);
    }

    public void recordResult(String message) {
        taResultsLog.debug(message + "\n");
    }
}
