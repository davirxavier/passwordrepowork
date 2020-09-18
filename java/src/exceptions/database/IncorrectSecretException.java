package exceptions.database;

/**
 * Exceção levantada quando o segredo passado para uma operação é incorreto.
 * 
 * @author Davi
 */
public class IncorrectSecretException extends Exception
{
	private static final long serialVersionUID = 8838784281923945953L;

	public IncorrectSecretException()
	{
		super("Incorrect cipher secret.");
	}
}
