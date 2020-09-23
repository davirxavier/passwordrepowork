package category.view.nodes;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXTextField;

import javafx.scene.Cursor;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import res.img.ImagePath;

public class SearchBar extends AnchorPane
{
	private JFXTextField textField;
	private JFXButton searchButton;
	private JFXButton clearButton;
	private JFXComboBox<String> searchOptions;

	public SearchBar()
	{
		init();
	}

	private void init()
	{
		searchOptions = new JFXComboBox<>();
		searchOptions.setVisible(false);
		searchOptions.setFocusTraversable(false);
		searchOptions.setStyle("-jfx-unfocus-color: transparent;" + "-jfx-focus-color: transparent;"
				+ "-fx-background-color: transparent;");
		searchOptions.setCursor(Cursor.HAND);

		clearButton = new JFXButton();
		clearButton.setGraphic(new ImageView(new Image(ImagePath.CLOSE_BLACK18DP.getPath())));
		clearButton.getStyleClass().add("searchBarButton");
		clearButton.setVisible(true);
		clearButton.setFocusTraversable(false);
		clearButton.setStyle("-fx-background-insets: -1 4 0 3;" + "-fx-background-radius: 100;"
				+ "-fx-background-color: transparent;");
		clearButton.setOnAction(e ->
		{
			textField.clear();
		});

		searchButton = new JFXButton();
		searchButton.setGraphic(new ImageView(new Image(ImagePath.SEARCH_BLACK18DP.getPath())));
		searchButton.getStyleClass().add("searchBarButton");
		searchButton.setFocusTraversable(false);
		searchButton.setStyle("-fx-background-insets: -1 4 0 3;" + "-fx-background-radius: 100;"
				+ "-fx-background-color: transparent;");

		textField = new JFXTextField();
		textField.setPrefHeight(28);
		textField.setMaxHeight(28);
		textField.setPromptText("Pesquisar...");
		String unfocusStyle = "-jfx-focus-color: transparent;" + "	-jfx-unfocus-color: transparent;"
				+ "   -fx-prompt-text-fill: gray;" + "	-jfx-label-float: false;" + "	-fx-background-color: white;"
				+ "	-fx-background-insets:  0 0 0 -30;" + "	-fx-background-radius:  1;"
				+ "	-fx-border-insets:  0 0 0 -30;" + "	-fx-border-radius: 1;" + "	-fx-border-color: gray;"
				+ "	-fx-border-width: 0.1;";
		String focusStyle = "-jfx-focus-color: transparent;" + "	-jfx-unfocus-color: transparent;"
				+ "   -fx-prompt-text-fill: gray;" + "	-jfx-label-float: false;" + "	-fx-background-color: white;"
				+ "	-fx-background-insets:  0 0 0 -30;" + "	-fx-background-radius:  1;"
				+ "	-fx-border-insets:  0 0 0 -30;" + "	-fx-border-radius: 1;" + "	-fx-border-color: lightblue;"
				+ "	-fx-border-width: 1;";

		textField.setStyle(unfocusStyle);
		textField.focusedProperty().addListener((l, oldVal, newVal) ->
		{
			if (newVal)
			{
				textField.setStyle(focusStyle);

				// clearButton.setVisible(true);
			} else
			{
				if (searchOptions.isShowing())
				{
					textField.requestFocus();
					return;
				}

				textField.setStyle(unfocusStyle);

//    			if (textField.getText().isEmpty())
//    			{
//    				clearButton.setVisible(false);
//    			}
			}

			// searchOptions.setVisible(newVal);
		});

		searchButton.onActionProperty().bind(textField.onActionProperty());

		getChildren().addAll(textField, clearButton, searchButton, searchOptions);
		textField.setPrefHeight(getHeight());
		heightProperty().addListener((c, oldval, newval) ->
		{
			textField.setPrefHeight((double)newval);
		});

		AnchorPane.setTopAnchor(textField, 0.0);
		AnchorPane.setLeftAnchor(textField, 30.0);
		AnchorPane.setRightAnchor(textField, 0.0);
		AnchorPane.setBottomAnchor(textField, 0.0);

		AnchorPane.setTopAnchor(searchButton, 1.0);
		AnchorPane.setLeftAnchor(searchButton, -1.0);

		AnchorPane.setRightAnchor(clearButton, 2.0);
		AnchorPane.setTopAnchor(clearButton, 1.0);

		AnchorPane.setTopAnchor(searchOptions, 2.0);
		AnchorPane.setRightAnchor(searchOptions, 40.0);
	}

	public JFXTextField getTextField()
	{
		return textField;
	}

	public JFXButton getSearchButton()
	{
		return searchButton;
	}

	public JFXButton getClearButton()
	{
		return clearButton;
	}

	public JFXComboBox<String> getSearchOptions()
	{
		return searchOptions;
	}
}
