package Utilities;

import models.User;
import models.InvitationCode;
import models.Role;

import java.time.LocalDateTime;
import java.util.*;

/**
 * <p> Title: User Manager Utility. </p>
 * 
 * <p> Description: This class is responsible for managing users in the system. It handles
 * tasks such as user authentication, password resets, role management, and invitation codes.
 * It follows the Singleton design pattern to ensure only one instance of the manager exists. </p>
 * 
 * <p> The `UserManager` manages users, invitation codes, and provides various utility methods 
 * to handle password resets, user roles, and session-related tasks. </p>
 * 
 * @author Naimish Maniya
 * 
 * @version 1.00   2024-10-09  Initial version.
 */
public class UserManager {
    
    private static UserManager instance = null;
    private Map<String, User> users;  // Stores users with username as the key
    private Map<String, InvitationCode> invitationCodes;  // Stores invitation codes

    /**
     * Private constructor to prevent instantiation from outside the class.
     * Implements the Singleton pattern.
     */
    private UserManager() {
        users = new HashMap<>();
        invitationCodes = new HashMap<>();
    }

    /**
     * Returns the single instance of the `UserManager` class.
     * 
     * @return The singleton instance of `UserManager`.
     */
    public static UserManager getInstance() {
        if (instance == null) {
            instance = new UserManager();
        }
        return instance;
    }

    // Authentication

    /**
     * Authenticates a user by checking the username and password. 
     * If the user is required to reset their password, it checks the one-time password (OTP).
     * 
     * @param username The username of the user.
     * @param password The password or OTP of the user.
     * @return The authenticated user, or null if authentication fails.
     */
    public User authenticate(String username, String password) {
        User user = users.get(username);
        if (user != null) {
            if (!user.isResetRequired() && user.getPassword().equals(password)) {
                return user;
            } else if (user.isResetRequired()) {
                // Check if OTP is valid and not expired
                if (user.getOneTimePassword().equals(password)) {
                    LocalDateTime now = LocalDateTime.now();
                    if (user.getOtpExpiration() != null && now.isBefore(user.getOtpExpiration())) {
                        return user;
                    } else {
                        // OTP expired
                        return null;
                    }
                }
            }
        }
        return null;
    }

    // Password Reset

    /**
     * Resets the user's password by generating a one-time password (OTP) that expires in 24 hours.
     * 
     * @param username The username of the user to reset the password for.
     */
    public void resetPassword(String username) {
        User user = users.get(username);
        if (user != null) {
            String oneTimePassword = UUID.randomUUID().toString().substring(0, 8);
            user.setOneTimePassword(oneTimePassword);
            user.setResetRequired(true);
            user.setOtpExpiration(LocalDateTime.now().plusHours(24));
            System.out.println("Password reset. One-time password: " + oneTimePassword);
            System.out.println("OTP expires at: " + user.getOtpExpiration());
        }
    }

    /**
     * Invalidates the OTP and resets the password reset flags for the user.
     * 
     * @param user The user whose OTP is to be invalidated.
     */
    public void invalidateOtp(User user) {
        user.setOneTimePassword(null);
        user.setResetRequired(false);
        user.setOtpExpiration(null);
    }

    // User Management

    /**
     * Adds a new user to the system.
     * 
     * @param user The user to be added.
     */
    public void addUser(User user) {
        users.put(user.getUsername(), user);
    }

    /**
     * Checks if a username is already taken in the system.
     * 
     * @param username The username to check.
     * @return True if the username is taken, false otherwise.
     */
    public boolean isUsernameTaken(String username) {
        return users.containsKey(username);
    }

    /**
     * Removes a user from the system by their username.
     * 
     * @param username The username of the user to be removed.
     */
    public void removeUser(String username) {
        if (users.containsKey(username)) {
            users.remove(username);
        } else {
            System.out.println("User not found.");
        }
    }

    /**
     * Returns a user by their username.
     * 
     * @param username The username of the user to retrieve.
     * @return The user, or null if no user is found.
     */
    public User getUserByUsername(String username) {
        return users.get(username);
    }

    /**
     * Returns a collection of all users in the system.
     * 
     * @return A collection of users.
     */
    public Collection<User> getAllUsers() {
        return users.values();
    }

    /**
     * Checks if no users exist in the system.
     * 
     * @return True if no users exist, false otherwise.
     */
    public boolean noUsersExist() {
        return users.isEmpty();
    }

    // Role Management

    /**
     * Adds a role to a user by their username.
     * 
     * @param username The username of the user.
     * @param role The role to be added.
     */
    public void addRoleToUser(String username, Role role) {
        User user = users.get(username);
        if (user != null) {
            user.addRole(role);
        }
    }

    /**
     * Removes a role from a user by their username.
     * 
     * @param username The username of the user.
     * @param role The role to be removed.
     */
    public void removeRoleFromUser(String username, Role role) {
        User user = users.get(username);
        if (user != null) {
            user.removeRole(role);
        }
    }

    // Invitation Code Management

    /**
     * Adds a new invitation code to the system.
     * 
     * @param code The invitation code to be added.
     */
    public void addInvitationCode(InvitationCode code) {
        invitationCodes.put(code.getCode(), code);
    }

    /**
     * Retrieves an invitation code by its code string.
     * 
     * @param code The invitation code string.
     * @return The invitation code, or null if not found.
     */
    public InvitationCode getInvitationCode(String code) {
        return invitationCodes.get(code);
    }

    /**
     * Removes an invitation code from the system.
     * 
     * @param code The code of the invitation to be removed.
     */
    public void removeInvitationCode(String code) {
        invitationCodes.remove(code);
    }
    
    public boolean isResetRequired(String username) {
        User user = users.get(username); 
        if (user != null) {
            return user.isResetRequired();
        }
        return false;
    }
    
    public List<User> getUsersByRole(Role role) {
        return users.values().stream()
                .filter(user -> user.getRoles().contains(role))
                .toList();
    }

}
