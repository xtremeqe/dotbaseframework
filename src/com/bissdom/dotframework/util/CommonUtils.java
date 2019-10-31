package com.bissdom.dotframework.util;

import com.bissdom.dotframework.common.DateDisplayFormat;
import java.awt.AWTException;
import java.awt.HeadlessException;
import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.StringReader;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Properties;
import java.util.Random;
import javax.imageio.ImageIO;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;
import jxl.Cell;
import jxl.Sheet;
import jxl.Workbook;
import org.openqa.selenium.By;
import org.testng.Assert;
import org.w3c.dom.Document;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

public abstract class CommonUtils {
    private static String locatorDelimter = "~";

    public CommonUtils() {
    }

    public static void assertAnyConditions(String msg, boolean... conditions) {
        boolean condition = false;
        boolean[] var3 = conditions;
        int var4 = conditions.length;

        for(int var5 = 0; var5 < var4; ++var5) {
            boolean condition2 = var3[var5];
            condition |= condition2;
        }

        Assert.assertTrue(condition, msg);
    }

    public static By buildDynamicLocator(String byLocatorAsString, String base, String value) {
        StringBuilder sb = new StringBuilder(base);
        sb.append(value);
        String locator = sb.toString();
        return convertStringToBy(byLocatorAsString, locator);
    }

    public static By buildDynamicLocator(String byLocatorAsString, String prefix, String value, String suffix) {
        StringBuilder sb = new StringBuilder(prefix);
        sb.append(value);
        sb.append(suffix);
        String locator = sb.toString();
        return convertStringToBy(byLocatorAsString, locator);
    }

    public static By convertStringLocatorToByLocator(String stringLocator) {
        String[] divide = stringLocator.split(locatorDelimter);
        String locatorType = divide[0];
        String locator = divide[1];
        return convertStringToBy(locatorType, locator);
    }

    public static By convertStringToBy(String byLocatorAsString, String value) {
        By locator = null;
        if (byLocatorAsString.equals("id")) {
            locator = By.id(value);
        } else if (byLocatorAsString.equals("name")) {
            locator = By.name(value);
        } else if (byLocatorAsString.equals("linkText")) {
            locator = By.linkText(value);
        } else if (byLocatorAsString.equals("partialLinkText")) {
            locator = By.partialLinkText(value);
        } else if (byLocatorAsString.equals("cssSelector")) {
            locator = By.cssSelector(value);
        } else if (byLocatorAsString.equals("className")) {
            locator = By.className(value);
        } else if (byLocatorAsString.equals("tagName")) {
            locator = By.tagName(value);
        } else {
            if (!byLocatorAsString.equals("xpath")) {
                throw new IllegalArgumentException(byLocatorAsString + locatorDelimter + value + " is not a valid locator!");
            }

            locator = By.xpath(value);
        }

        return locator;
    }

    public static int convertStringToInteger(String integerValue) {
        boolean var1 = false;

        try {
            int retIntegerValue = Integer.parseInt(integerValue);
            return retIntegerValue;
        } catch (NumberFormatException var3) {
            throw var3;
        }
    }

    public static String formatDate(DateDisplayFormat f, Date date) {
        String dateAsString = f.sdf.format(date);
        return dateAsString;
    }

    public static String generateAlpha(int length) {
        Random random = new Random();
        StringBuilder alpha = new StringBuilder();
        String alphabet = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";

        for(int i = 0; i < length; ++i) {
            alpha.append(alphabet.charAt(random.nextInt(52)));
        }

        String s = alpha.toString();
        return s;
    }

    public static String generateAlphaNumeric(int alphaLength, int numericLength) {
        StringBuilder alphaNumeric = new StringBuilder();
        String alpha = generateAlpha(alphaLength);
        alphaNumeric.append(alpha);
        int numeric = generateNumeric(numericLength);
        alphaNumeric.append(numeric);
        String s = alphaNumeric.toString();
        return s;
    }

    public static String generateEmail(int length, String email) {
        String s = generateAlpha(length) + email;
        return s;
    }

    public static int generateNumeric(int length) {
        int numericValue = false;
        StringBuilder numeric = new StringBuilder();
        Random random = new Random();

        for(int i = 0; i < length; ++i) {
            numeric.append(random.nextInt(9) + 1);
        }

        String s = numeric.toString();
        int numericValue = convertStringToInteger(s);
        return numericValue;
    }

    public static String getCurrentMonth() {
        Calendar c = Calendar.getInstance();
        String month = (new SimpleDateFormat("MMM")).format(c.getTime());
        return month;
    }

    public static int getCurrentYear() {
        Calendar c = Calendar.getInstance();
        String s = (new SimpleDateFormat("yyyy")).format(c.getTime());
        int year = Integer.parseInt(s);
        return year;
    }

