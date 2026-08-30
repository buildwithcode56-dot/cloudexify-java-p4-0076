package com.mycompany.ecommerceproductsystem;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

public class Order {

    public enum Status 
    {
        PENDING,
        CONFIRMED,
        SHIPPED,
        CANCELLED
    }

    private final String orderId;
    private final Customer customer;
    private final List<CartItem> items;
    private final double totalAmount;
    private Status status;

    public Order(String orderId, Customer customer, List<CartItem> cartItems) {
        if (orderId == null || orderId.isBlank()) {
            throw new IllegalArgumentException("Order ID cannot be null or blank.");
        }
        if (customer == null) {
            throw new IllegalArgumentException("Customer cannot be null.");
        }
        if (cartItems == null || cartItems.isEmpty()) {
            throw new IllegalArgumentException("An order must contain at least one item.");
        }
        this.orderId = orderId.trim();
        this.customer = customer;
        this.items = new ArrayList<>(cartItems);
        this.totalAmount = items.stream().mapToDouble(CartItem::getSubtotal).sum();
        this.status = Status.PENDING;
    }

    public String getOrderId() {
        return orderId;
    }

    public Customer getCustomer() {
        return customer;
    }

    public List<CartItem> getItems() {
        return Collections.unmodifiableList(items);
    }

    public double getTotalAmount() {
        return totalAmount;
    }

    public Status getStatus() {
        return status;
    }

    public void confirm() {
        if (this.status == Status.CANCELLED) {
            throw new IllegalStateException("Cannot confirm a cancelled order.");
        }
        this.status = Status.CONFIRMED;
    }

    public void ship() {
        if (this.status != Status.CONFIRMED) {
            throw new IllegalStateException("Only confirmed orders can be shipped.");
        }
        this.status = Status.SHIPPED;
    }

    public void cancel() {
        if (this.status == Status.SHIPPED) {
            throw new IllegalStateException("Cannot cancel an order that has already been shipped.");
        }
        this.status = Status.CANCELLED;
    }

    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Order order = (Order) o;
        return Objects.equals(orderId, order.orderId);
    }

    public int hashCode() {
        return Objects.hash(orderId);
    }

    public String toString() {
        return String.format("Order [ID: %s, Customer: %s, Items: %d, Total: Rs. %.2f, Status: %s]", orderId, customer.getName(), items.size(), totalAmount, status);
    }
}
