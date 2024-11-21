package Controllers;

import Utilities.HelpArticleDAO;
import Utilities.GroupDAO;
import Utilities.SessionManager;
import models.HelpArticle;
import models.Role;
import models.User;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;

import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

/**
 * <p> Title: HelpArticlePage Class </p>
 * 
 * <p> Description: This class provides the user interface and functionality for viewing,
 * adding, editing, and deleting help articles. It ensures that only authorized users can
 * perform certain operations based on their roles. </p>
 * 
 * @version 1.00  2024-10-29  Initial version.
 */
public class HelpArticlePage {

    private VBox view;
    private TableView<HelpArticle> tableView;
    private ObservableList<HelpArticle> articlesList;
    private HelpArticleDAO helpArticleDAO;
    private GroupDAO groupDAO;

    private Button backButton;
    private Button addButton;
    private Button editButton;
    private Button deleteButton;
    private Button refreshButton;
    private Button viewButton;

    /**
     * Constructs a HelpArticlePage instance.
     * Initializes UI components, sets up event handlers, and loads existing help articles from the database.
     */
    public HelpArticlePage() {
        try {
            helpArticleDAO = new HelpArticleDAO();
            groupDAO = new GroupDAO();
        } catch (SQLException e) {
            showAlert(Alert.AlertType.ERROR, "Database Error", "Failed to connect to the database.");
            return;
        }
        
        User currentUser = SessionManager.getInstance().getCurrentUser();

        view = new VBox(10);
        view.setPadding(new Insets(20));

        backButton = new Button("Back");
        backButton.setOnAction(e -> handleBack());

        tableView = new TableView<>();
        articlesList = FXCollections.observableArrayList();
        tableView.setItems(articlesList);

        TableColumn<HelpArticle, Long> idCol = new TableColumn<>("ID");
        idCol.setCellValueFactory(data -> new javafx.beans.property.SimpleLongProperty(data.getValue().getId()).asObject());
        idCol.setPrefWidth(50);

        TableColumn<HelpArticle, String> titleCol = new TableColumn<>("Title");
        titleCol.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue().getTitle()));
        titleCol.setPrefWidth(200);

        TableColumn<HelpArticle, String> shortDescCol = new TableColumn<>("Short Description");
        shortDescCol.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue().getShortDescription()));
        shortDescCol.setPrefWidth(300);

        tableView.getColumns().addAll(idCol, titleCol, shortDescCol);

        addButton = new Button("Add Article");
        addButton.setOnAction(e -> showAddArticleDialog());

        editButton = new Button("Edit Article");
        editButton.setOnAction(e -> handleEditArticle());

        deleteButton = new Button("Delete Article");
        deleteButton.setOnAction(e -> deleteSelectedArticle());

        refreshButton = new Button("Refresh");
        refreshButton.setOnAction(e -> loadArticles());
        
        viewButton = new Button("View");
        viewButton.setOnAction(e -> handleViewArticle());
        
        if (!currentUser.getRoles().contains(Role.INSTRUCTOR) && !currentUser.getRoles().contains(Role.ADMIN)) {
            editButton.setDisable(true);
            deleteButton.setDisable(true);
        }

        ToolBar toolBar = new ToolBar(backButton, addButton, editButton, deleteButton, viewButton, refreshButton);

        view.getChildren().addAll(toolBar, tableView);

        loadArticles();
    }
    
    private void handleViewArticle() {
        HelpArticle selected = tableView.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showAlert(Alert.AlertType.WARNING, "No Selection", "Please select an article to view.");
            return;
        }
        User currentUser = SessionManager.getInstance().getCurrentUser();
        try {
            HelpArticle article = helpArticleDAO.getHelpArticleById(selected.getId(), currentUser);
            if (article != null) {
                ViewArticleDialog dialog = new ViewArticleDialog(article, currentUser);
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
        if (!currentUser.getRoles().contains(Role.INSTRUCTOR) && !currentUser.getRoles().contains(Role.ADMIN)) {
            showAlert(Alert.AlertType.ERROR, "Access Denied", "Only instructors and admins can edit articles.");
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

    /**
     * Deletes the selected help article after user confirmation.
     * Removes the article from the database and refreshes the table view.
     */
    private void deleteSelectedArticle() {
        HelpArticle selected = tableView.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showAlert(Alert.AlertType.WARNING, "No Selection", "Please select an article to delete.");
            return;
        }

        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION, "Are you sure you want to delete the selected article?", ButtonType.YES, ButtonType.NO);
        Optional<ButtonType> result = confirm.showAndWait();

        if (result.isPresent() && result.get() == ButtonType.YES) {
            try {
                helpArticleDAO.deleteHelpArticle(selected.getId());
                showAlert(Alert.AlertType.INFORMATION, "Success", "Help article deleted successfully.");
                loadArticles();
            } catch (SQLException e) {
                showAlert(Alert.AlertType.ERROR, "Database Error", "Failed to delete help article.");
            }
        }
    }

    /**
     * Retrieves the main layout of the HelpArticlePage.
     *
     * @return The {@link VBox} containing all UI components of the page.
     */
    public VBox getView() {
        return view;
    }

    /**
     * Loads all help articles from the database into the table view.
     * Fetches the list of help articles and updates the observable list.
     */
    private void loadArticles() {
        User currentUser = SessionManager.getInstance().getCurrentUser();
        try {
            List<HelpArticle> articles = helpArticleDAO.getAllHelpArticles(currentUser);
            articlesList.setAll(articles);
        } catch (SQLException e) {
            showAlert(Alert.AlertType.ERROR, "Database Error", "Failed to load articles.");
        }
    }

    /**
     * Shows a dialog to add a new help article.
     * Prompts the user to enter details for the new article and handles the addition to the database.
     */
    private void showAddArticleDialog() {
        Dialog<HelpArticle> dialog = new Dialog<>();
        dialog.setTitle("Add Help Article");
        dialog.setHeaderText("Enter details for the new help article.");

        ButtonType addButtonType = new ButtonType("Add", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(addButtonType, ButtonType.CANCEL);

        GridPane grid = createArticleForm(null);
        dialog.getDialogPane().setContent(grid);

        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == addButtonType) {
                return extractArticleFromForm(grid, null);
            }
            return null;
        });

        Optional<HelpArticle> result = dialog.showAndWait();

        result.ifPresent(article -> {
            try {
                User currentUser = SessionManager.getInstance().getCurrentUser();
                boolean isSpecialAccessGroup = false;
                Long groupId = null;

                
                helpArticleDAO.addHelpArticle(article);
                if (isSpecialAccessGroup && groupId != null) {
                    helpArticleDAO.associateArticleWithGroup(article.getId(), groupId);
                }
                showAlert(Alert.AlertType.INFORMATION, "Success", "Help article added successfully.");
                loadArticles();
            } catch (SQLException e) {
                showAlert(Alert.AlertType.ERROR, "Database Error", "Failed to add help article.");
            }
        });
    }

    /**
     * Creates a form for adding or editing a help article.
     *
     * @param article The HelpArticle to edit, or null for adding a new article.
     * @return The GridPane containing the form fields.
     */
    private GridPane createArticleForm(HelpArticle article) {
        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20, 150, 10, 10));

        TextField headerField = new TextField();
        headerField.setPromptText("Header");
        TextField titleField = new TextField();
        titleField.setPromptText("Title");
        TextArea shortDescArea = new TextArea();
        shortDescArea.setPromptText("Short Description");
        shortDescArea.setPrefRowCount(3);
        TextField keywordsField = new TextField();
        keywordsField.setPromptText("Keywords (comma-separated)");
        TextArea bodyArea = new TextArea();
        bodyArea.setPromptText("Body");
        bodyArea.setPrefRowCount(10);
        TextField referenceLinksField = new TextField();
        referenceLinksField.setPromptText("Reference Links (comma-separated)");

        if (article != null) {
            headerField.setText(article.getHeader());
            titleField.setText(article.getTitle());
            shortDescArea.setText(article.getShortDescription());
            keywordsField.setText(String.join(",", article.getKeywords()));
            bodyArea.setText(article.getBody());
            referenceLinksField.setText(String.join(",", article.getReferenceLinks()));
        }

        grid.add(new Label("Header:"), 0, 0);
        grid.add(headerField, 1, 0);
        grid.add(new Label("Title:"), 0, 1);
        grid.add(titleField, 1, 1);
        grid.add(new Label("Short Description:"), 0, 2);
        grid.add(shortDescArea, 1, 2);
        grid.add(new Label("Keywords:"), 0, 3);
        grid.add(keywordsField, 1, 3);
        grid.add(new Label("Body:"), 0, 4);
        grid.add(bodyArea, 1, 4);
        grid.add(new Label("Reference Links:"), 0, 5);
        grid.add(referenceLinksField, 1, 5);

        return grid;
    }

    /**
     * Extracts a HelpArticle object from the form fields.
     *
     * @param grid The GridPane containing the form fields.
     * @param id   The ID of the article being edited, or null for a new article.
     * @return A HelpArticle object with the form data.
     */
    private HelpArticle extractArticleFromForm(GridPane grid, Long id) {
        TextField headerField = (TextField) grid.getChildren().get(1);
        TextField titleField = (TextField) grid.getChildren().get(3);
        TextArea shortDescArea = (TextArea) grid.getChildren().get(5);
        TextField keywordsField = (TextField) grid.getChildren().get(7);
        TextArea bodyArea = (TextArea) grid.getChildren().get(9);
        TextField referenceLinksField = (TextField) grid.getChildren().get(11);

        HelpArticle article = new HelpArticle();
        if (id != null) {
            article.setId(id);
        }
        article.setHeader(headerField.getText().trim());
        article.setTitle(titleField.getText().trim());
        article.setShortDescription(shortDescArea.getText().trim());
        String keywordsText = keywordsField.getText().trim();
        if (!keywordsText.isEmpty()) {
            article.setKeywords(Arrays.asList(keywordsText.split(",")));
        }
        String bodyText = bodyArea.getText().trim();
        if (!bodyText.isEmpty()) {
            article.setBody(bodyText);
        }
        String referencesText = referenceLinksField.getText().trim();
        if (!referencesText.isEmpty()) {
            article.setReferenceLinks(Arrays.asList(referencesText.split(",")));
        }

        return article;
    }

    /**
     * Handles navigation back to the home page based on the current user's role.
     * Invokes the {@link Main#showHomePage(User)} method to display the appropriate home page.
     */
    private void handleBack() {
        User currentUser = SessionManager.getInstance().getCurrentUser();
        Main.showHomePage(currentUser, null);
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
