package com.moneytransfer.repository;

import com.moneytransfer.model.Customer;
import com.moneytransfer.model.dto.CustomerDto;

import java.util.List;
import java.util.Optional;

public interface CustomerRepository {

    List<CustomerDto> findAll();

    Optional<CustomerDto> findById(int id);

    Optional<CustomerDto> save(Customer customer);

    boolean update(CustomerDto customer);
}
