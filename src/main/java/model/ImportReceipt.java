package model;

import java.time.LocalDateTime;

/**
 * Represents an inventory import receipt in the system
 */
public class ImportReceipt {
    private int id;
    private int laptopId;
    private int quantity;
    private LocalDateTime importDate;
    private String supplierName;
    private String supplierContact;
    private String notes;

    // Constructors
    public ImportReceipt() {
    }

    public ImportReceipt(int id, int laptopId, int quantity, LocalDateTime importDate,
            String supplierName, String supplierContact, String notes) {
        this.id = id;
        this.laptopId = laptopId;
        this.quantity = quantity;
        this.importDate = importDate;
        this.supplierName = supplierName;
        this.supplierContact = supplierContact;
        this.notes = notes;
    }

    // Getters and setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getLaptopId() {
        return laptopId;
    }

    public void setLaptopId(int laptopId) {
        this.laptopId = laptopId;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public LocalDateTime getImportDate() {
        return importDate;
    }

    public void setImportDate(LocalDateTime importDate) {
        this.importDate = importDate;
    }

    public String getSupplierName() {
        return supplierName;
    }

    public void setSupplierName(String supplierName) {
        this.supplierName = supplierName;
    }

    public String getSupplierContact() {
        return supplierContact;
    }

    public void setSupplierContact(String supplierContact) {
        this.supplierContact = supplierContact;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    @Override
    public String toString() {
        return "ImportReceipt{" +
                "id=" + id +
                ", laptopId=" + laptopId +
                ", quantity=" + quantity +
                ", importDate=" + importDate +
                ", supplierName='" + supplierName + '\'' +
                ", supplierContact='" + supplierContact + '\'' +
                ", notes='" + notes + '\'' +
                '}';
    }
}