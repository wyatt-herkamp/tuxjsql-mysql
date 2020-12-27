package me.kingtux.tuxjsql.mysql;

import dev.tuxjsql.basic.response.BasicDBDelete;
import dev.tuxjsql.basic.sql.BasicDeleteStatement;
import dev.tuxjsql.basic.sql.where.BasicWhereStatement;
import dev.tuxjsql.core.TuxJSQL;
import dev.tuxjsql.core.response.DBAction;
import dev.tuxjsql.core.response.DBDelete;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class MysqlDeleteStatement extends BasicDeleteStatement {
    public MysqlDeleteStatement(TuxJSQL tuxJSQL) {
        super(tuxJSQL);
    }

    @Override
    public DBAction<DBDelete> execute() {
        return new DBAction<>(this::dbDelete, tuxJSQL);
    }

    private DBDelete dbDelete() {
        ((BasicWhereStatement) whereStatement).setTable(table);
        DBDelete delete = null;
        String sql = String.format(Queries.DELETE.getString(), table.getName());
        if (whereStatement.getValues().length != 0) {
            sql += " " + String.format(Queries.WHERE.getString(), whereStatement.getQuery());
        }
        TuxJSQL.getLogger().debug(sql);
        try (Connection connection = tuxJSQL.getConnection()) {
            try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
                int i = 1;
                for (Object object : whereStatement.getValues()) {
                    preparedStatement.setObject(i++, object);
                }
                delete = new BasicDBDelete(table, preparedStatement.executeUpdate(), true);
            }
        } catch (SQLException e) {
            TuxJSQL.getLogger().error("Unable to execute delete query", e);
            return new BasicDBDelete(table, 0, false);
        }
        return delete;
    }
}
