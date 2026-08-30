package com.mycompany.ecommerceproductsystem;
public class OutOfStockException extends Exception 
{
    public OutOfStockException(String message) {
        super(message);
    }
}