    public static Document getDocObject(String XMLString) throws IOException {
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();

        try {
            dbf = DocumentBuilderFactory.newInstance();
            DocumentBuilder db = dbf.newDocumentBuilder();
            InputSource is = new InputSource();
            is.setCharacterStream(new StringReader(XMLString));
            Document doc = db.parse(is);
            doc.getDocumentElement().normalize();
            return doc;
        } catch (ParserConfigurationException var5) {
            var5.printStackTrace();
        } catch (SAXException var6) {
            var6.printStackTrace();
        }

        return null;
    }

    public static List<String> getNodeValues(Document doc, String xpath) throws TransformerException {
        List<String> sList = new LinkedList();
        return sList;
    }

    public static Object[][] getTestData(String dataFilePath, String dataSheetName) throws Exception {
        Object[][] ret = (Object[][])null;

        try {
            Workbook wb = readExcelWorkbook(dataFilePath);
            ret = readTestData(wb, dataSheetName);
            return ret;
        } catch (Exception var4) {
            throw var4;
        }
    }

    public static String getTimeInHMS() {
        Calendar c = Calendar.getInstance();
        StringBuilder sb = new StringBuilder();
        int hour = c.get(11);
        boolean isMilitaryTime = hour > 12;
        if (isMilitaryTime) {
            hour -= 12;
        }

        String h = String.format("%02d", hour);
        String m = String.format("%02d", c.get(12));
        String s = String.format("%02d", c.get(13));
        sb.append(h + ":" + m + ":" + s);
        sb.append(isMilitaryTime ? "pm" : "am");
        return sb.toString();
    }

    public static String getTimeStamp() {
        StringBuilder sb = new StringBuilder();
        Calendar c = Calendar.getInstance();
        String delimiter = "/";
        int month = c.get(2) + 1;
        int date = c.get(5);
        int year = c.get(1);
        sb.append(month + delimiter + date + delimiter + year);
        sb.append("_");
        delimiter = "-";
        int hour = c.get(11);
        if (hour > 12) {
            hour -= 12;
        }

        int minute = c.get(12);
        String min;
        if (minute < 10) {
            min = String.format("%02d", minute);
        } else {
            min = Integer.toString(minute);
        }

        int second = c.get(13);
        String sec;
        if (second < 10) {
            sec = String.format("%02d", second);
        } else {
            sec = Integer.toString(second);
        }

        sb.append(hour + delimiter + min + delimiter + sec);
        String text = sb.toString();
        return text;
    }

    private static String nextToken(String cellVal) {
        return cellVal.substring(0, cellVal.indexOf("_"));
    }

    public static String[] parseAndFormatDates(DateDisplayFormat f, String... dates) throws ParseException {
        String[] parsedAndFormattedDates = new String[dates.length];
        Date[] d = new Date[dates.length];

        int i;
        for(i = 0; i < dates.length; ++i) {
            dates[i] = dates[i].replace(" 00:00:00", "");
            Date parseDate = parseDate(dates[i]);
            d[i] = parseDate;
        }

        Arrays.sort(d);

        for(i = 0; i < d.length; ++i) {
            parsedAndFormattedDates[i] = formatDate(f, d[i]);
        }

        return parsedAndFormattedDates;
    }

    public static String parseCurrentDate(String cellVal) {
        String date = null;
        if (cellVal.startsWith("currentMonth")) {
            date = getCurrentMonth();
        } else if (cellVal.startsWith("currentYear")) {
            int currentYear = getCurrentYear();
            cellVal = stripUnderscore(cellVal, "currentYear");
            String op = nextToken(cellVal);
            if (op.equals("+")) {
                cellVal = stripUnderscore(cellVal, "+");
                int intValue = convertStringToInteger(cellVal);
                date = String.valueOf(currentYear + intValue);
            }
        }

        return date;
    }

    public static Date parseDate(String date) throws ParseException {
        SimpleDateFormat sdf = DateDisplayFormat.DASHES_YYYY_MM_DD.sdf;
        Date parse = sdf.parse(date);
        return parse;
    }

