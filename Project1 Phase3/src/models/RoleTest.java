package models;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * <p> Title: RoleTest Class </p>
 * 
 * <p> Description: This class provides a set of tests for the {@link Role} enum.
 * It validates the correctness of role codes assigned to each enum constant. The test
 * outputs indicate whether each role's code matches the expected value, ensuring that
 * role definitions are accurate.
 * </p>
 * 
 * @version 1.00  2024-10-29  Initial version.
 */
public class RoleTest {

    /**
     * Tests the ADMIN role's code.
     * It verifies that the ADMIN role has the correct code assigned.
     */
    @Test
    @DisplayName("Test ADMIN Role Code")
    public void testRoleAdmin() {
        Role role = Role.ADMIN;

        assertEquals("A", role.getCode(), "ADMIN role code should be 'A'.");
    }

    /**
     * Tests the STUDENT role's code.
     * It verifies that the STUDENT role has the correct code assigned.
     */
    @Test
    @DisplayName("Test STUDENT Role Code")
    public void testRoleStudent() {
        Role role = Role.STUDENT;

        assertEquals("S", role.getCode(), "STUDENT role code should be 'S'.");
    }

    /**
     * Tests the INSTRUCTOR role's code.
     * It verifies that the INSTRUCTOR role has the correct code assigned.
     */
    @Test
    @DisplayName("Test INSTRUCTOR Role Code")
    public void testRoleInstructor() {
        Role role = Role.INSTRUCTOR;

        assertEquals("I", role.getCode(), "INSTRUCTOR role code should be 'I'.");
    }

    /**
     * Tests the toString method of the Role enum.
     * It verifies that the toString method returns the correct role name.
     */
    @Test
    @DisplayName("Test toString Method")
    public void testToString() {
        Role role = Role.ADMIN;

        assertEquals("ADMIN", role.toString(), "toString() should return the role name.");
    }

    /**
     * Tests the equality of Role enum constants.
     * It verifies that the same enum constants are equal and different ones are not.
     */
    @Test
    @DisplayName("Test Role Enum Equality")
    public void testRoleEquality() {
        Role role1 = Role.ADMIN;
        Role role2 = Role.ADMIN;
        Role role3 = Role.STUDENT;

        assertEquals(role1, role2, "ADMIN roles should be equal.");
        assertNotEquals(role1, role3, "ADMIN and STUDENT roles should not be equal.");
    }
}
