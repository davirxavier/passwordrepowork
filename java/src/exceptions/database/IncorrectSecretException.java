package exceptions.database;

public class IncorrectSecretException extends Exception
{
	public IncorrectSecretException()
	{
		super("Incorrect cipher secret.");
	}
}
