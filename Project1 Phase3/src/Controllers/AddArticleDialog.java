package Controllers;

import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import models.HelpArticle;
import Utilities.HelpArticleDAO;

import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public class AddArticleDialog extends Dialog<HelpArticle> {

    private TextField headerField;
    private TextField titleField;
    private TextArea shortDescriptionArea;
    private TextArea bodyArea;
    private TextField keywordsField;
    private TextField referenceLinksField;

    public AddArticleDialog() {
        setTitle("Add New Article");
        setHeaderText("Enter the details of the new help article:");

        ButtonType addButtonType = new ButtonType("Add", ButtonBar.ButtonData.OK_DONE);
        getDialogPane().getButtonTypes().addAll(addButtonType, ButtonType.CANCEL);

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20, 150, 10, 10));

        headerField = new TextField();
        headerField.setPromptText("Header");

        titleField = new TextField();
        titleField.setPromptText("Title");

        shortDescriptionArea = new TextArea();
        shortDescriptionArea.setPromptText("Short Description");
        shortDescriptionArea.setPrefRowCount(3);

        bodyArea = new TextArea();
        bodyArea.setPromptText("Body");
        bodyArea.setPrefRowCount(10);

        keywordsField = new TextField();
        keywordsField.setPromptText("Keywords (comma-separated)");

        referenceLinksField = new TextField();
        referenceLinksField.setPromptText("Reference Links (comma-separated)");

        grid.add(new Label("Header:"), 0, 0);
        grid.add(headerField, 1, 0);
        grid.add(new Label("Title:"), 0, 1);
        grid.add(titleField, 1, 1);
        grid.add(new Label("Short Description:"), 0, 2);
        grid.add(shortDescriptionArea, 1, 2);
        grid.add(new Label("Body:"), 0, 3);
        grid.add(bodyArea, 1, 3);
        grid.add(new Label("Keywords:"), 0, 4);
        grid.add(keywordsField, 1, 4);
        grid.add(new Label("Reference Links:"), 0, 5);
        grid.add(referenceLinksField, 1, 5);

        getDialogPane().setContent(grid);

        setResultConverter(dialogButton -> {
            if (dialogButton == addButtonType) {
                String header = headerField.getText().trim();
                String title = titleField.getText().trim();
                String shortDescription = shortDescriptionArea.getText().trim();
                String body = bodyArea.getText().trim();
                String keywords = keywordsField.getText().trim();
                String referenceLinks = referenceLinksField.getText().trim();

                if (title.isEmpty()) {
                    showAlert(Alert.AlertType.WARNING, "Input Required", "Title cannot be empty.");
                    return null;
                }

                HelpArticle article = new HelpArticle();
                article.setHeader(header);
                article.setTitle(title);
                article.setShortDescription(shortDescription);
                article.setBody(body);
                if (!keywords.isEmpty()) {
                    article.setKeywords(Arrays.asList(keywords.split(",")));
                }
                if (!referenceLinks.isEmpty()) {
                    article.setReferenceLinks(Arrays.asList(referenceLinks.split(",")));
                }
                return article;
            }
            return null;
        });
    }

    private void showAlert(Alert.AlertType type, String title, String content) {
        Alert alert = new Alert(type, content, ButtonType.OK);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.showAndWait();
    }
}
