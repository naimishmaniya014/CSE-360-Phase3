package Utilities;

import models.User;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class SpecialAccessGroupRightsDAO {
    private Connection connection;

    public SpecialAccessGroupRightsDAO() throws SQLException {
        connection = DatabaseManager.getInstance().getConnection();
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

    // Similar methods for instructor viewers/admins and student viewers can be added here

    private User extractUserFromResultSet(ResultSet rs) throws SQLException {
        User user = new User(rs.getString("username"), rs.getString("password"));
        user.setEmail(rs.getString("email"));
        user.setFirstName(rs.getString("firstName"));
        user.setMiddleName(rs.getString("middleName"));
        user.setLastName(rs.getString("lastName"));
        user.setPreferredName(rs.getString("preferredName"));
        // Assume roles are handled separately
        return user;
    }
}