    public static String parseRandomString(String cellVal) {
        String randomStringVal = null;
        String extractedLength = null;
        int lengthOfNumber = false;
        int lengthOfAlpha = false;
        int lengthOfEmail = false;
        cellVal = cellVal.substring("randomString_".length());
        int lengthOfNumber;
        if (cellVal.startsWith("num")) {
            int generatedNum = false;
            extractedLength = cellVal.substring("num_".length());
            lengthOfNumber = convertStringToInteger(extractedLength);
            int generatedNum = generateNumeric(lengthOfNumber);
            randomStringVal = Integer.toString(generatedNum);
        } else {
            int lengthOfAlpha;
            if (cellVal.startsWith("alpha") && cellVal.indexOf("Num") == -1) {
                extractedLength = cellVal.substring("alpha_".length());
                lengthOfAlpha = convertStringToInteger(extractedLength);
                randomStringVal = generateAlpha(lengthOfAlpha);
            } else {
                String extractedEmailAddress;
                String extractedAlphaEmailLength;
                if (cellVal.startsWith("alphaNum")) {
                    extractedAlphaEmailLength = null;
                    extractedEmailAddress = null;
                    String alphaNumCombo = cellVal.substring("alphaNum_".length());
                    extractedAlphaEmailLength = alphaNumCombo.substring(0, alphaNumCombo.indexOf("_"));
                    extractedEmailAddress = alphaNumCombo.substring(alphaNumCombo.indexOf("_") + 1);
                    lengthOfAlpha = convertStringToInteger(extractedAlphaEmailLength);
                    lengthOfNumber = convertStringToInteger(extractedEmailAddress);
                    randomStringVal = generateAlphaNumeric(lengthOfAlpha, lengthOfNumber);
                } else if (cellVal.startsWith("email")) {
                    extractedAlphaEmailLength = null;
                    extractedEmailAddress = null;
                    extractedAlphaEmailLength = cellVal.substring("email_".length(), cellVal.lastIndexOf("_"));
                    extractedEmailAddress = cellVal.substring(cellVal.lastIndexOf("_") + 1);
                    int lengthOfEmail = convertStringToInteger(extractedAlphaEmailLength);
                    randomStringVal = generateEmail(lengthOfEmail, extractedEmailAddress);
                }
            }
        }

        return randomStringVal;
    }

    public static Workbook readExcelWorkbook(String filePath) throws Exception {
        Workbook ret = null;

        try {
            File excelFile = new File(filePath);
            if (excelFile.exists()) {
                ret = Workbook.getWorkbook(excelFile);
            }

            return ret;
        } catch (Exception var3) {
            throw var3;
        }
    }

    public static String readFile(File file) throws IOException {
        StringBuilder sb = new StringBuilder();
        BufferedReader br = null;

        try {
            br = new BufferedReader(new FileReader(file));
            String line = br.readLine();

            while(line != null) {
                sb.append(line);
                line = br.readLine();
                if (line != null) {
                    sb.append('\n');
                }
            }
        } catch (IOException var11) {
            var11.printStackTrace();
            throw var11;
        } finally {
            try {
                br.close();
            } catch (IOException var10) {
                var10.printStackTrace();
                throw var10;
            }
        }

        return sb.toString();
    }

    public static Properties readPropertyFile(String path) throws IOException {
        Properties prop = null;

        try {
            prop = new Properties();
            prop.load(new FileInputStream(path));
            return prop;
        } catch (IOException var3) {
            throw var3;
        }
    }

    private static Object[][] readTestData(Workbook wb, String sheetName) {
        String[][] ret = (String[][])null;
        boolean var4 = false;

        try {
            Sheet currentSheet = wb.getSheet(sheetName);
            int indexRowsCount = currentSheet.getColumn(0).length - 1;
            int columnsCount = currentSheet.getColumns() - 1;
            ret = new String[indexRowsCount][columnsCount];

            for(int i = 1; i <= indexRowsCount; ++i) {
                Cell[] currentRow = currentSheet.getRow(i);

                for(int j = 1; j <= columnsCount; ++j) {
                    String cellVal = currentRow[j].getContents();
                    if (cellVal == null) {
                        cellVal = "";
                    } else {
                        cellVal = cellVal.trim();
                    }

                    if (cellVal.startsWith("randomString")) {
                        cellVal = parseRandomString(cellVal);
                    }

                    if (cellVal.startsWith("current")) {
                        cellVal = parseCurrentDate(cellVal);
                    }

                    ret[i - 1][j - 1] = cellVal;
                }
            }

            return ret;
        } catch (Exception var10) {
            throw var10;
        }
    }

    private static String stripUnderscore(String cellVal, String prevToken) {
        cellVal = cellVal.substring(prevToken.length());
        cellVal = cellVal.substring(1);
        return cellVal;
    }

    public void takeEntireScreenshot(String filePath) {
        try {
            BufferedImage image = (new Robot()).createScreenCapture(new Rectangle(Toolkit.getDefaultToolkit().getScreenSize()));
            ImageIO.write(image, "png", new File(filePath));
        } catch (HeadlessException var3) {
            var3.printStackTrace();
        } catch (AWTException var4) {
            var4.printStackTrace();
        } catch (IOException var5) {
            var5.printStackTrace();
        }

    }
}

