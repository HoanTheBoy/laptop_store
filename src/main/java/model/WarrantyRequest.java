package model;

import java.time.LocalDateTime;

/**
 * Represents a warranty or return request in the system
 */
public class WarrantyRequest {
    private int id;
    private int orderId;
    private int laptopId;
    private int userId;
    private LocalDateTime requestDate;
    private String status; // PENDING, APPROVED, REJECTED
    private String reason;
    private String adminNotes;
    private LocalDateTime processedDate;

    // Constructors
    public WarrantyRequest() {
    }

    public WarrantyRequest(int id, int orderId, int laptopId, int userId, LocalDateTime requestDate,
            String status, String reason, String adminNotes, LocalDateTime processedDate) {
        this.id = id;
        this.orderId = orderId;
        this.laptopId = laptopId;
        this.userId = userId;
        this.requestDate = requestDate;
        this.status = status;
        this.reason = reason;
        this.adminNotes = adminNotes;
        this.processedDate = processedDate;
    }

    // Getters and setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getOrderId() {
        return orderId;
    }

    public void setOrderId(int orderId) {
        this.orderId = orderId;
    }

    public int getLaptopId() {
        return laptopId;
    }

    public void setLaptopId(int laptopId) {
        this.laptopId = laptopId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public LocalDateTime getRequestDate() {
        return requestDate;
    }

    public void setRequestDate(LocalDateTime requestDate) {
        this.requestDate = requestDate;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public String getAdminNotes() {
        return adminNotes;
    }

    public void setAdminNotes(String adminNotes) {
        this.adminNotes = adminNotes;
    }

    public LocalDateTime getProcessedDate() {
        return processedDate;
    }

    public void setProcessedDate(LocalDateTime processedDate) {
        this.processedDate = processedDate;
    }

    @Override
    public String toString() {
        return "WarrantyRequest{" +
                "id=" + id +
                ", orderId=" + orderId +
                ", laptopId=" + laptopId +
                ", userId=" + userId +
                ", requestDate=" + requestDate +
                ", status='" + status + '\'' +
                ", reason='" + reason + '\'' +
                ", processedDate=" + processedDate +
                '}';
    }
}