package Controllers;

import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import models.HelpArticle;
import models.User;

public class ViewArticleDialog extends Dialog<Void> {
	public ViewArticleDialog(HelpArticle article, User user) {
	    setTitle("View Article");
	    setHeaderText(article.getTitle());

	    ButtonType closeButtonType = new ButtonType("Close", ButtonBar.ButtonData.OK_DONE);
	    getDialogPane().getButtonTypes().addAll(closeButtonType);

	    VBox content = new VBox(10);
	    content.getChildren().addAll(
	            new Label("Header: " + article.getHeader()),
	            new Label("Short Description: " + article.getShortDescription()),
	            new Label("Keywords: " + String.join(", ", article.getKeywords())),
	            new Label("Reference Links: " + String.join(", ", article.getReferenceLinks()))
	    );

	    if (article.getBody() != null) {
	        content.getChildren().addAll(
	                new Label("Body:"),
	                new TextArea(article.getBody()) {{
	                    setWrapText(true);
	                    setEditable(false);
	                    setPrefHeight(200);
	                }}
	        );
	    }

	    getDialogPane().setContent(content);
	}
}
