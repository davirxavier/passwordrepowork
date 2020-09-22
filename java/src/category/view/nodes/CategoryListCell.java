package category.view.nodes;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXListCell;
import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXSpinner;
import com.jfoenix.controls.JFXTextField;

import category.CategoryHandlerResponse;
import category.CategoryInputHandler;
import category.view.HSpacer;
import category.view.ToolbarButton;
import exceptions.database.IncorrectSecretException;
import javafx.application.Platform;
import javafx.geometry.Bounds;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import res.img.ImagePath;

public class CategoryListCell extends JFXListCell<CategoryHandlerResponse>
{
	// nodes
	private ToolbarButton deleteButton;
	private ToolbarButton editButton;
	private ImageView arrow;
	private VBox mainVBox;
	private List<Node> passwordNodes;
	private Image arrowMore;
	private Image arrowLess;

	// data
	private CategoryHandlerResponse currentNode;
	private boolean expanded;
	private CategoryInputHandler inputHandler;

	public CategoryListCell()
	{
		super();

		expanded = false;
		init();
	}

	private void init()
	{
		arrowMore = new Image(ImagePath.EXPAND_MORE_BLACK24DP.getPath());
		arrowLess = new Image(ImagePath.EXPAND_LESS_BLACK24DP.getPath());

		deleteButton = new ToolbarButton(new Image(ImagePath.DELETE_BLACK24dp.getPath()));
		editButton = new ToolbarButton(new Image(ImagePath.EDIT_BLACK24DP.getPath()));
		arrow = new ImageView(arrowMore);
		mainVBox = new VBox();
		mainVBox.setAlignment(Pos.TOP_CENTER);
		mainVBox.setFillWidth(true);
		mainVBox.setSpacing(8);
		mainVBox.setStyle("-fx-padding: 0 0 6px 2px;");

		passwordNodes = new ArrayList<>();
		setOnMouseClicked((e) ->
		{
			for (Node node : passwordNodes)
			{
				Bounds nodeBounds = node.localToScene(node.getBoundsInLocal());
				if (nodeBounds.contains(e.getSceneX(), e.getSceneY()))
				{
					e.consume();
					return;
				}
			}

			setExpanded(!isExpanded());

			if (isExpanded())
			{
				arrow.setImage(arrowLess);
			} else
			{
				arrow.setImage(arrowMore);
			}
		});
	}

	@Override
	protected void updateItem(CategoryHandlerResponse node, boolean empty)
	{
		super.updateItem(node, empty);

		setText(null);
		setGraphic(null);

		mainVBox.getChildren().clear();
		if (empty)
		{
			return;
		}
		this.currentNode = node;

		HBox hbox1 = new HBox();
		hbox1.setAlignment(Pos.CENTER);

		Label nameLabel = new Label();
		nameLabel.setText(node.getCategoryName());
		nameLabel.setStyle("-fx-font-size: 10pt; -fx-text-fill: black; -fx-padding: 0 0 0 4px;");
		nameLabel.setAlignment(Pos.TOP_LEFT);

		hbox1.getChildren().add(nameLabel);
		hbox1.getChildren().add(new HSpacer());
		hbox1.getChildren().add(deleteButton);

		HBox hbox2 = new HBox();
		hbox2.getChildren().add(arrow);
		hbox2.getChildren().add(new HSpacer());
		hbox2.getChildren().add(editButton);

		mainVBox.getChildren().add(hbox1);
		mainVBox.getChildren().add(hbox2);

		if (isExpanded())
		{
			FlowPane flowPane = new FlowPane();
			flowPane.setHgap(6);
			flowPane.setVgap(6);

			for (int i = 0; i < node.getPasswords().size(); i++)
			{
				String string = node.getPasswords().get(i);
				String[] split = string.split(",");
				Label label = new Label(split[1]);
				Label label2 = new Label(split[2]);
				label.setStyle("-fx-text-fill: black");
				label2.setStyle("-fx-text-fill: gray; -fx-font-size: 11px;");
				label.setMinWidth(Region.USE_PREF_SIZE);
				label2.setMinWidth(Region.USE_PREF_SIZE);

				Region vspacer = new Region();
				VBox.setVgrow(vspacer, Priority.ALWAYS);

				VBox pane = new VBox(label, vspacer, label2);
				pane.setPrefWidth(100);
				pane.setPrefHeight(50);
				pane.setMinWidth(50);
				pane.setAlignment(Pos.CENTER);

				pane.setStyle("-fx-background-color: white; -fx-background-radius: 4px;"
						+ "-fx-effect: dropshadow(three-pass-box, black, 4, 0, 1, 1);" + "-fx-padding: 6px 0 6px 0;"
						+ "-fx-cursor: hand;");
				flowPane.getChildren().add(pane);

				pane.setOnMouseClicked((e) ->
				{
					viewPasswordDialog(split, node.getCatId(), Integer.parseInt(split[0]));
				});

				Platform.runLater(() ->
				{
					pane.setPrefWidth(Math.max(label.prefWidth(-1), label2.prefWidth(-1)) + 15);
					pane.toFront();
				});
				passwordNodes.add(pane);
			}

			mainVBox.getChildren().add(flowPane);
		}

		setGraphic(mainVBox);
	}

