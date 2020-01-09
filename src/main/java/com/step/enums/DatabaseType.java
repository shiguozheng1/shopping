package com.step.enums;

/**
 * Created by zhushubin  on 2019-11-21.
 *  数据库类型枚举
 * email:604580436@qq.com
 * * @since 3.0
 */

import java.util.Arrays;
import java.util.Objects;
import java.util.function.Predicate;


public enum DatabaseType {
    unknown(null, null, null, String::isEmpty),
    mysql("com.mysql.jdbc.Driver", "com.mysql.jdbc.jdbc2.optional.MysqlXADataSource", "select 1", createUrlPredicate("mysql")),
    h2("org.h2.Driver", "org.h2.jdbcx.JdbcDataSource", "select 1", createUrlPredicate("h2")),
    oracle("oracle.jdbc.driver.OracleDriver", "oracle.jdbc.xa.client.OracleXADataSource", "select 1 from dual", createUrlPredicate("oracle")),
    jtds_sqlserver("net.sourceforge.jtds.jdbc.Driver", "net.sourceforge.jtds.jdbcx.JtdsDataSource", "select 1 t", createUrlPredicate("jtds:sqlserver")),
    sqlserver("com.microsoft.sqlserver.jdbc.SQLServerDriver", "com.microsoft.sqlserver.jdbc.SQLServerXADataSource", "select 1 t", createUrlPredicate("sqlserver")),
    //beta
    postgresql("org.postgresql.Driver", "org.postgresql.xa.PGXADataSource", "select 1 ", createUrlPredicate("postgresql"));


    static Predicate<String> createUrlPredicate(String name) {
        return url -> {
            String urlWithoutPrefix = url.substring("jdbc".length()).toLowerCase();
            String prefix = ":" + name.toLowerCase() + ":";
            return urlWithoutPrefix.startsWith(prefix);
        };
    }

    DatabaseType(String driverClassName, String xaDataSourceClassName, String testQuery, Predicate<String> urlPredicate) {
        this.driverClassName = driverClassName;
        this.testQuery = testQuery;
        this.xaDataSourceClassName = xaDataSourceClassName;
        this.urlPredicate = urlPredicate;
    }

    private final String testQuery;

    private final String driverClassName;

    private final String xaDataSourceClassName;

    private final Predicate<String> urlPredicate;

    public String getDriverClassName() {
        return driverClassName;
    }

    public String getXaDataSourceClassName() {
        return xaDataSourceClassName;
    }

    public String getTestQuery() {
        return testQuery;
    }

    public static DatabaseType fromJdbcUrl(String url) {
        if (Objects.nonNull(url)) {
            return Arrays.stream(values()).filter(type -> type.urlPredicate.test(url)).findFirst().orElse(unknown);
        }
        return unknown;
    }
}
