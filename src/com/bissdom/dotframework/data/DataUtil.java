package com.bissdom.dotframework.data;

import com.bissdom.dotframework.util.Item;

public final class DataUtil {
    public DataUtil() {
    }

    public static final DataUtil data() {
        return DataUtil.SingletonHolder.INSTANCE;
    }

    public Item getItem(String criteria) {
        return null;
    }

    private static class SingletonHolder {
        private static final DataUtil INSTANCE = new DataUtil();

        private SingletonHolder() {
        }
    }
}
