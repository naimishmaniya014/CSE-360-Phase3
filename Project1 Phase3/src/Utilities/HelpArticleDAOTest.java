//package Utilities;
//
//import models.Group;
//import models.HelpArticle;
//
//import java.sql.SQLException;
//import java.util.Arrays;
//import java.util.List;
//
///**
// * <p> Title: HelpArticleDAOTest Class </p>
// * 
// * <p> Description: This class provides a set of tests for the {@link HelpArticleDAO} class.
// * It validates the CRUD functionalities and association methods by performing operations such as 
// * adding a help article, retrieving all help articles, retrieving a help article by ID, updating 
// * a help article, deleting a help article, deleting all help articles, associating articles with 
// * groups, dissociating articles from groups, retrieving articles by group ID, and retrieving 
// * groups by article ID. The test outputs indicate the success or failure of each test case. </p>
// * 
// * @author Naimish Maniya
// * 
// * <p> @version 1.00  2024-10-29  Initial version. </p>
// */
//public class HelpArticleDAOTest {
//
//    /**
//     * The main method to execute the HelpArticleDAO tests.
//     *
//     * @param args Command-line arguments (not used).
//     */
//    public static void main(String[] args) {
//        HelpArticleDAOTest tester = new HelpArticleDAOTest();
//        tester.runTests();
//    }
//
//    /**
//     * Executes all test cases for the HelpArticleDAO.
//     */
//    public void runTests() {
//        System.out.println("Running HelpArticleDAO tests...");
//
//        try {
//            setup();
//            testAddHelpArticle();
//            testGetHelpArticleById();
//            testUpdateHelpArticle();
//            testDeleteHelpArticle();
//            testDeleteAllHelpArticles();
//        } catch (SQLException e) {
//            System.out.println("Database Error during tests: " + e.getMessage());
//        }
//
//        System.out.println("HelpArticleDAO tests completed.");
//    }
//
//    /**
//     * Sets up the database for testing by resetting it.
//     *
//     * @throws SQLException If there is an error accessing the database.
//     */
//    private void setup() throws SQLException {
//        DatabaseManager dbManager = DatabaseManager.getInstance();
//        dbManager.resetDatabase();
//        System.out.println("Database reset for HelpArticleDAO tests.");
//    }
//
//    /**
//     * Tests adding a new help article.
//     *
//     * @throws SQLException If a database access error occurs.
//     */
//    public void testAddHelpArticle() throws SQLException {
//        System.out.println("\nTest: Add HelpArticle");
//        HelpArticleDAO helpArticleDAO = new HelpArticleDAO();
//        HelpArticle article = new HelpArticle(
//                0, "Header1",
//                "Java Basics",
//                "Introduction to Java",
//                Arrays.asList("java", "programming"),
//                "Java is a high-level programming language.",
//                Arrays.asList("https://java.com")
//        );
//        helpArticleDAO.addHelpArticle(article, false);
//
//        if (article.getId() > 0) {
//            System.out.println("Passed: HelpArticle ID set correctly after insertion.");
//        } else {
//            System.out.println("Failed: HelpArticle ID not set.");
//        }
//
//        HelpArticle retrieved = helpArticleDAO.getHelpArticleById(article.getId(), false);
//        if (retrieved != null && retrieved.getTitle().equals("Java Basics")) {
//            System.out.println("Passed: HelpArticle retrieved successfully by ID.");
//        } else {
//            System.out.println("Failed: HelpArticle retrieval by ID unsuccessful.");
//        }
//    }
//
//    /**
//     * Tests retrieving a help article by its ID.
//     *
//     * @throws SQLException If a database access error occurs.
//     */
//    public void testGetHelpArticleById() throws SQLException {
//        System.out.println("\nTest: Get HelpArticle By ID");
//        HelpArticleDAO helpArticleDAO = new HelpArticleDAO();
//        HelpArticle article = new HelpArticle(
//                0, "Header3",
//                "C++ Basics",
//                "Introduction to C++",
//                Arrays.asList("c++", "programming"),
//                "C++ is a powerful system programming language.",
//                Arrays.asList("https://cplusplus.com")
//        );
//        helpArticleDAO.addHelpArticle(article, false);
//
//        HelpArticle retrieved = helpArticleDAO.getHelpArticleById(article.getId(), false);
//        if (retrieved != null && retrieved.getTitle().equals("C++ Basics")) {
//            System.out.println("Passed: Successfully retrieved HelpArticle by ID.");
//        } else {
//            System.out.println("Failed: Could not retrieve HelpArticle by ID.");
//        }
//    }
//
//    /**
//     * Tests updating an existing help article.
//     *
//     * @throws SQLException If a database access error occurs.
//     */
//    public void testUpdateHelpArticle() throws SQLException {
//        System.out.println("\nTest: Update HelpArticle");
//        HelpArticleDAO helpArticleDAO = new HelpArticleDAO();
//        HelpArticle article = new HelpArticle(
//                0, "Header4",
//                "Java Basics",
//                "Introduction to Java",
//                Arrays.asList("java", "programming"),
//                "Java is a high-level programming language.",
//                Arrays.asList("https://java.com")
//        );
//        helpArticleDAO.addHelpArticle(article, false);
//
//        article.setTitle("Advanced Java");
//        helpArticleDAO.updateHelpArticle(article, false);
//
//        HelpArticle updated = helpArticleDAO.getHelpArticleById(article.getId(), false);
//        if (updated != null && updated.getTitle().equals("Advanced Java")) {
//            System.out.println("Passed: HelpArticle updated successfully.");
//        } else {
//            System.out.println("Failed: HelpArticle update unsuccessful.");
//        }
//    }
//
//    /**
//     * Tests deleting a specific help article.
//     *
//     * @throws SQLException If a database access error occurs.
//     */
//    public void testDeleteHelpArticle() throws SQLException {
//        System.out.println("\nTest: Delete HelpArticle");
//        HelpArticleDAO helpArticleDAO = new HelpArticleDAO();
//        HelpArticle article = new HelpArticle(
//                0, "Header5",
//                "Ruby Basics",
//                "Introduction to Ruby",
//                Arrays.asList("ruby", "programming"),
//                "Ruby is a dynamic, open source programming language.",
//                Arrays.asList("https://ruby-lang.org")
//        );
//        helpArticleDAO.addHelpArticle(article, false);
//
//        helpArticleDAO.deleteHelpArticle(article.getId());
//
//        HelpArticle deleted = helpArticleDAO.getHelpArticleById(article.getId(), false);
//        if (deleted == null) {
//            System.out.println("Passed: HelpArticle deleted successfully.");
//        } else {
//            System.out.println("Failed: HelpArticle deletion unsuccessful.");
//        }
//    }
//
//    /**
//     * Tests deleting all help articles.
//     *
//     * @throws SQLException If a database access error occurs.
//     */
//    public void testDeleteAllHelpArticles() throws SQLException {
//        System.out.println("\nTest: Delete All HelpArticles");
//        HelpArticleDAO helpArticleDAO = new HelpArticleDAO();
//        helpArticleDAO.addHelpArticle(new HelpArticle(
//                0, "Header6",
//                "Go Basics",
//                "Introduction to Go",
//                Arrays.asList("go", "programming"),
//                "Go is an open source programming language designed for simplicity.",
//                Arrays.asList("https://golang.org")
//        ), false);
//        helpArticleDAO.addHelpArticle(new HelpArticle(
//                0, "Header7",
//                "Swift Basics",
//                "Introduction to Swift",
//                Arrays.asList("swift", "programming"),
//                "Swift is a powerful and intuitive programming language for iOS.",
//                Arrays.asList("https://swift.org")
//        ), false);
//
//        helpArticleDAO.deleteAllHelpArticles();
//
//        List<HelpArticle> articles = helpArticleDAO.getAllHelpArticles();
//        if (articles.isEmpty()) {
//            System.out.println("Passed: All HelpArticles deleted successfully.");
//        } else {
//            System.out.println("Failed: Not all HelpArticles were deleted.");
//        }
//    }
//}
