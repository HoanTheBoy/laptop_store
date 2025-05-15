package dao;

import model.Account;
import java.util.List;

/**
 * Data Access Object interface for Account entities
 */
public interface AccountDAO {
    /**
     * Find an account by username
     * 
     * @param username the username to search for
     * @return the Account if found, null otherwise
     */
    Account findByUsername(String username);

    /**
     * Find an account by ID
     * 
     * @param id the account ID to search for
     * @return the Account if found, null otherwise
     */
    Account findById(int id);

    /**
     * Create a new account
     * 
     * @param account the account to create
     * @return the created account with ID assigned
     */
    Account create(Account account);

    /**
     * Update an existing account
     * 
     * @param account the account to update
     * @return true if successful, false otherwise
     */
    boolean update(Account account);

    /**
     * Delete an account by ID
     * 
     * @param id the account ID to delete
     * @return true if successful, false otherwise
     */
    boolean delete(int id);

    /**
     * Get all accounts
     * 
     * @return a list of all accounts
     */
    List<Account> findAll();
}