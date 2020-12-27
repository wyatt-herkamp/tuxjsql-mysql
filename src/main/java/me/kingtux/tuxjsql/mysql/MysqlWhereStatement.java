package me.kingtux.tuxjsql.mysql;

import dev.tuxjsql.basic.sql.where.BasicWhereResponse;
import dev.tuxjsql.basic.sql.where.BasicWhereStatement;
import dev.tuxjsql.basic.sql.where.WhereUtils;
import dev.tuxjsql.core.TuxJSQL;

public class MysqlWhereStatement<T> extends BasicWhereStatement<T> {
    private BasicWhereResponse response;

    public MysqlWhereStatement(T and, TuxJSQL core) {
        super(and, core);
    }

    public MysqlWhereStatement(TuxJSQL core) {
        super(core);
    }

    @Override
    public String getQuery() {
        if (response == null) {
            response = WhereUtils.doubleBuild(whereObjects.toArray(), table);
        }
        return response.getQuery();
    }

    @Override
    public Object[] getValues() {
        if (response == null) {
            response = WhereUtils.doubleBuild(whereObjects.toArray(), table);
        }
        return response.getValues();
    }
}
