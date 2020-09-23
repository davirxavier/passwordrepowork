package category.view;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXListCell;
import com.jfoenix.controls.JFXListView;
import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXSpinner;
import com.jfoenix.controls.JFXTextField;

import category.CategoryHandlerResponse;
import category.CategoryInputHandler;
import category.IView;
import category.view.nodes.CategoryListCell;
import category.view.nodes.FlatJFXDialog;
import category.view.nodes.SearchBar;
import exceptions.database.IncorrectSecretException;
import javafx.animation.FadeTransition;
import javafx.animation.PathTransition;
import javafx.animation.SequentialTransition;
import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Bounds;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.util.Callback;
import javafx.util.Duration;

/**
 * View para categorias e passwords em interface gráfica.
 * 
 * @author Davi
 */
public class CategoryViewGraphical implements IView
{
	private CategoryInputHandler inputHandler;
	private List<CategoryHandlerResponse> lastEntries;

	@FXML
	public AnchorPane primaryPane;

	@FXML
	public StackPane loginPane;

	@FXML
	public VBox loginInnerPane;

	@FXML
	public ImageView keyImg;

	@FXML
	private JFXPasswordField loginPasswordField;

	@FXML
	private JFXButton loginButton;

	@FXML
	private Label loginLabelError;

	@FXML
	private StackPane passwordsPane;

	@FXML
	private AnchorPane categoryListPane;

	@FXML
	private JFXListView<CategoryHandlerResponse> categoriesListView;

	@FXML
	private AnchorPane leftPasswordsPane;

	public CategoryViewGraphical()
	{
		lastEntries = new ArrayList<CategoryHandlerResponse>();
	}

	@FXML
	void initialize()
	{
		categoriesListView
				.setCellFactory(new Callback<ListView<CategoryHandlerResponse>, ListCell<CategoryHandlerResponse>>()
				{
					@Override
					public ListCell<CategoryHandlerResponse> call(ListView<CategoryHandlerResponse> param)
					{
						CategoryListCell cell = new CategoryListCell();
						cell.setInputHandler(inputHandler);

						return cell;
					}
				});

		SearchBar searchBar = new SearchBar();
		searchBar.setPrefHeight(30);
		searchBar.getTextField().setPromptText("Search...");
		SimpleStringProperty lastSearch = new SimpleStringProperty("");
		searchBar.getTextField().setOnAction(e ->
		{
			if (searchBar.getTextField().getText().length() < lastSearch.get().length())
			{
				try
				{
					inputHandler.handleRequestUpdate();
				} catch (Exception e1)
				{
					e1.printStackTrace();
				}
			}

			Platform.runLater(() ->
			{
				List<CategoryHandlerResponse> nodes = categoriesListView.getItems().filtered(node -> node
						.getCategoryName().toLowerCase().contains(searchBar.getTextField().getText().toLowerCase()));
				categoriesListView.setItems(FXCollections.observableArrayList(nodes));
				categoriesListView.refresh();

				lastSearch.set(searchBar.getTextField().getText());
			});
		});
		searchBar.getClearButton().setOnAction(e ->
		{
			searchBar.getTextField().clear();
			try
			{
				inputHandler.handleRequestUpdate();
			} catch (Exception e1)
			{
				e1.printStackTrace();
			}

			categoriesListView.refresh();
		});

		categoryListPane.getChildren().add(searchBar);
		AnchorPane.setTopAnchor(searchBar, 0.0);
		AnchorPane.setLeftAnchor(searchBar, 0.0);
		AnchorPane.setRightAnchor(searchBar, 0.0);
	}

