package Utilities;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;

/**
 * <p> Title: BackupRestoreManagerTest Class </p>
 * 
 * <p> Description: This class provides a set of tests for the {@link BackupRestoreManager} class.
 * It validates the backup and restoration functionalities by performing operations such as backing up
 * all groups, backing up specific groups, and restoring groups from backup files. The test outputs
 * indicate the success or failure of each test case. </p>
 * 
 * @author Naimish Maniya
 * 
 * <p> @version 1.00  2024-10-29  Initial version. </p>
 */
public class BackupRestoreManagerTest {

    /**
     * The main method to execute the BackupRestoreManager tests.
     *
     * @param args Command-line arguments (not used).
     */
    public static void main(String[] args) {
        BackupRestoreManagerTest tester = new BackupRestoreManagerTest();
        tester.runTests();
    }

    /**
     * Executes all test cases for the BackupRestoreManager.
     */
    public void runTests() {
        System.out.println("Running BackupRestoreManager class tests...");

        try {
            BackupRestoreManager manager = new BackupRestoreManager();
            testBackupAll(manager);
            testBackupByGroup(manager);
            testRestore(manager);
        } catch (SQLException e) {
            System.out.println("Failed: Unable to initialize BackupRestoreManager.");
        }
    }

    /**
     * Tests backing up all groups and their associated articles.
     *
     * @param manager The BackupRestoreManager instance to test.
     */
    public void testBackupAll(BackupRestoreManager manager) {
        System.out.println("\nTest: Backup All Groups with Articles");
        try {
            manager.backupAllGroups("backup_all_groups.bak");
            System.out.println("Passed: Backup all groups and articles successfully.");
        } catch (IOException | SQLException e) {
            System.out.println("Failed: Backup all groups and articles failed.");
            e.printStackTrace();
        }
    }

    /**
     * Tests backing up specific groups and their associated articles.
     *
     * @param manager The BackupRestoreManager instance to test.
     */
    public void testBackupByGroup(BackupRestoreManager manager) {
        System.out.println("\nTest: Backup Specific Groups with Verification");
        try {
            List<String> groupsToBackup = Arrays.asList("cs", "ai");
            manager.backupGroups(groupsToBackup, "backup_cs_ai_groups.bak");

            boolean fileExists = Files.exists(Paths.get("backup_cs_ai_groups.bak"));
            if (fileExists) {
                System.out.println("Passed: Backup file created successfully.");
            } else {
                System.out.println("Failed: Backup file not created.");
            }
        } catch (IOException | SQLException e) {
            System.out.println("Failed: Backup specific groups and their articles failed.");
            e.printStackTrace();
        }
    }

    /**
     * Tests restoring groups and their associated articles from a backup file.
     *
     * @param manager The BackupRestoreManager instance to test.
     */
    public void testRestore(BackupRestoreManager manager) {
        System.out.println("\nTest: Restore Groups and Articles with Merge Conflict Handling");
        try {
            manager.restoreGroups("backup_all_groups.bak", false); // Merge mode
            System.out.println("Passed: Restore with merge conflicts resolved successfully.");
        } catch (IOException | SQLException | ClassNotFoundException e) {
            System.out.println("Failed: Restore groups and articles with merge conflicts failed.");
            e.printStackTrace();
        }
    }
}
