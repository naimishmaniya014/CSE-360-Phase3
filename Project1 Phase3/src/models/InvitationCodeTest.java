package models;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

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
 * @version 1.00  2024-10-29  Initial version.
 */
public class InvitationCodeTest {

    /**
     * Tests the constructor of the InvitationCode class.
     * It verifies that the code and associated roles are correctly initialized.
     */
    @Test
    @DisplayName("Test InvitationCode Constructor")
    public void testInvitationCodeConstructor() {
        InvitationCode invitation = new InvitationCode("ABC123", Arrays.asList(Role.ADMIN, Role.STUDENT));

        assertEquals("ABC123", invitation.getCode(), "Constructor should initialize code correctly.");
        assertNotNull(invitation.getRoles(), "Constructor should initialize roles list.");
        assertEquals(2, invitation.getRoles().size(), "Roles list should contain exactly 2 roles.");
        assertTrue(invitation.getRoles().contains(Role.ADMIN), "Roles list should contain Role.ADMIN.");
        assertTrue(invitation.getRoles().contains(Role.STUDENT), "Roles list should contain Role.STUDENT.");
        assertFalse(invitation.isUsed(), "Invitation should be unused by default.");
    }

    /**
     * Tests the setUsed method of the InvitationCode class.
     * It verifies that the used status of the invitation code is correctly updated.
     */
    @Test
    @DisplayName("Test setUsed Method")
    public void testSetUsed() {
        InvitationCode invitation = new InvitationCode("XYZ456", Arrays.asList(Role.INSTRUCTOR));
        invitation.setUsed(true);

        assertTrue(invitation.isUsed(), "Invitation should be marked as used after setUsed(true).");

        invitation.setUsed(false);
        assertFalse(invitation.isUsed(), "Invitation should be marked as unused after setUsed(false).");
    }

    /**
     * Tests the getCode method of the InvitationCode class.
     */
    @Test
    @DisplayName("Test getCode Method")
    public void testGetCode() {
        InvitationCode invitation = new InvitationCode("LMN789", Arrays.asList(Role.STUDENT));
        assertEquals("LMN789", invitation.getCode(), "getCode should return the correct code.");
    }

    /**
     * Tests the getRoles method of the InvitationCode class.
     */
    @Test
    @DisplayName("Test getRoles Method")
    public void testGetRoles() {
        InvitationCode invitation = new InvitationCode("OPQ012", Arrays.asList(Role.ADMIN, Role.INSTRUCTOR));
        assertNotNull(invitation.getRoles(), "getRoles should not return null.");
        assertEquals(2, invitation.getRoles().size(), "getRoles should return correct number of roles.");
        assertTrue(invitation.getRoles().contains(Role.ADMIN), "getRoles should contain Role.ADMIN.");
        assertTrue(invitation.getRoles().contains(Role.INSTRUCTOR), "getRoles should contain Role.INSTRUCTOR.");
    }

    /**
     * Tests the toString method of the InvitationCode class.
     */
    @Test
    @DisplayName("Test toString Method")
    public void testToString() {
        InvitationCode invitation = new InvitationCode("RST345", Arrays.asList(Role.STUDENT));
        String expected = "InvitationCode{code='RST345', roles=[STUDENT], used=false}";
        String actual = invitation.toString();
        
        assertEquals(expected, actual, "toString() should return the correct string representation.");
    }

    /**
     * Tests the equals and hashCode methods of the InvitationCode class.
     */
    @Test
    @DisplayName("Test equals and hashCode Methods")
    public void testEqualsAndHashCode() {
        InvitationCode invitation1 = new InvitationCode("UVW678", Arrays.asList(Role.ADMIN));
        InvitationCode invitation2 = new InvitationCode("UVW678", Arrays.asList(Role.ADMIN));
        InvitationCode invitation3 = new InvitationCode("XYZ456", Arrays.asList(Role.STUDENT));

        // Test equality
        assertEquals(invitation1, invitation2, "Invitations with same code and roles should be equal.");
        assertNotEquals(invitation1, invitation3, "Invitations with different codes or roles should not be equal.");

        // Test hash codes
        assertEquals(invitation1.hashCode(), invitation2.hashCode(), "Hash codes should be equal for equal invitations.");
        assertNotEquals(invitation1.hashCode(), invitation3.hashCode(), "Hash codes should not be equal for different invitations.");
    }
}
