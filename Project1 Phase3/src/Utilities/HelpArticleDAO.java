package Utilities;

import models.Group;
import models.HelpArticle;
import models.Role;
import models.User;

import java.sql.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * <p> Title: HelpArticleDAO Class </p>
 * 
 * <p> Description: This class provides Data Access Object (DAO) functionalities for the {@link HelpArticle} entity.
 * It facilitates CRUD (Create, Read, Update, Delete) operations on the HelpArticles table in the database. 
 * Additionally, it manages the associations between help articles and groups through the ArticleGroups table.
 * The class interacts with the {@link DatabaseManager} to execute SQL queries and manage help article data.
 * </p>
 * 
 * @author Naimish Maniya
 * 
 * <p> @version 1.00  2024-10-29  Initial version. </p>
 */
public class HelpArticleDAO {
    private Connection connection;
    private EncryptionUtils encryptionUtils;
    private GroupDAO groupDAO;

    /**
     * Constructs a HelpArticleDAO instance.
     * Initializes the database connection using the DatabaseManager.
     *
     * @throws SQLException If there is an error accessing the database.
     */
    public HelpArticleDAO() throws SQLException {
        connection = DatabaseManager.getInstance().getConnection();
        try {
            encryptionUtils = new EncryptionUtils();
            groupDAO = new GroupDAO();
        } catch (Exception e) {
            throw new SQLException("Initialization failed.", e);
        }
    }

