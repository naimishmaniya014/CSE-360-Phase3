package Utilities;

import models.Group;
import models.GroupWithArticles;
import models.HelpArticle;
import models.User;

import java.io.*;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * <p> Title: BackupRestoreManager Class </p>
 * 
 * <p> Description: This class handles the backup and restoration of groups and their associated help articles.
 * It provides methods to backup all groups, backup specific groups, and restore groups from a backup file.
 * The class interacts with the data access objects (DAOs) to perform database operations and utilizes 
 * serialization for backup file handling. </p>
 * 
 * @author Naimish Maniya
 * 
 * <p> @version 1.00  2024-10-29  Initial version. </p>
 */
public class BackupRestoreManager {
    private HelpArticleDAO helpArticleDAO;
    private GroupDAO groupDAO;

    /**
     * Constructs a BackupRestoreManager instance.
     * Initializes the data access objects required for backup and restore operations.
     *
     * @throws SQLException If there is an error accessing the database.
     */
    public BackupRestoreManager() throws SQLException {
        helpArticleDAO = new HelpArticleDAO();
        groupDAO = new GroupDAO();
    }

    /**
     * Backs up all groups along with their associated articles to an external file.
     *
     * @param filePath The path to the backup file.
     * @throws IOException    If file operations fail.
     * @throws SQLException   If database operations fail.
     */
    public void backupAllGroups(String filePath) throws IOException, SQLException {
        List<Group> groups = groupDAO.getAllGroups();
        List<GroupWithArticles> backupData = new ArrayList<>();

        for (Group group : groups) {
            List<HelpArticle> articles = helpArticleDAO.getArticlesByGroupId(group.getId(), false);
            backupData.add(new GroupWithArticles(group, articles));
        }

        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(filePath))) {
            oos.writeObject(backupData);
        }
    }

    /**
     * Backs up specific groups along with their associated articles.
     *
     * @param groupNames The names of the groups to back up.
     * @param filePath   The path to the backup file.
     * @throws IOException    If file operations fail.
     * @throws SQLException   If database operations fail.
     */
    public void backupGroups(List<String> groupNames, String filePath) throws IOException, SQLException {
        List<GroupWithArticles> backupData = new ArrayList<>();

        for (String groupName : groupNames) {
            Group group = groupDAO.getGroupByName(groupName);
            if (group != null) {
                List<HelpArticle> articles = helpArticleDAO.getArticlesByGroupId(group.getId(), false);
                backupData.add(new GroupWithArticles(group, articles));
            }
        }

        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(filePath))) {
            oos.writeObject(backupData);
        }
    }

    /**
     * Restores groups and their associated articles from a backup file.
     *
     * @param filePath         The path to the backup file.
     * @param removeExisting   Whether to remove existing groups and articles before restoring.
     * @throws IOException             If file operations fail.
     * @throws SQLException            If database operations fail.
     * @throws ClassNotFoundException  If deserialization fails.
     */
    @SuppressWarnings("unchecked")
    public void restoreGroups(String filePath, boolean removeExisting) throws IOException, SQLException, ClassNotFoundException {
        List<GroupWithArticles> backupData;
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(filePath))) {
            backupData = (List<GroupWithArticles>) ois.readObject();
        }

        if (removeExisting) {
            helpArticleDAO.clearAllAssociations();
            groupDAO.deleteAllGroups();
            helpArticleDAO.deleteAllHelpArticles(); 
        }

        for (GroupWithArticles gwa : backupData) {
            Group group = gwa.getGroup();
            Group existingGroup = groupDAO.getGroupByName(group.getName());
            if (existingGroup == null) {
                groupDAO.addGroup(group);
                existingGroup = groupDAO.getGroupByName(group.getName());
            }

            for (HelpArticle article : gwa.getArticles()) {
                HelpArticle existingArticle = helpArticleDAO.getHelpArticleById(article.getId(), null);
                if (existingArticle == null) {
                	helpArticleDAO.addHelpArticle(article);
                }
                // Associate article with group
                helpArticleDAO.associateArticleWithGroup(article.getId(), existingGroup.getId());
            }
        }
    }
}
