package dev.tuxjsql.mysql.tests;
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
    public void main() throws InterruptedException {

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
        dbInsert.queue(dbInsert1 -> assertTrue(((BigInteger) dbInsert1.primaryKey()).intValue() != 0));
        tabletwo.insert().value("name","hey").value("tableone",1).execute().complete();
        DBSelect select = table.select().column("id").column("name").where().start("id", "=", 1).and().execute().complete();
        System.out.println(select.numberOfRows());
        System.out.println(select.first().get().getColumn("name").get().getAsString());
        System.out.println("Done");
        DBSelect two = tabletwo.select().column("id", "tableone").column(table.getColumn("name")).join(joinStatement -> {
            joinStatement.joinType(JoinType.INNER).on("tableone", table.getColumn("id"));
        }).where().start("id", 1).and().execute().complete();
        System.out.println(two.get(0).getColumn("test.name").get().getAsString());
        table.update().value("name", "kys").execute().complete();

        tabletwo.delete().execute().complete();
        table.delete().where().start("name", "kys").and().execute().complete();
    }

    private Properties getLocalProperties() {
        Properties properties = new Properties();
        File file = new File(System.getProperty("user.home"), "mysql.properties");
        if(!file.exists()){
            //Probably Travis-CI
            properties.setProperty("user","root");
            properties.setProperty("password","");
            properties.setProperty("db.db","test");
            properties.setProperty("db.host","127.0.0.1:3306");

        }else {
            try {
                properties.load(new FileReader(file));
            } catch (IOException e) {
                e.printStackTrace();
            }
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
