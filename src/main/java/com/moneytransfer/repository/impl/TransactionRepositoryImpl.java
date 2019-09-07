package com.moneytransfer.repository.impl;

import com.moneytransfer.exception.CurrencyMismatchingException;
import com.moneytransfer.model.dto.TransactionDto;
import com.moneytransfer.repository.TransactionRepository;
import org.jooq.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;

import static com.moneytransfer.repository.impl.AccountRepositoryImpl.*;
import static com.moneytransfer.util.ConnectionFactory.getContext;
import static java.util.Collections.emptyList;
import static java.util.Optional.empty;
import static org.jooq.impl.DSL.*;
import static org.jooq.impl.SQLDataType.DECIMAL;
import static org.jooq.impl.SQLDataType.INTEGER;

public class TransactionRepositoryImpl implements TransactionRepository {

    public static final Table<Record> TRANSACTIONS = table(name("transactions"));
    public static final Field<Integer> TRANSACTION_ID = field(name("transaction_id"), INTEGER);
    public static final Field<Integer> SOURCE_ACCOUNT = field(name("source_acc"), INTEGER);
    public static final Field<Integer> TARGET_ACCOUNT = field(name("target_acc"), INTEGER);
    public static final Field<BigDecimal> AMOUNT = field(name("amount"), DECIMAL(10, 4));

    private static final Logger LOG = LoggerFactory.getLogger(TransactionRepositoryImpl.class);

    private static final AtomicInteger SEQUENCE = new AtomicInteger();

    @Override
    public List<TransactionDto> findAllByCustomerId(final int accountId) {
        try (DSLContext ctx = getContext()) {
            return ctx.selectFrom(TRANSACTIONS)
                    .where(SOURCE_ACCOUNT.eq(accountId).or(TARGET_ACCOUNT.eq(accountId)))
                    .fetchInto(TransactionDto.class);
        } catch (SQLException ex) {
            LOG.error(ex.getMessage());
            return emptyList();
        }
    }

    @Override
    public Optional<TransactionDto> transferMoney(final int accIdToDebit,
                                                  final int accIdToCredit,
                                                  final BigDecimal amount) {
        try (DSLContext ctx = getContext()) {
            int nextVal = SEQUENCE.incrementAndGet();

            ctx.transaction(configuration -> {
                checkCurrency(accIdToDebit, accIdToCredit, configuration);

                using(configuration)
                        .update(ACCOUNTS)
                        .set(BALANCE, BALANCE.minus(amount))
                        .where(ACCOUNT_ID.eq(accIdToDebit))
                        .execute();

                using(configuration)
                        .update(ACCOUNTS)
                        .set(BALANCE, BALANCE.plus(amount))
                        .where(ACCOUNT_ID.eq(accIdToCredit))
                        .execute();

                using(configuration)
                        .insertInto(TRANSACTIONS, TRANSACTION_ID, SOURCE_ACCOUNT, TARGET_ACCOUNT, AMOUNT)
                        .values(nextVal, accIdToDebit, accIdToCredit, amount)
                        .execute();
            });
            return Optional.of(new TransactionDto(nextVal, accIdToDebit, accIdToCredit, amount));
        } catch (SQLException ex) {
            LOG.error(ex.getMessage());
            return empty();
        }
    }

    private void checkCurrency(final int accIdToDebit, final int accIdToCredit, final Configuration configuration) {
        Result<Record1<String>> currencyResult = using(configuration)
                .selectDistinct(CURRENCY).from(ACCOUNTS)
                .where(ACCOUNT_ID.eq(accIdToDebit)).or(ACCOUNT_ID.eq(accIdToCredit))
                .fetch();

        if (currencyResult.size() > 1) {
            throw new CurrencyMismatchingException();
        }
    }
}
