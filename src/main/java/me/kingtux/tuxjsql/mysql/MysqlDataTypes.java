package me.kingtux.tuxjsql.mysql;

import dev.tuxjsql.basic.sql.BasicDataTypes;
import dev.tuxjsql.core.sql.SQLDataType;

public enum MysqlDataTypes implements SQLDataType {
    TEXT(BasicDataTypes.TEXT),
    VARCHAR,
    TINYINT,
    MEDIUMINT,
    INT,
    BIGINT(BasicDataTypes.INTEGER),
    DOUBLE(BasicDataTypes.REAL);
    private BasicDataTypes types;

    MysqlDataTypes(BasicDataTypes types) {
        this.types = types;
    }

     MysqlDataTypes() {

    }

    @Override
    public String key() {
        return name();
    }

    public BasicDataTypes getTypes() {
        return types;
    }
}
