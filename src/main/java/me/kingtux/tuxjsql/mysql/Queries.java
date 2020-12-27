package me.kingtux.tuxjsql.mysql;

public enum Queries {
    FOREIGN_VALUE("FOREIGN KEY (`%1$s`) REFERENCES %2$s(`%3$s`)"),
    INSERT("INSERT INTO %1$s (%2$s) VALUES (%3$s);"),
    SELECT("SELECT %1$s FROM %2$s"),
    WHERE("WHERE %1$s"),
    DELETE("DELETE FROM %1$s"),
    UPDATE("UPDATE %1$s SET %2$s"),
    CREATE_TABLE_IF_NOT_EXISTS("CREATE TABLE IF NOT EXISTS %1$s (%2$s);"),
    CREATE_TABLE("CREATE TABLE %1$s (%2$s);"),
    MAX("SELECT MAX(%1$s) FROM %2$s"),
    MIN("SELECT MIN(%1$s) FROM %2$s"),
    DROP_TABLE("DROP TABLE %1$s"),
    DROP_COLUMN("ALTER TABLE %1$s DROP COLUMN %2$s"),
    ADD_COLUMN("ALTER TABLE %1$s ADD COLUMN %2$s"),
    MODIFY_COLUMN("ALTER TABLE %1$s MODIFY COLUMN %2$s"),
    //Departments ON Students.DepartmentId = Departments.DepartmentId
    JOIN("%1$s %2$s ON %3$s.%4$s = %2$s.%5$s")
    ;

    private String string;

    Queries(String string) {
        this.string = string;
    }

    public String getString() {
        return string;
    }
}


