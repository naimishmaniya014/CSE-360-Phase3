package models;

import java.util.Arrays;

/**
 * <p> Title: UserTest Class </p>
 * 
 * <p> Description: This class provides a set of tests for the {@link User} class.
 * It validates the functionalities of the User class, including constructors, 
 * password updates, role management, and OTP expiration settings. The test outputs
 * indicate the success or failure of each test case, ensuring that the User class
 * behaves as expected.
 * </p>
 * 
 * <p> Usage: Execute the main method of this class to run all tests related to the User class.
 * Each test method provides console output detailing the outcome of the test, making it easy
 * to identify any issues or confirm successful operations. </p>
 * 
 * @author Naimish Maniya
 * 
 * <p> @version 1.00  2024-10-29  Initial version. </p>
 */
public class UserTest {

    /**
     * The main method to execute the User tests.
     *
     * @param args Command-line arguments (not used).
     */
    public static void main(String[] args) {
        UserTest tester = new UserTest();
        tester.runTests();
    }

    /**
     * Executes all test cases for the User class.
     * This method sequentially runs individual tests for the User constructor,
     * password updates, role addition/removal, and OTP expiration settings.
     */
    public void runTests() {
        System.out.println("Running User class tests...");

        testUserConstructor();
        testSetPassword();
        testAddRole();
        testRemoveRole();
        testOtpExpiration();

        System.out.println("Tests completed.");
    }

    /**
     * Tests the constructor of the User class.
     * It verifies that the username and password are correctly initialized.
     */
    public void testUserConstructor() {
        System.out.println("\nTest: User Constructor");
        User user = new User("testUser", "password123");

        boolean usernameTest = "testUser".equals(user.getUsername());
        boolean passwordTest = "password123".equals(user.getPassword());

        if (usernameTest && passwordTest) {
            System.out.println("Passed: User constructor works correctly.");
        } else {
            System.out.println("Failed: User constructor failed.");
        }
    }

    /**
     * Tests the setPassword method of the User class.
     * It verifies that the user's password is correctly updated.
     */
    public void testSetPassword() {
        System.out.println("\nTest: Set Password");
        User user = new User("testUser", "password123");
        user.setPassword("newPassword123");

        if ("newPassword123".equals(user.getPassword())) {
            System.out.println("Passed: Set password works.");
        } else {
            System.out.println("Failed: Set password failed.");
        }
    }

    /**
     * Tests the addRole method of the User class.
     * It verifies that a role is correctly added to the user's role list.
     */
    public void testAddRole() {
        System.out.println("\nTest: Add Role");
        User user = new User("testUser", "password123");
        user.addRole(Role.ADMIN);

        if (user.getRoles().contains(Role.ADMIN)) {
            System.out.println("Passed: Role added successfully.");
        } else {
            System.out.println("Failed: Role was not added.");
        }
    }

    /**
     * Tests the removeRole method of the User class.
     * It verifies that a role is correctly removed from the user's role list.
     */
    public void testRemoveRole() {
        System.out.println("\nTest: Remove Role");
        User user = new User("testUser", "password123");
        user.addRole(Role.ADMIN);
        user.removeRole(Role.ADMIN);

        if (!user.getRoles().contains(Role.ADMIN)) {
            System.out.println("Passed: Role removed successfully.");
        } else {
            System.out.println("Failed: Role was not removed.");
        }
    }

    /**
     * Tests the OTP expiration setting of the User class.
     * It verifies that the OTP expiration time is correctly set.
     */
    public void testOtpExpiration() {
        System.out.println("\nTest: OTP Expiration");
        User user = new User("testUser", "password123");
        user.setOtpExpiration(java.time.LocalDateTime.now());

        if (user.getOtpExpiration() != null) {
            System.out.println("Passed: OTP expiration set correctly.");
        } else {
            System.out.println("Failed: OTP expiration not set.");
        }
    }
}
