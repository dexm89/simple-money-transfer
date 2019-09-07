package com.moneytransfer.service;

import com.moneytransfer.model.Customer;
import com.moneytransfer.model.dto.CustomerDto;

import java.util.List;

public interface CustomerService {

    List<CustomerDto> getCustomers();

    CustomerDto getCustomer(int id);

    CustomerDto addCustomer(Customer customer);

    void update(CustomerDto customer);
}
