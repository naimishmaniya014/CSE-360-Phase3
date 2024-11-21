package Controllers;

import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.application.Platform;
import javafx.concurrent.Task;
import models.User;
import Utilities.UserManager;

public class SetNewPasswordPage {

    /**
     * <p> Title: Set New Password Page Controller. </p>
     * 
     * <p> Description: This class handles the page where a user can set a new password after
     * password reset or upon first login. It validates the new password fields and updates
     * the user's password in the system. After successfully setting the password, the user
     * is redirected to the login page. </p>
     * 
     * @author Naimish Maniya
     * 
     * @version 1.00   2024-10-09  Initial version.
     */

    private GridPane view;
    private PasswordField newPasswordField;
    private PasswordField confirmNewPasswordField;
    private Button setPasswordButton;
    private Label messageLabel;
    private User user;

    /**
     * Constructor that initializes the set new password page.
     * The user is passed to this page, allowing them to set a new password.
     * 
     * @param user The user setting the new password.
     */
    public SetNewPasswordPage(User user) {
        this.user = user;

        view = new GridPane();
        view.setPadding(new Insets(20));
        view.setVgap(10);
        view.setHgap(10);

        Label newPasswordLabel = new Label("New Password:");
        newPasswordField = new PasswordField();

        Label confirmNewPasswordLabel = new Label("Confirm New Password:");
        confirmNewPasswordField = new PasswordField();

        setPasswordButton = new Button("Set Password");
        setPasswordButton.setOnAction(e -> handleSetPassword());

        messageLabel = new Label();

        view.add(newPasswordLabel, 0, 0);
        view.add(newPasswordField, 1, 0);
        view.add(confirmNewPasswordLabel, 0, 1);
        view.add(confirmNewPasswordField, 1, 1);
        view.add(setPasswordButton, 1, 2);
        view.add(messageLabel, 0, 3, 2, 1);
    }

    /**
     * Returns the view for the set new password page, which is a GridPane layout.
     * 
     * @return The GridPane layout of the set new password page.
     */
    public GridPane getView() {
        return view;
    }

    /**
     * Handles the logic when the "Set Password" button is clicked. It validates the
     * new password fields, checks if both passwords match, updates the user's password,
     * and resets the user's one-time password (OTP). After successfully updating the password,
     * it redirects the user to the login page.
     */
    private void handleSetPassword() {
        String newPassword = newPasswordField.getText().trim();
        String confirmNewPassword = confirmNewPasswordField.getText().trim();

        if (newPassword.isEmpty() || confirmNewPassword.isEmpty()) {
            messageLabel.setText("All fields are required.");
            return;
        }

        if (!newPassword.equals(confirmNewPassword)) {
            messageLabel.setText("Passwords do not match.");
            return;
        }

        user.setPassword(newPassword);

        UserManager.getInstance().invalidateOtp(user);

        messageLabel.setText("Password updated successfully. Redirecting to login...");

        setPasswordButton.setDisable(true);

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
