// src/test/java/Utilities/UserDAOTest.java
package Utilities;

import models.Role;
import models.User;
import org.junit.jupiter.api.*;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class UserDAOTest {
    private static TestDatabaseManager testDbManager;
    private static Connection connection;
    private UserDAO userDAO;

    @BeforeAll
    static void setupAll() throws SQLException {
        testDbManager = TestDatabaseManager.getInstance();
        connection = testDbManager.getConnection();
    }

    @BeforeEach
    void setup() throws SQLException {
        testDbManager.resetDatabase();
        userDAO = new UserDAO();
    }

    @Test
    void testAddAndRetrieveUser() throws SQLException {
        User user = new User("john_doe", "securepassword");
        user.setRoles(Arrays.asList(Role.INSTRUCTOR));
        userDAO.addStudent(user); // Assuming 'addStudent' handles general user addition

        User retrieved = userDAO.getUserByUsername("john_doe");
        assertNotNull(retrieved, "Retrieved user should not be null.");
        assertEquals("john_doe", retrieved.getUsername(), "Usernames should match.");
        assertEquals("securepassword", retrieved.getPassword(), "Passwords should match.");
        assertTrue(retrieved.getRoles().contains(Role.INSTRUCTOR), "User should have INSTRUCTOR role.");
    }

    @Test
    void testDeleteUser() throws SQLException {
        User user = new User("jane_doe", "anotherpassword");
        user.setRoles(Arrays.asList(Role.STUDENT));
        userDAO.addStudent(user);

        User retrieved = userDAO.getUserByUsername("jane_doe");
        assertNotNull(retrieved, "User should exist before deletion.");

        userDAO.deleteStudent("jane_doe");

        User deleted = userDAO.getUserByUsername("jane_doe");
        assertNull(deleted, "User should be null after deletion.");
    }

    @Test
    void testGetAllStudents() throws SQLException {
        User student1 = new User("student1", "pass1");
        student1.setRoles(Arrays.asList(Role.STUDENT));
        userDAO.addStudent(student1);

        User student2 = new User("student2", "pass2");
        student2.setRoles(Arrays.asList(Role.STUDENT));
        userDAO.addStudent(student2);

        User instructor = new User("instructor1", "instrpass");
        instructor.setRoles(Arrays.asList(Role.INSTRUCTOR));
        userDAO.addStudent(instructor); // Assuming 'addStudent' can add any role

        List<User> students = userDAO.getAllStudents();
        assertEquals(2, students.size(), "There should be two students.");
        assertTrue(students.stream().anyMatch(u -> u.getUsername().equals("student1")), "student1 should be in the list.");
        assertTrue(students.stream().anyMatch(u -> u.getUsername().equals("student2")), "student2 should be in the list.");
    }

    @Test
    void testDuplicateUsername() throws SQLException {
        User user1 = new User("duplicateUser", "password1");
        user1.setRoles(Arrays.asList(Role.STUDENT));
        userDAO.addStudent(user1);

        User user2 = new User("duplicateUser", "password2");
        user2.setRoles(Arrays.asList(Role.INSTRUCTOR));

        assertThrows(SQLException.class, () -> userDAO.addStudent(user2), "Adding a user with duplicate username should throw SQLException.");
    }
}
