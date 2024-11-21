// src/test/java/Utilities/HelpArticleDAOTest.java
package Utilities;

import models.HelpArticle;
import models.Role;
import models.User;
import org.junit.jupiter.api.*;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class HelpArticleDAOTest {
    private static TestDatabaseManager testDbManager;
    private static Connection connection;
    private HelpArticleDAO helpArticleDAO;

    @BeforeAll
    static void setupAll() throws SQLException {
        testDbManager = TestDatabaseManager.getInstance();
        connection = testDbManager.getConnection();
    }

    @BeforeEach
    void setup() throws SQLException {
        testDbManager.resetDatabase();
        helpArticleDAO = new HelpArticleDAO(connection);
        // Insert a test user
        UserDAO userDAO = new UserDAO();
        User testUser = new User("testuser", "password");
        testUser.setRoles(Arrays.asList(Role.INSTRUCTOR));
        userDAO.addStudent(testUser);
        // Insert a test group
        GroupDAO groupDAO = new GroupDAO();
        groupDAO.createGroup("TestGroup", false);
    }

    @Test
    void testAddHelpArticle() throws SQLException {
        HelpArticle article = new HelpArticle();
        article.setHeader("Test Header");
        article.setTitle("Test Title");
        article.setShortDescription("Test Short Description");
        article.setKeywords(Arrays.asList("test", "article"));
        article.setBody("This is the body of the test article.");
        article.setReferenceLinks(Arrays.asList("http://example.com"));

        helpArticleDAO.addHelpArticle(article);
        assertTrue(article.getId() > 0, "Article ID should be set after insertion.");

        // Retrieve the article
        User user = new User("testuser", "password");
        user.setRoles(Arrays.asList(Role.INSTRUCTOR));
        HelpArticle retrieved = helpArticleDAO.getHelpArticleById(article.getId(), user);
        assertNotNull(retrieved, "Retrieved article should not be null.");
        assertEquals("Test Title", retrieved.getTitle(), "Titles should match.");
    }

    @Test
    void testGetAllHelpArticles() throws SQLException {
        // Insert multiple articles
        HelpArticle article1 = new HelpArticle();
        article1.setHeader("Header1");
        article1.setTitle("Title1");
        article1.setShortDescription("Short Description1");
        article1.setKeywords(Arrays.asList("java", "junit"));
        article1.setBody("Body1");
        article1.setReferenceLinks(Arrays.asList("http://link1.com"));
        helpArticleDAO.addHelpArticle(article1);

        HelpArticle article2 = new HelpArticle();
        article2.setHeader("Header2");
        article2.setTitle("Title2");
        article2.setShortDescription("Short Description2");
        article2.setKeywords(Arrays.asList("testing", "dao"));
        article2.setBody("Body2");
        article2.setReferenceLinks(Arrays.asList("http://link2.com"));
        helpArticleDAO.addHelpArticle(article2);

        User user = new User("testuser", "password");
        user.setRoles(Arrays.asList(Role.INSTRUCTOR));

        List<HelpArticle> articles = helpArticleDAO.getAllHelpArticles(user);
        assertEquals(2, articles.size(), "There should be two articles.");
    }

    @Test
    void testUpdateHelpArticle() throws SQLException {
        HelpArticle article = new HelpArticle();
        article.setHeader("Original Header");
        article.setTitle("Original Title");
        article.setShortDescription("Original Short Description");
        article.setKeywords(Arrays.asList("original", "test"));
        article.setBody("Original Body");
        article.setReferenceLinks(Arrays.asList("http://original.com"));
        helpArticleDAO.addHelpArticle(article);

        // Update the article
        article.setHeader("Updated Header");
        article.setTitle("Updated Title");
        article.setShortDescription("Updated Short Description");
        article.setKeywords(Arrays.asList("updated", "test"));
        article.setBody("Updated Body");
        article.setReferenceLinks(Arrays.asList("http://updated.com"));
        helpArticleDAO.updateHelpArticle(article);

        // Retrieve the updated article
        User user = new User("testuser", "password");
        user.setRoles(Arrays.asList(Role.INSTRUCTOR));
        HelpArticle updated = helpArticleDAO.getHelpArticleById(article.getId(), user);
        assertNotNull(updated, "Updated article should not be null.");
        assertEquals("Updated Title", updated.getTitle(), "Title should be updated.");
        assertEquals("Updated Body", updated.getBody(), "Body should be updated.");
    }

    @Test
    void testDeleteHelpArticle() throws SQLException {
        HelpArticle article = new HelpArticle();
        article.setHeader("To Be Deleted");
        article.setTitle("Delete Me");
        article.setShortDescription("This article will be deleted.");
        article.setKeywords(Arrays.asList("delete", "test"));
        article.setBody("Delete this article.");
        article.setReferenceLinks(Arrays.asList("http://delete.com"));
        helpArticleDAO.addHelpArticle(article);

        // Verify insertion
        User user = new User("testuser", "password");
        user.setRoles(Arrays.asList(Role.INSTRUCTOR));
        HelpArticle retrieved = helpArticleDAO.getHelpArticleById(article.getId(), user);
        assertNotNull(retrieved, "Article should exist before deletion.");

        // Delete the article
        helpArticleDAO.deleteHelpArticle(article.getId());

        // Verify deletion
        HelpArticle deleted = helpArticleDAO.getHelpArticleById(article.getId(), user);
        assertNull(deleted, "Article should be null after deletion.");
    }

    @Test
    void testSearchHelpArticles() throws SQLException {
        // Insert articles
        HelpArticle article1 = new HelpArticle();
        article1.setHeader("Java Basics");
        article1.setTitle("Introduction to Java");
        article1.setShortDescription("Basics of Java programming.");
        article1.setKeywords(Arrays.asList("java", "programming"));
        article1.setBody("Java is a high-level programming language.");
        article1.setReferenceLinks(Arrays.asList("http://java.com"));
        helpArticleDAO.addHelpArticle(article1);

        HelpArticle article2 = new HelpArticle();
        article2.setHeader("JUnit Testing");
        article2.setTitle("Writing Tests with JUnit");
        article2.setShortDescription("Guide to writing unit tests using JUnit.");
        article2.setKeywords(Arrays.asList("junit", "testing"));
        article2.setBody("JUnit is a popular testing framework for Java.");
        article2.setReferenceLinks(Arrays.asList("http://junit.org"));
        helpArticleDAO.addHelpArticle(article2);

        User user = new User("testuser", "password");
        user.setRoles(Arrays.asList(Role.INSTRUCTOR));

        // Search for 'Java'
        List<HelpArticle> searchResults = helpArticleDAO.searchHelpArticles(user, "Java", "all");
        assertEquals(1, searchResults.size(), "There should be one search result for 'Java'.");
        assertEquals("Introduction to Java", searchResults.get(0).getTitle(), "Title should match 'Introduction to Java'.");

        // Search for 'testing'
        searchResults = helpArticleDAO.searchHelpArticles(user, "testing", "all");
        assertEquals(1, searchResults.size(), "There should be one search result for 'testing'.");
        assertEquals("Writing Tests with JUnit", searchResults.get(0).getTitle(), "Title should match 'Writing Tests with JUnit'.");

        // Search for 'Guide'
        searchResults = helpArticleDAO.searchHelpArticles(user, "Guide", "all");
        assertEquals(1, searchResults.size(), "There should be one search result for 'Guide'.");
        assertEquals("Writing Tests with JUnit", searchResults.get(0).getTitle(), "Title should match 'Writing Tests with JUnit'.");
    }
}
