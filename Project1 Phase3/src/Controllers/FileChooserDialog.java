package Controllers;

import java.io.File;
import java.util.Optional;

import javafx.stage.FileChooser;

/**
 * <p> Title: FileChooserDialog Class </p>
 * 
 * <p> Description: This utility class provides simplified methods for displaying 
 * file chooser dialogs within the application. It facilitates both saving and opening 
 * files with specified extension filters. This class streamlines the process of 
 * file selection by encapsulating common configurations and behaviors of 
 * JavaFX {@link FileChooser}. </p>
 * 
 * <p> Usage: Instantiate this class with the desired title and extension filter. 
 * Use {@link #showSaveDialog()} to display a save dialog or {@link #showOpenDialog()} 
 * to display an open dialog. The methods return an {@link Optional} containing 
 * the selected file path if a file is chosen, or an empty {@link Optional} if the 
 * operation is canceled. </p>
 * 
 * @author Naimish Maniya
 * 
 * <p> @version 1.00  2024-10-29  Initial version. </p>
 */
public class FileChooserDialog {

    private String title;
    private String extensionFilter;

    /**
     * Constructs a FileChooserDialog with the specified title and extension filter.
     *
     * @param title           The title of the file chooser dialog.
     * @param extensionFilter The file extension filter (e.g., "*.bak").
     */
    public FileChooserDialog(String title, String extensionFilter) {
        this.title = title;
        this.extensionFilter = extensionFilter;
    }

    /**
     * Displays a save dialog and returns the selected file path.
     *
     * @return An {@link Optional} containing the file path as a string if a file is selected, 
     *         or an empty {@link Optional} if the operation is canceled.
     */
    public Optional<String> showSaveDialog() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle(title);
        if (extensionFilter != null && !extensionFilter.isEmpty()) {
            fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Backup Files", extensionFilter));
        }
        File file = fileChooser.showSaveDialog(Main.getStage());
        if (file != null) {
            return Optional.of(file.getAbsolutePath());
        }
        return Optional.empty();
    }

    /**
     * Displays an open dialog and returns the selected file path.
     *
     * @return An {@link Optional} containing the file path as a string if a file is selected, 
     *         or an empty {@link Optional} if the operation is canceled.
     */
    public Optional<String> showOpenDialog() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle(title);
        if (extensionFilter != null && !extensionFilter.isEmpty()) {
            fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Backup Files", extensionFilter));
        }
        File file = fileChooser.showOpenDialog(Main.getStage());
        if (file != null) {
            return Optional.of(file.getAbsolutePath());
        }
        return Optional.empty();
    }
}