	@FXML
	void login(ActionEvent event)
	{
		char[] secret = loginPasswordField.getText().toCharArray();

		loginPane.setDisable(true);
		JFXSpinner spinner = new JFXSpinner();
		spinner.setMaxHeight(80);
		spinner.setMaxWidth(80);
		loginPane.getChildren().add(spinner);
		new Thread(() ->
		{
			try
			{
				boolean check = inputHandler.handleCheckSecret(secret);
				inputHandler.handleRequestUpdate();

				Platform.runLater(() ->
				{
					if (check)
					{
						loginLabelError.setVisible(false);
						loginPane.setDisable(true);
						passwordsPane.setVisible(true);
						loginPane.getChildren().remove(spinner);

						playLoginAnimation();
					} else
					{
						loginPane.setDisable(false);
						loginPane.getChildren().remove(spinner);
						loginLabelError.setText("Senha mestra incorreta.");
						loginLabelError.setVisible(true);
					}
				});
			} catch (IncorrectSecretException e)
			{
				Platform.runLater(() ->
				{
					loginLabelError.setText("Senha mestra incorreta.");
					loginLabelError.setVisible(true);
				});
			} catch (Exception e)
			{
				e.printStackTrace();
			}
			finally
			{
				Arrays.fill(secret, (char)0);
				loginPasswordField.clear();
			}
		}).start();
	}

	private void playLoginAnimation()
	{
		FadeTransition fadeTransition = new FadeTransition();
		fadeTransition.setNode(loginInnerPane);
		fadeTransition.setDuration(Duration.seconds(0.17));
		fadeTransition.setFromValue(1);
		fadeTransition.setToValue(0);

		PathTransition pathTransition = new PathTransition();
		pathTransition.setNode(loginPane);
		pathTransition.setDuration(Duration.seconds(0.4));

		Line line = new Line();
		Bounds paneBounds = loginPane.localToScene(loginPane.getBoundsInLocal());
		line.setStartX(paneBounds.getMinX() + paneBounds.getWidth() / 2);
		line.setStartY(paneBounds.getMinY() + paneBounds.getHeight() / 2);
		line.setEndX(0 - paneBounds.getWidth() / 2);
		line.setEndY(line.getStartY());

		pathTransition.setPath(line);

		FadeTransition fadeTransition2 = new FadeTransition();
		fadeTransition2.setNode(leftPasswordsPane);
		fadeTransition2.setDuration(Duration.seconds(0.10));
		fadeTransition2.setFromValue(0);
		fadeTransition2.setToValue(1);

		SequentialTransition transition = new SequentialTransition(fadeTransition, pathTransition, fadeTransition2);
		transition.setOnFinished(e ->
		{
			loginPane.setVisible(false);
		});
		transition.play();
	}

	@FXML
	void logout(ActionEvent event)
	{
		categoriesListView.getItems().clear();
		categoriesListView.refresh();

		loginPane.setDisable(false);
		loginPane.setVisible(true);
		playLogoutAnimation();
	}

	private void playLogoutAnimation()
	{
		FadeTransition fadeTransition = new FadeTransition();
		fadeTransition.setNode(loginInnerPane);
		fadeTransition.setDuration(Duration.seconds(0.17));
		fadeTransition.setFromValue(0);
		fadeTransition.setToValue(1);

		PathTransition pathTransition = new PathTransition();
		pathTransition.setNode(loginPane);
		pathTransition.setDuration(Duration.seconds(0.4));

		Line line = new Line();
		Bounds paneBounds = loginPane.localToScene(loginPane.getBoundsInLocal());
		line.setEndX(loginPane.getScene().getWidth()/2);
		line.setEndY(paneBounds.getMinY() + paneBounds.getHeight() / 2);
		line.setStartX(0 - paneBounds.getWidth() / 2);
		line.setStartY(line.getEndY());

		pathTransition.setPath(line);

		FadeTransition fadeTransition2 = new FadeTransition();
		fadeTransition2.setNode(leftPasswordsPane);
		fadeTransition2.setDuration(Duration.seconds(0.10));
		fadeTransition2.setFromValue(1);
		fadeTransition2.setToValue(0);

		SequentialTransition transition = new SequentialTransition(fadeTransition2, pathTransition, fadeTransition);
		transition.play();
	}

