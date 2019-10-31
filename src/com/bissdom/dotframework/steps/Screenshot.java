package com.bissdom.dotframework.steps;

import com.bissdom.dotframework.common.ScenarioContext;
import com.bissdom.dotframework.common.TestBase;
import com.bissdom.dotframework.util.FrameworkProperties;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;
import org.apache.commons.io.FileUtils;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;

public class Screenshot extends BaseDef {
    public static String screenshotDir = null;
    private String screenshotFolder = "Screenshots";

    public Screenshot() {
    }

    public void createScreenshotDirectory() {
        try {
            String screenShotFolder = FrameworkProperties.getScreenshotPath() + this.screenshotFolder;
            File createDir = new File(screenShotFolder);
            if (!createDir.exists()) {
                createDir.mkdir();
            }

            Date date = new Date();
            String currentDate = (new SimpleDateFormat("MM-dd-yyyyHH-mm-ss")).format(date);
            File dir = new File(screenShotFolder + File.separator + "Screenshot_" + currentDate);
            dir.mkdir();
            screenshotDir = dir.getName();
            System.out.println(screenshotDir);
        } catch (Exception var6) {
        }

    }

    public void takeScreenshotOnFailure() {
        try {
            File srcFile = (File)((TakesScreenshot)this.getSeleniumDriver()).getScreenshotAs(OutputType.FILE);

            try {
                Random a = new Random();
                String scenarioName = (String)((ScenarioContext)TestBase.scenarioContext.get()).getTestData("scenario") + "_" + a.nextInt(50);
                String filePath = FrameworkProperties.getScreenshotPath() + this.screenshotFolder + File.separator + screenshotDir + File.separator + scenarioName + ".png";
                File f = new File(filePath);
                ((ScenarioContext)TestBase.scenarioContext.get()).put("screenshotPath", f.getAbsolutePath().replaceAll(" ", "%20"));
                FileUtils.copyFile(srcFile, new File(filePath));
            } catch (IOException var6) {
                var6.printStackTrace();
            }

        } catch (ClassCastException var7) {
            throw var7;
        }
    }
}
