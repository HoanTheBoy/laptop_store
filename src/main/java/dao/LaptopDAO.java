package dao;

import model.Laptop;
import java.util.List;

/**
 * Data Access Object interface for Laptop entities
 */
public interface LaptopDAO {
    /**
     * Find a laptop by ID
     * 
     * @param id the laptop ID to search for
     * @return the Laptop if found, null otherwise
     */
    Laptop findById(int id);

    /**
     * Create a new laptop
     * 
     * @param laptop the laptop to create
     * @return the created laptop with ID assigned
     */
    Laptop create(Laptop laptop);

    /**
     * Update an existing laptop
     * 
     * @param laptop the laptop to update
     * @return true if successful, false otherwise
     */
    boolean update(Laptop laptop);

    /**
     * Delete a laptop by ID
     * 
     * @param id the laptop ID to delete
     * @return true if successful, false otherwise
     */
    boolean delete(int id);

    /**
     * Get all laptops
     * 
     * @return a list of all laptops
     */
    List<Laptop> findAll();

    /**
     * Search laptops by name
     * 
     * @param keyword the keyword to search for in the name
     * @return a list of matching laptops
     */
    List<Laptop> searchByName(String keyword);

    /**
     * Filter laptops by brand
     * 
     * @param brand the brand to filter by
     * @return a list of matching laptops
     */
    List<Laptop> filterByBrand(String brand);

    /**
     * Filter laptops by RAM size
     * 
     * @param ram the RAM size to filter by (in GB)
     * @return a list of matching laptops
     */
    List<Laptop> filterByRam(int ram);

    /**
     * Filter laptops by operating system
     * 
     * @param os the operating system to filter by
     * @return a list of matching laptops
     */
    List<Laptop> filterByOS(String os);

    /**
     * Update the stock quantity of a laptop
     * 
     * @param laptopId       the laptop ID
     * @param quantityChange the change in quantity (positive for increase, negative
     *                       for decrease)
     * @return true if successful, false otherwise
     */
    boolean updateStock(int laptopId, int quantityChange);
}