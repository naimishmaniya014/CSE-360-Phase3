package Utilities;

import models.Group;
import models.Role;
import models.User;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * <p> Title: GroupDAO Class </p>
 * 
 * <p> Description: This class provides Data Access Object (DAO) functionalities for the {@link Group} entity.
 * It facilitates CRUD (Create, Read, Update, Delete) operations on the Groups table in the database. 
 * The class interacts with the {@link DatabaseManager} to execute SQL queries and manage group data. 
 * </p>
 * 
 * @author Naimish Maniya
 * 
 * <p> @version 1.00  2024-10-29  Initial version. </p>
 */
public class GroupDAO {
    private Connection connection;

    /**
     * Constructs a GroupDAO instance.
     * Initializes the database connection using the DatabaseManager.
     *
     * @throws SQLException If there is an error accessing the database.
     */
    public GroupDAO() throws SQLException {
        connection = DatabaseManager.getInstance().getConnection();
    }
    
    public void addGroup(Group group) throws SQLException {
        String insertSQL = "INSERT INTO Groups (name, isSpecialAccessGroup) VALUES (?, ?);";
        try (PreparedStatement pstmt = connection.prepareStatement(insertSQL, Statement.RETURN_GENERATED_KEYS)) {
            pstmt.setString(1, group.getName());
            pstmt.setBoolean(2, group.isSpecialAccessGroup());
            pstmt.executeUpdate();
            try (ResultSet rs = pstmt.getGeneratedKeys()) {
                if (rs.next()) {
                    group.setId(rs.getLong(1));
                }
            }
        }
    }

