package service;

import dao.OrderDAO;
import model.CartItem;
import model.Laptop;
import model.Order;
import model.OrderItem;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Service class for handling shopping cart operations
 */
public class CartService {
    private final Map<Integer, List<CartItem>> userCarts; // userId -> list of cart items
    private final LaptopService laptopService;

    public CartService(LaptopService laptopService) {
        this.userCarts = new HashMap<>();
        this.laptopService = laptopService;
    }

    /**
     * Add an item to the user's cart
     * 
     * @param userId   the user ID
     * @param laptopId the laptop ID
     * @param quantity the quantity to add
     * @return true if successful, false otherwise
     */
    public boolean addToCart(int userId, int laptopId, int quantity) {
        Laptop laptop = laptopService.getLaptopById(laptopId);
        if (laptop == null || laptop.getStockQuantity() < quantity) {
            return false; // Laptop not found or insufficient stock
        }

        List<CartItem> cart = userCarts.computeIfAbsent(userId, k -> new ArrayList<>());

        // Check if the laptop is already in the cart
        for (CartItem item : cart) {
            if (item.getLaptopId() == laptopId) {
                item.setQuantity(item.getQuantity() + quantity);
                return true;
            }
        }

        // Add new cart item
        CartItem cartItem = new CartItem(userId, laptopId, quantity, laptop.getPrice(), laptop.getName());
        cart.add(cartItem);
        return true;
    }

    /**
     * Remove an item from the user's cart
     * 
     * @param userId   the user ID
     * @param laptopId the laptop ID
     * @return true if successful, false otherwise
     */
    public boolean removeFromCart(int userId, int laptopId) {
        List<CartItem> cart = userCarts.get(userId);
        if (cart == null) {
            return false;
        }

        for (int i = 0; i < cart.size(); i++) {
            if (cart.get(i).getLaptopId() == laptopId) {
                cart.remove(i);
                return true;
            }
        }

        return false;
    }

    /**
     * Update the quantity of an item in the user's cart
     * 
     * @param userId   the user ID
     * @param laptopId the laptop ID
     * @param quantity the new quantity
     * @return true if successful, false otherwise
     */
    public boolean updateCartItemQuantity(int userId, int laptopId, int quantity) {
        List<CartItem> cart = userCarts.get(userId);
        if (cart == null) {
            return false;
        }

        for (CartItem item : cart) {
            if (item.getLaptopId() == laptopId) {
                Laptop laptop = laptopService.getLaptopById(laptopId);
                if (laptop == null || laptop.getStockQuantity() < quantity) {
                    return false; // Laptop not found or insufficient stock
                }

                item.setQuantity(quantity);
                return true;
            }
        }

        return false;
    }

    /**
     * Get all items in the user's cart
     * 
     * @param userId the user ID
     * @return list of cart items
     */
    public List<CartItem> getCart(int userId) {
        return userCarts.getOrDefault(userId, new ArrayList<>());
    }

    /**
     * Clear the user's cart
     * 
     * @param userId the user ID
     */
    public void clearCart(int userId) {
        userCarts.remove(userId);
    }

    /**
     * Calculate the total price of all items in the user's cart
     * 
     * @param userId the user ID
     * @return the total price
     */
    public BigDecimal getCartTotal(int userId) {
        List<CartItem> cart = userCarts.getOrDefault(userId, new ArrayList<>());
        BigDecimal total = BigDecimal.ZERO;

        for (CartItem item : cart) {
            total = total.add(item.getSubtotal());
        }

        return total;
    }

    /**
     * Prepare an order from the user's cart
     * 
     * @param userId          the user ID
     * @param shippingAddress the shipping address
     * @return a new Order object with items from the cart
     */
    public Order prepareOrder(int userId, String shippingAddress) {
        List<CartItem> cart = userCarts.getOrDefault(userId, new ArrayList<>());
        if (cart.isEmpty()) {
            return null;
        }

        Order order = new Order();
        order.setUserId(userId);
        order.setOrderDate(LocalDateTime.now());
        order.setStatus("PENDING");
        order.setTotalAmount(getCartTotal(userId));
        order.setShippingAddress(shippingAddress);
        order.setPaymentMethod("PENDING");
        order.setPaymentStatus("UNPAID");

        for (CartItem cartItem : cart) {
            OrderItem orderItem = new OrderItem();
            orderItem.setLaptopId(cartItem.getLaptopId());
            orderItem.setQuantity(cartItem.getQuantity());
            orderItem.setUnitPrice(cartItem.getUnitPrice());
            order.addItem(orderItem);
        }

        return order;
    }
}