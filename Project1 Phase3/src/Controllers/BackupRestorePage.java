package Controllers;

import Utilities.BackupRestoreManager;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import models.Role;
import models.User;
import Utilities.SessionManager;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

/**
 * <p> Title: BackupRestorePage Class </p>
 * 
 * <p> Description: This class represents the backup and restore interface within the application. 
 * It provides functionalities to backup all groups, backup selected groups, and restore groups from 
 * backup files. The class interacts with the {@link BackupRestoreManager} to perform these operations 
 * and manages user interactions through various buttons and dialogs. </p>
 * 
 * <p> Usage: Integrate this page into the application's navigation system. Users can perform backup 
 * and restore operations by interacting with the provided buttons, which trigger corresponding methods 
 * to handle the actions. </p>
 * 
 * @author Naimish Maniya
 * 
 * <p> @version 1.00  2024-10-29  Initial version. </p>
 */
public class BackupRestorePage {

    private VBox view;
    private BackupRestoreManager backupRestoreManager;

    private Button backButton;
    private Button backupAllButton;
    private Button backupByGroupButton;
    private Button restoreButton;

    /**
     * Constructs the BackupRestorePage and initializes its UI components.
     * Sets up event handlers for backup and restore operations.
     */
    public BackupRestorePage() {
        try {
            backupRestoreManager = new BackupRestoreManager();
        } catch (SQLException e) {
            showAlert(Alert.AlertType.ERROR, "Database Error", "Failed to initialize backup/restore manager.");
            return;
        }

        view = new VBox(10);
        view.setPadding(new Insets(20));

        backButton = new Button("Back");
        backButton.setOnAction(e -> handleBack());

        backupAllButton = new Button("Backup All Groups");
        backupAllButton.setOnAction(e -> handleBackupAllGroups());

        backupByGroupButton = new Button("Backup Selected Groups");
        backupByGroupButton.setOnAction(e -> {
            try {
                handleBackupSelectedGroups();
            } catch (SQLException e1) {
                e1.printStackTrace();
                showAlert(Alert.AlertType.ERROR, "Backup Error", "An error occurred during backup.");
            }
        });

        restoreButton = new Button("Restore Groups");
        restoreButton.setOnAction(e -> handleRestoreGroups());

        ToolBar toolBar = new ToolBar(backButton, backupAllButton, backupByGroupButton, restoreButton);

        view.getChildren().addAll(toolBar);
    }

    /**
     * Retrieves the main layout of the BackupRestorePage.
     *
     * @return The {@link VBox} containing all UI components of the page.
     */
    public VBox getView() {
        return view;
    }

    /**
     * Handles backing up all groups along with their associated articles.
     * Prompts the user to select a destination file and performs the backup.
     */
    private void handleBackupAllGroups() {
        FileChooserDialog fileDialog = new FileChooserDialog("Backup All Groups with Articles", "*.bak");
        Optional<String> result = fileDialog.showSaveDialog();
        result.ifPresent(filePath -> {
            try {
                backupRestoreManager.backupAllGroups(filePath);
                showAlert(Alert.AlertType.INFORMATION, "Success", "All groups and their articles backed up successfully.");
            } catch (IOException | SQLException e) {
                showAlert(Alert.AlertType.ERROR, "Backup Error", "Failed to backup all groups and articles.");
            }
        });
    }

    /**
     * Handles backing up selected groups along with their associated articles.
     * Prompts the user to select groups and a destination file before performing the backup.
     *
     * @throws SQLException If there is an error accessing the database.
     */
    private void handleBackupSelectedGroups() throws SQLException {
        // Fetch all groups
        GroupSelectionDialog groupDialog = new GroupSelectionDialog();
        Optional<List<String>> result = groupDialog.showAndWait();

        result.ifPresent(groups -> {
            if (groups.isEmpty()) {
                showAlert(Alert.AlertType.WARNING, "No Groups Selected", "Please select at least one group.");
                return;
            }

            FileChooserDialog fileDialog = new FileChooserDialog("Backup Selected Groups with Articles", "*.bak");
            Optional<String> filePathOpt = fileDialog.showSaveDialog();
            filePathOpt.ifPresent(filePath -> {
                try {
                    backupRestoreManager.backupGroups(groups, filePath);
                    showAlert(Alert.AlertType.INFORMATION, "Success", "Selected groups and their articles backed up successfully.");
                } catch (IOException | SQLException e) {
                    showAlert(Alert.AlertType.ERROR, "Backup Error", "Failed to backup selected groups and articles.");
                }
            });
        });
    }

    /**
     * Handles restoring groups and their articles from a backup file.
     * Prompts the user to select a backup file and choose restore options before performing the restore.
     */
    private void handleRestoreGroups() {
        FileChooserDialog fileDialog = new FileChooserDialog("Select Backup File", "*.bak");
        Optional<String> filePathOpt = fileDialog.showOpenDialog();
        filePathOpt.ifPresent(filePath -> {
            // Ask whether to remove existing groups and articles before restoring
            ChoiceDialog<String> choiceDialog = new ChoiceDialog<>("Merge", "Merge", "Remove All");
            choiceDialog.setTitle("Restore Options");
            choiceDialog.setHeaderText("Choose how to restore the groups and articles:");
            choiceDialog.setContentText("Select an option:");

            Optional<String> choice = choiceDialog.showAndWait();
            if (choice.isPresent()) {
                boolean removeExisting = choice.get().equals("Remove All");
                try {
                    backupRestoreManager.restoreGroups(filePath, removeExisting);
                    showAlert(Alert.AlertType.INFORMATION, "Success", "Groups and articles restored successfully.");
                } catch (IOException | SQLException | ClassNotFoundException e) {
                    showAlert(Alert.AlertType.ERROR, "Restore Error", "Failed to restore groups and articles.");
                }
            }
        });
    }

    /**
     * Handles navigation back to the home page based on the current user's role.
     */
    private void handleBack() {
        User currentUser = SessionManager.getInstance().getCurrentUser();
        Role currentRole = SessionManager.getInstance().getCurrentRole();
        Main.showHomePage(currentUser, currentRole);
    }

    /**
     * Displays an alert dialog with the specified parameters.
     *
     * @param type    The type of alert (e.g., INFORMATION, ERROR).
     * @param title   The title of the alert dialog.
     * @param content The content message of the alert.
     */
    private void showAlert(Alert.AlertType type, String title, String content) {
        Alert alert = new Alert(type, content, ButtonType.OK);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.showAndWait();
    }
}
