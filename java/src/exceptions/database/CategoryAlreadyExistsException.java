package exceptions.database;

/**
 * Exce��o lan�ada quando uma Category que j� existe � inserida.
 * @author Davi
 *
 */
public class CategoryAlreadyExistsException extends Exception
{
	private static final long serialVersionUID = -5769495421233652234L;

	public CategoryAlreadyExistsException(String name)
	{
		super(name + " already exists!");
	}
}
