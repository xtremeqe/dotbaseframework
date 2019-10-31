package com.bissdom.dotframework.data;

import java.util.Map;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;

public abstract class DataRepository {
    public DataRepository() {
    }

    public Item getItem(String criteria) {
        throw new UnsupportedOperationException();
    }

    public Item getItem(String project, String feature, String criteria, String environment, String tag, String tenantId) {
        throw new UnsupportedOperationException();
    }

    public Item getRandomItem() {
        throw new UnsupportedOperationException();
    }

    public void setUp() {
    }

    public Item getItem(String criteria, Class type) {
        throw new UnsupportedOperationException();
    }

    public Search getCriteria(String criteriaString) {
        ObjectMapper mapper = new ObjectMapper();
        Search criteria = null;

        try {
            criteria = (Search)mapper.readValue(criteriaString, Search.class);
        } catch (IOException var5) {
            var5.printStackTrace();
        }

        return criteria;
    }

    public Item getItem(Map<String, String> tags) {
        throw new UnsupportedOperationException();
    }
}
