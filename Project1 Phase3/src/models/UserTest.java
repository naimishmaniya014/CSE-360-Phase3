package models;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDateTime;

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
 * @version 1.00  2024-10-29  Initial version.
 */
public class UserTest {

    /**
     * Tests the constructor of the User class.
     * It verifies that the username and password are correctly initialized.
     */
    @Test
    @DisplayName("Test User Constructor")
    public void testUserConstructor() {
        User user = new User("testUser", "password123");

        assertEquals("testUser", user.getUsername(), "Constructor should initialize username correctly.");
        assertEquals("password123", user.getPassword(), "Constructor should initialize password correctly.");
        assertNotNull(user.getRoles(), "Roles list should be initialized.");
        assertTrue(user.getRoles().isEmpty(), "Roles list should be empty upon initialization.");
        assertNull(user.getOtpExpiration(), "OTP expiration should be null upon initialization.");
    }

    /**
     * Tests the setPassword method of the User class.
     * It verifies that the user's password is correctly updated.
     */
    @Test
    @DisplayName("Test setPassword Method")
    public void testSetPassword() {
        User user = new User("testUser", "password123");
        user.setPassword("newPassword123");

        assertEquals("newPassword123", user.getPassword(), "setPassword should update the password correctly.");
    }

    /**
     * Tests the addRole method of the User class.
     * It verifies that a role is correctly added to the user's role list.
     */
    @Test
    @DisplayName("Test addRole Method")
    public void testAddRole() {
        User user = new User("testUser", "password123");
        user.addRole(Role.ADMIN);

        assertFalse(user.getRoles().isEmpty(), "Roles list should not be empty after adding a role.");
        assertTrue(user.getRoles().contains(Role.ADMIN), "Roles list should contain Role.ADMIN after addition.");
    }

    /**
     * Tests the removeRole method of the User class.
     * It verifies that a role is correctly removed from the user's role list.
     */
    @Test
    @DisplayName("Test removeRole Method")
    public void testRemoveRole() {
        User user = new User("testUser", "password123");
        user.addRole(Role.ADMIN);
        user.removeRole(Role.ADMIN);

        assertFalse(user.getRoles().contains(Role.ADMIN), "Roles list should not contain Role.ADMIN after removal.");
    }

    /**
     * Tests the OTP expiration setting of the User class.
     * It verifies that the OTP expiration time is correctly set.
     */
    @Test
    @DisplayName("Test setOtpExpiration Method")
    public void testOtpExpiration() {
        User user = new User("testUser", "password123");
        LocalDateTime expirationTime = LocalDateTime.now().plusMinutes(30);
        user.setOtpExpiration(expirationTime);

        assertNotNull(user.getOtpExpiration(), "OTP expiration should not be null after setting.");
        assertEquals(expirationTime, user.getOtpExpiration(), "OTP expiration should match the set value.");
    }

    /**
     * Tests the equals and hashCode methods of the User class.
     * It verifies that two users with the same username and password are equal.
     */
    @Test
    @DisplayName("Test equals and hashCode Methods")
    public void testEqualsAndHashCode() {
        User user1 = new User("testUser", "password123");
        user1.addRole(Role.STUDENT);
        user1.setOtpExpiration(LocalDateTime.now().plusMinutes(15));

        User user2 = new User("testUser", "password123");
        user2.addRole(Role.STUDENT);
        user2.setOtpExpiration(LocalDateTime.now().plusMinutes(15));

        User user3 = new User("anotherUser", "password456");
        user3.addRole(Role.ADMIN);

        assertEquals(user1, user2, "Users with the same data should be equal.");
        assertEquals(user1.hashCode(), user2.hashCode(), "Hash codes should be equal for equal users.");
        assertNotEquals(user1, user3, "Users with different data should not be equal.");
    }

    /**
     * Tests the toString method of the User class.
     * It verifies that the toString method returns the correct string representation.
     */
    @Test
    @DisplayName("Test toString Method")
    public void testToString() {
        User user = new User("testUser", "password123");
        user.addRole(Role.INSTRUCTOR);
        user.setOtpExpiration(LocalDateTime.now().plusMinutes(20));

        String expected = "User{username='testUser', password='password123', roles=[INSTRUCTOR], otpExpiration=" + user.getOtpExpiration() + "}";
        String actual = user.toString();

        assertEquals(expected, actual, "toString() should return the correct string representation.");
    }
}
