package dev.tuxjsql.mysql;

import dev.tuxjsql.basic.response.BasicDBUpdate;
import dev.tuxjsql.basic.sql.BasicUpdateStatement;
import dev.tuxjsql.core.TuxJSQL;
import dev.tuxjsql.core.response.DBAction;
import dev.tuxjsql.core.response.DBUpdate;

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
        String query = String.format(Queries.UPDATE.getString(), sqlTable.getName(), columnToUpdate, whereStatement.getQuery());
        values.addAll(Arrays.asList(whereStatement.getValues()));
        if(TuxJSQL.getLogger().isDebugEnabled())
            TuxJSQL.getLogger().debug(query);

        try (Connection connection = tuxJSQL.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            int i = 1;
            for (Object value : values) {
                preparedStatement.setObject(i, value);
            }
            dbUpdate = new BasicDBUpdate(sqlTable, preparedStatement.executeUpdate());
        } catch (SQLException e) {
            TuxJSQL.getLogger().error("Unable to update rows", e);
        }
        return dbUpdate;
    }
}
