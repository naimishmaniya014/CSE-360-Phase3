package Controllers;

import Utilities.GroupDAO;
import Utilities.SessionManager;
import Utilities.UserManager;
import models.Group;
import models.Role;
import models.User;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public class SpecialAccessGroupPage {
    private VBox view;
    private TableView<Group> tableView;
    private ObservableList<Group> groupsList;
    private GroupDAO groupDAO;

    private Button backButton;
    private Button addButton;
    private Button editButton;
    private Button deleteButton;
    private Button manageAccessButton;
    private Button refreshButton;

    public SpecialAccessGroupPage() {
        try {
            groupDAO = new GroupDAO();
        } catch (SQLException e) {
            showAlert(Alert.AlertType.ERROR, "Database Error", "Failed to connect to the database.");
            return;
        }

        view = new VBox(10);
        view.setPadding(new Insets(20));

        backButton = new Button("Back");
        backButton.setOnAction(e -> handleBack());

        tableView = new TableView<>();
        groupsList = FXCollections.observableArrayList();
        tableView.setItems(groupsList);

        TableColumn<Group, Long> idCol = new TableColumn<>("ID");
        idCol.setCellValueFactory(data -> new javafx.beans.property.SimpleLongProperty(data.getValue().getId()).asObject());
        idCol.setPrefWidth(50);

        TableColumn<Group, String> nameCol = new TableColumn<>("Group Name");
        nameCol.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue().getName()));
        nameCol.setPrefWidth(200);

        TableColumn<Group, Boolean> specialCol = new TableColumn<>("Special Access");
        specialCol.setCellValueFactory(data -> new javafx.beans.property.SimpleBooleanProperty(data.getValue().isSpecialAccessGroup()));
        specialCol.setPrefWidth(100);

        tableView.getColumns().addAll(idCol, nameCol, specialCol);

        addButton = new Button("Add Special Access Group");
        addButton.setOnAction(e -> showAddGroupDialog());

        editButton = new Button("Edit Special Access Group");
        editButton.setOnAction(e -> showEditGroupDialog());

        deleteButton = new Button("Delete Special Access Group");
        deleteButton.setOnAction(e -> deleteSelectedGroup());

        manageAccessButton = new Button("Manage Access Rights");
        manageAccessButton.setOnAction(e -> manageAccessRights());

        refreshButton = new Button("Refresh");
        refreshButton.setOnAction(e -> loadGroups());

        ToolBar toolBar = new ToolBar(backButton, addButton, editButton, deleteButton, manageAccessButton, refreshButton);

        view.getChildren().addAll(toolBar, tableView);

        loadGroups();
    }

    public VBox getView() {
        return view;
    }

    private void loadGroups() {
        try {
            List<Group> groups = groupDAO.getAllGroups();
            groupsList.setAll(groups.stream().filter(Group::isSpecialAccessGroup).toList());
        } catch (SQLException e) {
            showAlert(Alert.AlertType.ERROR, "Database Error", "Failed to load groups.");
        }
    }

    private void showAddGroupDialog() {
        Dialog<Group> dialog = new Dialog<>();
        dialog.setTitle("Add Special Access Group");
        dialog.setHeaderText("Enter details for the new special access group.");

        ButtonType addButtonType = new ButtonType("Add", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(addButtonType, ButtonType.CANCEL);

        GridPane grid = new GridPane();
        grid.add(new Label("Group Name:"), 0, 0);
        TextField nameField = new TextField();
        grid.add(nameField, 1, 0);

        dialog.getDialogPane().setContent(grid);

        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == addButtonType) {
                String name = nameField.getText().trim();
                if (!name.isEmpty()) {
                    Group group = new Group(name);
                    group.setSpecialAccessGroup(true);
                    return group;
                }
            }
            return null;
        });

        Optional<Group> result = dialog.showAndWait();

        result.ifPresent(group -> {
            try {
                groupDAO.addGroup(group);
                showAlert(Alert.AlertType.INFORMATION, "Success", "Special access group added successfully.");
                loadGroups();
            } catch (SQLException e) {
                showAlert(Alert.AlertType.ERROR, "Database Error", "Failed to add group.");
            }
        });
    }

    private void showEditGroupDialog() {
        Group selected = tableView.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showAlert(Alert.AlertType.WARNING, "No Selection", "Please select a group to edit.");
            return;
        }

        Dialog<Group> dialog = new Dialog<>();
        dialog.setTitle("Edit Special Access Group");
        dialog.setHeaderText("Modify details for the selected group.");

        ButtonType saveButtonType = new ButtonType("Save", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(saveButtonType, ButtonType.CANCEL);

        GridPane grid = new GridPane();
        grid.add(new Label("Group Name:"), 0, 0);
        TextField nameField = new TextField(selected.getName());
        grid.add(nameField, 1, 0);

        dialog.getDialogPane().setContent(grid);

        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == saveButtonType) {
                String name = nameField.getText().trim();
                if (!name.isEmpty()) {
                    selected.setName(name);
                    return selected;
                }
            }
            return null;
        });

        Optional<Group> result = dialog.showAndWait();

        result.ifPresent(group -> {
            try {
                groupDAO.updateGroup(group);
                showAlert(Alert.AlertType.INFORMATION, "Success", "Special access group updated successfully.");
                loadGroups();
            } catch (SQLException e) {
                showAlert(Alert.AlertType.ERROR, "Database Error", "Failed to update group.");
            }
        });
    }

    private void deleteSelectedGroup() {
        Group selected = tableView.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showAlert(Alert.AlertType.WARNING, "No Selection", "Please select a group to delete.");
            return;
        }

        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION, "Are you sure you want to delete the selected group?", ButtonType.YES, ButtonType.NO);
        Optional<ButtonType> result = confirm.showAndWait();

        if (result.isPresent() && result.get() == ButtonType.YES) {
            try {
                groupDAO.deleteGroup(selected.getId());
                showAlert(Alert.AlertType.INFORMATION, "Success", "Group deleted successfully.");
                loadGroups();
            } catch (SQLException e) {
                showAlert(Alert.AlertType.ERROR, "Database Error", "Failed to delete group.");
            }
        }
    }

    private void manageAccessRights() {
        Group selected = tableView.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showAlert(Alert.AlertType.WARNING, "No Selection", "Please select a group to manage access rights.");
            return;
        }

        ManageAccessRightsDialog dialog = new ManageAccessRightsDialog(selected);
        dialog.showAndWait();
    }

    private void handleBack() {
        User currentUser = SessionManager.getInstance().getCurrentUser();
        Role currentRole = SessionManager.getInstance().getCurrentRole();
        Main.showHomePage(currentUser, currentRole);
    }

    private void showAlert(Alert.AlertType type, String title, String content) {
        Alert alert = new Alert(type, content, ButtonType.OK);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.showAndWait();
    }
}
