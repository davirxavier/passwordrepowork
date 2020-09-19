package category.view;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXListCell;

import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;

public class CategoryListCell extends JFXListCell<CategoryCellNode>
{
	//nodes
	private ToolbarButton deleteButton;
	private ToolbarButton editButton;
	private ImageView arrow;
	private VBox mainVBox;
	
	//data
	private boolean expanded;
	
	public CategoryListCell()
	{
		super();
		
		expanded = false;
		init();
	}
	
	private void init()
	{
		deleteButton = new ToolbarButton(new Image("/res/img/delete_black24dp.png"));
		editButton = new ToolbarButton(new Image("/res/img/edit_black24dp.png"));
		arrow = new ImageView();
		mainVBox = new VBox();
		mainVBox.setAlignment(Pos.TOP_CENTER);
		mainVBox.setFillWidth(true);
	}
	
	@Override
	protected void updateItem(CategoryCellNode node, boolean empty)
	{
		super.updateItem(node, empty);
		
		setText(null);
		setGraphic(null);
		
		mainVBox.getChildren().clear();
		if (empty)
		{
			return;
		}
		
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
		
		setGraphic(mainVBox);
	}

	public boolean isExpanded()
	{
		return expanded;
	}
	
	public void setExpanded(boolean expand)
	{
		this.expanded = expand;
	}
}
