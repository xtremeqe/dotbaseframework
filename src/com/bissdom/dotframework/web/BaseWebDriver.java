package com.bissdom.dotframework.web;

import com.bissdom.dotframework.util.FrameworkProperties;
import com.bissdom.dotframework.util.LogUtil;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeDriverService.Builder;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxProfile;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.remote.service.DriverService;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;

public class BaseWebDriver implements WebDriver {
    private static DriverService driverService;
    private static Browser browser;
    private WebDriver driver;
    private int implicitWait;
    private JavascriptExecutor jsExecutor;

    public BaseWebDriver(Browser browserParam) throws Exception {
        this.implicitWait = FrameworkProperties.getImplicitWait();
        browser = browserParam;
        switch(browserParam) {
            case CHROME:
                System.setProperty("webdriver.chrome.driver", FrameworkProperties.getProperty("chromeDriverPath"));
                ChromeOptions chromeOptions = new ChromeOptions();
                chromeOptions.addArguments(new String[]{"--start-maximized"});
                chromeOptions.addArguments(new String[]{"--incognito"});
                this.driver = new ChromeDriver(chromeOptions);
                break;
            case FIREFOX:
                System.setProperty("webdriver.gecko.driver", FrameworkProperties.getProperty("firefoxDriverPath"));
                this.driver = new FirefoxDriver();
                this.driver.manage().window().maximize();
                break;
            case IE:
                System.setProperty("webdriver.ie.driver", FrameworkProperties.getProperty("ieDriverPath"));
                this.driver = new InternetExplorerDriver();
                break;
            default:
                throw new IllegalArgumentException(browser.toString() + " is not supported");
        }

        this.jsExecutor = (JavascriptExecutor)this.driver;
    }

    public BaseWebDriver(LogUtil log) throws Exception {
        this(FrameworkProperties.getBrowser());
    }

    public BaseWebDriver() throws Exception {
        this(Browser.CHROME);
    }

    private static synchronized void init(Browser browserParam) throws Exception {
        if (null == browserParam) {
            throw new Exception("Invalid browser property = > " + browserParam);
        } else {
            if (!browserParam.equals(browser) || !driverService.isRunning()) {
                browser = browserParam;
                startDriverService(browser);
            }

        }
    }

    protected BaseWebDriver driver() {
        return this;
    }

    public WebDriverWait newWait(int timeOutInSeconds) {
        return new WebDriverWait(this.driver, (long)timeOutInSeconds);
    }

    public void sleep(int seconds) {
        long milliSeconds = (long)(seconds * 1000);

        try {
            Thread.sleep(milliSeconds);
        } catch (InterruptedException var5) {
            var5.printStackTrace();
        }

    }

    protected String getText(WebElement element) {
        return element.getText();
    }

    protected void goBack() {
        this.driver().navigate().back();
    }

    protected void goToUrl(String url) {
        this.driver().get(url);
    }

    public String getCurrentUrl() {
        return this.driver.getCurrentUrl();
    }

    public void get(String url) {
        this.driver.get(url);
    }

    protected void refreshPage() {
        this.driver().navigate().refresh();
    }

    protected String getCurrentURL() {
        return this.driver().getCurrentUrl();
    }

    public void quit() {
        this.driver.quit();
    }

    public Options manage() {
        return this.driver.manage();
    }

    public Navigation navigate() {
        return this.driver.navigate();
    }

    public void close() {
        this.driver.close();
    }

    public TargetLocator switchTo() {
        return this.driver.switchTo();
    }

    public String getPageSource() {
        return this.driver.getPageSource();
    }

    public String getTitle() {
        return this.driver.getTitle();
    }

    public WebDriver getWebDriver() {
        return this.driver;
    }

    public boolean isDriverInValid() {
        return this.driver.toString().contains("(null)");
    }

    public WebDriver getDriver() {
        return new RemoteWebDriver(driverService.getUrl(), this.getCapabilities(browser));
    }

