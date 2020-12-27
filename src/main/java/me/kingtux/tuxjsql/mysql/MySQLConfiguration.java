package me.kingtux.tuxjsql.mysql;

import me.kingtux.tuxjsql.core.Configuration;
import me.kingtux.tuxjsql.core.TuxJSQL;
import me.kingtux.tuxjsql.core.builders.SQLBuilder;
import me.kingtux.tuxjsql.core.connection.ConnectionSettings;
import me.kingtux.tuxjsql.core.tools.SimpleSupplier;
import org.apache.commons.lang3.tuple.Pair;

import java.util.Properties;

import static me.kingtux.tuxjsql.mysql.MysqlBuilder.JDBC_CLASS;
import static me.kingtux.tuxjsql.mysql.MysqlBuilder.URL;

public class MySQLConfiguration implements Configuration<MySQLConfiguration> {
    private Properties userProperties = new Properties();
    private int poolSize;
    private String username;
    private String password;
    private String host;
    private String db;
    private String otherOptions;

    @Override
    public Pair<ConnectionSettings, Properties> createConnection() {
        String url = String.format(URL, host, db);
        if (otherOptions != null) {
            url += "?" + userProperties.getProperty("url.other.options");
        }
        if (TuxJSQL.getLogger().isDebugEnabled())
            TuxJSQL.getLogger().debug(String.format("URL:%s", url));
        return Pair.of(new ConnectionSettings(JDBC_CLASS, url), userProperties);
    }


    @Override
    public Properties getUserProperties() {
        return userProperties;
    }

    @Override
    public MySQLConfiguration setUserProperties(Properties properties) {
        this.userProperties = properties;
        return this;
    }

    @Override
    public MySQLConfiguration loadFromProperties(Properties properties) {
        this.userProperties = properties;
        this.username = properties.getProperty("user");
        this.password = properties.getProperty("password");
        this.host = properties.getProperty("db.host");
        this.host = properties.getProperty("db.db");
        this.username = properties.getProperty("url.other.options");
        return this;
    }


    public String getUsername() {
        return username;
    }

    public MySQLConfiguration setUsername(String username) {
        this.username = username;
        return this;
    }

    public String getPassword() {
        return password;
    }

    public MySQLConfiguration setPassword(String password) {
        this.password = password;
        return this;
    }

    public String getHost() {
        return host;
    }

    public MySQLConfiguration setHost(String host) {
        this.host = host;
        return this;
    }

    public String getOtherOptions() {
        return otherOptions;
    }

    public MySQLConfiguration setOtherOptions(String otherOptions) {
        this.otherOptions = otherOptions;
        return this;
    }

    public String getDb() {
        return db;
    }

    public MySQLConfiguration setDb(String db) {
        this.db = db;
        return this;
    }

    @Override
    public MySQLConfiguration setThreadPoolSize(int i) {
        poolSize = i;
        return this;
    }

    @Override
    public SimpleSupplier<SQLBuilder> getSQLBuilder() {
        return MysqlBuilder::new;
    }

    @Override
    public int getThreadPoolSize() {
        return poolSize;
    }
}
