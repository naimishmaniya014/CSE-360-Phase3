package Controllers;

import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import models.*;
import Utilities.*;
import javafx.concurrent.Task;

public class CreateUserPage {

    /**
     * <p> Title: Create User Page Controller. </p>
     * 
     * <p> Description: This class manages the user account creation page. It validates the user's
     * input, such as username and password, and handles the creation of a new user account
     * using an invitation code. The page also handles redirecting the user to the login
     * page after account creation. </p>
     * 
     * @author Naimish Maniya
     * 
     * @version 1.00   2024-10-09  Initial version.
     */

    private GridPane view;
    private TextField usernameField;
    private PasswordField passwordField;
    private PasswordField confirmPasswordField;
    private Button createButton;
    private Label messageLabel;
    private InvitationCode invitation;

    /**
     * Constructor that initializes the create user page.
     * The invitation code is passed to assign roles to the newly created user.
     * 
     * @param invitation The invitation code used to create a new user with predefined roles.
     */
    public CreateUserPage(InvitationCode invitation) {
        this.invitation = invitation;

        view = new GridPane();
        view.setPadding(new Insets(20));
        view.setVgap(10);
        view.setHgap(10);

        Label usernameLabel = new Label("Username:");
        usernameField = new TextField();

        Label passwordLabel = new Label("Password:");
        passwordField = new PasswordField();

        Label confirmPasswordLabel = new Label("Confirm Password:");
        confirmPasswordField = new PasswordField();

        createButton = new Button("Create Account");
        createButton.setOnAction(e -> handleCreateAccount());

        messageLabel = new Label();

        view.add(usernameLabel, 0, 0);
        view.add(usernameField, 1, 0);
        view.add(passwordLabel, 0, 1);
        view.add(passwordField, 1, 1);
        view.add(confirmPasswordLabel, 0, 2);
        view.add(confirmPasswordField, 1, 2);
        view.add(createButton, 1, 3);
        view.add(messageLabel, 0, 4, 2, 1);
    }

    /**
     * Returns the view for the create user page, which is a GridPane layout.
     * 
     * @return The GridPane layout of the create user page.
     */
    public GridPane getView() {
        return view;
    }

    /**
     * Handles the account creation process. Validates the input fields, checks if the username
     * is already taken, and ensures the passwords match. After successfully creating the account,
     * the system redirects the user to the login page after a brief delay.
     */
    private void handleCreateAccount() {
        String username = usernameField.getText().trim();
        String password = passwordField.getText().trim();
        String confirmPassword = confirmPasswordField.getText().trim();

        if (username.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
            messageLabel.setText("All fields are required.");
            return;
        }

        if (!password.equals(confirmPassword)) {
            messageLabel.setText("Passwords do not match.");
            return;
        }

        UserManager userManager = UserManager.getInstance();

        if (userManager.isUsernameTaken(username)) {
            messageLabel.setText("Username is already taken.");
            return;
        }

        User newUser = new User(username, password);
        newUser.setFirstLogin(true);
        newUser.setRoles(invitation.getRoles());

        userManager.addUser(newUser);
        invitation.setUsed(true);
        userManager.removeInvitationCode(invitation.getCode());

        messageLabel.setText("Account created successfully. Redirecting to login...");

        createButton.setDisable(true);

        Task<Void> redirectTask = new Task<Void>() {
            @Override
            protected Void call() throws Exception {
                Thread.sleep(3000);
                return null;
            }

            @Override
            protected void succeeded() {
                Platform.runLater(() -> Main.showLoginPage());
            }
        };
        new Thread(redirectTask).start();
    }
}
