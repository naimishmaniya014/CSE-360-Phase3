package Controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import models.HelpArticle;
import Utilities.GroupDAO;
import Utilities.HelpArticleDAO;

import java.sql.SQLException;
import java.util.List;

public class ViewGroupArticlesDialog extends Dialog<Void> {

    private GroupDAO groupDAO;
    private HelpArticleDAO helpArticleDAO;
    private long groupId;
    private TableView<HelpArticle> articlesTableView;
    private ObservableList<HelpArticle> articlesList;

    public ViewGroupArticlesDialog(GroupDAO groupDAO, HelpArticleDAO helpArticleDAO, long groupId) {
        this.groupDAO = groupDAO;
        this.helpArticleDAO = helpArticleDAO;
        this.groupId = groupId;

        setTitle("Articles in Group");
        setHeaderText("Articles assigned to the selected group:");

        ButtonType closeButtonType = new ButtonType("Close", ButtonBar.ButtonData.OK_DONE);
        getDialogPane().getButtonTypes().addAll(closeButtonType);

        VBox content = new VBox(10);
        content.setPadding(new Insets(20));

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

        content.getChildren().addAll(articlesTableView);
        getDialogPane().setContent(content);

        loadGroupArticles();
    }

    private void loadGroupArticles() {
        try {
            List<HelpArticle> articles = helpArticleDAO.getArticlesByGroup(groupId);
            articlesList.setAll(articles);
        } catch (SQLException e) {
            showAlert(Alert.AlertType.ERROR, "Database Error", "Failed to load articles for the group.");
        }
    }

    private void showAlert(Alert.AlertType type, String title, String content) {
        Alert alert = new Alert(type, content, ButtonType.OK);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.showAndWait();
    }
}
