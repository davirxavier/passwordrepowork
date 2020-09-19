package category.view;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXListCell;
import com.jfoenix.controls.JFXListView;
import com.jfoenix.controls.JFXPasswordField;

import category.CategoryInputHandler;
import category.IView;
import exceptions.database.IncorrectSecretException;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.util.Callback;

/**
 * View para categorias e passwords em interface gráfica.
 * 
 * @author Davi
 */
public class CategoryViewGraphical implements IView
{
	private CategoryInputHandler inputHandler;
	private HashMap<String, List<String>> lastEntries;

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
	private JFXListView<CategoryCellNode> categoriesListView;
	
	public CategoryViewGraphical()
	{
		lastEntries = new HashMap<>();
	}

	@FXML
	void initialize()
	{
		categoriesListView.setCellFactory(new Callback<ListView<CategoryCellNode>, ListCell<CategoryCellNode>>()
		{
			@Override
			public ListCell<CategoryCellNode> call(ListView<CategoryCellNode> param)
			{
				CategoryListCell cell = new CategoryListCell();
				
				return cell;
			}
		});
	}

	@FXML
	void login(ActionEvent event)
	{
		char[] secret = loginPasswordField.getText().toCharArray();

		new Thread(() ->
		{
			try
			{
				boolean check = inputHandler.handleCheckSecret(secret);
				Arrays.fill(secret, (char)0);

				Platform.runLater(() ->
				{
					if (check)
					{
						loginLabelError.setVisible(false);
						loginPane.setVisible(false);
						loginPane.setDisable(true);
					} else
					{
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
		}).start();
	}

	@FXML
	void logout(ActionEvent event)
	{

	}

	@FXML
	void newCategory(ActionEvent event)
	{
		
	}

	@FXML
	void newPassword(ActionEvent event)
	{

	}

	// Métodos de IView.
	@Override
	public void show(HashMap<String, List<String>> c)
	{
		categoriesListView.getItems().clear();
		
		lastEntries = c;
		lastEntries.forEach((k, v) ->
		{
			CategoryCellNode node = new CategoryCellNode();
			node.setCategoryName(k);
			node.setPasswords(v);
			
			categoriesListView.getItems().add(node);
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
