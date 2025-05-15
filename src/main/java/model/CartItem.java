package model;

import java.math.BigDecimal;

/**
 * Represents an item in a user's shopping cart
 */
public class CartItem {
    private int userId;
    private int laptopId;
    private int quantity;
    private BigDecimal unitPrice;
    private String laptopName; // For display purposes

    // Constructors
    public CartItem() {
    }

    public CartItem(int userId, int laptopId, int quantity, BigDecimal unitPrice, String laptopName) {
        this.userId = userId;
        this.laptopId = laptopId;
        this.quantity = quantity;
        this.unitPrice = unitPrice;
        this.laptopName = laptopName;
    }

    // Getters and setters
    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
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

    public BigDecimal getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(BigDecimal unitPrice) {
        this.unitPrice = unitPrice;
    }

    public String getLaptopName() {
        return laptopName;
    }

    public void setLaptopName(String laptopName) {
        this.laptopName = laptopName;
    }

    public BigDecimal getSubtotal() {
        if (unitPrice == null) {
            return BigDecimal.ZERO;
        }
        return unitPrice.multiply(BigDecimal.valueOf(quantity));
    }

    @Override
    public String toString() {
        return "CartItem{" +
                "laptopId=" + laptopId +
                ", laptopName='" + laptopName + '\'' +
                ", quantity=" + quantity +
                ", unitPrice=" + unitPrice +
                ", subtotal=" + getSubtotal() +
                '}';
    }
}