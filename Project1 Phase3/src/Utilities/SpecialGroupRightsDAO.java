package Utilities;

import models.User;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class SpecialGroupRightsDAO {
    private Connection connection;

    public SpecialGroupRightsDAO() throws SQLException {
        connection = DatabaseManager.getInstance().getConnection();
    }

    public List<User> getAdmins(long groupId) throws SQLException {
        List<User> admins = new ArrayList<>();
        String query = "SELECT u.* FROM Users u JOIN SpecialGroupAdmins sga ON u.username = sga.username WHERE sga.group_id = ?;";
        try (PreparedStatement pstmt = connection.prepareStatement(query)) {
            pstmt.setLong(1, groupId);
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    User user = new User(rs.getString("username"), rs.getString("password"));
                    user.setEmail(rs.getString("email"));
                    user.setFirstName(rs.getString("firstName"));
                    user.setMiddleName(rs.getString("middleName"));
                    user.setLastName(rs.getString("lastName"));
                    user.setPreferredName(rs.getString("preferredName"));
                    admins.add(user);
                }
            }
        }
        return admins;
    }

    // Similar methods for other rights can be added here
}
