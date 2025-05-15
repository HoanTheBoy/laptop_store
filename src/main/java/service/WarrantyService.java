package service;

import dao.WarrantyRequestDAO;
import dao.impl.WarrantyRequestDAOImpl;
import model.Order;
import model.WarrantyRequest;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

/**
 * Service class for warranty-related operations
 */
public class WarrantyService {
    private final WarrantyRequestDAO warrantyRequestDAO;
    private final OrderService orderService;
    private final LaptopService laptopService;

    public WarrantyService(OrderService orderService, LaptopService laptopService) {
        this.warrantyRequestDAO = new WarrantyRequestDAOImpl();
        this.orderService = orderService;
        this.laptopService = laptopService;
    }

    /**
     * Create a new warranty request
     * 
     * @param orderId  the ID of the order
     * @param laptopId the ID of the laptop
     * @param userId   the ID of the user
     * @param reason   the reason for the warranty request
     * @return the created warranty request, or null if failed
     */
    public WarrantyRequest createWarrantyRequest(int orderId, int laptopId, int userId, String reason) {
        // Validate that the order exists and belongs to the user
        Order order = orderService.getOrderById(orderId);
        if (order == null || order.getUserId() != userId) {
            return null;
        }

        // Create the warranty request
        WarrantyRequest warrantyRequest = new WarrantyRequest();
        warrantyRequest.setOrderId(orderId);
        warrantyRequest.setLaptopId(laptopId);
        warrantyRequest.setUserId(userId);
        warrantyRequest.setReason(reason);
        warrantyRequest.setRequestDate(LocalDateTime.now());
        warrantyRequest.setStatus("PENDING");

        return warrantyRequestDAO.createWarrantyRequest(warrantyRequest);
    }

    /**
     * Get a warranty request by its ID
     * 
     * @param warrantyRequestId the ID of the warranty request
     * @return the warranty request, or null if not found
     */
    public WarrantyRequest getWarrantyRequestById(int warrantyRequestId) {
        return warrantyRequestDAO.getWarrantyRequestById(warrantyRequestId);
    }

    /**
     * Get all warranty requests
     * 
     * @return a list of all warranty requests
     */
    public List<WarrantyRequest> getAllWarrantyRequests() {
        return warrantyRequestDAO.getAllWarrantyRequests();
    }

    /**
     * Get all warranty requests for a specific user
     * 
     * @param userId the ID of the user
     * @return a list of warranty requests for the user
     */
    public List<WarrantyRequest> getWarrantyRequestsByUserId(int userId) {
        return warrantyRequestDAO.getWarrantyRequestsByUserId(userId);
    }

    /**
     * Get all warranty requests with a specific status
     * 
     * @param status the status to filter by (e.g., "PENDING", "APPROVED",
     *               "REJECTED")
     * @return a list of warranty requests with the specified status
     */
    public List<WarrantyRequest> getWarrantyRequestsByStatus(String status) {
        return warrantyRequestDAO.getWarrantyRequestsByStatus(status);
    }

    /**
     * Get all pending warranty requests
     * 
     * @return a list of pending warranty requests
     */
    public List<WarrantyRequest> getPendingWarrantyRequests() {
        return warrantyRequestDAO.getWarrantyRequestsByStatus("PENDING");
    }

    /**
     * Update the status of a warranty request
     * 
     * @param warrantyRequestId the ID of the warranty request
     * @param status            the new status
     * @param adminNotes        admin notes about the decision
     * @return true if successful, false otherwise
     */
    public boolean updateWarrantyRequestStatus(int warrantyRequestId, String status, String adminNotes) {
        return warrantyRequestDAO.updateWarrantyRequestStatus(warrantyRequestId, status, adminNotes);
    }

    /**
     * Format a warranty request for display
     * 
     * @param warrantyRequest the warranty request to format
     * @return a formatted string representation of the warranty request
     */
    public String formatWarrantyRequest(WarrantyRequest warrantyRequest) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        StringBuilder sb = new StringBuilder();

        sb.append("ID: ").append(warrantyRequest.getId())
                .append(" | Order ID: ").append(warrantyRequest.getOrderId())
                .append(" | Laptop: ").append(laptopService.getLaptopById(warrantyRequest.getLaptopId()).getModel())
                .append(" | Date: ").append(warrantyRequest.getRequestDate().format(formatter))
                .append(" | Status: ").append(warrantyRequest.getStatus())
                .append(" | Reason: ").append(warrantyRequest.getReason());

        if (warrantyRequest.getProcessedDate() != null) {
            sb.append(" | Processed: ").append(warrantyRequest.getProcessedDate().format(formatter));
        }

        if (warrantyRequest.getAdminNotes() != null && !warrantyRequest.getAdminNotes().isEmpty()) {
            sb.append(" | Notes: ").append(warrantyRequest.getAdminNotes());
        }

        return sb.toString();
    }
}