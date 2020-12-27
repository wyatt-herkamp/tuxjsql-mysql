package me.kingtux.tuxjsql.mysql;

import me.kingtux.tuxjsql.basic.sql.where.BasicWhereResponse;
import me.kingtux.tuxjsql.basic.sql.where.BasicWhereStatement;
import me.kingtux.tuxjsql.basic.sql.where.WhereUtils;
import me.kingtux.tuxjsql.core.TuxJSQL;

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
