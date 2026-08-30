package com.mycompany.ecommerceproductsystem;

public class Electronics extends Product {

    private final String brand;
    private final int warrantyMonths;

    public Electronics(String productId, String name, double price, int stock, String brand, int warrantyMonths) {
        super(productId, name, price, stock);
        if (brand == null || brand.isBlank()) {
            throw new IllegalArgumentException("Brand cannot be null or blank.");
        }
        if (warrantyMonths < 0) {
            throw new IllegalArgumentException("Warranty months cannot be negative.");
        }
        this.brand = brand.trim();
        this.warrantyMonths = warrantyMonths;
    }

    public String getBrand() {
        return brand;
    }

    public int getWarrantyMonths() {
        return warrantyMonths;
    }

    public String getCategory() {
        return "Electronics";
    }

    public double calculateDiscount() {
        return getPrice() * 0.10;
    }

    public String toString() {
        return String.format("%s [Brand: %s, Warranty: %d months]", super.toString(), brand, warrantyMonths);
    }
}
