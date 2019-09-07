package com.moneytransfer.repository.impl;

import com.moneytransfer.model.Account;
import com.moneytransfer.model.dto.AccountDto;
import com.moneytransfer.repository.AccountRepository;
import org.jooq.DSLContext;
import org.jooq.Field;
import org.jooq.Record;
import org.jooq.Table;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;

import static com.moneytransfer.util.ConnectionFactory.getContext;
import static java.math.BigDecimal.ZERO;
import static java.util.Collections.emptyList;
import static java.util.Optional.empty;
import static org.jooq.impl.DSL.*;
import static org.jooq.impl.SQLDataType.*;

public class AccountRepositoryImpl implements AccountRepository {

    public static final Table<Record> ACCOUNTS = table(name("accounts"));
    public static final Field<Integer> ACCOUNT_ID = field(name("account_id"), INTEGER.identity(true));
    public static final Field<Integer> OWNER_ID = field(name("owner_id"), INTEGER);
    public static final Field<String> CURRENCY = field(name("currency"), VARCHAR(3));
    public static final Field<BigDecimal> BALANCE = field(name("balance"), DECIMAL(10, 4)
            .defaultValue(ZERO));

    private static final Logger LOG = LoggerFactory.getLogger(AccountRepositoryImpl.class);

    private static final AtomicInteger SEQUENCE = new AtomicInteger();

    @Override
    public List<AccountDto> findAllByCustomerId(final int customerId) {
        try (DSLContext ctx = getContext()) {
            return ctx.selectFrom(ACCOUNTS)
                    .where(OWNER_ID.eq(customerId))
                    .fetchInto(AccountDto.class);
        } catch (SQLException ex) {
            LOG.error(ex.getMessage());
            return emptyList();
        }
    }

    @Override
    public Optional<AccountDto> find(final int accountId) {
        try (DSLContext ctx = getContext()) {
            return ctx.selectFrom(ACCOUNTS)
                    .where(ACCOUNT_ID.eq(accountId))
                    .fetchOptionalInto(AccountDto.class);
        } catch (SQLException ex) {
            LOG.error(ex.getMessage());
            return empty();
        }
    }

    @Override
    public Optional<AccountDto> save(final int customerId, final Account acc) {
        try (DSLContext ctx = getContext()) {
            int nextVal = SEQUENCE.incrementAndGet();
            ctx.insertInto(ACCOUNTS, ACCOUNT_ID, OWNER_ID, CURRENCY, BALANCE)
                    .values(nextVal, customerId, acc.getCurrency(), acc.getBalance())
                    .execute();
            return Optional.of(new AccountDto(nextVal, customerId, acc.getCurrency(), acc.getBalance()));
        } catch (SQLException ex) {
            LOG.error(ex.getMessage());
            return empty();
        }
    }
}