    private static synchronized void startDriverService(Browser browser) throws IOException {
        if (null == driverService || !driverService.isRunning()) {
            switch(browser) {
                case CHROME:
                case CHROMELESS:
                    System.setProperty("webdriver.chrome.driver", FrameworkProperties.getProperty("chromeDriverPath"));
                    startChromeService();
                    break;
                case FIREFOX:
                    System.setProperty("webdriver.gecko.driver", FrameworkProperties.getProperty("firefoxDriverPath"));
                    startGeckoService();
                    break;
                case IE:
                    System.setProperty("webdriver.ie.driver", FrameworkProperties.getProperty("ieDriverPath"));
                    startIEService();
                    break;
                case PHANTOMJS:
                    System.setProperty("webdriver.phantomjs.driver", FrameworkProperties.getProperty("phantomjsDriverPath"));
                    startPhnatomJSService();
                    break;
                default:
                    throw new IllegalArgumentException(browser.toString() + " is not supported");
            }
        }

    }

    public RemoteWebDriver getBrowserStackDriver() {
        RemoteWebDriver driver = null;
        DesiredCapabilities capabilities = this.getCapabilities(Browser.FIREFOX);

        try {
            driver = new RemoteWebDriver(new URL("http://www.google.com"), capabilities);
            driver.manage().timeouts().implicitlyWait((long)FrameworkProperties.getImplicitWait(), TimeUnit.SECONDS);
            driver.manage().window().maximize();
        } catch (MalformedURLException var4) {
            var4.printStackTrace();
        }

        return driver;
    }

    private DesiredCapabilities getCapabilities(Browser browser) {
        DesiredCapabilities capability = null;
        switch(browser) {
            case CHROME:
                capability = DesiredCapabilities.chrome();
                capability.setPlatform(Platform.WINDOWS);
                capability.setAcceptInsecureCerts(true);
                break;
            case FIREFOX:
                capability = DesiredCapabilities.firefox();
                capability.setPlatform(Platform.WINDOWS);
                capability.setAcceptInsecureCerts(true);
                if (FrameworkProperties.IsDebugMode() && !FrameworkProperties.IsRemote()) {
                    FirefoxProfile profile = null;
                    profile = new FirefoxProfile();
                    profile.setPreference("extensions.firebug.currentVersion", FrameworkProperties.getProperty("firebugVersion"));
                    File extensions = new File("./../Framework/ffx_add-ons");
                    File[] listFiles = extensions.listFiles();
                    if (listFiles != null) {
                        File[] var7 = listFiles;
                        int var8 = listFiles.length;

                        for(int var9 = 0; var9 < var8; ++var9) {
                            File f = var7[var9];

                            try {
                                profile.addExtension(f);
                            } catch (Exception var12) {
                                var12.printStackTrace();
                            }
                        }
                    }

                    capability.setCapability("firefox_profile", profile);
                }
                break;
            case IE:
                capability = DesiredCapabilities.internetExplorer();
                capability.setPlatform(Platform.WINDOWS);
                capability.setAcceptInsecureCerts(true);
                break;
            case CHROMELESS:
                ChromeOptions chromeOptions = new ChromeOptions();
                chromeOptions.addArguments(new String[]{"--headless"});
                capability = DesiredCapabilities.chrome();
                capability.setCapability("goog:chromeOptions", chromeOptions);
                capability.setAcceptInsecureCerts(true);
                capability.setPlatform(Platform.WINDOWS);
                break;
            case PHANTOMJS:
                capability = DesiredCapabilities.phantomjs();
                capability.setPlatform(Platform.WINDOWS);
        }

        if (capability != null) {
            capability.setBrowserName(browser.name);
        }

        if (FrameworkProperties.getBooleanProperty("useProxy")) {
            this.configureProxy(FrameworkProperties.getProperty("proxyHost"), FrameworkProperties.getIntProperty("proxyPort"), browser, capability);
        }

        return capability;
    }

    public JavascriptExecutor getJSExecutor() {
        return this.jsExecutor;
    }

