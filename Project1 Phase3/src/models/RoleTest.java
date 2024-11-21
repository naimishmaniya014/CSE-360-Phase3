package models;

/**
 * <p> Title: RoleTest Class </p>
 * 
 * <p> Description: This class provides a set of tests for the {@link Role} enum.
 * It validates the correctness of role codes assigned to each enum constant. The test
 * outputs indicate whether each role's code matches the expected value, ensuring that
 * role definitions are accurate.
 * </p>
 * 
 * <p> Usage: Execute the main method of this class to run all tests related to the Role enum.
 * Each test method provides console output detailing the outcome of the test, making it easy
 * to identify any discrepancies or confirm correct configurations. </p>
 * 
 * @author Naimish Maniya
 * 
 * <p> @version 1.00  2024-10-29  Initial version. </p>
 */
public class RoleTest {

    /**
     * The main method to execute the Role tests.
     *
     * @param args Command-line arguments (not used).
     */
    public static void main(String[] args) {
        RoleTest tester = new RoleTest();
        tester.runTests();
    }

    /**
     * Executes all test cases for the Role enum.
     * This method sequentially runs individual tests for each role defined in the Role enum.
     */
    public void runTests() {
        System.out.println("Running Role enum tests...");

        testRoleAdmin();
        testRoleStudent();
        testRoleInstructor();

        System.out.println("Tests completed.");
    }

    /**
     * Tests the ADMIN role's code.
     * It verifies that the ADMIN role has the correct code assigned.
     */
    public void testRoleAdmin() {
        System.out.println("\nTest: Role ADMIN");
        Role role = Role.ADMIN;

        if ("A".equals(role.getCode())) {
            System.out.println("Passed: ADMIN role code is correct.");
        } else {
            System.out.println("Failed: ADMIN role code is incorrect.");
        }
    }

    /**
     * Tests the STUDENT role's code.
     * It verifies that the STUDENT role has the correct code assigned.
     */
    public void testRoleStudent() {
        System.out.println("\nTest: Role STUDENT");
        Role role = Role.STUDENT;

        if ("S".equals(role.getCode())) {
            System.out.println("Passed: STUDENT role code is correct.");
        } else {
            System.out.println("Failed: STUDENT role code is incorrect.");
        }
    }

    /**
     * Tests the INSTRUCTOR role's code.
     * It verifies that the INSTRUCTOR role has the correct code assigned.
     */
    public void testRoleInstructor() {
        System.out.println("\nTest: Role INSTRUCTOR");
        Role role = Role.INSTRUCTOR;

        if ("I".equals(role.getCode())) {
            System.out.println("Passed: INSTRUCTOR role code is correct.");
        } else {
            System.out.println("Failed: INSTRUCTOR role code is incorrect.");
        }
    }
}
