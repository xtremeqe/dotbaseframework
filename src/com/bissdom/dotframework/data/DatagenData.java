package com.bissdom.dotframework.data;

public class DatagenData implements IData {
    public DatagenData() {
    }

    public String getSampleData(String criteria) {
        Item item = this.getData(criteria);
        String jsonData = item.getJsonString();
        return jsonData;
    }

    private Item getData(String criteria) {
        return new Item();
    }
}

