package Controllers;

import Utilities.GroupDAO;
import Utilities.UserManager;
import models.Group;
import models.Role;
import models.User;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public class ManageAccessRightsDialog extends Dialog<Void> {
    private Group group;
    private GroupDAO groupDAO;
    private UserManager userManager;

    private ListView<User> adminsListView;
    private ListView<User> instructorViewersListView;
    private ListView<User> instructorAdminsListView;
    private ListView<User> studentViewersListView;

    public ManageAccessRightsDialog(Group group) {
        this.group = group;
        try {
            groupDAO = new GroupDAO();
            userManager = UserManager.getInstance();
        } catch (SQLException e) {
            showAlert(Alert.AlertType.ERROR, "Database Error", "Failed to initialize access rights manager.");
            return;
        }

        setTitle("Manage Access Rights for " + group.getName());
        setHeaderText("Manage admins, instructors, and students for this special access group.");

        ButtonType closeButtonType = new ButtonType("Close", ButtonBar.ButtonData.OK_DONE);
        getDialogPane().getButtonTypes().addAll(closeButtonType);

        adminsListView = new ListView<>();
        instructorViewersListView = new ListView<>();
        instructorAdminsListView = new ListView<>();
        studentViewersListView = new ListView<>();

        populateLists();

        Button addAdminButton = new Button("Add Admin");
        addAdminButton.setOnAction(e -> addAdmin());

        Button removeAdminButton = new Button("Remove Admin");
        removeAdminButton.setOnAction(e -> removeAdmin());

        Button addInstructorViewerButton = new Button("Add Instructor Viewer");
        addInstructorViewerButton.setOnAction(e -> addInstructorViewer());

        Button removeInstructorViewerButton = new Button("Remove Instructor Viewer");
        removeInstructorViewerButton.setOnAction(e -> removeInstructorViewer());

        Button addInstructorAdminButton = new Button("Add Instructor Admin");
        addInstructorAdminButton.setOnAction(e -> addInstructorAdmin());

        Button removeInstructorAdminButton = new Button("Remove Instructor Admin");
        removeInstructorAdminButton.setOnAction(e -> removeInstructorAdmin());

        Button addStudentViewerButton = new Button("Add Student Viewer");
        addStudentViewerButton.setOnAction(e -> addStudentViewer());

        Button removeStudentViewerButton = new Button("Remove Student Viewer");
        removeStudentViewerButton.setOnAction(e -> removeStudentViewer());

        GridPane grid = new GridPane();
        grid.setVgap(10);
        grid.setHgap(10);

        grid.add(new Label("Group Admins:"), 0, 0);
        grid.add(adminsListView, 0, 1);
        grid.add(addAdminButton, 0, 2);
        grid.add(removeAdminButton, 0, 3);

        grid.add(new Label("Instructor Viewers:"), 1, 0);
        grid.add(instructorViewersListView, 1, 1);
        grid.add(addInstructorViewerButton, 1, 2);
        grid.add(removeInstructorViewerButton, 1, 3);

        grid.add(new Label("Instructor Admins:"), 2, 0);
        grid.add(instructorAdminsListView, 2, 1);
        grid.add(addInstructorAdminButton, 2, 2);
        grid.add(removeInstructorAdminButton, 2, 3);

        grid.add(new Label("Student Viewers:"), 3, 0);
        grid.add(studentViewersListView, 3, 1);
        grid.add(addStudentViewerButton, 3, 2);
        grid.add(removeStudentViewerButton, 3, 3);

        getDialogPane().setContent(grid);
    }

    private void populateLists() {
        try {
            List<User> admins = groupDAO.getSpecialGroupAdmins(group.getId());
            List<User> instructorViewers = groupDAO.getSpecialGroupInstructorViewers(group.getId());
            List<User> instructorAdmins = groupDAO.getSpecialGroupInstructorAdmins(group.getId());
            List<User> studentViewers = groupDAO.getSpecialGroupStudentViewers(group.getId());

            adminsListView.setItems(FXCollections.observableArrayList(admins));
            instructorViewersListView.setItems(FXCollections.observableArrayList(instructorViewers));
            instructorAdminsListView.setItems(FXCollections.observableArrayList(instructorAdmins));
            studentViewersListView.setItems(FXCollections.observableArrayList(studentViewers));
        } catch (SQLException e) {
            showAlert(Alert.AlertType.ERROR, "Database Error", "Failed to load access rights.");
        }
    }

    private void addAdmin() {
        Optional<User> userOpt = showUserSelectionDialog();
        userOpt.ifPresent(user -> {
            try {
                groupDAO.addSpecialGroupAdmin(group.getId(), user.getUsername());
                adminsListView.getItems().add(user);
            } catch (SQLException e) {
                showAlert(Alert.AlertType.ERROR, "Database Error", "Failed to add admin.");
            }
        });
    }

    private void removeAdmin() {
        User selected = adminsListView.getSelectionModel().getSelectedItem();
        if (selected != null) {
            try {
                groupDAO.removeSpecialGroupAdmin(group.getId(), selected.getUsername());
                adminsListView.getItems().remove(selected);
            } catch (SQLException e) {
                showAlert(Alert.AlertType.ERROR, "Database Error", "Failed to remove admin.");
            }
        }
    }

    private void addInstructorViewer() {
        Optional<User> userOpt = showUserSelectionDialog(Role.INSTRUCTOR);
        userOpt.ifPresent(user -> {
            try {
                groupDAO.addSpecialGroupInstructorViewer(group.getId(), user.getUsername());
                instructorViewersListView.getItems().add(user);
            } catch (SQLException e) {
                showAlert(Alert.AlertType.ERROR, "Database Error", "Failed to add instructor viewer.");
            }
        });
    }

    private void removeInstructorViewer() {
        User selected = instructorViewersListView.getSelectionModel().getSelectedItem();
        if (selected != null) {
            try {
                groupDAO.removeSpecialGroupInstructorViewer(group.getId(), selected.getUsername());
                instructorViewersListView.getItems().remove(selected);
            } catch (SQLException e) {
                showAlert(Alert.AlertType.ERROR, "Database Error", "Failed to remove instructor viewer.");
            }
        }
    }

    private void addInstructorAdmin() {
        Optional<User> userOpt = showUserSelectionDialog(Role.INSTRUCTOR);
        userOpt.ifPresent(user -> {
            try {
                groupDAO.addSpecialGroupInstructorAdmin(group.getId(), user.getUsername());
                instructorAdminsListView.getItems().add(user);
            } catch (SQLException e) {
                showAlert(Alert.AlertType.ERROR, "Database Error", "Failed to add instructor admin.");
            }
        });
    }

    private void removeInstructorAdmin() {
        User selected = instructorAdminsListView.getSelectionModel().getSelectedItem();
        if (selected != null) {
            try {
                groupDAO.removeSpecialGroupInstructorAdmin(group.getId(), selected.getUsername());
                instructorAdminsListView.getItems().remove(selected);
            } catch (SQLException e) {
                showAlert(Alert.AlertType.ERROR, "Database Error", "Failed to remove instructor admin.");
            }
        }
    }

    private void addStudentViewer() {
        Optional<User> userOpt = showUserSelectionDialog(Role.STUDENT);
        userOpt.ifPresent(user -> {
            try {
                groupDAO.addSpecialGroupStudentViewer(group.getId(), user.getUsername());
                studentViewersListView.getItems().add(user);
            } catch (SQLException e) {
                showAlert(Alert.AlertType.ERROR, "Database Error", "Failed to add student viewer.");
            }
        });
    }

    private void removeStudentViewer() {
        User selected = studentViewersListView.getSelectionModel().getSelectedItem();
        if (selected != null) {
            try {
                groupDAO.removeSpecialGroupStudentViewer(group.getId(), selected.getUsername());
                studentViewersListView.getItems().remove(selected);
            } catch (SQLException e) {
                showAlert(Alert.AlertType.ERROR, "Database Error", "Failed to remove student viewer.");
            }
        }
    }

    private Optional<User> showUserSelectionDialog() {
        return showUserSelectionDialog(null);
    }

    private Optional<User> showUserSelectionDialog(Role roleFilter) {
        List<User> users = userManager.getAllUsers().stream()
                .filter(user -> roleFilter == null || user.getRoles().contains(roleFilter))
                .toList();
        ChoiceDialog<User> dialog = new ChoiceDialog<>(null, users);
        dialog.setTitle("Select User");
        dialog.setHeaderText("Select a user to modify access rights.");
        dialog.setContentText("User:");
        return dialog.showAndWait();
    }

    private void showAlert(Alert.AlertType type, String title, String content) {
        Alert alert = new Alert(type, content, ButtonType.OK);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.showAndWait();
    }
}
