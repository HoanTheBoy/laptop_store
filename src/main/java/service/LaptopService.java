package service;

import dao.LaptopDAO;
import dao.impl.LaptopDAOImpl;
import model.Laptop;

import java.math.BigDecimal;
import java.util.List;

/**
 * Service class for handling laptop operations
 */
public class LaptopService {
    private final LaptopDAO laptopDAO;

    public LaptopService() {
        this.laptopDAO = new LaptopDAOImpl();
    }

    /**
     * Add a new laptop to the system
     * 
     * @param name          the laptop name
     * @param brand         the brand
     * @param cpu           the CPU
     * @param ram           the RAM size in GB
     * @param os            the operating system
     * @param color         the color
     * @param price         the price
     * @param stockQuantity the initial stock quantity
     * @return the created laptop if successful, null otherwise
     */
    public Laptop addLaptop(String name, String brand, String cpu, int ram,
            String os, String color, BigDecimal price, int stockQuantity) {
        Laptop laptop = new Laptop();
        laptop.setName(name);
        laptop.setBrand(brand);
        laptop.setCpu(cpu);
        laptop.setRam(ram);
        laptop.setOs(os);
        laptop.setColor(color);
        laptop.setPrice(price);
        laptop.setStockQuantity(stockQuantity);

        return laptopDAO.create(laptop);
    }

    /**
     * Update an existing laptop
     * 
     * @param laptop the laptop to update
     * @return true if successful, false otherwise
     */
    public boolean updateLaptop(Laptop laptop) {
        return laptopDAO.update(laptop);
    }

    /**
     * Delete a laptop by ID
     * 
     * @param id the laptop ID to delete
     * @return true if successful, false otherwise
     */
    public boolean deleteLaptop(int id) {
        return laptopDAO.delete(id);
    }

    /**
     * Get a laptop by ID
     * 
     * @param id the laptop ID
     * @return the laptop if found, null otherwise
     */
    public Laptop getLaptopById(int id) {
        return laptopDAO.findById(id);
    }

    /**
     * Get all laptops
     * 
     * @return a list of all laptops
     */
    public List<Laptop> getAllLaptops() {
        return laptopDAO.findAll();
    }

    /**
     * Search laptops by name
     * 
     * @param keyword the keyword to search for
     * @return a list of matching laptops
     */
    public List<Laptop> searchLaptopsByName(String keyword) {
        return laptopDAO.searchByName(keyword);
    }

    /**
     * Filter laptops by brand
     * 
     * @param brand the brand to filter by
     * @return a list of matching laptops
     */
    public List<Laptop> filterLaptopsByBrand(String brand) {
        return laptopDAO.filterByBrand(brand);
    }

    /**
     * Filter laptops by RAM size
     * 
     * @param ram the RAM size to filter by
     * @return a list of matching laptops
     */
    public List<Laptop> filterLaptopsByRam(int ram) {
        return laptopDAO.filterByRam(ram);
    }

    /**
     * Filter laptops by operating system
     * 
     * @param os the operating system to filter by
     * @return a list of matching laptops
     */
    public List<Laptop> filterLaptopsByOS(String os) {
        return laptopDAO.filterByOS(os);
    }

    /**
     * Update the stock quantity of a laptop
     * 
     * @param laptopId       the laptop ID
     * @param quantityChange the change in quantity (positive for increase, negative
     *                       for decrease)
     * @return true if successful, false otherwise
     */
    public boolean updateLaptopStock(int laptopId, int quantityChange) {
        return laptopDAO.updateStock(laptopId, quantityChange);
    }
}