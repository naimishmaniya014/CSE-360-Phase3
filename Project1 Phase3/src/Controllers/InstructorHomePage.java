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
import java.sql.Statement;
import java.util.List;
import java.util.Optional;

public class InstructorHomePage {

    private VBox view;
    private User user;
    private Label welcomeLabel;
    private Button logoutButton;
    private HelpArticleDAO helpArticleDAO;
    private UserDAO userDAO;
    private GroupDAO groupDAO;
    private ObservableList<HelpArticle> articlesList;
    private TableView<HelpArticle> articlesTableView;
    private Button viewArticleButton;
    private Button editArticleButton;
    private Button addArticleButton;
    private Button assignArticleToGroupButton;
    private Button viewGroupArticlesButton;
    private ObservableList<String> groupsList;
    private ListView<String> groupsListView;
    private Button createGroupButton;
    private Button editGroupButton;
    private Button deleteGroupButton;
    private HBox groupButtonsBox;
    private ObservableList<String> groupMembersList;
    private ListView<String> groupMembersListView;
    private Button addStudentToGroupButton;
    private Button removeStudentFromGroupButton;
    private HBox groupMemberButtonsBox;
    private ObservableList<String> studentsList;
    private ListView<String> studentsListView;
    private Button addStudentButton;
    private Button deleteStudentButton;
    private HBox studentButtonsBox;
    private ObservableList<String> searchResults;
    private ListView<String> searchResultsListView;
    private TextField searchField;
    private ComboBox<String> contentLevelComboBox;
    private ComboBox<String> groupComboBox;
    private Button searchButton;
    private Button backupButton;
    private Button restoreButton;

