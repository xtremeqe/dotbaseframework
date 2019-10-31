package com.bissdom.dotframework.util;

import com.bissdom.framework.web.Browser;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.Properties;
import java.util.TreeMap;

public class FrameworkProperties {
    static String runDate = (new SimpleDateFormat("yyyyMMdd")).format(new Date());
    static Properties props = new Properties();

    public FrameworkProperties() {
    }

    public static boolean initializeProperties(String resource) {
        try {
            System.out.println("Initializing Framework Properties file from: " + resource);
            (new FrameworkProperties()).loadProperties(resource);
            return true;
        } catch (IOException var2) {
            System.out.println("ERROR: Unable to load properties from " + resource);
            return false;
        }
    }

    public static Browser getBrowser() {
        String browserName = getProperty("browser");
        Browser browser = null;
        byte var3 = -1;
        switch(browserName.hashCode()) {
            case -1508923629:
                if (browserName.equals("chromeless")) {
                    var3 = 3;
                }
                break;
            case -1361128838:
                if (browserName.equals("chrome")) {
                    var3 = 2;
                }
                break;
            case -849452327:
                if (browserName.equals("firefox")) {
                    var3 = 0;
                }
                break;
            case -554494698:
                if (browserName.equals("phantomjs")) {
                    var3 = 4;
                }
                break;
            case 3356:
                if (browserName.equals("ie")) {
                    var3 = 1;
                }
        }

        switch(var3) {
            case 0:
                browser = Browser.FIREFOX;
                break;
            case 1:
                browser = Browser.IE;
                break;
            case 2:
                browser = Browser.CHROME;
                break;
            case 3:
                browser = Browser.CHROMELESS;
                break;
            case 4:
                browser = Browser.PHANTOMJS;
                break;
            default:
                browser = Browser.CHROME;
        }

        return browser;
    }

    public static boolean IsDebugMode() {
        String debugMode = getProperty("debugMode");
        return Boolean.parseBoolean(debugMode);
    }

    public static int getImplicitWait() {
        String implicitWait = getProperty("implicitWait");
        return implicitWait == null ? 10 : Integer.parseInt(implicitWait);
    }

    public static boolean IsLogToConsole() {
        String logToConsole = getProperty("logToConsole");
        return Boolean.parseBoolean(logToConsole);
    }

    public static String getProperty(String key) {
        return props.getProperty(key);
    }

    public static boolean IsRemote() {
        String remote = getProperty("remote");
        return Boolean.parseBoolean(remote);
    }

    public static String getEnvironment() {
        String environment = getProperty("environment");
        return environment;
    }

    public static String getProjectName() {
        String project = getProperty("project");
        return project;
    }

    public static String getResultsPath() {
        String testresult = getProperty("testresult");
        return testresult;
    }

    public static String getResultsHTMLPath() {
        String testresult = getProperty("testresult");
        return testresult + "/TestResults-" + getRunName() + ".html";
    }

    public static String getRemoteServerIP() {
        String remote = getProperty("remoteServerIP");
        return remote;
    }

    public static boolean IsScreenshotInLog() {
        String screenshotInLog = getProperty("screenshotInLog");
        return Boolean.parseBoolean(screenshotInLog);
    }

    public static String getScreenshotPath() {
        String screenshotPath = getProperty("screenshotPath");
        return screenshotPath;
    }

    public static boolean IsTakeScreenshots() {
        String takeScreenshots = getProperty("takeScreenshots");
        return Boolean.parseBoolean(takeScreenshots);
    }

    private TreeMap<String, String> getProperties() {
        TreeMap<String, String> map = new TreeMap();
        Iterator var2 = props.stringPropertyNames().iterator();

        while(var2.hasNext()) {
            String key = (String)var2.next();
            map.put(key, getProperty(key));
        }

        return map;
    }

    private void loadProperties(String resource) throws IOException {
        ClassLoader loader = this.getClass().getClassLoader();
        InputStream in = loader.getResourceAsStream(resource);
        if (null == in) {
            in = new FileInputStream(resource);
        }

        (new StringBuilder()).append("Loading properties: ").append(resource.toLowerCase()).toString();
        boolean isFramework = resource.equals("framework");
        TreeMap var6 = this.getProperties();

        try {
            props.load((InputStream)in);
        } catch (Exception var9) {
            String msg = "Unable to load " + resource.toLowerCase() + ".properties; check Properties interface.";
            System.err.println("[ERROR]: " + msg);
            throw new IllegalStateException(msg);
        }

        TreeMap<String, String> after = this.getProperties();
    }

    public static boolean getBooleanProperty(String key) {
        return Boolean.valueOf(getProperty(key));
    }

    public static int getIntProperty(String key) {
        return Integer.valueOf(getProperty(key));
    }

    public static String getRunName() {
        String runName = getProperty("runname");
        return null == runName ? runDate : runName + '-' + runDate;
    }

    public static boolean useMongoDB() {
        String useMongoDB = getProperty("useMongoDB");
        return null == useMongoDB ? false : Boolean.valueOf(useMongoDB);
    }

    static {
        String propPath = System.getProperty("frameworkprop");
        if (propPath != null) {
            initializeProperties(propPath);
        } else {
            initializeProperties("framework.properties");
        }

    }
}
