package com.moneytransfer.config;

import com.moneytransfer.controller.AccountController;
import com.moneytransfer.controller.CustomerController;
import com.moneytransfer.controller.TransactionController;
import com.moneytransfer.error.handler.RestExceptionHandler;
import com.moneytransfer.repository.AccountRepository;
import com.moneytransfer.repository.CustomerRepository;
import com.moneytransfer.repository.TransactionRepository;
import com.moneytransfer.repository.impl.AccountRepositoryImpl;
import com.moneytransfer.repository.impl.CustomerRepositoryImpl;
import com.moneytransfer.repository.impl.TransactionRepositoryImpl;
import com.moneytransfer.service.AccountService;
import com.moneytransfer.service.CustomerService;
import com.moneytransfer.service.TransactionService;
import com.moneytransfer.service.impl.AccountServiceImpl;
import com.moneytransfer.service.impl.CustomerServiceImpl;
import com.moneytransfer.service.impl.TransactionServiceImpl;
import com.moneytransfer.transformer.JsonTransformer;

import static com.moneytransfer.controller.AccountController.ACCOUNTS_URI;
import static com.moneytransfer.controller.AccountController.ACCOUNT_ID_PATH;
import static com.moneytransfer.controller.CustomerController.CUSTOMERS_URI;
import static com.moneytransfer.controller.CustomerController.CUSTOMER_ID_PATH;
import static com.moneytransfer.controller.TransactionController.TRANSACTIONS_URI;
import static org.eclipse.jetty.http.MimeTypes.Type.APPLICATION_JSON;
import static spark.Spark.*;

public class Server {

    private final int port;

    public Server(final int port) {
        this.port = port;
    }

    public void start() {
        port(port);
        RestExceptionHandler.init();
        after((request, response) -> response.type(APPLICATION_JSON.asString()));
        defaultResponseTransformer(new JsonTransformer());

        CustomerController customerController = getCustomerController();
        AccountController accountController = getAccountController();
        TransactionController transactionController = getTransactionController();

        path(CUSTOMERS_URI, () -> {
            get("", customerController.getCustomers());
            get(CUSTOMER_ID_PATH, customerController.getCustomer());
            post("", APPLICATION_JSON.asString(), customerController.addCustomer());
            patch(CUSTOMER_ID_PATH, APPLICATION_JSON.asString(), customerController.updateCustomer());

            path(CUSTOMER_ID_PATH + ACCOUNTS_URI, () -> {
                get("", accountController.getAccounts());
                post("", APPLICATION_JSON.asString(), accountController.addAccount());
            });
        });
        path(ACCOUNTS_URI, () -> {
            get(ACCOUNT_ID_PATH, accountController.getAccount());
            get(ACCOUNT_ID_PATH + TRANSACTIONS_URI, transactionController.getTransactions());
        });
        post(TRANSACTIONS_URI, transactionController.transferMoney());
    }

    private static CustomerController getCustomerController() {
        CustomerRepository repository = new CustomerRepositoryImpl();
        CustomerService service = new CustomerServiceImpl(repository);
        return new CustomerController(service);
    }

    private static AccountController getAccountController() {
        AccountRepository repository = new AccountRepositoryImpl();
        AccountService service = new AccountServiceImpl(repository);
        return new AccountController(service);
    }

    private static TransactionController getTransactionController() {
        TransactionRepository repository = new TransactionRepositoryImpl();
        TransactionService service = new TransactionServiceImpl(repository);
        return new TransactionController(service);
    }

    public void shutdown() {
        stop();
    }
}
