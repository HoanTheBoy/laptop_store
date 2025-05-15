package dao;

import model.WarrantyRequest;
import java.util.List;

/**
 * Data Access Object interface for warranty requests
 */
public interface WarrantyRequestDAO {
    /**
     * Create a new warranty request in the database
     * 
     * @param warrantyRequest the warranty request to create
     * @return the created warranty request with ID assigned, or null if failed
     */
    WarrantyRequest createWarrantyRequest(WarrantyRequest warrantyRequest);

    /**
     * Get a warranty request by its ID
     * 
     * @param warrantyRequestId the ID of the warranty request
     * @return the warranty request, or null if not found
     */
    WarrantyRequest getWarrantyRequestById(int warrantyRequestId);

    /**
     * Get all warranty requests
     * 
     * @return a list of all warranty requests
     */
    List<WarrantyRequest> getAllWarrantyRequests();

    /**
     * Get all warranty requests for a specific user
     * 
     * @param userId the ID of the user
     * @return a list of warranty requests for the user
     */
    List<WarrantyRequest> getWarrantyRequestsByUserId(int userId);

    /**
     * Get all warranty requests with a specific status
     * 
     * @param status the status to filter by (e.g., "PENDING", "APPROVED",
     *               "REJECTED")
     * @return a list of warranty requests with the specified status
     */
    List<WarrantyRequest> getWarrantyRequestsByStatus(String status);

    /**
     * Update the status of a warranty request
     * 
     * @param warrantyRequestId the ID of the warranty request
     * @param status            the new status
     * @param adminNotes        admin notes about the decision
     * @return true if successful, false otherwise
     */
    boolean updateWarrantyRequestStatus(int warrantyRequestId, String status, String adminNotes);
}