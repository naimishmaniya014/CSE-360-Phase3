package Utilities;

import models.User;
import models.Role;

/**
 * <p> Title: SessionManagerTest Class </p>
 * 
 * <p> Description: This class provides a set of tests for the {@link SessionManager} class.
 * It validates session management functionalities by performing operations such as setting the current user,
 * setting the current role, and clearing the session. The test outputs indicate the success or failure
 * of each test case. </p>
 * 
 * @author Naimish Maniya
 * 
 * <p> @version 1.00  2024-10-29  Initial version. </p>
 */
public class SessionManagerTest {

    /**
     * The main method to execute the SessionManager tests.
     *
     * @param args Command-line arguments (not used).
     */
    public static void main(String[] args) {
        SessionManagerTest tester = new SessionManagerTest();
        tester.runTests();
    }

    /**
     * Executes all test cases for the SessionManager.
     */
    public void runTests() {
        System.out.println("Running SessionManager tests...");

        testSetCurrentUser();
        testClearSession();
        testSetCurrentRole();

        System.out.println("Tests completed.");
    }

    /**
     * Tests setting the current user in the session.
     */
    public void testSetCurrentUser() {
        System.out.println("\nTest: Set Current User");
        SessionManager sessionManager = SessionManager.getInstance();
        User user = new User("testUser", "password123");

        sessionManager.setCurrentUser(user);

        if (user.equals(sessionManager.getCurrentUser())) {
            System.out.println("Passed: Current user is set correctly.");
        } else {
            System.out.println("Failed: Current user is not set correctly.");
        }
    }

    /**
     * Tests clearing the current session.
     */
    public void testClearSession() {
        System.out.println("\nTest: Clear Session");
        SessionManager sessionManager = SessionManager.getInstance();
        User user = new User("testUser", "password123");

        sessionManager.setCurrentUser(user);
        sessionManager.clearSession();

        if (sessionManager.getCurrentUser() == null) {
            System.out.println("Passed: Session cleared successfully.");
        } else {
            System.out.println("Failed: Session not cleared.");
        }
    }

    /**
     * Tests setting the current role in the session.
     */
    public void testSetCurrentRole() {
        System.out.println("\nTest: Set Current Role");
        SessionManager sessionManager = SessionManager.getInstance();
        User user = new User("testUser", "password123");

        sessionManager.setCurrentUser(user);
        sessionManager.setCurrentRole(Role.ADMIN);

        if (Role.ADMIN.equals(sessionManager.getCurrentRole())) {
            System.out.println("Passed: Current role is set correctly.");
        } else {
            System.out.println("Failed: Current role is not set correctly.");
        }
    }
}
