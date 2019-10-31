package com.bissdom.dotframework.web;

import com.bissdom.dotframework.common.TestBase;
import com.bissdom.dotframework.util.FrameworkProperties;
import org.openqa.selenium.*;
import org.openqa.selenium.interactions.Action;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;

import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Set;
import java.util.concurrent.TimeUnit;

public class WebTestBase extends TestBase {
    boolean initialized = false;

    public WebTestBase() {
    }

    protected void init() {
        try {
            if (null == this.context().getDriver() || this.context().getDriver().isDriverInValid()) {
                BaseWebDriver driver = new BaseWebDriver(FrameworkProperties.getBrowser());
                this.context().setDriver(driver);
                this.context().register(this);
                this.initialized = true;
            }
        } catch (Exception var2) {
            var2.printStackTrace();
        }

    }

    protected BaseWebDriver driver() {
        if (!this.initialized) {
            this.init();
        }

        return this.context().getDriver();
    }

    public WebDriver getDriver() {
        if (!this.initialized) {
            this.init();
        }

        return this.driver();
    }

    public JavascriptExecutor getJSExecutor() {
        return this.driver().getJSExecutor();
    }

    protected void executeJavaScript(String script) {
        this.driver().callJS(script, new Object[0]);
    }

    public void goToURL(String url) {
        this.driver().get(url);
    }

    protected String getCurrentURL() {
        return this.driver().getCurrentUrl();
    }

    protected String getPageTitle() {
        return this.driver().getTitle();
    }

    protected void goBack() {
        this.driver().navigate().back();
    }

    protected void refreshPage() {
        this.driver().navigate().refresh();
    }

    public WebDriver getSeleniumDriver() {
        return this.driver().getWebDriver();
    }

