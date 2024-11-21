package Controllers;

import Utilities.GroupDAO;
import Utilities.HelpArticleDAO;
import Utilities.SessionManager;
import models.Group;
import models.HelpArticle;
import models.Role;
import models.User;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

/**
 * <p> Title: GroupPage Class </p>
 * 
 * <p> Description: This class represents the user interface for managing groups within the application.
 * It provides functionalities to add, edit, delete, and assign articles to groups. The page displays
 * a table of existing groups and a list of articles associated with the selected group. Users can
 * interact with the table and list to perform various operations related to group management. </p>
 * 
 * @author Naimish Maniya
 * 
 * <p> @version 1.00  2024-10-29  Initial version. </p>
 */
public class GroupPage {

    private VBox view;
    private TableView<Group> tableView;
    private ObservableList<Group> groupsList;
    private GroupDAO groupDAO;
    private HelpArticleDAO helpArticleDAO;

    private Button backButton;
    private Button addButton;
    private Button editButton;
    private Button deleteButton;
    private Button refreshButton;
    private Button assignArticlesButton;
    private Button removeArticlesButton; 
    private ListView<HelpArticle> articlesListView; 

    /**
     * Constructs a GroupPage instance.
     * Initializes UI components, sets up event handlers, and loads existing groups from the database.
     */
    public GroupPage() {
        try {
            groupDAO = new GroupDAO();
            helpArticleDAO = new HelpArticleDAO();
        } catch (SQLException e) {
            showAlert(Alert.AlertType.ERROR, "Database Error", "Failed to connect to the database.");
            return;
        }

        view = new VBox(10);
        view.setPadding(new Insets(20));

        view.setPrefWidth(800);
        view.setPrefHeight(600);

        // Initialize Back Button
        backButton = new Button("Back");
        backButton.setOnAction(e -> handleBack());

        tableView = new TableView<>();
        groupsList = FXCollections.observableArrayList();
        tableView.setItems(groupsList);

        // Define table columns
        TableColumn<Group, Long> idCol = new TableColumn<>("ID");
        idCol.setCellValueFactory(data -> new javafx.beans.property.SimpleLongProperty(data.getValue().getId()).asObject());
        idCol.setPrefWidth(50);

        TableColumn<Group, String> nameCol = new TableColumn<>("Group Name");
        nameCol.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue().getName()));
        nameCol.setPrefWidth(200);

        tableView.getColumns().addAll(idCol, nameCol);

        // Buttons
        addButton = new Button("Add Group");
        addButton.setOnAction(e -> showAddGroupDialog());

        editButton = new Button("Edit Group");
        editButton.setOnAction(e -> showEditGroupDialog());

        deleteButton = new Button("Delete Group");
        deleteButton.setOnAction(e -> deleteSelectedGroup());

        refreshButton = new Button("Refresh");
        refreshButton.setOnAction(e -> loadGroups());

        assignArticlesButton = new Button("Assign Articles");
        assignArticlesButton.setOnAction(e -> assignArticlesToGroup());

        removeArticlesButton = new Button("Remove Articles");
        removeArticlesButton.setOnAction(e -> removeArticlesFromGroup());

        ToolBar toolBar = new ToolBar(backButton, addButton, editButton, deleteButton, refreshButton, assignArticlesButton, removeArticlesButton);

        articlesListView = new ListView<>();
        articlesListView.setPrefHeight(200);
        articlesListView.setPlaceholder(new Label("No articles assigned to this group."));
        articlesListView.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE); 

        Label articlesLabel = new Label("Articles in Selected Group:");

        VBox articlesBox = new VBox(5, articlesLabel, articlesListView);
        articlesBox.setPadding(new Insets(10, 0, 0, 0));
        articlesBox.setPrefWidth(600);   
        articlesBox.setPrefHeight(300);  

        view.getChildren().addAll(toolBar, tableView, articlesBox);

        tableView.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                loadArticlesForGroup(newSelection.getId());
            } else {
                articlesListView.getItems().clear();
            }
        });

        loadGroups();
    }

    /**
     * Retrieves the main layout of the GroupPage.
     *
     * @return The {@link VBox} containing all UI components of the page.
     */
    public VBox getView() {
        return view;
    }

    /**
     * Loads all groups from the database into the table view.
     * Fetches the list of groups and updates the observable list.
     */
    private void loadGroups() {
        try {
            List<Group> groups = groupDAO.getAllGroups();
            groupsList.setAll(groups);
        } catch (SQLException e) {
            showAlert(Alert.AlertType.ERROR, "Database Error", "Failed to load groups.");
        }
    }

    /**
     * Loads articles associated with a specific group ID.
     * Fetches the list of articles linked to the group and updates the list view.
     *
     * @param groupId The ID of the group.
     */
    private void loadArticlesForGroup(long groupId) {
        try {
            List<HelpArticle> articles = helpArticleDAO.getArticlesByGroupId(groupId, false);
            ObservableList<HelpArticle> articlesList = FXCollections.observableArrayList(articles);
            articlesListView.setItems(articlesList);
        } catch (SQLException e) {
            showAlert(Alert.AlertType.ERROR, "Database Error", "Failed to load articles for the selected group.");
        }
    }

    /**
     * Shows a dialog to add a new group.
     * Prompts the user to enter the group name and handles the addition to the database.
     */
    private void showAddGroupDialog() {
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Add Group");
        dialog.setHeaderText("Enter the name of the new group:");
        dialog.setContentText("Group Name:");

        Optional<String> result = dialog.showAndWait();
        result.ifPresent(name -> {
            if (name.trim().isEmpty()) {
                showAlert(Alert.AlertType.WARNING, "Invalid Input", "Group name cannot be empty.");
                return;
            }
            try {
                Group existing = groupDAO.getGroupByName(name.trim());
                if (existing != null) {
                    showAlert(Alert.AlertType.WARNING, "Duplicate Group", "A group with this name already exists.");
                    return;
                }
                Group group = new Group(name.trim());
                groupDAO.addGroup(group);
                showAlert(Alert.AlertType.INFORMATION, "Success", "Group added successfully.");
                loadGroups();
            } catch (SQLException e) {
                showAlert(Alert.AlertType.ERROR, "Database Error", "Failed to add group.");
            }
        });
    }

    /**
     * Shows a dialog to edit the selected group.
     * Allows the user to modify the group's name and updates it in the database.
     */
    private void showEditGroupDialog() {
        Group selected = tableView.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showAlert(Alert.AlertType.WARNING, "No Selection", "Please select a group to edit.");
            return;
        }

        TextInputDialog dialog = new TextInputDialog(selected.getName());
        dialog.setTitle("Edit Group");
        dialog.setHeaderText("Modify the name of the selected group:");
        dialog.setContentText("Group Name:");

        Optional<String> result = dialog.showAndWait();
        result.ifPresent(name -> {
            if (name.trim().isEmpty()) {
                showAlert(Alert.AlertType.WARNING, "Invalid Input", "Group name cannot be empty.");
                return;
            }
            try {
                Group existing = groupDAO.getGroupByName(name.trim());
                if (existing != null && existing.getId() != selected.getId()) {
                    showAlert(Alert.AlertType.WARNING, "Duplicate Group", "A group with this name already exists.");
                    return;
                }
                selected.setName(name.trim());
                groupDAO.updateGroup(selected);
                showAlert(Alert.AlertType.INFORMATION, "Success", "Group updated successfully.");
                loadGroups();
            } catch (SQLException e) {
                showAlert(Alert.AlertType.ERROR, "Database Error", "Failed to update group.");
            }
        });
    }

    /**
     * Deletes the selected group after user confirmation.
     * Removes the group from the database and refreshes the table view.
     */
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
                articlesListView.getItems().clear();
            } catch (SQLException e) {
                showAlert(Alert.AlertType.ERROR, "Database Error", "Failed to delete group.");
            }
        }
    }

    /**
     * Assigns articles to the selected group by opening an assignment dialog.
     * Allows the user to select multiple articles to associate with the group.
     */
    private void assignArticlesToGroup() {
        Group selectedGroup = tableView.getSelectionModel().getSelectedItem();
        if (selectedGroup == null) {
            showAlert(Alert.AlertType.WARNING, "No Selection", "Please select a group to assign articles.");
            return;
        }

        AssignArticlesDialog dialog;
        try {
            dialog = new AssignArticlesDialog(selectedGroup);
            Optional<List<HelpArticle>> result = dialog.showAndWait();

            result.ifPresent(articles -> {
                try {
                    for (HelpArticle article : articles) {
                        helpArticleDAO.associateArticleWithGroup(article.getId(), selectedGroup.getId());
                    }

                    showAlert(Alert.AlertType.INFORMATION, "Success", "Articles assigned to group successfully.");
                    loadArticlesForGroup(selectedGroup.getId());
                } catch (SQLException e) {
                    showAlert(Alert.AlertType.ERROR, "Database Error", "Failed to assign articles to group.");
                }
            });
        } catch (SQLException e) {
            showAlert(Alert.AlertType.ERROR, "Database Error", "Failed to initialize assignment dialog.");
        }
    }

    /**
     * Removes selected articles from the selected group after user confirmation.
     * Updates the association in the database and refreshes the articles list.
     */
    private void removeArticlesFromGroup() {
        Group selectedGroup = tableView.getSelectionModel().getSelectedItem();
        if (selectedGroup == null) {
            showAlert(Alert.AlertType.WARNING, "No Selection", "Please select a group to remove articles from.");
            return;
        }

        ObservableList<HelpArticle> selectedArticles = articlesListView.getSelectionModel().getSelectedItems();
        if (selectedArticles == null || selectedArticles.isEmpty()) {
            showAlert(Alert.AlertType.WARNING, "No Articles Selected", "Please select at least one article to remove.");
            return;
        }

        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION, "Are you sure you want to remove the selected articles from the group?", ButtonType.YES, ButtonType.NO);
        Optional<ButtonType> result = confirm.showAndWait();

        if (result.isPresent() && result.get() == ButtonType.YES) {
            try {
                for (HelpArticle article : selectedArticles) {
                    helpArticleDAO.dissociateArticleFromGroup(article.getId(), selectedGroup.getId());
                }
                showAlert(Alert.AlertType.INFORMATION, "Success", "Selected articles removed from group successfully.");
                loadArticlesForGroup(selectedGroup.getId());
            } catch (SQLException e) {
                showAlert(Alert.AlertType.ERROR, "Database Error", "Failed to remove articles from group.");
            }
        }
    }

    /**
     * Handles navigation back to the home page based on the current user's role.
     * Invokes the {@link Main#showHomePage(User, Role)} method to display the appropriate home page.
     */
    private void handleBack() {
        User currentUser = SessionManager.getInstance().getCurrentUser();
        Role currentRole = SessionManager.getInstance().getCurrentRole();
        Main.showHomePage(currentUser, currentRole);
    }

    /**
     * Displays an alert dialog with the specified parameters.
     *
     * @param type    The type of alert (e.g., INFORMATION, ERROR, WARNING).
     * @param title   The title of the alert dialog.
     * @param content The content message of the alert dialog.
     */
    private void showAlert(Alert.AlertType type, String title, String content) {
        Alert alert = new Alert(type, content, ButtonType.OK);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.showAndWait();
    }
}
