package Controllers;

import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.stage.Stage;
import models.*;

import java.time.LocalDateTime;
import java.util.Optional;
import java.time.format.DateTimeFormatter;

import Utilities.*;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;

public class LoginPage {

	 /**
     * <p> Title: Login Page Controller. </p>
     * 
     * <p> Description: This class manages the login page where users can log in with their credentials
     * or use an invitation code to create a new account. </p>
     * 
     * @author Naimish Maniya
     * 
     * @version 1.00   2024-10-09  Initial version.
     */
	
    private GridPane view;
    private TextField usernameField;
    private PasswordField passwordField;
    private TextField invitationCodeField;
    private Button loginButton;
    private Button useInvitationButton;
    private Label messageLabel;
    
    private static final DateTimeFormatter DISPLAY_FORMATTER = DateTimeFormatter.ofPattern("MMMM d, yyyy h:mm a");

    /**
     * Constructor that initializes the login page interface, including input fields for
     * username, password, and invitation codes, along with action buttons.
     */
    public LoginPage() {
        view = new GridPane();
        view.setPadding(new Insets(20));
        view.setVgap(10);
        view.setHgap(10);

        Label usernameLabel = new Label("Username:");
        usernameField = new TextField();

        Label passwordLabel = new Label("Password:");
        passwordField = new PasswordField();

        loginButton = new Button("Login");
        loginButton.setOnAction(e -> handleLogin());

        Label invitationLabel = new Label("Invitation Code:");
        invitationCodeField = new TextField();

        useInvitationButton = new Button("Use Code");
        useInvitationButton.setOnAction(e -> handleInvitationCode());

        messageLabel = new Label();

        view.add(usernameLabel, 0, 0);
        view.add(usernameField, 1, 0);
        view.add(passwordLabel, 0, 1);
        view.add(passwordField, 1, 1);
        view.add(loginButton, 1, 2);
        view.add(new Separator(), 0, 3, 2, 1);
        view.add(invitationLabel, 0, 4);
        view.add(invitationCodeField, 1, 4);
        view.add(useInvitationButton, 1, 5);
        view.add(messageLabel, 0, 6, 2, 1);
    }

    /**
     * Returns the view (a GridPane layout) for the login page.
     * 
     * @return The GridPane layout of the login page.
     */
    public GridPane getView() {
        return view;
    }
    
    /**
     * Handles the login functionality. If the user list is empty, it triggers the creation
     * of the first admin user. Otherwise, it validates the entered username and password 
     * and proceeds to the next page based on the user's login status and roles.
     */
    private void handleLogin() {
        UserManager userManager = UserManager.getInstance();

        // Check if the user list is empty
        if (userManager.getAllUsers().isEmpty()) {
            handleFirstAdminCreation();
            return;
        }

        String username = usernameField.getText().trim();
        String password = passwordField.getText().trim();

        User user = userManager.authenticate(username, password);

        if (user != null) {
            SessionManager.getInstance().setCurrentUser(user);

            if (user.isResetRequired()) {
                SetNewPasswordPage setNewPasswordPage = new SetNewPasswordPage(user);
                Scene scene = new Scene(setNewPasswordPage.getView(), 400, 300);
                Main.getStage().setScene(scene);
            } else if (user.isFirstLogin()) {
                Main.showAccountSetupPage(user);
            } else if (user.getRoles().size() > 1) {
                Main.showRoleSelectionPage(user);
            } else {
                Role singleRole = user.getRoles().get(0);
                SessionManager.getInstance().setCurrentRole(singleRole);
                Main.showHomePage(user, singleRole);
            }
        } else {
        	User existingUser = userManager.getUserByUsername(username);
            if (existingUser != null && existingUser.isResetRequired()) {
                LocalDateTime now = LocalDateTime.now();
                if (existingUser.getOtpExpiration() != null && now.isAfter(existingUser.getOtpExpiration())) {
                    String formattedExpiry = existingUser.getOtpExpiration().format(DISPLAY_FORMATTER);
                    messageLabel.setText("One-time password has expired at " + formattedExpiry + ". Please contact an administrator.");
                } else {
                    messageLabel.setText("Invalid one-time password.");
                }
            } else {
                messageLabel.setText("Invalid username or password.");
            }
        }
    }

    /**
     * Handles the use of an invitation code to create a new account. If the invitation
     * code is valid and unused, it proceeds to the account creation page.
     */
    private void handleInvitationCode() {
        String code = invitationCodeField.getText().trim();
        UserManager userManager = UserManager.getInstance();
        InvitationCode invitation = userManager.getInvitationCode(code);

        if (invitation != null && !invitation.isUsed()) {
            CreateUserPage createUserPage = new CreateUserPage(invitation);
            Scene scene = new Scene(createUserPage.getView(), 400, 400);
            Main.getStage().setScene(scene);
        } else {
            messageLabel.setText("Invalid or used invitation code.");
        }
    }

    /**
     * Handles the creation of the first admin user when the user list is empty.
     * Prompts the admin to confirm the password and adds the user with admin role.
     */
    private void handleFirstAdminCreation() {
        String username = usernameField.getText().trim();
        String password = passwordField.getText().trim();

        if (username.isEmpty() || password.isEmpty()) {
            messageLabel.setText("Username and password cannot be empty.");
            return;
        }
        Dialog<String> dialog = new TextInputDialog();
        dialog.setTitle("Confirm Password");
        dialog.setHeaderText("Enter the password again for confirmation:");
        Optional<String> result = dialog.showAndWait();

        if (result.isPresent() && result.get().equals(password)) {
            UserManager userManager = UserManager.getInstance();
            User adminUser = new User(username, password);
            adminUser.addRole(Role.ADMIN); 
            userManager.addUser(adminUser);

            messageLabel.setText("Admin account created. Please log in.");
        } else {
            messageLabel.setText("Passwords do not match. Try again.");
        }
    }
}
