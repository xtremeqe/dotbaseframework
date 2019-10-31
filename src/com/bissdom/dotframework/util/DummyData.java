package com.bissdom.dotframework.util;

import com.bissdom.dotframework.data.IData;

public class DummyData implements IData {
    public DummyData() {
    }

    public String getSampleData(String criteria) {
        return (new Item()).getJsonString();
    }
}