    public WebDriver getRemoteDriver(Browser browser) {
        DesiredCapabilities capability = this.getCapabilities(browser);

        try {
            String hubURL = "http://" + FrameworkProperties.getRemoteServerIP() + ":4444/wd/hub";
            return new RemoteWebDriver(new URL(hubURL), capability);
        } catch (MalformedURLException var4) {
            var4.printStackTrace();
            return null;
        }
    }

    public static synchronized void startChromeService() {
        if (null == driverService) {
            try {
                String chromeDriverPath = FrameworkProperties.getProperty("chromeDriverPath");
                File chromeDriver = new File(chromeDriverPath);
                driverService = ((Builder)((Builder)(new Builder()).usingDriverExecutable(chromeDriver)).usingAnyFreePort()).build();
                System.out.println("Starting chrome service..." + driverService.getUrl().toString());
                driverService.start();
            } catch (IOException var2) {
                String message = "Error occured while starting service using chrome driver:  ";
                System.out.println(message + var2.getMessage());
            }
        }

    }

    public static synchronized void startGeckoService() throws IOException {
        if (null == driverService) {
            try {
                String geckoDriverPath = FrameworkProperties.getProperty("firefoxDriverPath");
                File geckoDriver = new File(geckoDriverPath);
                driverService = ((org.openqa.selenium.firefox.GeckoDriverService.Builder)((org.openqa.selenium.firefox.GeckoDriverService.Builder)(new org.openqa.selenium.firefox.GeckoDriverService.Builder()).usingDriverExecutable(geckoDriver)).usingAnyFreePort()).build();
                System.out.println("Starting Gecko service..." + driverService.getUrl().toString());
                driverService.start();
            } catch (IOException var2) {
                throw var2;
            }
        }

    }

    public static synchronized void startPhnatomJSService() throws IOException {
        if (null == driverService) {
            try {
                String phantomJSDriverPath = FrameworkProperties.getProperty("phantomjsDriverPath");
                File phantomDriver = new File(phantomJSDriverPath);
                driverService = (new org.openqa.selenium.phantomjs.PhantomJSDriverService.Builder()).usingPhantomJSExecutable(phantomDriver).usingAnyFreePort().build();
                System.out.println("Starting PhantomJSservice..." + driverService.getUrl().toString());
                driverService.start();
            } catch (IOException var2) {
                throw var2;
            }
        }

    }

    public static synchronized void startIEService() throws IOException {
        if (null == driverService) {
            try {
                String ieDriverPath = FrameworkProperties.getProperty("ieDriverPath");
                File ieDriver = new File(ieDriverPath);
                driverService = ((org.openqa.selenium.ie.InternetExplorerDriverService.Builder)((org.openqa.selenium.ie.InternetExplorerDriverService.Builder)(new org.openqa.selenium.ie.InternetExplorerDriverService.Builder()).usingDriverExecutable(ieDriver)).usingAnyFreePort()).build();
                System.out.println("Starting ie service..." + driverService.getUrl().toString());
                driverService.start();
            } catch (IOException var2) {
                throw var2;
            }
        }

    }

    public static void stopservice(LogUtil log) {
        log.logDebug("Stopping ie service..." + driverService.getUrl().toString());
        driverService.stop();
    }

    protected void acceptAlert() {
        this.driver().switchTo().alert().accept();
    }

    protected void acceptAlertIfPresent() {
        try {
            this.acceptAlert();
        } catch (NoAlertPresentException var2) {
        }

    }

    public void click(By locator) {
        WebElement element = this.findElement(locator);
        element.click();
    }

    public void clear(WebElement element) {
        element.clear();
    }

    public void click(WebElement element) {
        element.click();
    }

    public void sendKeys(WebElement el, String text) {
        el.sendKeys(new CharSequence[]{text});
    }

    public void moveToElement(By locator) {
        WebElement element = this.driver.findElement(locator);
        Actions builder = new Actions(this.driver);
        builder.moveToElement(element).build().perform();
        this.sleep(2);
    }

    public void moveToElement(WebElement el) {
        Actions builder = new Actions(this.driver);
        builder.moveToElement(el).build().perform();
        this.sleep(2);
    }

