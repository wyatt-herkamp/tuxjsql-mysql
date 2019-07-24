package dev.tuxjsql.mysql;

import dev.tuxjsql.basic.response.BasicDBDelete;
import dev.tuxjsql.basic.sql.BasicDeleteStatement;
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

    DBDelete dbDelete() {
        DBDelete delete = null;
        String sql = String.format(Queries.DELETE.getString(), table.getName(), whereStatement.getQuery());
        try (Connection connection = tuxJSQL.getConnection()) {
            try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
                int i = 1;
                for (Object object : whereStatement.getValues()) {
                    preparedStatement.setObject(i, object);
                    i++;
                }
                delete = new BasicDBDelete(table, preparedStatement.executeUpdate());
            }
        } catch (SQLException e) {
            TuxJSQL.getLogger().error("Unable to execute delete query", e);
        }
        return delete;
    }
}
