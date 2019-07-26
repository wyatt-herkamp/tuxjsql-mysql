package dev.tuxjsql.mysql;

import dev.tuxjsql.basic.sql.BasicSQLColumn;
import dev.tuxjsql.core.TuxJSQL;
import dev.tuxjsql.core.sql.SQLColumn;
import dev.tuxjsql.core.sql.SQLDataType;
import dev.tuxjsql.core.sql.SQLTable;

import java.util.ArrayList;
import java.util.List;

public class MysqlColumn extends BasicSQLColumn {
    private static final String AUTOINCREMENT = " AUTO_INCREMENT";
    private static final String PRIMARY_KEY = " PRIMARY KEY";

    public MysqlColumn(String name, Object defaultValue, List<String> dataTypeRules, boolean notNull, boolean unique, boolean autoIncrement, boolean primaryKey, SQLColumn foreignKey, SQLTable table, SQLDataType type, TuxJSQL tuxJsql) {
        super(name, defaultValue, dataTypeRules, notNull, unique, autoIncrement, primaryKey, foreignKey, table, type, tuxJsql);
    }

    @Override
    public String build() {
        if (defaultValue != null && getDataType().key().equals("TEXT")) {
            TuxJSQL.getLogger().warn("Unable to use DEFAULT with TEXT on MySQL");
            defaultValue = null;
        }
        StringBuilder builder = new StringBuilder();
        builder.append("`").append(name).append("`");
        builder.append(" ").append(buildDataType());
        builder.append(primaryKey() ? PRIMARY_KEY : "");
        builder.append(autoIncrement() ? AUTOINCREMENT : "");
        if (!autoIncrement()) {
            builder.append(notNull() ? " NOT NULL" : "");
            builder.append(unique() ? " UNIQUE" : "");
        }
        if (defaultValue != null) {
            builder.append(" DEFAULT ");
            builder.append("'").append(defaultValue).append("'");
        }
        return builder.toString();
    }

    @Override
    public String foreignBuild() {
        return String.format(Queries.FOREIGN_VALUE.getString(), getName(), foreignKey().getTable().getName(), foreignKey().getName());
    }
}
