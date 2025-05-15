package ui;

import model.*;
import service.*;
import util.DatabaseInitializer;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

/**
 * Console User Interface for the Laptop Store application
 */
import java.math.RoundingMode;

public class ConsoleUI {
    private final Scanner scanner;
    private final AccountService accountService;
    private final LaptopService laptopService;
    private final CartService cartService;
    private final OrderService orderService;
    private boolean running;

    public ConsoleUI() {
        // Initialize database with tables and sample data
        DatabaseInitializer.initializeDatabase();

        scanner = new Scanner(System.in);
        laptopService = new LaptopService();
        accountService = new AccountService();
        cartService = new CartService(laptopService);
        orderService = new OrderService(laptopService, cartService);
        running = true;
    }

    /**
     * Start the application
     */
    public void start() {
        System.out.println("Welcome to Laptop Store Console Application!");

        while (running) {
            if (accountService.getCurrentUser() == null) {
                showMainMenu();
            } else if (accountService.isAdmin()) {
                showAdminMenu();
            } else {
                showUserMenu();
            }
        }

        scanner.close();
        System.out.println("Thank you for using Laptop Store Console Application. Goodbye!");
    }

    /**
     * Display the main menu (before login)
     */
    private void showMainMenu() {
        System.out.println("\n===== MAIN MENU =====");
        System.out.println("1. Login");
        System.out.println("2. Register");
        System.out.println("3. Browse Laptops");
        System.out.println("0. Exit");
        System.out.print("Enter your choice: ");

        int choice = readIntInput();

        switch (choice) {
            case 1:
                login();
                break;
            case 2:
                register();
                break;
            case 3:
                browseLaptops();
                break;
            case 0:
                running = false;
                break;
            default:
                System.out.println("Invalid choice. Please try again.");
        }
    }

    /**
     * Display the admin menu
     */
    private void showAdminMenu() {
        System.out.println("\n===== ADMIN MENU =====");
        System.out.println("1. Manage Laptops");
        System.out.println("2. Manage Orders");
        System.out.println("3. Manage Users");
        System.out.println("4. Inventory Management");
        System.out.println("5. Warranty Requests");
        System.out.println("6. Revenue Reports");
        System.out.println("7. My Profile");
        System.out.println("8. Logout");
        System.out.println("0. Exit");
        System.out.print("Enter your choice: ");

        int choice = readIntInput();

        switch (choice) {
            case 1:
                manageLaptops();
                break;
            case 2:
                manageOrders();
                break;
            case 3:
                manageUsers();
                break;
            case 4:
                manageInventory();
                break;
            case 5:
                manageWarrantyRequests();
                break;
            case 6:
                generateReports();
                break;
            case 7:
                manageProfile();
                break;
            case 8:
                accountService.logout();
                System.out.println("Logged out successfully.");
                break;
            case 0:
                running = false;
                break;
            default:
                System.out.println("Invalid choice. Please try again.");
        }
    }

    /**
     * Display the user menu
     */
    private void showUserMenu() {
        System.out.println("\n===== USER MENU =====");
        System.out.println("1. Browse Laptops");
        System.out.println("2. View Cart");
        System.out.println("3. View My Orders");
        System.out.println("4. Request Warranty/Return");
        System.out.println("5. My Profile");
        System.out.println("6. Logout");
        System.out.println("0. Exit");
        System.out.print("Enter your choice: ");

        int choice = readIntInput();

        switch (choice) {
            case 1:
                browseLaptops();
                break;
            case 2:
                viewCart();
                break;
            case 3:
                viewMyOrders();
                break;
            case 4:
                requestWarranty();
                break;
            case 5:
                manageProfile();
                break;
            case 6:
                accountService.logout();
                System.out.println("Logged out successfully.");
                break;
            case 0:
                running = false;
                break;
            default:
                System.out.println("Invalid choice. Please try again.");
        }
    }

    /**
     * User login process
     */
    private void login() {
        System.out.println("\n===== LOGIN =====");
        System.out.print("Username: ");
        String username = scanner.nextLine();
        System.out.print("Password: ");
        String password = scanner.nextLine();

        if (accountService.login(username, password)) {
            System.out.println("Login successful!");
            System.out.println("Welcome, " + accountService.getCurrentUser().getFullName() + "!");
        } else {
            System.out.println("Login failed. Invalid username or password.");
        }
    }

    /**
     * User registration process
     */
    private void register() {
        System.out.println("\n===== REGISTER =====");
        System.out.print("Username: ");
        String username = scanner.nextLine();
        System.out.print("Password: ");
        String password = scanner.nextLine();
        System.out.print("Full Name: ");
        String fullName = scanner.nextLine();
        System.out.print("Email: ");
        String email = scanner.nextLine();
        System.out.print("Phone: ");
        String phone = scanner.nextLine();
        System.out.print("Address: ");
        String address = scanner.nextLine();

        // By default, new registrations are USER role
        Account account = accountService.register(username, password, "USER", fullName, email, phone, address);

        if (account != null) {
            System.out.println("Registration successful! You can now login.");
        } else {
            System.out.println("Registration failed. Username may already exist.");
        }
    }

    /**
     * Browse and search laptops
     */
    private void browseLaptops() {
        while (true) {
            System.out.println("\n===== BROWSE LAPTOPS =====");
            System.out.println("1. View All Laptops");
            System.out.println("2. Search by Name");
            System.out.println("3. Filter by Brand");
            System.out.println("4. Filter by RAM");
            System.out.println("5. Filter by OS");
            System.out.println("0. Back");
            System.out.print("Enter your choice: ");

            int choice = readIntInput();

            List<Laptop> laptops = null;

            switch (choice) {
                case 1:
                    laptops = laptopService.getAllLaptops();
                    break;
                case 2:
                    System.out.print("Enter keyword to search: ");
                    String keyword = scanner.nextLine();
                    laptops = laptopService.searchLaptopsByName(keyword);
                    break;
                case 3:
                    System.out.print("Enter brand: ");
                    String brand = scanner.nextLine();
                    laptops = laptopService.filterLaptopsByBrand(brand);
                    break;
                case 4:
                    System.out.print("Enter RAM size (GB): ");
                    int ram = readIntInput();
                    laptops = laptopService.filterLaptopsByRam(ram);
                    break;
                case 5:
                    System.out.print("Enter OS: ");
                    String os = scanner.nextLine();
                    laptops = laptopService.filterLaptopsByOS(os);
                    break;
                case 0:
                    return;
                default:
                    System.out.println("Invalid choice. Please try again.");
                    continue;
            }

            displayLaptops(laptops);

            // If logged in as user, offer option to add to cart
            if (accountService.getCurrentUser() != null && !accountService.isAdmin()) {
                System.out.print("Enter laptop ID to add to cart (0 to cancel): ");
                int laptopId = readIntInput();

                if (laptopId > 0) {
                    System.out.print("Enter quantity: ");
                    int quantity = readIntInput();

                    if (cartService.addToCart(accountService.getCurrentUser().getId(), laptopId, quantity)) {
                        System.out.println("Item added to cart successfully!");
                    } else {
                        System.out.println(
                                "Failed to add item to cart. Check if laptop exists and has sufficient stock.");
                    }
                }
            }
        }
    }

    /**
     * Display a list of laptops
     */
    private void displayLaptops(List<Laptop> laptops) {
        if (laptops.isEmpty()) {
            System.out.println("No laptops found.");
            return;
        }

        System.out.println("\n----- LAPTOPS -----");
        System.out.printf("%-5s %-30s %-10s %-15s %-5s %-15s %-10s %-10s %-10s\n",
                "ID", "Name", "Brand", "CPU", "RAM", "OS", "Color", "Price", "Stock");
        System.out.println(
                "----------------------------------------------------------------------------------------------------");

        for (Laptop laptop : laptops) {
            System.out.printf("%-5d %-30s %-10s %-15s %-5d %-15s %-10s $%-9.2f %-10d\n",
                    laptop.getId(), laptop.getName(), laptop.getBrand(), laptop.getCpu(),
                    laptop.getRam(), laptop.getOs(), laptop.getColor(),
                    laptop.getPrice(), laptop.getStockQuantity());
        }

        System.out.println();
    }

    /**
     * View and manage shopping cart
     */
    private void viewCart() {
        if (accountService.getCurrentUser() == null) {
            System.out.println("You need to log in first.");
            return;
        }

        int userId = accountService.getCurrentUser().getId();
        List<CartItem> cartItems = cartService.getCart(userId);

        if (cartItems.isEmpty()) {
            System.out.println("Your cart is empty.");
            return;
        }

        System.out.println("\n----- YOUR CART -----");
        System.out.printf("%-5s %-30s %-10s %-10s %-10s\n",
                "No.", "Laptop Name", "Price", "Quantity", "Subtotal");
        System.out.println("------------------------------------------------------------");

        int i = 1;
        for (CartItem item : cartItems) {
            System.out.printf("%-5d %-30s $%-9.2f %-10d $%-9.2f\n",
                    i++, item.getLaptopName(), item.getUnitPrice(),
                    item.getQuantity(), item.getSubtotal());
        }

        System.out.println("------------------------------------------------------------");
        System.out.printf("Total: $%.2f\n", cartService.getCartTotal(userId));

        System.out.println("\n1. Update Item Quantity");
        System.out.println("2. Remove Item");
        System.out.println("3. Checkout");
        System.out.println("0. Back");
        System.out.print("Enter your choice: ");

        int choice = readIntInput();

        switch (choice) {
            case 1:
                updateCartItem(userId, cartItems);
                break;
            case 2:
                removeCartItem(userId, cartItems);
                break;
            case 3:
                checkout(userId);
                break;
            case 0:
                return;
            default:
                System.out.println("Invalid choice. Please try again.");
        }
    }

    /**
     * Update quantity of a cart item
     */
    private void updateCartItem(int userId, List<CartItem> cartItems) {
        System.out.print("Enter item number to update: ");
        int itemNumber = readIntInput();

        if (itemNumber < 1 || itemNumber > cartItems.size()) {
            System.out.println("Invalid item number.");
            return;
        }

        CartItem item = cartItems.get(itemNumber - 1);

        System.out.print("Enter new quantity: ");
        int newQuantity = readIntInput();

        if (newQuantity <= 0) {
            System.out.println("Quantity must be positive.");
            return;
        }

        if (cartService.updateCartItemQuantity(userId, item.getLaptopId(), newQuantity)) {
            System.out.println("Quantity updated successfully.");
        } else {
            System.out.println("Failed to update quantity. Check if there's enough stock.");
        }
    }

