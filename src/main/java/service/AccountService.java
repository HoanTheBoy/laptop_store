package service;

import dao.AccountDAO;
import dao.impl.AccountDAOImpl;
import model.Account;

import java.util.List;

/**
 * Service class for handling account operations
 */
public class AccountService {
    private final AccountDAO accountDAO;
    private Account currentUser;

    public AccountService() {
        this.accountDAO = new AccountDAOImpl();
    }

    /**
     * Authenticate a user with username and password
     * 
     * @param username the username
     * @param password the plain-text password
     * @return true if authentication is successful, false otherwise
     */
    public boolean login(String username, String password) {
        Account account = accountDAO.findByUsername(username);
        if (account != null && account.getPassword().equals(password)) {
            currentUser = account;
            return true;
        }
        return false;
    }

    /**
     * Logout the current user
     */
    public void logout() {
        currentUser = null;
    }

    /**
     * Register a new user account
     * 
     * @param username the username
     * @param password the plain-text password
     * @param role     the role (ADMIN or USER)
     * @param fullName the full name
     * @param email    the email address
     * @param phone    the phone number
     * @param address  the physical address
     * @return the created account if successful, null otherwise
     */
    public Account register(String username, String password, String role,
            String fullName, String email, String phone, String address) {
        // Check if username already exists
        if (accountDAO.findByUsername(username) != null) {
            return null; // Username already taken
        }

        Account account = new Account();
        account.setUsername(username);
        account.setPassword(password);
        account.setRole(role);
        account.setFullName(fullName);
        account.setEmail(email);
        account.setPhone(phone);
        account.setAddress(address);

        return accountDAO.create(account);
    }

    /**
     * Update an existing account
     * 
     * @param account the account to update
     * @return true if successful, false otherwise
     */
    public boolean updateAccount(Account account) {
        return accountDAO.update(account);
    }

    /**
     * Change a user's password
     * 
     * @param userId      the user ID
     * @param oldPassword the old password for verification
     * @param newPassword the new password
     * @return true if successful, false otherwise
     */
    public boolean changePassword(int userId, String oldPassword, String newPassword) {
        Account account = accountDAO.findById(userId);
        if (account != null && account.getPassword().equals(oldPassword)) {
            account.setPassword(newPassword);
            return accountDAO.update(account);
        }
        return false;
    }

    /**
     * Get a list of all accounts (admin function)
     * 
     * @return list of all accounts
     */
    public List<Account> getAllAccounts() {
        return accountDAO.findAll();
    }

    /**
     * Get account by ID
     * 
     * @param id the account ID
     * @return the account if found, null otherwise
     */
    public Account getAccountById(int id) {
        return accountDAO.findById(id);
    }

    /**
     * Get the currently logged-in user
     * 
     * @return the current user account, or null if not logged in
     */
    public Account getCurrentUser() {
        return currentUser;
    }

    /**
     * Check if the current user is an admin
     * 
     * @return true if the current user is an admin, false otherwise
     */
    public boolean isAdmin() {
        return currentUser != null && "ADMIN".equals(currentUser.getRole());
    }

    /**
     * Create a new user account (admin function)
     * 
     * @param username the username
     * @param password the password
     * @param role     the role (ADMIN or USER)
     * @param fullName the full name
     * @param email    the email address
     * @param phone    the phone number
     * @param address  the physical address
     * @return the created account if successful, null otherwise
     */
    public Account createUser(String username, String password, String role,
            String fullName, String email, String phone, String address) {
        // Check if current user is admin
        if (!isAdmin()) {
            return null;
        }

        return register(username, password, role, fullName, email, phone, address);
    }

    /**
     * Delete a user account (admin function)
     * 
     * @param userId the ID of the user to delete
     * @return true if successful, false otherwise
     */
    public boolean deleteUser(int userId) {
        // Check if current user is admin
        if (!isAdmin()) {
            return false;
        }

        // Don't allow admins to delete themselves
        if (currentUser.getId() == userId) {
            return false;
        }

        return accountDAO.delete(userId);
    }

    /**
     * Delete a user account (alias for deleteUser)
     * 
     * @param accountId the ID of the account to delete
     * @return true if successful, false otherwise
     */
    public boolean deleteAccount(int accountId) {
        return deleteUser(accountId);
    }

    /**
     * Update a user's role (admin function)
     * 
     * @param userId  the ID of the user
     * @param newRole the new role (ADMIN or USER)
     * @return true if successful, false otherwise
     */
    public boolean updateUserRole(int userId, String newRole) {
        // Check if current user is admin
        if (!isAdmin()) {
            return false;
        }

        // Only allow "ADMIN" or "USER" roles
        if (!"ADMIN".equals(newRole) && !"USER".equals(newRole)) {
            return false;
        }

        Account account = accountDAO.findById(userId);
        if (account == null) {
            return false;
        }

        account.setRole(newRole);
        return accountDAO.update(account);
    }

    /**
     * Reset a user's password (admin function)
     * 
     * @param userId      the ID of the user
     * @param newPassword the new password
     * @return true if successful, false otherwise
     */
    public boolean resetUserPassword(int userId, String newPassword) {
        // Check if current user is admin
        if (!isAdmin()) {
            return false;
        }

        Account account = accountDAO.findById(userId);
        if (account == null) {
            return false;
        }

        account.setPassword(newPassword);
        return accountDAO.update(account);
    }

    /**
     * Reset a user's password (alias for resetUserPassword)
     * 
     * @param userId      the ID of the user
     * @param newPassword the new password
     * @return true if successful, false otherwise
     */
    public boolean resetPassword(int userId, String newPassword) {
        return resetUserPassword(userId, newPassword);
    }

    /**
     * Search users by username (admin function)
     * 
     * @param keyword the search keyword
     * @return list of matching accounts
     */
    public List<Account> searchUsersByUsername(String keyword) {
        // This method would require a new DAO method
        // For now, we'll filter in memory from all accounts
        if (!isAdmin()) {
            return null;
        }

        List<Account> allAccounts = accountDAO.findAll();
        allAccounts.removeIf(account -> !account.getUsername().toLowerCase().contains(keyword.toLowerCase()));
        return allAccounts;
    }
}