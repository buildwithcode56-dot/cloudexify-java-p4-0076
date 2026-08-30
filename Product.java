package com.mycompany.ecommerceproductsystem;

import java.util.Objects;

public abstract class Product {

    private final String productId;
    private final String name;
    private final double price;
    private int stock;
    private double rating;

    public Product(String productId, String name, double price, int stock) {
        if (productId == null || productId.isBlank()) {
            throw new IllegalArgumentException("Product ID cannot be null or blank.");
        }
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("Product name cannot be null or blank.");
        }
        if (price <= 0) {
            throw new IllegalArgumentException("Price must be greater than zero.");
        }
        if (stock < 0) {
            throw new IllegalArgumentException("Stock cannot be negative.");
        }
        this.productId = productId.trim();
        this.name = name.trim();
        this.price = price;
        this.stock = stock;
        this.rating = 4.0;
    }

    public String getProductId() {
        return productId;
    }

    public String getName() {
        return name;
    }

    public double getPrice() {
        return price;
    }

    public int getStock() {
        return stock;
    }

    public double getRating() {
        return rating;
    }

    public void setRating(double rating) {
        if (rating < 0.0 || rating > 5.0) {
            throw new IllegalArgumentException("Rating must be between 0.0 and 5.0.");
        }
        this.rating = rating;
    }

    public void increaseStock(int quantity) {
        if (quantity <= 0) {
            throw new IllegalArgumentException("Quantity to increase must be greater than zero.");
        }
        this.stock += quantity;
    }

    public void decreaseStock(int quantity) throws OutOfStockException {
        if (quantity <= 0) {
            throw new IllegalArgumentException("Quantity must be greater than zero.");
        }
        if (quantity > stock) {
            throw new OutOfStockException("Only " + stock + " item(s) available for " + name);
        }
        this.stock -= quantity;
    }

    public abstract double calculateDiscount();

    public double getFinalPrice() {
        return Math.max(0, price - calculateDiscount());
    }

    public String getCategory() {
        return getClass().getSimpleName();
    }

    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Product product = (Product) o;
        return Objects.equals(productId, product.productId);
    }

    public int hashCode() {
        return Objects.hash(productId);
    }

    public String toString() {
        return String.format("%s [ID: %s] | Rs. %.2f (Final: Rs. %.2f) | Rating: %.1f | Stock: %d", name, productId, price, getFinalPrice(), rating, stock);
    }
}