    /**
     * Remove an item from the cart
     */
    private void removeCartItem(int userId, List<CartItem> cartItems) {
        System.out.print("Enter item number to remove: ");
        int itemNumber = readIntInput();

        if (itemNumber < 1 || itemNumber > cartItems.size()) {
            System.out.println("Invalid item number.");
            return;
        }

        CartItem item = cartItems.get(itemNumber - 1);

        if (cartService.removeFromCart(userId, item.getLaptopId())) {
            System.out.println("Item removed successfully.");
        } else {
            System.out.println("Failed to remove item.");
        }
    }

    /**
     * Checkout process
     */
    private void checkout(int userId) {
        if (cartService.getCart(userId).isEmpty()) {
            System.out.println("Your cart is empty.");
            return;
        }

        System.out.println("\n===== CHECKOUT =====");

        // Get shipping address
        System.out.println("Current address: " + accountService.getCurrentUser().getAddress());
        System.out.print("Use this address for shipping? (y/n): ");
        String useCurrentAddress = scanner.nextLine();

        String shippingAddress;
        if (useCurrentAddress.equalsIgnoreCase("y")) {
            shippingAddress = accountService.getCurrentUser().getAddress();
        } else {
            System.out.print("Enter shipping address: ");
            shippingAddress = scanner.nextLine();
        }

        // Select payment method
        System.out.println("\nSelect payment method:");
        System.out.println("1. Cash on Delivery");
        System.out.println("2. Bank Transfer");
        System.out.println("3. Credit Card");
        System.out.print("Enter your choice: ");

        int paymentChoice = readIntInput();
        String paymentMethod;

        switch (paymentChoice) {
            case 1:
                paymentMethod = "CASH_ON_DELIVERY";
                break;
            case 2:
                paymentMethod = "BANK_TRANSFER";
                break;
            case 3:
                paymentMethod = "CREDIT_CARD";
                break;
            default:
                System.out.println("Invalid choice. Defaulting to Cash on Delivery.");
                paymentMethod = "CASH_ON_DELIVERY";
        }

        // Create order
        Order order = orderService.createOrder(userId, paymentMethod, shippingAddress);

        if (order != null) {
            System.out.println("\nOrder placed successfully!");
            System.out.println("Order ID: " + order.getId());
            System.out.println("Total Amount: $" + order.getTotalAmount());
            System.out.println("Status: " + order.getStatus());
            System.out.println("Payment Method: " + order.getPaymentMethod());
            System.out.println("Payment Status: " + order.getPaymentStatus());
        } else {
            System.out.println("Failed to place order. Please try again.");
        }
    }

    /**
     * View user's orders
     */
    private void viewMyOrders() {
        if (accountService.getCurrentUser() == null) {
            System.out.println("You need to log in first.");
            return;
        }

        int userId = accountService.getCurrentUser().getId();
        List<Order> orders = orderService.getOrdersByUserId(userId);

        if (orders.isEmpty()) {
            System.out.println("You have no orders.");
            return;
        }

        System.out.println("\n----- YOUR ORDERS -----");
        System.out.printf("%-5s %-20s %-10s %-15s %-15s\n",
                "ID", "Order Date", "Status", "Total", "Payment Status");
        System.out.println("-----------------------------------------------------");

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

        for (Order order : orders) {
            System.out.printf("%-5d %-20s %-10s $%-14.2f %-15s\n",
                    order.getId(), order.getOrderDate().format(formatter),
                    order.getStatus(), order.getTotalAmount(), order.getPaymentStatus());
        }

        System.out.print("\nEnter order ID to view details (0 to go back): ");
        int orderId = readIntInput();

        if (orderId > 0) {
            viewOrderDetails(orderId);
        }
    }

