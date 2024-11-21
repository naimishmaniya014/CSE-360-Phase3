package Controllers;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.stage.Stage;
import models.*;
import Utilities.SessionManager;

/**
 * <p> Title: Main Application Class </p>
 * 
 * <p> Description: This class serves as the entry point for the application.
 * It controls the primary stage and manages navigation between different pages
 * such as login, account setup, role selection, and the home pages for various user roles. 
 * Additionally, it initiates the execution of all test suites upon application launch. </p>
 * 
 * @author Naimish Maniya
 * 
 * <p> @version 1.00   2024-10-29  Initial version. </p>
 */
public class Main extends Application {

    private static Stage primaryStage;

    /**
     * Starts the application and sets the initial stage.
     * The login page is displayed when the application starts.
     *
     * @param primaryStage The primary stage for the application.
     */
    @Override
    public void start(Stage primaryStage) {
        Main.primaryStage = primaryStage;
        primaryStage.setTitle("Help System Application");
        showLoginPage();
        primaryStage.show();
    }

    /**
     * Displays the home page based on the user's selected role.
     * 
     * @param user The user object whose home page is being displayed.
     * @param role The selected role determining which home page to display (Admin, Student, Instructor).
     */
    public static void showHomePage(User user, Role role) {
        if (role == null) {
            showLoginPage();
            showAlert(Alert.AlertType.ERROR, "Role Error", "User role is not set. Please log in again.");
            return;
        }

        if (role == Role.ADMIN) {
            AdminHomePage adminHomePage = new AdminHomePage(user);
            Scene scene = new Scene(adminHomePage.getView(), 800, 600);
            primaryStage.setScene(scene);
        } else if (role == Role.INSTRUCTOR) {
            InstructorHomePage instructorHomePage = new InstructorHomePage(user);
            Scene scene = new Scene(instructorHomePage.getView(), 800, 600);
            primaryStage.setScene(scene);
        } else if (role == Role.STUDENT) {
            StudentHomePage studentHomePage = new StudentHomePage(user);
            Scene scene = new Scene(studentHomePage.getView(), 800, 600);
            getStage().setScene(scene);
        }
    }

    /**
     * Shows an alert dialog with the specified parameters.
     *
     * @param type    The type of alert (e.g., INFORMATION, ERROR, WARNING).
     * @param title   The title of the alert dialog.
     * @param content The content message of the alert dialog.
     */
    private static void showAlert(Alert.AlertType type, String title, String content) {
        Alert alert = new Alert(type, content, ButtonType.OK);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.showAndWait();
    }

    /**
     * Displays the login page in the primary stage.
     */
    public static void showLoginPage() {
        LoginPage loginPage = new LoginPage();
        Scene scene = new Scene(loginPage.getView(), 400, 300);
        primaryStage.setScene(scene);
    }

    /**
     * Displays the account setup page for a new user.
     * 
     * @param user The user object whose account is being set up.
     */
    public static void showAccountSetupPage(User user) {
        AccountSetupPage accountSetupPage = new AccountSetupPage(user);
        Scene scene = new Scene(accountSetupPage.getView(), 400, 400);
        primaryStage.setScene(scene);
    }

    /**
     * Displays the role selection page for users with multiple roles.
     * 
     * @param user The user object selecting their role.
     */
    public static void showRoleSelectionPage(User user) {
        RoleSelectionPage roleSelectionPage = new RoleSelectionPage(user);
        Scene scene = new Scene(roleSelectionPage.getView(), 400, 400);
        primaryStage.setScene(scene);
    }

    /**
     * Retrieves the primary stage of the application.
     * 
     * @return The primary stage being used by the application.
     */
    public static Stage getStage() {
        return primaryStage;
    }

    /**
     * Main method that launches the JavaFX application and executes all test suites.
     * 
     * @param args The command line arguments passed to the application.
     */
    public static void main(String[] args) {
        launch(args);
        System.out.println("Running all tests...");

        try {
            // 1. Call Role Tests
            models.RoleTest roleTester = new models.RoleTest();
            roleTester.runTests();

            // 2. Call UserManager Tests
            Utilities.UserManagerTest userManagerTester = new Utilities.UserManagerTest();
            userManagerTester.runTests();
            
            // 5. Call BackupRestoreManager Tests (Newly Added)
            Utilities.BackupRestoreManagerTest backupRestoreManagerTester = new Utilities.BackupRestoreManagerTest();
            backupRestoreManagerTester.runTests();

            // 6. Call User Tests
            models.UserTest userTester = new models.UserTest();
            userTester.runTests();

            // 7. Call InvitationCode Tests
            models.InvitationCodeTest invitationCodeTester = new models.InvitationCodeTest();
            invitationCodeTester.runTests();
            
//            // 8. Call HelpArticleDAOTest Tests (Newly Added)
//            Utilities.HelpArticleDAOTest helpArticleDAOTester = new Utilities.HelpArticleDAOTest();
//            helpArticleDAOTester.runTests();

            // 9. Call Group Tests (Newly Added)
            models.GroupTest groupTester = new models.GroupTest();
            groupTester.runTests();

            // 10. Call HelpArticle Tests (Newly Added)
//            models.HelpArticleTest helpArticleTester = new models.HelpArticleTest();
//            helpArticleTester.runTests();

            // 11. Call GroupWithArticles Tests (Newly Added)
            models.GroupWithArticlesTest groupWithArticlesTester = new models.GroupWithArticlesTest();
            groupWithArticlesTester.runTests();

        } catch (Exception e) {
            System.out.println("An error occurred during testing: " + e.getMessage());
            e.printStackTrace();
        }

        System.out.println("All tests completed.");
    }
}
