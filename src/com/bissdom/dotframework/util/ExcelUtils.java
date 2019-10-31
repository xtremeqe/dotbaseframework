package com.bissdom.dotframework.util;

import jxl.Cell;
import jxl.Sheet;
import jxl.Workbook;
import jxl.read.biff.BiffException;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public abstract class ExcelUtils {
    public ExcelUtils() {
    }

    public static String getCellContents(String filePath, String sheetName, int column, int row) {
        Workbook wb = getWorkbook(filePath);
        Sheet sheet = getSheet(wb, sheetName);
        Cell cell = sheet.getCell(column, row);
        return cell.getContents();
    }

    public static ArrayList<String> getColumnContents(Sheet sheet, int column) {
        ArrayList<String> contents = new ArrayList();
        Cell[] cells = sheet.getColumn(column);
        Cell[] var4 = cells;
        int var5 = cells.length;

        for(int var6 = 0; var6 < var5; ++var6) {
            Cell c = var4[var6];
            contents.add(c.getContents());
        }

        return contents;
    }

    public static int getColumnCount(Sheet sheet) {
        return sheet.getColumns();
    }

    public static ArrayList<String> getRowContents(Sheet sheet, int row) {
        ArrayList<String> contents = new ArrayList();
        Cell[] cells = sheet.getRow(row);
        Cell[] var4 = cells;
        int var5 = cells.length;

        for(int var6 = 0; var6 < var5; ++var6) {
            Cell c = var4[var6];
            contents.add(c.getContents());
        }

        return contents;
    }

    public static int getRowCount(Sheet sheet) {
        return sheet.getRows();
    }

    public static Sheet getSheet(Workbook workbook, String sheetName) {
        Sheet sheet = workbook.getSheet(sheetName);
        return sheet;
    }

    public static Workbook getWorkbook(String filePath) {
        Workbook wb = null;
        File excelFile = new File(filePath);
        if (excelFile.exists()) {
            try {
                wb = Workbook.getWorkbook(excelFile);
            } catch (BiffException var4) {
                var4.printStackTrace();
            } catch (IOException var5) {
                var5.printStackTrace();
            }
        }

        return wb;
    }
}
