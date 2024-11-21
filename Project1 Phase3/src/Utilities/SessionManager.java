package Utilities;

import models.User;
import models.Role;

/**
 * <p> Title: SessionManager Class </p>
 * 
 * <p> Description: This class manages the current user session within the application.
 * It follows the Singleton design pattern to ensure that only one session exists at a time.
 * The class provides methods to set and retrieve the current user and role, as well as to clear the session.
 * </p>
 * 
 * @author Naimish Maniya
 * 
 * <p> @version 1.00  2024-10-29  Initial version. </p>
 */
public class SessionManager {
    private static SessionManager instance;
    private User currentUser;
    private Role currentRole;

    /**
     * Private constructor to enforce Singleton pattern.
     */
    private SessionManager() {}

    /**
     * Retrieves the singleton instance of SessionManager.
     *
     * @return The SessionManager instance.
     */
    public static synchronized SessionManager getInstance() {
        if (instance == null) {
            instance = new SessionManager();
        }
        return instance;
    }

    /**
     * Retrieves the current user of the session.
     *
     * @return The current {@link User}, or null if no user is set.
     */
    public User getCurrentUser() {
        return currentUser;
    }

    /**
     * Sets the current user of the session.
     *
     * @param currentUser The {@link User} to set as current.
     */
    public void setCurrentUser(User currentUser) {
        this.currentUser = currentUser;
    }

    /**
     * Retrieves the current role of the session.
     *
     * @return The current {@link Role}, or null if no role is set.
     */
    public Role getCurrentRole() {
        return currentRole;
    }

    /**
     * Sets the current role of the session.
     *
     * @param currentRole The {@link Role} to set as current.
     */
    public void setCurrentRole(Role currentRole) {
        this.currentRole = currentRole;
    }

    /**
     * Clears the current session by resetting the user and role.
     */
    public void clearSession() {
        this.currentUser = null;
        this.currentRole = null;
    }
    
    
}
