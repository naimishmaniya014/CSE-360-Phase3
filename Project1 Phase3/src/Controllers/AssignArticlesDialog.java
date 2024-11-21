package Controllers;

import Utilities.HelpArticleDAO;
import models.Group;
import models.HelpArticle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * <p> Title: AssignArticlesDialog Class </p>
 * 
 * <p> Description: This class extends the JavaFX {@link Dialog} to provide a user interface 
 * for assigning multiple help articles to a specific group. It displays a list of all available 
 * help articles, allows users to select multiple articles, and associates the selected articles 
 * with the chosen group in the database. </p>
 * 
 * <p> Usage: Instantiate this dialog by passing a {@link Group} object. Upon confirmation, 
 * the selected articles are returned as a list and can be processed further as needed. </p>
 * 
 * @author Naimish Maniya
 * 
 * <p> @version 1.00  2024-10-29  Initial version. </p>
 */
public class AssignArticlesDialog extends Dialog<List<HelpArticle>> {

    private Group selectedGroup;
    private ListView<HelpArticle> articleListView;
    private ObservableList<HelpArticle> allArticles;
    private HelpArticleDAO helpArticleDAO;

    /**
     * Constructs an AssignArticlesDialog for the specified group.
     * Initializes the dialog's UI components and loads articles.
     *
     * @param group The {@link Group} to which articles will be assigned.
     * @throws SQLException If there is an error accessing the database.
     */
    public AssignArticlesDialog(Group group) throws SQLException {
        this.selectedGroup = group;
        helpArticleDAO = new HelpArticleDAO();

        setTitle("Assign Articles to Group");
        setHeaderText("Select articles to assign to the group: " + group.getName());

        ButtonType assignButtonType = new ButtonType("Assign", ButtonBar.ButtonData.OK_DONE);
        getDialogPane().getButtonTypes().addAll(assignButtonType, ButtonType.CANCEL);

        articleListView = new ListView<>();
        articleListView.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE); 

        allArticles = FXCollections.observableArrayList(helpArticleDAO.getAllHelpArticles(null));
        articleListView.setItems(allArticles);

        List<HelpArticle> assignedArticles = helpArticleDAO.getArticlesByGroupId(group.getId(), false);
        for (HelpArticle article : assignedArticles) {
            articleListView.getSelectionModel().select(article);
        }

        VBox content = new VBox(10);
        content.getChildren().addAll(new Label("Select Articles:"), articleListView);
        content.setPadding(new javafx.geometry.Insets(10));
        getDialogPane().setContent(content);

        setResultConverter(dialogButton -> {
            if (dialogButton == assignButtonType) {
                return new ArrayList<>(articleListView.getSelectionModel().getSelectedItems());
            }
            return null;
        });
    }
}