	public boolean isExpanded()
	{
		return expanded;
	}

	public void setExpanded(boolean expand)
	{
		this.expanded = expand;
		updateItem(currentNode, isEmpty());
	}

	public void setInputHandler(CategoryInputHandler inputHandler)
	{
		this.inputHandler = inputHandler;
	}

	private void viewPasswordDialog(String[] split, int catid, int passid)
	{
		StackPane mainPane = (StackPane) getScene().lookup("#passwordsPane");
		String header = "Please insert your master password:";
		JFXButton buttonYes = new JFXButton("Ok");
		JFXButton buttonNo = new JFXButton("Cancel");
		buttonYes.getStyleClass().add("button-dark");
		buttonNo.getStyleClass().addAll(buttonYes.getStyleClass());

		FlatJFXDialog dialog = new FlatJFXDialog(mainPane, header, "", buttonNo, buttonYes);

		JFXPasswordField passwordField = new JFXPasswordField();
		passwordField.setPromptText("Master password");
		passwordField.getStyleClass().add("textfield-light");

		Label errorLabel = new Label("Error");
		errorLabel.setTextFill(Color.RED);
		errorLabel.setVisible(false);
		errorLabel.setStyle("-fx-font-size: 12px;");

		VBox vBox = new VBox(passwordField, errorLabel);
		vBox.setSpacing(8);

		buttonYes.setOnAction(e ->
		{
			dialog.getLayout().getBody().clear();
			dialog.setHeader("Processing...");
			dialog.getLayout().setBody(new JFXSpinner());

			new Thread(() ->
			{
				try
				{
					char[] secret = passwordField.getText().toCharArray();
					char[] secretc = secret.clone();
					passwordField.clear();
					char[] decryptedPassword = inputHandler.handleViewPassword(catid, passid, secret);

					Platform.runLater(() ->
					{
						dialog.setHeader("Password Info");
						dialog.getDialog().setOnDialogClosed(eventClose ->
						{
							Arrays.fill(decryptedPassword, (char) 0);
						});

						Label descLabel = new Label("Description");
						JFXTextField descField = new JFXTextField();
						descField.setPromptText("Description");
						descField.setText(split[1]);

						Label userLabel = new Label("Username");
						JFXTextField userField = new JFXTextField();
						userField.setPromptText("Username");
						userField.setText(split[2]);

						Label passLabel = new Label("Decrypted Password");
						JFXTextField passField = new JFXTextField();
						passField.setPromptText("Password");
						passField.setText(new String(decryptedPassword));

						descLabel.setStyle("-fx-font-size: 12px;");
						userLabel.setStyle(descLabel.getStyle());
						passLabel.setStyle(descLabel.getStyle());

						vBox.getChildren().clear();
						vBox.getChildren().addAll(descLabel, descField, userLabel, userField, passLabel, passField);

						buttonNo.setText("Close");
						buttonYes.setText("Confirm changes");
						
						dialog.getDialog().setOnDialogClosed(closed ->
						{
							passField.clear();
							Arrays.fill(secret, (char)0);
							Arrays.fill(secretc, (char)0);
							Arrays.fill(decryptedPassword, (char)0);
						});

						buttonYes.setOnAction(eventChanges ->
						{
							dialog.getLayout().getBody().clear();
							dialog.setHeader("Processing...");
							dialog.getLayout().setBody(new JFXSpinner());

							new Thread(() ->
							{
								try
								{
									inputHandler.handleEditPassword(catid, passid, descField.getText(),
											userField.getText(), passField.getText().toCharArray(), secretc);

									Platform.runLater(() ->
									{
										dialog.setHeader("Success");
										dialog.setBody("Changes made successfully.");

										buttonYes.setVisible(false);
										buttonNo.setText("Close");
									});
								} catch (IncorrectSecretException incorrectException)
								{
									incorrectException.printStackTrace();
								} catch (Exception e1)
								{
									e1.printStackTrace();
									dialog.getDialog().close();
								}
							}).start();
						});

						dialog.getLayout().setBody(vBox);
					});
				} catch (IncorrectSecretException esecret)
				{
					esecret.printStackTrace();
					Platform.runLater(() ->
					{
						dialog.setHeader("Error");
						dialog.setBody("Incorrect master password, try again.");
						buttonYes.setVisible(false);
						buttonNo.setText("Close");
					});
				} catch (Exception e1)
				{
					e1.printStackTrace();
					dialog.getDialog().close();
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
}
