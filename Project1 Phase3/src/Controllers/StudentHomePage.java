package Controllers;

import Utilities.GroupDAO;
import Utilities.HelpArticleDAO;
import Utilities.SearchRequestDAO;
import models.HelpArticle;
import models.SearchRequest;
import models.Role;
import models.Group;
import models.User;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;

import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Arrays;
import javafx.util.Pair;
import java.util.Optional;

public class StudentHomePage {
    private VBox view;
    private ComboBox<String> contentLevelComboBox;
    private ComboBox<String> groupComboBox;
    private TextField searchField;
    private Button searchButton;
    private Button viewArticleButton;
    private Button quitButton;
    private Button sendGenericMessageButton;
    private Button sendSpecificMessageButton;
    private ListView<String> searchResultsListView;
    private ObservableList<String> searchResults;
    private HelpArticleDAO helpArticleDAO;
    private SearchRequestDAO searchRequestDAO;
    private List<HelpArticle> currentSearchResults;
    private GroupDAO groupDAO;

    public StudentHomePage(User user) {
        view = new VBox(10);
        view.setPadding(new Insets(20));

        Label welcomeLabel = new Label("Welcome, " + user.getPreferredName() + " (Student)");
        view.getChildren().add(welcomeLabel);

        GridPane searchPane = new GridPane();
        searchPane.setVgap(10);
        searchPane.setHgap(10);

        contentLevelComboBox = new ComboBox<>();
        contentLevelComboBox.getItems().addAll("all", "beginner", "intermediate", "advanced", "expert");
        contentLevelComboBox.setValue("all");

        groupComboBox = new ComboBox<>();
        try {
            groupComboBox.getItems().add("all");
            groupComboBox.getItems().addAll(new GroupDAO().getAllGroups().stream().map(Group::getName).toList());
            groupComboBox.setValue("all");
        } catch (SQLException e) {
            showAlert(Alert.AlertType.ERROR, "Database Error", "Failed to load groups.");
        }

        searchField = new TextField();
        searchField.setPromptText("Enter search query");

        searchButton = new Button("Search");
//        searchButton.setOnAction(e -> handleSearch(user));

        searchPane.add(new Label("Content Level:"), 0, 0);
        searchPane.add(contentLevelComboBox, 1, 0);
        searchPane.add(new Label("Group:"), 0, 1);
        searchPane.add(groupComboBox, 1, 1);
        searchPane.add(new Label("Search:"), 0, 2);
        searchPane.add(searchField, 1, 2);
        searchPane.add(searchButton, 2, 2);

        view.getChildren().add(searchPane);

        searchResultsListView = new ListView<>();
        searchResults = FXCollections.observableArrayList();
        searchResultsListView.setItems(searchResults);
        view.getChildren().add(searchResultsListView);

        GridPane actionsPane = new GridPane();
        actionsPane.setVgap(10);
        actionsPane.setHgap(10);

        viewArticleButton = new Button("View Article");
        viewArticleButton.setOnAction(e -> handleViewArticle(user));

        quitButton = new Button("Quit");
        quitButton.setOnAction(e -> {
            System.exit(0);
        });

        sendGenericMessageButton = new Button("Send Generic Message");
        sendGenericMessageButton.setOnAction(e -> handleSendGenericMessage(user));

        sendSpecificMessageButton = new Button("Send Specific Message");
        sendSpecificMessageButton.setOnAction(e -> handleSendSpecificMessage(user));

        actionsPane.add(viewArticleButton, 0, 0);
        actionsPane.add(quitButton, 1, 0);
        actionsPane.add(sendGenericMessageButton, 0, 1);
        actionsPane.add(sendSpecificMessageButton, 1, 1);

        view.getChildren().add(actionsPane);

        try {
            helpArticleDAO = new HelpArticleDAO();
            searchRequestDAO = new SearchRequestDAO();
            groupDAO = new GroupDAO();
        } catch (SQLException e) {
            showAlert(Alert.AlertType.ERROR, "Database Error", "Failed to initialize DAOs.");
        }
    }

