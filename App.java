package com.mycompany.ecommerceproductsystem;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.List;

public class App extends Application {

    private Stage stage;

    private final List<Product> products = new ArrayList<>();
    private final ShoppingCart cart = new ShoppingCart();
    private final List<Order> orderHistory = new ArrayList<>();

    private final List<String[]> savedOrders = new ArrayList<>();

    private int customerNumber = 1;
    private int orderNumber = 1001;

    public void start(Stage primaryStage) {
        this.stage = primaryStage;
        this.stage.setTitle("E-Commerce Product System");

        createProducts();
        loadSavedOrders();
        showWelcomeScreen();

        this.stage.show();
    }
    private void createProducts() {
        products.add(new Electronics("E101", "Wireless Headphones", 5000, 10, "SoundMax", 12));
        products.add(new Electronics("E102", "Smart Watch", 8500, 7, "TechTime", 18));
        products.add(new Book("B101", "Java Programming", 3000, 15, "James Gosling", "9781234567890", 450));
        products.add(new Book("B102", "Clean Coding", 2500, 8, "Robert Martin", "9780987654321", 350));
        products.add(new Clothing("C101", "Cotton Hoodie", 4000, 12, "Large", "Cotton", "Black"));
        products.add(new Clothing("C102", "Casual Shirt", 2800, 20, "Medium", "Linen", "White"));
    }

    private void loadSavedOrders() {
        List<String[]> loadedOrders = FileHandler.loadOrders();

        for (String[] data : loadedOrders) {
            try {
                String orderId = data[0];
                String customerId = data[1];
                String customerName = data[2];
                String status = data[4];

                Customer customer = new Customer(customerId, customerName);
                List<CartItem> items = new ArrayList<>();
                String savedItems = data[5];

                if (!savedItems.isEmpty()) {
                    String[] itemData = savedItems.split(";");
                    for (String item : itemData) {
                        if (item.isEmpty()) {
                            continue;
                        }
                        String[] parts = item.split(",");
                        String productId = parts[0];
                        int quantity = Integer.parseInt(parts[1]);

                        for (Product product : products) {
                            if (product.getProductId().equals(productId)) {
                                items.add(new CartItem(product, quantity));
                                break;
                            }
                        }
                    }
                }

                if (!items.isEmpty()) {
                    Order order = new Order(orderId, customer, items);
                    if ("CONFIRMED".equals(status)) {
                        order.confirm();
                    } else if ("SHIPPED".equals(status)) {
                        order.confirm();
                        order.ship();
                    } else if ("CANCELLED".equals(status)) {
                        order.cancel();
                    }

                    customer.addOrder(order);
                    orderHistory.add(order);
                }

                updateOrderAndCustomerNumbers(orderId, customerId);

            } catch (Exception e) {
                System.out.println("Unable to restore saved order: " + e.getMessage());
            }
        }
    }

    private void updateOrderAndCustomerNumbers(String orderId, String customerId) {
        try {
            if (orderId.startsWith("ORD-")) {
                int number = Integer.parseInt(orderId.substring(4));
                if (number >= orderNumber) {
                    orderNumber = number + 1;
                }
            }

            if (customerId.startsWith("C")) {
                int number = Integer.parseInt(customerId.substring(1));
                if (number >= customerNumber) {
                    customerNumber = number + 1;
                }
            }
        } catch (NumberFormatException e) {
            System.out.println("Unable to update numbering.");
        }
    }

    private void showWelcomeScreen() {
        VBox root = new VBox(20);
        root.setAlignment(Pos.CENTER);
        root.setPadding(new Insets(40));

        Label title = createLabel("E-Commerce Store", 32, true);
        Label subtitle = createLabel("Welcome to our online shopping experience", 17, false);
        Label description = createLabel("Browse products, add them to your cart, and place your order easily.", 14, false);
        Button startButton = createButton("Start Shopping", 180, 40, e -> showMainScreen());

        root.getChildren().addAll(title, subtitle, description, startButton);
        stage.setScene(new Scene(root, 700, 550));
    }

