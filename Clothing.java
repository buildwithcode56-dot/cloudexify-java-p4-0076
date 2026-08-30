package com.mycompany.ecommerceproductsystem;

public class Clothing extends Product {

    private final String size;
    private final String material;
    private final String color;

    public Clothing(String productId, String name, double price, int stock, String size, String material, String color) {
        super(productId, name, price, stock);
        if (size == null || size.isBlank()) {
            throw new IllegalArgumentException("Size cannot be null or blank.");
        }
        if (material == null || material.isBlank()) {
            throw new IllegalArgumentException("Material cannot be null or blank.");
        }
        if (color == null || color.isBlank()) {
            throw new IllegalArgumentException("Color cannot be null or blank.");
        }
        this.size = size.trim();
        this.material = material.trim();
        this.color = color.trim();
    }

    public String getSize() {
        return size;
    }

    public String getMaterial() {
        return material;
    }

    public String getColor() {
        return color;
    }

    public String getCategory() {
        return "Clothing";
    }

    public double calculateDiscount() {
        return getPrice() * 0.20;
    }

    public String toString() {
        return String.format("%s [Size: %s, Material: %s, Color: %s]", super.toString(), size, material, color);
    }
}
