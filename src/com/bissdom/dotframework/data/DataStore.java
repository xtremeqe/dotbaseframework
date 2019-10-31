package com.bissdom.dotframework.data;

public class DataStore {
    public DataStore() {
    }

    public static final DataRepository data() {
        return DataStore.SingletonHolder.instance;
    }

    private static class SingletonHolder {
        private static final DataRepository instance = new MongoRepository();

        private SingletonHolder() {
        }
    }
}
