package category.view;

import java.util.HashMap;
import java.util.List;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXPasswordField;

import category.CategoryInputHandler;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;

public class CategoryViewGraphical implements IView
{
	private CategoryInputHandler inputHandler;
	
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
    
    public CategoryViewGraphical()
	{
    	
	}

    @FXML
    void login(ActionEvent event) 
    {

    }

	@Override
	public void show(HashMap<String, List<String>> c)
	{
		// TODO Auto-generated method stub
		
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
