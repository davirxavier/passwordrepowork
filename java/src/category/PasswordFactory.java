package category;

/**
 * Fábrica de Passwords. Pode ser expandida no futuro no caso de aumento novos
 * tipos de Password.
 */
public class PasswordFactory
{
	public Password create(String description, String username, String encryptedPassword)
	{
		return new Password(description, username, encryptedPassword);
	}
}
