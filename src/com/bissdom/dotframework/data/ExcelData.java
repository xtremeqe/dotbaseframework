package com.bissdom.dotframework.data;

import java.lang.reflect.Field;

public class ExcelData implements IData {
    public ExcelData() {
    }

    protected Field[] getDeclaredFields() {
        return this.getClass().getDeclaredFields();
    }

    public void setField(int fieldNum, String value) {
        Field[] declaredFields = this.getDeclaredFields();

        try {
            declaredFields[fieldNum].set(this, value);
        } catch (IllegalArgumentException var5) {
            var5.printStackTrace();
        } catch (IllegalAccessException var6) {
            var6.printStackTrace();
        }

    }

    public String toString(String name) {
        StringBuilder sb = new StringBuilder(name);
        Field[] declaredFields = this.getDeclaredFields();
        Field[] var4 = declaredFields;
        int var5 = declaredFields.length;

        for(int var6 = 0; var6 < var5; ++var6) {
            Field i = var4[var6];

            try {
                sb.append(i.getName() + " = " + i.get(this).toString() + "; ");
            } catch (IllegalArgumentException var9) {
                var9.printStackTrace();
            } catch (IllegalAccessException var10) {
                var10.printStackTrace();
            }
        }

        return sb.toString();
    }

    public String getSampleData(String criteria) {
        return null;
    }
}