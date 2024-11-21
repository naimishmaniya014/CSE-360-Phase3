package Controllers;

import Utilities.HelpArticleDAO;
import Utilities.SessionManager;
import Utilities.GroupDAO;
import models.HelpArticle;
import models.Role;
import models.Group;
import models.User;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;

import java.sql.SQLException;
import java.util.Arrays;

public class EditArticleDialog extends Dialog<Void> {
    private HelpArticle article;
    private HelpArticleDAO helpArticleDAO;
    private GroupDAO groupDAO;

    private TextField headerField;
    private TextField titleField;
    private TextField shortDescriptionField;
    private TextField keywordsField;
    private TextArea bodyArea;
    private TextField referenceLinksField;
    private Label messageLabel;

    public EditArticleDialog(HelpArticle article) {
        this.article = article;
        try {
            helpArticleDAO = new HelpArticleDAO();
            groupDAO = new GroupDAO();
        } catch (SQLException e) {
            showAlert(Alert.AlertType.ERROR, "Database Error", "Failed to connect to the database.");
            return;
        }

        setTitle("Edit Article");
        setHeaderText("Edit the selected article.");

        ButtonType saveButtonType = new ButtonType("Save", ButtonBar.ButtonData.OK_DONE);
        getDialogPane().getButtonTypes().addAll(saveButtonType, ButtonType.CANCEL);

        GridPane grid = new GridPane();
        grid.setVgap(10);
        grid.setHgap(10);

        headerField = new TextField(article.getHeader());
        titleField = new TextField(article.getTitle());
        shortDescriptionField = new TextField(article.getShortDescription());
        keywordsField = new TextField(String.join(",", article.getKeywords()));
        bodyArea = new TextArea(article.getBody());
        referenceLinksField = new TextField(String.join(",", article.getReferenceLinks()));
        messageLabel = new Label();

        grid.add(new Label("Header:"), 0, 0);
        grid.add(headerField, 1, 0);
        grid.add(new Label("Title:"), 0, 1);
        grid.add(titleField, 1, 1);
        grid.add(new Label("Short Description:"), 0, 2);
        grid.add(shortDescriptionField, 1, 2);
        grid.add(new Label("Keywords:"), 0, 3);
        grid.add(keywordsField, 1, 3);
        grid.add(new Label("Body:"), 0, 4);
        grid.add(bodyArea, 1, 4);
        grid.add(new Label("Reference Links:"), 0, 5);
        grid.add(referenceLinksField, 1, 5);
        grid.add(messageLabel, 0, 6, 2, 1);

        getDialogPane().setContent(grid);

        User currentUser = SessionManager.getInstance().getCurrentUser();
        if (!currentUser.getRoles().contains(Role.INSTRUCTOR)) {
            bodyArea.setEditable(false);
            bodyArea.setDisable(true);
        }

        setResultConverter(dialogButton -> {
            if (dialogButton == saveButtonType) {
                handleSave();
            }
            return null;
        });
    }



    private void handleSave() {
        User currentUser = SessionManager.getInstance().getCurrentUser();
        if (!currentUser.getRoles().contains(Role.INSTRUCTOR)) {
            messageLabel.setText("Only instructors can edit articles.");
            return;
        }

        String header = headerField.getText().trim();
        String title = titleField.getText().trim();
        String shortDescription = shortDescriptionField.getText().trim();
        String keywords = keywordsField.getText().trim();
        String body = bodyArea.getText().trim();
        String referenceLinks = referenceLinksField.getText().trim();

        if (header.isEmpty() || title.isEmpty() || shortDescription.isEmpty() || keywords.isEmpty() || body.isEmpty()) {
            messageLabel.setText("All fields except Reference Links are required.");
            return;
        }

        article.setHeader(header);
        article.setTitle(title);
        article.setShortDescription(shortDescription);
        article.setKeywords(Arrays.asList(keywords.split(",")));
        article.setBody(body);
        article.setReferenceLinks(Arrays.asList(referenceLinks.split(",")));

        try {
            Long groupId = helpArticleDAO.getGroupIdByArticleId(article.getId());
            Group group = groupId != null ? groupDAO.getGroupById(groupId) : null;
            boolean isSpecialAccessGroup = group != null && group.isSpecialAccessGroup();
            helpArticleDAO.updateHelpArticle(article);
            messageLabel.setText("Article updated successfully.");
        } catch (SQLException e) {
            messageLabel.setText("Failed to update article.");
        }
    }

    private void showAlert(Alert.AlertType type, String title, String content) {
        Alert alert = new Alert(type, content, ButtonType.OK);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.showAndWait();
    }
}
