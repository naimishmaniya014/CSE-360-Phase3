package Controllers;

import Utilities.GroupDAO;
import models.Group;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.*;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * <p> Title: GroupSelectionDialog Class </p>
 * 
 * <p> Description: This class extends the JavaFX {@link Dialog} to provide a user interface 
 * for selecting multiple groups for backup purposes. It displays a list of all available 
 * groups, allows users to select multiple groups, and returns the selected group names 
 * upon confirmation. </p>
 * 
 * <p> Usage: Instantiate this dialog when a user needs to select groups for backup. The 
 * dialog returns a list of selected group names which can then be used for backup operations. 
 * </p>
 * 
 * @author Naimish Maniya
 * 
 * <p> @version 1.00  2024-10-29  Initial version. </p>
 */
public class GroupSelectionDialog extends Dialog<List<String>> {

    private ListView<String> groupListView;
    private ObservableList<String> groupNames;
    private GroupDAO groupDAO;

    /**
     * Constructs a GroupSelectionDialog instance.
     * Initializes the dialog's UI components and loads groups from the database.
     *
     * @throws SQLException If there is an error accessing the database.
     */
    public GroupSelectionDialog() throws SQLException {
        setTitle("Select Groups to Backup");
        setHeaderText("Select groups to include in the backup:");

        ButtonType backupButtonType = new ButtonType("Backup", ButtonBar.ButtonData.OK_DONE);
        getDialogPane().getButtonTypes().addAll(backupButtonType, ButtonType.CANCEL);

        groupListView = new ListView<>();
        groupListView.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        groupNames = FXCollections.observableArrayList();

        groupDAO = new GroupDAO();
        List<Group> groups = groupDAO.getAllGroups();
        for (Group group : groups) {
            groupNames.add(group.getName());
        }
        groupListView.setItems(groupNames);

        getDialogPane().setContent(groupListView);

        setResultConverter(dialogButton -> {
            if (dialogButton == backupButtonType) {
                return new ArrayList<>(groupListView.getSelectionModel().getSelectedItems());
            }
            return null;
        });
    }
}
