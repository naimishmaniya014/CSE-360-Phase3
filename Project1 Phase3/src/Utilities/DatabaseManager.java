package Utilities;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * <p> Title: DatabaseManager Class </p>
 * 
 * <p> Description: This class manages the database connection and initialization.
 * It follows the Singleton design pattern to ensure that only one instance of the 
 * database connection exists throughout the application. The class provides methods 
 * to retrieve the connection, initialize the database schema, and reset the database 
 * for testing purposes. </p>
 * 
 * @version 1.00  2024-10-29  Initial version.
 */
public class DatabaseManager {
    private static DatabaseManager instance;
    private Connection connection;
    // Replace with your actual database URL, username, and password
    private String url = "jdbc:h2:mem:testdb;DB_CLOSE_DELAY=-1"; // In-memory H2 database for testing
    private String username = "sa";
    private String password = "";

    /**
     * Private constructor to enforce Singleton pattern.
     *
     * @throws SQLException If there is an error connecting to the database.
     */
    private DatabaseManager() throws SQLException {
        try {
            Class.forName("org.h2.Driver"); // Ensure H2 driver is loaded
            this.connection = DriverManager.getConnection(url, username, password);
            initializeDatabase(); // Initialize tables
        } catch (ClassNotFoundException ex) {
            throw new SQLException(ex);
        }
    }

    /**
     * Retrieves the singleton instance of DatabaseManager.
     *
     * @return The DatabaseManager instance.
     * @throws SQLException If there is an error connecting to the database.
     */
    public static DatabaseManager getInstance() throws SQLException {
        if (instance == null) {
            instance = new DatabaseManager();
        } else if (instance.getConnection().isClosed()) {
            instance = new DatabaseManager();
        }
        return instance;
    }

    /**
     * Provides access to the database connection.
     *
     * @return The current database connection.
     */
    public Connection getConnection() {
        return connection;
    }

    /**
     * Initializes the database schema by creating necessary tables.
     *
     * @throws SQLException If there is an error executing the SQL statements.
     */
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
     * Resets the database by dropping existing tables and recreating them.
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