	@FXML
	void newCategory(ActionEvent event)
	{
		JFXButton buttonYes = new JFXButton("Confirm");
		JFXButton buttonNo = new JFXButton("Close");
		buttonNo.getStyleClass().add("button-dark");
		buttonYes.getStyleClass().addAll(buttonNo.getStyleClass());

		FlatJFXDialog dialog = new FlatJFXDialog(passwordsPane, "New Category", "", buttonNo, buttonYes);

		Label nameLabel = new Label("Insert a name for the Category (50 characters): ");
		JFXTextField nameField = new JFXTextField();
		nameField.setPromptText("Name");
		nameField.getStyleClass().add("textfield-light");
		nameField.textProperty().addListener((c, oldval, newval) ->
		{
			if (newval.length() > 50)
			{
				nameField.setText(oldval);
			}
		});

		Label passLabel = new Label("Insert your master password:");
		JFXPasswordField passField = new JFXPasswordField();
		passField.setPromptText("Master password");
		passField.getStyleClass().add("textfield-light");

		nameLabel.setStyle("-fx-font-size: 12px;");
		passLabel.setStyle(nameLabel.getStyle());

		VBox vBox = new VBox(nameLabel, nameField, passLabel, passField);
		vBox.setSpacing(8);

		buttonYes.setOnAction(e ->
		{
			Platform.runLater(() ->
			{
				dialog.getLayout().getBody().clear();
				dialog.setHeader("Processing...");
				dialog.getLayout().setBody(new JFXSpinner());
			});

			new Thread(() ->
			{
				try
				{
					inputHandler.handleNewCategory(nameField.getText(), passField.getText().toCharArray());

					Platform.runLater(() ->
					{
						dialog.setHeader("Success");
						dialog.setBody("Category inserted successfully.");

						buttonYes.setVisible(false);
					});
				} catch (IncorrectSecretException incorrectE)
				{
					incorrectE.printStackTrace();
					Platform.runLater(() ->
					{
						dialog.setHeader("Errorr");
						dialog.setBody("Incorrect master password, try again.");

						buttonYes.setVisible(false);
					});
				} catch (Exception e1)
				{
					e1.printStackTrace();
					dialog.getDialog().close();
				} finally
				{
					passField.clear();
				}
			}).start();
		});
		buttonNo.setOnAction(e ->
		{
			dialog.getDialog().close();
		});

		dialog.getLayout().setBody(vBox);
		dialog.getDialog().show();
	}

