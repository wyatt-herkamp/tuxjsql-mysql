package me.kingtux.tuxjsql.mysql;

import me.kingtux.tuxjsql.basic.response.BasicDBUpdate;
import me.kingtux.tuxjsql.basic.sql.BasicUpdateStatement;
import me.kingtux.tuxjsql.basic.sql.where.BasicWhereStatement;
import me.kingtux.tuxjsql.core.TuxJSQL;
import me.kingtux.tuxjsql.core.response.DBAction;
import me.kingtux.tuxjsql.core.response.DBUpdate;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MysqlUpdateStatement extends BasicUpdateStatement {
    public MysqlUpdateStatement(TuxJSQL tuxJSQL) {
        super(tuxJSQL);
    }

    @Override
    public DBAction<DBUpdate> execute() {
        return new DBAction<>(this::dbUpdate, tuxJSQL);
    }

    private DBUpdate dbUpdate() {
        ((BasicWhereStatement) whereStatement).setTable(sqlTable);

        DBUpdate dbUpdate = null;
        List<String> columns = new ArrayList<>();
        List<Object> values = new ArrayList<>();
        this.values.forEach((s, o) -> {
            columns.add(s);
            values.add(o);
        });
        StringBuilder columnToUpdate = new StringBuilder();
        for (String column : columns) {
            if (!columnToUpdate.toString().isEmpty()) {
                columnToUpdate.append(",");
            }
            columnToUpdate.append("`").append(column).append("`").append("=?");
        }
        String query = String.format(Queries.UPDATE.getString(), sqlTable.getName(), columnToUpdate);
        if(whereStatement.getValues().length!=0){
            query+=" "+ String.format(Queries.WHERE.getString(), whereStatement.getQuery());
            values.addAll(Arrays.asList(whereStatement.getValues()));

        }
        if(TuxJSQL.getLogger().isDebugEnabled())
            TuxJSQL.getLogger().debug(query);

        try (Connection connection = tuxJSQL.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            int i = 1;
            for (Object value : values) {
                preparedStatement.setObject(i++, value);
            }
            dbUpdate = new BasicDBUpdate(sqlTable, preparedStatement.executeUpdate(),true);
        } catch (SQLException e) {
            TuxJSQL.getLogger().error("Unable to update rows", e);
            return new BasicDBUpdate(sqlTable, 0, false);
        }
        return dbUpdate;
    }
}
