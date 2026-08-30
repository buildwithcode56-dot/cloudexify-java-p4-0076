package com.mycompany.ecommerceproductsystem;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

public class Customer {
    private final String customerId;
    private final String name;
    private final List<Order> orderHistory;

    public Customer(String customerId, String name) {
        if (customerId == null || customerId.isBlank()) {
            throw new IllegalArgumentException("Customer ID cannot be null or blank.");
        }
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("Customer name cannot be null or blank.");
        }
        this.customerId = customerId.trim();
        this.name = name.trim();
        this.orderHistory = new ArrayList<>();
    }

    public String getCustomerId() {
        return customerId;
    }

    public String getName() {
        return name;
    }

    public void addOrder(Order order) {
        if (order == null) {
            throw new IllegalArgumentException("Order cannot be null.");
        }
        this.orderHistory.add(order);
    }

    public List<Order> getOrderHistory() {
        return Collections.unmodifiableList(orderHistory);
    }

    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Customer customer = (Customer) o;
        return Objects.equals(customerId, customer.customerId);
    }

    public int hashCode() {
        return Objects.hash(customerId);
    }

    public String toString() {
        return String.format("Customer [ID: %s, Name: %s, Total Orders: %d]", customerId, name, orderHistory.size());
    }
}
