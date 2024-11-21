package Controllers;

import Utilities.GroupDAO;
import Utilities.HelpArticleDAO;
import models.HelpArticle;
import models.Group;
import models.User;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;

import java.sql.SQLException;
import java.util.Arrays;

public class EditArticlePage {
    private GridPane view;
    private TextField headerField;
    private TextField titleField;
    private TextField shortDescriptionField;
    private TextField keywordsField;
    private TextArea bodyArea;
    private TextField referenceLinksField;
    private Button saveButton;
    private Label messageLabel;
    private HelpArticle article;
    private HelpArticleDAO helpArticleDAO;
    private GroupDAO groupDAO;

    public EditArticlePage(HelpArticle article) {
        this.article = article;
        try {
            helpArticleDAO = new HelpArticleDAO();
            groupDAO = new GroupDAO();
        } catch (SQLException e) {
            showAlert(Alert.AlertType.ERROR, "Database Error", "Failed to connect to the database.");
            return;
        }

        view = new GridPane();
        view.setPadding(new Insets(20));
        view.setVgap(10);
        view.setHgap(10);

        headerField = new TextField(article.getHeader());
        titleField = new TextField(article.getTitle());
        shortDescriptionField = new TextField(article.getShortDescription());
        keywordsField = new TextField(String.join(",", article.getKeywords()));
        bodyArea = new TextArea(article.getBody());
        referenceLinksField = new TextField(String.join(",", article.getReferenceLinks()));

        saveButton = new Button("Save");
        saveButton.setOnAction(e -> handleSave());

        messageLabel = new Label();

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
        view.add(saveButton, 1, 6);
        view.add(messageLabel, 0, 7, 2, 1);
    }

    public GridPane getView() {
        return view;
    }

    private void handleSave() {
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
