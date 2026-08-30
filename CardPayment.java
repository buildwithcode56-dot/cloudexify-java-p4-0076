package com.mycompany.ecommerceproductsystem;

public class CardPayment implements Payment {

    private final String cardHolder;
    private final String cardNumber;

    public CardPayment(String cardHolder, String cardNumber) {
        if (cardHolder == null || cardHolder.isBlank()) {
            throw new IllegalArgumentException("Card holder name is required.");
        }
        if (cardNumber == null || !cardNumber.matches("\\d{16}")) {
            throw new IllegalArgumentException("Card number must contain 16 digits.");
        }
        this.cardHolder = cardHolder;
        this.cardNumber = cardNumber;
    }

    public boolean process(double amount) throws InvalidPaymentException {
        if (amount <= 0) {
            throw new InvalidPaymentException("Invalid payment amount.");
        }
        return true;
    }

    public String getPaymentMethod() {
        return "Card";
    }

    public String getCardHolder() {
        return cardHolder;
    }
}