    public List<Group> getAllGroups() throws SQLException {
        List<Group> groups = new ArrayList<>();
        String selectSQL = "SELECT * FROM Groups;";
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(selectSQL)) {
            while (rs.next()) {
                Group group = new Group();
                group.setId(rs.getLong("id"));
                group.setName(rs.getString("name"));
                group.setSpecialAccessGroup(rs.getBoolean("isSpecialAccessGroup"));
                groups.add(group);
            }
        }
        return groups;
    }

    public Group getGroupByName(String name) throws SQLException {
        String query = "SELECT * FROM Groups WHERE name = ?;";
        try (PreparedStatement pstmt = connection.prepareStatement(query)) {
            pstmt.setString(1, name);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    Group group = new Group();
                    group.setId(rs.getLong("id"));
                    group.setName(rs.getString("name"));
                    group.setSpecialAccessGroup(rs.getBoolean("isSpecialAccessGroup"));
                    return group;
                }
            }
        }
        return null;
    }

    public Group getGroupById(long groupId) throws SQLException {
        String query = "SELECT * FROM Groups WHERE id = ?;";
        try (PreparedStatement pstmt = connection.prepareStatement(query)) {
            pstmt.setLong(1, groupId);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    Group group = new Group();
                    group.setId(rs.getLong("id"));
                    group.setName(rs.getString("name"));
                    group.setSpecialAccessGroup(rs.getBoolean("isSpecialAccessGroup"));
                    return group;
                }
            }
        }
        return null;
    }

    public void updateGroup(long groupId, String newGroupName, boolean isSpecialAccessGroup) throws SQLException {
        String updateSQL = "UPDATE Groups SET name = ?, isSpecialAccessGroup = ? WHERE id = ?;";
        try (PreparedStatement pstmt = connection.prepareStatement(updateSQL)) {
            pstmt.setString(1, newGroupName);
            pstmt.setBoolean(2, isSpecialAccessGroup);
            pstmt.setLong(3, groupId);
            pstmt.executeUpdate();
        }
    }

    public void deleteGroup(long groupId) throws SQLException {
        String deleteSQL = "DELETE FROM Groups WHERE id = ?;";
        try (PreparedStatement pstmt = connection.prepareStatement(deleteSQL)) {
            pstmt.setLong(1, groupId);
            pstmt.executeUpdate();
        }
    }

    public void deleteAllGroups() throws SQLException {
        String deleteSQL = "DELETE FROM Groups;";
        try (PreparedStatement pstmt = connection.prepareStatement(deleteSQL)) {
            pstmt.executeUpdate();
        }
    }

    public List<User> getSpecialGroupAdmins(long groupId) throws SQLException {
        List<User> admins = new ArrayList<>();
        String query = "SELECT u.* FROM Users u JOIN SpecialGroupAdmins sga ON u.username = sga.username WHERE sga.group_id = ?;";
        try (PreparedStatement pstmt = connection.prepareStatement(query)) {
            pstmt.setLong(1, groupId);
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    User user = extractUserFromResultSet(rs);
                    admins.add(user);
                }
            }
        }
        return admins;
    }

    public void addSpecialGroupAdmin(long groupId, String username) throws SQLException {
        String insertSQL = "INSERT INTO SpecialGroupAdmins (group_id, username) VALUES (?, ?);";
        try (PreparedStatement pstmt = connection.prepareStatement(insertSQL)) {
            pstmt.setLong(1, groupId);
            pstmt.setString(2, username);
            pstmt.executeUpdate();
        }
    }

    public void removeSpecialGroupAdmin(long groupId, String username) throws SQLException {
        String deleteSQL = "DELETE FROM SpecialGroupAdmins WHERE group_id = ? AND username = ?;";
        try (PreparedStatement pstmt = connection.prepareStatement(deleteSQL)) {
            pstmt.setLong(1, groupId);
            pstmt.setString(2, username);
            pstmt.executeUpdate();
        }
    }

    public List<User> getSpecialGroupInstructorViewers(long groupId) throws SQLException {
        List<User> viewers = new ArrayList<>();
        String query = "SELECT u.* FROM Users u JOIN SpecialGroupInstructorViewers sgiv ON u.username = sgiv.username WHERE sgiv.group_id = ?;";
        try (PreparedStatement pstmt = connection.prepareStatement(query)) {
            pstmt.setLong(1, groupId);
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    User user = extractUserFromResultSet(rs);
                    viewers.add(user);
                }
            }
        }
        return viewers;
    }

    public void addSpecialGroupInstructorViewer(long groupId, String username) throws SQLException {
        String insertSQL = "INSERT INTO SpecialGroupInstructorViewers (group_id, username) VALUES (?, ?);";
        try (PreparedStatement pstmt = connection.prepareStatement(insertSQL)) {
            pstmt.setLong(1, groupId);
            pstmt.setString(2, username);
            pstmt.executeUpdate();
        }
    }

    public void removeSpecialGroupInstructorViewer(long groupId, String username) throws SQLException {
        String deleteSQL = "DELETE FROM SpecialGroupInstructorViewers WHERE group_id = ? AND username = ?;";
        try (PreparedStatement pstmt = connection.prepareStatement(deleteSQL)) {
            pstmt.setLong(1, groupId);
            pstmt.setString(2, username);
            pstmt.executeUpdate();
        }
    }

    public List<User> getSpecialGroupInstructorAdmins(long groupId) throws SQLException {
        List<User> admins = new ArrayList<>();
        String query = "SELECT u.* FROM Users u JOIN SpecialGroupInstructorAdmins sgia ON u.username = sgia.username WHERE sgia.group_id = ?;";
        try (PreparedStatement pstmt = connection.prepareStatement(query)) {
            pstmt.setLong(1, groupId);
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    User user = extractUserFromResultSet(rs);
                    admins.add(user);
                }
            }
        }
        return admins;
    }

    public void addSpecialGroupInstructorAdmin(long groupId, String username) throws SQLException {
        if (isFirstInstructorInGroup(groupId, username)) {
            addSpecialGroupInstructorAdminInternal(groupId, username);
            addSpecialGroupInstructorViewerInternal(groupId, username);
        } else {
            addSpecialGroupInstructorAdminInternal(groupId, username);
        }
    }

    private void addSpecialGroupInstructorAdminInternal(long groupId, String username) throws SQLException {
        String insertSQL = "INSERT INTO SpecialGroupInstructorAdmins (group_id, username) VALUES (?, ?);";
        try (PreparedStatement pstmt = connection.prepareStatement(insertSQL)) {
            pstmt.setLong(1, groupId);
            pstmt.setString(2, username);
            pstmt.executeUpdate();
        }
    }

    private void addSpecialGroupInstructorViewerInternal(long groupId, String username) throws SQLException {
        String insertSQL = "INSERT INTO SpecialGroupInstructorViewers (group_id, username) VALUES (?, ?);";
        try (PreparedStatement pstmt = connection.prepareStatement(insertSQL)) {
            pstmt.setLong(1, groupId);
            pstmt.setString(2, username);
            pstmt.executeUpdate();
        }
    }

    public void removeSpecialGroupInstructorAdmin(long groupId, String username) throws SQLException {
        String deleteSQL = "DELETE FROM SpecialGroupInstructorAdmins WHERE group_id = ? AND username = ?;";
        try (PreparedStatement pstmt = connection.prepareStatement(deleteSQL)) {
            pstmt.setLong(1, groupId);
            pstmt.setString(2, username);
            pstmt.executeUpdate();
        }
    }

    public List<User> getSpecialGroupStudentViewers(long groupId) throws SQLException {
        List<User> viewers = new ArrayList<>();
        String query = "SELECT u.* FROM Users u JOIN SpecialGroupStudentViewers sgsv ON u.username = sgsv.username WHERE sgsv.group_id = ?;";
        try (PreparedStatement pstmt = connection.prepareStatement(query)) {
            pstmt.setLong(1, groupId);
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    User user = extractUserFromResultSet(rs);
                    viewers.add(user);
                }
            }
        }
        return viewers;
    }

    public void addSpecialGroupStudentViewer(long groupId, String username) throws SQLException {
        String insertSQL = "INSERT INTO SpecialGroupStudentViewers (group_id, username) VALUES (?, ?);";
        try (PreparedStatement pstmt = connection.prepareStatement(insertSQL)) {
            pstmt.setLong(1, groupId);
            pstmt.setString(2, username);
            pstmt.executeUpdate();
        }
    }

    public void removeSpecialGroupStudentViewer(long groupId, String username) throws SQLException {
        String deleteSQL = "DELETE FROM SpecialGroupStudentViewers WHERE group_id = ? AND username = ?;";
        try (PreparedStatement pstmt = connection.prepareStatement(deleteSQL)) {
            pstmt.setLong(1, groupId);
            pstmt.setString(2, username);
            pstmt.executeUpdate();
        }
    }

    private User extractUserFromResultSet(ResultSet rs) throws SQLException {
        User user = new User(rs.getString("username"), rs.getString("password"));
        user.setEmail(rs.getString("email"));
        user.setFirstName(rs.getString("firstName"));
        user.setMiddleName(rs.getString("middleName"));
        user.setLastName(rs.getString("lastName"));
        user.setPreferredName(rs.getString("preferredName"));
        return user;
    }

    public boolean isFirstInstructorInGroup(long groupId, String username) throws SQLException {
        List<User> instructors = getSpecialGroupInstructorAdmins(groupId);
        return instructors.isEmpty() && userExists(username) && getUserByUsername(username).getRoles().contains(Role.INSTRUCTOR);
    }

    private boolean userExists(String username) throws SQLException {
        String query = "SELECT COUNT(*) FROM Users WHERE username = ?;";
        try (PreparedStatement pstmt = connection.prepareStatement(query)) {
            pstmt.setString(1, username);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            }
        }
        return false;
    }

    public User getUserByUsername(String username) throws SQLException {
        String query = "SELECT * FROM Users WHERE username = ?;";
        try (PreparedStatement pstmt = connection.prepareStatement(query)) {
            pstmt.setString(1, username);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    User user = extractUserFromResultSet(rs);
                    return user;
                }
            }
        }
        return null;
    }

    public List<Group> getGroupsByNames(List<String> groupNames) throws SQLException {
        List<Group> groups = new ArrayList<>();
        if (groupNames == null || groupNames.isEmpty()) {
            return groups;
        }
        StringBuilder queryBuilder = new StringBuilder("SELECT * FROM Groups WHERE name IN (");
        String placeholders = String.join(",", groupNames.stream().map(name -> "?").toArray(String[]::new));
        queryBuilder.append(placeholders).append(");");
        String query = queryBuilder.toString();
        try (PreparedStatement pstmt = connection.prepareStatement(query)) {
            for (int i = 0; i < groupNames.size(); i++) {
                pstmt.setString(i + 1, groupNames.get(i));
            }
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    Group group = new Group();
                    group.setId(rs.getLong("id"));
                    group.setName(rs.getString("name"));
                    group.setSpecialAccessGroup(rs.getBoolean("isSpecialAccessGroup"));
                    groups.add(group);
                }
            }
        }
        return groups;
    }

    public List<Group> getGroupsByInstructor(String username) throws SQLException {
        List<Group> groups = new ArrayList<>();
        String query = "SELECT g.* FROM Groups g JOIN GroupMembers gm ON g.id = gm.group_id WHERE gm.username = ? AND g.isSpecialAccessGroup = FALSE;";
        try (PreparedStatement pstmt = connection.prepareStatement(query)) {
            pstmt.setString(1, username);
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    Group group = new Group();
                    group.setId(rs.getLong("id"));
                    group.setName(rs.getString("name"));
                    group.setSpecialAccessGroup(rs.getBoolean("isSpecialAccessGroup"));
                    groups.add(group);
                }
            }
        }
        return groups;
    }

    public void addStudentToGroup(long groupId, String username) throws SQLException {
        String insertSQL = "INSERT INTO GroupMembers (group_id, username) VALUES (?, ?);";
        try (PreparedStatement pstmt = connection.prepareStatement(insertSQL)) {
            pstmt.setLong(1, groupId);
            pstmt.setString(2, username);
            pstmt.executeUpdate();
        }
    }

    public void removeStudentFromGroup(long groupId, String username) throws SQLException {
        String deleteSQL = "DELETE FROM GroupMembers WHERE group_id = ? AND username = ?;";
        try (PreparedStatement pstmt = connection.prepareStatement(deleteSQL)) {
            pstmt.setLong(1, groupId);
            pstmt.setString(2, username);
            pstmt.executeUpdate();
        }
    }

    public List<String> getGroupMembers(long groupId) throws SQLException {
        List<String> members = new ArrayList<>();
        String query = "SELECT username FROM GroupMembers WHERE group_id = ?;";
        try (PreparedStatement pstmt = connection.prepareStatement(query)) {
            pstmt.setLong(1, groupId);
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    members.add(rs.getString("username"));
                }
            }
        }
        return members;
    }

    
    /**
     * Updates an existing group in the database.
     *
     * @param group The Group object with updated information.
     * @throws SQLException If a database access error occurs.
     */
    public void updateGroup(Group group) throws SQLException {
        String updateSQL = "UPDATE Groups SET name = ?, isSpecialAccessGroup = ? WHERE id = ?;";
        try (PreparedStatement pstmt = connection.prepareStatement(updateSQL)) {
            pstmt.setString(1, group.getName());
            pstmt.setBoolean(2, group.isSpecialAccessGroup());
            pstmt.setLong(3, group.getId());
            pstmt.executeUpdate();
        }
    }
    
    /**
     * Creates a new group.
     *
     * @param groupName The name of the group.
     * @param isSpecialAccessGroup Whether the group is a special access group.
     * @throws SQLException If there is an error executing the SQL statement.
     */
    public void createGroup(String groupName, boolean isSpecialAccessGroup) throws SQLException {
        String insertSQL = "INSERT INTO Groups (name, isSpecialAccessGroup) VALUES (?, ?);";
        try (PreparedStatement pstmt = connection.prepareStatement(insertSQL, Statement.RETURN_GENERATED_KEYS)) {
            pstmt.setString(1, groupName);
            pstmt.setBoolean(2, isSpecialAccessGroup);
            pstmt.executeUpdate();
            try (ResultSet rs = pstmt.getGeneratedKeys()) {
                if (rs.next()) {
                    // Optionally set the ID in the Group object if needed
                }
            }
        }
    }

}
