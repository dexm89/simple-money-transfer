package com.moneytransfer.config;

import org.jooq.DSLContext;

import java.sql.SQLException;

import static com.moneytransfer.repository.impl.AccountRepositoryImpl.*;
import static com.moneytransfer.repository.impl.CustomerRepositoryImpl.*;
import static com.moneytransfer.repository.impl.TransactionRepositoryImpl.*;
import static com.moneytransfer.util.ConnectionFactory.getContext;
import static org.jooq.impl.DSL.constraint;

public class Database {

    public void init() throws SQLException {
        try (DSLContext ctx = getContext()) {
            ctx.createTable(CUSTOMERS)
                    .columns(CUSTOMER_ID, FIRST_NAME, LAST_NAME)
                    .constraint(constraint("pk_customer").primaryKey(CUSTOMER_ID))
                    .execute();

            ctx.createTable(ACCOUNTS)
                    .columns(ACCOUNT_ID, OWNER_ID, CURRENCY, BALANCE)
                    .constraints(
                            constraint("pk_account").primaryKey(ACCOUNT_ID),
                            constraint("fk_customer").foreignKey(OWNER_ID).references(CUSTOMERS)
                    ).execute();

            ctx.createTable(TRANSACTIONS)
                    .columns(TRANSACTION_ID, SOURCE_ACCOUNT, TARGET_ACCOUNT, AMOUNT)
                    .constraints(
                            constraint("pk_transaction").primaryKey(TRANSACTION_ID),
                            constraint("fk_source_account").foreignKey(SOURCE_ACCOUNT).references(ACCOUNTS),
                            constraint("fk_target_account").foreignKey(TARGET_ACCOUNT).references(ACCOUNTS)
                    ).execute();
        }
    }

    public void destroy() throws SQLException {
        try (DSLContext ctx = getContext()) {
            ctx.query("DROP ALL OBJECTS").execute();
        }
    }
}