    private void showMainScreen() {
        VBox root = new VBox(20);
        root.setPadding(new Insets(35));
        root.setAlignment(Pos.CENTER);

        Label title = createLabel("E-Commerce Store", 30, true);
        Label subtitle = createLabel("Browse products and manage your shopping", 16, false);

        Button browseButton = createButton("Browse Products", 220, 0, e -> showProductsScreen());
        Button cartButton = createButton("Shopping Cart", 220, 0, e -> showCartScreen());
        Button historyButton = createButton("Order History", 220, 0, e -> showOrderHistory());
        Button exitButton = createButton("Exit", 220, 0, e -> handleExit());

        root.getChildren().addAll(title, subtitle, browseButton, cartButton, historyButton, exitButton);
        stage.setScene(new Scene(root, 750, 550));
    }

    private void showProductsScreen() {
        BorderPane root = new BorderPane();
        root.setPadding(new Insets(20));

        Label title = createLabel("Available Products", 25, true);
        root.setTop(title);

        VBox productList = new VBox(15);
        for (Product product : products) {
            productList.getChildren().add(createProductCard(product));
        }

        ScrollPane scrollPane = new ScrollPane(productList);
        scrollPane.setFitToWidth(true);
        root.setCenter(scrollPane);

        Button backButton = createButton("Back", 0, 0, e -> showMainScreen());
        HBox bottom = new HBox(backButton);
        bottom.setPadding(new Insets(15, 0, 0, 0));
        root.setBottom(bottom);

        Scene scene=new Scene(root,750,550);
        scene.setFill(javafx.scene.paint.Color.WHITE);
        stage.setScene(scene);
    }

    private HBox createProductCard(Product product) {
        VBox infoBox = new VBox(5);

        Label name = createLabel(product.getName(), 18, true);
        Label category = createLabel("Category: " + product.getCategory(), 14, false);
        Label price = createLabel(String.format("Price: Rs. %.2f", product.getFinalPrice()), 14, false);
        Label stock = new Label();

        if (product.getStock() == 0) {
            stock.setText("Out of Stock");
        } else if (product.getStock() <= 3) {
            stock.setText("Only " + product.getStock() + " left!");
        } else {
            stock.setText("Stock: " + product.getStock());
        }

        infoBox.getChildren().addAll(name, category, price, stock);

        Spinner<Integer> quantitySpinner = new Spinner<>(1, Math.max(1, product.getStock()), 1);
        Button addButton = createButton("Add to Cart", 0, 0, null);

        if (product.getStock() == 0) {
            addButton.setDisable(true);
            addButton.setText("Out of Stock");
        } else {
            addButton.setOnAction(event -> {
                try {
                    cart.addProduct(product, quantitySpinner.getValue());
                    showAlert(Alert.AlertType.INFORMATION, "Added", product.getName() + " added to cart.");
                } catch (OutOfStockException | IllegalArgumentException ex) {
                    showAlert(Alert.AlertType.ERROR, "Unable to Add", ex.getMessage());
                }
            });
        }

        VBox actionBox = new VBox(8, new Label("Quantity"), quantitySpinner, addButton);
        actionBox.setAlignment(Pos.CENTER);

        HBox card = new HBox(30, infoBox, actionBox);
        card.setAlignment(Pos.CENTER_LEFT);
        card.setPadding(new Insets(15));
        card.setStyle("-fx-border-color: #cccccc; -fx-border-radius: 8; -fx-background-radius: 8;");
        HBox.setHgrow(infoBox, Priority.ALWAYS);

        return card;
    }

