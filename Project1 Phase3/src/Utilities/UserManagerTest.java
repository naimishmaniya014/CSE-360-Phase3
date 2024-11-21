package Utilities;

import models.User;

/**
 * <p> Title: UserManagerTest Class </p>
 * 
 * <p> Description: This class provides a set of tests for the {@link UserManager} class.
 * It validates user management functionalities by performing operations such as adding a user,
 * checking if a username is taken, authenticating a user, and resetting a user's password.
 * The test outputs indicate the success or failure of each test case, aiding in ensuring the
 * robustness and reliability of the {@link UserManager} class. </p>
 * 
 * <p> Usage: Execute the main method of this class to run all tests related to user management.
 * Each test method provides console output detailing the outcome of the test, making it easy
 * to identify any issues or confirm successful operations. </p>
 * 
 * @author Naimish Maniya
 * 
 * <p> @version 1.00  2024-10-29  Initial version. </p>
 */
public class UserManagerTest {

    /**
     * The main method to execute the UserManager tests.
     *
     * @param args Command-line arguments (not used).
     */
    public static void main(String[] args) {
        UserManagerTest tester = new UserManagerTest();
        tester.runTests();
    }

    /**
     * Executes all test cases for the UserManager.
     * This method sequentially runs individual tests for adding a user, checking username availability,
     * authenticating a user, and resetting a user's password.
     */
    public void runTests() {
        System.out.println("Running UserManager tests...");
        
        testAddUser();
        testIsUsernameTaken();
        testAuthenticate();
        testResetPassword();
        
        System.out.println("Tests completed.");
    }

    /**
     * Tests the addition of a new user to the system.
     * It creates a new {@link User} object and attempts to add it using the {@link UserManager}.
     * The test verifies whether the user was added successfully by attempting to retrieve
     * the user by username.
     */
    public void testAddUser() {
        System.out.println("\nTest: Add User");
        UserManager userManager = UserManager.getInstance();
        User user = new User("testUser", "password123");

        userManager.addUser(user);
        if (userManager.getUserByUsername("testUser") != null) {
            System.out.println("Passed: User was added successfully.");
        } else {
            System.out.println("Failed: User was not added.");
        }
    }

    /**
     * Tests whether the system correctly identifies if a username is already taken.
     * It adds a new user and then checks if the username is marked as taken.
     */
    public void testIsUsernameTaken() {
        System.out.println("\nTest: Is Username Taken");
        UserManager userManager = UserManager.getInstance();
        User user = new User("testUser2", "password123");
        userManager.addUser(user);

        if (userManager.isUsernameTaken("testUser2")) {
            System.out.println("Passed: Username is correctly marked as taken.");
        } else {
            System.out.println("Failed: Username is not marked as taken.");
        }
    }

    /**
     * Tests the authentication functionality of the system.
     * It adds a new user and attempts to authenticate with the correct credentials.
     * The test verifies whether the authentication is successful.
     */
    public void testAuthenticate() {
        System.out.println("\nTest: Authenticate User");
        UserManager userManager = UserManager.getInstance();
        User user = new User("testUser3", "password123");
        userManager.addUser(user);

        if (userManager.authenticate("testUser3", "password123") != null) {
            System.out.println("Passed: User was authenticated successfully.");
        } else {
            System.out.println("Failed: Authentication failed.");
        }
    }

    /**
     * Tests the password reset functionality of the system.
     * It adds a new user, resets the user's password, and verifies whether the reset flag is set.
     */
    public void testResetPassword() {
        System.out.println("\nTest: Reset Password");
        UserManager userManager = UserManager.getInstance();
        User user = new User("testUser4", "password123");
        userManager.addUser(user);

        userManager.resetPassword("testUser4");
        if (user.isResetRequired()) {
            System.out.println("Passed: Password reset successfully.");
        } else {
            System.out.println("Failed: Password reset did not occur.");
        }
    }
}
