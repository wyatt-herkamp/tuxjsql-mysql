package me.kingtux.tuxjsql.mysql;

import me.kingtux.tuxjsql.basic.response.BasicDBSelect;
import me.kingtux.tuxjsql.basic.sql.select.BasicJoinStatement;
import me.kingtux.tuxjsql.basic.sql.select.BasicSelectStatement;
import me.kingtux.tuxjsql.basic.sql.where.BasicWhereStatement;
import me.kingtux.tuxjsql.basic.utils.BasicUtils;
import me.kingtux.tuxjsql.core.TuxJSQL;
import me.kingtux.tuxjsql.core.response.DBAction;
import me.kingtux.tuxjsql.core.response.DBSelect;
import me.kingtux.tuxjsql.core.sql.SQLColumn;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class MysqlSelectStatement extends BasicSelectStatement {


    public MysqlSelectStatement(TuxJSQL tuxJSQL) {
        super(tuxJSQL);
    }

    @Override
    public DBAction<DBSelect> execute() {
        return new DBAction<>(this::select, tuxJSQL);
    }

    private DBSelect select() {
        DBSelect dbSelect = null;
        StringBuilder columnBuilder = new StringBuilder();
        int i = 0;
        if (columns.isEmpty()) {
            columnBuilder.append("*");
        } else {
            for (SQLColumn column : this.columns) {
                if (i != 0) columnBuilder.append(",");

                columnBuilder.append(column.getTable().getName()).append(".").append(column.getName());
                i++;
            }
        }
        String select = String.format(Queries.SELECT.getString(), columnBuilder.toString(), table.getName());
        Object[] values = new Object[0];
        BasicJoinStatement joinStatement = (BasicJoinStatement) this.join;
        if (joinStatement.getJoinType() != null) {
            select += " " + String.format(Queries.JOIN.getString(), MysqlJoinTypes.getType(joinStatement.getJoinType()).getKey(), joinStatement.getTableTwo().getTable().getName(), table.getName(), joinStatement.getTableOneColumn(), joinStatement.getTableTwo().getName());
        }
        ((BasicWhereStatement) whereStatement).setTable(table);
        if (whereStatement.getValues().length != 0) {
            select = String.format("%s WHERE %s", select, whereStatement.getQuery());
            values = whereStatement.getValues();
        }

        TuxJSQL.getLogger().debug(select);
        try (Connection connection = tuxJSQL.getConnection(); PreparedStatement statement = connection.prepareStatement(select)) {
            i = 1;
            for (Object o : values) {
                statement.setObject(i++, o);
            }
            try(ResultSet set = statement.executeQuery()) {
                //If set is null return a fail
                if (set == null || set.isClosed()) return new BasicDBSelect(false, table);

                dbSelect = new BasicDBSelect(BasicUtils.resultSetToDBSelect(set,tuxJSQL), true, table);
            }catch (SQLException e){
                TuxJSQL.getLogger().error("Unable to select", e);
                return new BasicDBSelect(false, table);
            }
        } catch (SQLException e) {
            TuxJSQL.getLogger().error("Unable to select", e);
            return new BasicDBSelect(false, table);
        }
        return dbSelect;

    }
}
