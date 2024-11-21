package Controllers;

import Utilities.HelpArticleDAO;
import Utilities.GroupDAO;
import Utilities.SessionManager;
import models.Group;
import models.HelpArticle;
import models.Role;
import models.User;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;

import java.sql.SQLException;
import java.util.Arrays;

/**
 * <p> Title: CreateArticlePage Class </p>
 * 
 * <p> Description: This class provides the user interface and functionality for creating
 * new help articles within the application. It allows instructors and admins to add new articles
 * with specified details, ensuring proper role-based access and data validation. </p>
 * 
 * @version 1.00  2024-10-29  Initial version.
 */
public class CreateArticlePage {
    private GridPane view;
    private TextField headerField;
    private TextField titleField;
    private TextField shortDescriptionField;
    private TextField keywordsField;
    private TextArea bodyArea;
    private TextField referenceLinksField;
    private ComboBox<Group> groupComboBox;
    private Button createButton;
    private Label messageLabel;
    private HelpArticleDAO helpArticleDAO;
    private GroupDAO groupDAO;

    /**
     * Constructs a CreateArticlePage instance.
     * Initializes UI components and loads necessary data.
     */
    public CreateArticlePage() {
        try {
            helpArticleDAO = new HelpArticleDAO();
            groupDAO = new GroupDAO();
        } catch (SQLException e) {
            showAlert(Alert.AlertType.ERROR, "Database Error", "Failed to load groups.");
            return;
        }

        view = new GridPane();
        view.setPadding(new Insets(20));
        view.setVgap(10);
        view.setHgap(10);

        headerField = new TextField();
        titleField = new TextField();
        shortDescriptionField = new TextField();
        keywordsField = new TextField();
        bodyArea = new TextArea();
        referenceLinksField = new TextField();
        groupComboBox = new ComboBox<>();

        // Populate groupComboBox with groups
        try {
            groupComboBox.getItems().addAll(groupDAO.getAllGroups());
        } catch (SQLException e) {
            showAlert(Alert.AlertType.ERROR, "Database Error", "Failed to load groups.");
        }

        createButton = new Button("Create");
        createButton.setOnAction(e -> handleCreate());

        messageLabel = new Label();

        // Layout Setup
        view.add(new Label("Header:"), 0, 0);
        view.add(headerField, 1, 0);
        view.add(new Label("Title:"), 0, 1);
        view.add(titleField, 1, 1);
        view.add(new Label("Short Description:"), 0, 2);
        view.add(shortDescriptionField, 1, 2);
        view.add(new Label("Keywords:"), 0, 3);
        view.add(keywordsField, 1, 3);
        view.add(new Label("Body:"), 0, 4);
        view.add(bodyArea, 1, 4);
        view.add(new Label("Reference Links:"), 0, 5);
        view.add(referenceLinksField, 1, 5);
        view.add(new Label("Group:"), 0, 6);
        view.add(groupComboBox, 1, 6);
        view.add(createButton, 1, 7);
        view.add(messageLabel, 0, 8, 2, 1);

        // Article Type Selection (General or Special Access)
        ToggleGroup groupToggle = new ToggleGroup();
        RadioButton generalRadio = new RadioButton("General Article");
        generalRadio.setToggleGroup(groupToggle);
        generalRadio.setSelected(true);
        RadioButton specialRadio = new RadioButton("Special Access Group Article");
        specialRadio.setToggleGroup(groupToggle);

        view.add(new Label("Article Type:"), 0, 9);
        view.add(generalRadio, 1, 9);
        view.add(specialRadio, 1, 10);

        // Disable groupComboBox initially
        groupComboBox.setDisable(true);

        // Toggle groupComboBox based on article type selection
        groupToggle.selectedToggleProperty().addListener((obs, oldToggle, newToggle) -> {
            if (newToggle == specialRadio) {
                groupComboBox.setDisable(false);
            } else {
                groupComboBox.setDisable(true);
            }
        });
    }

    /**
     * Returns the view for the CreateArticlePage, which is a GridPane layout.
     * 
     * @return The GridPane layout of the create article page.
     */
    public GridPane getView() {
        return view;
    }

    /**
     * Handles the creation of a new help article based on user input.
     */
    private void handleCreate() {
        String header = headerField.getText().trim();
        String title = titleField.getText().trim();
        String shortDescription = shortDescriptionField.getText().trim();
        String keywords = keywordsField.getText().trim();
        String body = bodyArea.getText().trim();
        String referenceLinks = referenceLinksField.getText().trim();
        Group selectedGroup = groupComboBox.getValue();

        // Validation
        if (header.isEmpty() || title.isEmpty() || shortDescription.isEmpty() || keywords.isEmpty() || body.isEmpty()) {
            messageLabel.setText("All fields except Reference Links are required.");
            return;
        }

        boolean isSpecialAccessGroup = selectedGroup != null;

        // Get current user
        User currentUser = SessionManager.getInstance().getCurrentUser();
        if (currentUser == null) {
            showAlert(Alert.AlertType.ERROR, "Session Error", "No user is currently logged in.");
            return;
        }
        String authorUsername = currentUser.getUsername(); // Removed author handling

        // Create HelpArticle object
        HelpArticle article = new HelpArticle(
                0, header, title, shortDescription,
                Arrays.asList(keywords.split(",")),
                body, Arrays.asList(referenceLinks.split(","))
        );

        try {
            if (isSpecialAccessGroup) {
                // Only admins can create special access group articles
                if (!currentUser.getRoles().contains(Role.ADMIN)) {
                    messageLabel.setText("Only admins can create special access group articles.");
                    return;
                }
            } else {
                // Only admins and instructors can create general articles
                if (!currentUser.getRoles().contains(Role.ADMIN) && !currentUser.getRoles().contains(Role.INSTRUCTOR)) {
                    messageLabel.setText("You do not have permission to create articles.");
                    return;
                }
            }

            // Add article using HelpArticleDAO
            helpArticleDAO.addHelpArticle(article);
            if (isSpecialAccessGroup && selectedGroup != null) {
                helpArticleDAO.associateArticleWithGroup(article.getId(), selectedGroup.getId());
            }
            messageLabel.setText("Article created successfully.");
            clearForm();
        } catch (SQLException e) {
            messageLabel.setText("Failed to create article.");
            e.printStackTrace(); // For debugging purposes
        }
    }

    /**
     * Clears all input fields in the form after successful creation.
     */
    private void clearForm() {
        headerField.clear();
        titleField.clear();
        shortDescriptionField.clear();
        keywordsField.clear();
        bodyArea.clear();
        referenceLinksField.clear();
        groupComboBox.setValue(null);
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
