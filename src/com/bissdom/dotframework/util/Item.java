package com.bissdom.dotframework.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;

public class Item {
    private String className;
    private String jsonString;

    public String getClassName() {
        return this.className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public String getJsonString() {
        return this.jsonString;
    }

    public Item(String classNameParam) {
        this.setClassName(classNameParam);
    }

    public Item() {
        this("java.lang.Object");
    }

    public String getJson() {
        return this.jsonString;
    }

    public void setJsonString(String json) {
        this.jsonString = json;
    }

    private Object convert(String type) {
        ObjectMapper mapper = new ObjectMapper();
        Class classType = null;

        try {
            classType = Class.forName(type);
        } catch (ClassNotFoundException var5) {
            var5.printStackTrace();
        }

        if (null != classType) {
            try {
                return mapper.readValue(this.jsonString, classType);
            } catch (IOException var6) {
                var6.printStackTrace();
            }
        }

        return null;
    }
}
