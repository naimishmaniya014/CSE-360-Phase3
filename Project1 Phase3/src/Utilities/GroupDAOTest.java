// src/test/java/Utilities/GroupDAOTest.java
package Utilities;

import models.Group;
import models.Role;
import models.User;

import org.junit.jupiter.api.*;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class GroupDAOTest {
    private static TestDatabaseManager testDbManager;
    private static Connection connection;
    private GroupDAO groupDAO;

    @BeforeAll
    static void setupAll() throws SQLException {
        testDbManager = TestDatabaseManager.getInstance();
        connection = testDbManager.getConnection();
    }

    @BeforeEach
    void setup() throws SQLException {
        testDbManager.resetDatabase();
        groupDAO = new GroupDAO();
    }

    @Test
    void testCreateAndRetrieveGroup() throws SQLException {
        groupDAO.createGroup("Developers", false);
        List<Group> groups = groupDAO.getAllGroups();
        assertEquals(1, groups.size(), "There should be one group.");
        assertEquals("Developers", groups.get(0).getName(), "Group name should match.");
        assertFalse(groups.get(0).isSpecialAccessGroup(), "Group should not be a special access group.");
    }

    @Test
    void testCreateSpecialAccessGroup() throws SQLException {
        groupDAO.createGroup("Admins", true);
        List<Group> groups = groupDAO.getAllGroups();
        assertEquals(1, groups.size(), "There should be one group.");
        assertEquals("Admins", groups.get(0).getName(), "Group name should match.");
        assertTrue(groups.get(0).isSpecialAccessGroup(), "Group should be a special access group.");
    }

    @Test
    void testDeleteGroup() throws SQLException {
        groupDAO.createGroup("TestGroup", false);
        List<Group> groups = groupDAO.getAllGroups();
        assertEquals(1, groups.size(), "There should be one group before deletion.");

        Group group = groups.get(0);
        groupDAO.deleteGroup(group.getId());

        groups = groupDAO.getAllGroups();
        assertTrue(groups.isEmpty(), "There should be no groups after deletion.");
    }

    @Test
    void testUpdateGroup() throws SQLException {
        groupDAO.createGroup("OldGroupName", false);
        List<Group> groups = groupDAO.getAllGroups();
        Group group = groups.get(0);

        groupDAO.updateGroup(group.getId(), "NewGroupName", true);
        List<Group> updatedGroups = groupDAO.getAllGroups();
        assertEquals(1, updatedGroups.size(), "There should be one group after update.");
        assertEquals("NewGroupName", updatedGroups.get(0).getName(), "Group name should be updated.");
        assertTrue(updatedGroups.get(0).isSpecialAccessGroup(), "Group should be updated to special access group.");
    }

    @Test
    void testAddAndRemoveStudentFromGroup() throws SQLException {
        // Create group and user
        groupDAO.createGroup("TestGroup", false);
        List<Group> groups = groupDAO.getAllGroups();
        Group group = groups.get(0);

        UserDAO userDAO = new UserDAO();
        User student = new User("student1", "pass1");
        student.setRoles(Arrays.asList(Role.STUDENT));
        userDAO.addStudent(student);

        // Add student to group
        groupDAO.addStudentToGroup(group.getId(), "student1");
        List<String> members = groupDAO.getGroupMembers(group.getId());
        assertEquals(1, members.size(), "There should be one member in the group.");
        assertEquals("student1", members.get(0), "Member username should match.");

        // Remove student from group
        groupDAO.removeStudentFromGroup(group.getId(), "student1");
        members = groupDAO.getGroupMembers(group.getId());
        assertTrue(members.isEmpty(), "There should be no members in the group after removal.");
    }

    @Test
    void testDuplicateGroupName() throws SQLException {
        groupDAO.createGroup("UniqueGroup", false);
        assertThrows(SQLException.class, () -> groupDAO.createGroup("UniqueGroup", true), "Creating a group with duplicate name should throw SQLException.");
    }
}