	@FXML
	void newPassword(ActionEvent event)
	{
		JFXButton buttonYes = new JFXButton("Confirm");
		JFXButton buttonNo = new JFXButton("Cancel");
		buttonYes.getStyleClass().add("button-dark");
		buttonNo.getStyleClass().add("button-dark");

		FlatJFXDialog dialog = new FlatJFXDialog(passwordsPane, "New Password", "", buttonNo, buttonYes);

		Label descLabel = new Label("Description (100 characters): ");
		JFXTextField descField = new JFXTextField();
		descField.setPromptText("Description");
		descField.textProperty().addListener((c, oldval, newval) ->
		{
			if (newval.length() > 100)
			{
				descField.setText(oldval);
			}
		});

		Label usernameLabel = new Label("Username (100 characters): ");
		JFXTextField userField = new JFXTextField();
		userField.setPromptText("Username");
		userField.textProperty().addListener((c, oldval, newval) ->
		{
			if (newval.length() > 100)
			{
				descField.setText(oldval);
			}
		});

		Label passLabel = new Label("Password to be encrypted: ");
		JFXTextField passField = new JFXTextField();
		passField.setPromptText("Password");

		Label secretLabel = new Label("Your master password: ");
		JFXPasswordField secretField = new JFXPasswordField();
		secretField.setPromptText("Master password");

		Label errorLabel = new Label("Error");
		errorLabel.setStyle("-fx-font-size: 12px;");
		errorLabel.setVisible(false);
		errorLabel.setTextFill(Color.RED);

		descLabel.setStyle("-fx-font-size: 12px; -fx-padding: 0 0 -8 0;");
		usernameLabel.styleProperty().bind(descLabel.styleProperty());
		passLabel.styleProperty().bind(descLabel.styleProperty());
		secretLabel.styleProperty().bind(descLabel.styleProperty());

		Label catLabel = new Label("Select a category to add the password to:");
		catLabel.styleProperty().bind(descLabel.styleProperty());
		JFXListView<CategoryHandlerResponse> catList = new JFXListView<>();
		catList.setMinHeight(120);
		catList.getStyleClass().add("select-jfx-list");
		catList.getItems().addAll(lastEntries);
		catList.setCellFactory(lv -> new JFXListCell<CategoryHandlerResponse>()
		{
			@Override
			protected void updateItem(CategoryHandlerResponse item, boolean empty)
			{
				super.updateItem(item, empty);
				if (empty || item == null)
				{
					setText("");
				} else
				{
					setText(item.getCategoryName());
				}
			}
		});

		buttonYes.setOnAction(e ->
		{
			try
			{
				char[] secret = secretField.getText().toCharArray();
				boolean check = inputHandler.handleCheckSecret(secret);

				if (descField.getText().isEmpty() || userField.getText().isEmpty() || passField.getText().isEmpty())
				{
					errorLabel.setVisible(true);
					errorLabel.setText("Empty field detected. Please fill out every field.");
					return;
				}
				if (!check)
				{
					errorLabel.setVisible(true);
					errorLabel.setText("Incorrect master password, try again.");
					return;
				}
				int catpos = catList.getSelectionModel().getSelectedItem().getCatId();
				if (catpos == -1)
				{
					errorLabel.setVisible(true);
					errorLabel.setText("Please select one category from the list.");
					return;
				}

				dialog.setHeader("Processing...");
				dialog.getLayout().getBody().clear();
				dialog.getLayout().setBody(new JFXSpinner());

				new Thread(() ->
				{
					errorLabel.setVisible(false);
					try
					{
						inputHandler.handleNewPassword(catpos, descField.getText(), userField.getText(),
								passField.getText().toCharArray(), secret);
					} catch (Exception e1)
					{
						e1.printStackTrace();
						dialog.getDialog().close();
						Platform.runLater(() ->
						{
							dialog.setHeader("Error");
							dialog.setBody("An error ocurred trying to create a new password, try again.");
						});
						return;
					}
					passField.clear();
					Arrays.fill(secret, (char) 0);

					Platform.runLater(() ->
					{
						dialog.setHeader("Sucess");
						dialog.setBody("Password created successfully.");
						buttonYes.setVisible(false);
						buttonNo.setText("Close");
					});
				}).start();
			} catch (Exception e1)
			{
				e1.printStackTrace();
				dialog.getDialog().close();
			}
		});
		buttonNo.setOnAction(e ->
		{
			dialog.getDialog().close();
		});

		VBox vBox = new VBox(descLabel, descField, usernameLabel, userField, passLabel, passField, secretLabel,
				secretField, catLabel, catList, errorLabel);
		vBox.setSpacing(10);
		dialog.getLayout().setBody(vBox);
		dialog.getDialog().show();
	}

	// Métodos de IView.
	@Override
	public void show(List<CategoryHandlerResponse> responses)
	{
		Platform.runLater(() ->
		{
			categoriesListView.getItems().clear();
		});

		lastEntries = responses;
		lastEntries.forEach(v ->
		{
			CategoryHandlerResponse node = new CategoryHandlerResponse();
			node.setCategoryName(v.getCategoryName());
			node.setPasswords(v.getPasswords());
			node.setCatId(v.getCatId());

			Platform.runLater(() ->
			{
				categoriesListView.getItems().add(node);
			});
		});
	}

	@Override
	public char[] askForSecret(String textToShow)
	{
		return null;
	}

	@Override
	public void setInputHandler(CategoryInputHandler handler)
	{
		this.inputHandler = handler;
	}

	@Override
	public void startMainLoop()
	{
		// TODO Auto-generated method stub
	}

}