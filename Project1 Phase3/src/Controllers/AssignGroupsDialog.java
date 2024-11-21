package Controllers;

import Utilities.GroupDAO;
import Utilities.HelpArticleDAO;
import models.Group;
import models.HelpArticle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.*;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * <p> Title: AssignGroupsDialog Class </p>
 * 
 * <p> Description: This class extends the JavaFX {@link Dialog} to facilitate the association 
 * of multiple groups with a specific help article. It presents a list of all available groups, 
 * allows users to select multiple groups, and links the selected groups to the given help article 
 * in the database. </p>
 * 
 * <p> Usage: Instantiate this dialog by passing a {@link HelpArticle} object. Upon confirmation, 
 * the selected groups are returned as a list for further processing. </p>
 * 
 * @author Naimish Maniya
 * 
 * <p> @version 1.00  2024-10-29  Initial version. </p>
 */
public class AssignGroupsDialog extends Dialog<List<Group>> {

    private HelpArticle selectedArticle;
    private ListView<Group> groupListView;
    private ObservableList<Group> allGroups;
    private GroupDAO groupDAO;
    private HelpArticleDAO helpArticleDAO;

    /**
     * Constructs an AssignGroupsDialog for the specified help article.
     * Initializes the dialog's UI components and loads groups.
     *
     * @param article The {@link HelpArticle} to which groups will be associated.
     * @throws SQLException If there is an error accessing the database.
     */
    public AssignGroupsDialog(HelpArticle article) throws SQLException {
        this.selectedArticle = article;
        groupDAO = new GroupDAO();
        helpArticleDAO = new HelpArticleDAO();

        setTitle("Assign Groups to Article");
        setHeaderText("Select groups to associate with the article: " + article.getTitle());

        ButtonType assignButtonType = new ButtonType("Assign", ButtonBar.ButtonData.OK_DONE);
        getDialogPane().getButtonTypes().addAll(assignButtonType, ButtonType.CANCEL);

        groupListView = new ListView<>();
        groupListView.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        allGroups = FXCollections.observableArrayList();

        List<Group> groups = groupDAO.getAllGroups();
        allGroups.addAll(groups);
        groupListView.setItems(allGroups);

        List<Group> associatedGroups = helpArticleDAO.getGroupsByArticleId(article.getId());
        for (Group group : associatedGroups) {
            groupListView.getSelectionModel().select(group);
        }

        getDialogPane().setContent(groupListView);

        setResultConverter(dialogButton -> {
            if (dialogButton == assignButtonType) {
                return new ArrayList<>(groupListView.getSelectionModel().getSelectedItems());
            }
            return null;
        });
    }
}
