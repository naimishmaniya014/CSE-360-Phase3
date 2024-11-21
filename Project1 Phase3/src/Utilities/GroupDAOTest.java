package Utilities;

import models.Group;

import java.sql.SQLException;
import java.util.List;

/**
 * <p> Title: GroupDAOTest Class </p>
 * 
 * <p> Description: This class provides a set of tests for the {@link GroupDAO} class.
 * It validates the CRUD functionalities by performing operations such as adding a group,
 * retrieving all groups, retrieving a group by name, updating a group, deleting a group,
 * and deleting all groups. The test outputs indicate the success or failure of each test case.
 * </p>
 * 
 * @author Naimish Maniya
 * 
 * <p> @version 1.00  2024-10-29  Initial version. </p>
 */
public class GroupDAOTest {

    /**
     * The main method to execute the GroupDAO tests.
     *
     * @param args Command-line arguments (not used).
     */
    public static void main(String[] args) {
        GroupDAOTest tester = new GroupDAOTest();
        tester.runTests();
    }

    /**
     * Executes all test cases for the GroupDAO.
     */
    public void runTests() {
        System.out.println("Running GroupDAO tests...");

        try {
            setup();
            testAddGroup();
            testGetAllGroups();
            testGetGroupByName();
            testUpdateGroup();
            testDeleteGroup();
            testDeleteAllGroups();
        } catch (SQLException e) {
            System.out.println("Database Error during tests: " + e.getMessage());
        }

        System.out.println("GroupDAO tests completed.");
    }

    /**
     * Sets up the database for testing by resetting it.
     *
     * @throws SQLException If there is an error accessing the database.
     */
    private void setup() throws SQLException {
        DatabaseManager dbManager = DatabaseManager.getInstance();
        dbManager.resetDatabase();
        System.out.println("Database reset for GroupDAO tests.");
    }

    /**
     * Tests adding a new group.
     *
     * @throws SQLException If a database access error occurs.
     */
    public void testAddGroup() throws SQLException {
        System.out.println("\nTest: Add Group");
        GroupDAO groupDAO = new GroupDAO();
        Group group = new Group("cs");
        groupDAO.addGroup(group);

        if (group.getId() > 0) {
            System.out.println("Passed: Group ID set correctly after insertion.");
        } else {
            System.out.println("Failed: Group ID not set.");
        }

        Group retrieved = groupDAO.getGroupByName("cs");
        if (retrieved != null && retrieved.getName().equals("cs")) {
            System.out.println("Passed: Group retrieved successfully by name.");
        } else {
            System.out.println("Failed: Group retrieval by name unsuccessful.");
        }
    }

    /**
     * Tests retrieving all groups.
     *
     * @throws SQLException If a database access error occurs.
     */
    public void testGetAllGroups() throws SQLException {
        System.out.println("\nTest: Get All Groups");
        GroupDAO groupDAO = new GroupDAO();
        groupDAO.addGroup(new Group("cs"));
        groupDAO.addGroup(new Group("math"));

        List<Group> groups = groupDAO.getAllGroups();
        if (groups.size() == 2) {
            System.out.println("Passed: Retrieved correct number of groups.");
        } else {
            System.out.println("Failed: Incorrect number of groups retrieved.");
        }
    }

    /**
     * Tests retrieving a group by its name.
     *
     * @throws SQLException If a database access error occurs.
     */
    public void testGetGroupByName() throws SQLException {
        System.out.println("\nTest: Get Group By Name");
        GroupDAO groupDAO = new GroupDAO();
        groupDAO.addGroup(new Group("biology"));

        Group group = groupDAO.getGroupByName("biology");
        if (group != null && group.getName().equals("biology")) {
            System.out.println("Passed: Successfully retrieved group by name.");
        } else {
            System.out.println("Failed: Could not retrieve group by name.");
        }
    }

    /**
     * Tests updating an existing group.
     *
     * @throws SQLException If a database access error occurs.
     */
    public void testUpdateGroup() throws SQLException {
        System.out.println("\nTest: Update Group");
        GroupDAO groupDAO = new GroupDAO();
        Group group = new Group("chemistry");
        groupDAO.addGroup(group);

        group.setName("organic chemistry");
        groupDAO.updateGroup(group);

        Group updatedGroup = groupDAO.getGroupByName("organic chemistry");
        if (updatedGroup != null && updatedGroup.getName().equals("organic chemistry")) {
            System.out.println("Passed: Group updated successfully.");
        } else {
            System.out.println("Failed: Group update unsuccessful.");
        }
    }

    /**
     * Tests deleting a specific group.
     *
     * @throws SQLException If a database access error occurs.
     */
    public void testDeleteGroup() throws SQLException {
        System.out.println("\nTest: Delete Group");
        GroupDAO groupDAO = new GroupDAO();
        Group group = new Group("physics");
        groupDAO.addGroup(group);

        groupDAO.deleteGroup(group.getId());

        Group deletedGroup = groupDAO.getGroupByName("physics");
        if (deletedGroup == null) {
            System.out.println("Passed: Group deleted successfully.");
        } else {
            System.out.println("Failed: Group deletion unsuccessful.");
        }
    }

    /**
     * Tests deleting all groups.
     *
     * @throws SQLException If a database access error occurs.
     */
    public void testDeleteAllGroups() throws SQLException {
        System.out.println("\nTest: Delete All Groups");
        GroupDAO groupDAO = new GroupDAO();
        groupDAO.addGroup(new Group("history"));
        groupDAO.addGroup(new Group("geography"));

        groupDAO.deleteAllGroups();

        List<Group> groups = groupDAO.getAllGroups();
        if (groups.isEmpty()) {
            System.out.println("Passed: All groups deleted successfully.");
        } else {
            System.out.println("Failed: Not all groups were deleted.");
        }
    }
}
