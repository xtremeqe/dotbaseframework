package com.bissdom.dotframework.data;

public class DummyData implements IData {
    public DummyData() {
    }

    public String getSampleData(String criteria) {
        return (new Item()).getJsonString();
    }
}
