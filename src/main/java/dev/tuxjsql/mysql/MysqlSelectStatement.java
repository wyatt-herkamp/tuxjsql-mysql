package dev.tuxjsql.mysql;

import dev.tuxjsql.basic.sql.select.BasicJoinStatement;
import dev.tuxjsql.basic.sql.select.BasicSelectStatement;
import dev.tuxjsql.basic.sql.where.BasicWhereStatement;
import dev.tuxjsql.basic.utils.BasicUtils;
import dev.tuxjsql.core.TuxJSQL;
import dev.tuxjsql.core.response.DBAction;
import dev.tuxjsql.core.response.DBSelect;
import dev.tuxjsql.core.sql.SQLColumn;

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
        for (SQLColumn column : this.columns) {
            if (i != 0) columnBuilder.append(",");

            columnBuilder.append(column.getTable().getName()).append(".").append(column.getName());
            i++;
        }
        String select = String.format(Queries.SELECT.getString(), columnBuilder.toString(), table.getName());
        Object[] values = new Object[0];
        BasicJoinStatement joinStatement = (BasicJoinStatement) this.join;
        if (joinStatement.getJoinType() != null) {
//Departments ON Students.DepartmentId = Departments.DepartmentId
            select += " " + String.format(Queries.JOIN.getString(), MysqlJoinTypes.getType(joinStatement.getJoinType()).getKey(), joinStatement.getTableTwo().getTable().getName(), table.getName(), joinStatement.getTableOneColumn(), joinStatement.getTableTwo().getName());
//TODO add join statement
            //PROBLEM I dont know how it works
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
                statement.setObject(i, o);
                i++;
            }
            ResultSet set = statement.executeQuery();
            dbSelect = BasicUtils.resultSetToDBSelect(set);
        } catch (SQLException e) {
            TuxJSQL.getLogger().error("Unable to select", e);
        }
        return dbSelect;

    }
}
