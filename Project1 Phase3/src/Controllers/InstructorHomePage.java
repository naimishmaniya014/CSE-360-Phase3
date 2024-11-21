package Controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;

import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import java.util.Optional;

import Controllers.EditArticleDialog;
import models.*;
import Utilities.*;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.util.Pair;
import Controllers.HelpArticlePage;

public class InstructorHomePage {

	/**
     * <p> Title: Instructor Home Page Controller. </p>
     * 
     * <p> Description: This class manages the Instructor Home Page, providing basic
     * functionalities such as displaying a welcome message and allowing the
     * instructor to log out. </p>
     * 
     * @author Naimish Maniya
     * 
     * @version 1.00   2024-10-09  Initial version.
     */
	
	private VBox view;
    private User user;
    private Label welcomeLabel;
    private Button viewArticlesButton;
    private Button manageArticlesButton;
    private Button logoutButton;
    private Button editArticleButton;
    private TableView<HelpArticle> articlesTableView;
    private ObservableList<HelpArticle> articlesList;
    private HelpArticleDAO helpArticleDAO;
    private UserDAO userDAO;
    private TableView<HelpArticle> tableView;
    private GroupDAO groupDAO;
    private ComboBox<String> contentLevelComboBox;
    private ComboBox<String> groupComboBox;
    private TextField searchField;
    private Button searchButton;
    private ListView<String> searchResultsListView;
    private ObservableList<String> searchResults;
    private List<HelpArticle> currentSearchResults;
    private Button viewArticleButton;
    private Button createGroupButton;
    private Button editGroupButton;
    private Button deleteGroupButton;
    private ListView<String> groupsListView;
    private ObservableList<String> groupsList;
    private List<Group> currentGroups;
    private Button backupButton;
    private Button restoreButton;
    private Button addStudentButton;
    private Button deleteStudentButton;
    private ListView<String> studentsListView;
    private ObservableList<String> studentsList;
    private List<User> currentStudents;
    private Button addStudentToGroupButton;
    private Button removeStudentFromGroupButton;
    private ListView<String> groupMembersListView;
    private ObservableList<String> groupMembersList;


