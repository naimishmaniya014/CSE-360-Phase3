package Controllers;

import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import models.InvitationCode;
import models.Role;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class InviteUserDialog extends Dialog<InvitationCode> {

	/**
     * <p> Title: Invite User Dialog. </p>
     * 
     * <p> Description: This class creates a dialog that allows an admin to generate an invitation code for a new user.
     * The dialog prompts the admin to input roles (comma-separated) and generates an invitation code 
     * that includes those roles. </p>
     * 
     * @author Naimish Maniya
     * 
     * @version 1.00   2024-10-09  Initial version.
     */
	
    private TextField rolesField;

    /**
     * Constructor that initializes the invite user dialog.
     * The admin inputs the roles and generates an invitation code for a new user.
     */
    public InviteUserDialog() {
        setTitle("Invite User");
        setHeaderText("Generate an invitation code for a new user.");

        ButtonType generateButtonType = new ButtonType("Generate", ButtonBar.ButtonData.OK_DONE);
        getDialogPane().getButtonTypes().addAll(generateButtonType, ButtonType.CANCEL);

        rolesField = new TextField();
        rolesField.setPromptText("Roles (comma-separated, e.g., ADMIN,STUDENT)");

        GridPane grid = new GridPane();
        grid.add(new Label("Roles:"), 0, 0);
        grid.add(rolesField, 1, 0);

        getDialogPane().setContent(grid);

        /**
         * Sets the result converter for the dialog to return an InvitationCode
         * object when the "Generate" button is clicked, or null if canceled.
         * 
         * @return An InvitationCode object containing the generated code and the roles, or null if canceled.
         */
        setResultConverter(dialogButton -> {
            if (dialogButton == generateButtonType) {
                String rolesInput = rolesField.getText().trim();
                if (!rolesInput.isEmpty()) {
                    List<Role> roles = new ArrayList<>();
                    for (String roleStr : rolesInput.split(",")) {
                        try {
                            roles.add(Role.valueOf(roleStr.trim().toUpperCase()));
                        } catch (IllegalArgumentException e) {
                        }
                    }
                    if (!roles.isEmpty()) {
                        String code = UUID.randomUUID().toString().substring(0, 8);
                        return new InvitationCode(code, roles);
                    }
                }
            }
            return null;
        });
    }
}
