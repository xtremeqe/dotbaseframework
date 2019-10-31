package com.bissdom.dotframework.db;

import com.google.gson.Gson;
import com.bissdom.dotframework.util.FrameworkProperties;
import com.microsoft.sqlserver.jdbc.SQLServerDataSource;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import javax.sql.DataSource;
import org.jooq.DSLContext;
import org.jooq.Record;
import org.jooq.Result;
import org.jooq.SQLDialect;
import org.jooq.impl.DSL;

public class DBManager {
    static DataSource ds;
    static DSLContext context;
    static Gson gson = new Gson();

    public DBManager() {
    }

    public <T> List<T> queryDB(String sqlString, Class<T> type) {
        List<T> objList = new ArrayList();
        List<String> jsonList = this.executeQuery(sqlString);
        Iterator var5 = jsonList.iterator();

        while(var5.hasNext()) {
            String o = (String)var5.next();
            objList.add(gson.fromJson(o, type));
        }

        return objList;
    }

    public List<String> executeQuery(String sqlString) {
        Result<Record> result = context.fetch(sqlString);
        List<String> list = new ArrayList();
        result.stream().forEach((i) -> {
            list.add(gson.toJson(i.intoMap()));
        });
        return list;
    }

    public void init() throws SQLException {
        context = DSL.using(getDataSource(), SQLDialect.DEFAULT);
    }

    public static void cleanUp() throws Exception {
    }

    public static DataSource getDataSource() {
        return getSQLDataSource();
    }

    public static DataSource getSQLDataSource() {
        SQLServerDataSource sds = new SQLServerDataSource();
        sds.setURL(FrameworkProperties.getProperty("db.url"));
        if ("integrated".equalsIgnoreCase(FrameworkProperties.getProperty("db.authenticationType"))) {
            sds.setIntegratedSecurity(true);
        } else {
            sds.setUser(FrameworkProperties.getProperty("db.username"));
            sds.setPassword(FrameworkProperties.getProperty("db.password"));
        }

        return sds;
    }
}

