package exceptions.database;

/**
 * Exceção lançada quando uma Category que já existe é inserida.
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