    private void showCartScreen() {
        BorderPane root = new BorderPane();
        root.setPadding(new Insets(20));

        Label title = createLabel("Shopping Cart", 25, true);
        root.setTop(title);

        VBox content = new VBox(15);

        if (cart.isEmpty()) {
            content.getChildren().add(new Label("Your cart is empty."));
        } 
        else
        {
            for (CartItem item : cart.getItems()) {
                Label itemLabel = createLabel(
                        String.format("%s × %d = Rs. %.2f", item.getProduct().getName(), item.getQuantity(), item.getSubtotal()),
                        16,
                        false
                );
                content.getChildren().add(itemLabel);
            }

            Label total = createLabel(String.format("Total: Rs. %.2f", cart.getTotal()), 20, true);
            Button checkout = createButton("Proceed to Checkout", 0, 0, e -> showCheckoutScreen());
            Button clearCart = createButton("Clear Cart", 0, 0, e -> handleClearCart());

            content.getChildren().addAll(total, checkout, clearCart);
        }

        root.setCenter(content);

        Button back = createButton("Back", 0, 0, e -> showMainScreen());
        root.setBottom(back);

        stage.setScene(new Scene(root, 700, 550));
    }

    private void handleClearCart() {
        cart.clear();
        showCartScreen();
    }

    private void showCheckoutScreen() {
        if (cart.isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "Checkout", "Your cart is empty.");
            return;
        }

        VBox root = new VBox(15);
        root.setPadding(new Insets(30));

        Label title = createLabel("Checkout", 25, true);
        Label totalLabel = createLabel(String.format("Amount: Rs. %.2f", cart.getTotal()), 16, true);

        TextField cardHolder = new TextField();
        cardHolder.setPromptText("Card Holder Name");

        TextField cardNumber = new TextField();
        cardNumber.setPromptText("16-digit Card Number");

        Button payButton = createButton("Pay & Place Order", 0, 0, null);
        payButton.setOnAction(event -> processPayment(cardHolder.getText().trim(), cardNumber.getText().trim()));

        Button backButton = createButton("Back", 0, 0, e -> showCartScreen());

