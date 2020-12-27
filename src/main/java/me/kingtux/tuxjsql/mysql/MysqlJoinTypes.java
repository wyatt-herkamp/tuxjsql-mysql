package me.kingtux.tuxjsql.mysql;

import dev.tuxjsql.core.sql.select.JoinType;

public enum MysqlJoinTypes {
    INNER("INNER JOIN", JoinType.INNER);


    private String key;
    private JoinType joinType;

    public String getKey() {
        return key;
    }

    public JoinType getJoinType() {
        return joinType;
    }

    MysqlJoinTypes(String key, JoinType joinType) {
        this.key = key;
        this.joinType = joinType;
    }

    public static MysqlJoinTypes getType(JoinType type) {
        for (MysqlJoinTypes sqliteJoin : values()) {
            if (sqliteJoin.joinType == type) return sqliteJoin;
        }
        return null;
    }
}