    /**
     * Adds a new help article to the database.
     *
     * @param article The HelpArticle object to add.
     * @throws SQLException If a database access error occurs.
     */
    public void addHelpArticle(HelpArticle article) throws SQLException {
        String insertSQL = "INSERT INTO HelpArticles (header, title, shortDescription, keywords, body, referenceLinks) VALUES (?, ?, ?, ?, ?, ?);";
        try (PreparedStatement pstmt = connection.prepareStatement(insertSQL, Statement.RETURN_GENERATED_KEYS)) {
            pstmt.setString(1, article.getHeader());
            pstmt.setString(2, article.getTitle());
            pstmt.setString(3, article.getShortDescription());
            pstmt.setString(4, String.join(",", article.getKeywords()));
            pstmt.setString(5, article.getBody());
            pstmt.setString(6, String.join(",", article.getReferenceLinks()));
            pstmt.executeUpdate();

            // Retrieve the generated article ID
            try (ResultSet generatedKeys = pstmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    article.setId(generatedKeys.getLong(1));
                } else {
                    throw new SQLException("Creating article failed, no ID obtained.");
                }
            }
        }
    }


    /**
     * Retrieves all help articles from the database.
     *
     * @return A list of all HelpArticle objects.
     * @throws SQLException If a database access error occurs.
     */
    public List<HelpArticle> getAllHelpArticles(User user) throws SQLException {
        List<HelpArticle> articles = new ArrayList<>();
        String query = "SELECT DISTINCT ha.* FROM HelpArticles ha " +
                       "LEFT JOIN ArticleGroups ag ON ha.id = ag.article_id " +
                       "LEFT JOIN GroupMembers gm ON ag.group_id = gm.group_id " +
                       "WHERE gm.username = ? OR ag.group_id IS NULL"; // Articles not associated with any group are accessible to all

        try (PreparedStatement pstmt = connection.prepareStatement(query)) {
            pstmt.setString(1, user.getUsername());
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    HelpArticle article = new HelpArticle();
                    article.setId(rs.getLong("id"));
                    article.setHeader(rs.getString("header"));
                    article.setTitle(rs.getString("title"));
                    article.setShortDescription(rs.getString("shortDescription"));
                    article.setKeywords(Arrays.asList(rs.getString("keywords").split(",")));
                    article.setBody(rs.getString("body"));
                    article.setReferenceLinks(Arrays.asList(rs.getString("referenceLinks").split(",")));
                    articles.add(article);
                }
            }
        }
        return articles;
    }

    /**
     * Updates an existing help article in the database.
     *
     * @param article The HelpArticle object with updated information.
     * @throws SQLException If a database access error occurs.
     */
    public void updateHelpArticle(HelpArticle article) throws SQLException {
        String updateSQL = "UPDATE HelpArticles SET header = ?, title = ?, shortDescription = ?, keywords = ?, body = ?, referenceLinks = ? WHERE id = ?;";
        try (PreparedStatement pstmt = connection.prepareStatement(updateSQL)) {
            pstmt.setString(1, article.getHeader());
            pstmt.setString(2, article.getTitle());
            pstmt.setString(3, article.getShortDescription());
            pstmt.setString(4, String.join(",", article.getKeywords()));
            pstmt.setString(5, article.getBody());
            pstmt.setString(6, String.join(",", article.getReferenceLinks()));
            pstmt.setLong(7, article.getId());
            pstmt.executeUpdate();
        }
    }

    /**
     * Deletes a help article from the database.
     *
     * @param articleId The ID of the help article to delete.
     * @throws SQLException If a database access error occurs.
     */
    public void deleteHelpArticle(long articleId) throws SQLException {
        String deleteSQL = "DELETE FROM HelpArticles WHERE id = ?;";
        try (PreparedStatement pstmt = connection.prepareStatement(deleteSQL)) {
            pstmt.setLong(1, articleId);
            pstmt.executeUpdate();
        }
    }

    /**
     * Deletes all help articles from the database.
     *
     * @throws SQLException If a database access error occurs.
     */
    public void deleteAllHelpArticles() throws SQLException {
        String deleteSQL = "DELETE FROM HelpArticles;";
        try (PreparedStatement pstmt = connection.prepareStatement(deleteSQL)) {
            pstmt.executeUpdate();
        }
    }

    /**
     * Associates a HelpArticle with a Group.
     *
     * @param articleId The ID of the HelpArticle.
     * @param groupId   The ID of the Group.
     * @throws SQLException If a database access error occurs.
     */
    public void associateArticleWithGroup(long articleId, long groupId) throws SQLException {
        Group group = groupDAO.getGroupById(groupId);
        if (group != null && group.isSpecialAccessGroup()) {
            String insertAssociationSQL = "MERGE INTO ArticleGroups (article_id, group_id) KEY (article_id, group_id) VALUES (?, ?);";
            try (PreparedStatement pstmt = connection.prepareStatement(insertAssociationSQL)) {
                pstmt.setLong(1, articleId);
                pstmt.setLong(2, groupId);
                pstmt.executeUpdate();
            }
        } else {
            throw new SQLException("Cannot associate article with a non-special access group.");
        }
    }


    /**
     * Removes the association between a HelpArticle and a Group.
     *
     * @param articleId The ID of the HelpArticle.
     * @param groupId   The ID of the Group.
     * @throws SQLException If a database access error occurs.
     */
    public void dissociateArticleFromGroup(long articleId, long groupId) throws SQLException {
        String deleteAssociationSQL = "DELETE FROM ArticleGroups WHERE article_id = ? AND group_id = ?;";
        try (PreparedStatement pstmt = connection.prepareStatement(deleteAssociationSQL)) {
            pstmt.setLong(1, articleId);
            pstmt.setLong(2, groupId);
            pstmt.executeUpdate();
        }
    }

    /**
     * Retrieves HelpArticles belonging to a specific group.
     *
     * @param groupId The ID of the group.
     * @return A list of HelpArticles associated with the group.
     * @throws SQLException If a database access error occurs.
     */

    /**
     * Retrieves Groups associated with a specific HelpArticle.
     *
     * @param articleId The ID of the HelpArticle.
     * @return A list of Groups associated with the article.
     * @throws SQLException If a database access error occurs.
     */
    public List<Group> getGroupsByArticleId(long articleId) throws SQLException {
        List<Group> groups = new ArrayList<>();
        String query = "SELECT g.* FROM Groups g " +
                       "JOIN ArticleGroups ag ON g.id = ag.group_id " +
                       "WHERE ag.article_id = ?;";
        try (PreparedStatement pstmt = connection.prepareStatement(query)) {
            pstmt.setLong(1, articleId);
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    Group group = new Group();
                    group.setId(rs.getLong("id"));
                    group.setName(rs.getString("name"));
                    groups.add(group);
                }
            }
        }
        return groups;
    }

    /**
     * Clears all group associations for a specific group.
     *
     * @param groupId The ID of the Group.
     * @throws SQLException If a database access error occurs.
     */
    public void clearAssociationsForGroup(long groupId) throws SQLException {
        String deleteSQL = "DELETE FROM ArticleGroups WHERE group_id = ?;";
        try (PreparedStatement pstmt = connection.prepareStatement(deleteSQL)) {
            pstmt.setLong(1, groupId);
            pstmt.executeUpdate();
        }
    }

    /**
     * Clears all group associations for a specific article.
     *
     * @param articleId The ID of the HelpArticle.
     * @throws SQLException If a database access error occurs.
     */
    public void clearAssociationsForArticle(long articleId) throws SQLException {
        String deleteSQL = "DELETE FROM ArticleGroups WHERE article_id = ?;";
        try (PreparedStatement pstmt = connection.prepareStatement(deleteSQL)) {
            pstmt.setLong(1, articleId);
            pstmt.executeUpdate();
        }
    }

    /**
     * Clears all group associations.
     *
     * @throws SQLException If a database access error occurs.
     */
    public void clearAllAssociations() throws SQLException {
        String deleteSQL = "DELETE FROM ArticleGroups;";
        try (PreparedStatement pstmt = connection.prepareStatement(deleteSQL)) {
            pstmt.executeUpdate();
        }
    }

    /**
     * Extracts a HelpArticle object from the current row of the ResultSet.
     *
     * @param rs The ResultSet positioned at the desired row.
     * @return A HelpArticle object.
     * @throws SQLException If data extraction fails.
     */