        root.getChildren().addAll(title, totalLabel, cardHolder, cardNumber, payButton, backButton);
        stage.setScene(new Scene(root, 600, 500));
    }

    private void processPayment(String customerName, String cardNumber) {
        try {
            if (customerName.isEmpty()) {
                throw new IllegalArgumentException("Please enter the card holder name.");
            }

            String customerId = String.format("C%03d", customerNumber++);
            Customer customer = new Customer(customerId, customerName);

            Payment payment = new CardPayment(customerName, cardNumber);
            payment.process(cart.getTotal());

            List<CartItem> orderItems = new ArrayList<>(cart.getItems());
            Order order = new Order("ORD-" + orderNumber++, customer, cart.getItems());

            order.confirm();

            for (CartItem item : orderItems) {
                item.getProduct().decreaseStock(item.getQuantity());
            }

            customer.addOrder(order);
            orderHistory.add(order);
            FileHandler.saveOrder(order);

            savedOrders.add(new String[]{
                    order.getOrderId(),
                    order.getCustomer().getName(),
                    String.valueOf(order.getTotalAmount()),
                    order.getStatus().name()
            });

            cart.clear();
            showOrderSuccessScreen(order);

        } catch (Exception ex) {
            showAlert(Alert.AlertType.ERROR, "Payment Failed", ex.getMessage());
        }
    }

    private void showOrderSuccessScreen(Order order) {
        Alert successAlert = new Alert(Alert.AlertType.INFORMATION);
        successAlert.setTitle("Order Successful");
        successAlert.setHeaderText("Order Placed Successfully!");
        successAlert.setContentText(null);

        ButtonType viewDetails = new ButtonType("View Details", ButtonBar.ButtonData.OK_DONE);
        ButtonType continueShopping = new ButtonType("Continue Shopping", ButtonBar.ButtonData.CANCEL_CLOSE);

        successAlert.getButtonTypes().setAll(viewDetails, continueShopping);

        successAlert.showAndWait().ifPresent(result -> {
            if (result == viewDetails) {
                showOrderDetails(order);
            } else if (result == continueShopping) {
                showProductsScreen();
            }
        });
    }
    
    private void showOrderHistory() {
        BorderPane root = new BorderPane();
        root.setPadding(new Insets(25));

        Label title = createLabel("Order History", 28, true);
        BorderPane.setAlignment(title, Pos.CENTER);
        root.setTop(title);

        VBox orderContainer = new VBox(18);
        orderContainer.setPadding(new Insets(25, 10, 25, 10));

        boolean hasOrders = !orderHistory.isEmpty() || !savedOrders.isEmpty();

        if (!hasOrders) {
            Label emptyLabel = createLabel("No orders placed yet.", 18, false);
            orderContainer.setAlignment(Pos.CENTER);
            orderContainer.getChildren().add(emptyLabel);
        } else {
            for (Order order : orderHistory) {
                orderContainer.getChildren().add(createOrderCard(order));
            }

            for (String[] data : savedOrders) {
                boolean alreadyDisplayed = false;
                for (Order order : orderHistory) {
                    if (order.getOrderId().equals(data[0])) {
                        alreadyDisplayed = true;
                        break;
                    }
                }
                if (!alreadyDisplayed) {
                    orderContainer.getChildren().add(createSavedOrderCard(data));
                }
            }
        }
        ScrollPane scrollPane = new ScrollPane(orderContainer);
        scrollPane.setFitToWidth(true);
        scrollPane.setStyle("-fx-background-color: transparent;");

        root.setCenter(scrollPane);

        Button backButton = createButton("Back", 0, 0, e -> showMainScreen());
        HBox bottom = new HBox(backButton);
        bottom.setPadding(new Insets(15, 0, 0, 0));
        root.setBottom(bottom);

        stage.setScene(new Scene(root, 750, 600));
    }
    
    private VBox createOrderCard(Order order) {
        VBox orderCard = new VBox(10);
        orderCard.setPadding(new Insets(18));
        orderCard.setStyle("-fx-border-color: #cfcfcf; -fx-border-radius: 10; -fx-background-radius: 10; -fx-background-color: #f8f8f8;");

        Label orderTitle = createLabel("Order #" + order.getOrderId(), 19, true);
        Label customerLabel = createLabel("Customer: " + order.getCustomer().getName(), 14, false);
        Label amountLabel = createLabel(String.format("Total Amount: Rs. %.2f", order.getTotalAmount()), 14, false);
        Label statusLabel = createLabel("Status: " + order.getStatus(), 14, true);

        Button detailsButton = createButton("View Details", 0, 0, e -> showOrderDetails(order));
        HBox buttonBox = new HBox(detailsButton);
        buttonBox.setAlignment(Pos.CENTER_RIGHT);

        orderCard.getChildren().addAll(orderTitle, customerLabel, amountLabel, statusLabel, buttonBox);

        return orderCard;
    }

    private VBox createSavedOrderCard(String[] data) {
        VBox orderCard = new VBox(10);
        orderCard.setPadding(new Insets(18));
        orderCard.setStyle("-fx-border-color: #cfcfcf; -fx-border-radius: 10; -fx-background-radius: 10; -fx-background-color: #f8f8f8;");

        Label orderTitle = createLabel("Order #" + data[0], 19, true);
        Label customerLabel = createLabel("Customer: " + data[1], 14, false);
        Label amountLabel = createLabel(String.format("Total Amount: Rs. %.2f", Double.parseDouble(data[2])), 14, false);
        Label statusLabel = createLabel("Status: " + data[3], 14, true);
        Label savedLabel = createLabel("Saved Order", 13, false);

        orderCard.getChildren().addAll(orderTitle, customerLabel, amountLabel, statusLabel, savedLabel);

        return orderCard;
    }

    private void showOrderDetails(Order order) {
        BorderPane root = new BorderPane();
        root.setPadding(new Insets(30));

        Label title = createLabel("Order Details", 30, true);
        BorderPane.setAlignment(title, Pos.CENTER);
        root.setTop(title);

        VBox orderInfoCard = new VBox(12);
        orderInfoCard.setPadding(new Insets(20));
        orderInfoCard.setStyle("-fx-background-color: #f4f4f4; -fx-background-radius: 10; -fx-border-color: #d0d0d0; -fx-border-radius: 10;");

        Label infoTitle = createLabel("Order Information", 19, true);
        Label orderId = createLabel("Order ID: " + order.getOrderId(), 14, false);
        Label customer = createLabel("Customer: " + order.getCustomer().getName(), 14, false);
        Label status = createLabel("Status: " + order.getStatus(), 14, true);

        orderInfoCard.getChildren().addAll(infoTitle, orderId, customer, status);

        Label productsTitle = createLabel("Order Items", 20, true);
        VBox productList = new VBox(10);

        for (CartItem item : order.getItems()) {
            productList.getChildren().add(createOrderItemRow(item));
        }

        Label total = createLabel(String.format("Total: Rs. %.2f", order.getTotalAmount()), 22, true);
        HBox totalBox = new HBox(total);
        totalBox.setAlignment(Pos.CENTER_RIGHT);

        Button backButton = createButton("Back to Order History", 0, 35, e -> showOrderHistory());
        HBox buttonBox = new HBox(backButton);
        buttonBox.setAlignment(Pos.CENTER);

        VBox content = new VBox(20);
        content.setPadding(new Insets(25, 10, 25, 10));
        content.getChildren().addAll(orderInfoCard, productsTitle, productList, totalBox, buttonBox);

        ScrollPane scrollPane = new ScrollPane(content);
        scrollPane.setFitToWidth(true);
        scrollPane.setStyle("-fx-background-color: transparent;");

        root.setCenter(scrollPane);

        stage.setScene(new Scene(root, 800, 650));
    }

    private HBox createOrderItemRow(CartItem item) {
        HBox itemRow = new HBox(20);
        itemRow.setPadding(new Insets(14));
        itemRow.setAlignment(Pos.CENTER_LEFT);
        itemRow.setStyle("-fx-background-color: white; -fx-border-color: #dddddd; -fx-border-radius: 8; -fx-background-radius: 8;");

        Label productName = createLabel(item.getProduct().getName(), 16, true);
        productName.setPrefWidth(300);

        Label quantity = createLabel("Qty: " + item.getQuantity(), 14, false);
        quantity.setPrefWidth(100);

        Label subtotal = createLabel(String.format("Rs. %.2f", item.getSubtotal()), 15, true);

        itemRow.getChildren().addAll(productName, quantity, subtotal);

        return itemRow;
    }

    private Label createLabel(String text, int fontSize, boolean bold) {
        Label label = new Label(text);
        if (bold) {
            label.setFont(Font.font("System", FontWeight.BOLD, fontSize));
        } else {
            label.setFont(Font.font("System", fontSize));
        }
        return label;
    }

    private Button createButton(String text, double width, double height, javafx.event.EventHandler<javafx.event.ActionEvent> action) {
        Button button = new Button(text);
        if (width > 0) {
            button.setPrefWidth(width);
        }
        if (height > 0) {
            button.setPrefHeight(height);
        }
        if (action != null) {
            button.setOnAction(action);
        }
        return button;
    }

    private void handleExit() {
    Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
    alert.setTitle("Exit");
    alert.setHeaderText("Are you sure you want to exit?");
    
    ButtonType yesButton = new ButtonType("Yes");
    ButtonType noButton = new ButtonType("No");
    alert.getButtonTypes().setAll(yesButton, noButton);

    alert.showAndWait().ifPresent(response -> {
        if (response == yesButton) {
            stage.close();
        }
    });
}
    private void showAlert(Alert.AlertType alertType, String title, String content) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
    public static void main(String[] args) {
        launch(args);
    }
}
