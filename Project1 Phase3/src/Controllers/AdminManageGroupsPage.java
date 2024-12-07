package Controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import models.*;
import Utilities.*;
import javafx.util.Pair;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public class AdminManageGroupsPage {

	private VBox view;
    private GroupDAO groupDAO;
    private HelpArticleDAO helpArticleDAO;
    private TableView<Group> groupsTableView;
    private ObservableList<Group> groupsList;
    private Button assignArticleButton;
    private Button createGroupButton;
    private Button editGroupButton;
    private Button deleteGroupButton;

    public AdminManageGroupsPage() {
        view = new VBox(10);
        view.setPadding(new Insets(20));

        groupsList = FXCollections.observableArrayList();
        groupsTableView = new TableView<>();
        groupsTableView.setItems(groupsList);

        TableColumn<Group, Long> idCol = new TableColumn<>("ID");
        idCol.setCellValueFactory(data -> new javafx.beans.property.SimpleLongProperty(data.getValue().getId()).asObject());
        idCol.setPrefWidth(50);

        TableColumn<Group, String> nameCol = new TableColumn<>("Name");
        nameCol.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue().getName()));
        nameCol.setPrefWidth(200);

        TableColumn<Group, Boolean> specialCol = new TableColumn<>("Special Access");
        specialCol.setCellValueFactory(data -> new javafx.beans.property.SimpleBooleanProperty(data.getValue().isSpecialAccessGroup()).asObject());
        specialCol.setPrefWidth(100);

        groupsTableView.getColumns().addAll(idCol, nameCol, specialCol);

        HBox groupButtons = new HBox(10);
        createGroupButton = new Button("Create Group");
        createGroupButton.setOnAction(e -> handleCreateGroup());
        editGroupButton = new Button("Edit Group");
        editGroupButton.setOnAction(e -> handleEditGroup());
        deleteGroupButton = new Button("Delete Group");
        deleteGroupButton.setOnAction(e -> handleDeleteGroup());
        assignArticleButton = new Button("Assign Article");
        assignArticleButton.setOnAction(e -> handleAssignArticle());
        groupButtons.getChildren().addAll(createGroupButton, editGroupButton, deleteGroupButton, assignArticleButton);

        view.getChildren().addAll(groupsTableView, groupButtons);

        try {
            groupDAO = new GroupDAO();
            helpArticleDAO = new HelpArticleDAO();
        } catch (SQLException e) {
            showAlert(Alert.AlertType.ERROR, "Database Error", "Failed to load data.");
            return;
        }

        loadGroups();
    }

    private void handleAssignArticle() {
        Group selectedGroup = groupsTableView.getSelectionModel().getSelectedItem();
        if (selectedGroup == null) {
            showAlert(Alert.AlertType.WARNING, "No Selection", "Please select a group first.");
            return;
        }

        AssignArticleToGroupDialog assignDialog = new AssignArticleToGroupDialog(groupDAO, helpArticleDAO, selectedGroup.getId());
        Optional<Void> result = assignDialog.showAndWait();
        if (result.isPresent()) {
            try {
                HelpArticle selectedArticle = assignDialog.getSelectedArticle();
                if (selectedArticle != null) {
                    helpArticleDAO.associateArticleWithGroup(selectedArticle.getId(), selectedGroup.getId());
                    showAlert(Alert.AlertType.INFORMATION, "Success", "Article assigned to group successfully.");
                }
            } catch (SQLException e) {
                showAlert(Alert.AlertType.ERROR, "Database Error", "Failed to assign article to group.");
            }
        }
    }

    private void handleCreateGroup() {
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Create Group");
        dialog.setHeaderText("Enter the name of the new group:");
        dialog.setContentText("Group Name:");

        Optional<String> result = dialog.showAndWait();
        result.ifPresent(groupName -> {
            if (groupName.trim().isEmpty()) {
                showAlert(Alert.AlertType.WARNING, "Input Required", "Group name cannot be empty.");
                return;
            }

            ChoiceDialog<String> accessDialog = new ChoiceDialog<>("No", "Yes", "No");
            accessDialog.setTitle("Group Access Type");
            accessDialog.setHeaderText("Is this a Special Access Group?");
            accessDialog.setContentText("Select Yes or No:");

            Optional<String> accessResult = accessDialog.showAndWait();
            accessResult.ifPresent(access -> {
                boolean isSpecial = access.equalsIgnoreCase("Yes");
                try {
                    groupDAO.createGroup(groupName, isSpecial);
                    showAlert(Alert.AlertType.INFORMATION, "Success", "Group created successfully.");
                    loadGroups();
                } catch (SQLException e) {
                    showAlert(Alert.AlertType.ERROR, "Database Error", "Failed to create group.");
                }
            });
        });
    }

    private void handleEditGroup() {
        Group selectedGroup = groupsTableView.getSelectionModel().getSelectedItem();
        if (selectedGroup == null) {
            showAlert(Alert.AlertType.WARNING, "No Selection", "Please select a group to edit.");
            return;
        }

        TextInputDialog nameDialog = new TextInputDialog(selectedGroup.getName());
        nameDialog.setTitle("Edit Group");
        nameDialog.setHeaderText("Edit the name of the group:");
        nameDialog.setContentText("Group Name:");

        Optional<String> nameResult = nameDialog.showAndWait();
        nameResult.ifPresent(newName -> {
            if (newName.trim().isEmpty()) {
                showAlert(Alert.AlertType.WARNING, "Input Required", "Group name cannot be empty.");
                return;
            }

            ChoiceDialog<String> accessDialog = new ChoiceDialog<>(selectedGroup.isSpecialAccessGroup() ? "Yes" : "No", "Yes", "No");
            accessDialog.setTitle("Group Access Type");
            accessDialog.setHeaderText("Is this a Special Access Group?");
            accessDialog.setContentText("Select Yes or No:");

            Optional<String> accessResult = accessDialog.showAndWait();
            accessResult.ifPresent(access -> {
                boolean isSpecial = access.equalsIgnoreCase("Yes");
                try {
                    groupDAO.updateGroup(selectedGroup.getId(), newName, isSpecial);
                    showAlert(Alert.AlertType.INFORMATION, "Success", "Group updated successfully.");
                    loadGroups();
                } catch (SQLException e) {
                    showAlert(Alert.AlertType.ERROR, "Database Error", "Failed to update group.");
                }
            });
        });
    }

    private void handleDeleteGroup() {
        Group selectedGroup = groupsTableView.getSelectionModel().getSelectedItem();
        if (selectedGroup == null) {
            showAlert(Alert.AlertType.WARNING, "No Selection", "Please select a group to delete.");
            return;
        }

        Alert confirmationAlert = new Alert(Alert.AlertType.CONFIRMATION, 
            "Are you sure you want to delete the group: " + selectedGroup.getName() + "?", 
            ButtonType.YES, ButtonType.NO);
        confirmationAlert.setTitle("Confirm Deletion");
        confirmationAlert.setHeaderText(null);
        confirmationAlert.showAndWait().ifPresent(response -> {
            if (response == ButtonType.YES) {
                try {
                    groupDAO.deleteGroup(selectedGroup.getId());
                    showAlert(Alert.AlertType.INFORMATION, "Deleted", "Group deleted successfully.");
                    loadGroups();
                } catch (SQLException e) {
                    showAlert(Alert.AlertType.ERROR, "Deletion Failed", "Failed to delete the group.");
                }
            }
        });
    }

    private void loadGroups() {
        try {
            List<Group> currentGroups = groupDAO.getAllGroups();
            groupsList.setAll(currentGroups);
        } catch (SQLException e) {
            showAlert(Alert.AlertType.ERROR, "Database Error", "Failed to load groups.");
        }
    }

    private void showAlert(Alert.AlertType type, String title, String content) {
        Alert alert = new Alert(type, content, ButtonType.OK);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.showAndWait();
    }

    public VBox getView() {
        return view;
    }
}
