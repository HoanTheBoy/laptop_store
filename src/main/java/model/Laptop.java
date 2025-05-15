package model;

import java.math.BigDecimal;

/**
 * Represents a laptop in the system
 */
public class Laptop {
    private int id;
    private String name;
    private String brand;
    private String cpu;
    private int ram;
    private String os;
    private String color;
    private BigDecimal price;
    private int stockQuantity;

    // Constructors
    public Laptop() {
    }

    public Laptop(int id, String name, String brand, String cpu, int ram, String os,
            String color, BigDecimal price, int stockQuantity) {
        this.id = id;
        this.name = name;
        this.brand = brand;
        this.cpu = cpu;
        this.ram = ram;
        this.os = os;
        this.color = color;
        this.price = price;
        this.stockQuantity = stockQuantity;
    }

    // Getters and setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getCpu() {
        return cpu;
    }

    public void setCpu(String cpu) {
        this.cpu = cpu;
    }

    public int getRam() {
        return ram;
    }

    public void setRam(int ram) {
        this.ram = ram;
    }

    public String getOs() {
        return os;
    }

    public void setOs(String os) {
        this.os = os;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public int getStockQuantity() {
        return stockQuantity;
    }

    public void setStockQuantity(int stockQuantity) {
        this.stockQuantity = stockQuantity;
    }

    /**
     * Gets the model of the laptop (alias for getName())
     * 
     * @return the model name of the laptop
     */
    public String getModel() {
        return name;
    }

    @Override
    public String toString() {
        return "Laptop{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", brand='" + brand + '\'' +
                ", cpu='" + cpu + '\'' +
                ", ram=" + ram +
                ", os='" + os + '\'' +
                ", color='" + color + '\'' +
                ", price=" + price +
                ", stockQuantity=" + stockQuantity +
                '}';
    }
}