    public void moveToElementAndClick(By locator) {
        WebElement element = this.driver.findElement(locator);
        Actions builder = new Actions(this.driver);
        builder.moveToElement(element).click().perform();
    }

    protected void dragAndDrop(By locator, int xOffset, int yOffset) {
        Actions builder = new Actions(this.driver().getWebDriver());
        WebElement element = this.driver().findElement(locator);
        builder.dragAndDropBy(element, xOffset, yOffset).perform();
    }

    protected void waitForElement(By locator) {
        this.driver().waitForElementVisible(locator, FrameworkProperties.getImplicitWait());
    }

    protected void waitForElement(By locator, int timeout) {
        this.driver().waitForElementVisible(locator, timeout);
    }

    protected void waitForElementClickable(By locator) {
        WebDriverWait wait = new WebDriverWait(this.driver(), (long)FrameworkProperties.getIntProperty("implicitWait"));
        wait.until(ExpectedConditions.elementToBeClickable(locator));
    }

    public WebElement findElement(By locator) {
        try {
            this.waitForElementVisible(locator);
            WebElement element = this.driver.findElement(locator);
            return element;
        } catch (NoSuchElementException var5) {
            String msg = "Unable to find element located " + locator.toString();
            throw new NoSuchElementException(msg);
        }
    }

    public List<WebElement> findElements(By locator) {
        try {
            this.waitForElementVisible(locator);
            List<WebElement> elements = this.driver.findElements(locator);
            return elements;
        } catch (NoSuchElementException var5) {
            String msg = "Unable to find element located " + locator.toString();
            throw new NoSuchElementException(msg);
        }
    }

    public void waitForElementInvisible(By locator, int timeOutInSeconds) {
        WebDriverWait wait = this.newWait(timeOutInSeconds);

        try {
            wait.until(ExpectedConditions.invisibilityOfElementLocated(locator));
        } catch (TimeoutException var5) {
            throw var5;
        }
    }

    public void waitForElementVisible(By locator) {
        WebDriverWait wait = this.newWait(this.implicitWait);

        try {
            wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
        } catch (TimeoutException var4) {
            throw var4;
        }
    }

    public void waitForElementVisible(By locator, int timeOutInSeconds) {
        WebDriverWait wait = this.newWait(timeOutInSeconds);

        try {
            wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
        } catch (TimeoutException var5) {
            throw var5;
        }
    }

    protected boolean isDisabled(WebElement element) {
        return element.getAttribute("disabled") != null;
    }

    protected boolean isDisplayed(WebElement element) {
        return element.isDisplayed();
    }

    public boolean isDisplayed(By locator) {
        this.driver.manage().timeouts().implicitlyWait(2L, TimeUnit.SECONDS);

        WebElement element;
        try {
            element = this.findElement(locator);
            this.driver.manage().timeouts().implicitlyWait((long)FrameworkProperties.getImplicitWait(), TimeUnit.SECONDS);
        } catch (NoSuchElementException var4) {
            this.driver.manage().timeouts().implicitlyWait((long)FrameworkProperties.getImplicitWait(), TimeUnit.SECONDS);
            return false;
        }

        return element.isDisplayed();
    }

    public boolean isSelected(By locator) {
        WebElement element = this.findElement(locator);
        boolean selected = element.isSelected();
        return selected;
    }

    protected void scrollWindowDown(int pixels) {
        this.executeJavaScript("window.scrollBy(0," + pixels + ");");
    }

    protected void scrollWindowLeft(int pixels) {
        this.executeJavaScript("window.scrollBy(-" + pixels + ",0);");
    }

    protected void scrollWindowRight(int pixels) {
        this.executeJavaScript("window.scrollBy(" + pixels + ",0);");
    }

    protected void scrollWindowUp(int pixels) {
        this.executeJavaScript("window.scrollBy(0,-" + pixels + ");");
    }

    public String getWindowHandle() {
        return this.driver.getWindowHandle();
    }

    public Set<String> getWindowHandles() {
        return this.driver.getWindowHandles();
    }

