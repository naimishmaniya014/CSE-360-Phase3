package Controllers;

import javafx.scene.control.TextInputDialog;

public class ResetUserDialog extends TextInputDialog {

    /**
     * <p> Title: Reset User Dialog. </p>
     * 
     * <p> Description: This class extends the `TextInputDialog` to prompt the admin
     * for the username of a user whose password is to be reset. It sets the dialog's
     * title, header, and input prompt for entering the username. </p>
     * 
     * @author Naimish Maniya
     * 
     * @version 1.00   2024-10-09  Initial version.
     */

    /**
     * Constructor that initializes the reset user dialog.
     * The dialog prompts the admin to enter the username of the account whose password will be reset.
     */
    public ResetUserDialog() {
        setTitle("Reset User Password");
        setHeaderText("Reset a user's password.");
        setContentText("Enter the username:");

        getEditor().setText("");
    }
}
