package com.moneytransfer;

import com.moneytransfer.config.Database;
import com.moneytransfer.config.Server;

import java.sql.SQLException;

public class MoneyTransferApplication {

    public static void main(final String[] args) throws SQLException {
        Database database = new Database();
        database.init();

        Server server = new Server(8080);
        server.start();
    }
}
