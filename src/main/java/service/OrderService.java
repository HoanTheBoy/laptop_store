package service;

import dao.OrderDAO;
import dao.impl.OrderDAOImpl;
import model.Order;
import model.OrderItem;

import java.util.List;

/**
 * Service class for handling order operations
 */
public class OrderService {
    private final OrderDAO orderDAO;
    private final LaptopService laptopService;
    private final CartService cartService;

    public OrderService(LaptopService laptopService, CartService cartService) {
        this.orderDAO = new OrderDAOImpl();
        this.laptopService = laptopService;
        this.cartService = cartService;
    }

    /**
     * Create a new order from the user's cart
     * 
     * @param userId          the user ID
     * @param paymentMethod   the payment method
     * @param shippingAddress the shipping address
     * @return the created order if successful, null otherwise
     */
    public Order createOrder(int userId, String paymentMethod, String shippingAddress) {
        // Prepare order from cart
        Order order = cartService.prepareOrder(userId, shippingAddress);
        if (order == null) {
            return null; // Empty cart
        }

        // Set payment method
        order.setPaymentMethod(paymentMethod);

        // Create order in database
        Order createdOrder = orderDAO.create(order);
        if (createdOrder == null) {
            return null;
        }

        // Add order items
        for (OrderItem item : order.getItems()) {
            item.setOrderId(createdOrder.getId());
            orderDAO.addOrderItem(item);

            // Update stock
            laptopService.updateLaptopStock(item.getLaptopId(), -item.getQuantity());
        }

        // Clear the cart
        cartService.clearCart(userId);

        return createdOrder;
    }

    /**
     * Get an order by ID
     * 
     * @param orderId the order ID
     * @return the order if found, null otherwise
     */
    public Order getOrderById(int orderId) {
        Order order = orderDAO.findById(orderId);
        if (order != null) {
            List<OrderItem> items = orderDAO.getOrderItems(orderId);
            order.setItems(items);
        }
        return order;
    }

    /**
     * Get all orders
     * 
     * @return a list of all orders
     */
    public List<Order> getAllOrders() {
        return orderDAO.findAll();
    }

    /**
     * Get orders by user ID
     * 
     * @param userId the user ID
     * @return a list of orders for the specified user
     */
    public List<Order> getOrdersByUserId(int userId) {
        return orderDAO.findByUserId(userId);
    }

    /**
     * Get orders by status
     * 
     * @param status the order status
     * @return a list of orders with the specified status
     */
    public List<Order> getOrdersByStatus(String status) {
        return orderDAO.findByStatus(status);
    }

    /**
     * Update the status of an order
     * 
     * @param orderId the order ID
     * @param status  the new status
     * @return true if successful, false otherwise
     */
    public boolean updateOrderStatus(int orderId, String status) {
        return orderDAO.updateStatus(orderId, status);
    }

    /**
     * Update the payment status of an order
     * 
     * @param orderId       the order ID
     * @param paymentStatus the new payment status
     * @return true if successful, false otherwise
     */
    public boolean updatePaymentStatus(int orderId, String paymentStatus) {
        Order order = orderDAO.findById(orderId);
        if (order == null) {
            return false;
        }

        order.setPaymentStatus(paymentStatus);
        return orderDAO.update(order);
    }

    /**
     * Cancel an order
     * 
     * @param orderId the order ID
     * @return true if successful, false otherwise
     */
    public boolean cancelOrder(int orderId) {
        Order order = orderDAO.findById(orderId);
        if (order == null || !"PENDING".equals(order.getStatus())) {
            return false; // Can only cancel pending orders
        }

        // Update order status to CANCELLED
        if (!orderDAO.updateStatus(orderId, "CANCELLED")) {
            return false;
        }

        // Return items to inventory
        List<OrderItem> items = orderDAO.getOrderItems(orderId);
        for (OrderItem item : items) {
            laptopService.updateLaptopStock(item.getLaptopId(), item.getQuantity());
        }

        return true;
    }
}