package com.moneytransfer.util;

import org.jooq.DSLContext;
import org.jooq.SQLDialect;
import org.jooq.impl.DSL;

import java.sql.DriverManager;
import java.sql.SQLException;

public class ConnectionFactory {

    private static final String DB_URL = "jdbc:h2:mem:some_bank;DB_CLOSE_DELAY=-1";

    private ConnectionFactory() {
    }

    public static DSLContext getContext() throws SQLException {
        return DSL.using(DriverManager.getConnection(DB_URL), SQLDialect.H2);
    }
}
