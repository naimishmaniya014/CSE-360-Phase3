// src/test/java/Utilities/SessionManagerTest.java
package Utilities;

import models.Role;
import models.User;
import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Arrays;

/**
 * <p> Title: SessionManagerTest Class </p>
 * 
 * <p> Description: This class provides a set of JUnit 5 tests for the {@link SessionManager} class.
 * It validates session management functionalities by performing operations such as setting the current user,
 * setting the current role, and clearing the session. The tests use assertions to verify the correctness
 * of each operation, ensuring the robustness and reliability of the {@link SessionManager} class. </p>
 * 
 * @version 1.00  2024-11-20  Initial version.
 */
class SessionManagerTest {
    
    private static SessionManager sessionManager;
    
    @BeforeAll
    static void setupAll() {
        // Initialize the SessionManager instance
        sessionManager = SessionManager.getInstance();
    }
    
    @BeforeEach
    void setup() {
        // Clear session before each test to ensure isolation
        sessionManager.clearSession();
    }
    
    @Test
    @DisplayName("Test Setting and Getting Current User")
    void testSetAndGetCurrentUser() {
        User user = new User("sessionUser", "password123");
        user.setRoles(Arrays.asList(Role.ADMIN));
        
        sessionManager.setCurrentUser(user);
        
        User retrievedUser = sessionManager.getCurrentUser();
        assertNotNull(retrievedUser, "Retrieved user should not be null.");
        assertEquals("sessionUser", retrievedUser.getUsername(), "Usernames should match.");
        assertEquals("password123", retrievedUser.getPassword(), "Passwords should match.");
        assertTrue(retrievedUser.getRoles().contains(Role.ADMIN), "User should have ADMIN role.");
    }
    
    @Test
    @DisplayName("Test Clearing Session")
    void testClearSession() {
        User user = new User("clearUser", "password123");
        user.setRoles(Arrays.asList(Role.STUDENT));
        
        sessionManager.setCurrentUser(user);
        sessionManager.setCurrentRole(Role.STUDENT);
        
        sessionManager.clearSession();
        
        assertNull(sessionManager.getCurrentUser(), "Current user should be null after clearing session.");
        assertNull(sessionManager.getCurrentRole(), "Current role should be null after clearing session.");
    }
    
    @Test
    @DisplayName("Test Setting and Getting Current Role")
    void testSetAndGetCurrentRole() {
        User user = new User("roleUser", "password123");
        user.setRoles(Arrays.asList(Role.INSTRUCTOR));
        
        sessionManager.setCurrentUser(user);
        sessionManager.setCurrentRole(Role.INSTRUCTOR);
        
        Role retrievedRole = sessionManager.getCurrentRole();
        assertNotNull(retrievedRole, "Retrieved role should not be null.");
        assertEquals(Role.INSTRUCTOR, retrievedRole, "Roles should match.");
    }
    
    @Test
    @DisplayName("Test Session Persistence Across Operations")
    void testSessionPersistence() {
        User user = new User("persistentUser", "password123");
        user.setRoles(Arrays.asList(Role.ADMIN, Role.INSTRUCTOR));
        
        sessionManager.setCurrentUser(user);
        sessionManager.setCurrentRole(Role.ADMIN);
        
        // Simulate some operations
        User retrievedUser = sessionManager.getCurrentUser();
        Role retrievedRole = sessionManager.getCurrentRole();
        
        assertNotNull(retrievedUser, "Retrieved user should not be null.");
        assertEquals("persistentUser", retrievedUser.getUsername(), "Usernames should match.");
        assertTrue(retrievedUser.getRoles().contains(Role.ADMIN), "User should have ADMIN role.");
        
        assertNotNull(retrievedRole, "Retrieved role should not be null.");
        assertEquals(Role.ADMIN, retrievedRole, "Roles should match.");
    }
    
    @Test
    @DisplayName("Test Setting Role Without Setting User")
    void testSetRoleWithoutUser() {
        sessionManager.setCurrentRole(Role.STUDENT);
        
        Role retrievedRole = sessionManager.getCurrentRole();
        assertNotNull(retrievedRole, "Retrieved role should not be null even if user is not set.");
        assertEquals(Role.STUDENT, retrievedRole, "Roles should match.");
        
        // Depending on implementation, you might want to assert user is still null
        assertNull(sessionManager.getCurrentUser(), "Current user should still be null.");
    }
}
