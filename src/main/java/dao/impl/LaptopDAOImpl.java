package dao.impl;

import dao.LaptopDAO;
import model.Laptop;
import util.DBConnection;

import java.math.BigDecimal;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Implementation of LaptopDAO interface using JDBC
 */
public class LaptopDAOImpl implements LaptopDAO {

    @Override
    public Laptop findById(int id) {
        String sql = "SELECT * FROM laptops WHERE id = ?";
        try (Connection conn = DBConnection.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return mapResultSetToLaptop(rs);
            }
        } catch (SQLException e) {
            System.err.println("Error finding laptop by ID: " + e.getMessage());
        }
        return null;
    }

    @Override
    public Laptop create(Laptop laptop) {
        String sql = "INSERT INTO laptops (name, brand, cpu, ram, os, color, price, stock_quantity) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = DBConnection.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setString(1, laptop.getName());
            stmt.setString(2, laptop.getBrand());
            stmt.setString(3, laptop.getCpu());
            stmt.setInt(4, laptop.getRam());
            stmt.setString(5, laptop.getOs());
            stmt.setString(6, laptop.getColor());
            stmt.setBigDecimal(7, laptop.getPrice());
            stmt.setInt(8, laptop.getStockQuantity());

            int affectedRows = stmt.executeUpdate();
            if (affectedRows == 0) {
                throw new SQLException("Creating laptop failed, no rows affected.");
            }

            try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    laptop.setId(generatedKeys.getInt(1));
                    return laptop;
                } else {
                    throw new SQLException("Creating laptop failed, no ID obtained.");
                }
            }
        } catch (SQLException e) {
            System.err.println("Error creating laptop: " + e.getMessage());
        }
        return null;
    }

    @Override
    public boolean update(Laptop laptop) {
        String sql = "UPDATE laptops SET name = ?, brand = ?, cpu = ?, ram = ?, " +
                "os = ?, color = ?, price = ?, stock_quantity = ? WHERE id = ?";
        try (Connection conn = DBConnection.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, laptop.getName());
            stmt.setString(2, laptop.getBrand());
            stmt.setString(3, laptop.getCpu());
            stmt.setInt(4, laptop.getRam());
            stmt.setString(5, laptop.getOs());
            stmt.setString(6, laptop.getColor());
            stmt.setBigDecimal(7, laptop.getPrice());
            stmt.setInt(8, laptop.getStockQuantity());
            stmt.setInt(9, laptop.getId());

            int affectedRows = stmt.executeUpdate();
            return affectedRows > 0;
        } catch (SQLException e) {
            System.err.println("Error updating laptop: " + e.getMessage());
            return false;
        }
    }

    @Override
    public boolean delete(int id) {
        String sql = "DELETE FROM laptops WHERE id = ?";
        try (Connection conn = DBConnection.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            int affectedRows = stmt.executeUpdate();
            return affectedRows > 0;
        } catch (SQLException e) {
            System.err.println("Error deleting laptop: " + e.getMessage());
            return false;
        }
    }

    @Override
    public List<Laptop> findAll() {
        List<Laptop> laptops = new ArrayList<>();
        String sql = "SELECT * FROM laptops";
        try (Connection conn = DBConnection.getConnection();
                Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                laptops.add(mapResultSetToLaptop(rs));
            }
        } catch (SQLException e) {
            System.err.println("Error finding all laptops: " + e.getMessage());
        }
        return laptops;
    }

    @Override
    public List<Laptop> searchByName(String keyword) {
        List<Laptop> laptops = new ArrayList<>();
        String sql = "SELECT * FROM laptops WHERE name LIKE ?";
        try (Connection conn = DBConnection.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, "%" + keyword + "%");
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                laptops.add(mapResultSetToLaptop(rs));
            }
        } catch (SQLException e) {
            System.err.println("Error searching laptops by name: " + e.getMessage());
        }
        return laptops;
    }

    @Override
    public List<Laptop> filterByBrand(String brand) {
        List<Laptop> laptops = new ArrayList<>();
        String sql = "SELECT * FROM laptops WHERE brand = ?";
        try (Connection conn = DBConnection.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, brand);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                laptops.add(mapResultSetToLaptop(rs));
            }
        } catch (SQLException e) {
            System.err.println("Error filtering laptops by brand: " + e.getMessage());
        }
        return laptops;
    }

    @Override
    public List<Laptop> filterByRam(int ram) {
        List<Laptop> laptops = new ArrayList<>();
        String sql = "SELECT * FROM laptops WHERE ram = ?";
        try (Connection conn = DBConnection.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, ram);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                laptops.add(mapResultSetToLaptop(rs));
            }
        } catch (SQLException e) {
            System.err.println("Error filtering laptops by RAM: " + e.getMessage());
        }
        return laptops;
    }

    @Override
    public List<Laptop> filterByOS(String os) {
        List<Laptop> laptops = new ArrayList<>();
        String sql = "SELECT * FROM laptops WHERE os = ?";
        try (Connection conn = DBConnection.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, os);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                laptops.add(mapResultSetToLaptop(rs));
            }
        } catch (SQLException e) {
            System.err.println("Error filtering laptops by OS: " + e.getMessage());
        }
        return laptops;
    }

    @Override
    public boolean updateStock(int laptopId, int quantityChange) {
        String sql = "UPDATE laptops SET stock_quantity = stock_quantity + ? WHERE id = ?";
        try (Connection conn = DBConnection.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, quantityChange);
            stmt.setInt(2, laptopId);

            int affectedRows = stmt.executeUpdate();
            return affectedRows > 0;
        } catch (SQLException e) {
            System.err.println("Error updating laptop stock: " + e.getMessage());
            return false;
        }
    }

    private Laptop mapResultSetToLaptop(ResultSet rs) throws SQLException {
        Laptop laptop = new Laptop();
        laptop.setId(rs.getInt("id"));
        laptop.setName(rs.getString("name"));
        laptop.setBrand(rs.getString("brand"));
        laptop.setCpu(rs.getString("cpu"));
        laptop.setRam(rs.getInt("ram"));
        laptop.setOs(rs.getString("os"));
        laptop.setColor(rs.getString("color"));
        laptop.setPrice(rs.getBigDecimal("price"));
        laptop.setStockQuantity(rs.getInt("stock_quantity"));
        return laptop;
    }
}