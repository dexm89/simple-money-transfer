package com.moneytransfer.model.dto;

public class CustomerDto {

    private final Integer customerId;
    private final String firstName;
    private final String lastName;

    public CustomerDto(final Integer customerId, final String firstName, final String lastName) {
        this.customerId = customerId;
        this.firstName = firstName;
        this.lastName = lastName;
    }

    public Integer getCustomerId() {
        return customerId;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }
}
