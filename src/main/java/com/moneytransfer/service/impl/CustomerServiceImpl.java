package com.moneytransfer.service.impl;

import com.moneytransfer.exception.DataNotFoundException;
import com.moneytransfer.exception.DataSavingErrorException;
import com.moneytransfer.model.Customer;
import com.moneytransfer.model.dto.CustomerDto;
import com.moneytransfer.repository.CustomerRepository;
import com.moneytransfer.service.CustomerService;

import java.util.List;

public class CustomerServiceImpl implements CustomerService {

    private static final String CUSTOMER_NOT_FOUND_BY_ID = "Customer not found by id ";

    private final CustomerRepository repository;

    public CustomerServiceImpl(final CustomerRepository repository) {
        this.repository = repository;
    }

    @Override
    public List<CustomerDto> getCustomers() {
        List<CustomerDto> customers = repository.findAll();
        if (customers.isEmpty()) {
            throw new DataNotFoundException("Customers not found");
        }
        return customers;
    }

    @Override
    public CustomerDto getCustomer(final int customerId) {
        return repository.findById(customerId)
                .orElseThrow(() -> new DataNotFoundException(CUSTOMER_NOT_FOUND_BY_ID + customerId));
    }

    @Override
    public CustomerDto addCustomer(final Customer customer) {
        return repository.save(customer)
                .orElseThrow(() -> new DataSavingErrorException("Customer hasn't been saved"));
    }

    @Override
    public void update(final CustomerDto customer) {
        if (!repository.update(customer)) {
            throw new DataNotFoundException(CUSTOMER_NOT_FOUND_BY_ID + customer.getCustomerId());
        }
    }
}
