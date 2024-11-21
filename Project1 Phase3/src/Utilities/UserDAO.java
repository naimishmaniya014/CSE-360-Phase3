package Utilities;

import models.Role;
import models.User;

import java.sql.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * <p> Title: UserDAO Class </p>
 * 
 * <p> Description: This class provides Data Access Object (DAO) functionalities for the {@link User} entity.
 * It facilitates CRUD (Create, Read, Update, Delete) operations on the Users table in the database.
 * The class interacts with the {@link DatabaseManager} to execute SQL queries and manage user data.
 * </p>
 * 
 * @version 1.00  2024-10-29  Initial version.
 */
public class UserDAO {
    private Connection connection;

    /**
     * Constructs a UserDAO instance.
     * Initializes the database connection using the DatabaseManager.
     *
     * @throws SQLException If there is an error accessing the database.
     */
    public UserDAO() throws SQLException {
        connection = DatabaseManager.getInstance().getConnection();
    }

    /**
     * Adds a new student to the Users table.
     *
     * @param user The User object representing the student.
     * @throws SQLException If there is an error executing the SQL statement.
     */
    public void addStudent(User user) throws SQLException {
        String insertSQL = "INSERT INTO Users (username, password, role) VALUES (?, ?, ?);";
        try (PreparedStatement pstmt = connection.prepareStatement(insertSQL)) {
            pstmt.setString(1, user.getUsername());
            pstmt.setString(2, user.getPassword()); // Ensure password is hashed in production
            pstmt.setString(3, convertRolesToString(user.getRoles()));
            pstmt.executeUpdate();
        }
    }

    /**
     * Retrieves all students from the Users table.
     *
     * @return A list of User objects representing students.
     * @throws SQLException If there is an error executing the SQL statement.
     */
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

    /**
     * Deletes a student from the Users table by username.
     *
     * @param username The username of the student to delete.
     * @throws SQLException If there is an error executing the SQL statement.
     */
    public void deleteStudent(String username) throws SQLException {
        String deleteSQL = "DELETE FROM Users WHERE username = ? AND role LIKE '%STUDENT%';";
        try (PreparedStatement pstmt = connection.prepareStatement(deleteSQL)) {
            pstmt.setString(1, username);
            pstmt.executeUpdate();
        }
    }

    /**
     * Retrieves a User by their username.
     *
     * @param username The username of the user.
     * @return The User object if found; otherwise, null.
     * @throws SQLException If there is an error executing the SQL statement.
     */
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

    /**
     * Converts a list of Role enums to a comma-separated string.
     *
     * @param roles The list of roles.
     * @return A comma-separated string of role names.
     */
    private String convertRolesToString(List<Role> roles) {
        return roles.stream()
                    .map(Role::name)
                    .collect(Collectors.joining(","));
    }

    /**
     * Parses a comma-separated role string into a list of Role enums.
     *
     * @param rolesStr The comma-separated role string.
     * @return A list of Role enums.
     */
    private List<Role> parseRoles(String rolesStr) {
        if (rolesStr == null || rolesStr.trim().isEmpty()) {
            return new ArrayList<>();
        }
        return Arrays.stream(rolesStr.split(","))
                     .map(String::trim)
                     .map(Role::valueOf)
                     .collect(Collectors.toList());
    }
}
