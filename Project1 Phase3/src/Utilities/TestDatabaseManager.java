// src/test/java/Utilities/TestDatabaseManager.java
package Utilities;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * <p> Title: TestDatabaseManager Class </p>
 * 
 * <p> Description: This class manages the test database connection and initialization.
 * It sets up an in-memory H2 database for testing purposes. </p>
 * 
 * @version 1.00  2024-11-20  Initial version.
 */
public class TestDatabaseManager {
    private static TestDatabaseManager instance;
    private Connection connection;
    private String url = "jdbc:h2:mem:testdb;DB_CLOSE_DELAY=-1"; // In-memory H2 database
    private String username = "sa";
    private String password = "";

    private TestDatabaseManager() throws SQLException {
        try {
            Class.forName("org.h2.Driver");
            this.connection = DriverManager.getConnection(url, username, password);
            initializeDatabase();
        } catch (ClassNotFoundException e) {
            throw new SQLException(e);
        }
    }

    public static TestDatabaseManager getInstance() throws SQLException {
        if (instance == null) {
            instance = new TestDatabaseManager();
        }
        return instance;
    }

    public Connection getConnection() {
        return connection;
    }

    private void initializeDatabase() throws SQLException {
        String createGroupsTable = "CREATE TABLE IF NOT EXISTS Groups (" +
                "id BIGINT AUTO_INCREMENT PRIMARY KEY," +
                "name VARCHAR(255) UNIQUE NOT NULL," +
                "isSpecialAccessGroup BOOLEAN DEFAULT FALSE" +
                ");";

        String createHelpArticlesTable = "CREATE TABLE IF NOT EXISTS HelpArticles (" +
                "id BIGINT AUTO_INCREMENT PRIMARY KEY," +
                "header VARCHAR(255)," +
                "title VARCHAR(255) NOT NULL," +
                "shortDescription VARCHAR(500)," +
                "keywords VARCHAR(500)," +
                "body CLOB," +
                "referenceLinks VARCHAR(1000)" +
                ");";

        String createArticleGroupsTable = "CREATE TABLE IF NOT EXISTS ArticleGroups (" +
                "article_id BIGINT NOT NULL," +
                "group_id BIGINT NOT NULL," +
                "PRIMARY KEY (article_id, group_id)," +
                "FOREIGN KEY (article_id) REFERENCES HelpArticles(id) ON DELETE CASCADE," +
                "FOREIGN KEY (group_id) REFERENCES Groups(id) ON DELETE CASCADE" +
                ");";

        String createUsersTable = "CREATE TABLE IF NOT EXISTS Users (" +
                "id BIGINT AUTO_INCREMENT PRIMARY KEY," +
                "username VARCHAR(255) UNIQUE NOT NULL," +
                "password VARCHAR(255) NOT NULL," +
                "role VARCHAR(50) NOT NULL" +
                ");";

        String createGroupMembersTable = "CREATE TABLE IF NOT EXISTS GroupMembers (" +
                "group_id BIGINT NOT NULL," +
                "username VARCHAR(255) NOT NULL," +
                "PRIMARY KEY (group_id, username)," +
                "FOREIGN KEY (group_id) REFERENCES Groups(id) ON DELETE CASCADE," +
                "FOREIGN KEY (username) REFERENCES Users(username) ON DELETE CASCADE" +
                ");";

        try (Statement stmt = connection.createStatement()) {
            stmt.execute(createGroupsTable);
            stmt.execute(createHelpArticlesTable);
            stmt.execute(createArticleGroupsTable);
            stmt.execute(createUsersTable);
            stmt.execute(createGroupMembersTable);
        }
    }

    /**
     * Resets the database by dropping all tables and recreating them.
     *
     * @throws SQLException If there is an error executing the SQL statements.
     */
    public void resetDatabase() throws SQLException {
        String dropGroupMembers = "DROP TABLE IF EXISTS GroupMembers;";
        String dropArticleGroups = "DROP TABLE IF EXISTS ArticleGroups;";
        String dropHelpArticles = "DROP TABLE IF EXISTS HelpArticles;";
        String dropGroups = "DROP TABLE IF EXISTS Groups;";
        String dropUsers = "DROP TABLE IF EXISTS Users;";

        try (Statement stmt = connection.createStatement()) {
            stmt.execute(dropGroupMembers);
            stmt.execute(dropArticleGroups);
            stmt.execute(dropHelpArticles);
            stmt.execute(dropGroups);
            stmt.execute(dropUsers);

            // Recreate tables
            initializeDatabase();
        }
    }
}
