import dev.tuxjsql.basic.sql.BasicDataTypes;
import dev.tuxjsql.core.TuxJSQL;
import dev.tuxjsql.core.TuxJSQLBuilder;
import dev.tuxjsql.core.response.DBAction;
import dev.tuxjsql.core.response.DBInsert;
import dev.tuxjsql.core.response.DBRow;
import dev.tuxjsql.core.response.DBSelect;
import dev.tuxjsql.core.sql.SQLTable;
import dev.tuxjsql.core.sql.select.JoinType;
import dev.tuxjsql.core.sql.select.SelectStatement;
import dev.tuxjsql.core.sql.where.WhereStatement;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.math.BigInteger;
import java.util.Arrays;
import java.util.Properties;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class TestMain {
    @Test
    public void main() {

        Properties properties = getLocalProperties();
        properties.setProperty("db.type", "dev.tuxjsql.mysql.MysqlBuilder");

        TuxJSQL tuxJSQL = TuxJSQLBuilder.create(properties);
        SQLTable table = tuxJSQL.createTable().setName("test").addColumn().primaryKey().autoIncrement().name("id").setDataType(BasicDataTypes.INTEGER).and().
                addColumn(cb -> {
                    cb.setDataType(BasicDataTypes.TEXT).name("name");
                }).createTable();
        SQLTable tabletwo = tuxJSQL.createTable().setName("two").addColumn().primaryKey().autoIncrement().name("id").setDataType(BasicDataTypes.INTEGER).and().addColumn(cb -> {
            cb.setDataType(BasicDataTypes.TEXT).name("name");
        }).addColumn().name("tableone").setDataType(BasicDataTypes.INTEGER).foreignColumn(table.getColumn("id")).and().createTable();
        System.out.println(table.getName());
        DBAction<DBInsert> dbInsert = table.insert().value("name", "bobby").execute();
        //DBInsert dbInsert1  = dbInsert.complete();
        //System.out.println(dbInsert1.primaryKey());
        dbInsert.queue(dbInsert1 -> assertTrue(((BigInteger) dbInsert1.primaryKey()).intValue() != 0));

        DBSelect select = table.select().column("id").column("name").where().start("id", "=", 1).and().execute().complete();
        System.out.println(select.numberOfRows());
        System.out.println(select.first().getRow("name").getAsString());
        System.out.println("Done");
        DBSelect two = tabletwo.select().column("id","tableone").column(table.getColumn("name")).join(joinStatement -> {
            joinStatement.joinType(JoinType.INNER).on("tableone", table.getColumn("id"));
        }).where().start("id",1).and().execute().complete();
        System.out.println(two.get(0).getRow("test.name").getAsString());
    }

    private Properties getLocalProperties() {
        Properties properties = new Properties();
        File file = new File(System.getProperty("user.home"), "mysql.properties");
        if(!file.exists()){
            throw new RuntimeException("Please configure a mysql.properties in your home directory");
        }
        try {
            properties.load(new FileReader(file));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return properties;
    }

    @Test
    public void whereAndSubWhere() {
        Properties properties = getLocalProperties();
        properties.setProperty("db.type", "dev.tuxjsql.mysql.MysqlBuilder");


        TuxJSQL tuxJSQL = TuxJSQLBuilder.create(properties);
        WhereStatement whereStatement = (WhereStatement) tuxJSQL.createWhere().start("bob", "=", "32").AND().start("x", "=", 2).OR("y", "=", "x").and();
        System.out.println(whereStatement.getQuery());
        Arrays.stream(whereStatement.getValues()).forEach(System.out::println);
    }
}
