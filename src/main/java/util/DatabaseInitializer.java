package util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Utility class for initializing the database with sample data
 */
public class DatabaseInitializer {

    /**
     * Initialize the database with tables and sample data
     */
    public static void initializeDatabase() {
        System.out.println("Initializing database...");
        try (Connection conn = DBConnection.getConnection()) {
            // Read SQL script from resources
            String sqlScript = readSqlScript();

            // Drop existing tables and recreate them
            dropAllTables(conn);

            // Split script into individual statements
            String[] statements = sqlScript.split(";");

            // Execute each statement
            try (Statement stmt = conn.createStatement()) {
                for (String sql : statements) {
                    sql = sql.trim();
                    if (!sql.isEmpty()) {
                        try {
                            stmt.execute(sql);
                        } catch (SQLException e) {
                            System.err.println("Error executing SQL: " + sql);
                            System.err.println("Error message: " + e.getMessage());
                            // Continue with next statement instead of breaking
                        }
                    }
                }
            }
            System.out.println("Database initialization completed successfully!");
        } catch (SQLException | IOException e) {
            System.err.println("Database initialization failed: " + e.getMessage());
        }
    }

    /**
     * Drops all existing tables to ensure a clean database
     */
    private static void dropAllTables(Connection conn) throws SQLException {
        try (Statement stmt = conn.createStatement()) {
            // Disable foreign key checks to avoid constraint errors during drop
            stmt.execute("SET FOREIGN_KEY_CHECKS = 0");

            // Drop all tables in reverse order of dependencies
            String[] dropStatements = {
                    "DROP TABLE IF EXISTS warranty_requests",
                    "DROP TABLE IF EXISTS import_receipts",
                    "DROP TABLE IF EXISTS order_items",
                    "DROP TABLE IF EXISTS orders",
                    "DROP TABLE IF EXISTS laptops",
                    "DROP TABLE IF EXISTS accounts"
            };

            for (String sql : dropStatements) {
                try {
                    stmt.execute(sql);
                    System.out.println("Executed: " + sql);
                } catch (SQLException e) {
                    System.err.println("Error dropping table: " + e.getMessage());
                }
            }

            // Re-enable foreign key checks
            stmt.execute("SET FOREIGN_KEY_CHECKS = 1");
        }
    }

    /**
     * Read SQL script from resources
     * 
     * @return the content of the SQL script
     * @throws IOException if an I/O error occurs
     */
    private static String readSqlScript() throws IOException {
        StringBuilder sb = new StringBuilder();
        try (InputStream is = DatabaseInitializer.class.getClassLoader().getResourceAsStream("init_database.sql");
                BufferedReader reader = new BufferedReader(new InputStreamReader(is))) {

            String line;
            while ((line = reader.readLine()) != null) {
                // Skip comments
                if (!line.trim().startsWith("--")) {
                    sb.append(line).append("\n");
                }
            }
        }
        return sb.toString();
    }
}