package com.bissdom.dotframework.common;

import java.text.SimpleDateFormat;

public enum DateDisplayFormat {
    CSV_MONTH_DD_YYYY(new SimpleDateFormat("MMMM d, yyyy")),
    SLASHES_MM_DD_YYYY(new SimpleDateFormat("MM/dd/yyyy")),
    DASHES_YYYY_MM_DD(new SimpleDateFormat("yyyy-MM-dd"));

    public SimpleDateFormat sdf;

    private DateDisplayFormat(SimpleDateFormat f) {
        this.sdf = f;
    }
}