    public InstructorHomePage(User user) {
        this.user = user;

        view = new VBox(10);
        view.setPadding(new Insets(20));

        welcomeLabel = new Label("Welcome, " + user.getPreferredName() + " (Instructor)");
        viewArticlesButton = new Button("View Help Articles");
        viewArticlesButton.setOnAction(e -> handleViewArticles());

        manageArticlesButton = new Button("Manage Help Articles");
        manageArticlesButton.setOnAction(e -> handleManageArticles());

        logoutButton = new Button("Log Out");
        logoutButton.setOnAction(e -> Main.showLoginPage());

        editArticleButton = new Button("Edit Article");
        editArticleButton.setOnAction(e -> handleEditArticle());

        view.getChildren().addAll(welcomeLabel, viewArticlesButton, manageArticlesButton, editArticleButton, logoutButton);

        try {
            helpArticleDAO = new HelpArticleDAO();
            groupDAO = new GroupDAO();
        } catch (SQLException e) {
            showAlert(Alert.AlertType.ERROR, "Database Error", "Failed to load articles.");
            return;
        }
        
        try {
            userDAO = new UserDAO();
            userDAO = new UserDAO();
        } catch (SQLException e) {
            showAlert(Alert.AlertType.ERROR, "Database Error", "Failed to load articles.");
            return;
        }

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

        view.getChildren().add(articlesTableView);
        
        editArticleButton = new Button("Edit Article");
        editArticleButton.setOnAction(e -> {
            HelpArticle selected = articlesTableView.getSelectionModel().getSelectedItem();
            if (selected != null) {
                EditArticleDialog editDialog = new EditArticleDialog(selected);
                editDialog.showAndWait();
                loadArticles();
            }
        });
        
        editArticleButton.setDisable(true);

        articlesTableView.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            User currentUser = SessionManager.getInstance().getCurrentUser();
            if (newSelection != null && currentUser.getRoles().contains(Role.INSTRUCTOR)) {
                editArticleButton.setDisable(false);
            } else {
                editArticleButton.setDisable(true);
            }
        });
        
        GridPane searchPane = new GridPane();
        searchPane.setVgap(10);
        searchPane.setHgap(10);

        contentLevelComboBox = new ComboBox<>();
        contentLevelComboBox.getItems().addAll("all", "beginner", "intermediate", "advanced", "expert");
        contentLevelComboBox.setValue("all");

        groupComboBox = new ComboBox<>();
        try {
            groupComboBox.getItems().add("all");
            groupComboBox.getItems().addAll(new GroupDAO().getAllGroups().stream().map(Group::getName).toList());
            groupComboBox.setValue("all");
        } catch (SQLException e) {
            showAlert(Alert.AlertType.ERROR, "Database Error", "Failed to load groups.");
        }

        searchField = new TextField();
        searchField.setPromptText("Enter search query");

        searchButton = new Button("Search");
        searchButton.setOnAction(e -> handleSearch());

        searchPane.add(new Label("Content Level:"), 0, 0);
        searchPane.add(contentLevelComboBox, 1, 0);
        searchPane.add(new Label("Group:"), 0, 1);
        searchPane.add(groupComboBox, 1, 1);
        searchPane.add(new Label("Search:"), 0, 2);
        searchPane.add(searchField, 1, 2);
        searchPane.add(searchButton, 2, 2);

        view.getChildren().add(searchPane);

        searchResultsListView = new ListView<>();
        searchResults = FXCollections.observableArrayList();
        searchResultsListView.setItems(searchResults);
        view.getChildren().add(searchResultsListView);

     // Inside the constructor after initializing searchResultsListView
        viewArticleButton = new Button("View Article");
        viewArticleButton.setOnAction(e -> handleViewArticle(user));
        view.getChildren().add(viewArticleButton);
        
        createGroupButton = new Button("Create Group");
        createGroupButton.setOnAction(e -> handleCreateGroup());

        editGroupButton = new Button("Edit Group");
        editGroupButton.setOnAction(e -> handleEditGroup());

        deleteGroupButton = new Button("Delete Group");
        deleteGroupButton.setOnAction(e -> handleDeleteGroup());

        groupsListView = new ListView<>();
        groupsList = FXCollections.observableArrayList();
        groupsListView.setItems(groupsList);
        view.getChildren().add(groupsListView);

        ToolBar groupToolBar = new ToolBar(createGroupButton, editGroupButton, deleteGroupButton);
        view.getChildren().add(groupToolBar);
        loadGroups();
        
        backupButton = new Button("Backup Database");
        backupButton.setOnAction(e -> handleBackup());

        restoreButton = new Button("Restore Database");
        restoreButton.setOnAction(e -> handleRestore());

        ToolBar backupRestoreToolBar = new ToolBar(backupButton, restoreButton);
        view.getChildren().add(backupRestoreToolBar);
        
     // Inside the constructor after initializing other UI components
        addStudentButton = new Button("Add Student");
        addStudentButton.setOnAction(e -> handleAddStudent());

        deleteStudentButton = new Button("Delete Student");
        deleteStudentButton.setOnAction(e -> handleDeleteStudent());

        studentsListView = new ListView<>();
        studentsList = FXCollections.observableArrayList();
        studentsListView.setItems(studentsList);
        view.getChildren().add(studentsListView);

        ToolBar studentToolBar = new ToolBar(addStudentButton, deleteStudentButton);
        view.getChildren().add(studentToolBar);
        
     // Inside the constructor after initializing other UI components
        addStudentToGroupButton = new Button("Add Student to Group");
        addStudentToGroupButton.setOnAction(e -> handleAddStudentToGroup());

        removeStudentFromGroupButton = new Button("Remove Student from Group");
        removeStudentFromGroupButton.setOnAction(e -> handleRemoveStudentFromGroup());

        groupMembersListView = new ListView<>();
        groupMembersList = FXCollections.observableArrayList();
        groupMembersListView.setItems(groupMembersList);
        view.getChildren().add(groupMembersListView);

        ToolBar groupMembershipToolBar = new ToolBar(addStudentToGroupButton, removeStudentFromGroupButton);
        view.getChildren().add(groupMembershipToolBar);

        // Load group members when a group is selected
        groupsListView.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null && newSelection.matches("^\\d+\\. .*")) {
                int groupId = Integer.parseInt(newSelection.split("\\.")[0]);
                loadGroupMembers(groupId);
            }
        });


        loadStudents(); 

        initializeButtons();
        loadArticles();
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
        int groupId = Integer.parseInt(selectedGroup.split("\\.")[0]);

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
                // Check if the user exists and is a student
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
        int groupId = Integer.parseInt(selectedGroup.split("\\.")[0]);

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
            currentStudents = userDAO.getAllStudents();
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

            // Prompt for password
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
                try {
                    userDAO.addStudent(newStudent);
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
                    userDAO.deleteStudent(username);
                    showAlert(Alert.AlertType.INFORMATION, "Deleted", "Student deleted successfully.");
                    loadStudents();
                } catch (SQLException e) {
                    showAlert(Alert.AlertType.ERROR, "Deletion Failed", "Failed to delete the student.");
                }
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
                showAlert(Alert.AlertType.INFORMATION, "Backup Successful", "Database backed up to " + filename);
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
                loadArticles(); // Implement loadArticles() to refresh the articles list
                loadStudents(); // Refresh students list if necessary
            } catch (SQLException e) {
                showAlert(Alert.AlertType.ERROR, "Restore Failed", "Failed to restore the database.");
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

            // Prompt for special access group
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
        int groupId = Integer.parseInt(selected.split("\\.")[0]);
        Group groupToEdit = currentGroups.stream().filter(g -> g.getId() == groupId).findFirst().orElse(null);
        if (groupToEdit == null) {
            showAlert(Alert.AlertType.ERROR, "Error", "Selected group does not exist.");
            return;
        }

        // Prompt for new group name
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

            // Prompt for special access group
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
    }

    private void handleDeleteGroup() {
        String selected = groupsListView.getSelectionModel().getSelectedItem();
        if (selected == null || !selected.matches("^\\d+\\. .*")) {
            showAlert(Alert.AlertType.WARNING, "No Selection", "Please select a group to delete.");
            return;
        }
        int groupId = Integer.parseInt(selected.split("\\.")[0]);
        Group groupToDelete = currentGroups.stream().filter(g -> g.getId() == groupId).findFirst().orElse(null);
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
    }

    
    private void loadGroups() {
        try {
            currentGroups = groupDAO.getAllGroups();
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
    
    private void handleSearch() {
        String query = searchField.getText().trim();
        String groupName = groupComboBox.getValue();

        if (query.isEmpty()) {
            showAlert(Alert.AlertType.WARNING, "Input Required", "Please enter a search query.");
            return;
        }

        try {
            List<HelpArticle> results = helpArticleDAO.searchHelpArticles(user, query, groupName);
            currentSearchResults = results;
            searchResults.clear();

            searchResults.add("Active Group: " + groupName);
            searchResults.add("Search Results:");

            for (int i = 0; i < results.size(); i++) {
                HelpArticle article = results.get(i);
                String display = String.format("%d. %s - %s", 
                    i + 1, article.getTitle(), article.getShortDescription());
                searchResults.add(display);
            }

        } catch (SQLException e) {
            showAlert(Alert.AlertType.ERROR, "Database Error", "Failed to perform search.");
        }
    }


    
    private void handleViewArticle(User user) {
        String selected = searchResultsListView.getSelectionModel().getSelectedItem();
        if (selected == null || !selected.matches("^\\d+\\. .*")) {
            showAlert(Alert.AlertType.WARNING, "No Selection", "Please select an article to view.");
            return;
        }
        int sequenceNumber = Integer.parseInt(selected.split("\\.")[0]) - 1;
        if (sequenceNumber < 0 || sequenceNumber >= currentSearchResults.size()) {
            showAlert(Alert.AlertType.ERROR, "Invalid Selection", "Invalid article selection.");
            return;
        }
        HelpArticle article = currentSearchResults.get(sequenceNumber);
        try {
            HelpArticle fullArticle = helpArticleDAO.getHelpArticleById(article.getId(), user);
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
        User currentUser = SessionManager.getInstance().getCurrentUser();
        if (!currentUser.getRoles().contains(Role.INSTRUCTOR)) {
            showAlert(Alert.AlertType.ERROR, "Access Denied", "Only instructors can edit articles.");
            return;
        }
        HelpArticle selected = tableView.getSelectionModel().getSelectedItem();
        if (selected != null) {
            EditArticleDialog editDialog = new EditArticleDialog(selected);
            editDialog.showAndWait();
            loadArticles();
        } else {
            showAlert(Alert.AlertType.WARNING, "No Selection", "Please select an article to edit.");
        }
    }
    
    private void initializeButtons() {
        User currentUser = SessionManager.getInstance().getCurrentUser();

        editArticleButton.setDisable(true);

        articlesTableView.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null && currentUser.getRoles().contains(Role.INSTRUCTOR)) {
                editArticleButton.setDisable(false);
            } else {
                editArticleButton.setDisable(true);
            }
        });
    }
    
    private void loadArticles() {
        try {
            articlesList.setAll(helpArticleDAO.getAllHelpArticles(user));
        } catch (SQLException e) {
            showAlert(Alert.AlertType.ERROR, "Database Error", "Failed to load articles.");
        }
    }
    
    private void showAlert(Alert.AlertType type, String title, String content) {
        Alert alert = new Alert(type, content, ButtonType.OK);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.showAndWait();
    }
    
    
    private void handleViewArticles() {
        HelpArticlePage helpArticlePage = new HelpArticlePage();
        Scene scene = new Scene(helpArticlePage.getView(), 800, 600);
        Main.getStage().setScene(scene);
    }

    /**
     * Navigates to the HelpArticlePage for managing articles.
     */
    private void handleManageArticles() {
        HelpArticlePage helpArticlePage = new HelpArticlePage();
        Scene scene = new Scene(helpArticlePage.getView(), 800, 600);
        Main.getStage().setScene(scene);
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
