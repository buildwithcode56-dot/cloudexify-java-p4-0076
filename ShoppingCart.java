package com.mycompany.ecommerceproductsystem;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ShoppingCart {

    private final List<CartItem> items;

    public ShoppingCart() {
        this.items = new ArrayList<>();
    }

    public void addProduct(Product product, int quantity) throws OutOfStockException {
        if (product == null) {
            throw new IllegalArgumentException("Product cannot be null.");
        }
        if (quantity <= 0) {
            throw new IllegalArgumentException("Quantity must be greater than zero.");
        }
        for (CartItem item : items) {
            if (item.getProduct().equals(product)) {
                int newTotalQuantity = item.getQuantity() + quantity;
                if (newTotalQuantity > product.getStock()) {
                    throw new OutOfStockException(
                        String.format("Cannot add %d more units. Total in cart (%d) would exceed available stock (%d).", quantity, item.getQuantity(), product.getStock())
                    );
                }
                item.increaseQuantity(quantity);
                return;
            }
        }
        if (quantity > product.getStock()) {
            throw new OutOfStockException(
                String.format("Requested quantity (%d) exceeds available stock (%d) for %s.", quantity, product.getStock(), product.getName())
            );
        }
        items.add(new CartItem(product, quantity));
    }

    public void removeProduct(Product product) {
        if (product == null) {
            throw new IllegalArgumentException("Product cannot be null.");
        }
        items.removeIf(item -> item.getProduct().equals(product));
    }

    public List<CartItem> getItems() {
        return Collections.unmodifiableList(items);
    }

    public double getTotal() {
        return items.stream().mapToDouble(CartItem::getSubtotal).sum();
    }

    public int getTotalItemCount() {
        return items.stream().mapToInt(CartItem::getQuantity).sum();
    }

    public boolean isEmpty() {
        return items.isEmpty();
    }

    public void clear() {
        items.clear();
    }

    public String toString() {
        return String.format("ShoppingCart [Items: %d, Total Products: %d, Total: Rs. %.2f]", items.size(), getTotalItemCount(), getTotal());
    }
}
