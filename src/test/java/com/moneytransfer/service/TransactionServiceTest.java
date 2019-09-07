package com.moneytransfer.service;

import com.moneytransfer.config.Database;
import com.moneytransfer.config.Server;
import com.moneytransfer.model.Account;
import com.moneytransfer.model.Customer;
import com.moneytransfer.model.TransactionRequest;
import com.moneytransfer.model.dto.AccountDto;
import com.moneytransfer.model.dto.CustomerDto;
import com.moneytransfer.model.dto.TransactionDto;
import okhttp3.*;
import org.junit.jupiter.api.*;

import java.io.IOException;
import java.math.BigDecimal;
import java.sql.SQLException;

import static com.moneytransfer.controller.AccountController.ACCOUNTS_URI;
import static com.moneytransfer.controller.CustomerController.CUSTOMERS_URI;
import static com.moneytransfer.controller.TransactionController.TRANSACTIONS_URI;
import static com.moneytransfer.util.JsonUtils.fromJson;
import static com.moneytransfer.util.JsonUtils.toJson;
import static org.eclipse.jetty.http.HttpStatus.*;
import static org.hamcrest.MatcherAssert.*;
import static org.hamcrest.Matchers.*;

class TransactionServiceTest {

    private static final MediaType JSON = MediaType.get("application/json");
    private static final String URL = "http://localhost:8090";

    private static OkHttpClient client;

    private Server server;
    private Database database;

    @BeforeAll
    static void init() {
        client = new OkHttpClient();
    }

    @BeforeEach
    void setUp() throws SQLException {
        database = new Database();
        database.init();

        server = new Server(8090);
        server.start();
    }

    @AfterEach
    void tearDown() throws SQLException {
        database.destroy();
        server.shutdown();
    }

    @DisplayName("Transfer money between accounts")
    @Test
    void testTransferMoneyBetweenAccounts() throws IOException {
        int customer1 = postCustomer(new Customer("Ivan", "Ivanov"));
        int customer2 = postCustomer(new Customer("Petr", "Petrov"));

        int account1 = postAccount(customer1, new Account("USD", BigDecimal.valueOf(1000)));
        int account2 = postAccount(customer2, new Account("USD", BigDecimal.valueOf(1500)));

        TransactionRequest transactionRequest = new TransactionRequest(customer1, customer2, BigDecimal.valueOf(700));
        RequestBody body = RequestBody.create(toJson(transactionRequest), JSON);
        Request request = new Request.Builder()
                .url(URL + TRANSACTIONS_URI)
                .post(body)
                .build();
        try (Response response = client.newCall(request).execute()) {
            assertThat(response.code(), equalTo(OK_200));
            TransactionDto transaction = fromJson(response.body().string(), TransactionDto.class);
            assertThat(transaction.getTransactionId(), equalTo(1));
            assertThat(transaction.getFromAccount(), equalTo(customer1));
            assertThat(transaction.getToAccount(), equalTo(customer2));
            assertThat(transaction.getAmount(), equalTo(BigDecimal.valueOf(700)));
        }

        assertAccountBalance(account1, BigDecimal.valueOf(300));
        assertAccountBalance(account2, BigDecimal.valueOf(2200));
    }

    @DisplayName("Transfer money between accounts when currencies are different")
    @Test
    void testTransferMoneyBetweenAccountsWhenCurrenciesAreDifferent() throws IOException {
        int customer1 = postCustomer(new Customer("Ivan", "Ivanov"));
        int customer2 = postCustomer(new Customer("Petr", "Petrov"));

        postAccount(customer1, new Account("USD", BigDecimal.valueOf(1000)));
        postAccount(customer2, new Account("EUR", BigDecimal.valueOf(1500)));

        TransactionRequest transactionRequest = new TransactionRequest(customer1, customer2, BigDecimal.valueOf(500));
        RequestBody body = RequestBody.create(toJson(transactionRequest), JSON);
        Request request = new Request.Builder()
                .url(URL + TRANSACTIONS_URI)
                .post(body)
                .build();
        try (Response response = client.newCall(request).execute()) {
            assertThat(response.code(), equalTo(CONFLICT_409));
        }
    }

    private int postCustomer(final Customer customer) throws IOException {
        RequestBody body = RequestBody.create(toJson(customer), JSON);
        Request request = new Request.Builder()
                .url(URL + CUSTOMERS_URI)
                .post(body)
                .build();

        int customerId;
        try (Response response = client.newCall(request).execute()) {
            assertThat(response.code(), equalTo(CREATED_201));

            CustomerDto newCustomer = fromJson(response.body().string(), CustomerDto.class);
            assertThat(newCustomer.getFirstName(), equalTo(customer.getFirstName()));
            assertThat(newCustomer.getLastName(), equalTo(customer.getLastName()));
            customerId = newCustomer.getCustomerId();
        }
        return customerId;
    }

    private int postAccount(final int customerId, final Account account) throws IOException {
        RequestBody body = RequestBody.create(toJson(account), JSON);
        Request request = new Request.Builder()
                .url(URL + CUSTOMERS_URI + "/" + customerId + ACCOUNTS_URI)
                .post(body)
                .build();

        int accountId;
        try (Response response = client.newCall(request).execute()) {
            assertThat(response.code(), equalTo(CREATED_201));

            AccountDto newAccount = fromJson(response.body().string(), AccountDto.class);
            assertThat(newAccount.getOwnerId(), equalTo(customerId));
            assertThat(newAccount.getCurrency(), equalTo(account.getCurrency()));
            assertThat(newAccount.getBalance(), comparesEqualTo(account.getBalance()));
            accountId = newAccount.getAccountId();
        }
        return accountId;
    }

    private void assertAccountBalance(final int accountId, final BigDecimal expectedBalance) throws IOException {
        Request request = new Request.Builder()
                .url(URL + ACCOUNTS_URI + "/" + accountId)
                .build();

        try (Response response = client.newCall(request).execute()) {
            assertThat(response.code(), equalTo(OK_200));

            AccountDto newAccount = fromJson(response.body().string(), AccountDto.class);
            assertThat(newAccount.getBalance(), comparesEqualTo(expectedBalance));
        }
    }
}
