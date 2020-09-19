package category.view;

import java.util.ArrayList;
import java.util.List;

public class CategoryCellNode
{
	private String categoryName;
	private List<String> passwords;
	
	public CategoryCellNode()
	{
		categoryName = "";
		passwords = new ArrayList<String>();
	}

	public String getCategoryName()
	{
		return categoryName;
	}

	public void setCategoryName(String categoryName)
	{
		this.categoryName = categoryName;
	}

	public List<String> getPasswords()
	{
		return passwords;
	}

	public void setPasswords(List<String> passwords)
	{
		this.passwords = passwords;
	}
}
