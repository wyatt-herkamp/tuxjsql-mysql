package dev.tuxjsql.mysql;

import dev.tuxjsql.basic.sql.where.BasicSubWhereStatement;
import dev.tuxjsql.basic.sql.where.BasicWhereResponse;
import dev.tuxjsql.basic.sql.where.WhereUtils;
import dev.tuxjsql.core.TuxJSQL;

public class MysqlSubWhereStatement<T> extends BasicSubWhereStatement<T> {
    private BasicWhereResponse response;
    public MysqlSubWhereStatement(T and, TuxJSQL core) {
        super(and, core);
    }

    public MysqlSubWhereStatement(TuxJSQL core) {
        super(core);
    }

    @Override
    public String getQuery() {
        if(response==null){
            response = WhereUtils.doubleBuild(whereObjects.toArray(),table);
        }
        return response.getQuery();
    }

    @Override
    public Object[] getValues() {
        if(response==null){
            response = WhereUtils.doubleBuild(whereObjects.toArray(),table);
        }
        return response.getValues();
    }

}
