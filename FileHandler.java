package com.mycompany.ecommerceproductsystem;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class FileHandler {

    private static final String FILE_NAME = "orders.txt";

    public static void saveOrder(Order order) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_NAME, true))) {
            StringBuilder items = new StringBuilder();
            for (CartItem item : order.getItems()) {
                items.append(item.getProduct().getProductId()).append(",").append(item.getQuantity()).append(";");
            }
            writer.write(order.getOrderId() + "|" + order.getCustomer().getCustomerId() + "|" + order.getCustomer().getName() + "|" + order.getTotalAmount() + "|" + order.getStatus() + "|" + items);
            writer.newLine();
        } catch (IOException e) {
            System.out.println("Unable to save order: " + e.getMessage());
        }
    }

    public static List<String[]> loadOrders() {
        List<String[]> savedOrders = new ArrayList<>();
        File file = new File(FILE_NAME);
        if (!file.exists()) {
            return savedOrders;
        }
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (!line.trim().isEmpty()) {
                    String[] data = line.split("\\|", -1);
                    if (data.length >= 6) {
                        savedOrders.add(data);
                    }
                }
            }
        } catch (IOException e) {
            System.out.println("Unable to load orders: " + e.getMessage());
        }
        return savedOrders;
    }
}
