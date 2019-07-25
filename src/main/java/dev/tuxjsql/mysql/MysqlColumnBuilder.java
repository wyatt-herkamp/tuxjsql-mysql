package dev.tuxjsql.mysql;

import dev.tuxjsql.basic.builders.BasicColumnBuilder;
import dev.tuxjsql.core.TuxJSQL;
import dev.tuxjsql.core.sql.SQLColumn;

public class MysqlColumnBuilder<T> extends BasicColumnBuilder<T> {
    public MysqlColumnBuilder(TuxJSQL tuxJSQL, T andValue) {
        super(tuxJSQL,andValue);
    }

    public MysqlColumnBuilder(TuxJSQL tuxJSQL) {
        this(tuxJSQL,null);
    }

    @Override
    public SQLColumn build() {
        return new MysqlColumn(getName(), getDefaultValue(), getDataTypeRules(), isNotNull(), isUnique(), isAutoIncrement(), isPrimaryKey(), getForeignColumn(), getTable(), getType(),tuxJSQL);
    }
}
