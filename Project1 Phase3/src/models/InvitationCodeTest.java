package models;

import java.util.Arrays;

/**
 * <p> Title: InvitationCodeTest Class </p>
 * 
 * <p> Description: This class provides a set of tests for the {@link InvitationCode} class.
 * It validates the functionalities of the InvitationCode class, including its constructors,
 * getter and setter methods, and status updates. The test outputs indicate the success or
 * failure of each test case, ensuring that the InvitationCode class behaves as expected.
 * </p>
 * 
 * <p> Usage: Execute the main method of this class to run all tests related to the InvitationCode class.
 * Each test method provides console output detailing the outcome of the test, making it easy
 * to identify any issues or confirm successful operations. </p>
 * 
 * @author Naimish Maniya
 * 
 * <p> @version 1.00  2024-10-29  Initial version. </p>
 */
public class InvitationCodeTest {

    /**
     * The main method to execute the InvitationCode tests.
     *
     * @param args Command-line arguments (not used).
     */
    public static void main(String[] args) {
        InvitationCodeTest tester = new InvitationCodeTest();
        tester.runTests();
    }

    /**
     * Executes all test cases for the InvitationCode class.
     * This method sequentially runs individual tests for the InvitationCode constructor
     * and the setUsed method.
     */
    public void runTests() {
        System.out.println("Running InvitationCode class tests...");

        testInvitationCode();
        testSetUsed();

        System.out.println("Tests completed.");
    }

    /**
     * Tests the constructor of the InvitationCode class.
     * It verifies that the code and associated roles are correctly initialized.
     */
    public void testInvitationCode() {
        System.out.println("\nTest: InvitationCode Constructor");
        InvitationCode invitation = new InvitationCode("ABC123", Arrays.asList(Role.ADMIN, Role.STUDENT));

        boolean codeTest = "ABC123".equals(invitation.getCode());
        boolean rolesTest = invitation.getRoles().contains(Role.ADMIN) && invitation.getRoles().contains(Role.STUDENT);

        if (codeTest && rolesTest) {
            System.out.println("Passed: InvitationCode constructor works.");
        } else {
            System.out.println("Failed: InvitationCode constructor failed.");
        }
    }

    /**
     * Tests the setUsed method of the InvitationCode class.
     * It verifies that the used status of the invitation code is correctly updated.
     */
    public void testSetUsed() {
        System.out.println("\nTest: Set InvitationCode Used");
        InvitationCode invitation = new InvitationCode("XYZ456", Arrays.asList(Role.INSTRUCTOR));
        invitation.setUsed(true);

        if (invitation.isUsed()) {
            System.out.println("Passed: InvitationCode is marked as used.");
        } else {
            System.out.println("Failed: InvitationCode is not marked as used.");
        }
    }
}
