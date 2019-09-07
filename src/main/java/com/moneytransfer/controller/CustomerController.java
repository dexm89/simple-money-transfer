package com.moneytransfer.controller;

import com.moneytransfer.model.Customer;
import com.moneytransfer.model.dto.CustomerDto;
import com.moneytransfer.service.CustomerService;
import spark.Request;
import spark.Response;
import spark.Route;

import static com.moneytransfer.util.JsonUtils.fromJson;
import static com.moneytransfer.util.RequestParser.parseCustomerId;
import static org.eclipse.jetty.http.HttpHeader.LOCATION;
import static org.eclipse.jetty.http.HttpStatus.CREATED_201;

public class CustomerController {

    public static final String CUSTOMERS_URI = "/customers";
    public static final String CUSTOMER_ID_PATH = "/:customerId";

    private final CustomerService customerService;

    public CustomerController(final CustomerService customerService) {
        this.customerService = customerService;
    }

    public Route getCustomers() {
        return (Request request, Response response) -> customerService.getCustomers();
    }

    public Route getCustomer() {
        return (Request request, Response response) -> customerService.getCustomer(parseCustomerId(request));
    }

    public Route addCustomer() {
        return (Request request, Response response) -> {
            CustomerDto newCustomer = customerService.addCustomer(getCustomer(request));

            response.status(CREATED_201);
            response.header(LOCATION.asString(), request.url() + "/" + newCustomer.getCustomerId());
            return newCustomer;
        };
    }

    public Route updateCustomer() {
        return (Request request, Response response) -> {
            Customer customer = getCustomer(request);
            customerService.update(
                    new CustomerDto(parseCustomerId(request), customer.getFirstName(), customer.getLastName()));
            return "";
        };
    }

    private Customer getCustomer(final Request request) {
        return fromJson(request.body(), Customer.class);
    }
}
