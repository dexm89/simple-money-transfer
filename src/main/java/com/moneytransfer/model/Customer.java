package com.moneytransfer.model;

public class Customer {

    private final String firstName;
    private final String lastName;

    public Customer(final String firstName, final String lastName) {
        this.firstName = firstName;
        this.lastName = lastName;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    @Override
    public String toString() {
        return "{"
                + "\"firstName\":\"" + firstName + "\""
                + ",\"lastName\":\"" + lastName + "\""
                + "}";
    }
}
