package category;

import java.util.ArrayList;
import java.util.List;

public class CategoryHandlerResponse
{
	private int catId;
	private String categoryName;
	private List<String> passwords;

	public CategoryHandlerResponse()
	{
		categoryName = "";
		setCatId(-1);
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

	public int getCatId()
	{
		return catId;
	}

	public void setCatId(int catId)
	{
		this.catId = catId;
	}

	@Override
	public boolean equals(Object obj)
	{
		if (obj instanceof CategoryHandlerResponse)
		{
			CategoryHandlerResponse response = (CategoryHandlerResponse) obj;

			return getCatId() == response.getCatId() && getCategoryName().equals(response.getCategoryName())
					&& getPasswords().equals(response.getPasswords());
		}
		return false;
	}
}
