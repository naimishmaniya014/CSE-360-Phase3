package Controllers;

import models.*;
import Utilities.*;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;

public class AccountSetupPage {

    /**
     * <p> Title: Account Setup Page Controller. </p>
     * 
     * <p> Description: This class handles the Account Setup page where a user inputs
     * their personal details for initial setup. It validates the input and
     * completes the account setup. </p>
     * 
     * @author Naimish Maniya
     * 
     * @version 1.00   2024-10-09  Initial version.
     */

    private GridPane view;
    private TextField emailField;
    private TextField firstNameField;
    private TextField middleNameField;
    private TextField lastNameField;
    private TextField preferredNameField;
    private Button finishButton;
    private Label messageLabel;
    private User user;

    /**
     * Constructor that sets up the form elements for the account setup page.
     * 
     * @param user The user object for whom the account setup is being performed.
     */
    public AccountSetupPage(User user) {
        this.user = user;

        view = new GridPane();
        view.setPadding(new Insets(20));
        view.setVgap(10);
        view.setHgap(10);

        emailField = new TextField();
        firstNameField = new TextField();
        middleNameField = new TextField();
        lastNameField = new TextField();
        preferredNameField = new TextField();

        finishButton = new Button("Finish Setup");
        finishButton.setOnAction(e -> handleFinishSetup());

        messageLabel = new Label();

        view.add(new Label("Email:"), 0, 0);
        view.add(emailField, 1, 0);
        view.add(new Label("First Name:"), 0, 1);
        view.add(firstNameField, 1, 1);
        view.add(new Label("Middle Name:"), 0, 2);
        view.add(middleNameField, 1, 2);
        view.add(new Label("Last Name:"), 0, 3);
        view.add(lastNameField, 1, 3);
        view.add(new Label("Preferred First Name (optional):"), 0, 4);
        view.add(preferredNameField, 1, 4);
        view.add(finishButton, 1, 5);
        view.add(messageLabel, 0, 6, 2, 1);
    }

    /**
     * Returns the view for this page, which is a GridPane layout.
     * 
     * @return The GridPane layout of the account setup page.
     */
    public GridPane getView() {
        return view;
    }

    /**
     * Handles the logic when the "Finish Setup" button is clicked. 
     * This includes validating input fields and saving the user information.
     */
    private void handleFinishSetup() {
        String email = emailField.getText().trim();
        String firstName = firstNameField.getText().trim();
        String middleName = middleNameField.getText().trim();
        String lastName = lastNameField.getText().trim();
        String preferredName = preferredNameField.getText().trim();

        if (email.isEmpty() || firstName.isEmpty() || lastName.isEmpty()) {
            messageLabel.setText("Email, First Name, and Last Name are required.");
            return;
        }

        user.setEmail(email);
        user.setFirstName(firstName);
        user.setMiddleName(middleName);
        user.setLastName(lastName);
        user.setPreferredName(preferredName.isEmpty() ? firstName : preferredName);
        user.setFirstLogin(false);

        UserManager.getInstance().addUser(user);
        SessionManager.getInstance().setCurrentUser(user);

        if (user.getRoles().size() > 1) {
            Main.showRoleSelectionPage(user);
        } else {
            Main.showHomePage(user, user.getRoles().get(0));
        }
    }
}
