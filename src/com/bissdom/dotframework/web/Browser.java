package com.bissdom.dotframework.web;

public enum Browser {
    IE("internet explorer", "Internet Explorer"),
    FIREFOX("firefox", "Firefox"),
    CHROME("chrome", "Chrome"),
    CHROMELESS("chromeless", "Headless Chrome"),
    HTMLUNIT("", "HTML Unit"),
    SAFARI("safari", "Safari"),
    PHANTOMJS("phantomjs", "PhantomJS");

    public final String name;
    private final String toString;

    private Browser(String n, String toString) {
        this.name = n;
        this.toString = toString;
    }

    public String toString() {
        return this.toString;
    }

    public String getBrowserValue() {
        return this.name;
    }
}