package com.bissdom.dotframework.data;

import org.bson.Document;

import java.util.Map;

public class Item {
    private Document document;

    public Item() {
    }

    public Document getJson() {
        return this.document;
    }

    public void setJson(Document json) {
        this.document = json;
    }

    public Map<String, Object> getData() {
        return this.as();
    }

    private Map<String, Object> as() {
        return this.document;
    }

    public String getJsonString() {
        return this.document.toString();
    }
}
