package com.mycompany.ecommerceproductsystem;

import java.util.Objects;

public class CartItem {

    private final Product product;
    private int quantity;

    public CartItem(Product product, int quantity) {
        if (product == null) {
            throw new IllegalArgumentException("Product cannot be null.");
        }
        if (quantity <= 0) {
            throw new IllegalArgumentException("Quantity must be greater than zero.");
        }
        if (quantity > product.getStock()) {
            throw new IllegalArgumentException("Quantity exceeds available stock (" + product.getStock() + ").");
        }
        this.product = product;
        this.quantity = quantity;
    }

    public Product getProduct() {
        return product;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        if (quantity <= 0) {
            throw new IllegalArgumentException("Quantity must be greater than zero.");
        }
        if (quantity > product.getStock()) {
            throw new IllegalArgumentException("Requested quantity exceeds available stock (" + product.getStock() + ").");
        }
        this.quantity = quantity;
    }

    public void increaseQuantity(int amount) {
        if (amount <= 0) {
            throw new IllegalArgumentException("Amount to increase must be positive.");
        }
        setQuantity(this.quantity + amount);
    }

    public double getSubtotal() {
        return product.getFinalPrice() * quantity;
    }

    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        CartItem cartItem = (CartItem) o;
        return Objects.equals(product, cartItem.product);
    }

    public int hashCode() {
        return Objects.hash(product);
    }

    public String toString() {
        return String.format("%s (x%d) - Subtotal: Rs. %.2f", product.getName(), quantity, getSubtotal());
    }
}
