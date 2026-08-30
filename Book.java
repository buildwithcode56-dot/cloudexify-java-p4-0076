package com.mycompany.ecommerceproductsystem;

public class Book extends Product {

    private final String author;
    private final String isbn;
    private final int pages;

    public Book(String productId, String name, double price, int stock, String author, String isbn, int pages) {
        super(productId, name, price, stock);
        if (author == null || author.isBlank()) {
            throw new IllegalArgumentException("Author cannot be null or blank.");
        }
        if (isbn == null || isbn.isBlank()) {
            throw new IllegalArgumentException("ISBN cannot be null or blank.");
        }
        if (pages <= 0) {
            throw new IllegalArgumentException("Number of pages must be greater than zero.");
        }
        this.author = author.trim();
        this.isbn = isbn.trim();
        this.pages = pages;
    }
    public String getAuthor() {
        return author;
    }
    public String getIsbn() {
        return isbn;
    }

    public int getPages() {
        return pages;
    }
    public String getCategory() {
        return "Book";
    }
    public double calculateDiscount() {
        return getPrice() * 0.15;
    }
    public String toString() {
        return String.format("%s [Author: %s, ISBN: %s, Pages: %d]", super.toString(), author, isbn, pages);
    }
}
