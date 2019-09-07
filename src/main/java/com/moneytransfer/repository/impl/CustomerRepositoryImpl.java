package com.moneytransfer.repository.impl;

import com.moneytransfer.model.Customer;
import com.moneytransfer.model.dto.CustomerDto;
import com.moneytransfer.repository.CustomerRepository;
import org.jooq.DSLContext;
import org.jooq.Field;
import org.jooq.Record;
import org.jooq.Table;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;

import static com.moneytransfer.util.ConnectionFactory.getContext;
import static java.util.Collections.emptyList;
import static java.util.Optional.empty;
import static org.jooq.impl.DSL.*;
import static org.jooq.impl.SQLDataType.INTEGER;
import static org.jooq.impl.SQLDataType.VARCHAR;

public class CustomerRepositoryImpl implements CustomerRepository {

    public static final Table<Record> CUSTOMERS = table(name("customers"));
    public static final Field<Integer> CUSTOMER_ID = field(name("customer_id"), INTEGER);
    public static final Field<String> FIRST_NAME = field(name("first_name"), VARCHAR);
    public static final Field<String> LAST_NAME = field(name("last_name"), VARCHAR);

    private static final Logger LOG = LoggerFactory.getLogger(CustomerRepositoryImpl.class);

    private static final AtomicInteger SEQUENCE = new AtomicInteger();

    @Override
    public List<CustomerDto> findAll() {
        try (DSLContext ctx = getContext()) {
            return ctx.selectFrom(CUSTOMERS)
                    .fetchInto(CustomerDto.class);
        } catch (SQLException ex) {
            LOG.error(ex.getMessage());
            return emptyList();
        }
    }

    @Override
    public Optional<CustomerDto> findById(final int id) {
        try (DSLContext ctx = getContext()) {
            return ctx.select(FIRST_NAME, LAST_NAME).from(CUSTOMERS)
                    .where(CUSTOMER_ID.eq(id))
                    .fetchOptionalInto(CustomerDto.class);
        } catch (SQLException ex) {
            LOG.error(ex.getMessage());
            return empty();
        }
    }

    @Override
    public Optional<CustomerDto> save(final Customer customer) {
        try (DSLContext ctx = getContext()) {
            int nextVal = SEQUENCE.incrementAndGet();
            ctx.insertInto(CUSTOMERS, CUSTOMER_ID, FIRST_NAME, LAST_NAME)
                    .values(nextVal, customer.getFirstName(), customer.getLastName())
                    .execute();
            return Optional.of(new CustomerDto(nextVal, customer.getFirstName(), customer.getLastName()));
        } catch (SQLException ex) {
            LOG.error(ex.getMessage());
            return empty();
        }
    }

    @Override
    public boolean update(final CustomerDto customer) {
        try (DSLContext ctx = getContext()) {
            return ctx.update(CUSTOMERS)
                    .set(row(FIRST_NAME, LAST_NAME), row(customer.getFirstName(), customer.getLastName()))
                    .where(CUSTOMER_ID.eq(customer.getCustomerId()))
                    .execute() == 1;
        } catch (SQLException ex) {
            LOG.error(ex.getMessage());
            return false;
        }
    }
}
