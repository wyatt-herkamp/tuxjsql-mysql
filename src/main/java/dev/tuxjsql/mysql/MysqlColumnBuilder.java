package dev.tuxjsql.mysql;

import dev.tuxjsql.basic.builders.BasicColumnBuilder;
import dev.tuxjsql.core.sql.SQLColumn;

public class MysqlColumnBuilder<T> extends BasicColumnBuilder<T> {
    public MysqlColumnBuilder(T andValue) {
        super(andValue);
    }

    public MysqlColumnBuilder() {
        super(null);
    }

    @Override
    public SQLColumn build() {
        return new MysqlColumn(getName(), getDefaultValue(), getDataTypeRules(), isNotNull(), isUnique(), isAutoIncrement(), isPrimaryKey(), getForeignColumn(), getTable(), getType());
    }
}
