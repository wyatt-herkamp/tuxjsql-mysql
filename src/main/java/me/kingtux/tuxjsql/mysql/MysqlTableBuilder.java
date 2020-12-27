package me.kingtux.tuxjsql.mysql;

import dev.tuxjsql.basic.builders.BasicTableBuilder;
import dev.tuxjsql.core.TuxJSQL;
import dev.tuxjsql.core.builders.ColumnBuilder;
import dev.tuxjsql.core.sql.SQLTable;

import java.util.stream.Collectors;

public class MysqlTableBuilder extends BasicTableBuilder {
    public MysqlTableBuilder(TuxJSQL jsql) {
        super(jsql);
    }

    @Override
    public SQLTable createTable() {
        MysqlTable table = new MysqlTable(getJsql(), getName(), getColumnBuilders().stream().map(ColumnBuilder::build).collect(Collectors.toList()));
        getJsql().getExecutor().execute(table::prepareTable);
        table.prepareTable();
        getJsql().addTable(table);
        return table;
    }
}
