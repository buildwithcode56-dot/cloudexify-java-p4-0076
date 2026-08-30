package com.mycompany.ecommerceproductsystem;
public interface Payment 
{
    boolean process(double amount)throws InvalidPaymentException;
    String getPaymentMethod();
}