    protected WebDriver switchToDefaultContent() {
        return this.driver().switchTo().defaultContent();
    }

    protected void switchToFrame(By locator) {
        WebElement reportFrame = this.findElement(locator);
        this.driver().switchTo().frame(reportFrame);
    }

    private void switchWindow(String master) {
        Set<String> handles = this.driver().getWindowHandles();
        Iterator var3 = handles.iterator();

        while(var3.hasNext()) {
            String handle = (String)var3.next();
            if (!handle.equals(master)) {
                this.driver().switchTo().window(handle);
            }
        }

        this.sleep(2);
    }

    public void selectCheckbox(WebElement checkBox) {
        if (!checkBox.isSelected()) {
            checkBox.click();
        }

    }

    public void selectComboByIndex(By locator, int index) {
        WebElement element = this.findElement(locator);
        Select select = new Select(element);
        select.selectByIndex(index);
    }

    public void selectComboByValue(By locator, String value) {
        WebElement element = this.findElement(locator);
        Select select = new Select(element);
        select.selectByValue(value);
    }

    public void selectComboByVisibleText(By locator, String visibleText) {
        WebElement element = this.findElement(locator);
        Select select = new Select(element);
        select.selectByVisibleText(visibleText);
    }

    protected void selectCombo(By locator, String text) {
        this.driver().selectComboByVisibleText(locator, text);
    }

    public void launchBrowser(Browser browser2, String url) {
        this.driver.manage().timeouts().implicitlyWait((long)this.implicitWait, TimeUnit.SECONDS);
        this.driver.manage().window().maximize();
        this.get(url);
    }

    public Object callJS(String script, Object... objects) {
        return objects.length > 0 ? this.jsExecutor.executeScript(script, objects) : this.jsExecutor.executeScript(script, new Object[0]);
    }

    private void changeColor(String color, WebElement element) {
        this.jsExecutor.executeScript("arguments[0].style.backgroundColor = '" + color + "'", new Object[]{element});
    }

    private void configureProxy(String proxyHost, int proxyPort, Browser browser, DesiredCapabilities capabilities) {
        switch(browser) {
            case CHROME:
            case CHROMELESS:
                capabilities.setCapability("chrome.switches", Arrays.asList("--proxy-server=http://" + proxyHost + ":" + proxyPort + "\""));
                break;
            case FIREFOX:
                Proxy proxy = new Proxy();
                proxy.setHttpProxy(proxyHost + ":" + proxyPort);
                proxy.setFtpProxy(proxyHost + ":" + proxyPort);
                proxy.setSslProxy(proxyHost + ":" + proxyPort);
                capabilities.setCapability("proxy", proxy);
            case IE:
        }

    }

    private void flash(WebElement element) {
        String bgcolor = element.getCssValue("backgroundColor");

        for(int i = 0; i < 1; ++i) {
            this.changeColor("yellow", element);
            this.changeColor(bgcolor, element);
        }

    }

    public void focus(By locator) {
        WebElement element = this.findElement(locator);
        (new Actions(this.driver)).moveToElement(element).perform();
    }

    public void highlight(WebElement element) {
        for(int i = 0; i < 2; ++i) {
            this.jsExecutor.executeScript("arguments[0].setAttribute('style', arguments[1]);", new Object[]{element, "color: yellow; border: 2px solid yellow;"});
            this.jsExecutor.executeScript("arguments[0].setAttribute('style', arguments[1]);", new Object[]{element, ""});
        }

    }

    protected void closeAndSwitchWindow() {
        String master = this.driver().getWindowHandle();
        this.driver().close();
        this.sleep(2);
        this.switchWindow(master);
    }

    protected void closeBrowser(LogUtil log) {
        this.driver().close();
    }

    protected void executeJavaScript(String script) {
        this.driver().callJS(script);
    }

    protected String getFirstSelectedOption(By locator) {
        Select selectedOption = new Select(this.findElement(locator));
        return selectedOption.getFirstSelectedOption().getText();
    }
}
