package dao.impl;

import dao.WarrantyRequestDAO;
import model.WarrantyRequest;
import util.DBConnection;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Implementation of the WarrantyRequestDAO interface
 */
public class WarrantyRequestDAOImpl implements WarrantyRequestDAO {

    /**
     * Create a new warranty request in the database
     */
    @Override
    public WarrantyRequest createWarrantyRequest(WarrantyRequest warrantyRequest) {
        String sql = "INSERT INTO warranty_requests (order_id, laptop_id, user_id, request_date, status, reason) " +
                "VALUES (?, ?, ?, ?, ?, ?)";

        try (Connection conn = DBConnection.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            pstmt.setInt(1, warrantyRequest.getOrderId());
            pstmt.setInt(2, warrantyRequest.getLaptopId());
            pstmt.setInt(3, warrantyRequest.getUserId());
            pstmt.setTimestamp(4, Timestamp.valueOf(LocalDateTime.now()));
            pstmt.setString(5, "PENDING"); // Default status is PENDING
            pstmt.setString(6, warrantyRequest.getReason());

            int affectedRows = pstmt.executeUpdate();

            if (affectedRows > 0) {
                try (ResultSet generatedKeys = pstmt.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        warrantyRequest.setId(generatedKeys.getInt(1));
                        warrantyRequest.setStatus("PENDING");
                        warrantyRequest.setRequestDate(LocalDateTime.now());
                        return warrantyRequest;
                    }
                }
            }
        } catch (SQLException e) {
            System.err.println("Error creating warranty request: " + e.getMessage());
        }

        return null;
    }

    /**
     * Get a warranty request by its ID
     */
    @Override
    public WarrantyRequest getWarrantyRequestById(int warrantyRequestId) {
        String sql = "SELECT * FROM warranty_requests WHERE id = ?";

        try (Connection conn = DBConnection.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, warrantyRequestId);

            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return extractWarrantyRequestFromResultSet(rs);
                }
            }
        } catch (SQLException e) {
            System.err.println("Error getting warranty request: " + e.getMessage());
        }

        return null;
    }

    /**
     * Get all warranty requests
     */
    @Override
    public List<WarrantyRequest> getAllWarrantyRequests() {
        List<WarrantyRequest> warrantyRequests = new ArrayList<>();
        String sql = "SELECT * FROM warranty_requests ORDER BY request_date DESC";

        try (Connection conn = DBConnection.getConnection();
                Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                warrantyRequests.add(extractWarrantyRequestFromResultSet(rs));
            }
        } catch (SQLException e) {
            System.err.println("Error getting all warranty requests: " + e.getMessage());
        }

        return warrantyRequests;
    }

    /**
     * Get all warranty requests for a specific user
     */
    @Override
    public List<WarrantyRequest> getWarrantyRequestsByUserId(int userId) {
        List<WarrantyRequest> warrantyRequests = new ArrayList<>();
        String sql = "SELECT * FROM warranty_requests WHERE user_id = ? ORDER BY request_date DESC";

        try (Connection conn = DBConnection.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, userId);

            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    warrantyRequests.add(extractWarrantyRequestFromResultSet(rs));
                }
            }
        } catch (SQLException e) {
            System.err.println("Error getting warranty requests by user: " + e.getMessage());
        }

        return warrantyRequests;
    }

    /**
     * Get all warranty requests with a specific status
     */
    @Override
    public List<WarrantyRequest> getWarrantyRequestsByStatus(String status) {
        List<WarrantyRequest> warrantyRequests = new ArrayList<>();
        String sql = "SELECT * FROM warranty_requests WHERE status = ? ORDER BY request_date DESC";

        try (Connection conn = DBConnection.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, status);

            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    warrantyRequests.add(extractWarrantyRequestFromResultSet(rs));
                }
            }
        } catch (SQLException e) {
            System.err.println("Error getting warranty requests by status: " + e.getMessage());
        }

        return warrantyRequests;
    }

    /**
     * Update the status of a warranty request
     */
    @Override
    public boolean updateWarrantyRequestStatus(int warrantyRequestId, String status, String adminNotes) {
        String sql = "UPDATE warranty_requests SET status = ?, admin_notes = ?, processed_date = ? WHERE id = ?";

        try (Connection conn = DBConnection.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, status);
            pstmt.setString(2, adminNotes);
            pstmt.setTimestamp(3, Timestamp.valueOf(LocalDateTime.now()));
            pstmt.setInt(4, warrantyRequestId);

            int affectedRows = pstmt.executeUpdate();
            return affectedRows > 0;
        } catch (SQLException e) {
            System.err.println("Error updating warranty request status: " + e.getMessage());
        }

        return false;
    }

    /**
     * Helper method to extract a warranty request from a result set
     */
    private WarrantyRequest extractWarrantyRequestFromResultSet(ResultSet rs) throws SQLException {
        WarrantyRequest warrantyRequest = new WarrantyRequest();

        warrantyRequest.setId(rs.getInt("id"));
        warrantyRequest.setOrderId(rs.getInt("order_id"));
        warrantyRequest.setLaptopId(rs.getInt("laptop_id"));
        warrantyRequest.setUserId(rs.getInt("user_id"));

        Timestamp requestDate = rs.getTimestamp("request_date");
        if (requestDate != null) {
            warrantyRequest.setRequestDate(requestDate.toLocalDateTime());
        }

        warrantyRequest.setStatus(rs.getString("status"));
        warrantyRequest.setReason(rs.getString("reason"));
        warrantyRequest.setAdminNotes(rs.getString("admin_notes"));

        Timestamp processedDate = rs.getTimestamp("processed_date");
        if (processedDate != null) {
            warrantyRequest.setProcessedDate(processedDate.toLocalDateTime());
        }

        return warrantyRequest;
    }
}