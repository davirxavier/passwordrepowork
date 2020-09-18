package exceptions.database;

public class CategoryAlreadyExistsException extends Exception
{
	public CategoryAlreadyExistsException(String name)
	{
		super(name + " already exists!");
	}
}
