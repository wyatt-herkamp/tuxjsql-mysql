package dev.tuxjsql.mysql;

import dev.tuxjsql.basic.builders.BasicSQLBuilder;
import dev.tuxjsql.basic.sql.BasicDataTypes;
import dev.tuxjsql.basic.sql.select.BasicJoinStatement;
import dev.tuxjsql.core.TuxJSQL;
import dev.tuxjsql.core.builders.ColumnBuilder;
import dev.tuxjsql.core.builders.TableBuilder;
import dev.tuxjsql.core.connection.ConnectionProvider;
import dev.tuxjsql.core.connection.ConnectionSettings;
import dev.tuxjsql.core.sql.*;
import dev.tuxjsql.core.sql.select.JoinStatement;
import dev.tuxjsql.core.sql.select.SelectStatement;
import dev.tuxjsql.core.sql.where.SubWhereStatement;
import dev.tuxjsql.core.sql.where.WhereStatement;

import java.io.File;
import java.util.Properties;

public final class MysqlBuilder extends BasicSQLBuilder {
    public static final String URL = "jdbc:mysql://%1$s/%2$s?serverTimezone=UTC";
    public static final String JDBC_CLASS = "com.mysql.cj.jdbc.Driver";
    public static final SQLAction[] SUPPORTED_ACTIONS = {SQLAction.SELECT, SQLAction.INSERT, SQLAction.UPDATE, SQLAction.DELETE};

    @Override
    public TableBuilder createTable() {
        return new MysqlTableBuilder(tuxJSQL);
    }

    @Override
    public ColumnBuilder createColumn() {
        return new MysqlColumnBuilder();
    }

    @Override
    public WhereStatement createWhere() {
        return new MysqlWhereStatement(tuxJSQL);
    }

    @Override
    public SubWhereStatement createSubWhereStatement() {
        return new MysqlSubWhereStatement(tuxJSQL);
    }

    @Override
    public <T> WhereStatement<T> createWhere(T t) {
        return new MysqlWhereStatement<>(t, tuxJSQL);
    }

    @Override
    public <T> SubWhereStatement<T> createSubWhereStatement(T t) {
        return new MysqlSubWhereStatement<>(t, tuxJSQL);
    }

    @Override
    public SelectStatement createSelectStatement() {
        return new MysqlSelectStatement(tuxJSQL);
    }

    @Override
    public JoinStatement createJoinStatement(SelectStatement basicSelectStatement) {
        return new BasicJoinStatement(basicSelectStatement);
    }


    @Override
    public UpdateStatement createUpdateStatement() {
        return new MysqlUpdateStatement(tuxJSQL);
    }

    @Override
    public DeleteStatement createDeleteStatement() {
        return new MysqlDeleteStatement(tuxJSQL);
    }

    @Override
    public String name() {
        return "Mysql";
    }

    @Override
    public String jdbcClass() {
        return JDBC_CLASS;
    }

    @Override
    public SQLAction[] supportedActions() {
        return SUPPORTED_ACTIONS;
    }

    @Override
    public SQLDataType convertDataType(BasicDataTypes dataType) {
        for (MysqlDataTypes type : MysqlDataTypes.values()) {
            if (type.getTypes() == dataType) {
                return type;
            }
        }
        return null;
    }

    @Override
    public InsertStatement createInsertStatement() {
        return new MysqlInsertStatement(tuxJSQL);
    }

    @Override
    public void configureConnectionProvider(ConnectionProvider provider, Properties userProperties) {
        String url = String.format(URL, userProperties.getProperty("db.host"), userProperties.getProperty("db.db"));
        if (TuxJSQL.getLogger().isDebugEnabled())
            TuxJSQL.getLogger().debug(String.format("URL:%s", url));
        provider.setup(new ConnectionSettings(jdbcClass(), url), userProperties);
    }

    @Override
    public <T> ColumnBuilder<T> createColumn(T t) {
        return new MysqlColumnBuilder<>(t);
    }
}
