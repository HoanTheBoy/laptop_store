package dao;

import model.Order;
import model.OrderItem;
import java.util.List;

/**
 * Data Access Object interface for Order entities
 */
public interface OrderDAO {
    /**
     * Find an order by ID
     * 
     * @param id the order ID to search for
     * @return the Order if found, null otherwise
     */
    Order findById(int id);

    /**
     * Create a new order
     * 
     * @param order the order to create
     * @return the created order with ID assigned
     */
    Order create(Order order);

    /**
     * Update an existing order
     * 
     * @param order the order to update
     * @return true if successful, false otherwise
     */
    boolean update(Order order);

    /**
     * Delete an order by ID
     * 
     * @param id the order ID to delete
     * @return true if successful, false otherwise
     */
    boolean delete(int id);

    /**
     * Get all orders
     * 
     * @return a list of all orders
     */
    List<Order> findAll();

    /**
     * Get orders by user ID
     * 
     * @param userId the user ID to filter by
     * @return a list of orders for the specified user
     */
    List<Order> findByUserId(int userId);

    /**
     * Get orders by status
     * 
     * @param status the status to filter by
     * @return a list of orders with the specified status
     */
    List<Order> findByStatus(String status);

    /**
     * Update the status of an order
     * 
     * @param orderId the order ID
     * @param status  the new status
     * @return true if successful, false otherwise
     */
    boolean updateStatus(int orderId, String status);

    /**
     * Add an order item to an order
     * 
     * @param item the order item to add
     * @return the created order item with ID assigned
     */
    OrderItem addOrderItem(OrderItem item);

    /**
     * Get all order items for an order
     * 
     * @param orderId the order ID
     * @return a list of order items for the specified order
     */
    List<OrderItem> getOrderItems(int orderId);
}