    public InstructorHomePage(User user) {
        this.user = user;

        view = new VBox(10);
        view.setPadding(new Insets(20));

        welcomeLabel = new Label("Welcome, " + user.getPreferredName() + " (Instructor)");
        logoutButton = new Button("Log Out");
        logoutButton.setOnAction(e -> Main.showLoginPage());

        TabPane tabPane = new TabPane();

        Tab helpArticlesTab = new Tab("Help Articles");
        helpArticlesTab.setClosable(false);
        VBox helpArticlesBox = new VBox(10);
        articlesList = FXCollections.observableArrayList();
        articlesTableView = new TableView<>();
        articlesTableView.setItems(articlesList);

        TableColumn<HelpArticle, Long> idCol = new TableColumn<>("ID");
        idCol.setCellValueFactory(data -> new javafx.beans.property.SimpleLongProperty(data.getValue().getId()).asObject());
        idCol.setPrefWidth(50);

        TableColumn<HelpArticle, String> titleCol = new TableColumn<>("Title");
        titleCol.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue().getTitle()));
        titleCol.setPrefWidth(200);

        TableColumn<HelpArticle, String> descriptionCol = new TableColumn<>("Short Description");
        descriptionCol.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue().getShortDescription()));
        descriptionCol.setPrefWidth(300);

        articlesTableView.getColumns().addAll(idCol, titleCol, descriptionCol);

        HBox articleButtons = new HBox(10);
        viewArticleButton = new Button("View Article");
        viewArticleButton.setOnAction(e -> handleViewArticle());
        editArticleButton = new Button("Edit Article");
        editArticleButton.setOnAction(e -> handleEditArticle());
        addArticleButton = new Button("Add Article");
        addArticleButton.setOnAction(e -> handleAddArticle());
        assignArticleToGroupButton = new Button("Assign to Group");
        assignArticleToGroupButton.setOnAction(e -> handleAssignArticleToGroup());
        viewGroupArticlesButton = new Button("View Group Articles");
        viewGroupArticlesButton.setOnAction(e -> handleViewGroupArticles());
        viewGroupArticlesButton.setDisable(true);
        articleButtons.getChildren().addAll(viewArticleButton, editArticleButton, addArticleButton, assignArticleToGroupButton, viewGroupArticlesButton);

        articlesTableView.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                editArticleButton.setDisable(false);
                assignArticleToGroupButton.setDisable(false);
                viewGroupArticlesButton.setDisable(false);
            } else {
                editArticleButton.setDisable(true);
                assignArticleToGroupButton.setDisable(true);
                viewGroupArticlesButton.setDisable(true);
            }
        });

        helpArticlesBox.getChildren().addAll(articlesTableView, articleButtons);
        helpArticlesTab.setContent(helpArticlesBox);

        Tab groupManagementTab = new Tab("Group Management");
        groupManagementTab.setClosable(false);
        VBox groupManagementBox = new VBox(10);

        groupsList = FXCollections.observableArrayList();
        groupsListView = new ListView<>();
        groupsListView.setItems(groupsList);

        groupButtonsBox = new HBox(10);
        createGroupButton = new Button("Create Group");
        createGroupButton.setOnAction(e -> handleCreateGroup());
        editGroupButton = new Button("Edit Group");
        editGroupButton.setOnAction(e -> handleEditGroup());
        deleteGroupButton = new Button("Delete Group");
        deleteGroupButton.setOnAction(e -> handleDeleteGroup());
        groupButtonsBox.getChildren().addAll(createGroupButton, editGroupButton, deleteGroupButton);

        groupMembersList = FXCollections.observableArrayList();
        groupMembersListView = new ListView<>();
        groupMembersListView.setItems(groupMembersList);

        groupMemberButtonsBox = new HBox(10);
        addStudentToGroupButton = new Button("Add Student to Group");
        addStudentToGroupButton.setOnAction(e -> handleAddStudentToGroup());
        removeStudentFromGroupButton = new Button("Remove Student from Group");
        removeStudentFromGroupButton.setOnAction(e -> handleRemoveStudentFromGroup());
        groupMemberButtonsBox.getChildren().addAll(addStudentToGroupButton, removeStudentFromGroupButton);

        groupsListView.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null && newSelection.matches("^\\d+\\. .*")) {
                long groupId = Long.parseLong(newSelection.split("\\.")[0]);
                loadGroupMembers(groupId);
                viewGroupArticlesButton.setDisable(false);
            } else {
                groupMembersList.clear();
                viewGroupArticlesButton.setDisable(true);
            }
        });

        groupManagementBox.getChildren().addAll(new Label("Groups:"), groupsListView, groupButtonsBox, new Label("Group Members:"), groupMembersListView, groupMemberButtonsBox);
        groupManagementTab.setContent(groupManagementBox);

        Tab studentManagementTab = new Tab("Student Management");
        studentManagementTab.setClosable(false);
        VBox studentManagementBox = new VBox(10);

        studentsList = FXCollections.observableArrayList();
        studentsListView = new ListView<>();
        studentsListView.setItems(studentsList);

        studentButtonsBox = new HBox(10);
        addStudentButton = new Button("Add Student");
        addStudentButton.setOnAction(e -> handleAddStudent());
        deleteStudentButton = new Button("Delete Student");
        deleteStudentButton.setOnAction(e -> handleDeleteStudent());
        studentButtonsBox.getChildren().addAll(addStudentButton, deleteStudentButton);

        studentManagementBox.getChildren().addAll(new Label("Students:"), studentsListView, studentButtonsBox);
        studentManagementTab.setContent(studentManagementBox);

        Tab backupRestoreTab = new Tab("Backup/Restore");
        backupRestoreTab.setClosable(false);
        VBox backupRestoreBox = new VBox(10);

        HBox backupRestoreButtons = new HBox(10);
        backupButton = new Button("Backup Database");
        backupButton.setOnAction(e -> handleBackup());
        restoreButton = new Button("Restore Database");
        restoreButton.setOnAction(e -> handleRestore());
        backupRestoreButtons.getChildren().addAll(backupButton, restoreButton);

        backupRestoreBox.getChildren().addAll(backupRestoreButtons);
        backupRestoreTab.setContent(backupRestoreBox);

        tabPane.getTabs().addAll(helpArticlesTab, groupManagementTab, studentManagementTab, backupRestoreTab);

        view.getChildren().addAll(welcomeLabel, tabPane, logoutButton);

        try {
            helpArticleDAO = new HelpArticleDAO();
            groupDAO = new GroupDAO();
            userDAO = new UserDAO();
        } catch (SQLException e) {
            showAlert(Alert.AlertType.ERROR, "Database Error", "Failed to load data.");
            return;
        }

        loadArticles();
        loadGroups();
        loadStudents();
    }
    
    private void handleViewGroupArticles() {
    	String selectedGroup = groupsListView.getSelectionModel().getSelectedItem();
        if (selectedGroup == null || !selectedGroup.matches("^\\d+\\. .*")) {
            showAlert(Alert.AlertType.WARNING, "No Group Selected", "Please select a group first.");
            return;
        }
        long groupId = Long.parseLong(selectedGroup.split("\\.")[0]);
        ViewGroupArticlesDialog dialog = new ViewGroupArticlesDialog(groupDAO, helpArticleDAO, groupId);
        dialog.showAndWait();
    }
    
    private void handleAssignArticleToGroup() {
        HelpArticle selectedArticle = articlesTableView.getSelectionModel().getSelectedItem();
        if (selectedArticle == null) {
            showAlert(Alert.AlertType.WARNING, "No Selection", "Please select an article to assign.");
            return;
        }

        String selectedGroup = groupsListView.getSelectionModel().getSelectedItem();
        if (selectedGroup == null || !selectedGroup.matches("^\\d+\\. .*")) {
            showAlert(Alert.AlertType.WARNING, "No Group Selected", "Please select a group to assign the article to.");
            return;
        }
        long groupId = Long.parseLong(selectedGroup.split("\\.")[0]);

        AssignArticleToGroupDialog assignDialog = new AssignArticleToGroupDialog(groupDAO, helpArticleDAO, groupId);
        Optional<Void> result = assignDialog.showAndWait();
        if (result.isPresent()) {
            try {
                helpArticleDAO.associateArticleWithGroup(selectedArticle.getId(), groupId);
                showAlert(Alert.AlertType.INFORMATION, "Success", "Article assigned to group successfully.");
            } catch (SQLException e) {
                showAlert(Alert.AlertType.ERROR, "Database Error", "Failed to assign article to group.");
            }
        }
    }
    
    private void handleViewArticle() {
        HelpArticle selected = articlesTableView.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showAlert(Alert.AlertType.WARNING, "No Selection", "Please select an article to view.");
            return;
        }
        try {
            HelpArticle fullArticle = helpArticleDAO.getHelpArticleById(selected.getId(), user);
            if (fullArticle != null && fullArticle.getBody() != null) {
                ViewArticleDialog dialog = new ViewArticleDialog(fullArticle, user);
                dialog.showAndWait();
            } else {
                showAlert(Alert.AlertType.ERROR, "Access Denied", "You do not have permission to view this article.");
            }
        } catch (SQLException e) {
            showAlert(Alert.AlertType.ERROR, "Database Error", "Failed to retrieve article.");
        }
    }
    
    private void handleEditArticle() {
        HelpArticle selected = articlesTableView.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showAlert(Alert.AlertType.WARNING, "No Selection", "Please select an article to edit.");
            return;
        }
        EditArticleDialog editDialog = new EditArticleDialog(selected);
        editDialog.showAndWait();
        loadArticles();
    }

    private void handleAddArticle() {
        AddArticleDialog addDialog = new AddArticleDialog();
        Optional<HelpArticle> result = addDialog.showAndWait();
        result.ifPresent(article -> {
            try {
                helpArticleDAO.addHelpArticle(article);
                showAlert(Alert.AlertType.INFORMATION, "Success", "Article added successfully.");
                loadArticles();
            } catch (SQLException e) {
                showAlert(Alert.AlertType.ERROR, "Database Error", "Failed to add article.");
            }
        });
    }

    private void handleBackup() {
        TextInputDialog dialog = new TextInputDialog("backup.sql");
        dialog.setTitle("Backup Database");
        dialog.setHeaderText("Enter the filename for the backup:");
        dialog.setContentText("Filename:");

        Optional<String> result = dialog.showAndWait();
        result.ifPresent(filename -> {
            try (Statement stmt = helpArticleDAO.getConnection().createStatement()) {
                String backupSQL = "SCRIPT TO '" + filename + "';";
                stmt.execute(backupSQL);
            } catch (SQLException e) {
                showAlert(Alert.AlertType.ERROR, "Backup Failed", "Failed to backup the database.");
            }
        });
    }

    private void handleRestore() {
        TextInputDialog dialog = new TextInputDialog("backup.sql");
        dialog.setTitle("Restore Database");
        dialog.setHeaderText("Enter the filename for the restore:");
        dialog.setContentText("Filename:");

        Optional<String> result = dialog.showAndWait();
        result.ifPresent(filename -> {
            try (Statement stmt = helpArticleDAO.getConnection().createStatement()) {
                String restoreSQL = "RUNSCRIPT FROM '" + filename + "';";
                stmt.execute(restoreSQL);
                showAlert(Alert.AlertType.INFORMATION, "Restore Successful", "Database restored from " + filename);
                loadGroups();
                loadArticles();
                loadStudents();
            } catch (SQLException e) {
                showAlert(Alert.AlertType.ERROR, "Restore Failed", "Failed to restore the database.");
            }
        });
    }

    private void loadGroupMembers(long groupId) {
        try {
            List<String> members = groupDAO.getGroupMembers(groupId);
            groupMembersList.clear();
            groupMembersList.addAll(members);
        } catch (SQLException e) {
            showAlert(Alert.AlertType.ERROR, "Database Error", "Failed to load group members.");
        }
    }

    private void handleAddStudentToGroup() {
        String selectedGroup = groupsListView.getSelectionModel().getSelectedItem();
        if (selectedGroup == null || !selectedGroup.matches("^\\d+\\. .*")) {
            showAlert(Alert.AlertType.WARNING, "No Group Selected", "Please select a group first.");
            return;
        }
        long groupId = Long.parseLong(selectedGroup.split("\\.")[0]);

        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Add Student to Group");
        dialog.setHeaderText("Enter the username of the student to add:");
        dialog.setContentText("Username:");

        Optional<String> result = dialog.showAndWait();
        result.ifPresent(usernameInput -> {
            if (usernameInput.trim().isEmpty()) {
                showAlert(Alert.AlertType.WARNING, "Input Required", "Username cannot be empty.");
                return;
            }

            try {
                User student = userDAO.getUserByUsername(usernameInput);
                if (student == null || !student.getRoles().contains(Role.STUDENT)) {
                    showAlert(Alert.AlertType.ERROR, "Invalid User", "The specified user is not a student.");
                    return;
                }

                groupDAO.addStudentToGroup(groupId, usernameInput);
                showAlert(Alert.AlertType.INFORMATION, "Success", "Student added to group successfully.");
                loadGroupMembers(groupId);
            } catch (SQLException e) {
                showAlert(Alert.AlertType.ERROR, "Database Error", "Failed to add student to group.");
            }
        });
    }

    private void handleRemoveStudentFromGroup() {
        String selectedGroup = groupsListView.getSelectionModel().getSelectedItem();
        if (selectedGroup == null || !selectedGroup.matches("^\\d+\\. .*")) {
            showAlert(Alert.AlertType.WARNING, "No Group Selected", "Please select a group first.");
            return;
        }
        long groupId = Long.parseLong(selectedGroup.split("\\.")[0]);

        String selectedStudent = groupMembersListView.getSelectionModel().getSelectedItem();
        if (selectedStudent == null) {
            showAlert(Alert.AlertType.WARNING, "No Student Selected", "Please select a student to remove.");
            return;
        }

        Alert confirmationAlert = new Alert(Alert.AlertType.CONFIRMATION, 
            "Are you sure you want to remove the student: " + selectedStudent + " from the group?", 
            ButtonType.YES, ButtonType.NO);
        confirmationAlert.setTitle("Confirm Removal");
        confirmationAlert.setHeaderText(null);
        confirmationAlert.showAndWait().ifPresent(response -> {
            if (response == ButtonType.YES) {
                try {
                    groupDAO.removeStudentFromGroup(groupId, selectedStudent);
                    showAlert(Alert.AlertType.INFORMATION, "Removed", "Student removed from group successfully.");
                    loadGroupMembers(groupId);
                } catch (SQLException e) {
                    showAlert(Alert.AlertType.ERROR, "Removal Failed", "Failed to remove student from group.");
                }
            }
        });
    }

    private void loadStudents() {
        try {
            List<User> currentStudents = userDAO.getAllStudents();
            studentsList.clear();
            for (User student : currentStudents) {
                String display = String.format("%s - %s %s", 
                    student.getUsername(), student.getFirstName(), student.getLastName());
                studentsList.add(display);
            }
        } catch (SQLException e) {
            showAlert(Alert.AlertType.ERROR, "Database Error", "Failed to load students.");
        }
    }

    private void handleAddStudent() {
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Add Student");
        dialog.setHeaderText("Enter the username for the new student:");
        dialog.setContentText("Username:");

        Optional<String> result = dialog.showAndWait();
        result.ifPresent(usernameInput -> {
            if (usernameInput.trim().isEmpty()) {
                showAlert(Alert.AlertType.WARNING, "Input Required", "Username cannot be empty.");
                return;
            }

            PasswordField passwordField = new PasswordField();
            Dialog<Pair<String, String>> passwordDialog = new Dialog<>();
            passwordDialog.setTitle("Set Password");
            passwordDialog.setHeaderText("Set password for the new student:");

            ButtonType setButtonType = new ButtonType("Set", ButtonBar.ButtonData.OK_DONE);
            passwordDialog.getDialogPane().getButtonTypes().addAll(setButtonType, ButtonType.CANCEL);

            GridPane grid = new GridPane();
            grid.setHgap(10);
            grid.setVgap(10);
            grid.setPadding(new Insets(20, 150, 10, 10));

            grid.add(new Label("Password:"), 0, 0);
            grid.add(passwordField, 1, 0);

            passwordDialog.getDialogPane().setContent(grid);

            passwordDialog.setResultConverter(dialogButton -> {
                if (dialogButton == setButtonType) {
                    return new Pair<>(usernameInput, passwordField.getText());
                }
                return null;
            });

            Optional<Pair<String, String>> passwordResult = passwordDialog.showAndWait();
            passwordResult.ifPresent(pair -> {
                String username = pair.getKey();
                String password = pair.getValue();

                if (password.trim().isEmpty()) {
                    showAlert(Alert.AlertType.WARNING, "Input Required", "Password cannot be empty.");
                    return;
                }

                User newStudent = new User(username, password);
                newStudent.setRoles(FXCollections.observableArrayList(Role.STUDENT));
                try {
                    userDAO.addUser(newStudent);
                    showAlert(Alert.AlertType.INFORMATION, "Success", "Student added successfully.");
                    loadStudents();
                } catch (SQLException e) {
                    showAlert(Alert.AlertType.ERROR, "Database Error", "Failed to add student. Username might already exist.");
                }
            });
        });
    }

    private void handleDeleteStudent() {
        String selected = studentsListView.getSelectionModel().getSelectedItem();
        if (selected == null || !selected.contains(" - ")) {
            showAlert(Alert.AlertType.WARNING, "No Selection", "Please select a student to delete.");
            return;
        }
        String username = selected.split(" - ")[0];

        Alert confirmationAlert = new Alert(Alert.AlertType.CONFIRMATION, 
            "Are you sure you want to delete the student: " + username + "?", 
            ButtonType.YES, ButtonType.NO);
        confirmationAlert.setTitle("Confirm Deletion");
        confirmationAlert.setHeaderText(null);
        confirmationAlert.showAndWait().ifPresent(response -> {
            if (response == ButtonType.YES) {
                try {
                    userDAO.deleteUser(username);
                    showAlert(Alert.AlertType.INFORMATION, "Deleted", "Student deleted successfully.");
                    loadStudents();
                } catch (SQLException e) {
                    showAlert(Alert.AlertType.ERROR, "Deletion Failed", "Failed to delete the student.");
                }
            }
        });
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
        String selected = groupsListView.getSelectionModel().getSelectedItem();
        if (selected == null || !selected.matches("^\\d+\\. .*")) {
            showAlert(Alert.AlertType.WARNING, "No Selection", "Please select a group to edit.");
            return;
        }
        long groupId = Long.parseLong(selected.split("\\.")[0]);
        try {
            Group groupToEdit = groupDAO.getGroupById(groupId);
            if (groupToEdit == null) {
                showAlert(Alert.AlertType.ERROR, "Error", "Selected group does not exist.");
                return;
            }

            TextInputDialog nameDialog = new TextInputDialog(groupToEdit.getName());
            nameDialog.setTitle("Edit Group");
            nameDialog.setHeaderText("Edit the name of the group:");
            nameDialog.setContentText("Group Name:");

            Optional<String> nameResult = nameDialog.showAndWait();
            nameResult.ifPresent(newName -> {
                if (newName.trim().isEmpty()) {
                    showAlert(Alert.AlertType.WARNING, "Input Required", "Group name cannot be empty.");
                    return;
                }

                ChoiceDialog<String> accessDialog = new ChoiceDialog<>(groupToEdit.isSpecialAccessGroup() ? "Yes" : "No", "Yes", "No");
                accessDialog.setTitle("Group Access Type");
                accessDialog.setHeaderText("Is this a Special Access Group?");
                accessDialog.setContentText("Select Yes or No:");

                Optional<String> accessResult = accessDialog.showAndWait();
                accessResult.ifPresent(access -> {
                    boolean isSpecial = access.equalsIgnoreCase("Yes");
                    try {
                        groupDAO.updateGroup(groupId, newName, isSpecial);
                        showAlert(Alert.AlertType.INFORMATION, "Success", "Group updated successfully.");
                        loadGroups();
                    } catch (SQLException e) {
                        showAlert(Alert.AlertType.ERROR, "Database Error", "Failed to update group.");
                    }
                });
            });
        } catch (SQLException e) {
            showAlert(Alert.AlertType.ERROR, "Error", "Failed to retrieve group details.");
        }
    }

    private void handleDeleteGroup() {
        String selected = groupsListView.getSelectionModel().getSelectedItem();
        if (selected == null || !selected.matches("^\\d+\\. .*")) {
            showAlert(Alert.AlertType.WARNING, "No Selection", "Please select a group to delete.");
            return;
        }
        long groupId = Long.parseLong(selected.split("\\.")[0]);
        try {
            Group groupToDelete = groupDAO.getGroupById(groupId);
            if (groupToDelete == null) {
                showAlert(Alert.AlertType.ERROR, "Error", "Selected group does not exist.");
                return;
            }

            Alert confirmationAlert = new Alert(Alert.AlertType.CONFIRMATION, 
                "Are you sure you want to delete the group: " + groupToDelete.getName() + "?", 
                ButtonType.YES, ButtonType.NO);
            confirmationAlert.setTitle("Confirm Deletion");
            confirmationAlert.setHeaderText(null);
            confirmationAlert.showAndWait().ifPresent(response -> {
                if (response == ButtonType.YES) {
                    try {
                        groupDAO.deleteGroup(groupId);
                        showAlert(Alert.AlertType.INFORMATION, "Deleted", "Group deleted successfully.");
                        loadGroups();
                    } catch (SQLException e) {
                        showAlert(Alert.AlertType.ERROR, "Deletion Failed", "Failed to delete the group.");
                    }
                }
            });
        } catch (SQLException e) {
            showAlert(Alert.AlertType.ERROR, "Error", "Failed to retrieve group details.");
        }
    }

    private void handleViewArticles() {
        HelpArticlePage helpArticlePage = new HelpArticlePage();
        Scene scene = new Scene(helpArticlePage.getView(), 800, 600);
        Main.getStage().setScene(scene);
    }

    private void handleManageArticles() {
        HelpArticlePage helpArticlePage = new HelpArticlePage();
        Scene scene = new Scene(helpArticlePage.getView(), 800, 600);
        Main.getStage().setScene(scene);
    }


    private void loadArticles() {
        try {
            articlesList.setAll(helpArticleDAO.getAllHelpArticles(user));
        } catch (SQLException e) {
            showAlert(Alert.AlertType.ERROR, "Database Error", "Failed to load articles.");
        }
    }

    private void loadGroups() {
        try {
            List<Group> currentGroups = groupDAO.getAllGroups();
            groupsList.clear();
            for (Group group : currentGroups) {
                String display = String.format("%d. %s (Special Access: %s)", 
                    group.getId(), group.getName(), group.isSpecialAccessGroup() ? "Yes" : "No");
                groupsList.add(display);
            }
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

    /**
     * Returns the view for the Instructor Home Page, which is a VBox layout.
     * 
     * @return The VBox layout of the instructor's home page.
     */
    public VBox getView() {
        return view;
    }
}