    public void switchFrame(String frameName) {
        this.driver().switchTo().frame(frameName);
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

    protected WebElement findElement(By locator) {
        return this.driver().findElement(locator);
    }

    protected List<WebElement> findElements(By locator) {
        return this.driver().findElements(locator);
    }

    protected void clear(WebElement el) {
        this.driver().clear(el);
    }

    protected void clear(By locator) {
        WebElement el = this.driver().findElement(locator);
        this.driver().clear(el);
    }

    protected void sendKeys(WebElement el, String text) {
        this.driver().sendKeys(el, text);
    }

    protected void sendKeys(By locator, String text) {
        WebElement element = this.driver().findElement(locator);
        this.driver().sendKeys(element, text);
    }

    protected void click(WebElement el) {
        this.driver().click(el);
    }

    protected void click(By locator) {
        this.driver().click(locator);
    }

    protected void dragAndDrop(By locator, int xOffset, int yOffset) {
        Actions builder = new Actions(this.driver().getWebDriver());
        WebElement element = this.driver().findElement(locator);
        builder.dragAndDropBy(element, xOffset, yOffset).perform();
    }

    protected void dragAndDrop(WebElement source, WebElement destination) {
        Actions builder = new Actions(this.driver().getWebDriver());
        Action dragAndDrop = builder.clickAndHold(source).moveToElement(destination).release(destination).build();
        dragAndDrop.perform();
    }

    protected void moveToElement(By locator) {
        this.driver().moveToElement(locator);
    }

    protected void moveToElement(WebElement el) {
        this.driver().moveToElement(el);
    }

    protected void moveToElementAndClick(By locator) {
        this.driver().moveToElementAndClick(locator);
    }

    protected void waitForElement(By locator) {
        this.driver().waitForElementVisible(locator, FrameworkProperties.getImplicitWait());
    }

    protected void waitForElement(By locator, int timeout) {
        this.driver().waitForElementVisible(locator, timeout);
    }

    protected void waitForElementClickable(By locator) {
        WebDriverWait wait = new WebDriverWait(this.driver(), (long)FrameworkProperties.getImplicitWait());
        wait.until(ExpectedConditions.elementToBeClickable(locator));
    }

    protected void waitForElementInvisible(By locator, int timeout) {
        this.driver().waitForElementInvisible(locator, timeout);
    }

    protected void waitForElementVisible(By locator, int timeOutInSeconds) {
        this.driver().waitForElementVisible(locator, timeOutInSeconds);
    }

    protected boolean isDisabled(WebElement element) {
        return element.getAttribute("disabled") != null;
    }

    protected boolean isDisplayed(By locator) {
        return this.driver().isDisplayed(locator);
    }

    protected boolean isDisplayed(WebElement element) {
        return element.isDisplayed();
    }

    protected void verifyElementDisabled(WebElement element) {
        boolean isEnabled = element.isEnabled();
        this.verifyFalse(isEnabled, "Element was not disabled!");
    }

    protected void verifyElementDisplayed(By locator) {
        boolean isDisplayed = this.isDisplayed(locator);
        this.verifyTrue(isDisplayed, "Element was not displayed!");
    }

    protected void verifyElementNotDisplayed(By locator) {
        boolean isDisplayed = this.isDisplayed(locator);
        this.verifyFalse(isDisplayed, "Element was displayed!");
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

    protected WebDriver switchToDefaultContent() {
        return this.driver().switchTo().defaultContent();
    }

    protected void switchToFrame(By locator) {
        WebElement reportFrame = this.findElement(locator);
        this.driver().switchTo().frame(reportFrame);
    }

    protected void switchWindow(String master) {
        Set<String> handles = this.driver().getWindowHandles();
        Iterator var3 = handles.iterator();

        while(var3.hasNext()) {
            String handle = (String)var3.next();
            if (!handle.equals(master)) {
                this.driver().switchTo().window(handle);
            }
        }

        try {
            sleep(2);
        } catch (InterruptedException var5) {
            var5.printStackTrace();
        }

    }

    protected void closeAndSwitchWindow() throws InterruptedException {
        String master = this.driver().getWindowHandle();
        this.driver().close();
        sleep(2);
        this.switchWindow(master);
    }

    protected void selectCheckbox(WebElement el) {
        this.driver().selectCheckbox(el);
    }

    protected void selectCombo(By locator, String text) {
        this.driver().selectComboByVisibleText(locator, text);
    }

    protected void selectComboByIndex(By locator, int index) {
        this.driver().selectComboByIndex(locator, index);
    }

    protected void selectComboByValue(By locator, String value) {
        this.driver().selectComboByValue(locator, value);
    }

    protected void verifyElementDoesNotHaveAttribute(WebElement el, String attribute) {
        boolean hasAttribute = el.getAttribute(attribute) != null;
        this.verifyFalse(hasAttribute, "Element has the " + attribute + " attribute!");
    }

    protected void verifyElementHasAttribute(WebElement element, String attribute) {
        boolean hasAttribute = element.getAttribute(attribute) != null;
        this.verifyTrue(hasAttribute, "Element did not have the " + attribute + " attribute!");
    }

    protected void verifyFalse(boolean condition, String failMsg) {
        this.sa.assertFalse(condition, failMsg);

        try {
            Assert.assertFalse(condition, failMsg);
        } catch (Error var4) {
        }

    }

    protected void verifyPartialText(WebElement el, String text) {
        String actualText = el.getText();
        boolean textContainsExpected = actualText.contains(text);
        this.verifyTrue(textContainsExpected, "Element did not contain " + text + "!");
    }

    protected void verifyRadioNotSelected(By locator) {
        boolean isSelected = this.driver().isSelected(locator);
        this.verifyFalse(isSelected, "Radio button was selected!");
    }

    protected void verifyRadioSelected(By locator) {
        boolean isSelected = this.driver().isSelected(locator);
        this.verifyTrue(isSelected, "Radio button was not selected!");
    }

    protected void verifyTrue(boolean condition, String failMsg) {
        this.sa.assertTrue(condition, failMsg);

        try {
            Assert.assertTrue(condition, failMsg);
        } catch (Error var4) {
        }

    }

    protected void verifyPossibleConditions(String msg, boolean... conditions) {
        boolean condition = false;
        boolean[] var4 = conditions;
        int var5 = conditions.length;

        for(int var6 = 0; var6 < var5; ++var6) {
            boolean condition2 = var4[var6];
            condition |= condition2;
        }

        this.verifyTrue(condition, msg);
    }

    protected void closeBrowser(Browser browser) {
        this.driver().close();
    }

    protected void tearDown(Browser browser) {
        this.closeBrowser(browser);
        this.checkForVerificationErrors();
    }

    protected void setup(Browser browser, String url) throws Exception {
        BaseWebDriver driver = new BaseWebDriver(browser);
        this.context().setDriver(driver);
        this.driver().launchBrowser(browser, url);
    }

    protected void clickIfExists(By locator) {
        this.driver().manage().timeouts().implicitlyWait(1L, TimeUnit.NANOSECONDS);

        try {
            WebElement el = this.driver().findElement(locator);
            this.click(el);
        } catch (NoSuchElementException var3) {
            throw var3;
        }

        this.driver().manage().timeouts().implicitlyWait((long) FrameworkProperties.getImplicitWait(), TimeUnit.SECONDS);
    }

    protected void clickWhileExists(By locator) {
        for(boolean flag = true; flag; flag = this.driver().isDisplayed(locator)) {
            this.click(locator);
        }

    }

    protected String getFirstSelectedOption(By locator) {
        Select selectedOption = new Select(this.findElement(locator));
        return selectedOption.getFirstSelectedOption().getText();
    }

    public void cleanup() {
        if (null != this.driver()) {
            this.driver().quit();
        }

    }
}

