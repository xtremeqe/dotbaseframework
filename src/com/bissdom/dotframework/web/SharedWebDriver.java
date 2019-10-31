package com.bissdom.dotframework.web;

import cucumber.api.Scenario;
import cucumber.api.java.After;
import cucumber.api.java.Before;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.support.events.EventFiringWebDriver;

public class SharedWebDriver extends EventFiringWebDriver {
    private static final WebDriver REAL_DRIVER = new FirefoxDriver();
    private static final Thread CLOSE_THREAD = new Thread() {
        public void run() {
            SharedWebDriver.REAL_DRIVER.close();
        }
    };

    public SharedWebDriver() {
        super(REAL_DRIVER);
    }

    public void close() {
        if (Thread.currentThread() != CLOSE_THREAD) {
            throw new UnsupportedOperationException("You shouldn't close this WebDriver. It's shared and will close when the JVM exits.");
        } else {
            super.close();
        }
    }

    @Before
    public void deleteAllCookies() {
        this.manage().deleteAllCookies();
    }

    @After
    public void embedScreenshot(Scenario scenario) {
        try {
            byte[] screenshot = (byte[])this.getScreenshotAs(OutputType.BYTES);
            scenario.embed(screenshot, "image/png");
        } catch (WebDriverException var3) {
        }

    }

    static {
        Runtime.getRuntime().addShutdownHook(CLOSE_THREAD);
    }
}
