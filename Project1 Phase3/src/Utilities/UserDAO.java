package Utilities;

import models.Role;
import models.User;

import java.sql.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class UserDAO {
    private Connection connection;

    public UserDAO() throws SQLException {
        connection = DatabaseManager.getInstance().getConnection();
    }

    public void addUser(User user) throws SQLException {
        String insertSQL = "INSERT INTO Users (username, password, role) VALUES (?, ?, ?);";
        try (PreparedStatement pstmt = connection.prepareStatement(insertSQL)) {
            pstmt.setString(1, user.getUsername());
            pstmt.setString(2, user.getPassword());
            pstmt.setString(3, convertRolesToString(user.getRoles()));
            pstmt.executeUpdate();
        }
    }

    public void addStudent(User user) throws SQLException {
        addUser(user);
    }

    public void deleteUser(String username) throws SQLException {
        String deleteSQL = "DELETE FROM Users WHERE username = ?;";
        try (PreparedStatement pstmt = connection.prepareStatement(deleteSQL)) {
            pstmt.setString(1, username);
            pstmt.executeUpdate();
        }
    }

    public void deleteStudent(String username) throws SQLException {
        String deleteSQL = "DELETE FROM Users WHERE username = ? AND role LIKE '%STUDENT%';";
        try (PreparedStatement pstmt = connection.prepareStatement(deleteSQL)) {
            pstmt.setString(1, username);
            pstmt.executeUpdate();
        }
    }

    public List<User> getAllStudents() throws SQLException {
        List<User> students = new ArrayList<>();
        String query = "SELECT * FROM Users WHERE role LIKE '%STUDENT%';";
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {
            while (rs.next()) {
                User student = new User(rs.getString("username"), rs.getString("password"));
                List<Role> roles = parseRoles(rs.getString("role"));
                student.setRoles(roles);
                students.add(student);
            }
        }
        return students;
    }

    public User getUserByUsername(String username) throws SQLException {
        String query = "SELECT * FROM Users WHERE username = ?;";
        try (PreparedStatement pstmt = connection.prepareStatement(query)) {
            pstmt.setString(1, username);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    User user = new User(rs.getString("username"), rs.getString("password"));
                    List<Role> roles = parseRoles(rs.getString("role"));
                    user.setRoles(roles);
                    return user;
                }
            }
        }
        return null;
    }

    private String convertRolesToString(List<Role> roles) {
        return roles.stream()
                    .map(Role::name)
                    .collect(Collectors.joining(","));
    }

    private List<Role> parseRoles(String rolesStr) {
        if (rolesStr == null || rolesStr.trim().isEmpty()) {
            return new ArrayList<>();
        }
        return Arrays.stream(rolesStr.split(","))
                     .map(String::trim)
                     .map(Role::valueOf)
                     .collect(Collectors.toList());
    }
    
    public void associateArticleWithGroup(long articleId, long groupId) throws SQLException {
        String insertSQL = "INSERT INTO ArticleGroups (article_id, group_id) VALUES (?, ?);";
        try (PreparedStatement pstmt = connection.prepareStatement(insertSQL)) {
            pstmt.setLong(1, articleId);
            pstmt.setLong(2, groupId);
            pstmt.executeUpdate();
        }
    }
}
