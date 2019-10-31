package com.bissdom.dotframework.common;

import com.bissdom.dotframework.data.DataRepository;
import com.bissdom.dotframework.data.DataStore;
import com.bissdom.dotframework.db.DBManager;
import com.bissdom.dotframework.util.LogResult;
import com.bissdom.dotframework.util.LogUtil;
import com.bissdom.dotframework.web.BaseWebDriver;
import cucumber.api.Scenario;
import java.util.HashMap;
import java.util.Map;
import java.util.Stack;
import java.util.stream.Collectors;

public class ScenarioContext {
    protected Scenario scenario = null;
    protected String scenarioRunID = null;
    protected Map<String, Object> context = null;
    protected Stack<ResultNote> notes = null;
    protected DataRepository data = null;
    protected DBManager db = null;
    protected LogUtil log = null;
    protected BaseWebDriver driver = null;
    protected Map<String, TestBase> baseMap = new HashMap();

    public Map<String, TestBase> getBaseMap() {
        return this.baseMap;
    }

    public void register(TestBase base) {
        this.baseMap.put(String.valueOf(base.hashCode()), base);
    }

    protected void setBaseMap(Map<String, TestBase> baseMap) {
        this.baseMap = baseMap;
    }

    public BaseWebDriver getDriver() {
        return this.driver;
    }

    public void setDriver(BaseWebDriver driver) {
        this.driver = driver;
    }

    protected DataRepository getData() {
        return this.data;
    }

    protected void setData(DataRepository data) {
        this.data = data;
    }

    public DBManager getDB() {
        return this.db;
    }

    public void setDB(DBManager dbManager) {
        this.db = dbManager;
    }

    protected LogUtil getLog() {
        return this.log;
    }

    protected void setLog(LogUtil log) {
        this.log = log;
    }

    public Scenario getScenario() {
        return this.scenario;
    }

    public void setScenario(Scenario scenario) {
        this.scenario = scenario;
    }

    public Object get(String name) {
        return this.context.get(name);
    }

    public Object getTestData(String name) {
        return this.context.get(name);
    }

    public void put(String name, String obj) {
        this.context.put(name, obj);
    }

    public void put(String name, Object obj) {
        this.context.put(name, obj);
    }

    public ScenarioContext() {
        this.context = new HashMap();
        this.notes = new Stack();
    }

    public void reset() {
        this.context.clear();
    }

    protected Map<String, Object> getContext() {
        return this.context;
    }

    public void markScenarioStart() {
        this.getMap().put("startTime", String.valueOf(System.currentTimeMillis()));
    }

    public void markScenario_Start() {
        this.getMap().put("start_Time", String.valueOf(System.currentTimeMillis()));
    }

    public void setTestData(Map<String, Object> dataMap) {
        this.context.putAll(dataMap);
    }

    public void addTestData(String key, Object val) {
        this.context.put(key, val);
    }

    public void markScenarioEnd() {
        this.getMap().put("endTime", String.valueOf(System.currentTimeMillis()));
    }

    public void markScenario_End() {
        this.getMap().put("end_Time", String.valueOf(System.currentTimeMillis()));
    }

    public void addNote(LogResult key, String note) {
        this.notes.add(new ResultNote(key, note));
    }

    public String getNotes() {
        return (String)this.notes.stream().map(Object::toString).collect(Collectors.joining(","));
    }

    public ResultNote getScenarioResult() {
        return !this.notes.isEmpty() ? (ResultNote)this.notes.peek() : new ResultNote(LogResult.INFO, "Unknown");
    }

    public Map<String, Object> getMap() {
        return this.context;
    }

    protected void setLogAndData() throws Exception {
        if (null == this.log) {
            LogUtil logger = LogUtil.withContext(this);
            this.log = logger;
        }

        if (null == this.data) {
            DataRepository dataRepo = DataStore.data();
            this.data = dataRepo;
        }

        if (null == this.db) {
            this.db = new DBManager();
            this.db.init();
        }

    }
}