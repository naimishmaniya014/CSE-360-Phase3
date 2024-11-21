package Controllers;

import javafx.geometry.Insets;
import models.*;
import Utilities.*;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;

public class RoleSelectionPage {

    /**
     * <p> Title: Role Selection Page Controller. </p>
     * 
     * <p> Description: This class handles the role selection page where a user with multiple roles
     * can select which role to use for the current session. The selected role determines
     * the functionalities available during the session. </p>
     * 
     * @author Naimish Maniya
     * 
     * @version 1.00   2024-10-09  Initial version.
     */

    private GridPane view;
    private ComboBox<Role> roleComboBox;
    private Button proceedButton;
    private Label messageLabel;
    private User user;

    /**
     * Constructor that initializes the role selection page for a user with multiple roles.
     * The roles are presented in a ComboBox for the user to select from.
     * 
     * @param user The user who is selecting their role for the session.
     */
    public RoleSelectionPage(User user) {
        this.user = user;

        view = new GridPane();
        view.setPadding(new Insets(20));
        view.setVgap(10);
        view.setHgap(10);

        roleComboBox = new ComboBox<>();
        roleComboBox.getItems().addAll(user.getRoles());

        proceedButton = new Button("Proceed");
        proceedButton.setOnAction(e -> handleProceed());

        messageLabel = new Label();

        view.add(new Label("Select Role for this session:"), 0, 0);
        view.add(roleComboBox, 1, 0);
        view.add(proceedButton, 1, 1);
        view.add(messageLabel, 0, 2, 2, 1);
    }

    /**
     * Returns the view for this page, which is a GridPane layout.
     * 
     * @return The GridPane layout of the role selection page.
     */
    public GridPane getView() {
        return view;
    }

    /**
     * Handles the proceed action when the user selects a role and clicks "Proceed."
     * If no role is selected, an error message is displayed. Otherwise, the selected
     * role is set for the current session and the corresponding home page is displayed.
     */
    private void handleProceed() {
        Role selectedRole = roleComboBox.getValue();
        if (selectedRole == null) {
            messageLabel.setText("Please select a role.");
            return;
        }
        SessionManager.getInstance().setCurrentRole(selectedRole);
        Main.showHomePage(user, selectedRole);
    }
}
