package dev.tuxjsql.mysql;

import dev.tuxjsql.basic.response.BasicDBInsert;
import dev.tuxjsql.basic.sql.BasicInsertStatement;
import dev.tuxjsql.core.TuxJSQL;
import dev.tuxjsql.core.response.DBAction;
import dev.tuxjsql.core.response.DBInsert;

import java.sql.*;
import java.util.Map;

public class MysqlInsertStatement extends BasicInsertStatement {
    public MysqlInsertStatement(TuxJSQL tuxJSQL) {
        super(tuxJSQL);
    }

    @Override
    public DBAction<DBInsert> execute() {
        return new DBAction<>(this::doInsert, tuxJSQL);
    }

    private DBInsert doInsert() {
        DBInsert insert = null;
        StringBuilder columnsToInsert = new StringBuilder();
        StringBuilder question = new StringBuilder();
        for (String column : values.keySet()) {
            if (!columnsToInsert.toString().isEmpty()) {
                columnsToInsert.append(",");
                question.append(",");
            }
            columnsToInsert.append(column);
            question.append("?");
        }
        String query = String.format(Queries.INSERT.getString(), table.getName(), columnsToInsert.toString(), question.toString());
        TuxJSQL.getLogger().debug(query);
        Object[] values = this.values.values().toArray();
        try (Connection connection = tuxJSQL.getConnection(); PreparedStatement preparedStatement = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
            int i = 1;
            for (Object object : values) {
                preparedStatement.setObject(i, object);
                i++;
            }
            preparedStatement.executeUpdate();
            Object primaryKey = null;
            for (Map.Entry<String, Object> o : this.values.entrySet()) {
                if (table.getColumn(o.getKey()).primaryKey()) {
                    primaryKey = o.getValue();
                }
            }

            if (primaryKey == null) {
                try (ResultSet set = preparedStatement.getGeneratedKeys()) {
                    if (set != null && set.next()) {
                        primaryKey = set.getObject(1);
                    }
                }catch (SQLException e){
                    TuxJSQL.getLogger().error("Unable to get primaryKey for latest insert", e);
                }
            }
            insert = new BasicDBInsert(table, primaryKey);

        } catch (SQLException e) {
            TuxJSQL.getLogger().error("Unable to insert to table", e);
        }
        return insert;
    }
}
