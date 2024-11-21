package Controllers;

import javafx.collections.FXCollections;
import models.*;
import Utilities.*;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import java.time.format.DateTimeFormatter;

import java.util.*;

public class AdminHomePage {

    /**
     * <p> Title: Admin Home Page Controller. </p>
     * 
     * <p> Description: This class manages the Admin Home Page functionalities
     * such as inviting users, resetting passwords, and managing roles. </p>
     * 
     * @author Naimish Maniya
     * 
     * @version 1.00   2024-10-09  Initial version.
     */

	private VBox view;
    private User user;
    private Label welcomeLabel;
    private Button inviteButton;
    private Button resetButton;
    private Button deleteButton;
    private Button listUsersButton;
    private Button manageRolesButton;
    private Button manageArticlesButton;
    private Button manageGroupsButton;
    private Button backupRestoreButton;
    private Button logoutButton;
    private TextArea outputArea;
    private Button manageSpecialAccessGroupsButton;
    
    private static final DateTimeFormatter DISPLAY_FORMATTER = DateTimeFormatter.ofPattern("MMMM d, yyyy h:mm a");

    /**
     * Constructor that sets up the admin home page elements and their actions.
     * 
     * @param user The admin user logged in.
     */
    public AdminHomePage(User user) {
        this.user = user;

        view = new VBox(10);
        view.setPadding(new Insets(20));

        welcomeLabel = new Label("Welcome, " + user.getPreferredName() + " (Admin)");

        inviteButton = new Button("Invite User");
        inviteButton.setOnAction(e -> handleInviteUser());

        resetButton = new Button("Reset User Password");
        resetButton.setOnAction(e -> handleResetUser());

        deleteButton = new Button("Delete User");
        deleteButton.setOnAction(e -> handleDeleteUser());

        listUsersButton = new Button("List Users");
        listUsersButton.setOnAction(e -> handleListUsers());

        manageRolesButton = new Button("Manage User Roles");
        manageRolesButton.setOnAction(e -> handleManageRoles());

        manageArticlesButton = new Button("Manage Help Articles");
        manageArticlesButton.setOnAction(e -> handleManageArticles());

        manageGroupsButton = new Button("Manage Groups");
        manageGroupsButton.setOnAction(e -> handleManageGroups());

        backupRestoreButton = new Button("Backup/Restore");
        backupRestoreButton.setOnAction(e -> handleBackupRestore());
        
        manageSpecialAccessGroupsButton = new Button("Manage Special Access Groups");
        manageSpecialAccessGroupsButton.setOnAction(e -> handleManageSpecialAccessGroups());

        logoutButton = new Button("Log Out");
        logoutButton.setOnAction(e -> {
            SessionManager.getInstance().clearSession();
            Main.showLoginPage();
        });

        outputArea = new TextArea();
        outputArea.setEditable(false);

        view.getChildren().addAll(
                welcomeLabel,
                inviteButton,
                resetButton,
                deleteButton,
                listUsersButton,
                manageRolesButton,
                manageArticlesButton,
                manageGroupsButton,
                backupRestoreButton,
                logoutButton,
                outputArea,
                manageSpecialAccessGroupsButton
        );
    }
    
    private void handleManageSpecialAccessGroups() {
        SpecialAccessGroupPage specialAccessGroupPage = new SpecialAccessGroupPage();
        Scene scene = new Scene(specialAccessGroupPage.getView(), 800, 600);
        Main.getStage().setScene(scene);
    }
    
    /**
     * Returns the view for this page, which is a VBox layout.
     * 
     * @return The VBox layout of the admin home page.
     */
    public VBox getView() {
        return view;
    }
    
    /**
     * Handles inviting a new user and displaying the generated invitation code.
     */
    private void handleInviteUser() {
        InviteUserDialog dialog = new InviteUserDialog();
        Optional<InvitationCode> result = dialog.showAndWait();

        result.ifPresent(code -> {
            UserManager.getInstance().addInvitationCode(code);
            outputArea.appendText("Invitation Code: " + code.getCode() + "\n");
            outputArea.appendText("Roles: " + code.getRoles() + "\n");
        });
    }

    /**
     * Handles resetting a user's password. It opens a dialog where the admin can input the username.
     * If the user is found, their password is reset, and a one-time password (OTP) is generated.
     * The OTP expiration time is displayed in the output area.
     * If the user is not found, an appropriate message is displayed.
     */
    private void handleResetUser() {
    	ResetUserDialog dialog = new ResetUserDialog();
        Optional<String> result = dialog.showAndWait();

        result.ifPresent(username -> {
            UserManager userManager = UserManager.getInstance();
            User targetUser = userManager.getUserByUsername(username);

            if (targetUser != null) {
                userManager.resetPassword(username);
                String formattedExpiry = targetUser.getOtpExpiration().format(DISPLAY_FORMATTER);
                outputArea.appendText("User " + username + " password reset. One-time password: " + targetUser.getOneTimePassword() + "\n");
                outputArea.appendText("OTP expires at: " + formattedExpiry + "\n"); // Use formatted date
            } else {
                outputArea.appendText("User not found.\n");
            }
        });
    }
    
    /**
     * Handles the deletion of a user account by showing a dialog to input the username,
     * then confirms with the admin before proceeding with the deletion.
     */
    private void handleDeleteUser() {
        DeleteUserDialog dialog = new DeleteUserDialog();
        Optional<String> result = dialog.showAndWait();

        result.ifPresent(username -> {
            UserManager userManager = UserManager.getInstance();
            User targetUser = userManager.getUserByUsername(username);

            if (targetUser != null) {
                Alert confirmAlert = new Alert(Alert.AlertType.CONFIRMATION, "Are you sure you want to delete " + username + "?");
                Optional<ButtonType> confirmation = confirmAlert.showAndWait();

                if (confirmation.isPresent() && confirmation.get() == ButtonType.OK) {
                    userManager.removeUser(username);
                    outputArea.appendText("User " + username + " deleted.\n");
                }
            } else {
                outputArea.appendText("User not found.\n");
            }
        });
    }

    /**
     * Lists all users present in the system by fetching them from the UserManager and 
     * displaying their usernames, names, and roles in the output area.
     */
    private void handleListUsers() {
        UserManager userManager = UserManager.getInstance();
        Collection<User> users = userManager.getAllUsers();

        outputArea.clear();
        for (User u : users) {
            outputArea.appendText("Username: " + u.getUsername() + ", Name: " + u.getFirstName() + " " + u.getLastName() + ", Roles: " + u.getRoles() + "\n");
        }
    }

    /**
     * Opens a dialog for managing roles of users in the system, allowing the admin
     * to assign or modify roles.
     */
    private void handleManageRoles() {
        ManageRolesDialog dialog = new ManageRolesDialog();
        dialog.showAndWait();
    }
    
    private void handleManageArticles() {
        HelpArticlePage helpArticlePage = new HelpArticlePage();
        Scene scene = new Scene(helpArticlePage.getView(), 800, 600);
        Main.getStage().setScene(scene);
    }

    /**
     * Handles navigation to the GroupPage.
     */
    private void handleManageGroups() {
        GroupPage groupPage = new GroupPage();
        Scene scene = new Scene(groupPage.getView(), 600, 400);
        Main.getStage().setScene(scene);
    }

    /**
     * Handles navigation to the BackupRestorePage.
     */
    private void handleBackupRestore() {
        BackupRestorePage backupRestorePage = new BackupRestorePage();
        Scene scene = new Scene(backupRestorePage.getView(), 400, 300);
        Main.getStage().setScene(scene);
    }
}
