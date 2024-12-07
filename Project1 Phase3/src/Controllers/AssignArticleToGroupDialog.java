package Controllers;

import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import models.HelpArticle;
import Utilities.GroupDAO;
import Utilities.HelpArticleDAO;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public class AssignArticleToGroupDialog extends Dialog<Void> {

    private ComboBox<String> articleComboBox;
    private HelpArticleDAO helpArticleDAO;
    private GroupDAO groupDAO;
    private HelpArticle selectedArticle;
    private long groupId;

    public AssignArticleToGroupDialog(GroupDAO groupDAO, HelpArticleDAO helpArticleDAO, long groupId) {
        this.groupDAO = groupDAO;
        this.helpArticleDAO = helpArticleDAO;
        this.groupId = groupId;

        setTitle("Assign Article to Group");
        setHeaderText("Select an article to assign to the group:");

        ButtonType assignButtonType = new ButtonType("Assign", ButtonBar.ButtonData.OK_DONE);
        getDialogPane().getButtonTypes().addAll(assignButtonType, ButtonType.CANCEL);

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20, 150, 10, 10));

        articleComboBox = new ComboBox<>();
        try {
            List<HelpArticle> articles = helpArticleDAO.getAllHelpArticles(null);
            for (HelpArticle article : articles) {
                articleComboBox.getItems().add(article.getId() + ". " + article.getTitle());
            }
        } catch (SQLException e) {
            // Handle exception
        }

        grid.add(new Label("Article:"), 0, 0);
        grid.add(articleComboBox, 1, 0);

        getDialogPane().setContent(grid);

        setResultConverter(dialogButton -> {
            if (dialogButton == assignButtonType) {
                String selected = articleComboBox.getValue();
                if (selected == null || !selected.matches("^\\d+\\. .*")) {
                    showAlert(Alert.AlertType.WARNING, "No Selection", "Please select an article.");
                    return null;
                }
                long articleId = Long.parseLong(selected.split("\\.")[0]);
                try {
                    selectedArticle = helpArticleDAO.getHelpArticleById(articleId, null);
                } catch (SQLException e) {
                    showAlert(Alert.AlertType.ERROR, "Database Error", "Failed to retrieve article details.");
                    return null;
                }
                return null;
            }
            return null;
        });
    }

    public HelpArticle getSelectedArticle() {
        return selectedArticle;
    }

    private void showAlert(Alert.AlertType type, String title, String content) {
        Alert alert = new Alert(type, content, ButtonType.OK);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.showAndWait();
    }
}
