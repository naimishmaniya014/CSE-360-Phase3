package Controllers;

import javafx.scene.control.TextInputDialog;

public class DeleteUserDialog extends TextInputDialog {

	/**
     * <p> Title: Delete User Dialog. </p>
     * 
     * <p> Description: This class extends the TextInputDialog to prompt the admin for the
     * username of the user whose account is to be deleted. It sets the dialog title,
     * header text, and input prompt for entering the username. </p>
     * 
     * @author Naimish Maniya
     * 
     * @version 1.00   2024-10-09  Initial version.
     */

    /**
     * Constructor that configures the dialog for deleting a user.
     * The dialog prompts the admin to enter the username of the account to be deleted.
     */
	
    public DeleteUserDialog() {
        setTitle("Delete User");
        setHeaderText("Delete a user account.");
        setContentText("Enter the username:");

        getEditor().setText("");
    }
}