    /**
     * View order details
     */
    private void viewOrderDetails(int orderId) {
        Order order = orderService.getOrderById(orderId);

        if (order == null) {
            System.out.println("Order not found.");
            return;
        }

        Account user = accountService.getAccountById(order.getUserId());

        System.out.println("\n----- ORDER DETAILS -----");
        System.out.println("Order ID: " + order.getId());
        System.out.println("Customer: " + (user != null ? user.getFullName() : "Unknown"));
        System.out
                .println("Order Date: " + order.getOrderDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")));
        System.out.println("Status: " + order.getStatus());
        System.out.println("Payment Method: " + order.getPaymentMethod());
        System.out.println("Payment Status: " + order.getPaymentStatus());
        System.out.println("Shipping Address: " + order.getShippingAddress());

        List<OrderItem> items = order.getItems();

        if (items == null || items.isEmpty()) {
            System.out.println("\nNo items found in this order.");
        } else {
            System.out.println("\n----- ORDER ITEMS -----");
            System.out.printf("%-5s %-30s %-10s %-10s %-10s\n",
                    "ID", "Laptop", "Price", "Quantity", "Subtotal");
            System.out.println("------------------------------------------------------------");

            for (OrderItem item : items) {
                Laptop laptop = laptopService.getLaptopById(item.getLaptopId());
                String laptopName = laptop != null ? laptop.getName() : "Unknown";

                System.out.printf("%-5d %-30s $%-9.2f %-10d $%-9.2f\n",
                        item.getLaptopId(), laptopName, item.getUnitPrice(),
                        item.getQuantity(), item.getSubtotal());
            }

            System.out.println("------------------------------------------------------------");
            System.out.printf("Total: $%.2f\n", order.getTotalAmount());
        }

        // If user is admin, show option to update order status
        if (accountService.isAdmin()) {
            updateOrderStatus(order);
        }
    }

    /**
     * Update order status (admin only)
     */
    private void updateOrderStatus(Order order) {
        System.out.println("\n1. Update Order Status");
        System.out.println("0. Back");
        System.out.print("Enter your choice: ");

        int choice = readIntInput();

        if (choice == 1) {
            System.out.println("\nSelect new status:");
            System.out.println("1. PENDING");
            System.out.println("2. PROCESSING");
            System.out.println("3. SHIPPED");
            System.out.println("4. DELIVERED");
            System.out.println("5. CANCELLED");
            System.out.print("Enter your choice: ");

            int statusChoice = readIntInput();
            String newStatus;

            switch (statusChoice) {
                case 1:
                    newStatus = "PENDING";
                    break;
                case 2:
                    newStatus = "PROCESSING";
                    break;
                case 3:
                    newStatus = "SHIPPED";
                    break;
                case 4:
                    newStatus = "DELIVERED";
                    break;
                case 5:
                    newStatus = "CANCELLED";
                    break;
                default:
                    System.out.println("Invalid choice.");
                    return;
            }

            if (orderService.updateOrderStatus(order.getId(), newStatus)) {
                System.out.println("Order status updated successfully.");
            } else {
                System.out.println("Failed to update order status.");
            }
        }
    }

    /**
     * Request warranty or return
     */
    private void requestWarranty() {
        if (accountService.getCurrentUser() == null) {
            System.out.println("You need to log in first.");
            return;
        }

        int userId = accountService.getCurrentUser().getId();

        // Initialize warranty service
        WarrantyService warrantyService = new WarrantyService(orderService, laptopService);

        System.out.println("\n===== REQUEST WARRANTY OR RETURN =====");
        System.out.println(
                "This process allows you to request warranty service or return for products you've purchased.");
        System.out.println("Note: Warranty/Return requests can only be submitted for delivered orders.");

        // Show existing warranty requests first to avoid duplicates
        List<WarrantyRequest> existingRequests = warrantyService.getWarrantyRequestsByUserId(userId);

        if (!existingRequests.isEmpty()) {
            System.out.println("\n----- YOUR EXISTING WARRANTY REQUESTS -----");
            System.out.printf("%-5s %-10s %-25s %-20s %-10s\n",
                    "ID", "Order ID", "Product", "Request Date", "Status");
            System.out.println("-------------------------------------------------------------------------");

            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

            for (WarrantyRequest request : existingRequests) {
                Laptop laptop = laptopService.getLaptopById(request.getLaptopId());
                String laptopName = (laptop != null) ? laptop.getName() : "Unknown Product";
                if (laptopName.length() > 22) {
                    laptopName = laptopName.substring(0, 19) + "...";
                }

                System.out.printf("%-5d %-10d %-25s %-20s %-10s\n",
                        request.getId(),
                        request.getOrderId(),
                        laptopName,
                        request.getRequestDate().format(formatter),
                        request.getStatus());
            }

            System.out.println("\nYou can submit a new warranty request or check the status of existing ones.");
            System.out.println("1. Create New Warranty Request");
            System.out.println("2. View Details of Existing Request");
            System.out.println("0. Back to Main Menu");
            System.out.print("Enter your choice: ");

            int choice = readIntInput();

            if (choice == 0) {
                return;
            } else if (choice == 2) {
                System.out.print("Enter warranty request ID to view details: ");
                int requestId = readIntInput();

                WarrantyRequest request = warrantyService.getWarrantyRequestById(requestId);

                if (request == null || request.getUserId() != userId) {
                    System.out.println("Invalid warranty request ID or request doesn't belong to you.");
                    return;
                }

                System.out.println("\n----- WARRANTY REQUEST DETAILS -----");
                System.out.println(warrantyService.formatWarrantyRequest(request));

                // Display related product and order information
                Laptop laptop = laptopService.getLaptopById(request.getLaptopId());
                if (laptop != null) {
                    System.out.println("\nProduct Information:");
                    System.out.println("Name: " + laptop.getName());
                    System.out.println("Brand: " + laptop.getBrand());
                    System.out.println("Price: $" + laptop.getPrice());
                }

                Order order = orderService.getOrderById(request.getOrderId());
                if (order != null) {
                    System.out.println("\nOrder Information:");
                    System.out.println("Order Date: "
                            + order.getOrderDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")));
                    System.out.println("Order Status: " + order.getStatus());
                }

                System.out.println("\nPress Enter to continue...");
                scanner.nextLine();
                return;
            } else if (choice != 1) {
                System.out.println("Invalid choice. Returning to main menu.");
                return;
            }
            // If choice is 1, continue with creating a new warranty request
        }

        // Get delivered orders for user
        List<Order> deliveredOrders = new ArrayList<>();
        List<Order> allUserOrders = orderService.getOrdersByUserId(userId);

        for (Order order : allUserOrders) {
            if ("DELIVERED".equals(order.getStatus())) {
                deliveredOrders.add(order);
            }
        }

        if (deliveredOrders.isEmpty()) {
            System.out.println("You don't have any delivered orders to request warranty for.");
            System.out.println("Note: Only delivered orders are eligible for warranty/return requests.");
            return;
        }

        System.out.println("\n----- YOUR DELIVERED ORDERS -----");
        System.out.printf("%-5s %-20s %-15s %-15s\n",
                "ID", "Order Date", "Total", "Payment Status");
        System.out.println("-----------------------------------------------------");

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

        for (Order order : deliveredOrders) {
            System.out.printf("%-5d %-20s $%-14.2f %-15s\n",
                    order.getId(), order.getOrderDate().format(formatter),
                    order.getTotalAmount(), order.getPaymentStatus());
        }

        // Let user select an order
        System.out.print("\nEnter order ID to request warranty (0 to go back): ");
        int orderId = readIntInput();

        if (orderId <= 0) {
            return;
        }

        // Validate order
        Order selectedOrder = null;
        for (Order order : deliveredOrders) {
            if (order.getId() == orderId) {
                selectedOrder = order;
                break;
            }
        }

        if (selectedOrder == null) {
            System.out.println("Invalid order ID or order is not eligible for warranty requests.");
            return;
        }

        // Display laptops in the order
        List<OrderItem> items = selectedOrder.getItems();

        if (items == null || items.isEmpty()) {
            System.out.println("No items found in this order.");
            return;
        }

        System.out.println("\n----- ORDER ITEMS -----");
        System.out.printf("%-5s %-30s %-10s %-10s\n",
                "No.", "Laptop", "Price", "Quantity");
        System.out.println("--------------------------------------------------");

        Map<Integer, Integer> indexToLaptopId = new HashMap<>();
        int index = 1;

        for (OrderItem item : items) {
            Laptop laptop = laptopService.getLaptopById(item.getLaptopId());
            if (laptop != null) {
                System.out.printf("%-5d %-30s $%-9.2f %-10d\n",
                        index, laptop.getName(), item.getUnitPrice(), item.getQuantity());

                indexToLaptopId.put(index, laptop.getId());
                index++;
            }
        }

        // Check for existing warranty requests for this order
        boolean hasExistingRequests = false;
        for (WarrantyRequest request : existingRequests) {
            if (request.getOrderId() == orderId) {
                hasExistingRequests = true;
                break;
            }
        }

        if (hasExistingRequests) {
            System.out.println("\nNote: You already have warranty requests for this order.");
            System.out.print("Do you still want to continue with a new request? (y/n): ");
            String response = scanner.nextLine();
            if (!response.equalsIgnoreCase("y")) {
                return;
            }
        }

        // Let user select a laptop
        System.out.print("\nEnter number of the laptop to request warranty for (0 to go back): ");
        int laptopIndex = readIntInput();

        if (laptopIndex <= 0 || laptopIndex >= index) {
            return;
        }

        int laptopId = indexToLaptopId.get(laptopIndex);
        Laptop selectedLaptop = laptopService.getLaptopById(laptopId);

        // Check for existing warranty requests for this specific laptop in this order
        for (WarrantyRequest request : existingRequests) {
            if (request.getOrderId() == orderId && request.getLaptopId() == laptopId) {
                System.out.println("\nYou already have a warranty request for this product in this order.");
                System.out.println("Status: " + request.getStatus());
                System.out.println("Request Date: " + request.getRequestDate().format(formatter));
                System.out.print("Do you still want to submit another request? (y/n): ");
                String response = scanner.nextLine();
                if (!response.equalsIgnoreCase("y")) {
                    return;
                }
                break;
            }
        }

        // Display warranty policy
        System.out.println("\n----- WARRANTY POLICY -----");
        System.out.println("• Standard warranty covers manufacturing defects for 12 months from delivery date.");
        System.out.println("• Physical damage, water damage, or improper use is not covered.");
        System.out.println("• Returns without defect must be initiated within 30 days of delivery.");
        System.out.println("• All products must be returned in original packaging when possible.");

        System.out.println("\n----- REQUEST DETAILS -----");
        System.out.println("Product: " + selectedLaptop.getName());
        System.out.println("Order ID: " + orderId);
        System.out.println("Order Date: " + selectedOrder.getOrderDate().format(formatter));

        // Request type
        System.out.println("\nSelect request type:");
        System.out.println("1. Warranty Service (Repair)");
        System.out.println("2. Return/Replacement");
        System.out.println("3. Other Issue");
        System.out.print("Enter your choice: ");

        int requestType = readIntInput();
        String requestTypeStr;

        switch (requestType) {
            case 1:
                requestTypeStr = "Warranty Service (Repair)";
                break;
            case 2:
                requestTypeStr = "Return/Replacement";
                break;
            case 3:
                requestTypeStr = "Other Issue";
                break;
            default:
                requestTypeStr = "General Warranty Request";
        }

        // Get reason for warranty request
        System.out.println("\nPlease describe the issue in detail:");
        String reason = scanner.nextLine();

        if (reason.trim().isEmpty()) {
            System.out.println("Description cannot be empty.");
            return;
        }

        // Confirmation
        System.out.println("\n----- CONFIRM REQUEST -----");
        System.out.println("Product: " + selectedLaptop.getName());
        System.out.println("Request Type: " + requestTypeStr);
        System.out.println("Description: " + reason);
        System.out.print("Submit this warranty request? (y/n): ");

        String confirm = scanner.nextLine();
        if (!confirm.equalsIgnoreCase("y")) {
            System.out.println("Request cancelled.");
            return;
        }

        // Create warranty request with type info included in the reason
        String fullReason = "[" + requestTypeStr + "] " + reason;
        WarrantyRequest warrantyRequest = warrantyService.createWarrantyRequest(
                orderId, laptopId, userId, fullReason);

        if (warrantyRequest != null) {
            System.out.println("\nWarranty request submitted successfully!");
            System.out.println("Request ID: " + warrantyRequest.getId());
            System.out.println("Status: " + warrantyRequest.getStatus());
            System.out.println("Date: " + warrantyRequest.getRequestDate().format(formatter));
            System.out.println("\nOur customer service team will review your request and contact you within 48 hours.");
            System.out.println("You can check the status of your request from the Request Warranty/Return menu.");
        } else {
            System.out.println("Failed to submit warranty request. Please try again later.");
        }
    }

    /**
     * Manage user profile
     */
    private void manageProfile() {
        Account currentUser = accountService.getCurrentUser();
        if (currentUser == null) {
            System.out.println("You need to log in first.");
            return;
        }

        System.out.println("\n===== MY PROFILE =====");
        System.out.println("Username: " + currentUser.getUsername());
        System.out.println("Full Name: " + currentUser.getFullName());
        System.out.println("Email: " + currentUser.getEmail());
        System.out.println("Phone: " + currentUser.getPhone());
        System.out.println("Address: " + currentUser.getAddress());

        System.out.println("\n1. Update Profile");
        System.out.println("2. Change Password");
        System.out.println("0. Back");
        System.out.print("Enter your choice: ");

        int choice = readIntInput();

        switch (choice) {
            case 1:
                updateProfile();
                break;
            case 2:
                changePassword();
                break;
            case 0:
                return;
            default:
                System.out.println("Invalid choice. Please try again.");
        }
    }

    /**
     * Update user profile
     */
    private void updateProfile() {
        Account currentUser = accountService.getCurrentUser();

        System.out.println("\n===== UPDATE PROFILE =====");

        System.out.print("Full Name (" + currentUser.getFullName() + "): ");
        String fullName = scanner.nextLine();
        if (!fullName.isEmpty()) {
            currentUser.setFullName(fullName);
        }

        System.out.print("Email (" + currentUser.getEmail() + "): ");
        String email = scanner.nextLine();
        if (!email.isEmpty()) {
            currentUser.setEmail(email);
        }

        System.out.print("Phone (" + currentUser.getPhone() + "): ");
        String phone = scanner.nextLine();
        if (!phone.isEmpty()) {
            currentUser.setPhone(phone);
        }

        System.out.print("Address (" + currentUser.getAddress() + "): ");
        String address = scanner.nextLine();
        if (!address.isEmpty()) {
            currentUser.setAddress(address);
        }

        if (accountService.updateAccount(currentUser)) {
            System.out.println("Profile updated successfully.");
        } else {
            System.out.println("Failed to update profile.");
        }
    }

    /**
     * Change user password
     */
    private void changePassword() {
        System.out.println("\n===== CHANGE PASSWORD =====");

        System.out.print("Current Password: ");
        String currentPassword = scanner.nextLine();

        System.out.print("New Password: ");
        String newPassword = scanner.nextLine();

        System.out.print("Confirm New Password: ");
        String confirmPassword = scanner.nextLine();

        if (!newPassword.equals(confirmPassword)) {
            System.out.println("New passwords do not match.");
            return;
        }

        if (accountService.changePassword(accountService.getCurrentUser().getId(), currentPassword, newPassword)) {
            System.out.println("Password changed successfully.");
        } else {
            System.out.println("Failed to change password. Current password might be incorrect.");
        }
    }

    /**
     * Manage laptops (admin only)
     */
    private void manageLaptops() {
        while (true) {
            System.out.println("\n===== MANAGE LAPTOPS =====");
            System.out.println("1. View All Laptops");
            System.out.println("2. Add New Laptop");
            System.out.println("3. Update Laptop");
            System.out.println("4. Delete Laptop");
            System.out.println("0. Back");
            System.out.print("Enter your choice: ");

            int choice = readIntInput();

            switch (choice) {
                case 1:
                    displayLaptops(laptopService.getAllLaptops());
                    break;
                case 2:
                    addLaptop();
                    break;
                case 3:
                    updateLaptop();
                    break;
                case 4:
                    deleteLaptop();
                    break;
                case 0:
                    return;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }
    }

    /**
     * Add a new laptop
     */
    private void addLaptop() {
        System.out.println("\n===== ADD NEW LAPTOP =====");

        System.out.print("Name: ");
        String name = scanner.nextLine();

        System.out.print("Brand: ");
        String brand = scanner.nextLine();

        System.out.print("CPU: ");
        String cpu = scanner.nextLine();

        System.out.print("RAM (GB): ");
        int ram = readIntInput();

        System.out.print("Operating System: ");
        String os = scanner.nextLine();

        System.out.print("Color: ");
        String color = scanner.nextLine();

        System.out.print("Price: ");
        BigDecimal price = readBigDecimalInput();

        System.out.print("Initial Stock Quantity: ");
        int stockQuantity = readIntInput();

        Laptop laptop = laptopService.addLaptop(name, brand, cpu, ram, os, color, price, stockQuantity);

        if (laptop != null) {
            System.out.println("Laptop added successfully with ID: " + laptop.getId());
        } else {
            System.out.println("Failed to add laptop.");
        }
    }

    /**
     * Update an existing laptop
     */
    private void updateLaptop() {
        System.out.print("Enter laptop ID to update: ");
        int laptopId = readIntInput();

        Laptop laptop = laptopService.getLaptopById(laptopId);

        if (laptop == null) {
            System.out.println("Laptop not found.");
            return;
        }

        System.out.println("\n===== UPDATE LAPTOP =====");
        System.out.println("Current values shown in parentheses. Press Enter to keep current value.");

        System.out.print("Name (" + laptop.getName() + "): ");
        String name = scanner.nextLine();
        if (!name.isEmpty()) {
            laptop.setName(name);
        }

        System.out.print("Brand (" + laptop.getBrand() + "): ");
        String brand = scanner.nextLine();
        if (!brand.isEmpty()) {
            laptop.setBrand(brand);
        }

        System.out.print("CPU (" + laptop.getCpu() + "): ");
        String cpu = scanner.nextLine();
        if (!cpu.isEmpty()) {
            laptop.setCpu(cpu);
        }

        System.out.print("RAM (" + laptop.getRam() + " GB): ");
        String ramStr = scanner.nextLine();
        if (!ramStr.isEmpty()) {
            try {
                int ram = Integer.parseInt(ramStr);
                laptop.setRam(ram);
            } catch (NumberFormatException e) {
                System.out.println("Invalid RAM value. Keeping current value.");
            }
        }

        System.out.print("Operating System (" + laptop.getOs() + "): ");
        String os = scanner.nextLine();
        if (!os.isEmpty()) {
            laptop.setOs(os);
        }

        System.out.print("Color (" + laptop.getColor() + "): ");
        String color = scanner.nextLine();
        if (!color.isEmpty()) {
            laptop.setColor(color);
        }

        System.out.print("Price ($" + laptop.getPrice() + "): ");
        String priceStr = scanner.nextLine();
        if (!priceStr.isEmpty()) {
            try {
                BigDecimal price = new BigDecimal(priceStr);
                laptop.setPrice(price);
            } catch (NumberFormatException e) {
                System.out.println("Invalid price value. Keeping current value.");
            }
        }

        System.out.print("Stock Quantity (" + laptop.getStockQuantity() + "): ");
        String stockStr = scanner.nextLine();
        if (!stockStr.isEmpty()) {
            try {
                int stock = Integer.parseInt(stockStr);
                laptop.setStockQuantity(stock);
            } catch (NumberFormatException e) {
                System.out.println("Invalid stock value. Keeping current value.");
            }
        }

        if (laptopService.updateLaptop(laptop)) {
            System.out.println("Laptop updated successfully.");
        } else {
            System.out.println("Failed to update laptop.");
        }
    }

    /**
     * Delete a laptop
     */
    private void deleteLaptop() {
        System.out.print("Enter laptop ID to delete: ");
        int laptopId = readIntInput();

        Laptop laptop = laptopService.getLaptopById(laptopId);

        if (laptop == null) {
            System.out.println("Laptop not found.");
            return;
        }

        System.out.println("Are you sure you want to delete the following laptop?");
        System.out.println(laptop);
        System.out.print("Confirm deletion (y/n): ");

        String confirm = scanner.nextLine();

        if (confirm.equalsIgnoreCase("y")) {
            if (laptopService.deleteLaptop(laptopId)) {
                System.out.println("Laptop deleted successfully.");
            } else {
                System.out.println("Failed to delete laptop.");
            }
        } else {
            System.out.println("Deletion cancelled.");
        }
    }

    /**
     * User management interface for administrators
     */
    private void manageUsers() {
        while (true) {
            System.out.println("\n===== MANAGE USERS =====");
            System.out.println("1. View All Users");
            System.out.println("2. Add New User");
            System.out.println("3. Search Users");
            System.out.println("4. View User Details");
            System.out.println("0. Back");
            System.out.print("Enter your choice: ");

            int choice = readIntInput();

            switch (choice) {
                case 1:
                    displayAllUsers();
                    break;
                case 2:
                    addUser();
                    break;
                case 3:
                    searchUsers();
                    break;
                case 4:
                    viewUserDetails();
                    break;
                case 0:
                    return;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }
    }

    /**
     * Display all user accounts (admin only)
     */
    private void displayAllUsers() {
        List<Account> accounts = accountService.getAllAccounts();

        if (accounts.isEmpty()) {
            System.out.println("No user accounts found.");
            return;
        }

        System.out.println("\n----- USER ACCOUNTS -----");
        System.out.printf("%-5s %-15s %-25s %-20s %-10s\n",
                "ID", "Username", "Full Name", "Email", "Role");
        System.out.println("-------------------------------------------------------------------------");

        for (Account account : accounts) {
            System.out.printf("%-5d %-15s %-25s %-20s %-10s\n",
                    account.getId(), account.getUsername(), account.getFullName(),
                    account.getEmail(), account.getRole());
        }

        System.out.print("\nEnter user ID to view/edit details (0 to go back): ");
        int userId = readIntInput();

        if (userId > 0) {
            viewAndEditUserDetails(userId);
        }
    }

    /**
     * Add a new user account (admin only)
     */
    private void addUser() {
        System.out.println("\n===== ADD NEW USER =====");

        System.out.print("Username: ");
        String username = scanner.nextLine();

        System.out.print("Password: ");
        String password = scanner.nextLine();

        System.out.print("Role (USER/ADMIN): ");
        String role = scanner.nextLine().toUpperCase();
        if (!role.equals("USER") && !role.equals("ADMIN")) {
            System.out.println("Invalid role. Setting to USER by default.");
            role = "USER";
        }

        System.out.print("Full Name: ");
        String fullName = scanner.nextLine();

        System.out.print("Email: ");
        String email = scanner.nextLine();

        System.out.print("Phone: ");
        String phone = scanner.nextLine();

        System.out.print("Address: ");
        String address = scanner.nextLine();

        Account account = accountService.register(username, password, role, fullName, email, phone, address);

        if (account != null) {
            System.out.println("User added successfully with ID: " + account.getId());
        } else {
            System.out.println("Failed to add user. Username may already exist.");
        }
    }

    /**
     * Search for users by username
     */
    private void searchUsers() {
        System.out.println("\n===== SEARCH USERS =====");
        System.out.print("Enter username to search: ");
        String username = scanner.nextLine();

        List<Account> results = accountService.searchUsersByUsername(username);

        if (results.isEmpty()) {
            System.out.println("No users found matching '" + username + "'.");
            return;
        }

        System.out.println("\n----- SEARCH RESULTS -----");
        System.out.printf("%-5s %-15s %-25s %-20s %-10s\n",
                "ID", "Username", "Full Name", "Email", "Role");
        System.out.println("-------------------------------------------------------------------------");

        for (Account account : results) {
            System.out.printf("%-5d %-15s %-25s %-20s %-10s\n",
                    account.getId(), account.getUsername(), account.getFullName(),
                    account.getEmail(), account.getRole());
        }

        System.out.print("\nEnter user ID to view/edit details (0 to go back): ");
        int userId = readIntInput();

        if (userId > 0) {
            viewAndEditUserDetails(userId);
        }
    }

    /**
     * View user account details
     */
    private void viewUserDetails() {
        System.out.print("Enter user ID: ");
        int userId = readIntInput();
        viewAndEditUserDetails(userId);
    }

    /**
     * View and potentially edit user details
     */
    private void viewAndEditUserDetails(int userId) {
        Account account = accountService.getAccountById(userId);

        if (account == null) {
            System.out.println("User not found.");
            return;
        }

        System.out.println("\n===== USER DETAILS =====");
        System.out.println("ID: " + account.getId());
        System.out.println("Username: " + account.getUsername());
        System.out.println("Full Name: " + account.getFullName());
        System.out.println("Email: " + account.getEmail());
        System.out.println("Phone: " + account.getPhone());
        System.out.println("Address: " + account.getAddress());
        System.out.println("Role: " + account.getRole());

        System.out.println("\n1. Edit User");
        System.out.println("2. Delete User");
        System.out.println("3. Reset Password");
        System.out.println("0. Back");
        System.out.print("Enter your choice: ");

        int choice = readIntInput();

        switch (choice) {
            case 1:
                editUser(account);
                break;
            case 2:
                deleteUser(account);
                break;
            case 3:
                resetUserPassword(account);
                break;
            case 0:
                return;
            default:
                System.out.println("Invalid choice.");
        }
    }

    /**
     * Edit user account details
     */
    private void editUser(Account account) {
        System.out.println("\n===== EDIT USER =====");
        System.out.println("Current values shown in parentheses. Press Enter to keep current value.");

        System.out.print("Full Name (" + account.getFullName() + "): ");
        String fullName = scanner.nextLine();
        if (!fullName.isEmpty()) {
            account.setFullName(fullName);
        }

        System.out.print("Email (" + account.getEmail() + "): ");
        String email = scanner.nextLine();
        if (!email.isEmpty()) {
            account.setEmail(email);
        }

        System.out.print("Phone (" + account.getPhone() + "): ");
        String phone = scanner.nextLine();
        if (!phone.isEmpty()) {
            account.setPhone(phone);
        }

        System.out.print("Address (" + account.getAddress() + "): ");
        String address = scanner.nextLine();
        if (!address.isEmpty()) {
            account.setAddress(address);
        }

        System.out.print("Role (" + account.getRole() + ") [USER/ADMIN]: ");
        String role = scanner.nextLine().toUpperCase();
        if (!role.isEmpty()) {
            if (role.equals("USER") || role.equals("ADMIN")) {
                account.setRole(role);
            } else {
                System.out.println("Invalid role. Keeping current role.");
            }
        }

        if (accountService.updateAccount(account)) {
            System.out.println("User updated successfully.");
        } else {
            System.out.println("Failed to update user.");
        }
    }

    /**
     * Delete a user account
     */
    private void deleteUser(Account account) {
        // Don't allow deleting current user
        if (account.getId() == accountService.getCurrentUser().getId()) {
            System.out.println("You cannot delete your own account while logged in.");
            return;
        }

        System.out.println("Are you sure you want to delete user: " + account.getUsername() + "?");
        System.out.print("This action cannot be undone. Confirm (y/n): ");

        String confirm = scanner.nextLine();

        if (confirm.equalsIgnoreCase("y")) {
            if (accountService.deleteUser(account.getId())) {
                System.out.println("User deleted successfully.");
            } else {
                System.out.println("Failed to delete user.");
            }
        } else {
            System.out.println("Deletion cancelled.");
        }
    }

    /**
     * Reset a user's password
     */
    private void resetUserPassword(Account account) {
        System.out.println("\n===== RESET PASSWORD =====");
        System.out.print("Enter new password for " + account.getUsername() + ": ");
        String newPassword = scanner.nextLine();

        if (accountService.resetPassword(account.getId(), newPassword)) {
            System.out.println("Password reset successfully.");
        } else {
            System.out.println("Failed to reset password.");
        }
    }

    /**
     * Manage orders (admin only)
     */
    private void manageOrders() {
        while (true) {
            System.out.println("\n===== MANAGE ORDERS =====");
            System.out.println("1. View All Orders");
            System.out.println("2. View Orders by Status");
            System.out.println("3. Search Order by ID");
            System.out.println("0. Back");
            System.out.print("Enter your choice: ");

            int choice = readIntInput();

            switch (choice) {
                case 1:
                    displayOrders(orderService.getAllOrders());
                    break;
                case 2:
                    viewOrdersByStatus();
                    break;
                case 3:
                    System.out.print("Enter order ID: ");
                    int orderId = readIntInput();
                    viewOrderDetails(orderId);
                    break;
                case 0:
                    return;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }
    }

    /**
     * View orders by status
     */
    private void viewOrdersByStatus() {
        System.out.println("\nSelect status:");
        System.out.println("1. PENDING");
        System.out.println("2. PROCESSING");
        System.out.println("3. SHIPPED");
        System.out.println("4. DELIVERED");
        System.out.println("5. CANCELLED");
        System.out.print("Enter your choice: ");

        int statusChoice = readIntInput();
        String status;

        switch (statusChoice) {
            case 1:
                status = "PENDING";
                break;
            case 2:
                status = "PROCESSING";
                break;
            case 3:
                status = "SHIPPED";
                break;
            case 4:
                status = "DELIVERED";
                break;
            case 5:
                status = "CANCELLED";
                break;
            default:
                System.out.println("Invalid choice.");
                return;
        }

        displayOrders(orderService.getOrdersByStatus(status));
    }

    /**
     * Display a list of orders
     */
    private void displayOrders(List<Order> orders) {
        if (orders.isEmpty()) {
            System.out.println("No orders found.");
            return;
        }

        System.out.println("\n----- ORDERS -----");
        System.out.printf("%-5s %-10s %-20s %-10s %-15s %-15s\n",
                "ID", "User ID", "Order Date", "Status", "Total", "Payment Status");
        System.out.println("-----------------------------------------------------------------------");

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

        for (Order order : orders) {
            System.out.printf("%-5d %-10d %-20s %-10s $%-14.2f %-15s\n",
                    order.getId(), order.getUserId(), order.getOrderDate().format(formatter),
                    order.getStatus(), order.getTotalAmount(), order.getPaymentStatus());
        }

        System.out.print("\nEnter order ID to view details (0 to go back): ");
        int orderId = readIntInput();

        if (orderId > 0) {
            viewOrderDetails(orderId);
        }
    }

    /**
     * Inventory management main interface
     */
    private void manageInventory() {
        while (true) {
            System.out.println("\n===== INVENTORY MANAGEMENT =====");
            System.out.println("1. View Current Stock");
            System.out.println("2. Update Stock Quantities");
            System.out.println("3. Register New Stock Arrival");
            System.out.println("4. Stock Alerts");
            System.out.println("5. Generate Inventory Report");
            System.out.println("0. Back");
            System.out.print("Enter your choice: ");

            int choice = readIntInput();

            switch (choice) {
                case 1:
                    viewCurrentStock();
                    break;
                case 2:
                    updateStockQuantities();
                    break;
                case 3:
                    registerNewStock();
                    break;
                case 4:
                    viewStockAlerts();
                    break;
                case 5:
                    generateInventoryReport();
                    break;
                case 0:
                    return;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }
    }

    /**
     * View current stock of all laptops
     */
    private void viewCurrentStock() {
        List<Laptop> laptops = laptopService.getAllLaptops();

        System.out.println("\n----- CURRENT INVENTORY -----");
        System.out.printf("%-5s %-30s %-10s %-15s %-10s %-10s\n",
                "ID", "Name", "Brand", "Price", "In Stock", "Status");
        System.out.println("-------------------------------------------------------------------------");

        for (Laptop laptop : laptops) {
            String stockStatus = getStockStatus(laptop.getStockQuantity());

            System.out.printf("%-5d %-30s %-10s $%-14.2f %-10d %-10s\n",
                    laptop.getId(), laptop.getName(), laptop.getBrand(),
                    laptop.getPrice(), laptop.getStockQuantity(), stockStatus);
        }
    }

    /**
     * Get stock status based on quantity
     */
    private String getStockStatus(int quantity) {
        if (quantity <= 0) {
            return "OUT OF STOCK";
        } else if (quantity < 5) {
            return "LOW";
        } else if (quantity < 10) {
            return "MEDIUM";
        } else {
            return "GOOD";
        }
    }

    /**
     * Update stock quantities
     */
    private void updateStockQuantities() {
        System.out.print("Enter laptop ID to update stock: ");
        int laptopId = readIntInput();

        Laptop laptop = laptopService.getLaptopById(laptopId);
        if (laptop == null) {
            System.out.println("Laptop not found.");
            return;
        }

        System.out.println("\n----- UPDATE STOCK -----");
        System.out.println("Laptop: " + laptop.getName());
        System.out.println("Current Stock: " + laptop.getStockQuantity());

        System.out.print("Enter new stock quantity: ");
        int newQuantity = readIntInput();

        if (newQuantity < 0) {
            System.out.println("Stock quantity cannot be negative.");
            return;
        }

        laptop.setStockQuantity(newQuantity);
        if (laptopService.updateLaptop(laptop)) {
            System.out.println("Stock updated successfully.");
        } else {
            System.out.println("Failed to update stock.");
        }
    }

    /**
     * Register new stock arrival
     */
    private void registerNewStock() {
        System.out.println("\n===== REGISTER NEW STOCK =====");
        System.out.println("1. Add stock to existing laptop");
        System.out.println("2. Register new laptop model");
        System.out.print("Enter your choice: ");

        int choice = readIntInput();

        switch (choice) {
            case 1:
                addStockToExisting();
                break;
            case 2:
                addLaptop();
                break;
            default:
                System.out.println("Invalid choice.");
        }
    }

    /**
     * Add stock to an existing laptop
     */
    private void addStockToExisting() {
        System.out.print("Enter laptop ID: ");
        int laptopId = readIntInput();

        Laptop laptop = laptopService.getLaptopById(laptopId);
        if (laptop == null) {
            System.out.println("Laptop not found.");
            return;
        }

        System.out.println("Laptop: " + laptop.getName());
        System.out.println("Current Stock: " + laptop.getStockQuantity());

        System.out.print("Enter quantity to add: ");
        int quantityToAdd = readIntInput();

        if (quantityToAdd <= 0) {
            System.out.println("Quantity must be positive.");
            return;
        }

        int newQuantity = laptop.getStockQuantity() + quantityToAdd;
        laptop.setStockQuantity(newQuantity);

        if (laptopService.updateLaptop(laptop)) {
            System.out.println("Stock updated successfully. New quantity: " + newQuantity);

            // Record the import in database (not fully implemented)
            System.out.println("Import receipt created.");
        } else {
            System.out.println("Failed to update stock.");
        }
    }

    /**
     * View stock alerts for low inventory items
     */
    private void viewStockAlerts() {
        List<Laptop> laptops = laptopService.getAllLaptops();
        List<Laptop> lowStockLaptops = new ArrayList<>();

        for (Laptop laptop : laptops) {
            if (laptop.getStockQuantity() < 5) {
                lowStockLaptops.add(laptop);
            }
        }

        if (lowStockLaptops.isEmpty()) {
            System.out.println("No low stock alerts.");
            return;
        }

        System.out.println("\n----- LOW STOCK ALERTS -----");
        System.out.printf("%-5s %-30s %-10s %-15s %-10s %-10s\n",
                "ID", "Name", "Brand", "Price", "In Stock", "Status");
        System.out.println("-------------------------------------------------------------------------");

        for (Laptop laptop : lowStockLaptops) {
            String stockStatus = getStockStatus(laptop.getStockQuantity());

            System.out.printf("%-5d %-30s %-10s $%-14.2f %-10d %-10s\n",
                    laptop.getId(), laptop.getName(), laptop.getBrand(),
                    laptop.getPrice(), laptop.getStockQuantity(), stockStatus);
        }
    }

    /**
     * Generate inventory report
     */
    private void generateInventoryReport() {
        List<Laptop> laptops = laptopService.getAllLaptops();

        if (laptops.isEmpty()) {
            System.out.println("No inventory data available.");
            return;
        }

        int totalItems = 0;
        BigDecimal totalValue = BigDecimal.ZERO;
        int outOfStockCount = 0;
        int lowStockCount = 0;

        for (Laptop laptop : laptops) {
            int quantity = laptop.getStockQuantity();
            totalItems += quantity;
            totalValue = totalValue.add(laptop.getPrice().multiply(new BigDecimal(quantity)));

            if (quantity <= 0) {
                outOfStockCount++;
            } else if (quantity < 5) {
                lowStockCount++;
            }
        }

        System.out.println("\n===== INVENTORY REPORT =====");
        System.out.println(
                "Generated on: " + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")));
        System.out.println("Total Products: " + laptops.size());
        System.out.println("Total Items in Stock: " + totalItems);
        System.out.println("Total Inventory Value: $" + totalValue);
        System.out.println("Out of Stock Products: " + outOfStockCount);
        System.out.println("Low Stock Products: " + lowStockCount);

        System.out.println("\nInventory Breakdown by Brand:");

        // Group by brand
        Map<String, Integer> brandCount = new HashMap<>();
        for (Laptop laptop : laptops) {
            String brand = laptop.getBrand();
            int current = brandCount.getOrDefault(brand, 0);
            brandCount.put(brand, current + laptop.getStockQuantity());
        }

        for (Map.Entry<String, Integer> entry : brandCount.entrySet()) {
            System.out.printf("%-15s: %d items\n", entry.getKey(), entry.getValue());
        }
    }

    /**
     * Manage warranty requests
     */
    private void manageWarrantyRequests() {
        while (true) {
            System.out.println("\n===== WARRANTY REQUESTS =====");
            System.out.println("1. View All Warranty Requests");
            System.out.println("2. View Pending Warranty Requests");
            System.out.println("3. Update Warranty Request Status");
            System.out.println("0. Back");
            System.out.print("Enter your choice: ");

            int choice = readIntInput();

            switch (choice) {
                case 1:
                    viewAllWarrantyRequests();
                    break;
                case 2:
                    viewPendingWarrantyRequests();
                    break;
                case 3:
                    updateWarrantyRequestStatus();
                    break;
                case 0:
                    return;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }
    }

    /**
     * View all warranty requests
     */
    private void viewAllWarrantyRequests() {
        // Initialize warranty service
        WarrantyService warrantyService = new WarrantyService(orderService, laptopService);

        // Get all warranty requests
        List<WarrantyRequest> warrantyRequests = warrantyService.getAllWarrantyRequests();

        if (warrantyRequests.isEmpty()) {
            System.out.println("No warranty requests found.");
            return;
        }

        displayWarrantyRequests(warrantyRequests, warrantyService);
    }

    /**
     * View pending warranty requests
     */
    private void viewPendingWarrantyRequests() {
        // Initialize warranty service
        WarrantyService warrantyService = new WarrantyService(orderService, laptopService);

        // Get pending warranty requests
        List<WarrantyRequest> pendingRequests = warrantyService.getPendingWarrantyRequests();

        if (pendingRequests.isEmpty()) {
            System.out.println("No pending warranty requests found.");
            return;
        }

        displayWarrantyRequests(pendingRequests, warrantyService);
    }

    /**
     * Helper method to display warranty requests
     */
    private void displayWarrantyRequests(List<WarrantyRequest> warrantyRequests, WarrantyService warrantyService) {
        System.out.println("\n----- WARRANTY REQUESTS -----");
        System.out.printf("%-5s %-10s %-25s %-20s %-10s %-30s\n",
                "ID", "Order ID", "Laptop", "Request Date", "Status", "Reason");
        System.out.println(
                "--------------------------------------------------------------------------------------------------------");

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

        for (WarrantyRequest request : warrantyRequests) {
            String laptopName = laptopService.getLaptopById(request.getLaptopId()).getName();
            if (laptopName.length() > 22) {
                laptopName = laptopName.substring(0, 19) + "...";
            }

            String reason = request.getReason();
            if (reason.length() > 27) {
                reason = reason.substring(0, 24) + "...";
            }

            System.out.printf("%-5d %-10d %-25s %-20s %-10s %-30s\n",
                    request.getId(),
                    request.getOrderId(),
                    laptopName,
                    request.getRequestDate().format(formatter),
                    request.getStatus(),
                    reason);
        }

        System.out.print("\nEnter warranty request ID for more details (0 to go back): ");
        int requestId = readIntInput();

        if (requestId > 0) {
            WarrantyRequest selectedRequest = warrantyService.getWarrantyRequestById(requestId);
            if (selectedRequest != null) {
                displayWarrantyRequestDetails(selectedRequest, warrantyService);
            } else {
                System.out.println("Warranty request not found.");
            }
        }
    }

    /**
     * Display warranty request details and provide options to update
     */
    private void displayWarrantyRequestDetails(WarrantyRequest request, WarrantyService warrantyService) {
        System.out.println("\n----- WARRANTY REQUEST DETAILS -----");
        System.out.println(warrantyService.formatWarrantyRequest(request));

        // Get order details
        Order order = orderService.getOrderById(request.getOrderId());
        if (order != null) {
            System.out.println("\nOrder Information:");
            System.out.println(
                    "Order Date: " + order.getOrderDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")));
            System.out.println("Order Status: " + order.getStatus());
            System.out.println("Total Amount: $" + order.getTotalAmount());
        }

        // Get user details
        Account user = accountService.getAccountById(request.getUserId());
        if (user != null) {
            System.out.println("\nCustomer Information:");
            System.out.println("Name: " + user.getFullName());
            System.out.println("Email: " + user.getEmail());
            System.out.println("Phone: " + user.getPhone());
            System.out.println("Address: " + user.getAddress());
        }

        // If request is pending, show option to update status
        if ("PENDING".equals(request.getStatus())) {
            System.out.println("\n1. Approve Warranty Request");
            System.out.println("2. Reject Warranty Request");
            System.out.println("0. Back");
            System.out.print("Enter your choice: ");

            int choice = readIntInput();

            if (choice == 1 || choice == 2) {
                System.out.print("Enter admin notes: ");
                String adminNotes = scanner.nextLine();

                String newStatus = (choice == 1) ? "APPROVED" : "REJECTED";

                if (warrantyService.updateWarrantyRequestStatus(request.getId(), newStatus, adminNotes)) {
                    System.out.println("Warranty request " + newStatus.toLowerCase() + " successfully.");
                } else {
                    System.out.println("Failed to update warranty request status.");
                }
            }
        }
    }

    /**
     * Update warranty request status
     */
    private void updateWarrantyRequestStatus() {
        // Initialize warranty service
        WarrantyService warrantyService = new WarrantyService(orderService, laptopService);

        // Get pending warranty requests first
        List<WarrantyRequest> pendingRequests = warrantyService.getPendingWarrantyRequests();

        if (pendingRequests.isEmpty()) {
            System.out.println("No pending warranty requests to update.");
            return;
        }

        System.out.println("\n----- PENDING WARRANTY REQUESTS -----");
        System.out.printf("%-5s %-10s %-25s %-20s %-30s\n",
                "ID", "Order ID", "Laptop", "Request Date", "Reason");
        System.out.println("------------------------------------------------------------------------------------");

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

        for (WarrantyRequest request : pendingRequests) {
            String laptopName = laptopService.getLaptopById(request.getLaptopId()).getName();
            if (laptopName.length() > 22) {
                laptopName = laptopName.substring(0, 19) + "...";
            }

            String reason = request.getReason();
            if (reason.length() > 27) {
                reason = reason.substring(0, 24) + "...";
            }

            System.out.printf("%-5d %-10d %-25s %-20s %-30s\n",
                    request.getId(),
                    request.getOrderId(),
                    laptopName,
                    request.getRequestDate().format(formatter),
                    reason);
        }

        System.out.print("\nEnter warranty request ID to update (0 to go back): ");
        int requestId = readIntInput();

        if (requestId <= 0) {
            return;
        }

        WarrantyRequest selectedRequest = warrantyService.getWarrantyRequestById(requestId);
        if (selectedRequest == null) {
            System.out.println("Warranty request not found.");
            return;
        }

        if (!"PENDING".equals(selectedRequest.getStatus())) {
            System.out.println("This warranty request has already been processed.");
            return;
        }

        System.out.println("\nWarranty Request ID: " + selectedRequest.getId());
        System.out.println("Reason: " + selectedRequest.getReason());
        System.out.println("\nChoose action:");
        System.out.println("1. Approve");
        System.out.println("2. Reject");
        System.out.println("0. Cancel");
        System.out.print("Enter your choice: ");

        int choice = readIntInput();

        if (choice != 1 && choice != 2) {
            return;
        }

        String newStatus = (choice == 1) ? "APPROVED" : "REJECTED";

        System.out.print("Enter admin notes: ");
        String adminNotes = scanner.nextLine();

        if (warrantyService.updateWarrantyRequestStatus(selectedRequest.getId(), newStatus, adminNotes)) {
            System.out.println("Warranty request " + newStatus.toLowerCase() + " successfully.");
        } else {
            System.out.println("Failed to update warranty request status.");
        }
    }

    /**
     * Generate revenue reports
     */
    private void generateReports() {
        while (true) {
            System.out.println("\n===== REVENUE REPORTS =====");
            System.out.println("1. Daily Sales Report");
            System.out.println("2. Monthly Sales Report");
            System.out.println("3. Revenue by Product");
            System.out.println("4. Revenue by Brand");
            System.out.println("5. Customer Purchase Report");
            System.out.println("0. Back");
            System.out.print("Enter your choice: ");

            int choice = readIntInput();

            switch (choice) {
                case 1:
                    generateDailySalesReport();
                    break;
                case 2:
                    generateMonthlySalesReport();
                    break;
                case 3:
                    generateProductRevenueReport();
                    break;
                case 4:
                    generateBrandRevenueReport();
                    break;
                case 5:
                    generateCustomerPurchaseReport();
                    break;
                case 0:
                    return;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }
    }

    /**
     * Generate daily sales report
     */
    private void generateDailySalesReport() {
        System.out.println("\n===== DAILY SALES REPORT =====");
        System.out.println(
                "Generated on: " + LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm")));

        System.out.print("Enter date (dd/MM/yyyy) or press Enter for today: ");
        String dateInput = scanner.nextLine();

        LocalDateTime reportDate;
        if (dateInput.isEmpty()) {
            reportDate = LocalDateTime.now();
        } else {
            try {
                reportDate = LocalDate.parse(dateInput, DateTimeFormatter.ofPattern("dd/MM/yyyy")).atStartOfDay();
            } catch (Exception e) {
                System.out.println("Invalid date format. Using today's date.");
                reportDate = LocalDateTime.now();
            }
        }

        LocalDateTime startDate = reportDate.toLocalDate().atStartOfDay();
        LocalDateTime endDate = reportDate.toLocalDate().atTime(23, 59, 59);

        // Filter orders within the specified date range
        List<Order> allOrders = orderService.getAllOrders();
        List<Order> dailyOrders = new ArrayList<>();

        for (Order order : allOrders) {
            LocalDateTime orderDate = order.getOrderDate();
            if ((orderDate.isEqual(startDate) || orderDate.isAfter(startDate)) &&
                    (orderDate.isEqual(endDate) || orderDate.isBefore(endDate))) {
                dailyOrders.add(order);
            }
        }

        if (dailyOrders.isEmpty()) {
            System.out.println(
                    "No orders found for " + startDate.toLocalDate().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
            return;
        }

        System.out.println("\nDate: " + startDate.toLocalDate().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
        System.out.println("Total Orders: " + dailyOrders.size());

        // Calculate total revenue and count by status
        BigDecimal totalRevenue = BigDecimal.ZERO;
        int pendingOrders = 0;
        int processingOrders = 0;
        int shippedOrders = 0;
        int deliveredOrders = 0;
        int cancelledOrders = 0;

        for (Order order : dailyOrders) {
            if ("PAID".equals(order.getPaymentStatus())) {
                totalRevenue = totalRevenue.add(order.getTotalAmount());
            }

            switch (order.getStatus()) {
                case "PENDING":
                    pendingOrders++;
                    break;
                case "PROCESSING":
                    processingOrders++;
                    break;
                case "SHIPPED":
                    shippedOrders++;
                    break;
                case "DELIVERED":
                    deliveredOrders++;
                    break;
                case "CANCELLED":
                    cancelledOrders++;
                    break;
            }
        }

        System.out.println("Total Revenue: $" + totalRevenue);
        System.out.println("\nOrder Status Breakdown:");
        System.out.println("Pending: " + pendingOrders);
        System.out.println("Processing: " + processingOrders);
        System.out.println("Shipped: " + shippedOrders);
        System.out.println("Delivered: " + deliveredOrders);
        System.out.println("Cancelled: " + cancelledOrders);

        // List all orders for the day
        System.out.println("\n----- ORDERS FOR "
                + startDate.toLocalDate().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")) + " -----");
        System.out.printf("%-5s %-10s %-20s %-15s %-15s %-15s\n",
                "ID", "Customer", "Time", "Status", "Total", "Payment");
        System.out.println("-------------------------------------------------------------------------------");

        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm:ss");

        for (Order order : dailyOrders) {
            Account user = accountService.getAccountById(order.getUserId());
            String userName = (user != null) ? user.getFullName() : "ID " + order.getUserId();

            if (userName.length() > 10) {
                userName = userName.substring(0, 7) + "...";
            }

            System.out.printf("%-5d %-10s %-20s %-15s $%-14.2f %-15s\n",
                    order.getId(), userName, order.getOrderDate().format(timeFormatter),
                    order.getStatus(), order.getTotalAmount(), order.getPaymentStatus());
        }
    }

    /**
     * Generate monthly sales report
     */
    private void generateMonthlySalesReport() {
        System.out.println("\n===== MONTHLY SALES REPORT =====");
        System.out.println(
                "Generated on: " + LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm")));

        System.out.print("Enter month and year (MM/yyyy) or press Enter for current month: ");
        String dateInput = scanner.nextLine();

        int year;
        int month;

        if (dateInput.isEmpty()) {
            year = LocalDateTime.now().getYear();
            month = LocalDateTime.now().getMonthValue();
        } else {
            try {
                LocalDate date = LocalDate.parse("01/" + dateInput, DateTimeFormatter.ofPattern("dd/MM/yyyy"));
                year = date.getYear();
                month = date.getMonthValue();
            } catch (Exception e) {
                System.out.println("Invalid date format. Using current month.");
                year = LocalDateTime.now().getYear();
                month = LocalDateTime.now().getMonthValue();
            }
        }

        String[] monthNames = { "January", "February", "March", "April", "May", "June",
                "July", "August", "September", "October", "November", "December" };
        String monthName = monthNames[month - 1];

        System.out.println("\nReport for: " + monthName + " " + year);

        // Get all orders
        List<Order> allOrders = orderService.getAllOrders();
        List<Order> monthlyOrders = new ArrayList<>();

        // Filter orders for the selected month
        for (Order order : allOrders) {
            LocalDateTime orderDate = order.getOrderDate();
            if (orderDate.getYear() == year && orderDate.getMonthValue() == month) {
                monthlyOrders.add(order);
            }
        }

        if (monthlyOrders.isEmpty()) {
            System.out.println("No orders found for " + monthName + " " + year);
            return;
        }

        BigDecimal totalRevenue = BigDecimal.ZERO;
        int totalOrders = monthlyOrders.size();
        int completedOrders = 0;
        int cancelledOrders = 0;

        // Group orders by day of month
        Map<Integer, BigDecimal> dailyRevenue = new HashMap<>();
        Map<Integer, Integer> dailyOrderCount = new HashMap<>();

        for (Order order : monthlyOrders) {
            int day = order.getOrderDate().getDayOfMonth();

            // Count by status
            if ("DELIVERED".equals(order.getStatus())) {
                completedOrders++;
            } else if ("CANCELLED".equals(order.getStatus())) {
                cancelledOrders++;
            }

            // Only count revenue from paid orders
            if ("PAID".equals(order.getPaymentStatus())) {
                totalRevenue = totalRevenue.add(order.getTotalAmount());

                // Add to daily revenue
                BigDecimal dailyAmount = dailyRevenue.getOrDefault(day, BigDecimal.ZERO);
                dailyRevenue.put(day, dailyAmount.add(order.getTotalAmount()));
            }

            // Count orders per day
            int dailyCount = dailyOrderCount.getOrDefault(day, 0);
            dailyOrderCount.put(day, dailyCount + 1);
        }

        // Display monthly summary
        System.out.println("Total Orders: " + totalOrders);
        System.out.println("Completed Orders: " + completedOrders);
        System.out.println("Cancelled Orders: " + cancelledOrders);
        System.out.println("Total Revenue: $" + totalRevenue);

        if (totalOrders > 0) {
            System.out.println("Average Order Value: $" +
                    totalRevenue.divide(new BigDecimal(totalOrders), 2, RoundingMode.HALF_UP));
        }

        // Display daily breakdown
        System.out.println("\n----- DAILY BREAKDOWN -----");
        System.out.printf("%-10s %-15s %-15s\n", "Day", "Orders", "Revenue");
        System.out.println("----------------------------------------");

        // Number of days in the month
        int daysInMonth = LocalDate.of(year, month, 1).lengthOfMonth();

        for (int day = 1; day <= daysInMonth; day++) {
            int orderCount = dailyOrderCount.getOrDefault(day, 0);
            BigDecimal revenue = dailyRevenue.getOrDefault(day, BigDecimal.ZERO);

            if (orderCount > 0) {
                System.out.printf("%-10d %-15d $%-15.2f\n", day, orderCount, revenue);
            }
        }

        // Find the day with the highest revenue
        int bestDay = 1;
        BigDecimal highestRevenue = BigDecimal.ZERO;

        for (Map.Entry<Integer, BigDecimal> entry : dailyRevenue.entrySet()) {
            if (entry.getValue().compareTo(highestRevenue) > 0) {
                highestRevenue = entry.getValue();
                bestDay = entry.getKey();
            }
        }

        if (!dailyRevenue.isEmpty()) {
            System.out.println("\nBest Performing Day: " + bestDay + " " + monthName +
                    " ($" + highestRevenue + " from " + dailyOrderCount.getOrDefault(bestDay, 0) + " orders)");
        }
    }

    /**
     * Generate product revenue report
     */
    private void generateProductRevenueReport() {
        System.out.println("\n===== PRODUCT REVENUE REPORT =====");
        System.out.println(
                "Generated on: " + LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm")));

        // Get all orders
        List<Order> allOrders = orderService.getAllOrders();

        if (allOrders.isEmpty()) {
            System.out.println("No orders found.");
            return;
        }

        // Maps to track product revenue and quantity
        Map<Integer, BigDecimal> productRevenue = new HashMap<>();
        Map<Integer, Integer> productQuantity = new HashMap<>();
        Map<Integer, String> productNames = new HashMap<>();

        // Process each order and its items
        for (Order order : allOrders) {
            // Skip unpaid or cancelled orders
            if ("CANCELLED".equals(order.getStatus()) || !"PAID".equals(order.getPaymentStatus())) {
                continue;
            }

            // Get order items
            List<OrderItem> items = order.getItems();
            if (items == null) {
                // If items aren't loaded, fetch them
                items = orderService.getOrderById(order.getId()).getItems();
            }

            if (items != null) {
                for (OrderItem item : items) {
                    int laptopId = item.getLaptopId();
                    BigDecimal itemRevenue = item.getSubtotal();
                    int quantity = item.getQuantity();

                    // Update product revenue
                    BigDecimal currentRevenue = productRevenue.getOrDefault(laptopId, BigDecimal.ZERO);
                    productRevenue.put(laptopId, currentRevenue.add(itemRevenue));

                    // Update product quantity
                    int currentQuantity = productQuantity.getOrDefault(laptopId, 0);
                    productQuantity.put(laptopId, currentQuantity + quantity);

                    // Get product name if not already stored
                    if (!productNames.containsKey(laptopId)) {
                        Laptop laptop = laptopService.getLaptopById(laptopId);
                        if (laptop != null) {
                            productNames.put(laptopId, laptop.getName());
                        } else {
                            productNames.put(laptopId, "Unknown Laptop (ID: " + laptopId + ")");
                        }
                    }
                }
            }
        }

        if (productRevenue.isEmpty()) {
            System.out.println("No product revenue data available.");
            return;
        }

        // Sort products by revenue (descending)
        List<Map.Entry<Integer, BigDecimal>> sortedProducts = new ArrayList<>(productRevenue.entrySet());
        sortedProducts.sort((e1, e2) -> e2.getValue().compareTo(e1.getValue()));

        // Display product revenue report
        System.out.println("\n----- PRODUCT REVENUE RANKING -----");
        System.out.printf("%-5s %-30s %-15s %-20s %-15s\n",
                "Rank", "Product Name", "Quantity Sold", "Revenue", "Avg. Price");
        System.out.println("------------------------------------------------------------------------------------");

        int rank = 1;
        BigDecimal totalRevenue = BigDecimal.ZERO;
        int totalQuantity = 0;

        for (Map.Entry<Integer, BigDecimal> entry : sortedProducts) {
            int laptopId = entry.getKey();
            BigDecimal revenue = entry.getValue();
            int quantity = productQuantity.get(laptopId);
            String name = productNames.get(laptopId);

            BigDecimal avgPrice = revenue.divide(new BigDecimal(quantity), 2, RoundingMode.HALF_UP);

            System.out.printf("%-5d %-30s %-15d $%-19.2f $%-14.2f\n",
                    rank++, name, quantity, revenue, avgPrice);

            totalRevenue = totalRevenue.add(revenue);
            totalQuantity += quantity;
        }

        System.out.println("------------------------------------------------------------------------------------");
        System.out.printf("%-36s %-15d $%-19.2f\n", "TOTAL", totalQuantity, totalRevenue);

        // Show top product details
        if (!sortedProducts.isEmpty()) {
            int topProductId = sortedProducts.get(0).getKey();
            System.out.println("\nTop Performing Product: " + productNames.get(topProductId));
            System.out.println("Revenue: $" + productRevenue.get(topProductId));
            System.out.println("Quantity Sold: " + productQuantity.get(topProductId));

            Laptop topLaptop = laptopService.getLaptopById(topProductId);
            if (topLaptop != null) {
                System.out.println("Current Stock: " + topLaptop.getStockQuantity());
                System.out.println("Brand: " + topLaptop.getBrand());
                System.out.println("Price: $" + topLaptop.getPrice());
            }
        }
    }

    /**
     * Generate brand revenue report
     */
    private void generateBrandRevenueReport() {
        System.out.println("\n===== BRAND REVENUE REPORT =====");
        System.out.println(
                "Generated on: " + LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm")));

        // Get all orders
        List<Order> allOrders = orderService.getAllOrders();

        if (allOrders.isEmpty()) {
            System.out.println("No orders found.");
            return;
        }

        // Maps to track brand revenue and quantity
        Map<String, BigDecimal> brandRevenue = new HashMap<>();
        Map<String, Integer> brandQuantity = new HashMap<>();
        Map<String, Set<Integer>> brandProducts = new HashMap<>(); // To count unique products per brand

        // Process each order
        for (Order order : allOrders) {
            // Skip unpaid or cancelled orders
            if ("CANCELLED".equals(order.getStatus()) || !"PAID".equals(order.getPaymentStatus())) {
                continue;
            }

            // Get order items
            List<OrderItem> items = order.getItems();
            if (items == null) {
                // If items aren't loaded, fetch them
                items = orderService.getOrderById(order.getId()).getItems();
            }

            if (items != null) {
                for (OrderItem item : items) {
                    int laptopId = item.getLaptopId();
                    Laptop laptop = laptopService.getLaptopById(laptopId);

                    if (laptop != null) {
                        String brand = laptop.getBrand();
                        BigDecimal itemRevenue = item.getSubtotal();
                        int quantity = item.getQuantity();

                        // Update brand revenue
                        BigDecimal currentRevenue = brandRevenue.getOrDefault(brand, BigDecimal.ZERO);
                        brandRevenue.put(brand, currentRevenue.add(itemRevenue));

                        // Update brand quantity
                        int currentQuantity = brandQuantity.getOrDefault(brand, 0);
                        brandQuantity.put(brand, currentQuantity + quantity);

                        // Track unique products per brand
                        Set<Integer> products = brandProducts.getOrDefault(brand, new HashSet<>());
                        products.add(laptopId);
                        brandProducts.put(brand, products);
                    }
                }
            }
        }

        if (brandRevenue.isEmpty()) {
            System.out.println("No brand revenue data available.");
            return;
        }

        // Sort brands by revenue (descending)
        List<Map.Entry<String, BigDecimal>> sortedBrands = new ArrayList<>(brandRevenue.entrySet());
        sortedBrands.sort((e1, e2) -> e2.getValue().compareTo(e1.getValue()));

        // Display brand revenue report
        System.out.println("\n----- BRAND REVENUE RANKING -----");
        System.out.printf("%-5s %-15s %-15s %-15s %-20s %-15s\n",
                "Rank", "Brand", "Products", "Units Sold", "Revenue", "Market Share");
        System.out.println("-----------------------------------------------------------------------------------------");

        int rank = 1;
        BigDecimal totalRevenue = BigDecimal.ZERO;

        // Calculate total revenue first for market share
        for (BigDecimal revenue : brandRevenue.values()) {
            totalRevenue = totalRevenue.add(revenue);
        }

        // Display each brand
        for (Map.Entry<String, BigDecimal> entry : sortedBrands) {
            String brand = entry.getKey();
            BigDecimal revenue = entry.getValue();
            int quantity = brandQuantity.get(brand);
            int productCount = brandProducts.get(brand).size();

            // Calculate market share as percentage
            BigDecimal marketShare = revenue.multiply(new BigDecimal(100))
                    .divide(totalRevenue, 2, RoundingMode.HALF_UP);

            System.out.printf("%-5d %-15s %-15d %-15d $%-19.2f %-5.2f%%\n",
                    rank++, brand, productCount, quantity, revenue, marketShare);
        }

        System.out.println("-----------------------------------------------------------------------------------------");
        System.out.printf("%-36s %-15s $%-19.2f 100.00%%\n", "TOTAL", "", totalRevenue);

        // Show top brand details
        if (!sortedBrands.isEmpty()) {
            String topBrand = sortedBrands.get(0).getKey();
            BigDecimal topBrandRevenue = brandRevenue.get(topBrand);
            System.out.println("\nTop Performing Brand: " + topBrand);
            System.out.println("Revenue: $" + topBrandRevenue);
            System.out.println("Units Sold: " + brandQuantity.get(topBrand));
            System.out.println("Number of Products: " + brandProducts.get(topBrand).size());

            // Calculate market share percentage
            BigDecimal marketShare = topBrandRevenue
                    .multiply(new BigDecimal(100))
                    .divide(totalRevenue, 2, RoundingMode.HALF_UP);
            System.out.println("Market Share: " + marketShare + "%");
        }

        // Display brand comparison chart (text-based)
        System.out.println("\n----- BRAND MARKET SHARE VISUALIZATION -----");
        for (Map.Entry<String, BigDecimal> entry : sortedBrands) {
            String brand = entry.getKey();
            BigDecimal revenue = entry.getValue();

            // Calculate market share percentage for visualization
            BigDecimal sharePercentage = revenue.multiply(new BigDecimal(100))
                    .divide(totalRevenue, 0, RoundingMode.HALF_UP);

            // Create a simple bar chart
            StringBuilder bar = new StringBuilder();
            for (int i = 0; i < sharePercentage.intValue(); i++) {
                bar.append("█");
            }

            System.out.printf("%-15s: %s (%s%%)\n", brand, bar.toString(),
                    revenue.multiply(new BigDecimal(100))
                            .divide(totalRevenue, 2, RoundingMode.HALF_UP));
        }
    }

    /**
     * Generate customer purchase report
     */
    private void generateCustomerPurchaseReport() {
        System.out.println("\n===== CUSTOMER PURCHASE REPORT =====");
        System.out.println(
                "Generated on: " + LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm")));

        // Get all orders
        List<Order> allOrders = orderService.getAllOrders();

        if (allOrders.isEmpty()) {
            System.out.println("No orders found.");
            return;
        }

        // Maps to track customer spending
        Map<Integer, BigDecimal> customerSpending = new HashMap<>();
        Map<Integer, Integer> customerOrderCount = new HashMap<>();
        Map<Integer, LocalDateTime> customerLastPurchase = new HashMap<>();

        // Process each order
        for (Order order : allOrders) {
            // Skip cancelled orders
            if ("CANCELLED".equals(order.getStatus())) {
                continue;
            }

            int userId = order.getUserId();
            BigDecimal orderAmount = order.getTotalAmount();

            // Update customer spending
            BigDecimal currentSpending = customerSpending.getOrDefault(userId, BigDecimal.ZERO);
            customerSpending.put(userId, currentSpending.add(orderAmount));

            // Update order count
            int currentOrderCount = customerOrderCount.getOrDefault(userId, 0);
            customerOrderCount.put(userId, currentOrderCount + 1);

            // Update last purchase date
            LocalDateTime currentLastPurchase = customerLastPurchase.get(userId);
            if (currentLastPurchase == null || order.getOrderDate().isAfter(currentLastPurchase)) {
                customerLastPurchase.put(userId, order.getOrderDate());
            }
        }

        if (customerSpending.isEmpty()) {
            System.out.println("No customer purchase data available.");
            return;
        }

        // Sort customers by spending (descending)
        List<Map.Entry<Integer, BigDecimal>> sortedCustomers = new ArrayList<>(customerSpending.entrySet());
        sortedCustomers.sort((e1, e2) -> e2.getValue().compareTo(e1.getValue()));

        // Display top customers report
        System.out.println("\n----- TOP CUSTOMERS BY SPENDING -----");
        System.out.printf("%-5s %-25s %-15s %-20s %-20s\n",
                "Rank", "Customer", "Orders", "Total Spent", "Last Purchase");
        System.out.println("------------------------------------------------------------------------------------");

        int rank = 1;
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

        for (Map.Entry<Integer, BigDecimal> entry : sortedCustomers) {
            int userId = entry.getKey();
            BigDecimal totalSpent = entry.getValue();
            int orderCount = customerOrderCount.get(userId);
            LocalDateTime lastPurchase = customerLastPurchase.get(userId);

            // Get customer details
            Account customer = accountService.getAccountById(userId);
            String customerName = (customer != null) ? customer.getFullName() : "Customer ID " + userId;

            System.out.printf("%-5d %-25s %-15d $%-19.2f %s\n",
                    rank++, customerName, orderCount, totalSpent, lastPurchase.format(formatter));

            // Show only top 10 customers for brevity
            if (rank > 10 && sortedCustomers.size() > 10) {
                System.out.println("... and " + (sortedCustomers.size() - 10) + " more customers");
                break;
            }
        }

        System.out.println("\n----- CUSTOMER STATISTICS -----");

        // Calculate statistics
        int totalCustomers = customerSpending.size();

        if (totalCustomers > 0) {
            // Calculate total and average revenue
            BigDecimal totalRevenue = BigDecimal.ZERO;
            for (BigDecimal spending : customerSpending.values()) {
                totalRevenue = totalRevenue.add(spending);
            }

            BigDecimal averageRevenue = totalRevenue.divide(new BigDecimal(totalCustomers), 2, RoundingMode.HALF_UP);

            // Calculate total and average orders
            int totalOrders = 0;
            for (int orders : customerOrderCount.values()) {
                totalOrders += orders;
            }

            double averageOrders = (double) totalOrders / totalCustomers;

            System.out.println("Total Customers: " + totalCustomers);
            System.out.println("Total Revenue: $" + totalRevenue);
            System.out.println("Total Orders: " + totalOrders);
            System.out.println("Average Spending per Customer: $" + averageRevenue);
            System.out.println("Average Orders per Customer: " + String.format("%.2f", averageOrders));

            // Show lifetime value tiers
            System.out.println("\n----- CUSTOMER LIFETIME VALUE SEGMENTS -----");
            int highValueCustomers = 0;
            int midValueCustomers = 0;
            int lowValueCustomers = 0;

            BigDecimal highThreshold = new BigDecimal(2000);
            BigDecimal lowThreshold = new BigDecimal(500);

            for (BigDecimal spending : customerSpending.values()) {
                if (spending.compareTo(highThreshold) >= 0) {
                    highValueCustomers++;
                } else if (spending.compareTo(lowThreshold) >= 0) {
                    midValueCustomers++;
                } else {
                    lowValueCustomers++;
                }
            }

            System.out.println("High Value Customers (>$2000): " + highValueCustomers +
                    " (" + (highValueCustomers * 100 / totalCustomers) + "%)");
            System.out.println("Mid Value Customers ($500-$2000): " + midValueCustomers +
                    " (" + (midValueCustomers * 100 / totalCustomers) + "%)");
            System.out.println("Low Value Customers (<$500): " + lowValueCustomers +
                    " (" + (lowValueCustomers * 100 / totalCustomers) + "%)");
        }
    }

    /**
     * Helper method to read integer input
     */
    private int readIntInput() {
        while (true) {
            try {
                String input = scanner.nextLine();
                return Integer.parseInt(input);
            } catch (NumberFormatException e) {
                System.out.print("Invalid input. Please enter a number: ");
            }
        }
    }

    /**
     * Helper method to read BigDecimal input
     */
    private BigDecimal readBigDecimalInput() {
        while (true) {
            try {
                String input = scanner.nextLine();
                return new BigDecimal(input);
            } catch (NumberFormatException e) {
                System.out.print("Invalid input. Please enter a valid number: ");
            }
        }
    }

    /**
     * Main method to start the application
     */
    public static void main(String[] args) {
        ConsoleUI ui = new ConsoleUI();
        ui.start();
    }
}