    public VBox getView() {
        return view;
    }

//    private void handleSearch(User user) {
//        String query = searchField.getText().trim();
//        String contentLevel = contentLevelComboBox.getValue();
//        String groupName = groupComboBox.getValue();
//
//        if (query.isEmpty()) {
//            showAlert(Alert.AlertType.WARNING, "Input Required", "Please enter a search query.");
//            return;
//        }
//
//        try {
//            List<HelpArticle> articles = helpArticleDAO.searchHelpArticles(user, query, contentLevel, groupName);
//            currentSearchResults = articles;
//            searchResults.clear();
//
//            long beginner = articles.stream().filter(a -> a.getContentLevel().equalsIgnoreCase("beginner")).count();
//            long intermediate = articles.stream().filter(a -> a.getContentLevel().equalsIgnoreCase("intermediate")).count();
//            long advanced = articles.stream().filter(a -> a.getContentLevel().equalsIgnoreCase("advanced")).count();
//            long expert = articles.stream().filter(a -> a.getContentLevel().equalsIgnoreCase("expert")).count();
//
//            searchResults.add("Active Group: " + groupName);
//            searchResults.add("Beginner: " + beginner);
//            searchResults.add("Intermediate: " + intermediate);
//            searchResults.add("Advanced: " + advanced);
//            searchResults.add("Expert: " + expert);
//            searchResults.add("Search Results:");
//
//            for (int i = 0; i < articles.size(); i++) {
//                HelpArticle article = articles.get(i);
//                String display = String.format("%d. %s by %s - %s", 
//                    i + 1, article.getTitle(), article.getAuthor(), article.getShortDescription());
//                searchResults.add(display);
//            }
//
//            SearchRequest request = new SearchRequest(user.getUsername(), query, LocalDateTime.now());
//            searchRequestDAO.addSearchRequest(request);
//
//        } catch (SQLException e) {
//            showAlert(Alert.AlertType.ERROR, "Database Error", "Failed to perform search.");
//        }
//    }

    private void handleViewArticle(User user) {
        String selected = searchResultsListView.getSelectionModel().getSelectedItem();
        if (selected == null || !selected.matches("^\\d+\\. .*")) {
            showAlert(Alert.AlertType.WARNING, "No Selection", "Please select an article to view.");
            return;
        }
        int sequenceNumber = Integer.parseInt(selected.split("\\.")[0]) - 1;
        if (sequenceNumber < 0 || sequenceNumber >= currentSearchResults.size()) {
            showAlert(Alert.AlertType.ERROR, "Invalid Selection", "Invalid article selection.");
            return;
        }
        HelpArticle article = currentSearchResults.get(sequenceNumber);
        try {
            HelpArticle fullArticle = helpArticleDAO.getHelpArticleById(article.getId(), user);
            if (fullArticle != null && fullArticle.getBody() != null) {
                ViewArticleDialog dialog = new ViewArticleDialog(fullArticle, user);
                dialog.showAndWait();
            } else {
                showAlert(Alert.AlertType.ERROR, "Access Denied", "You do not have permission to view this article.");
            }
        } catch (SQLException e) {
            showAlert(Alert.AlertType.ERROR, "Database Error", "Failed to retrieve article.");
        }
    }

    private void handleSendGenericMessage(User user) {
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Send Generic Message");
        dialog.setHeaderText("Express confusion about using the tool.");
        dialog.setContentText("Message:");
        dialog.showAndWait().ifPresent(message -> {
            System.out.println("Generic message from " + user.getUsername() + ": " + message);
            showAlert(Alert.AlertType.INFORMATION, "Message Sent", "Your message has been sent.");
        });
    }

    private void handleSendSpecificMessage(User user) {
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Send Specific Message");
        dialog.setHeaderText("Specify what you need and cannot find.");
        dialog.setContentText("Message:");
        dialog.showAndWait().ifPresent(message -> {
            try {
                SearchRequest request = new SearchRequest(user.getUsername(), message, LocalDateTime.now());
                searchRequestDAO.addSearchRequest(request);
                showAlert(Alert.AlertType.INFORMATION, "Message Sent", "Your specific request has been sent.");
            } catch (SQLException e) {
                showAlert(Alert.AlertType.ERROR, "Database Error", "Failed to send message.");
            }
        });
    }

    private void showAlert(Alert.AlertType type, String title, String content) {
        Alert alert = new Alert(type, content, ButtonType.OK);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.showAndWait();
    }
}