//    private HelpArticle extractHelpArticleFromResultSet(ResultSet rs) throws SQLException {
//        HelpArticle article = new HelpArticle(
//            rs.getLong("id"),
//            rs.getString("header"),
//            rs.getString("title"),
//            rs.getString("shortDescription"),
//            Arrays.asList(rs.getString("keywords").split(",")),
//            rs.getString("body"),
//            Arrays.asList(rs.getString("referenceLinks").split(",")),
//            rs.getString("contentLevel"),
//            rs.getString("author") // Set author
//        );
//        return article;
//    }

    
    public boolean isSpecialAccessGroup(long groupId) throws SQLException {
        String query = "SELECT isSpecialAccessGroup FROM Groups WHERE id = ?;";
        try (PreparedStatement pstmt = connection.prepareStatement(query)) {
            pstmt.setLong(1, groupId);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getBoolean("isSpecialAccessGroup");
                }
            }
        }
        return false;
    }
    
    public HelpArticle getHelpArticleById(long articleId, User user) throws SQLException {
        String query = "SELECT ha.* FROM HelpArticles ha " +
                       "LEFT JOIN ArticleGroups ag ON ha.id = ag.article_id " +
                       "LEFT JOIN GroupMembers gm ON ag.group_id = gm.group_id " +
                       "WHERE ha.id = ? AND (gm.username = ? OR ag.group_id IS NULL)";

        try (PreparedStatement pstmt = connection.prepareStatement(query)) {
            pstmt.setLong(1, articleId);
            pstmt.setString(2, user.getUsername());
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    HelpArticle article = new HelpArticle();
                    article.setId(rs.getLong("id"));
                    article.setHeader(rs.getString("header"));
                    article.setTitle(rs.getString("title"));
                    article.setShortDescription(rs.getString("shortDescription"));
                    article.setKeywords(Arrays.asList(rs.getString("keywords").split(",")));
                    article.setBody(rs.getString("body"));
                    article.setReferenceLinks(Arrays.asList(rs.getString("referenceLinks").split(",")));
                    return article;
                }
            }
        }
        return null;
    }
    
    public Long getGroupIdByArticleId(long articleId) throws SQLException {
        String query = "SELECT group_id FROM ArticleGroups WHERE article_id = ? LIMIT 1;";
        try (PreparedStatement pstmt = connection.prepareStatement(query)) {
            pstmt.setLong(1, articleId);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getLong("group_id");
                }
            }
        }
        return null;
    }

    private boolean hasViewRights(long groupId, User user) throws SQLException {
        if (user.getRoles().contains(Role.ADMIN)) {
            return false;
        }
        List<User> groupAdmins = groupDAO.getSpecialGroupAdmins(groupId);
        List<User> instructorViewers = groupDAO.getSpecialGroupInstructorViewers(groupId);
        List<User> instructorAdmins = groupDAO.getSpecialGroupInstructorAdmins(groupId);
        List<User> studentViewers = groupDAO.getSpecialGroupStudentViewers(groupId);
        if (groupAdmins.contains(user)) {
            return true;
        }
        if (instructorAdmins.contains(user) || instructorViewers.contains(user) || studentViewers.contains(user)) {
            return true;
        }
        return false;
    }
 
    
    public List<HelpArticle> searchHelpArticles(User user, String query, String groupName) throws SQLException {
        List<HelpArticle> articles = new ArrayList<>();
        StringBuilder sql = new StringBuilder("SELECT DISTINCT ha.* FROM HelpArticles ha ");
        sql.append("LEFT JOIN ArticleGroups ag ON ha.id = ag.article_id ");
        sql.append("LEFT JOIN GroupMembers gm ON ag.group_id = gm.group_id ");
        sql.append("WHERE (ha.title LIKE ? OR ha.shortDescription LIKE ? OR ha.keywords LIKE ?) ");
        sql.append("AND (ag.group_id IN (SELECT group_id FROM GroupMembers WHERE username = ?) OR ag.group_id IS NULL) ");

        try (PreparedStatement pstmt = connection.prepareStatement(sql.toString())) {
            String likeQuery = "%" + query + "%";
            pstmt.setString(1, likeQuery);
            pstmt.setString(2, likeQuery);
            pstmt.setString(3, likeQuery);
            pstmt.setString(4, user.getUsername());

            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    HelpArticle article = new HelpArticle();
                    article.setId(rs.getLong("id"));
                    article.setHeader(rs.getString("header"));
                    article.setTitle(rs.getString("title"));
                    article.setShortDescription(rs.getString("shortDescription"));
                    article.setKeywords(Arrays.asList(rs.getString("keywords").split(",")));
                    article.setBody(rs.getString("body"));
                    article.setReferenceLinks(Arrays.asList(rs.getString("referenceLinks").split(",")));
                    articles.add(article);
                }
            }
        }

        return articles;
    }

    

    /**
     * Provides access to the database connection.
     *
     * @return The current database connection.
     */
    public Connection getConnection() {
        return connection;
    }

    public List<HelpArticle> getArticlesByGroupId(long groupId, boolean hasViewRights) throws SQLException {
        List<HelpArticle> articles = new ArrayList<>();
        String query = "SELECT ha.*, g.isSpecialAccessGroup FROM HelpArticles ha " +
                       "JOIN ArticleGroups ag ON ha.id = ag.article_id " +
                       "JOIN Groups g ON ag.group_id = g.id " +
                       "WHERE ag.group_id = ?;";
        return articles;
    }
}
